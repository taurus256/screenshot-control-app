package ru.taustudio.duckview.control.screenshotcontrol.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/job")
public class JobController {

	@Autowired
	JobService jobService;

	/**This method called from agents*/
	@PutMapping("/{jobId}")
	public void updateJob(@PathVariable Long jobId, @RequestBody ByteArrayResource bars) throws IOException {
		jobService.saveDataFromAgent(jobId, bars);
	}

	@GetMapping(value = "/{jobId}/show", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getJobImage(@PathVariable Long jobId) throws IOException {
		return jobService.getJobImageData(jobId);
	}
}
