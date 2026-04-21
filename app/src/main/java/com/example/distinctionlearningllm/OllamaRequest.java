package com.example.distinctionlearningllm;

public class OllamaRequest {

    public String model;
    public String prompt;
    public boolean stream;

    public OllamaRequest(String model, String prompt) {
        this.model = model;
        this.prompt = prompt;
        this.stream = false; // non-streaming for simplicity
    }
}
