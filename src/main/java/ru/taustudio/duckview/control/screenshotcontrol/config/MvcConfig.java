package ru.taustudio.duckview.control.screenshotcontrol.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	@Override
	public void addViewControllers(ViewControllerRegistry registry){
		registry.addViewController("/").setViewName("start");
		registry.addViewController("/start").setViewName("start");
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/success").setViewName("success");
		registry.addViewController("/add_user").setViewName("user_edit");
		registry.addViewController("/task/history").setViewName("history");
	}
}
