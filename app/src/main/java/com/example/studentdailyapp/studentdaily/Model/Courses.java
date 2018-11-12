package com.example.studentdailyapp.studentdaily.Model;

public class Courses {

    public String id_subject,subject,yearSem_id;
    public int color;

    public Courses(){}

    public Courses(String id_subject, String subject,  String yearSem_id, int color) {
        this.id_subject = id_subject;
        this.subject = subject;
        this.color = color;
        this.yearSem_id = yearSem_id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getId_subject() {
        return id_subject;
    }

    public void setId_subject(String id_subject) {
        this.id_subject = id_subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getYearSem_id() {
        return yearSem_id;
    }

    public void setYearSem_id(String yearSem_id) {
        this.yearSem_id = yearSem_id;
    }
}
