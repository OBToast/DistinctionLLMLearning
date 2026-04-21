package com.example.distinctionlearningllm;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class InterestsFragment extends Fragment {

    private String username;
    private String password;
    private String name;
    private String email;
    private final List<String> interests = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Get the saved values from signup
        Bundle args = getArguments();
        if (args != null) {
            username = args.getString("username");
            password = args.getString("password");
            name = args.getString("name");
            email = args.getString("email");
        }

        //Inflate the view
        View view = inflater.inflate(R.layout.fragment_interests, container, false);

        //Get all of the toggle buttons
        ToggleButton mathsButton = view.findViewById(R.id.mathsButton);
        ToggleButton scienceButton = view.findViewById(R.id.scienceButton);
        ToggleButton triviaButton = view.findViewById(R.id.trivaButton);
        ToggleButton geographyButton = view.findViewById(R.id.geographyButton);
        ToggleButton geologyButton = view.findViewById(R.id.geologyButton);

        //Bind the buttons to onclick listeners
        setupToggle(mathsButton, "Maths");
        setupToggle(scienceButton, "Science");
        setupToggle(triviaButton, "Trivia");
        setupToggle(geographyButton, "Geography");
        setupToggle(geologyButton, "Geology");

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v ->
                getParentFragmentManager().popBackStack()
        );

        Button confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {

            //Concat interests into a string for ease
            String interestsString = String.join(",", interests);

            AppDatabase db = Room.databaseBuilder(
                    requireContext(),
                    AppDatabase.class,
                    "account-database"
            ).allowMainThreadQueries().build();

            //Create the account
            db.accountDao().newAccount(
                    new UserAccount(username, password, interestsString, email)
            );

            //Go to login fragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new loginFragment())
                    .commit();
        });

        return view;
    }

    private void setupToggle(ToggleButton button, String label) {

        //Set the toggle behaviour of the buttons so they change colour and add their interests to the list
        button.setOnClickListener(v -> {

            if (button.isChecked()) {
                if (!interests.contains(label)) {
                    interests.add(label);
                }
                button.setBackgroundColor(Color.BLUE);
                button.setTextColor(Color.WHITE);
            } else {
                interests.remove(label);
                button.setBackgroundColor(Color.BLACK);
                button.setTextColor(Color.WHITE);
            }
        });
    }
}