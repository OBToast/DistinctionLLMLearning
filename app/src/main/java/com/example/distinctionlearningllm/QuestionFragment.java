package com.example.distinctionlearningllm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class QuestionFragment extends Fragment {

    //Create fields for a list of questions and answer indicies
    private List<Question> questions = new ArrayList<>();
    private List<Integer> submittedAnswersIndices = new ArrayList<>();

    //Index for current question
    private int index = 0;

    //UI fields
    private TextView questionNumber;
    private TextView questionBody;
    private RadioGroup radioGroup;
    private RadioButton radioButton0, radioButton1, radioButton2, radioButton3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create example questions

        questions.add(new Question(
                "What is 1+1",
                "0", "1", "2", "3",
                2
        ));

        questions.add(new Question(
                "What is 1+2",
                "0", "1", "2", "3",
                3
        ));

        questions.add(new Question(
                "What is 1+0",
                "0", "1", "2", "3",
                1
        ));

        //initialise answers list with no answer selected
        for (int i = 0; i < questions.size(); i++) {
            submittedAnswersIndices.add(-1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the view
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        //Get the UI fields
        questionNumber = view.findViewById(R.id.QuestionNumberTextView);
        questionBody = view.findViewById(R.id.QuestionBodyTextView);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton0 = view.findViewById(R.id.radioButton0);
        radioButton1 = view.findViewById(R.id.radioButton1);
        radioButton2 = view.findViewById(R.id.radioButton2);
        radioButton3 = view.findViewById(R.id.radioButton3);

        //Bind functions to buttons
        Button nextButton = view.findViewById(R.id.NextButton);
        nextButton.setOnClickListener(v -> nextQuestion(nextButton));
        view.findViewById(R.id.PreviousButton).setOnClickListener(v -> previousQuestion(nextButton));
        view.findViewById(R.id.GenerateHintButton).setOnClickListener(v -> generateHint());

        //Update text fields
        renderQuestion();

        return view;
    }

    private void renderQuestion() {
        //Get the question
        Question question = questions.get(index);

        //Set the fields
        questionNumber.setText("Question " + (index + 1));
        questionBody.setText(question.body);
        radioButton0.setText(question.answerZero);
        radioButton1.setText(question.answerOne);
        radioButton2.setText(question.answerTwo);
        radioButton3.setText(question.answerThree);

        //Clear all selected options
        radioGroup.clearCheck();

        //Check to see if we have a saved answer, if so set the proper button to be checked already
        int saved = submittedAnswersIndices.get(index);
        if (saved != -1) {
            switch (saved) {
                case 0: radioButton0.setChecked(true); break;
                case 1: radioButton1.setChecked(true); break;
                case 2: radioButton2.setChecked(true); break;
                case 3: radioButton3.setChecked(true); break;
            }
        }
    }

    private void saveAnswer() {
        //Get the selected button
        int selected = radioGroup.getCheckedRadioButtonId();

        //Set the answer index
        int answerIndex = -1;
        if (selected == R.id.radioButton0) answerIndex = 0;
        else if (selected == R.id.radioButton1) answerIndex = 1;
        else if (selected == R.id.radioButton2) answerIndex = 2;
        else if (selected == R.id.radioButton3) answerIndex = 3;

        //Save the answer at that index, with this new value
        submittedAnswersIndices.set(index, answerIndex);
    }

    private void generateHint(){
        //Transfer to the generate content fragment with relevant data saved so we can formulate a prompt
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("current_question", questions.get(index).body).apply();
        sharedPreferences.edit().putString("mode","question_hint");
        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new GenerateContentFragment()).addToBackStack(null).commit();
    }

    private void nextQuestion(Button nextButton) {
        //Save the answer
        saveAnswer();

        //Check to see we're not at the end of the questions, if not then proceed
        if (index < questions.size() - 1) {
            index++;
            //If its the last question, change the text of the next button to say submit
            if (index == questions.size()-1)
            {
                nextButton.setText("Submit quiz");
            }
            //Update all of the text fields
            renderQuestion();
        } else {
            //If they are submitting, save the questions and answers into a bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable("questions", new ArrayList<>(questions));
            bundle.putIntegerArrayList("answers", new ArrayList<>(submittedAnswersIndices));

            //Go to the results fragment with this bundle
            ResultsFragment fragment = new ResultsFragment();
            fragment.setArguments(bundle);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .commit();
        }
    }

    private void previousQuestion(Button nextButton) {
        //Save the answer
        saveAnswer();

        //Reduce the index
        if (index > 0) {
            index--;
            renderQuestion();
        }
        nextButton.setText("Next question");

    }
}