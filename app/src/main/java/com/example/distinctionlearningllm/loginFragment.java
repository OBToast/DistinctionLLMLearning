package com.example.distinctionlearningllm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

public class loginFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Get the database
        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "account-database").allowMainThreadQueries().build();

        //Get the UI fields
        EditText usernameEditText = view.findViewById(R.id.usernameEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        Button loginButton = view.findViewById(R.id.loginButton);

        //Set the login buttons onclick listener
        loginButton.setOnClickListener(v -> {
            //Get the username and password
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            //Check the database with the username and password, also dont allow empty passwords or usernames
            if (db.accountDao().attemptLogin(username, password) && !username.isEmpty() && !password.isEmpty()) {
                //Start a 'session' with this user by saving their username into the shared prefferences
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("current_user", username).apply();
                //Transition to the home fragment, with add to back stack so we can pop back here when they log out
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).addToBackStack(null).commit();
            }
        });

        //Bind the sign up button to transition to the sign up fragment
        Button signUpButton = view.findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new signUpFragment()).addToBackStack(null).commit();
        });
        return view;
    }
}