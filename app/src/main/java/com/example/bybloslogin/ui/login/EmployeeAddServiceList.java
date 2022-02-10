package com.example.bybloslogin.ui.login;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bybloslogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EmployeeAddServiceList extends ArrayAdapter<Service> {

    private Activity context;
    List<Service> services;
    private String username;

    public EmployeeAddServiceList(Activity context, List<Service> services, String username) {
        super(context, R.layout.individual_service_layout, services);
        this.context = context;
        this.services = services;
        this.username = username;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.individual_service_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewRate = (TextView) listViewItem.findViewById(R.id.textViewDetail);

        Service service = services.get(position);
        textViewName.setText(service.getName());
        textViewRate.setText("$" + service.getHourlyRate() + "/hr");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference().child("users").child(username).child("servicesOffered").child((String)textViewName.getText());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listViewItem.setBackgroundColor(Color.GREEN);
                }

            };
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return listViewItem;
    }
}
