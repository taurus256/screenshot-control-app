package ru.taustudio.duckview.control.screenshotcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ScreenshotControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreenshotControlApplication.class, args);
	}

}
