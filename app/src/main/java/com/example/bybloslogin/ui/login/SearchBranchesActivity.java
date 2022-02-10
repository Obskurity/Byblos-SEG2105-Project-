package com.example.bybloslogin.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bybloslogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class SearchBranchesActivity extends AppCompatActivity {
    private DataSnapshot backup;
    public ArrayList<Branch> branchArrayList;
    ListView listViewBranches;
    DatabaseReference databaseUsers;
    private boolean editing;

    EditText txtSearchAddress;
    EditText txtSearch;
    EditText txtSearchServices;
    EditText txtSearchHours;

    BranchList branchAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_services_layout);

        txtSearchAddress = (EditText) findViewById(R.id.txtSearchAddress);
        txtSearch = (EditText) findViewById(R.id.txtSearchQueryServices);
        txtSearchServices = (EditText) findViewById(R.id.txtSearchServicesOffered);

        txtSearchHours = (EditText) findViewById(R.id.txtSearchHours);
        txtSearchHours.setHint("Search by Working Hours (Enter a time)");
        txtSearchHours.setVisibility(View.VISIBLE);

        txtSearch.setHint("Search by Branch Name");
        txtSearchAddress.setVisibility(View.VISIBLE);
        txtSearchServices.setVisibility(View.VISIBLE);

        TextView description = (TextView) findViewById(R.id.txtDescriptionServices);
        description.setText("Tap and Hold on a Branch to Submit a Service Request or Review the Branch");

        ((Button)findViewById(R.id.btnAddService)).setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        String customerName = extras.getString("username");


        listViewBranches = (ListView) findViewById(R.id.listViewServices);
        branchArrayList = new ArrayList<>();

        listViewBranches.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Branch branch = branchArrayList.get(i);
                showBranchDetails(branch,customerName);
                return true;
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                searchBranches(txtSearch.getText().toString().toLowerCase(),txtSearchAddress.getText().toString().toLowerCase(), txtSearchServices.getText().toString().toLowerCase(), txtSearchHours.getText().toString().replaceAll(" +", ""));
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

        txtSearchHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                searchBranches(txtSearch.getText().toString().toLowerCase(),txtSearchAddress.getText().toString().toLowerCase(), txtSearchServices.getText().toString().toLowerCase(), txtSearchHours.getText().toString().replaceAll(" +", ""));
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

        txtSearchAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                searchBranches(txtSearch.getText().toString().toLowerCase(),txtSearchAddress.getText().toString().toLowerCase(), txtSearchServices.getText().toString().toLowerCase(), txtSearchHours.getText().toString().replaceAll(" +", ""));
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

        txtSearchServices.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                searchBranches(txtSearch.getText().toString().toLowerCase(),txtSearchAddress.getText().toString().toLowerCase(), txtSearchServices.getText().toString().toLowerCase(), txtSearchHours.getText().toString().replaceAll(" +", ""));
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

//            Button btnAddService = (Button)findViewById(R.id.btnAddService);
//            btnAddService.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent addServiceIntent = new Intent(com.example.bybloslogin.ui.login.ManageServicesActivity.this, AddServiceActivity.class);
//                    startActivity(addServiceIntent);
//                }
//            });

    }

    @Override
    protected void onStart() {
        super.onStart();

//            ArrayList<Branch> branchArrayList = new ArrayList<>();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        Query branchQuery =  databaseUsers.orderByChild("accountType").equalTo("Byblos Branch Employee");



        branchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                users.clear();
                //for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){

                    backup = dataSnapshot;
//                        String workingHours = singleSnapshot.child("workingHours").getValue(String.class);
//                        //String password = singleSnapshot.child("password").getValue(String.class);
//                        Branch branch = new Branch(singleSnapshot.getKey(),workingHours);
//                        String query = ((EditText)findViewById(R.id.txtSearchQueryServices)).getText().toString().toLowerCase();
//                        if (branch.getName().toLowerCase().contains(query)) {
//                            branchArrayList.add(branch);
//                        }
                String queryName = ((EditText)findViewById(R.id.txtSearchQueryServices)).getText().toString().toLowerCase();
                searchBranches(txtSearch.getText().toString().toLowerCase(),txtSearchAddress.getText().toString().toLowerCase(), txtSearchServices.getText().toString().toLowerCase(), txtSearchHours.getText().toString().replaceAll(" +", ""));
                String queryAddress = ((EditText)findViewById(R.id.txtSearchAddress)).getText().toString().toLowerCase();
                //searchBranchesAddress(queryAddress);
                String queryServices = ((EditText)findViewById(R.id.txtSearchServicesOffered)).getText().toString().toLowerCase();
                //searchBranchesServices(queryServices );

                //BranchList branchAdapter = new BranchList(SearchBranchesActivity.this,branchArrayList) ;


//
//                    listViewBranches.setAdapter(branchAdapter);
//
//                    backup = dataSnapshot;
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
                //String query = ((EditText)findViewById(R.id.txtSearchQueryServices)).getText().toString().toLowerCase();
                //searchServices(query);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

    private void showBranchDetails(Branch branch, String customerName) {

        ArrayList<String> branchServices = branch.branchServices;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.remove_edit_layout, null);
        dialogBuilder.setView(dialogView);
        editing = false;

        //ArrayList<String> branchServices = new ArrayList<>();

//            DatabaseReference branchServicesDB = FirebaseDatabase.getInstance().getReference("users").child(branch.getName()).child("servicesOffered");
//
//            branchServicesDB.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
//
//                        branchServices.add(singleSnapshot.getValue(String.class));
//                    }
//
//
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError){
//
//                }
//            });


        final Button btnSubmitRequest = (Button) dialogView.findViewById(R.id.btnDeleteService);
        final Button btnRateBranch = (Button) dialogView.findViewById(R.id.btnEditService);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        final Button btnAddField = (Button) dialogView.findViewById(R.id.btnAddField);

        btnSubmitRequest.setVisibility(View.GONE);
        btnSubmitRequest.setTextSize(10);
        btnRateBranch.setText("RATE BRANCH");
        btnRateBranch.setTextSize(10);

        TextView title = new TextView(this);
        title.setText(branch.getName());
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setPadding(10, 10, 10, 10);
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, Typeface.BOLD);

        dialogBuilder.setCustomTitle(title);
        final AlertDialog b = dialogBuilder.create();

        ArrayList<TextView> textViews = new ArrayList<TextView>();

//        ViewSwitcher viewSwitcher = new ViewSwitcher(ManageServicesActivity.this);
        LinearLayout viewFields = (LinearLayout) dialogView.findViewById(R.id.viewFieldLayout);
        viewFields.setVisibility(View.VISIBLE);
//            LinearLayout editFields = (LinearLayout) dialogView.findViewById(R.id.editFieldLayout);
//            editFields.setVisibility(View.GONE);

       // ArrayList<EditText> editTexts = new ArrayList<EditText>();
        //show all fields
        for (int i = 0; i < branchServices.size(); i++) {

            LinearLayout service = new LinearLayout(SearchBranchesActivity.this);

            TextView serviceName = new TextView(com.example.bybloslogin.ui.login.SearchBranchesActivity.this);
            serviceName.setTextSize(20);
            String serviceNameStr = branchServices.get(i);
            serviceName.setText(serviceNameStr);

            LinearLayout.LayoutParams serviceNameBoxSize = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.7f);
            serviceName.setLayoutParams(serviceNameBoxSize);



            service.addView(serviceName);

            Button btnCompleteForm = new Button(SearchBranchesActivity.this);
            btnCompleteForm.setText("SUBMIT REQUEST");

            btnCompleteForm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SearchBranchesActivity.this, SubmitRequestActivity.class);
                    intent.putExtra("branchName",branch.getName());
                    intent.putExtra("serviceName",serviceNameStr);
                    intent.putExtra("customerName",customerName);
                    startActivity(intent);

                }

            });

            service.addView( btnCompleteForm);

            viewFields.addView(service);


//            LinearLayout fieldMenu = new LinearLayout(com.example.bybloslogin.ui.login.SearchBranchesActivity.this);
//            fieldMenu.setOrientation(LinearLayout.HORIZONTAL);

//                EditText editField = new EditText(com.example.bybloslogin.ui.login.SearchBranchesActivity.this);
//                editField.setTextSize(20);
//                editField.setText(service.getFields().get(i));
//                editTexts.add(editField);
//
//                LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.7f);
//                editField.setLayoutParams(editTextParams);


//                fieldMenu.addView(editField, fieldMenu.getChildCount() );

            if (i != 0) {
                Button btnDeleteField = new Button(com.example.bybloslogin.ui.login.SearchBranchesActivity.this);
                btnDeleteField.setText("DELETE");


//                    btnDeleteField.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            editField.setVisibility(View.GONE);
//                            fieldMenu.setVisibility(View.GONE);
//
//                        }
//
//                    });


//                    fieldMenu.addView(btnDeleteField, fieldMenu.getChildCount());

            }
//                editFields.addView(fieldMenu, editFields.getChildCount() );
        }


        b.show();

        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchBranchesActivity.this, SubmitRequestActivity.class);
                intent.putExtra("branchName",branch.getName());
                intent.putExtra("branchServices",branch.getServices());


                startActivity(intent);
            }
        });


        btnRateBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchBranchesActivity.this, RateBranchActivity.class);
                intent.putExtra("branchName",branch.getName());
                startActivity(intent);
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
//                    EditText field = new EditText(com.example.bybloslogin.ui.login.ManageServicesActivity.this);
//
//                    //LinearLayout editTexts = (LinearLayout) findViewById(R.id.editFieldLayout);
//
//                    LinearLayout fieldMenu = new LinearLayout(com.example.bybloslogin.ui.login.ManageServicesActivity.this);
//                    fieldMenu.setOrientation(LinearLayout.HORIZONTAL);
//
//                    EditText editField = new EditText(com.example.bybloslogin.ui.login.ManageServicesActivity.this);
//                    editField.setTextSize(20);
//                    editField.setHint("Enter new field");
//                    editTexts.add(editField);
//
//                    LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.7f);
//                    editField.setLayoutParams(editTextParams);
//
//
//                    fieldMenu.addView(editField, fieldMenu.getChildCount() );
//
//                    Button btnDeleteField = new Button(com.example.bybloslogin.ui.login.ManageServicesActivity.this);
//                    btnDeleteField.setText("DELETE");

//                    btnDeleteField.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            editField.setVisibility(View.GONE);
//                            fieldMenu.setVisibility(View.GONE);
//
//                        }
//
//                    });
//
//                    fieldMenu.addView(btnDeleteField, fieldMenu.getChildCount() );
//
//                    editFields.addView(fieldMenu, editFields.getChildCount() );


            }
        });

    }


    private ArrayList<String> getBranchServices(Branch branch){


        ArrayList<String> branchServices = new ArrayList<>();

        DatabaseReference branchServicesDB = FirebaseDatabase.getInstance().getReference("users").child(branch.getName()).child("servicesOffered");

        branchServicesDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){

                    branchServices.add(singleSnapshot.getValue(String.class));
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

        return branchServices;

    }


//       private void searchBranchesName(String query) {
//            branchArrayList.clear();
//            for (DataSnapshot postSnapshot : backup.getChildren()) {
//                String workingHours = postSnapshot.child("workingHours").getValue(String.class);
//                Branch branch = new Branch(postSnapshot.getKey(),workingHours);
//                if (branch.getName().toLowerCase().contains(query)) {
//                    branchArrayList.add(branch);
//                }
//            }
//
//
//
//           BranchList branchAdapter = new BranchList(SearchBranchesActivity.this,branchArrayList) ;
//           listViewBranches.setAdapter(branchAdapter);
//       }
//
//        private void searchBranchesAddress(String query) {
//            branchArrayList.clear();
//            for (DataSnapshot postSnapshot : backup.getChildren()) {
//                String workingHours = postSnapshot.child("workingHours").getValue(String.class);
//                Branch branch = new Branch(postSnapshot.getKey(),workingHours);
//
//                DatabaseReference branchAddressDB = FirebaseDatabase.getInstance().getReference("users").child(branch.getName()).child("address");
//
//                branchAddressDB.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                            branch.address =  dataSnapshot.getValue(String.class);
//                            if (branch.getAddress() != null && branch.getAddress().toLowerCase().contains(query)) {
//                                branchArrayList.add(branch);
//                            }
//                            BranchList branchAdapter = new BranchList(SearchBranchesActivity.this,branchArrayList) ;
//                            listViewBranches.setAdapter(branchAdapter);
//
//
//
//
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError){
//
//                    }
//                });
//
//            }
//
//        }
//
//        private void searchBranchesServices(String query) {
//            branchArrayList.clear();
//            for (DataSnapshot postSnapshot : backup.getChildren()) {
//                String workingHours = postSnapshot.child("workingHours").getValue(String.class);
//                Branch branch = new Branch(postSnapshot.getKey(), workingHours);
//
//                DatabaseReference branchServicesDB = FirebaseDatabase.getInstance().getReference("users").child(branch.getName()).child("servicesOffered");
//                branchServicesDB.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
//                            branch.branchServices.add(singleSnapshot.getKey());
//                            if (singleSnapshot.getKey().toLowerCase().contains(query) && !branchArrayList.contains(branch)) {
//                                branchArrayList.add(branch);
//                            }
//                        }
//                        BranchList branchAdapter = new BranchList(SearchBranchesActivity.this, branchArrayList);
//                        listViewBranches.setAdapter(branchAdapter);
//
//
//                    }

//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//            }
//        }

    private void searchBranches(String queryName, String queryAddress, String queryService, String queryHour) {

        branchArrayList.clear();
        for (DataSnapshot postSnapshot : backup.getChildren()) {
            String address = postSnapshot.child("address").getValue(String.class);

            String workingHours = postSnapshot.child("workingHours").getValue(String.class);


            ArrayList<String> servicesOffered = new ArrayList<>();
            DataSnapshot servicesSnapshot = postSnapshot.child("servicesOffered");
            for (DataSnapshot serviceSnapshot : servicesSnapshot.getChildren()) {
                servicesOffered.add(serviceSnapshot.getKey());
            }
            Branch branch = new Branch(postSnapshot.getKey(), address, workingHours, servicesOffered);

//                switch (searchBy){
//                    case "name":
//                        if (branch.getName().toLowerCase().contains(query)) {
//                            branchArrayList.add(branch);
//                        }
//                        break;
//                    case "address":
//                        if (branch.getAddress() != null && branch.getAddress().toLowerCase().contains(query)) {
//                            branchArrayList.add(branch);
//                        }
//                        break;
//                    case "services":
//                        for (int i = 0; i<servicesOffered.size(); i++) {
//                            if (servicesOffered.get(i).toLowerCase().contains(query) && !branchArrayList.contains(branch)) {
//                                branchArrayList.add(branch);
//                                break;
//                            }
//                        }
//                        break;
//                }
            if (branch.getName().toLowerCase().contains(queryName) && (branch.getAddress() != null && branch.getAddress().toLowerCase().replace("\n", " ").contains(queryAddress)) && branchContainsHour(branch, queryHour)) {
                for (int i = 0; i< servicesOffered.size(); i++) {
                    if (servicesOffered.get(i).toLowerCase().contains(queryService) && !branchArrayList.contains(branch)) {
                        branchArrayList.add(branch);
                        break;
                    }
                }
            }
        }
        branchAdapter = new BranchList(SearchBranchesActivity.this, branchArrayList);
        listViewBranches.setAdapter(branchAdapter);
    }

     public static boolean branchContainsHour(Branch branch, String queryHour) {
        String clean = queryHour.replace(" ", "");
        SimpleDateFormat twelveHR = new SimpleDateFormat("hh:mma");
        SimpleDateFormat twentyFourHR = new SimpleDateFormat("HH:mm");

        if (queryHour.isEmpty()) {
            return true;
        }

        Date parsed;

        try {
            parsed = twelveHR.parse(clean);
        }
        catch (Exception e) {
            try {
                parsed = twentyFourHR.parse(clean);
            }
            catch (Exception d) {
                return true;
            }
        }
        String[] hours = branch.workingHours.split("\n");
        for (int i = 0; i < hours.length; i+=2) {
            try {
                Date start = twentyFourHR.parse(hours[i]);
                Date end = twentyFourHR.parse(hours[i + 1]);
                if (parsed.compareTo(start) >= 0 && parsed.compareTo(end) <= 0) {
                    return true;
                }
            }
            catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}




