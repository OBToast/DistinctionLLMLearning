package com.example.distinctionlearningllm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

public class signUpFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the view
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        //Get the database
        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "account-database").allowMainThreadQueries().build();

        //Get the UI fields
        EditText nameEditText = view.findViewById(R.id.nameEditText);
        EditText usernameEditText = view.findViewById(R.id.usernameEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        Button createAccountButton = view.findViewById(R.id.createAccountButton);

        //Set the create account button function
        createAccountButton.setOnClickListener(v -> {
            //Get the values from all of the fields
            String name = nameEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            //Make sure none of the fields are empty
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty())
            {
                Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
            //Make sure the username isnt already taken
            else if (db.accountDao().doesUsernameExist(username))
            {
                Toast.makeText(requireContext(), "Username taken. Try again", Toast.LENGTH_SHORT).show();

            }
            //Make sure the passwords match
            else if (!password.equals(confirmPassword))
            {
                Toast.makeText(requireContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            }
            //If all checks are passed, create a bundle with the details so far
            else {

                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("password", password);
                bundle.putString("name", name);
                bundle.putString("email", email);

                //Go to the interests fragment
                InterestsFragment fragment = new InterestsFragment();
                fragment.setArguments(bundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
        return view;
    }
}