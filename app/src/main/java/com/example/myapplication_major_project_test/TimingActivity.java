package com.example.myapplication_major_project_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TimingActivity extends AppCompatActivity {

    TextInputLayout mEtClass;
    TextInputLayout mEtStime;
    TextInputLayout mEtEtime;
    TextView mBtnSubmit;
    DatabaseReference mDatabase;
    int counter=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);

        mEtClass = findViewById(R.id.til_class);
        mEtStime = findViewById(R.id.til_start_time);
        mEtEtime = findViewById(R.id.til_end_time);
        mBtnSubmit = findViewById(R.id.tv_submit_details);


        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String class_n = mEtClass.getEditText().getText().toString();
                String starttime = mEtStime.getEditText().getText().toString();
                String endtime = mEtEtime.getEditText().getText().toString();
                String duration = starttime + " - " + endtime;
                if (class_n.isEmpty()) {
                    mEtClass.setError("Field cannot be Empty");
                } else if (starttime.isEmpty()) {
                    mEtStime.setError("Field cannot be Empty");
                } else if (endtime.isEmpty()) {
                    mEtEtime.setError("Field cannot be Empty");
                } else {
                    String userId = FirebaseAuth.getInstance().getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Faculty").child(userId).child("status").setValue("Available");
                    mDatabase.child("Faculty").child(userId).child("class_n").setValue(class_n);
                    mDatabase.child("Faculty").child(userId).child("duration").setValue(duration);
                    Toast.makeText(TimingActivity.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TimingActivity.this, FacultyActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        counter++;
        if(counter>0){
            startActivity(new Intent(TimingActivity.this,FacultyActivity.class));
            finish();
        }
        super.onBackPressed();
    }
}