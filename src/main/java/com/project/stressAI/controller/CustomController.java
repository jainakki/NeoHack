package com.project.stressAI.controller;

import com.project.stressAI.service.SentimentAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomController {

    @Autowired
    SentimentAnalyzer sentimentAnalyzer;

    @PostMapping(value = "/custom")
    public String custom(@RequestBody String mail) {
        return sentimentAnalyzer.checkTone(mail);
    }
    @GetMapping(value = "/test")
    public String custom() {
        return "test";
    }
}