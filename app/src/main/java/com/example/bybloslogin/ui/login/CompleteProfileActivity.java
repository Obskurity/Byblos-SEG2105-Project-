package com.example.bybloslogin.ui.login;

import android.os.Bundle;
import android.view.View;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompleteProfileActivity extends AppCompatActivity {

    private Button btnSubmitProfile;

    public static final Pattern VALID_POSTALCODE_PATTERN =
            Pattern.compile("^[A-Za-z0-9]{3} {0,1}[A-Za-z0-9]{3}$",Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_ZIPCODE_PATTERN =
            Pattern.compile("^[0-9]+$",Pattern.CASE_INSENSITIVE);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] fieldNames = {"streetNumber","unitNumber","streetName","city","province","postalCode","country","phoneNumber"};

        setContentView(R.layout.employee_profile_layout);

        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");

        btnSubmitProfile = (Button)findViewById(R.id.btnSubmitProfile);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(username);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                //DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(serviceName);
                LinearLayout layout = (LinearLayout) findViewById(R.id.profileFieldsLayout);
                String addressString = "";
                for (int i = 0; i < layout.getChildCount() - 1; i++) {
                    if (dataSnapshot.hasChild(fieldNames[i]))
                        ((EditText) (layout.getChildAt(i))).setText(dataSnapshot.child(fieldNames[i]).getValue().toString());
                }



            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });



        btnSubmitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.profileFieldsLayout);
                String addressString = "";
//                for (int i = 0; i < layout.getChildCount() - 2; i++) {
//                    if (!((EditText) layout.getChildAt(i)).getText().toString().trim().equals("")) {
//                        if (i == 0) {
//                            addressString += ((EditText) layout.getChildAt(i)).getText().toString().trim().replaceAll("[+-]","") + "\n";
//                        }
//                        else{
//                            addressString += ((EditText) layout.getChildAt(i)).getText().toString().trim() + "\n";
//                        }
//
//                    }
//                }

                //final String addressStringFinal = addressString.trim();

                boolean emptyField = false;
                boolean invalidStreetNumber = false;
                boolean invalidUnitNumber = false;
                boolean invalidPostal = false;
                boolean invalidPhoneNumber = false;
                boolean invalidInput = false;

                String fieldInput;

                for (int i = 0; i<layout.getChildCount()-1; i++){
                    fieldInput = ((EditText)layout.getChildAt(i)).getText().toString();
                    if ((i != 1) && (fieldInput.isEmpty())){
                        Toast.makeText(getApplicationContext(), "Please enter a " + ((EditText)layout.getChildAt(i)).getHint(), Toast.LENGTH_SHORT).show();
                        emptyField = true;
                        return;
                    }
                    if (i==0){
                        String streetNumber = ((EditText)layout.getChildAt(i)).getText().toString();
                        if (!isValidStreetNumber(streetNumber)){
                            Toast.makeText(getApplicationContext(), "Invalid street number", Toast.LENGTH_SHORT).show();

                            return;
                        }
//                        try {
//                            Integer.parseInt(streetNumber);
//                        } catch (Exception e) {
//                            Toast.makeText(getApplicationContext(), "Invalid street number", Toast.LENGTH_SHORT).show();

//                            return;
//                        }
                    } else if (i==1) {
                        String unitNumber = ((EditText) layout.getChildAt(i)).getText().toString();
                        if (!isValidUnitNumber(unitNumber)) {

                            Toast.makeText(getApplicationContext(), "Invalid unit number", Toast.LENGTH_SHORT).show();
//
                            return;
//                            try {
//                                Integer.parseInt(unitNumber);
//                            } catch (Exception e) {
//                                Toast.makeText(getApplicationContext(), "Invalid unit number", Toast.LENGTH_SHORT).show();
//                                invalidUnitNumber = true;
//                                return;
//                            }
                        }
                    }else if ((i==3)){
                            String input = ((EditText)layout.getChildAt(i)).getText().toString();
                            if (! isValidCity(input)) {
                                Toast.makeText(getApplicationContext(), "Invalid City Name", Toast.LENGTH_SHORT).show();

                                return;
                            }
                    }else if ((i==4)){
                        String input = ((EditText)layout.getChildAt(i)).getText().toString();
                        if (!isValidProvince(input)) {
                            Toast.makeText(getApplicationContext(), "Invalid Province/State Name", Toast.LENGTH_SHORT).show();

                            return;
                        }
                    }
                    else if ((i==6)){
                        String input = ((EditText)layout.getChildAt(i)).getText().toString();
                        if (!isValidCountry(input)) {
                            Toast.makeText(getApplicationContext(), "Invalid Country Name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else if (i==5) {
                        String postalCode = ((EditText)layout.getChildAt(i)).getText().toString();

                        if (! isValidPostal(postalCode)) {
                            Toast.makeText(getApplicationContext(), "Invalid zip or postal code", Toast.LENGTH_SHORT).show();

                            return;
                        }
                    }  else if (i==7) {

                        String phoneNumber = ((EditText) layout.getChildAt(i)).getText().toString();

                        if (!isValidPhoneNumber(phoneNumber)) {
                            Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();

                            return;
                        }

                    }


                }

               // if (!emptyField && !invalidStreetNumber && !invalidUnitNumber && !invalidStreetNumber && !invalidPhoneNumber && !invalidPostal) {
                    DatabaseReference dbRefField;
                    for (int i = 0; i < layout.getChildCount() - 1; i++) {
                        dbRefField = FirebaseDatabase.getInstance().getReference("users").child(username).child(fieldNames[i]);
                        dbRefField.setValue(((EditText) layout.getChildAt(i)).getText().toString());
                        addressString += (((EditText) layout.getChildAt(i)).getText().toString() + "\n");

                    }
                    dbRefField = FirebaseDatabase.getInstance().getReference("users").child(username).child("address");
                    dbRefField.setValue(addressString.trim());
                    Toast.makeText(getApplicationContext(), "Successfully Updated Profile", Toast.LENGTH_SHORT).show();
                //}







//                try {
//                    Integer.parseInt(phoneNumber);
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
//                    return;
//                }



//                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("address");
//                dbRef.addListenerForSingleValueEvent(new
//                    ValueEventListener() {
//                        @Override
//                        public void onDataChange (@NonNull DataSnapshot dataSnapshot){
//                            //DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(serviceName);
//                            dbRef.setValue(addressStringFinal);
//
//                            DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference("users").child(username).child("phoneNumber");
//                            dbRef2.setValue(phoneNumber);
//
//                            Toast.makeText(getApplicationContext(), "Successfully Updated Profile", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCancelled (@NonNull DatabaseError error){
//
//                        }
//                });
            }

        });



    }


    public static boolean isValidStreetNumber(String toValidate) {

        try {
            Integer.parseInt(toValidate);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isValidUnitNumber(String toValidate) {

        try {
            Integer.parseInt(toValidate);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isValidCity(String toValidate) {

        return toValidate.matches("[A-Za-z]+");
    }
    public static boolean isValidProvince(String toValidate) {
        return toValidate.matches("[A-Za-z]+");
    }
    public static boolean isValidPostal(String toValidate) {

        return  (toValidate.matches("^[A-Za-z][0-9][A-Za-z] {0,1}[0-9][A-Za-z][0-9]$") || toValidate.matches("^[0-9]{5}$"));
    }
    public static boolean isValidCountry(String toValidate) {
        return toValidate.matches("[A-Za-z]+");
    }
    public static boolean isValidPhoneNumber(String toValidate) {
        return toValidate.matches("^[0-9]{9,12}$");
    }




}
