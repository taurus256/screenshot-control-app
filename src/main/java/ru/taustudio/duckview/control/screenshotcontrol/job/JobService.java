package ru.taustudio.duckview.control.screenshotcontrol.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScJob;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.RENDERER;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.OS;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.Resolution;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.UUID;
import ru.taustudio.duckview.control.screenshotcontrol.misc.FileUtilMethods;
import ru.taustudio.duckview.control.screenshotcontrol.misc.ImageProcessingService;
import ru.taustudio.duckview.shared.JobDescription;

@Service
@Slf4j
public class JobService {
	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	ImageProcessingService imageProcessingService;

	@Autowired
	private KafkaTemplate<String, JobDescription> kafkaTemplate;

	final String FILE_DIRECTORY = "/tmp/";
	final String FILE_EXTENSION = ".png";

	public void createJobs(ScTask task) {
		//for Windows
		if (task.getWinEdge()) {
			createJob(RENDERER.EDGE, OS.WINDOWS, task.getUrl(), task);
		}
		if (task.getWinFirefox()) {
			createJob(RENDERER.FIREFOX, OS.WINDOWS, task.getUrl(), task);
		}
		if (task.getWinChrome()) {
			createJob(RENDERER.CHROME, OS.WINDOWS, task.getUrl(), task);
		}
		if (task.getWinOpera()) {
			createJob(RENDERER.OPERA, OS.WINDOWS, task.getUrl(), task);
		}
		//for Linux
		if (task.getLinFirefox()) {
			createJob(RENDERER.FIREFOX, OS.LINUX, task.getUrl(), task);
		}
		if (task.getLinChrome()) {
			createJob(RENDERER.CHROME, OS.LINUX, task.getUrl(), task);
		}
		if (task.getLinOpera()) {
			createJob(RENDERER.OPERA, OS.LINUX, task.getUrl(), task);
		}
		//for iOs
		if (task.getIosIPHONE_SE()) {
			createJob(RENDERER.IPHONESE, OS.IOS, task.getUrl(), task);
		}
		if (task.getIosIPHONE_PRO()) {
			createJob(RENDERER.IPHONEPRO, OS.IOS, task.getUrl(), task);
		}
		if (task.getIosIPAD()) {
			createJob(RENDERER.IPAD, OS.IOS, task.getUrl(), task);
		}
		// for macOS
		if (task.getMacSafari()) {
			createJob(RENDERER.SAFARI, OS.MACOS, task.getUrl(), task);
		}
		if (task.getMacFirefox()){
			createJob(RENDERER.FIREFOX, OS.MACOS, task.getUrl(), task);
		}
	}

	public void createJob(RENDERER renderer, OS operationSystem, String url, ScTask task) {
		ScJob job = ScJob.builder()
				.uuid(UUID.randomUUID().toString())
				.createTime(Instant.now())
				.renderer(renderer)
				.operationSystem(operationSystem)
				.url(url)
				.task(task)
				.status(TaskStatus.CREATED)
				.build();
		jobRepository.save(job);
		sendToKafka(job.getId(), job.getUuid(), renderer, operationSystem, url, task.getResolution());
	}

	private void sendToKafka(Long jobId, String jobUUID, RENDERER renderer, OS os, String targetUrl, Resolution res){
		JobDescription job = JobDescription.builder()
				.jobId(jobId)
				.jobUUID(jobUUID)
				.url(targetUrl)
				.width(res.getWidth())
				.height(res.getHeight())
				.build();
		kafkaTemplate.send(getTopicName(os, renderer), job);
	}

	private String getTopicName(OS os, RENDERER renderer){
		return os.getShortname() + "_" + renderer.name();
	}

	public byte[] getJobImageData(String jobUUID) throws IOException {
		File f = new File(FILE_DIRECTORY + jobUUID + FILE_EXTENSION);
		return Files.readAllBytes(f.toPath());
	}

	public byte[] getJobImagePreview(String uuid) throws IOException {
		File f = new File(FILE_DIRECTORY + uuid + ".preview" + FILE_EXTENSION);
		return Files.readAllBytes(f.toPath());
	}

	public void saveDataFromAgent(String jobUUID, ByteArrayResource resource) throws IOException{
		FileUtilMethods.writeImage(jobUUID, resource.getByteArray());
		imageProcessingService.generatePreview(jobUUID, resource);
		setJobStatus(jobUUID, TaskStatus.SUCCESS);
	}

	private void setJobStatus(String jobUUID, TaskStatus status) {
		ScJob job = jobRepository.getScJobByUuid(jobUUID);
		job.setStatus(status);
		jobRepository.save(job);
	}
}
