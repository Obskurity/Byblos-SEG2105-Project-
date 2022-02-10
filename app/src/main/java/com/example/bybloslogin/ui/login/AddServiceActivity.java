package com.example.bybloslogin.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bybloslogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddServiceActivity extends AppCompatActivity {

    private Button btnAddField, btnSubmit;
    private ArrayList<EditText> fields = new ArrayList<>();

    private EditText txtName, txtRate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtRate = (EditText)findViewById(R.id.txtHourlyRate);
        fields.add(txtRate);
        setContentView(R.layout.add_service_layout);

        btnAddField = (Button)findViewById(R.id.btnAddField);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add a new edittext
                EditText field = new EditText(AddServiceActivity.this);

                LinearLayout layout = (LinearLayout) findViewById(R.id.fieldLayout);
                field.setHint("Field " + (layout.getChildCount() - 3));
                layout.addView(field, layout.getChildCount() - 1);
                fields.add(field);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.fieldLayout);
                String serviceName = ((EditText) layout.getChildAt(1)).getText().toString();
                if (serviceName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Service Name Cannot be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (((EditText) layout.getChildAt(1)).getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Hourly Rate Cannot be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                double hourly;
                try {
                    hourly = Double.parseDouble(((EditText)layout.getChildAt(2)).getText().toString());
                    if (hourly < 0) {
                        Toast.makeText(getApplicationContext(), "Hourly Rate Must be a Non-Negative Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Hourly Rate Must be Numeric", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fieldString = "";
                for (int i = 2; i < layout.getChildCount() - 1; i++) {
                    if (!((EditText) layout.getChildAt(i)).getText().toString().trim().equals("")) {
                        if (i == 2) {
                            fieldString += ((EditText) layout.getChildAt(i)).getText().toString().trim().replaceAll("[+-]","") + "\n";
                        }
                        else{
                            fieldString += ((EditText) layout.getChildAt(i)).getText().toString().trim() + "\n";
                        }

                    }
                }
                final String fieldStringFinal = fieldString.trim();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("services").child(serviceName);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Administrator.addService(serviceName, fieldStringFinal);
                            Toast.makeText(getApplicationContext(), "Successfully Added Service", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "A Service with that Name already exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}
