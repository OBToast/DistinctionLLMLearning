package com.example.distinctionlearningllm;

public class OllamaResponse {

    public String model;
    public String response;
    public boolean done;

    /**
     * Returns the generated text from the Ollama response.
     */
    public String getResponseText() {
        return (response != null && !response.isEmpty())
                ? response
                : "No response received.";
    }
}