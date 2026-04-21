package com.example.distinctionlearningllm;

import retrofit2.Call;
import retrofit2.http.*;

public interface OllamaApiService {

    @POST("api/generate")
    Call<OllamaResponse> generate(@Body OllamaRequest request);
}
