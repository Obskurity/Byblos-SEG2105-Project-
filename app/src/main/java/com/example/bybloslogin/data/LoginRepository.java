package com.example.bybloslogin.data;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bybloslogin.data.model.LoggedInUser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private boolean successful;
    private String feedback;

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password, String accountType) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) { // REGISTER IF NOT ADMIN
                    if (!accountType.equals("Administrator")) {
                        DatabaseReference newUserAccountTypeRef = database.getReference("users/" + username + "/accountType");
                        DatabaseReference newUserPasswordRef = database.getReference("users/" + username + "/password");
                        newUserPasswordRef.setValue(password);
                        newUserAccountTypeRef.setValue(accountType);
                        // display successfully registered but not next screen
                        successful = false;
                        feedback = "Account successfully registered";
                    }
                    else {
                        successful = false;
                        feedback = "Cannot create Administrator account";
                    }
                }
                else { // ATTEMPT TO LOGIN
                    if (password.equals(database.getReference().child("users").child("username").child("password").get().toString())
                            && accountType.equals(database.getReference().child("users").child("username").child("account_type").get().toString())) {
                        successful = true;
                        feedback = "Successfully logged in";
                    }
                    else {
                        successful = false;
                        feedback = "Incorrect password or account type";
                    }
                }
                //Toast toast = Toast.makeText(lActivity, feedback, Toast.LENGTH_SHORT);
                //toast.show();
            }


        });


        /*FirebaseDatabase database = FirebaseDatabase.getInstance();

        // handle whether to register OR login
//        if (database.getReference("users/").getKey())//user exists{
//            // then find if the password and accounttype matches
//        }

//        else { // register
            DatabaseReference newUserAccountTypeRef = database.getReference("users/" + username + "/accountType");
            DatabaseReference newUserPasswordRef = database.getReference("users/" + username + "/password");
            newUserPasswordRef.setValue(password);
            newUserAccountTypeRef.setValue(accountType);
//        }

*/
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }

        return result;
        //return new Result(successful,feedback);
    }
}