package com.example.p3;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText mEmail, mPassword, mName;
    private Button mSignUp;
    private TextView tvSignIn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();
        mName = findViewById(R.id.name_edittext_signup);
        mEmail = findViewById(R.id.email_edittext_signup);
        mPassword = findViewById(R.id.password_edittext_signup);
        tvSignIn = findViewById(R.id.already_have_account_textview);
        mSignUp = findViewById(R.id.signup_button);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        register();
    }

    protected void register(){
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                if (name.isEmpty()) {
                    mName.setError("Please enter your name");
                    mName.requestFocus();
                } else if (email.isEmpty()) {
                    mEmail.setError("Please enter your email");
                    mEmail.requestFocus();
                } else if (password.isEmpty()) {
                    mPassword.setError("Please enter your password");
                    mPassword.requestFocus();
                } else if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fil all the fields!", Toast.LENGTH_SHORT);
                } else if (!(name.isEmpty() && email.isEmpty() && password.isEmpty())) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                            writeNewUser(mAuth.getUid(),mName.getText().toString(),mEmail.getText().toString());
                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));

                            } else {

                                Toast.makeText(SignUpActivity.this, "Sign up error", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT);
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


    }

    private void writeNewUser(String userId, String name, String email) {
        UserProfile user = new UserProfile(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}
