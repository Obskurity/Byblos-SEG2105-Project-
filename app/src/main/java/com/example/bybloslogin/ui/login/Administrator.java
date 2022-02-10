package com.example.bybloslogin.ui.login;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.bybloslogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Administrator extends User {
    public Administrator(){
        super("admin", "administrator", "Admin");
    }

    public static void deleteUser(String username) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(username);
        dR.removeValue();
    }

    public static void addService(String serviceName, String newField){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(serviceName);
        dR.setValue(newField);
    }

    public static void editService(Service service, String newField){
        String serviceName = service.getName();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(serviceName);
        dR.setValue(newField);
    }

    public static boolean deleteService(DataSnapshot backup, String serviceName) {
        boolean successful = false;
        if (backup.getChildrenCount() > 3) {
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(serviceName);
            dR.removeValue();
            successful = true;
        }
        return successful;
    }


}
