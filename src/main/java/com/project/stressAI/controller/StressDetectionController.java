package com.project.stressAI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.stressAI.service.StressDetectorService;

@RestController
public class StressDetectionController {
	
	@Autowired
	private StressDetectorService service;
	
	@GetMapping(value = "/calculateStress")
	public String getStressScore() {
		return service.getStress();
	}
	
}
