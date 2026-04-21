package com.example.distinctionlearningllm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
//import com.example.distinctionlearningllm.network.*;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;


public class GenerateContentFragment extends Fragment {

    // 10.0.2.2 is the Android emulator alias for the host machine's localhost
    private static final String BASE_URL = "http://10.0.2.2:11434/";
    //private static final String BASE_URL = "http://192.168.4.67:11434/";
    //    private static final String MODEL = "llama2";
    private static final String MODEL = "llama3.1:8b";

    private EditText etPrompt;
    private TextView tvPromptLabel, tvResponse;
    private Button btnSend;
    private ProgressBar progressBar;
    private OllamaApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_hint, container, false);

        etPrompt      = view.findViewById(R.id.et_prompt);
        tvPromptLabel = view.findViewById(R.id.tv_prompt_label);
        tvResponse    = view.findViewById(R.id.tv_response);
        btnSend       = view.findViewById(R.id.btn_send);
        progressBar   = view.findViewById(R.id.progress_bar);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)   // Ollama can be slow on first run
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(OllamaApiService.class);

        //Get the current working question, the answer given, and what mode we are in
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        String question = sharedPreferences.getString("current_question", "Question not found!");
        String answer = sharedPreferences.getString("answer_given", "Answer not given!");
        String mode = sharedPreferences.getString("mode","question_hint");
        //Depending on the mode, formulate the prompt
        if (mode.equals("question_hint")) {
            etPrompt.setText("Generate a hint for this question: " + question);
        } else if (mode.equals("explain_answer")) {
            etPrompt.setText("Generate a explanation for why this answer is right/wrong. Question: '" + question + "', Answer: '" + answer + "'");
        }

        btnSend.setOnClickListener(v -> sendToOllama());

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void sendToOllama() {
        String prompt = etPrompt.getText().toString().trim();

        if (prompt.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a prompt", Toast.LENGTH_SHORT).show();
            return;
        }

        tvPromptLabel.setText("Prompt sent:\n" + prompt);
        tvResponse.setText("Waiting for Ollama (" + MODEL + ")...");
        progressBar.setVisibility(View.VISIBLE);
        btnSend.setEnabled(false);

        OllamaRequest request = new OllamaRequest(MODEL, prompt);

        apiService.generate(request).enqueue(new Callback<OllamaResponse>() {
            @Override
            public void onResponse(@NonNull Call<OllamaResponse> call,
                                   @NonNull Response<OllamaResponse> response) {

                progressBar.setVisibility(View.GONE);
                btnSend.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    tvResponse.setText("\uD83E\uDD99 Ollama (" + MODEL + ") says:\n\n"
                            + response.body().getResponseText());
                } else {
                    String errorMsg = "Error " + response.code() + ": " + response.message();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += "\n" + response.errorBody().string();
                        }
                    } catch (Exception e) { /* ignore */ }
                    tvResponse.setText(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<OllamaResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSend.setEnabled(true);

                String message = t.getMessage() != null ? t.getMessage() : "Unknown error";

                if (message.contains("Failed to connect") || message.contains("Connection refused")) {
                    tvResponse.setText("\u274C Could not connect to Ollama.\n\n"
                            + "Make sure Ollama is running on your computer:\n"
                            + "  1. Install Ollama: https://ollama.com\n"
                            + "  2. Run: ollama run " + MODEL + "\n"
                            + "  3. Ollama should be listening on port 11434\n\n"
                            + "Error: " + message);
                } else {
                    tvResponse.setText("Network error:\n" + message);
                }
            }
        });
    }
}
