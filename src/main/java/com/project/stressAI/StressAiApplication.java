package com.project.stressAI;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@OpenAPIDefinition(info=@Info(title="Stress AI Application"))
public class StressAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StressAiApplication.class, args);
	}

}
