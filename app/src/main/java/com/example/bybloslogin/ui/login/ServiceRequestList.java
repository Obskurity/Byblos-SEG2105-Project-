package com.example.bybloslogin.ui.login;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bybloslogin.R;

import java.util.List;

public class ServiceRequestList extends ArrayAdapter<ServiceRequest> {

    private Activity context;
    List<ServiceRequest> serviceRequests;

    public ServiceRequestList(Activity context, List<ServiceRequest> serviceRequests) {
        super(context, R.layout.individual_service_layout, serviceRequests);
        this.context = context;
        this.serviceRequests = serviceRequests;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.individual_service_layout, null, true);

        TextView textViewCustomerName = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewServiceName = (TextView) listViewItem.findViewById(R.id.textViewDetail);

        ServiceRequest serviceRequest = serviceRequests.get(position);
        textViewCustomerName.setText(serviceRequest.getCustomerName());
        textViewServiceName.setText(serviceRequest.getServiceName());


        return listViewItem;
    }
}
