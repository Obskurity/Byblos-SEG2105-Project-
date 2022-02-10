package com.example.bybloslogin.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bybloslogin.R;

public class WelcomeUserScreen extends AppCompatActivity {
    private User loggedInUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user_screen);

        Bundle extras = getIntent().getExtras();

        String username = extras.getString("username");
        String password = extras.getString("password");
        String accountType = extras.getString("account_type");

        if (accountType.equals("Admin")) {
            loggedInUser = new Administrator();

        }
        else if (accountType.equals("Byblos Branch Employee")) {
            loggedInUser = new Employee(username, password);
        }
        else {
            loggedInUser = new Customer(username, password);
        }

        TextView welcomeMessage = findViewById(R.id.txtWelcome);
        welcomeMessage.setText("Welcome " + loggedInUser.getUsername() + "! You are logged in as " + loggedInUser.getAccountType());
        final Button btnAction1 = (Button) findViewById(R.id.btnAction1);
        final Button btnAction2 = (Button) findViewById(R.id.btnAction2);



        switch (loggedInUser.getAccountType()) {
            case "Admin":
                btnAction1.setText("Delete Users");
                btnAction2.setText("Manage Services");

                btnAction1.setVisibility(View.VISIBLE);
                btnAction2.setVisibility(View.VISIBLE);
                break;
            case "Employee":
                btnAction1.setText("Complete Profile Information");
                btnAction2.setText("Manage Services and Working Hours");

                btnAction1.setVisibility(View.VISIBLE);
                btnAction2.setVisibility(View.VISIBLE);
                break;
            case "Customer":
                btnAction1.setText("SEARCH BRANCHES");
                btnAction2.setText("");

                btnAction1.setVisibility(View.VISIBLE);
                btnAction2.setVisibility(View.GONE);
                break;
            default:
                btnAction1.setText("How are you even here??");
                btnAction2.setText("What O_O");
        }
        btnAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggedInUser.getAccountType().equals("Admin")) {
                    Intent editUsers = new Intent(WelcomeUserScreen.this, DeleteUserActivity.class);
                    startActivity(editUsers);
                } else if  (loggedInUser.getAccountType().equals("Employee")){
                    Intent completeProfile = new Intent(WelcomeUserScreen.this, CompleteProfileActivity.class);
                    completeProfile.putExtra("username",loggedInUser.getUsername());
                    startActivity(completeProfile);
                } else if  (loggedInUser.getAccountType().equals("Customer")) {
                    Intent searchBranches = new Intent(WelcomeUserScreen.this, SearchBranchesActivity.class);
                    searchBranches.putExtra("username", loggedInUser.getUsername());
                    startActivity(searchBranches);
                }
            }
        });

        btnAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggedInUser.getAccountType().equals("Admin")) {
                    Intent manageServices = new Intent(WelcomeUserScreen.this, ManageServicesActivity.class);
                    startActivity(manageServices);
                } else if  (loggedInUser.getAccountType().equals("Employee")) {
                    Intent manageServices = new Intent(WelcomeUserScreen.this, EmployeeMenu.class);
                    manageServices.putExtra("username",loggedInUser.getUsername());
                    startActivity(manageServices);
                }
            }
        });




    }





}