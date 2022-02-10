package com.example.bybloslogin.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bybloslogin.R;
import com.example.bybloslogin.ui.login.Branch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class RateBranchActivity extends AppCompatActivity {
    private DataSnapshot backup;
    public ArrayList<Branch> branchArrayList;
    ListView listViewBranches;
    DatabaseReference databaseUsers;
    private boolean editing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_branch_layout);

        Bundle extras = getIntent().getExtras();
        String branchName = extras.getString("branchName");

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        EditText commentBox  = (EditText) findViewById(R.id.commentBox);
        Button submitButton = (Button) findViewById(R.id.btnSubmitReview);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                String comment = commentBox.getText().toString();
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(branchName).child("Reviews");
                String id = dR.push().getKey();
                dR.child(id).child("Rating").setValue(rating);
                dR.child(id).child("Comment").setValue(comment);
                Toast.makeText(getApplicationContext(), "Successfully Rated Branch", Toast.LENGTH_SHORT).show();
                finish();

            }
        });




    }

}

