package com.project.stressAI.service;

import java.io.IOException;

public interface StressDetectorService {

	public String getStress() throws IOException, InterruptedException;
}
