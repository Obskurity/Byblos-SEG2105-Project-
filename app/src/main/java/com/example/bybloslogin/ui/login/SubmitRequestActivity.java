package com.example.bybloslogin.ui.login;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bybloslogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class SubmitRequestActivity extends AppCompatActivity {


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_request_layout);

        Button btnSubmitRequest =  (Button)findViewById(R.id.btnSubmitRequest);

        Bundle extras = getIntent().getExtras();
        String branchName = extras.getString("branchName");
        String serviceName = extras.getString("serviceName");
        String customerName = extras.getString("customerName");

        LinearLayout fieldsList = (LinearLayout) findViewById(R.id.fields);

        DatabaseReference databaseServiceFields = FirebaseDatabase.getInstance().getReference("services").child(serviceName);
        databaseServiceFields.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {

                String fieldsString = dataSnapshot.getValue(String.class);

                String[] fields = fieldsString.split("\n");

                for (int i = 1; i < fields.length; i++) {

                    LinearLayout row = new LinearLayout(SubmitRequestActivity.this);

                    TextView fieldName = new TextView(SubmitRequestActivity.this);
                    fieldName.setText(fields[i]);
                    row.addView(fieldName);

                    LinearLayout.LayoutParams inputBoxSize = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
                    EditText fieldInput = new EditText(SubmitRequestActivity.this);
                    fieldInput.setLayoutParams(inputBoxSize);
                    row.addView(fieldInput);

                    fieldsList.addView(row, fieldsList.getChildCount()-1);

                }


//

//
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String completedForm = (customerName + "\n" + serviceName + "\n");

                for (int i = 0; i<fieldsList.getChildCount()-1; i++){



                    LinearLayout indivField = (LinearLayout) fieldsList.getChildAt(i);

                    if (((EditText)(indivField.getChildAt(1))).getText().toString().isEmpty()){
                        Toast.makeText(SubmitRequestActivity.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    completedForm += (((EditText)(indivField.getChildAt(1))).getText().toString() + "\n");

                }

                DatabaseReference branchServiceRequests =  FirebaseDatabase.getInstance().getReference("users").child(branchName).child("serviceRequests");
                String key = branchServiceRequests.push().getKey();
                branchServiceRequests.child(key).setValue(completedForm);

                Toast.makeText(SubmitRequestActivity.this, "Successfully submitted request", Toast.LENGTH_SHORT).show();

            }

        });

    }

}


