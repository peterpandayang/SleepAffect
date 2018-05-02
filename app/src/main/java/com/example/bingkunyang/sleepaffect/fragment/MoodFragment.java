package com.example.bingkunyang.sleepaffect.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bingkunyang.sleepaffect.R;
import com.example.bingkunyang.sleepaffect.model.Record;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by bingkunyang on 4/9/18.
 */

public class MoodFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ImageView imageView;
    List<Record> records = new LinkedList<>();
    private Button showMoodBtn;
    MediaPlayer mediaPlayer;
    private TextView textView;

    public static MoodFragment newInstance() {
        return new MoodFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference("confirmations");
        auth = FirebaseAuth.getInstance();
        final String userEmail = auth.getCurrentUser().getEmail();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Map map = (Map) data.getValue();
                    Record record = new Record((String)map.get("email"), (String)map.get("time"));
                    if(record.email.equals(userEmail)){
                        records.add(record);
                        System.out.println("add data to records");
                        System.out.println("records are: " + records);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_mood, container, false);
        showMoodBtn = (Button)rootView.findViewById(R.id.show_mood);
        imageView = (ImageView) rootView.findViewById(R.id.puppy_image);

        mediaPlayer = MediaPlayer.create(this.getContext(), R.raw.happy_puppy);
//        textView = rootView.findViewById(R.id.text_view_id);

        // set on click listener
        showMoodBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showMode();
//                showText();
            }
        });

        return rootView;
    }



    private void showMode() {
        // retrieve the user's previous sleep
        if(records.size() == 0){
            System.out.println("there are no records");
            imageView.setImageResource(R.drawable.puppy_5);
        }
        else if(records.size() == 1){
            System.out.println("there is only one record for the user");
            imageView.setImageResource(R.drawable.puppy_5);
            mediaPlayer.start();
        }
        else{
            // poll out two record and compare
            System.out.println("there are multiple record for the user");
            int size = records.size();
            Record prevprev = records.get(size - 2);
            Record prev = records.get(size - 1);
            System.out.println("prevprev date is: " + prevprev.getTime());
            System.out.println("prev date is: " + prev.getTime());
            helper(imageView, prevprev.getTime(), prev.getTime());
        }
    }

    private void showText() {
        // retrieve the user's previous sleep
        if(records.size() == 0){
            System.out.println("there are no records");
//            imageView.setImageResource(R.drawable.puppy_5);
        }
        else if(records.size() == 1){
            System.out.println("there is only one record for the user");
            imageView.setImageResource(R.drawable.puppy_5);
        }
        else{
            // poll out two record and compare
            System.out.println("there are multiple record for the user");
            int size = records.size();
            Record prevprev = records.get(size - 2);
            Record prev = records.get(size - 1);
            System.out.println("prevprev date is: " + prevprev.getTime());
            System.out.println("prev date is: " + prev.getTime());
//            helper(imageView, prevprev.getTime(), prev.getTime());
        }
    }

    private void helper(ImageView imageView, String prevprev, String prev){
        boolean ppGood = getBoolean(prevprev);
        boolean pGood = getBoolean(prev);
        if(ppGood && pGood){
            imageView.setImageResource(R.drawable.puppy_8);
            mediaPlayer.start();
        }
        else if(!ppGood && pGood){
            imageView.setImageResource(R.drawable.puppy_7);
            mediaPlayer.start();
        }
        else if(ppGood && !pGood){
            imageView.setImageResource(R.drawable.puppy_3);
        }
        else{
            int prevprevHour = Integer.parseInt(prevprev.split(" ")[3].split(":")[0]);
            int prevHour = Integer.parseInt(prev.split(" ")[3].split(":")[0]);
            if(prevHour <= prevprevHour){
                imageView.setImageResource(R.drawable.puppy_5);
            }
            else {
                imageView.setImageResource(R.drawable.puppy_1);
            }
        }
    }

    private boolean getBoolean(String date){
        String time = date.split(" ")[3];
        System.out.println("time is : " + time);
        String hour = time.split(":")[0];
        System.out.println("hour is: " + hour);
        return Integer.parseInt(hour) > 9;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

}
