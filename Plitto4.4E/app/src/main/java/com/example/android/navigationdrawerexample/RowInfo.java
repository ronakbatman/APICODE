package com.example.android.navigationdrawerexample;

/**
 * Created by batman on 11/10/14.
 */
public class RowInfo {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String name;
    private String date;

    public RowInfo(int id,String name,String date)
    {
        this.id=id;
        this.name=name;
        this.date=date;
    }

    public RowInfo(int id,String name)
    {
        this.id=id;
        this.name=name;
        this.date="";
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
