package com.example.myapplication_major_project_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultyActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    TextView mTvfName;
    TextView mTvfclass;
    TextView mTvftime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        mTvfName = findViewById(R.id.tv_timing_facultyname);
        mTvfclass = findViewById(R.id.tv_faculty_class);
        mTvftime = findViewById(R.id.tv_faculty_timing);

        FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Faculty");
        mDatabase.addValueEventListener(new ValueEventListener() {
            String name, classn, timing, status;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Faculty user = dataSnapshot.getValue(Faculty.class);
                    if(user.getId().equals(firebaseuser.getUid())){
                        assert user != null;
                        name = user.getName();
                        classn = user.getClass_n();
                        timing = user.getDuration();
                        status = user.getStatus();
                        if(status.equals("Default")){
                            mTvfName.setText("No Entry Available");
                            mTvfclass.setVisibility(View.GONE);
                            mTvftime.setVisibility(View.GONE);
                        }
                        else if(status.equals("Leave")){
                            mTvfName.setText(name+" - On leave");
                            mTvfclass.setVisibility(View.GONE);
                            mTvftime.setVisibility(View.GONE);
                        }
                        else{
                            mTvfName.setText(name);
                            mTvfclass.setText(classn);
                            mTvftime.setText(timing);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void leave_click(View view) {
        String userId = FirebaseAuth.getInstance().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Faculty").child(userId).child("status").setValue("Leave");
        mDatabase.child("Faculty").child(userId).child("class_n").setValue("Default");
        mDatabase.child("Faculty").child(userId).child("duration").setValue("Default");
    }

    public void avail_click(View view) {
        Intent intent = new Intent(FacultyActivity.this,TimingActivity.class);
        startActivity(intent);
        finish();
    }

    public void move_faculty_list(View view) {
        Intent intent = new Intent(FacultyActivity.this,StudentActivity.class);
        intent.putExtra("MODE","Faculty");
        startActivity(intent);
        finish();
    }
}