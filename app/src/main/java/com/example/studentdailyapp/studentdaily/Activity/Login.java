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

import com.example.studentdailyapp.studentdaily.Admin;
import com.example.studentdailyapp.studentdaily.Dashboard;
import com.example.studentdailyapp.studentdaily.Model.Members;
import com.example.studentdailyapp.studentdaily.NotificationTasks;
import com.example.studentdailyapp.studentdaily.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText et_email_login;
    private EditText et_pass_login;
    private Button btn_login;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference memberDb;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();

        et_email_login = (EditText) findViewById(R.id.email_login);
        et_pass_login = (EditText) findViewById(R.id.pass_login);

        btn_login = (Button) findViewById(R.id.login_btn);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = ProgressDialog.show(Login.this, "Processing", "Please waiting...", true);

                final String email_login = et_email_login.getText().toString();
                final String pass_login = et_pass_login.getText().toString();

                if (TextUtils.isEmpty(email_login)) {
                    et_email_login.setError("Please Fill your email");
                    et_email_login.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pass_login)) {
                    et_pass_login.setError("Please Fill your password");
                    et_pass_login.requestFocus();
                    return;
                }
                String email_admin = "AdminStd@gmail.com";
                String password_admin = "adminstd12345";

                if (email_login.equals(email_admin) && pass_login.equals(password_admin)) {
                    startActivity(new Intent(getApplicationContext(), Admin.class));
                } else {
                    memberDb = FirebaseDatabase.getInstance().getReference("Members");
                    memberDb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                Boolean activated = (Boolean) dataSnapshot1.child("activated").getValue();
                                String email = dataSnapshot1.child("email").getValue().toString();

                                if (email.equals(email_login)){
                                    if (activated.equals(false)) {
                                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        break;
                                    }else {
                                        login(email_login,pass_login);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    public void login(final String email_login, final String pass_login) {
        firebaseAuth.signInWithEmailAndPassword(email_login, pass_login).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Welcome to STD Application", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));

                } else {
                    Toast.makeText(getApplicationContext(), "Could not login, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
