package com.example.distinctionlearningllm;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    //Create fields to store the questions and answers
    private List<Question> questions;
    private List<Integer> answers;
    //We need the fragment so we can update it from within the explain answer buttons
    private Fragment fragment;
    public ResultsAdapter(List<Question> questions, List<Integer> answers, Fragment fragment) {
        this.questions = questions;
        this.answers = answers;
        this.fragment = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Get the UI elements
        TextView question, userAnswer, correctAnswer;
        Button explainButton;
        public ViewHolder(View view) {
            super(view);
            question = view.findViewById(R.id.questionText);
            userAnswer = view.findViewById(R.id.userAnswerText);
            correctAnswer = view.findViewById(R.id.correctAnswerText);
            explainButton = view.findViewById(R.id.explainButton);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Get the relevant question
        Question question = questions.get(position);
        //Get the user provided answer
        int userIndex = answers.get(position);
        //Get the relevant string versions
        String userAnswer = getAnswer(question, userIndex);
        String correctAnswer = getAnswer(question, question.answerIndex);

        //Set the holder UI to display the text from the question
        holder.question.setText(question.body);
        holder.userAnswer.setText("Your answer: " + userAnswer);
        holder.correctAnswer.setText("Correct: " + correctAnswer);

        //Bind the explain button to move fragments, and to store relevant data in shared prefs
        holder.explainButton.setOnClickListener(v -> {

            SharedPreferences sharedPreferences =
                    fragment.requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);

            sharedPreferences.edit().putString("answer_given", userAnswer).apply();
            sharedPreferences.edit().putString("current_question", question.body).apply();
            sharedPreferences.edit().putString("mode","explain_answer").apply();

            fragment.getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new GenerateContentFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    //Convert the index of answer to textual answer assosciated
    private String getAnswer(Question q, int index) {
        switch (index) {
            case 0: return q.answerZero;
            case 1: return q.answerOne;
            case 2: return q.answerTwo;
            case 3: return q.answerThree;
            default: return "Not answered";
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}