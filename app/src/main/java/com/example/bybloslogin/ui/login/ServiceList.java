package com.example.bybloslogin.ui.login;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bybloslogin.R;

import java.util.List;

public class ServiceList extends ArrayAdapter<Service> {

    private Activity context;
    List<Service> services;

    public ServiceList(Activity context, List<Service> services) {
        super(context, R.layout.individual_service_layout, services);
        this.context = context;
        this.services = services;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.individual_service_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewRate = (TextView) listViewItem.findViewById(R.id.textViewDetail);

        Service service = services.get(position);
        textViewName.setText(service.getName());
        textViewRate.setText("$" + service.getHourlyRate() + "/hr");


        return listViewItem;
    }
}
