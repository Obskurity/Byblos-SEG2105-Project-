package com.example.bybloslogin.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bybloslogin.ManageBranchWorkingHours;
import com.example.bybloslogin.R;

public class EmployeeMenu  extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_menu_layout);

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");

        final Button btnMngServices = (Button) findViewById(R.id.btnMngServices);
        final Button btnWorkingHrs = (Button) findViewById(R.id.btnWorkingHrs);
        final Button btnMngRequests = (Button) findViewById(R.id.btnMngRequests);


        btnMngServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageServices = new Intent(EmployeeMenu.this, EmployeeManageServicesActivity.class);
                manageServices.putExtra("username",username);
                startActivity(manageServices);
            }
        });

        btnWorkingHrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent workingHours = new Intent(EmployeeMenu.this, WorkingHoursActivity.class);
                //startActivity(workingHours);
                Intent manageWorkingHours = new Intent(EmployeeMenu.this, ManageBranchWorkingHours.class);
                manageWorkingHours.putExtra("username",username);
                startActivity(manageWorkingHours);

            }
        });

        btnMngRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageRequests = new Intent(EmployeeMenu.this, ManageServiceRequests.class);
                manageRequests.putExtra("username",username);
                startActivity(manageRequests);

            }
        });
    }

}
