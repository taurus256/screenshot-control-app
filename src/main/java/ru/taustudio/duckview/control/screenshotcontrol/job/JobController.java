package ru.taustudio.duckview.control.screenshotcontrol.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/job")
public class JobController {

	@Autowired
	JobService jobService;

	/**This method called from agents*/
	@PutMapping("/{jobUUID}")
	public void updateJob(@PathVariable String jobUUID, @RequestBody ByteArrayResource bars) throws IOException {
		jobService.saveDataFromAgent(jobUUID, bars);
	}

	@GetMapping(value = "/{jobUUID}/show", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getJobImage(@PathVariable String jobUUID) throws IOException {
		return jobService.getJobImageData(jobUUID);
	}

	@GetMapping(value = "/{jobUUID}/preview", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getJobImagePreview(@PathVariable String jobUUID) throws IOException {
		return jobService.getJobImagePreview(jobUUID);
	}

	@GetMapping(value = "/{jobUUID}/diff/preview", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getJobImageDiffPreview(@PathVariable String jobUUID) throws IOException {
		return jobService.getJobImageDiffPreview(jobUUID);
	}

	@GetMapping(value = "/{jobUUID}/diff/show", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getJobImageDiff(@PathVariable String jobUUID) throws IOException {
		return jobService.getJobImageDiff(jobUUID);
	}
}
