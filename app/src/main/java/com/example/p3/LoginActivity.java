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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitvity);

        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.email_edittext_login);
        mPassword = findViewById(R.id.password_edittext_login);
        tvRegister = findViewById(R.id.register_textview);
        mLogin = findViewById(R.id.login_button);

       login();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    protected void login(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this, "Logged in successfully",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please Login",Toast.LENGTH_SHORT);
                }
            }
        };

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                if(email.isEmpty()){
                    mEmail.setError("Please enter your email");
                    mEmail.requestFocus();
                }

                else if(password.isEmpty()){
                    mPassword.setError("Please enter your password");
                    mPassword.requestFocus();
                }

                else if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please fil all the fields!",Toast.LENGTH_SHORT).show();
                }

                else if(!(email.isEmpty() && password.isEmpty())){
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login error",Toast.LENGTH_SHORT).show();

                            }
                            else {
                            Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intToHome);
                        }}
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "error",Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intRegister =  new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intRegister);
            }
        });


    }


}
