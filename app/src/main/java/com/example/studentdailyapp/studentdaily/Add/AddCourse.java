package com.example.studentdailyapp.studentdaily.Add;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentdailyapp.studentdaily.Dashboard;
import com.example.studentdailyapp.studentdaily.Model.Courses;
import com.example.studentdailyapp.studentdaily.Model.YearSems;
import com.example.studentdailyapp.studentdaily.R;
import com.example.studentdailyapp.studentdaily.Recyclerview.Course_Fragment;
import com.example.studentdailyapp.studentdaily.Recyclerview.YearSem_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import yuku.ambilwarna.AmbilWarnaDialog;


public class AddCourse extends AppCompatActivity {

    private EditText et_id_subject;
    private EditText et_subject;
    private TextView _color;
    private Button btn_add_submit;
    private Button btn_add_cancle;
    int colorDefault;
    private DatabaseReference courseDb;
    private String currentUser;
    private AutoCompleteTextView et_yearSem;
    private ArrayAdapter<String> yearSemAdapter;
    private DatabaseReference yearSemDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);


        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        courseDb = FirebaseDatabase.getInstance().getReference("Courses").child(currentUser);

        et_id_subject = (EditText) findViewById(R.id.id_subject);
        et_subject = (EditText) findViewById(R.id.subject);
        _color = (TextView) findViewById(R.id.color);

        btn_add_submit = (Button) findViewById(R.id.submit_add_course);
        btn_add_cancle = (Button) findViewById(R.id.cancle_add_course);
        colorDefault = ContextCompat.getColor(this,R.color.colorPrimary);
        et_yearSem = (AutoCompleteTextView) findViewById(R.id.yearSem_autoCom);


        _color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPicker();
            }
        });

        btn_add_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        btn_add_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id_subject = et_id_subject.getText().toString();
                String subject = et_subject.getText().toString();
                int color = colorDefault;
                String id_yearSem = et_yearSem.getText().toString();


                Courses courses = new Courses(id_subject, subject, id_yearSem, color);

                if (TextUtils.isEmpty(id_subject)) {
                    et_id_subject.setError("Please insert id subject");
                } else if (TextUtils.isEmpty(subject)) {
                    et_subject.setError("Please insert subject");
                } else {

                    courseDb.child(id_subject).setValue(courses).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Add new Course Successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        et_yearSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_yearSem();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void select_yearSem() {

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        yearSemDB = FirebaseDatabase.getInstance().getReference("YearSems").child(currentUser);


        yearSemDB.addValueEventListener(new ValueEventListener() {

            public ArrayList<String> yearSemList;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yearSemList = new ArrayList<String>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    YearSems yearSems = dataSnapshot1.getValue(YearSems.class);
                    String yearsem = yearSems.getYearSem().toString();
                    yearSemList.add(yearsem);
                }
                yearSemAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_dropdown_item_1line, yearSemList);
                et_yearSem.setAdapter(yearSemAdapter);
                et_yearSem.showDropDown();
                et_yearSem.setThreshold(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void colorPicker() {
        AmbilWarnaDialog warnaDialog = new AmbilWarnaDialog(this, colorDefault, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                startActivity(new Intent(getApplicationContext(), AddCourse.class));
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                colorDefault = color;
                _color.setBackgroundColor(colorDefault);
            }
        });
        warnaDialog.show();
    }




}
