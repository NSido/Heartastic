package com.example.p3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class HeartRateFragment extends Fragment {
       private Button mMeasureButton;
       private FirebaseAuth mAuth;
       private DatabaseReference mDatabaseRef;
       private FirebaseUser mUser;
       Date mDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_heartrate,container,false);

        ArrayList<Integer> hrData = new ArrayList<>();

        for (int i = 1; i <= 100; i++){
            hrData.add(i);
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDate = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        String date = simpleDateFormat.format(mDate);

        Log.d(TAG, "onCreateView: " + date);

        if (mUser != null){
        for(Integer hrRate : hrData) {
            mDatabaseRef.child("UserHeartRateData").child(mAuth.getUid()).child(date).setValue(hrData);
        }
        }

        mMeasureButton = view.findViewById(R.id.measure_button);
        mMeasureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HtRe.class);
                startActivity(intent);
            }
        });

        return view;
    }
}