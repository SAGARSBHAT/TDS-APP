package com.example.myapp.Model;

public class Data {
    private String name;
    private String detail;
    private String id;
    private String date;
    private String time;

    public Data(){

    }

    public Data(String name, String detail, String id, String date) {
        this.name = name;
        this.detail = detail;
        this.id = id;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

