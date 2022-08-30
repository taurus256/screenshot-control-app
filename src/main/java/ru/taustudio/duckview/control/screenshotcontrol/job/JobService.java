package ru.taustudio.duckview.control.screenshotcontrol.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScJob;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.BROWSER;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.OS;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.Resolution;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.TaskStatus;

import javax.imageio.stream.FileImageOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class JobService {
	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private RestTemplate restTemplate;

//	@Qualifier("eurekaClient")
//	@Autowired
//	EurekaClient eurekaClient;

	final String FILE_DIRECTORY = "/tmp/";
	final String FILE_EXTENSION = ".png";

	public void createJobs(ScTask task) {
		//for Windows
		if (task.getWinEdge()) {
			createJob(BROWSER.EDGE, OS.WINDOWS, task.getUrl(), task);
		}
		if (task.getWinFirefox()) {
			createJob(BROWSER.FIREFOX, OS.WINDOWS, task.getUrl(), task);
		}
		if (task.getWinChrome()) {
			createJob(BROWSER.CHROME, OS.WINDOWS, task.getUrl(), task);
		}
		if (task.getWinOpera()) {
			createJob(BROWSER.OPERA, OS.WINDOWS, task.getUrl(), task);
		}
		//for Linux
		if (task.getLinFirefox()) {
			createJob(BROWSER.FIREFOX, OS.LINUX, task.getUrl(), task);
		}
		if (task.getLinChrome()) {
			createJob(BROWSER.CHROME, OS.LINUX, task.getUrl(), task);
		}
		if (task.getLinOpera()) {
			createJob(BROWSER.OPERA, OS.LINUX, task.getUrl(), task);
		}
	}

	public void createJob(BROWSER browser, OS operationSystem, String url, ScTask task) {
		ScJob job = ScJob.builder()
				.uuid(UUID.randomUUID().toString())
				.createTime(Instant.now())
				.browser(browser)
				.operationSystem(operationSystem)
				.url(url)
				.task(task)
				.status(TaskStatus.CREATED)
				.build();
		jobRepository.save(job);
		sendToAgent(job.getId(), browser, operationSystem, url, task.getResolution());
	}

	private void sendToAgent(Long jobId, BROWSER browser, OS os, String targetUrl, Resolution res) {
		try {
			Boolean isProcessedNow = restTemplate.getForObject("http://AGENT-"
							+ os.name() + "-"
							+ browser.name()
							+ "/screenshot?url=" + targetUrl
							+ "&width=" + res.getWidth()
							+ "&height=" + res.getHeight()
							+ "&jobId=" + jobId
					, Boolean.class);

			if (Boolean.TRUE.equals(isProcessedNow))
				setJobStatus(jobId, TaskStatus.PROCESS);
			else
				setJobStatus(jobId, TaskStatus.PENDING);
		} catch (RestClientException ex){
			setJobStatus(jobId, TaskStatus.ERROR);
		}
	}

	public byte[] getJobImageData(Long id) throws IOException {
		File f = new File(FILE_DIRECTORY + id + FILE_EXTENSION);
		return Files.readAllBytes(f.toPath());
	}

	public void saveDataFromAgent(Long jobId, ByteArrayResource resource) throws IOException{
		String fileName = jobId.toString() + FILE_EXTENSION;
		File outputFile = new File(FILE_DIRECTORY + fileName);

		try (FileImageOutputStream fio = new FileImageOutputStream(outputFile)) {
			fio.write(resource.getByteArray());
		}
		setJobStatus(jobId, TaskStatus.SUCCESS);
	}

	private void setJobStatus(Long jobId, TaskStatus status) {
		ScJob job =jobRepository.getReferenceById(jobId);
		job.setStatus(status);
		jobRepository.save(job);
	}
}
