package com.example.agenda;

public class Work {
    private String work;
    private String time;






    public Work() {}

    public Work(String work, String time) {
        this.work = work;
        this.time = time;
    }

    public String getWork() {
        return work;
    }

    public String getTime() {
        return time;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
