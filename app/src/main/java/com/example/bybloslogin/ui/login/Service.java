package com.example.bybloslogin.ui.login;

import java.util.ArrayList;
import java.util.Arrays;

public class Service {
    private ArrayList<String> fields;
    private String name;

    public Service() {

    }

    public Service(String name, String s) {
        this.name = name;
        String[] split = s.split("\n");
        this.fields = new ArrayList<String>(Arrays.asList(split));
    }

    public Service(String name, ArrayList<String> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public String getHourlyRate() {
        return fields.get(0);
    }

    public ArrayList<String> getFields() {
        return fields;
    }
}
