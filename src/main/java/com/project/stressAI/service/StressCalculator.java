package com.project.stressAI.service;

public class StressCalculator {
    public static void main(String[] args) {
        double systolicBP = 120.0;
        double diastolicBP = 80.0;
        double RMSSD = 60.0;
        double meanNN = 1000.0;

        double bpStressScore = (systolicBP + 2 * diastolicBP) / 3;
        double hrvStressScore = (RMSSD / meanNN) * 1000;
        double overallStressScore = (bpStressScore + hrvStressScore) / 2;

        System.out.println("BP Stress Score: " + bpStressScore);
        System.out.println("HRV Stress Score: " + hrvStressScore);
        System.out.println("Overall Stress Score: " + overallStressScore);
    }
}