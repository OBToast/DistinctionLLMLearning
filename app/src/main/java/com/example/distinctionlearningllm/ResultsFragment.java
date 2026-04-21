package com.example.distinctionlearningllm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class ResultsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Question> questions;
    private List<Integer> answers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results, container, false);

        //Get the questions and answers from the bundle
        questions = (ArrayList<Question>) getArguments().getSerializable("questions");
        answers = getArguments().getIntegerArrayList("answers");

        //Set the recycler view with the results adpater
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(
                new ResultsAdapter(questions, answers, this)
        );

        //Bind the home button
        Button home = view.findViewById(R.id.homeButton);
        home.setOnClickListener(v ->
        {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).addToBackStack(null).commit();
        });

        return view;
    }
}