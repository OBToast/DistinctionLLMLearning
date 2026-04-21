package com.example.distinctionlearningllm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;


public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Get the database
        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "account-database").allowMainThreadQueries().build();

        //Get the shared preferences for this session
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        TextView welcomeText = view.findViewById(R.id.welcomeTextView);
        welcomeText.setText("Welcome, " + sharedPreferences.getString("current_user", "user") + "!");

        Button logOutButton = view.findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(v -> {

            sharedPreferences.edit().remove("current_user").apply();
            sharedPreferences.edit().remove("current_video").apply();
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new loginFragment()).commit();

        });

        //Bind the play button to transition to the video player fragment
        Button playButton = view.findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {
            //Transition to the play screen, but first save the URL into the preferences so we can retrieve it when there
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new QuestionFragment()).addToBackStack(null).commit();
        });
        return view;
    }
}