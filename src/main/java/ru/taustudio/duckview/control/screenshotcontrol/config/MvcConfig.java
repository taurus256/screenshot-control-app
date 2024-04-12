package ru.taustudio.duckview.control.screenshotcontrol.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	@Override
	public void addViewControllers(ViewControllerRegistry registry){
		registry.addViewController("/").setViewName("darkview/front-end/pages/main");
		registry.addViewController("/start").setViewName("darkview/front-end/pages/params");
		registry.addViewController("/home").setViewName("darkview/front-end/pages/main");
		registry.addViewController("/register").setViewName("darkview/front-end/pages/registration");
		registry.addViewController("/login").setViewName("darkview/front-end/pages/login");
		registry.addViewController("/profile").setViewName("darkview/front-end/pages/profile");
		registry.addViewController("/task/history").setViewName("history");
		registry.addViewController("/task").setViewName("darkview/front-end/pages/results");
		registry.addViewController("/404").setViewName("darkview/front-end/pages/404");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/styles/**").addResourceLocations("classpath:/templates/darkview/front-end/styles/");
		registry.addResourceHandler("/scripts/**").addResourceLocations("classpath:/templates/darkview/front-end/scripts/");
		registry.addResourceHandler("/src/**").addResourceLocations("classpath:/templates/darkview/front-end/src/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}
}
