package com.tokayoapp.Modal;

public class NotificationModal {
    public String title;
    public String disc;
    public String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public NotificationModal(String title, String disc, String date, String time) {
        this.title = title;
        this.disc = disc;
        this.date = date;
        this.time = time;
    }

    public String time;

}
