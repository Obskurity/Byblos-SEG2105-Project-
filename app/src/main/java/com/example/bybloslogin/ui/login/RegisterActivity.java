package com.example.bybloslogin.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bybloslogin.R;
import com.example.bybloslogin.databinding.ActivityLoginBinding;
import com.example.bybloslogin.databinding.ActivityRegisterBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityRegisterBinding binding;

    private String feedback = "";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final RadioGroup accountTypeRadioButton = binding.accountTypeRadioButton;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        final Button registerButton = binding.register;



        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Result<LoggedInUser> result = loginViewModel.login(usernameEditText.getText().toString(),
                //passwordEditText.getText().toString(),((RadioButton)findViewById(accountTypeRadioButton.getCheckedRadioButtonId())).getText().toString());
                boolean goodCharacter;
                String inputUsername = usernameEditText.getText().toString().trim().toLowerCase();
                String goodChars = "1234567890abcdefghijklmnopqrstuvwxyz";
                for (int i = 0; i < inputUsername.length(); i++) {
                    goodCharacter = false;
                    for (int j = 0; j < goodChars.length(); j++) {
                        if (goodChars.charAt(j) == inputUsername.charAt(i)) {
                            goodCharacter = true;
                        }
                    }
                    if (goodCharacter == false) {
                        Toast toast = Toast.makeText(RegisterActivity.this, "Invalid username", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                }
                final String username = inputUsername;


                String password = passwordEditText.getText().toString();
                String accountType = ((RadioButton) findViewById(accountTypeRadioButton.getCheckedRadioButtonId())).getText().toString();


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference().child("users").child(username);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            if (!accountType.equals("Admin")) {
                                DatabaseReference newUserAccountTypeRef = database.getReference("users/" + username + "/accountType");
                                DatabaseReference newUserPasswordRef = database.getReference("users/" + username + "/password");
                                newUserPasswordRef.setValue(password);
                                newUserAccountTypeRef.setValue(accountType);
                                // display successfully registered but not next screen
//                                if ((accountType.equals("Byblos Branch Employee"))||(accountType.equals("Employee"))){
//                                    database.getReference().child("users").child(username).child("workingHours").setValue("9:00\n17:0\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00");
//                                    DatabaseReference servicesOffered = database.getReference("users/" + username + "/serviceRequests");
//                                    String id = servicesOffered.push().getKey();
//                                    servicesOffered.child(id).setValue("customer1\nserviceA\n2002\n");
//
//                                    DatabaseReference servicesOffered2 = database.getReference("users/" + username + "/serviceRequests");
//                                    String id2 = servicesOffered2.push().getKey();
//                                    servicesOffered2.child(id2).setValue("customer2\nserviceB\n2002\n");
//                                }

                                feedback = "Account successfully registered";
                            }
//                            else {
//                                successful = false;
//                                feedback = "Cannot create Administrator account";
//                            }
                        }
                        else {
                            String aT = dataSnapshot.child("accountType").getValue(String.class);
                            //String pass = dataSnapshot.child("password").getValue(String.class);

                            feedback = "Account Already Exists";
                        }
                        Toast toast = Toast.makeText(RegisterActivity.this, feedback, Toast.LENGTH_LONG);
                        toast.show();

                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError error){

                    }
                    //successful = account registered
                });

            }

        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }







//    private void updateUiWithUser (LoggedInUserView model){
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }
//
//    private void showLoginFailed (@StringRes Integer errorString){
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }













}