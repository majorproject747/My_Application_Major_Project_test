package com.example.myapplication_major_project_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout mTilSignName;
    TextInputLayout mTilSignEmail;
    TextInputLayout mTilSignPass;
    TextView mBtnSignUp;

    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference,reference_faculty;
    RadioGroup mRgRole;
    RadioButton mRbRole;
    String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mTilSignEmail = findViewById(R.id.til_signup_email);
        mTilSignName = findViewById(R.id.til_signup_name);
        mTilSignPass = findViewById(R.id.til_signup_password);
        mBtnSignUp = findViewById(R.id.btn_signup);
        mRgRole = findViewById(R.id.rg_role_select);

        mAuth = FirebaseAuth.getInstance();

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateFullName() | !validateEmail() | !validatePassword()) {
                    return;
                }

                String email = mTilSignEmail.getEditText().getText().toString();
                String name = mTilSignName.getEditText().getText().toString();
                String password = mTilSignPass.getEditText().getText().toString();
                int roleid = mRgRole.getCheckedRadioButtonId();
                mRbRole = findViewById(roleid);
                role = mRbRole.getText().toString();

                register(email, name, password, role);
            }
        });
    }

    private void register(String email, String name, String password, String role) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            reference = database.getReference("User").child(userId);
                            if(role.equals("Faculty")){
                                reference_faculty = database.getReference("Faculty").child(userId);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("name", name);
                                hashMap1.put("status","Default");
                                hashMap1.put("id",userId);
                                hashMap1.put("duration","Default");
                                hashMap1.put("class_n","Default");

                                reference_faculty.setValue(hashMap1);
                            }

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("username", name);
                            hashMap.put("role", role);
                            hashMap.put("id",userId);


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Database failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SignUpActivity.this, "You can't register with this email or password" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private boolean validatePassword() {
        String val = mTilSignPass.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                "(?=.*[0-9])" +           //at least 1 digit
                "(?=.*[a-z])" +           //at least 1 lower case letter
                "(?=.*[A-Z])" +           //at least 1 upper case letter
//                "(?=.*[a-zA-Z])" +        //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "(?=S+$)" +               //no white spaces
                ".{4,}" +                 //at least 4 characters
                "$";

        if (val.isEmpty()) {
            mTilSignPass.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            mTilSignPass.setError("*Password must contain min. 4 character. 1 uppercase and 1 lowercase,1 number at least");
            return false;
        } else {
            mTilSignPass.setError(null);
            mTilSignPass.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = mTilSignEmail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            mTilSignEmail.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            mTilSignEmail.setError("Invalid Email!");
            return false;
        } else {
            mTilSignEmail.setError(null);
            mTilSignEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateFullName() {
        String val = mTilSignName.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            mTilSignName.setError("Field can not be empty");
            return false;
        } else {
            mTilSignName.setError(null);
            mTilSignName.setErrorEnabled(false);
            return true;
        }
    }


    public void move_login_page(View view) {
        Intent intent  = new Intent(SignUpActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}