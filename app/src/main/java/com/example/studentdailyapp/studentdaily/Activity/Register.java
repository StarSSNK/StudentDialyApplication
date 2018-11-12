package com.example.studentdailyapp.studentdaily.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.example.studentdailyapp.studentdaily.Dashboard;
import com.example.studentdailyapp.studentdaily.Model.Members;
import com.example.studentdailyapp.studentdaily.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private EditText et_username_regis;
    private EditText et_email_regis;
    private EditText et_pass_regis;
    private Button btn_regis;

    FirebaseAuth firebaseAuth;
    DatabaseReference memberDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();


        et_username_regis = (EditText) findViewById(R.id.username_regis);
        et_email_regis = (EditText) findViewById(R.id.email_regis);
        et_pass_regis = (EditText) findViewById(R.id.pass_regis);
        btn_regis = (Button) findViewById(R.id.regis_btn);


        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }


    public void register() {
        final String username = et_username_regis.getText().toString().trim();
        final String email = et_email_regis.getText().toString().trim();
        final String pass = et_pass_regis.getText().toString().trim();
        final Boolean activated = true;


        Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);


        if (TextUtils.isEmpty(username)) {
            et_username_regis.setError("Please Fill your username");
            et_username_regis.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            et_email_regis.setError("Please Fill your email");
            et_email_regis.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(pass)) {
            et_pass_regis.setError("Please Fill your password");
            et_pass_regis.requestFocus();
            return;
        }
        else if (!matcher.matches()) {
            et_email_regis.setError("Email address is wrong, Please try again");
            et_email_regis.requestFocus();
            return;
        }
        else if (pass.length() < 6) {
            et_pass_regis.setError("Password length should more than 6");
            et_pass_regis.requestFocus();
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Processing", "Please waiting...", true);

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    memberDb = FirebaseDatabase.getInstance().getReference("Members");
                    String id_user = firebaseAuth.getCurrentUser().getUid().toString();

                    Members members = new Members(email, username, activated, id_user);

                    memberDb.child(id_user).setValue(members).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Welcome to STD Application", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Could not Register, Please try again", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
