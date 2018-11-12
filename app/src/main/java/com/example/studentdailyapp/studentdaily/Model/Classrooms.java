package com.example.studentdailyapp.studentdaily.Model;

public class Classrooms {

    public String room,building,period,start_time,end_time,date,place,teacher,course;

    public Classrooms(){}

    public Classrooms(String room, String building, String period, String start_time, String end_time, String date, String place, String teacher, String course) {
        this.room = room;
        this.building = building;
        this.period = period;
        this.start_time = start_time;
        this.end_time = end_time;
        this.date = date;
        this.place = place;
        this.teacher = teacher;
        this.course = course;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
