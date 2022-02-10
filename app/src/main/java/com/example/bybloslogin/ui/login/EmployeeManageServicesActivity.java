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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bybloslogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EmployeeManageServicesActivity extends AppCompatActivity{
    private DataSnapshot backup;
    private List<Service> serviceArrayList;
    ListView listViewServices;
    DatabaseReference databaseServices;
    private boolean editing;
    private String username;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_services_layout);

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");

        TextView message = (TextView) findViewById(R.id.txtDescriptionServices);
        message.setText("Tap and Hold on a Service to add it to this Branch");

        listViewServices = (ListView) findViewById(R.id.listViewServices);
        serviceArrayList = new ArrayList<>();



        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = serviceArrayList.get(i);
                showEditDeleteDialog(service,username);
                return true;
            }
        });

        EditText txtSearch = (EditText) findViewById(R.id.txtSearchQueryServices);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                searchServices(txtSearch.getText().toString().toLowerCase());
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

        Button btnAddService = (Button)findViewById(R.id.btnAddService);
        btnAddService.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseServices = FirebaseDatabase.getInstance().getReference("services");
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

    private void showEditDeleteDialog(Service service, String username) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.employee_add_service_layout, null);
        dialogBuilder.setView(dialogView);

        final Button btnAddtoProfile = (Button) dialogView.findViewById(R.id.btnAddtoProfile);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        final Button btnRemove = (Button) dialogView.findViewById(R.id.btnRemove);

        TextView title = new TextView(this);
        title.setText(service.getName());
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
        for (int i = 0; i < service.getFields().size(); i++) {

            TextView viewField = new TextView(EmployeeManageServicesActivity.this);
            viewField.setTextSize(20);
            viewField.setText(service.getFields().get(i));
            viewFields.addView(viewField, viewFields.getChildCount());
            textViews.add(viewField);

            LinearLayout fieldMenu = new LinearLayout(EmployeeManageServicesActivity.this);
            fieldMenu.setOrientation(LinearLayout.HORIZONTAL);

            EditText editField = new EditText(EmployeeManageServicesActivity.this);
            editField.setTextSize(20);
            editField.setText(service.getFields().get(i));
            editTexts.add(editField);

            LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
            editField.setLayoutParams(editTextParams);


            fieldMenu.addView(editField, fieldMenu.getChildCount());

            Button btnDeleteField = new Button(EmployeeManageServicesActivity.this);
            btnDeleteField.setText("DELETE");

            btnDeleteField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editField.setVisibility(View.GONE);
                    fieldMenu.setVisibility(View.GONE);

                }

            });

            fieldMenu.addView(btnDeleteField, fieldMenu.getChildCount());

            editFields.addView(fieldMenu, editFields.getChildCount());
        }

        DatabaseReference databaseServicesOffered = FirebaseDatabase.getInstance().getReference("users").child(username).child("servicesOffered");


        b.show();


        btnAddtoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseServicesOffered.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (task.getResult().hasChild(service.getName())) {
                            Toast.makeText(getApplicationContext(), "Service Already Added to Profile", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference newService = databaseServicesOffered.child(service.getName());
                            newService.setValue(service.getHourlyRate());
                            Toast.makeText(getApplicationContext(), "Service Successfully Added to Profile", Toast.LENGTH_SHORT).show();
                            String query = ((EditText)findViewById(R.id.txtSearchQueryServices)).getText().toString().toLowerCase();
                            searchServices(query);
                        }

                    }
                });

//                databaseServicesOffered.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot.hasChild(service.getName())){
//                    Toast.makeText(getApplicationContext(), "Service Already Added to Profile", Toast.LENGTH_SHORT).show();
//                } else {
//                    DatabaseReference newService = databaseServicesOffered.child(service.getName());
//
//                    newService.setValue(service.getHourlyRate());
//                    Toast.makeText(getApplicationContext(), "Service Successfully Added to Profile", Toast.LENGTH_SHORT).show();
//                }
//

//
//
//                }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });


                //Toast.makeText(getApplicationContext(), "Service Successfully Added to Profile", Toast.LENGTH_SHORT).show();
                b.dismiss();
//                finish();
//                startActivity(getIntent());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Employee.removeService(databaseServicesOffered, service, username);

                databaseServicesOffered.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (task.getResult().hasChild(service.getName())) {
                            DatabaseReference dR = databaseServicesOffered.child(service.getName());
                            dR.removeValue();
                            Toast.makeText(getApplicationContext(), "Service Successfully Removed from Profile", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Service Not Added to Profile", Toast.LENGTH_SHORT).show();
                        }


//                databaseServicesOffered.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.hasChild(service.getName())) {
//                            DatabaseReference dR = databaseServicesOffered.child(service.getName());
//                            dR.removeValue();
//                            Toast.makeText(getApplicationContext(), "Service Successfully Removed from Profile", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Service Not Added to Profile", Toast.LENGTH_SHORT).show();
//                        }
//
                        String query = ((EditText)findViewById(R.id.txtSearchQueryServices)).getText().toString().toLowerCase();
                        searchServices(query);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

                        b.dismiss();
                    }
                });

            }

        });

    }

    private void searchServices(String query) {
        serviceArrayList.clear();
        for (DataSnapshot postSnapshot : backup.getChildren()) {
            String s = postSnapshot.getValue(String.class);
            Service service = new Service(postSnapshot.getKey(), s);
            if (service.getName().toLowerCase().contains(query)) {
                serviceArrayList.add(service);
            }
        }

        EmployeeAddServiceList servicesAdapter = new EmployeeAddServiceList(EmployeeManageServicesActivity.this, serviceArrayList, username);
        listViewServices.setAdapter(servicesAdapter);


    }





}
