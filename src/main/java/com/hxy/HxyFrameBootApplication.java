package com.hxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class HxyFrameBootApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HxyFrameBootApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HxyFrameBootApplication.class);
	}
}
