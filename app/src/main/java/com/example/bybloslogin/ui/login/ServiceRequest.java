package com.example.bybloslogin.ui.login;

import java.util.ArrayList;
import java.util.Arrays;

public class ServiceRequest {
    private ArrayList<String> fields;

    private String key;
    private String fieldsString;

    public ServiceRequest() {

    }

    public ServiceRequest(String key, String s) {
        this.key = key;
        String[] split = s.split("\n");
        this.fields = new ArrayList<String>(Arrays.asList(split));
        this.fieldsString = s;
    }

    public ServiceRequest(String key, ArrayList<String> fields) {
        this.key = key;
        this.fields = fields;
    }

    public String getCustomerName() {
        return fields.get(0);
    }

    public String getServiceName() {
        return fields.get(1);
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public String getKey(){
        return key;
    }

    public String getFieldsString(){
        return fieldsString;
    }
}
