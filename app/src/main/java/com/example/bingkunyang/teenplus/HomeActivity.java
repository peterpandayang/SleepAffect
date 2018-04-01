package com.example.bingkunyang.teenplus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.bingkunyang.teenplus.com.example.bingkunyang.teenplus.fragment.ConfirmFragment;
import com.example.bingkunyang.teenplus.com.example.bingkunyang.teenplus.model.Record;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by bingkunyang on 3/14/18.
 */


public class HomeActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(ConfirmFragment.newInstance());
        toolbar.setTitle("Confirming");
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
                case R.id.navigation_confirm:
                    toolbar.setTitle("Confirming");
                    fragment = ConfirmFragment.newInstance();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_feed:
                    toolbar.setTitle("Feed");
                    return true;
                case R.id.navigation_search:
                    toolbar.setTitle("Search");
                    return true;
                case R.id.navigation_notifications:
                    toolbar.setTitle("Notifications");
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    return true;
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
        builder.setTitle("Create your confirmation")
                .setPositiveButton("confirm sleep", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        String text = currentTime.toString();
                        Record record = new Record(email, text);
                        mDatabase.child("confirmations").push().setValue(record);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}