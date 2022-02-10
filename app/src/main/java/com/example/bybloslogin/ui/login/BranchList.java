package com.example.bybloslogin.ui.login;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bybloslogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BranchList extends ArrayAdapter<Branch> {
        private Activity context;
        List<Branch> branches;

        public BranchList(Activity context, List<Branch> branches) {
            super(context, R.layout.display_working_hours_layout, branches);
            this.context = context;
            this.branches = branches;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.display_working_hours_layout, null, true);

            TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewTitle);

            TextView textViewAddress = (TextView) listViewItem.findViewById(R.id.textViewAddress);

            TextView textViewRating = (TextView) listViewItem.findViewById(R.id.textViewRating);

            LinearLayout layoutWorkingDays = (LinearLayout) listViewItem.findViewById(R.id.layoutWorkingDays);

            Branch branch = branches.get(position);

            for (int i = 0; i < layoutWorkingDays.getChildCount(); i++){
                if (! (branch.workingHours == null)) {
                    ((TextView) (layoutWorkingDays.getChildAt(i))).setText(branch.workingDays.get(i));
                    updateHourText(branch, ((TextView)context.findViewById(R.id.txtSearchHours)).getText().toString(), layoutWorkingDays);
                }
            }



            textViewName.setText(branch.name);

            if (branch.getAddress() != null) {
                //if there is a unit number add a # infront
                String address = "";
                if (!branch.getAddress().contains("\n\n")) {
                    address = branch.getAddress().replaceFirst("\n", "\n(Unit#:)");
                    address = address.replaceAll("[\n]+", " ");
                }
                else {
                    address = branch.getAddress().replaceAll("[\n]+", " ");
                }
                textViewAddress.setText(address);
            }

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(branch.getName()).child("Reviews");
            Query q = db.orderByChild("Rating");
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int m = 0;
                        int n = 0;
                        for (DataSnapshot rating : dataSnapshot.getChildren()) {
                            m += rating.child("Rating").getValue(Double.class);
                            n += 1;

                        }
                        if (n == 0){
                            textViewRating.setText("Rating: None");
                        }
                        else{
                            double average = (double)m / n;
                            textViewRating.setText("Rating: " + String.format("%.2f", average));
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if(textViewRating.getText().toString().isEmpty()){
                textViewRating.setText("Rating: None");
            }





            return listViewItem;
        }

    private void updateHourText(Branch branch, String queryHour, LinearLayout layout) {
        String clean = queryHour.replace(" ", "");
        SimpleDateFormat twelveHR = new SimpleDateFormat("hh:mma");
        SimpleDateFormat twentyFourHR = new SimpleDateFormat("HH:mm");

        if (queryHour.isEmpty()) {
            return;
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
                for (int i = 0; i < layout.getChildCount(); i++) {
                    ((TextView)layout.getChildAt(i)).setTypeface(null, Typeface.NORMAL);
                    ((TextView)layout.getChildAt(i)).setVisibility(View.VISIBLE);
                    ((TextView)layout.getChildAt(i/2)).setPaintFlags(((TextView)layout.getChildAt(i/2)).getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                }
                return;
            }
        }
        String[] hours = branch.workingHours.split("\n");
        for (int i = 0; i < hours.length; i+=2) {
            try {
                Date start = twentyFourHR.parse(hours[i]);
                Date end = twentyFourHR.parse(hours[i + 1]);
                if (parsed.compareTo(start) >= 0 && parsed.compareTo(end) <= 0) {
                    ((TextView)layout.getChildAt(i/2)).setTypeface(null, Typeface.BOLD_ITALIC);
                    ((TextView)layout.getChildAt(i/2)).setPaintFlags(((TextView)layout.getChildAt(i/2)).getPaintFlags() | (Paint.UNDERLINE_TEXT_FLAG));
                }
                else {
                    ((TextView)layout.getChildAt(i/2)).setVisibility(View.VISIBLE);
                }
            }
            catch (Exception e) {
                return;
            }
        }
    }
}
