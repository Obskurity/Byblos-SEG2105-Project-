package com.example.bybloslogin.ui.login;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class DeleteUserActivity extends AppCompatActivity {

//    EditText editUserName;
//    EditText editTextPrice;
//    Button buttonAddProduct;
    ListView listViewUsers;

    private List<User> users;

    DatabaseReference databaseUsers;
    private DataSnapshot backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_users);

//        editTextName = (EditText) findViewById(R.id.editTextName);
//        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
//        buttonAddProduct = (Button) findViewById(R.id.addButton);

        users = new ArrayList<>();

        //adding an onclicklistener to button
//        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addProduct();
//            }
//        });

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                showUpdateDeleteDialog(user.getUsername(), user.getAccountType());
                return true;
            }
        });

        EditText txtSearch = (EditText) findViewById(R.id.txtSearchQuery);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                searchUsers(txtSearch.getText().toString().toLowerCase());
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

    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.addValueEventListener(new ValueEventListener() {

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
                searchUsers(((EditText)findViewById(R.id.txtSearchQuery)).getText().toString().toLowerCase());
            }
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }


    private void showUpdateDeleteDialog(final String username, String accountType) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialogue, null);
        dialogBuilder.setView(dialogView);
//
//        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
//        final EditText editTextPrice  = (EditText) dialogView.findViewById(R.id.editTextPrice);
        final Button buttonDeleteYes = (Button) dialogView.findViewById(R.id.btnDeleteYes);
        final Button buttonDeleteNo = (Button) dialogView.findViewById(R.id.btnDeleteNo);

        TextView title = new TextView(this);
        title.setText(username + " - " + accountType);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setPadding(10, 10, 10, 10);
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, Typeface.BOLD);


        dialogBuilder.setCustomTitle(title);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDeleteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Administrator.deleteUser(username);
                Toast.makeText(getApplicationContext(), "User Account Deleted", Toast.LENGTH_SHORT).show();

                b.dismiss();
            }
        });

        buttonDeleteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                b.dismiss();
            }
        });
    }

//    private void updateProduct(String id, String name, double price) {
//
//        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("products").child(id);
//        Product product = new Product(id,name,price);
//        dR.setValue(product);
//
//        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_LONG).show();
//    }

    // uncomment and delete from Administrator if it stops working - arthur
//    private boolean deleteUser(String username) {
//
//        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(username);
//
//        dR.removeValue();
//
//        Toast.makeText(getApplicationContext(), "User Account Deleted", Toast.LENGTH_LONG).show();
//        return true;
//    }

//    private boolean inString(String s, String query) {
//        if (query.equals("")) {
//            return true;
//        }
//        for (int i = 0; i < s.length() - query.length() + 1; i++) {
//            if (s.substring(i, i + query.length()).equals(query)) {
//                return true;
//            }
//        }
//        return false;
//    }
    
    private void searchUsers(String query) {
        users.clear();
        for (DataSnapshot postSnapshot : backup.getChildren()) {

            User user = postSnapshot.getValue(User.class);
            user = new User(postSnapshot.getKey(), user.getPassword(), user.getAccountType());
            if (user != null && user.getAccountType() != null) {
                if (!user.accountType.equals("Admin") && user.getUsername().contains(query)) {
                    users.add(user);
                }
            }
        }

        UserList usersAdapter = new UserList(DeleteUserActivity.this, users);
        listViewUsers.setAdapter(usersAdapter);
    }
//    private void addProduct() {
//        String name = editTextName.getText().toString().trim();
//
//        double price;
//        try {
//            price = Double.parseDouble(String.valueOf(editTextPrice.getText().toString()));
//            if (price < 0) {
//                throw new Exception();
//            }
//        }
//        catch (Exception e) {
//            if (!TextUtils.isEmpty(name)) {
//                Toast.makeText(getApplicationContext(), "Please enter a valid price", Toast.LENGTH_LONG).show();
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "Please enter a valid name and price", Toast.LENGTH_LONG).show();
//            }
//            return;
//        }
//
//        if (!TextUtils.isEmpty(name)){
//            String id = databaseProducts.push().getKey();
//
//            Product product = new Product(id, name, price);
//
//            databaseProducts.child(id).setValue(product);
//
//            editTextName.setText("");
//            editTextPrice.setText("");
//
//            Toast.makeText(this, "Product added", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
//        }
//
//
//
//    }
}



