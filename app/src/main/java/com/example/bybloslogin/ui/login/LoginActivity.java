package com.example.bybloslogin.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.bybloslogin.R;
import com.example.bybloslogin.data.Result;
import com.example.bybloslogin.data.model.LoggedInUser;
import com.example.bybloslogin.ui.login.LoginViewModelFactory;
import com.example.bybloslogin.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    private boolean successful;
    private String feedback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;

     

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
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
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
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


        loginButton.setOnClickListener(new View.OnClickListener() {
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
                        Toast toast = Toast.makeText(LoginActivity.this, "Invalid username", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                }
                final String username = inputUsername;


                String password = passwordEditText.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference().child("users").child(username);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String accountType = "";
                        if (!dataSnapshot.exists()) {
                                successful = false;
                                feedback = "Account does not exist";
                        }
                        else {
                            //String aT = dataSnapshot.child("accountType").getValue(String.class);
                            String pass = dataSnapshot.child("password").getValue(String.class);
                            //String accountType;
                            if (password.equals(pass)){
                                successful = true;
                                feedback = "Successfully logged in";
                                accountType = dataSnapshot.child("accountType").getValue(String.class);
                            }
                            else {
                                successful = false;
                                feedback = "Incorrect password";
                            }
                        }
                        if (successful) {
                            Intent intent = new Intent(LoginActivity.this, WelcomeUserScreen.class);
                            intent.putExtra("username", usernameEditText.getText().toString());
                            intent.putExtra("password", passwordEditText.getText().toString());
                            intent.putExtra("account_type", accountType);
                            // get Account type from database
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast toast = Toast.makeText(LoginActivity.this, feedback, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }



}