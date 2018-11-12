package com.example.studentdailyapp.studentdaily.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.studentdailyapp.studentdaily.Dashboard;
import com.example.studentdailyapp.studentdaily.R;

public class PendingIntentTask extends AppCompatActivity {

    private TextView title;
    private TextView detail;
    private TextView type;
    private TextView classroom;
    private TextView dueDate;
    private TextView dueTime;
    private TextView backDash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_intent_task);

        title = (TextView) findViewById(R.id.title);
        detail = (TextView) findViewById(R.id.detail);
        type = (TextView) findViewById(R.id.type);
        classroom = (TextView) findViewById(R.id.classroom);
        dueDate = (TextView) findViewById(R.id.date);
        dueTime = (TextView) findViewById(R.id.time);
        backDash = (TextView) findViewById(R.id.backDash);


        Bundle bundle = getIntent().getExtras();
        String titleTask = bundle.getString("title");
        String detailTask = bundle.getString("detail");
        String typeTask = bundle.getString("type");
        String classroomTask = bundle.getString("classroom");
        String dueDateTask = bundle.getString("dueDate");
        String dueTimeTask = bundle.getString("dueTime");

        title.setText(titleTask);
        detail.setText(detailTask);
        type.setText(typeTask);
        classroom.setText(classroomTask);
        dueDate.setText(dueDateTask);
        dueTime.setText(dueTimeTask);

        backDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });


    }
}
