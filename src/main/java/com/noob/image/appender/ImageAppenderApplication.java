package com.noob.image.appender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ImageAppenderApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ImageAppenderApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ImageAppenderApplication.class);
	}
}
