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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bybloslogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ApprovedServiceRequests extends AppCompatActivity {
    private DataSnapshot backup;
    private List<ServiceRequest> serviceRequestArrayList;
    ListView listViewServiceRequest;
    DatabaseReference databaseServices;
    private boolean editing;
    String username;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_services_layout);

        listViewServiceRequest = (ListView) findViewById(R.id.listViewServices);
        serviceRequestArrayList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");



        //EditText searchRequests = (EditText) findViewById(R.id.txtSearchQueryServices);
        //searchRequests.setText("Search for Requests");

        TextView description = (TextView) findViewById(R.id.txtDescriptionServices);
        description.setText("Approved Requests");

//        listViewServiceRequest.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ServiceRequest serviceRequest = serviceRequestArrayList.get(i);
//                showAcceptDialog(serviceRequest);
//                return true;
//            }
//        });

        EditText searchRequests = (EditText) findViewById(R.id.txtSearchQueryServices);
        searchRequests.setHint("Search for Requests");
        searchRequests.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                searchServices(searchRequests.getText().toString().toLowerCase());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }

        });

        Button btnApprovedServices = (Button)findViewById(R.id.btnAddService);
        btnApprovedServices.setVisibility(View.GONE);
//        btnApprovedServices.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent approvedServicesIntent = new Intent(ApprovedServiceRequests.this, AddServiceActivity.class);
//                startActivity(approvedServicesIntent);
//            }
//        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        databaseServices = FirebaseDatabase.getInstance().getReference("users").child(username).child("approvedRequests");
        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                users.clear();
                backup = dataSnapshot;
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//
//                    User user = postSnapshot.getValue(User.class);
//                    user = new User(postSnapshot.getKey(), user.getPassword(), user.getAccountType());
//                    if (!user.accountType.equals("Admin")) {
//                        users.add(user);
//                    }
//                }

//                UserList usersAdapter = new UserList(DeleteUserActivity.this, users);
//                listViewUsers.setAdapter(usersAdapter);
                String query = ((EditText)findViewById(R.id.txtSearchQueryServices)).getText().toString().toLowerCase();
                searchServices(query);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

    private void showAcceptDialog(ServiceRequest serviceRequest) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.remove_edit_layout, null);
        dialogBuilder.setView(dialogView);
        editing = false;

        final Button btnApprove = (Button) dialogView.findViewById(R.id.btnDeleteService);
        final Button btnReject = (Button) dialogView.findViewById(R.id.btnEditService);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        final Button btnAddField = (Button) dialogView.findViewById(R.id.btnAddField);

        TextView title = new TextView(this);
        title.setText(serviceRequest.getCustomerName());
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setPadding(10, 10, 10, 10);
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, Typeface.BOLD);

        dialogBuilder.setCustomTitle(title);
        final AlertDialog b = dialogBuilder.create();


//        ViewSwitcher viewSwitcher = new ViewSwitcher(ManageServicesActivity.this);
        LinearLayout viewFields = (LinearLayout) dialogView.findViewById(R.id.viewFieldLayout);
        viewFields.setVisibility(View.VISIBLE);
        LinearLayout editFields = (LinearLayout) dialogView.findViewById(R.id.editFieldLayout);
        editFields.setVisibility(View.GONE);

        ArrayList<TextView> textViews = new ArrayList<TextView>();
        ArrayList<EditText> editTexts = new ArrayList<EditText>();
        //show all fields
        for (int i = 0; i < serviceRequest.getFields().size(); i++) {

            TextView viewField = new TextView(ApprovedServiceRequests.this);
            viewField.setTextSize(20);
            viewField.setText(serviceRequest.getFields().get(i));
            viewFields.addView(viewField, viewFields.getChildCount());
            textViews.add(viewField);

            LinearLayout fieldMenu = new LinearLayout(ApprovedServiceRequests.this);
            fieldMenu.setOrientation(LinearLayout.HORIZONTAL);

            EditText editField = new EditText(ApprovedServiceRequests.this);
            editField.setTextSize(20);
            editField.setText(serviceRequest.getFields().get(i));
            editTexts.add(editField);

            LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.7f);
            editField.setLayoutParams(editTextParams);


            fieldMenu.addView(editField, fieldMenu.getChildCount() );

            if (i != 0) {
                Button btnDeleteField = new Button(ApprovedServiceRequests.this);
                btnDeleteField.setText("DELETE");


                btnDeleteField.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editField.setVisibility(View.GONE);
                        fieldMenu.setVisibility(View.GONE);

                    }

                });


                fieldMenu.addView(btnDeleteField, fieldMenu.getChildCount());

            }
            editFields.addView(fieldMenu, editFields.getChildCount() );
        }


        b.show();
        btnApprove.setText("APPROVE");
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Employee.approveService(serviceRequest, username);
                Toast.makeText(getApplicationContext(), "Service Request Successfully Approved", Toast.LENGTH_SHORT).show();
                b.dismiss();
                finish();
                startActivity(getIntent());
            }
        });

        btnReject.setText("REJECT");
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Employee.rejectService(serviceRequest, username);
                Toast.makeText(getApplicationContext(), "Service Request Successfully Rejected", Toast.LENGTH_SHORT).show();
                b.dismiss();
                finish();
                startActivity(getIntent());
//                    editing = !editing;
//
//
//                    if (editing) {
//                        viewFields.setVisibility(View.GONE);
//                        editFields.setVisibility(View.VISIBLE);
//                        btnAddField.setVisibility(View.VISIBLE);
//                        btnReject.setText("SAVE");
//
//
//
//                    } else { // viewing preview of fields - should match what is in database
//
//                        LinearLayout fieldMenu = (LinearLayout) editFields.getChildAt(0);
//                        EditText priceField = (EditText) fieldMenu.getChildAt(0);
//
//                        if (priceField.getText().toString().equals("")) {
//                            Toast.makeText(getApplicationContext(), "Hourly Rate Cannot be Empty", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        double hourly;
//                        String fieldString = "";
//                        try {
//                            hourly = Double.parseDouble(priceField.getText().toString());
//                            if (hourly < 0) {
//                                Toast.makeText(getApplicationContext(), "Hourly Rate Must be a Non-Negative Number", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        } catch (Exception e) {
//                            Toast.makeText(getApplicationContext(), "Hourly Rate Must be Numeric", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        for (int i = 1; i < editFields.getChildCount(); i++) {
//
//                            fieldMenu = (LinearLayout) editFields.getChildAt(i);
//                            if (!(fieldMenu.getVisibility() == View.GONE)) {
//                                EditText editField = (EditText) fieldMenu.getChildAt(0);
//                                if (!(editField.getVisibility() == View.GONE)) {
//                                    if (editField.getText().toString().trim().equals("")) {
//                                        Toast.makeText(getApplicationContext(), "Field name cannot be empty", Toast.LENGTH_SHORT).show();
//                                        return;
//                                    } else {
//                                        if (i == 0) {
//                                            fieldString += editField.getText().toString().trim().replaceAll("[+-]", "") + "\n";
//                                        } else {
//                                            fieldString += editField.getText().toString().trim() + "\n";
//                                        }
//                                    }
//
//
//                                }
//                            }
//
//                        }
//                    }

//                        btnEdit.setText("EDIT");
//                        btnAddField.setVisibility(View.GONE);
//                        for (int i = 0; i < textViews.size(); i++) {
//
//                            // if editText has been deleted, delete textView as well
//
//                            if (editTexts.get(i).getVisibility() == View.GONE){
//                                textViews.get(i).setVisibility(View.GONE);
//                            } else {
//                                textViews.get(i).setText(editTexts.get(i).getText());
//                            }
//
//                        }
//                        // convert all editTexts to textViews
//                        for (int i = textViews.size(); i < editTexts.size(); i++){
//                            TextView viewField = new TextView(com.example.bybloslogin.ui.login.ManageServiceRequests.this);
//                            viewField.setTextSize(20);
//                            viewField.setText(editTexts.get(i).getText());
//                            viewFields.addView(viewField, viewFields.getChildCount());
//                            textViews.add(viewField);
//
//                        }
//
////
//
//                        viewFields.setVisibility(View.VISIBLE);
//                        editFields.setVisibility(View.GONE);
//
////                    LinearLayout fieldMenu =  (LinearLayout)editFields.getChildAt(0);
////                    EditText priceField = (EditText) fieldMenu.getChildAt(0);
////
////                    if (priceField.getText().toString().equals("")) {
////                        Toast.makeText(getApplicationContext(), "Hourly Rate Cannot be Empty", Toast.LENGTH_SHORT).show();
////                        return;
////                    }
////                    double hourly;
////                    String fieldString = "";
////                    try {
////                        hourly = Double.parseDouble(priceField.getText().toString());
////                        if (hourly < 0) {
////                            Toast.makeText(getApplicationContext(), "Hourly Rate Must be a Non-Negative Number", Toast.LENGTH_SHORT).show();
////                            return;
////                        }
////                    }
////                    catch (Exception e) {
////                        Toast.makeText(getApplicationContext(), "Hourly Rate Must be Numeric", Toast.LENGTH_SHORT).show();
////                        return;
////                    }
//
//
//
////                    for (int i = 1; i < editFields.getChildCount(); i++) {
////
////                        fieldMenu =  (LinearLayout)editFields.getChildAt(i);
////                       if (!(fieldMenu.getVisibility() == View.GONE)) {
////                           EditText editField = (EditText) fieldMenu.getChildAt(0);
////                           if (!(editField.getVisibility() == View.GONE)) {
////                               if (!editField.getText().toString().trim().equals("")) {
////                                   if (i == 0) {
////                                       fieldString += editField.getText().toString().trim().replaceAll("[+-]", "") + "\n";
////                                   } else {
////                                       fieldString += editField.getText().toString().trim() + "\n";
////                                   }
////                                }
////
////
////                               }
////                           }
////
////                       }
//
//                        final String fieldStringFinal = fieldString.trim();
//                        Administrator.editService(serviceRequest, fieldStringFinal);

//                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("services").child(service.getName());
//                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                Administrator.addService(service.getName(), fieldStringFinal);
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });


//                LinearLayout editFields = (LinearLayout) dialogView.findViewById(R.id.editFieldLayout);


            }


        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });



        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText field = new EditText(ApprovedServiceRequests.this);

                //LinearLayout editTexts = (LinearLayout) findViewById(R.id.editFieldLayout);

                LinearLayout fieldMenu = new LinearLayout(ApprovedServiceRequests.this);
                fieldMenu.setOrientation(LinearLayout.HORIZONTAL);

                EditText editField = new EditText(ApprovedServiceRequests.this);
                editField.setTextSize(20);
                editField.setHint("Enter new field");
                editTexts.add(editField);

                LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.7f);
                editField.setLayoutParams(editTextParams);


                fieldMenu.addView(editField, fieldMenu.getChildCount() );

                Button btnDeleteField = new Button(ApprovedServiceRequests.this);
                btnDeleteField.setText("DELETE");

                btnDeleteField.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editField.setVisibility(View.GONE);
                        fieldMenu.setVisibility(View.GONE);

                    }

                });

                fieldMenu.addView(btnDeleteField, fieldMenu.getChildCount() );

                editFields.addView(fieldMenu, editFields.getChildCount() );


            }
        });

    }




    private void searchServices(String query) {
        serviceRequestArrayList.clear();
        for (DataSnapshot postSnapshot : backup.getChildren()) {
            String s = postSnapshot.getValue(String.class);
            ServiceRequest serviceRequest = new ServiceRequest(postSnapshot.getKey(), s);
            if (serviceRequest.getCustomerName().toLowerCase().contains(query)) {
                serviceRequestArrayList.add(serviceRequest);
            }
        }

        ServiceRequestList servicesAdapter = new ServiceRequestList(ApprovedServiceRequests.this, serviceRequestArrayList);
        listViewServiceRequest.setAdapter(servicesAdapter);
    }


}


