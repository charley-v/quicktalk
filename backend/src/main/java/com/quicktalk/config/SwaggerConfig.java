package com.quicktalk.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.quicktalk"))
				.paths(PathSelectors.regex("/.*"))
				.build()
				.apiInfo(apiDetails());
	}

	private ApiInfo apiDetails() {
		return new ApiInfo("QuickTalk API", 
				"This comprises of backend APIs that allows user registartion, private and group chat room creation and addition of users to group chat rooms. This API also enables to fetch list of all registered users and room details of each user.", 
				"1.0", 
				null, 
				null, 
				null, 
				null, 
				Collections.emptyList());
	}

}
