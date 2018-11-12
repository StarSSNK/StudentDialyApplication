package com.example.studentdailyapp.studentdaily.Model;

public class YearSems {
    public String yearSem,start_date,end_date;

    public YearSems(){}


    public YearSems(String yearSem, String start_date, String end_date) {
        this.yearSem = yearSem;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getYearSem() {
        return yearSem;
    }

    public void setYearSem(String yearSem) {
        this.yearSem = yearSem;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}

