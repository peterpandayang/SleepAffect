package com.example.bingkunyang.teenplus.com.example.bingkunyang.teenplus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bingkunyang.teenplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bingkunyang on 4/9/18.
 */

public class MoodFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ImageView imageView;

    public static MoodFragment newInstance() {
        return new MoodFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_mood, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.puppy_image);
        setImage();
        return rootView;
    }

    private void setImage(){
        imageView.setImageResource(R.drawable.puppy_8);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

}
