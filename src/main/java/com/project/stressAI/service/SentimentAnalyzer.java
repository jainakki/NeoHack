package com.project.stressAI.service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static com.project.stressAI.configuration.Constants.*;

@Service
public class SentimentAnalyzer {

    private static final StanfordCoreNLP pipeline;

    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public static void main(String[] args) {
        String email = "Dear Manager,\n\nI am writing to express my concerns about the recent project deadlines. I understand that we are facing some challenges, but I do not believe we can overcome them if we work together. I hope we can discuss this further in our next meeting.\n\nBest regards,\nJohn";
        String tone = checkTone(email);
        System.out.println("The tone of the email is " + tone);
    }

    public static int analyzeSentiment(String text) {
        Annotation annotation = pipeline.process(text);
        int mainSentiment = 0;
        int longest = 0;
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            int sentiment = RNNCoreAnnotations.getPredictedClass(sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class));
            String partText = sentence.toString();
            if (partText.length() > longest) {
                mainSentiment = sentiment;
                longest = partText.length();
            }
        }
        return mainSentiment - 2;
    }

    public static String checkTone(String email) {
        Annotation annotation = pipeline.process(email);
        String mainSentiment = "";
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            if (sentiment.equals(POSITIVE) || sentiment.equals(NEGATIVE)) {
                mainSentiment = sentiment;
                break;
            } else if (sentiment.equals(NEUTRAL) && mainSentiment.isEmpty()) {
                mainSentiment = sentiment;
            }
        }
        return mainSentiment;
    }
}
