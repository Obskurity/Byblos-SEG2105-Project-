package com.example.bybloslogin.ui.login;

import android.text.format.DateFormat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Branch {

    String name;
    String workingHours;
    ArrayList<String> workingDays = new ArrayList<>();
    ArrayList<String> branchServices = new ArrayList<>();
    String address;

    public Branch(String name, String address, String workingHours, ArrayList<String> branchServices){
        this.name = name;
        this.address = address;
        this.workingHours = workingHours;
        this.branchServices = branchServices;

        String[] workingDaysTmp;
        String[] days = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        if (!(workingHours == null) && !workingHours.isEmpty()) {
            workingDaysTmp = workingHours.split("\n");
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i<workingDaysTmp.length; i+=2) {
                String[] start = workingDaysTmp[i].split(":");
                String[] end = workingDaysTmp[i+1].split(":");
                calendar.set(0,0,0,Integer.parseInt(start[0]),Integer.parseInt(start[1]));

                String startString = DateFormat.format("hh:mm aa", calendar).toString();

                calendar.set(0,0,0,Integer.parseInt(end[0]),Integer.parseInt(end[1]));
                String endString = DateFormat.format("hh:mm aa", calendar).toString();
                workingDays.add(days[i/2] + ": " + startString + " - " + endString);
            }
        }


//            DatabaseReference branchServicesDB = FirebaseDatabase.getInstance().getReference("users").child(this.getName()).child("servicesOffered");
//
//            branchServicesDB.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
//
//                        branchServices.add(singleSnapshot.getKey());
//                    }
//
//
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError){
//
//                }
//            });
//
//        DatabaseReference branchAddressDB = FirebaseDatabase.getInstance().getReference("users").child(this.getName()).child("address");
//
//        branchAddressDB.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                    address = dataSnapshot.getValue(String.class);
//
//
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError){
//
//            }
//        });

        }



    public String getName(){
        return this.name;
    }

    public String getAddress(){
        return this.address;
    }

    public ArrayList<String> getServices(){
        return this.branchServices;
    }
}
