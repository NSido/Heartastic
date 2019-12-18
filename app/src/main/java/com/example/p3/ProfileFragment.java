package com.example.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileFragment extends Fragment {

    FirebaseAuth mFirebaseAuth;
    Button mSignOutButton;
    Button mUpdateProfile;
    FirebaseUser user;
    TextView mEmail;
    TextView mPassword;
    TextView mName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mSignOutButton = view.findViewById(R.id.button);
        mUpdateProfile = view.findViewById(R.id.button_update_profile);
        mEmail = view.findViewById(R.id.edittext_email);
        mPassword = view.findViewById(R.id.edittext_password);
        mName = view.findViewById(R.id.edittext_name);

        if (user != null) {
            // Name, email address
            String name = user.getDisplayName();
            String email = user.getEmail();


            mName.setText(name);
            mEmail.setText(email);

            final String[] options = {"Change name", "Change password"};
            mUpdateProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("What would you like to change?");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            // the user clicked on options[which]
                            if (which == 0) {
                                final AlertDialog.Builder mBuilderName = new AlertDialog.Builder(getActivity());
                                View mViewName = getLayoutInflater().inflate(R.layout.dialog_name, null);
                                final EditText newName = mViewName.findViewById(R.id.edittext_name_dialog);
                                Button changeName = mViewName.findViewById(R.id.button_change_name);

                                changeName.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!newName.getText().toString().isEmpty()) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(newName.getText().toString()).build();

                                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(),
                                                                R.string.name_updated,
                                                                Toast.LENGTH_LONG).show();
                                                        mName.setText(newName.getText().toString());

                                                    } else {
                                                        Toast.makeText(getActivity(),
                                                                "Name could not be updated. Try Again!",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getActivity(), "Please enter a name", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                mBuilderName.setView(mViewName);
                                AlertDialog alertDialogName = mBuilderName.create();
                                alertDialogName.show();


                            } else if (which == 1) {
                                AlertDialog.Builder mBuilderPassword = new AlertDialog.Builder(getActivity());
                                View mViewPassword = getLayoutInflater().inflate(R.layout.dialog_password, null);
                                final EditText oldPassword = mViewPassword.findViewById(R.id.edittext_password_dialog1);
                                final EditText newPassword1 = mViewPassword.findViewById(R.id.edittext_password_dialog2);
                                final EditText newPassword2 = mViewPassword.findViewById(R.id.edittext_password_dialog3);
                                Button changePassword = mViewPassword.findViewById(R.id.button_change_password);

                                changePassword.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (oldPassword.getText().toString().isEmpty()
                                                || newPassword2.getText().toString().isEmpty()
                                                || newPassword1.getText().toString().isEmpty()) {
                                            Toast.makeText(getActivity(),
                                                    R.string.fill_all,
                                                    Toast.LENGTH_SHORT)
                                                    .show();

                                        } else if (newPassword1.getText().toString().equals(newPassword2.getText().toString())) {
                                            AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),
                                                    oldPassword.getText().toString());
                                            user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        user.updatePassword(newPassword2.getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getActivity(),
                                                                                    "Password changed",
                                                                                    Toast.LENGTH_SHORT).show();

                                                                        } else {
                                                                            Toast.makeText(getActivity(),
                                                                                    "Password could not change, try again!",
                                                                                    Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                    } else {
                                                        Toast.makeText(getActivity(),
                                                                "Enter your correct old password",
                                                                Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

                                        } else {
                                            Toast.makeText(getActivity(),
                                                    "Passwords doesn't match",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                });

                                mBuilderPassword.setView(mViewPassword);
                                AlertDialog alertDialogName = mBuilderPassword.create();
                                alertDialogName.show();

                            }
                        }
                    });
                    builder.show();
                }
            });
        }

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


