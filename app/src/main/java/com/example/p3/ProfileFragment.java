package com.example.p3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    FirebaseAuth mFirebaseAuth;
    Button mSignOutButton;
    Button mUpdateProfile;
    FirebaseUser user;
    EditText mEmail;
    EditText mPassword;
    EditText mName;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mSignOutButton = view.findViewById(R.id.button);
        mUpdateProfile = view.findViewById(R.id.button_update_profile);
        mEmail = view.findViewById(R.id.edittext_email);
        mPassword = view.findViewById(R.id.edittext_password);
        mName = view.findViewById(R.id.edittext_name);

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();


            mName.setText(name);
            mEmail.setText(email);






        }

        mUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPassword.getText().toString().isEmpty()) {
                    if (user != null) {
                        user.updatePassword("dinfarer")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Password Could not be updated", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getActivity(), "Field is empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        return view;


    }




}


