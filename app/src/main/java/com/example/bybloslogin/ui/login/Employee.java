package com.example.bybloslogin.ui.login;

import android.widget.EditText;
import android.widget.Toast;

import com.example.bybloslogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Employee extends User {
    public Employee(String username, String password) {
        super(username, password, "Employee");
    }

    public static void approveService(ServiceRequest serviceRequest, String username){
        String key = serviceRequest.getKey();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(username).child("approvedRequests").child(key);
        dR.setValue(serviceRequest.getFieldsString());
        DatabaseReference dR2 = FirebaseDatabase.getInstance().getReference("users").child(username).child("serviceRequests").child(key);
        dR2.removeValue();
    }
    public static void rejectService(ServiceRequest serviceRequest, String username){
        String key = serviceRequest.getKey();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(username).child("rejectedRequests").child(key);
        dR.setValue(serviceRequest.getFieldsString());
        DatabaseReference dR2 = FirebaseDatabase.getInstance().getReference("users").child(username).child("serviceRequests").child(key);
        dR2.removeValue();
    }

    public static void removeService(DatabaseReference dbRef, Service service, String username) {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(service.getName())) {
                    DatabaseReference dR = dbRef.child(service.getName());
                    dR.removeValue();

                } else {

                }

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(username).child("servicesOffered").child(service.getName());

                dR.removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}