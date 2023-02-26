package com.project.stressAI.model;


import io.swagger.v3.oas.models.security.SecurityScheme;

public class OpenAI {
    String prompt;
    String model;
    Double temperature;
    Integer max_tokens;
    Integer top_p;
    Integer frequency_penalty;
    Double presence_penalty;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(Integer max_tokens) {
        this.max_tokens = max_tokens;
    }

    public Integer getTop_p() {
        return top_p;
    }

    public void setTop_p(Integer top_p) {
        this.top_p = top_p;
    }

    public Integer getFrequency_penalty() {
        return frequency_penalty;
    }

    public void setFrequency_penalty(Integer frequency_penalty) {
        this.frequency_penalty = frequency_penalty;
    }

    public Double getPresence_penalty() {
        return presence_penalty;
    }

    public void setPresence_penalty(Double presence_penalty) {
        this.presence_penalty = presence_penalty;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }


}
