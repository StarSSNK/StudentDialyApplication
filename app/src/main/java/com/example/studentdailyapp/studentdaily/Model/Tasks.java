package com.example.studentdailyapp.studentdaily.Model;

public class Tasks {
   public String type,title,detail,classroom,status,dueDate,dueTime,udt;

    public Boolean selected;

    public Tasks(){}


    public Tasks(String type, String title, String detail, String classroom, String status, String dueDate, String dueTime, String udt) {

        this.type = type;
        this.title = title;
        this.detail = detail;
        this.classroom = classroom;
        this.status = status;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.udt = udt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getUdt() {
        return udt;
    }

    public void setUdt(String udt) {
        this.udt = udt;
    }

    public boolean isSelected() {
        return false;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

