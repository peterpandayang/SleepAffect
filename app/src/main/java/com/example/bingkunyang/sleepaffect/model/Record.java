package com.example.bingkunyang.sleepaffect.model;

/**
 * Created by bingkunyang on 4/1/18.
 */

public class Record {
    public String email;
    public String time;

    public Record(String email, String time){
        this.email = email;
        this.time = time;
    }

    public Record(){}

    public String getEmail(){
        return email;
    }

    public String getTime(){
        return time;
    }
}

