package com.project.stressAI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.stressAI.service.OpenAiService;
import com.project.stressAI.service.SentimentAnalyzer;
import com.project.stressAI.service.TextRewriting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomController {

    @Autowired
    SentimentAnalyzer sentimentAnalyzer;
    @Autowired
    OpenAiService openAi;
    @Autowired
    TextRewriting textRewriting;

    @PostMapping(value = "/emailTone")
    public String emailTone(@RequestBody String mail) {
        return sentimentAnalyzer.checkTone(mail);
    }

    @GetMapping(value = "/openAI")
    public String openAI() throws JsonProcessingException {
        String mail = "re write this email in positive tone \\nDear Manager,\\n\\nI am writing to express my concerns about the recent project deadlines. I understand that we are facing some challenges, but I do not believe we can overcome them if we work together. I hope we can discuss this further in our next meeting.\\n\\nBest regards,\\nJohn";
        return openAi.textResponse(mail);
    }

    @PostMapping(value = "/emailCheck")
    public String emailCheck(@RequestBody String mail) throws JsonProcessingException {
        return textRewriting.emailCheck(mail);
    }
}