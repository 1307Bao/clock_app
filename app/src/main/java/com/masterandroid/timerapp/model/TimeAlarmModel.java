package com.masterandroid.timerapp.model;

public class TimeAlarmModel {

    private int id;
    private int check;
    private String hour;
    private String minute;
    private String note;

    public TimeAlarmModel() {

    }

    public TimeAlarmModel(int id, String hour, String minute, String note) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
