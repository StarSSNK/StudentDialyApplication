package com.example.studentdailyapp.studentdaily.Add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.studentdailyapp.studentdaily.Dashboard;
import com.example.studentdailyapp.studentdaily.Model.Classrooms;
import com.example.studentdailyapp.studentdaily.Model.Courses;
import com.example.studentdailyapp.studentdaily.R;
import com.example.studentdailyapp.studentdaily.Recyclerview.YearSem_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddClassroom extends AppCompatActivity {

    private EditText et_room;
    private EditText et_building;
    private EditText et_place;
    private EditText et_teacher;
    private Button btn_submit;
    private Button btn_cancle;
    private TextView tv_show_start_time;
    private TextView tv_show_end_time;
    private TextView tv_show_day;
    private DatabaseReference classroomDB;
    private FirebaseUser firebaseAuth;
    private TextView m;
    private TextView t;
    private TextView w;
    private TextView th;
    private TextView f;
    private TextView sat;
    private TextView s;
    private AutoCompleteTextView ac_course;
    private DatabaseReference courseDB;
    private ArrayAdapter<String> courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classroom);

        classroomDB = FirebaseDatabase.getInstance().getReference("Classrooms");
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();

        et_room = (EditText) findViewById(R.id.room);
        et_building = (EditText) findViewById(R.id.building);
        ac_course = (AutoCompleteTextView)findViewById(R.id.sl_course);
        et_place = (EditText) findViewById(R.id.place);
        et_teacher = (EditText) findViewById(R.id.teacher);
        tv_show_start_time = (TextView) findViewById(R.id.start_time_show);
        tv_show_end_time = (TextView) findViewById(R.id.end_time_show);
        tv_show_day = (TextView) findViewById(R.id.date_show);
        btn_submit = (Button) findViewById(R.id.submit_classroom);
        btn_cancle = (Button) findViewById(R.id.cancle_classroom);


        final MaterialSpinner period_spinner = (MaterialSpinner) findViewById(R.id.period_spinner);
        String[] period = {"1", "2", "3", "4", "5", "6"};
        period_spinner.setItems(period);
        period_spinner.setSelectedIndex(0);

        tv_show_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time_dailog();
            }
        });

        tv_show_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_time_dailog();
            }
        });

        tv_show_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_dailog();
            }
        });

        ac_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_course();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String room = et_room.getText().toString();
                String building = et_building.getText().toString();
                String course = ac_course.getText().toString();
                String _period = period_spinner.getText().toString();
                String place = et_place.getText().toString();
                String teacher = et_teacher.getText().toString();
                String start_time = tv_show_start_time.getText().toString();
                String end_time = tv_show_end_time.getText().toString();
                String date = tv_show_day.getText().toString();


                Classrooms classrooms = new Classrooms(room, building, _period,start_time, end_time,date, place, teacher, course );

                if (TextUtils.isEmpty(room)) {
                    et_room.setError("Please insert room");
                }
                if (TextUtils.isEmpty(building)) {
                    et_room.setError("Please insert building");
                } else if (TextUtils.isEmpty(place)) {
                    et_place.setError("Please insert place");
                }else if (TextUtils.isEmpty(course)) {
                    ac_course.setError("Please insert Course");
                }
                else if (TextUtils.isEmpty(teacher)) {
                    et_teacher.setError("Please insert teacher");
                } else if (TextUtils.isEmpty(start_time)) {
                    Toast.makeText(getApplicationContext(), "Please insert start time", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(end_time)) {
                    Toast.makeText(getApplicationContext(), "Please insert end time", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(date)) {
                    Toast.makeText(getApplicationContext(), "Please insert due day", Toast.LENGTH_LONG).show();

                } else {

                    String[] s_time = start_time.split(":");
                    String[] e_time = end_time.split(":");

                    Integer s_hour = Integer.parseInt(s_time[0]);
                    Integer s_minute = Integer.parseInt(s_time[1]);

                    Integer e_hour = Integer.parseInt(e_time[0]);
                    Integer e_minute= Integer.parseInt(e_time[1]);

                    LocalTime start_t = LocalTime.of(s_hour,s_minute);
                    LocalTime end_t = LocalTime.of(e_hour,e_minute);

                    if (start_t.isBefore(end_t)) {

                        classroomDB.child(firebaseAuth.getUid()).child(room).setValue(classrooms).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Add new classroom Successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (start_t.equals(end_t)|| start_t.isAfter(end_t)){
                        Toast.makeText(getApplicationContext(), "Incorrect, Please select start time and end time again", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void select_course() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        courseDB = FirebaseDatabase.getInstance().getReference("Courses").child(currentUser);

        courseDB.addValueEventListener(new ValueEventListener() {

            public ArrayList<String> courseList;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList = new ArrayList<String>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Courses courses = dataSnapshot1.getValue(Courses.class);
                    String course = courses.getId_subject().toString();
                    courseList.add(course);
                }
                courseAdapter = new ArrayAdapter<String>(AddClassroom.this, android.R.layout.simple_dropdown_item_1line, courseList);
                ac_course.setAdapter(courseAdapter);
                ac_course.showDropDown();
                ac_course.setThreshold(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void date_dailog() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(AddClassroom.this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogview = inflater.inflate(R.layout.day_alertdialog,null);
        builder.setView(dialogview);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        m = (TextView) dialogview.findViewById(R.id.monday);
        t = (TextView) dialogview.findViewById(R.id.tuesdy);
        w = (TextView) dialogview.findViewById(R.id.wednesday);
        th = (TextView) dialogview.findViewById(R.id.thursday);
        f = (TextView) dialogview.findViewById(R.id.friday);
        sat = (TextView) dialogview.findViewById(R.id.saturday);
        s = (TextView) dialogview.findViewById(R.id.sunday);

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show_day.setText("Monday");
                alertDialog.dismiss();
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show_day.setText("Tuesday");
                alertDialog.dismiss();
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show_day.setText("Wednesday");
                alertDialog.dismiss();
            }
        });
        th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show_day.setText("Thursday");
                alertDialog.dismiss();
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show_day.setText("Friday");
                alertDialog.dismiss();
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show_day.setText("Saturday");
                alertDialog.dismiss();
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show_day.setText("Sunday");
                alertDialog.dismiss();
            }
        });
    }



    private void end_time_dailog() {
        GregorianCalendar now = new GregorianCalendar();

        int hour = now.get(now.HOUR_OF_DAY);
        int minute = now.get(now.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                endTimeSet(hours, minutes);
            }
        }, hour, minute, true);
        timePickerDialog.show();

    }

    private void endTimeSet(int hours, int minutes) {
        String endTime = hours + ":" + minutes;
        tv_show_end_time.setText(endTime);
    }


    private void start_time_dailog() {

        GregorianCalendar now = new GregorianCalendar();

        int hour = now.get(now.HOUR_OF_DAY);
        int minute = now.get(now.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                startTimeSet(hours, minutes);
            }
        }, hour, minute, true);
        timePickerDialog.show();

    }

    private void startTimeSet(int hours, int minutes) {
        String startTime = hours + ":" + minutes;
        tv_show_start_time.setText(startTime);
    }


}
