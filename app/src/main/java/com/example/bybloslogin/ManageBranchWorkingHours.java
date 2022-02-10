package com.example.bybloslogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;

public class ManageBranchWorkingHours extends AppCompatActivity {
    String username;

    String[] times = new String[14];
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        db = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("workingHours");

        setContentView(R.layout.activity_manage_branch_working_hours);
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("tagtag", dataSnapshot.getValue().toString());
//                times = dataSnapshot.getValue().toString().split("\n");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        final TextView mondayStart = (TextView) findViewById(R.id.txtMondayStart);
        mondayStart.setPaintFlags(mondayStart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView mondayEnd = (TextView) findViewById(R.id.txtMondayEnd);
        mondayEnd.setPaintFlags(mondayEnd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView tuesdayStart = (TextView) findViewById(R.id.txtTuesdayStart);
        tuesdayStart.setPaintFlags(tuesdayStart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView tuesdayEnd = (TextView) findViewById(R.id.txtTuesdayEnd);
        tuesdayEnd.setPaintFlags(tuesdayEnd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView wednesdayStart = (TextView) findViewById(R.id.txtWednesdayStart);
        wednesdayStart.setPaintFlags(wednesdayStart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView wednesdayEnd = (TextView) findViewById(R.id.txtWednesdayEnd);
        wednesdayEnd.setPaintFlags(wednesdayEnd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView thursdayStart = (TextView) findViewById(R.id.txtThursdayStart);
        thursdayStart.setPaintFlags(thursdayStart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView thursdayEnd = (TextView) findViewById(R.id.txtThursdayEnd);
        thursdayEnd.setPaintFlags(thursdayEnd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView fridayStart =(TextView) findViewById(R.id.txtFridayStart);
        fridayStart.setPaintFlags(fridayStart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView fridayEnd = (TextView) findViewById(R.id.txtFridayEnd);
        fridayEnd.setPaintFlags(fridayEnd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView saturdayStart = (TextView) findViewById(R.id.txtSaturdayStart);
        saturdayStart.setPaintFlags(saturdayStart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView saturdayEnd = (TextView) findViewById(R.id.txtSaturdayEnd);
        saturdayEnd.setPaintFlags(saturdayEnd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView sundayStart = (TextView) findViewById(R.id.txtSundayStart);
        sundayStart.setPaintFlags(sundayStart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView sundayEnd = (TextView) findViewById(R.id.txtSundayEnd);
        sundayEnd.setPaintFlags(sundayEnd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                }
                else {
                    if (task.getResult().getValue() == null) {
                        times = new String[14];
                        for (int i = 0; i < times.length; i+=2) {
                            times[i] = "9:00";
                        }
                        for (int i = 1; i < times.length; i+=2) {
                            times[i] = "17:00";
                        }
                        db.setValue(strJoin("\n", times));
                    }
                    else {
                        times = task.getResult().getValue().toString().split("\n");
                    }

                    //if not initialized correctly due to bad data
                    if (times.length != 14) {
                        times = new String[14];
                        for (int i = 0; i < times.length; i+=2) {
                            times[i] = "9:00";
                        }
                        for (int i = 1; i < times.length; i+=2) {
                            times[i] = "17:00";
                        }
                        db.setValue(strJoin("\n", times));
                    }

                    updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);


                    mondayStart.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 0;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);

                                            if (timeIsSmallerThan(mHour, mMinute, times[index + 1], index, mondayStart)) {
                                                mondayStart.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    mondayEnd.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 1;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);

                                            if (timeIsBiggerThan(mHour, mMinute, times[index - 1], index, mondayStart)) {
                                                mondayEnd.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    tuesdayStart.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 2;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);

                                            if (timeIsSmallerThan(mHour, mMinute, times[index + 1], index, tuesdayStart)) {
                                                tuesdayStart.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    tuesdayEnd.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 3;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsBiggerThan(mHour, mMinute, times[index - 1], index, tuesdayStart)) {
                                                tuesdayEnd.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    wednesdayStart.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 4;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsSmallerThan(mHour, mMinute, times[index + 1], index, wednesdayStart)) {
                                                wednesdayStart.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    wednesdayEnd.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 5;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsBiggerThan(mHour, mMinute, times[index - 1], index, wednesdayStart)) {
                                                wednesdayEnd.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    thursdayStart.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 6;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsSmallerThan(mHour, mMinute, times[index + 1], index, thursdayStart)) {
                                                thursdayStart.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    thursdayEnd.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 7;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsBiggerThan(mHour, mMinute, times[index - 1], index, thursdayStart)) {
                                                thursdayEnd.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    fridayStart.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 8;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsSmallerThan(mHour, mMinute, times[index + 1], index, fridayStart)) {
                                                fridayStart.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    fridayEnd.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 9;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsBiggerThan(mHour, mMinute, times[index - 1], index, fridayStart)) {
                                                fridayEnd.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    saturdayStart.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 10;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsSmallerThan(mHour, mMinute, times[index + 1], index, saturdayStart)) {
                                                saturdayStart.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    saturdayEnd.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 11;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsBiggerThan(mHour, mMinute, times[index - 1], index, saturdayStart)) {
                                                saturdayEnd.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    sundayStart.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 12;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsSmallerThan(mHour, mMinute, times[index + 1], index, sundayEnd)) {
                                                sundayStart.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                db.setValue(strJoin("\n", times));
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });

                    sundayEnd.setOnClickListener(new View.OnClickListener() {
                        int mHour;
                        int mMinute;

                        @Override
                        public void onClick(View v) {
                            int index = 13;

                            if (times[index].equals("-1:-1")){
                                mHour= 0;
                                mMinute = 0;
                            }
                            else {
                                mHour = Integer.parseInt(times[index].split(":")[0]);
                                mMinute = Integer.parseInt(times[index].split(":")[1]);
                            }
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ManageBranchWorkingHours.this,
                                    new TimePickerDialog.OnTimeSetListener(){
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                            mHour = hourOfDay;
                                            mMinute = minute;
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(0,0,0,mHour,mMinute);
                                            if (timeIsBiggerThan(mHour, mMinute, times[index - 1], index, sundayStart)) {
                                                sundayEnd.setText(DateFormat.format("hh:mm aa", calendar));
                                                times[index] = mHour + ":" + mMinute;
                                                updateHours(mondayStart, mondayEnd, tuesdayStart, tuesdayEnd, wednesdayStart, wednesdayEnd, thursdayStart, thursdayEnd, fridayStart, fridayEnd, saturdayStart, saturdayEnd, sundayStart, sundayEnd);
                                                db.setValue(strJoin("\n", times));
                                                Toast.makeText(getApplicationContext(), "Successfully Set Time", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Invalid Time Chosen", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }, mHour, mMinute, true
                            );

                            timePickerDialog.updateTime(mHour,mMinute);
                            timePickerDialog.show();


                        }
                    });
                }
            }
        });




    }


    private boolean timeIsSmallerThan(int hour, int minute, String time2, int index, TextView t) {

        String[] s2 = time2.split(":");
        if(t.getText().equals("CLOSED")){
            times[index + 1] = "23:59";
            return true;
        }

        if (hour < Integer.parseInt(s2[0])) {
            return true;
        }
        if (hour == Integer.parseInt(s2[0])) {
            if (minute <= Integer.parseInt(s2[1])) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean timeIsBiggerThan(int hour, int minute, String time2, int index, TextView t) {

        String[] s2 = time2.split(":");

        if(t.getText().equals("CLOSED")){
            times[index - 1] = "00:00";
            return true;
        }

        if (hour > Integer.parseInt(s2[0])) {
            return true;
        }
        if (hour == Integer.parseInt(s2[0])) {
            if (minute >= Integer.parseInt(s2[1])) {
                return true;
            }
            return false;
        }
        return false;
    }

    private String strJoin(String delimeter, String[] arr) {
        String finStr = "";
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length - 1) {
                finStr += arr[i];
            }
            else {
                finStr += arr[i] + delimeter;
            }
        }
        return finStr;
    }

    private void updateHours(TextView mondayStart, TextView mondayEnd, TextView tuesdayStart, TextView tuesdayEnd, TextView wednesdayStart, TextView wednesdayEnd, TextView thursdayStart, TextView thursdayEnd,
                             TextView fridayStart, TextView fridayEnd, TextView saturdayStart, TextView saturdayEnd, TextView sundayStart, TextView sundayEnd){
        int day = 0;

        String[] splitStr = times[day].split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        mondayStart.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        mondayEnd.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        if(mondayStart.getText().equals(mondayEnd.getText())){
            mondayStart.setText("CLOSED");
            mondayEnd.setText("CLOSED");
        }

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        tuesdayStart.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        tuesdayEnd.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        if(tuesdayStart.getText().equals(tuesdayEnd.getText())){
            tuesdayStart.setText("CLOSED");
            tuesdayEnd.setText("CLOSED");
        }

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        wednesdayStart.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        wednesdayEnd.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        if(wednesdayStart.getText().equals(wednesdayEnd.getText())){
            wednesdayStart.setText("CLOSED");
            wednesdayEnd.setText("CLOSED");
        }

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        thursdayStart.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        thursdayEnd.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        if(thursdayStart.getText().equals(thursdayEnd.getText())){
            thursdayStart.setText("CLOSED");
            thursdayEnd.setText("CLOSED");
        }

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        fridayStart.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        fridayEnd.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        if(fridayStart.getText().equals(fridayEnd.getText())){
            fridayStart.setText("CLOSED");
            fridayEnd.setText("CLOSED");
        }

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        saturdayStart.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        saturdayEnd.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        if(saturdayStart.getText().equals(saturdayEnd.getText())){
            saturdayStart.setText("CLOSED");
            saturdayEnd.setText("CLOSED");
        }

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        sundayStart.setText(DateFormat.format("hh:mm aa", calendar));
        day++;

        splitStr = times[day].split(":");
        calendar = Calendar.getInstance();
        calendar.set(0,0,0, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
        sundayEnd.setText(DateFormat.format("hh:mm aa", calendar));

        if(sundayStart.getText().equals(sundayEnd.getText())){
            sundayStart.setText("CLOSED");
            sundayEnd.setText("CLOSED");
        }
    }


}