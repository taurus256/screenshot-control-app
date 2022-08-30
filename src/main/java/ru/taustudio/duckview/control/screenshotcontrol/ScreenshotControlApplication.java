package ru.taustudio.duckview.control.screenshotcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ScreenshotControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreenshotControlApplication.class, args);
	}

}
