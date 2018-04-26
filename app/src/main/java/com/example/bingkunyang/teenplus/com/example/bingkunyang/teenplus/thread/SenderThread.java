package com.example.bingkunyang.teenplus.com.example.bingkunyang.teenplus.thread;

import android.app.Activity;

import com.example.bingkunyang.teenplus.com.example.bingkunyang.teenplus.model.Record;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by bingkunyang on 4/24/18.
 */

public class SenderThread extends Thread {
    String text;
    Record record;
    DatabaseReference mDatabase;
    ConcurrentLinkedQueue<Date> timeQ;
    ConcurrentLinkedQueue<Float> dataQ;
    float sum;

    public SenderThread(String text, Record record, DatabaseReference mDatabase, ConcurrentLinkedQueue<Date> timeQ, ConcurrentLinkedQueue<Float> dataQ, float sum) {
        this.text = text;
        this.record = record;
        this.mDatabase = mDatabase;
        this.timeQ = timeQ;
        this.dataQ = dataQ;
        this.sum = sum;
    }

    private float getSum(){
        sum = 0;
        for(float f : dataQ){
            sum += f;
        }
        return sum;
    }

    @Override
    public void run() {
    // before sending to the database do the check
        while(true){
            if(timeQ.size() > 0){
                Date start = timeQ.peek();
                Date curr = new Date();
//                System.out.println(dateFormat.format(curr));
                sum = getSum();
                long diff = (curr.getTime() - start.getTime()) / 1000;
                System.out.println("time diff is:" + diff);
                float avg = sum / timeQ.size();
                System.out.println("light value is: " + avg);
                if(diff > 60 && avg < 60){
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        mDatabase.child("confirmations").push().setValue(record);
    }
}
