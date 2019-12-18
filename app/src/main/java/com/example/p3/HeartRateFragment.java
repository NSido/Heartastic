package com.example.p3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class HeartRateFragment extends Fragment {
    private Button mMeasureButton;
    private Button mInstructButton;
    private TextView mNameTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;
    Date mDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heartrate, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mNameTextView = view.findViewById(R.id.textview_name);
        mInstructButton = view.findViewById(R.id.instruct_button);
        mMeasureButton = view.findViewById(R.id.measure_button);

        if (mUser != null) {
            String name = mUser.getDisplayName();
            mNameTextView.setText(name);
        }


        mInstructButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopUp.class);
                startActivity(intent);
            }
        });


        mMeasureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConnectionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}