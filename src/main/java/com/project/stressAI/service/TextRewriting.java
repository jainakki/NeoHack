package com.project.stressAI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project.stressAI.configuration.Constants.NEGATIVE;

@Service
public class TextRewriting {

    @Autowired
    SentimentAnalyzer sentimentAnalyzer;
    @Autowired
    OpenAiService openAi;

    public String emailCheck(String text) throws JsonProcessingException {
        if(NEGATIVE.equals(sentimentAnalyzer.checkTone(text))){
            return openAi.textResponse("re write this email in positive tone \\n"+text);
        }
        return text;
    }

    public String recommend(List<String> text) throws JsonProcessingException {
        String prompt=" i like ";
        for(String t:text){
            prompt += t+", ";
            System.out.println(t);
        }
        prompt += ". please recommend something to release stress based on my preferences.";
        return openAi.textResponse(prompt);
    }

}
