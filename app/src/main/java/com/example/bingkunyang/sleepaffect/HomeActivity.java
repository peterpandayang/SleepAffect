package com.example.bingkunyang.sleepaffect;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bingkunyang.sleepaffect.fragment.ConfirmFragment;
import com.example.bingkunyang.sleepaffect.fragment.FeedFragment;
import com.example.bingkunyang.sleepaffect.fragment.MoodFragment;
import com.example.bingkunyang.sleepaffect.fragment.ProfileFragment;
import com.example.bingkunyang.sleepaffect.model.Record;
import com.example.bingkunyang.sleepaffect.thread.SenderThread;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by bingkunyang on 3/14/18.
 */


public class HomeActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private static ConcurrentLinkedQueue<Float> dataQ = new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<Date> timeQ = new ConcurrentLinkedQueue<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private float sum = 0;
    private static ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

//    private boolean clicked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(MoodFragment.newInstance());
        toolbar.setTitle("Mood for Today");
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_mood:
                    toolbar.setTitle("Mood for Today");
                    loadFragment(MoodFragment.newInstance());
                    return true;
                case R.id.navigation_feed:
                    toolbar.setTitle("Feed");
                    loadFragment(FeedFragment.newInstance());
                    return true;
                case R.id.navigation_confirm:
                    toolbar.setTitle("Confirm to sleep");
                    loadFragment(ConfirmFragment.newInstance());
                    return true;
//                case R.id.navigation_profile:
//                    toolbar.setTitle("Profile");
//                    loadFragment(ProfileFragment.newInstance());
//                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_new_confirmation:
                return true;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
                return true;
        }

        return false;
    }

    public void createConfirmation(MenuItem item) {
//        final EditText input = new EditText(HomeActivity.this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        input.setLayoutParams(lp);

        final String email = auth.getCurrentUser().getEmail();
        final Date currentTime = Calendar.getInstance().getTime();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Create TextView
        final TextView lightText = new TextView (this);
        lightText.setPadding(40, 40, 40, 40);
        lightText.setGravity(Gravity.CENTER);
        lightText.setTextSize(20);

        dataQ = new ConcurrentLinkedQueue<>();
        timeQ = new ConcurrentLinkedQueue<>();
        sum = 0;
//        clicked = false;

        /*
        * source: https://stackoverflow.com/questions/17411562/android-light-sensor-detect-significant-light-changes
        * */
        final SensorEventListener lightSensorListener = new SensorEventListener(){
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                    lightText.setText("LIGHT: " + event.values[0]);
//                    System.out.println("the changed value is : " + event.values[0]);
                    Date date = new Date();
                    dataQ.offer(event.values[0]);
                    timeQ.offer(date);
                    sum += event.values[0];
                }
            }
        };

        SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(LightSensor != null){
            System.out.println("has light sensor");
            lightText.setText("Sensor.TYPE_LIGHT Available");
            mySensorManager.registerListener(
                    lightSensorListener,
                    LightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }else{
            System.out.println("no light sensor");
            lightText.setText("Sensor.TYPE_LIGHT NOT Available");
        }

        builder.setTitle("Create your confirmation")
                .setView(lightText)
                .setPositiveButton("confirm sleep", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        String text = currentTime.toString();
                        Record record = new Record(email, text);

                        SenderThread thread = new SenderThread(text, record, mDatabase, timeQ, dataQ, sum);
                        threadPool.execute(thread);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}