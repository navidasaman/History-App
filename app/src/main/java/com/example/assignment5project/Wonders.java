package com.example.assignment5project;

public class Wonders {
    private String ID;
    private String name;
    private String type;
    private String company;
    private String location;
    private String category;
    private int size;
    private int cost;
    private String auxdata;

    public Wonders(long _in, String _name, String _company, String _location, String _category, String _auxdata) {
        ID=Long.toString(_in);
        auxdata = _auxdata;
        name = _name;
        company = _company;
        location = _location;
        category = _category;

    }

    public String getName() { return name;}

    public String getCompany() { return company;}

    public String getLocation() { return location;}

    public String getCategory() { return category;}

    public String getAuxdata() { return auxdata;}

      @Override
    public String toString() { return name;}

}
