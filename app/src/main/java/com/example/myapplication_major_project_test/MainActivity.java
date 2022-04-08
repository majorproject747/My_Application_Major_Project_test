package com.example.myapplication_major_project_test;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextInputLayout mLoginEmail;
    TextInputLayout mLoginPass;
    TextView mTvLoginForgot;
    TextView mBtnLogin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginEmail = findViewById(R.id.til_email);
        mLoginPass = findViewById(R.id.til_password);
        mTvLoginForgot = findViewById(R.id.tv_forgot_pass);
        mBtnLogin = findViewById(R.id.btn_login);


        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.tv_forgot_pass).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String resetEmail = mLoginEmail.getEditText().getText().toString();
                                    if (resetEmail.isEmpty()) {
                                        Toast.makeText(MainActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mAuth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainActivity.this, "Reset Mail sent successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPass.getEditText().getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Successful Login",Toast.LENGTH_SHORT).show();
                            String userId = FirebaseAuth.getInstance().getUid();
                            FirebaseDatabase.getInstance().getReference("User").child(userId)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String role = snapshot.child("role").getValue(String.class);
                                            String curId = snapshot.child("id").getValue(String.class);
                                            if(role.equals("Student") && userId.equals(curId)){
                                                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                            else if(role.equals("Faculty") && userId.equals(curId))
                                            {
                                                Intent intent = new Intent(MainActivity.this, FacultyActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }else {
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void move_SignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}