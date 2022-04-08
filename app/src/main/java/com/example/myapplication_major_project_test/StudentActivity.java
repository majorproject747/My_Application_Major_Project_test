package com.example.myapplication_major_project_test;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    RecyclerView rVfaculty;
    FacultyAdapter facultyAdapter;
    List<Faculty> flist;
    ImageView mIvback;
    String value;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        rVfaculty = findViewById(R.id.rv_faculties);
        rVfaculty.setHasFixedSize(true);
        rVfaculty.setLayoutManager(new LinearLayoutManager(StudentActivity.this));
        mIvback = findViewById(R.id.iv_flist_back);
        intent = getIntent();
        value = intent.getStringExtra("MODE");

        mIvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(value.equals("Faculty")){
                    startActivity(new Intent(StudentActivity.this,FacultyActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(StudentActivity.this,MainActivity.class));
                    finish();
                }
            }
        });

        flist = new ArrayList<>();
        readList();
    }

    private void readList() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Faculty");
        Query query = reference.orderByChild("name");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Faculty user = dataSnapshot.getValue(Faculty.class);

                    assert firebaseUser != null;
                    /*if (!user.getId().equals(firebaseUser.getUid())){
                        flist.add(user);
                    }*/
                    flist.add(user);
                }

                facultyAdapter = new FacultyAdapter(StudentActivity.this,flist);

                rVfaculty.setAdapter(facultyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}