package com.example.bingkunyang.sleepaffect.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bingkunyang.sleepaffect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bingkunyang on 4/1/18.
 */

public class ConfirmFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("confirmations");

    public static ConfirmFragment newInstance() {
        return new ConfirmFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_confirm, container, false);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.confirm_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}



