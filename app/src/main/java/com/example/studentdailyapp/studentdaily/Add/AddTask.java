package com.example.studentdailyapp.studentdaily.Add;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.studentdailyapp.studentdaily.Activity.Profile;
import com.example.studentdailyapp.studentdaily.Dashboard;
import com.example.studentdailyapp.studentdaily.Model.Classrooms;
import com.example.studentdailyapp.studentdaily.Model.Courses;
import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.example.studentdailyapp.studentdaily.NotificationTasks;
import com.example.studentdailyapp.studentdaily.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import android.icu.util.Calendar;

import java.util.GregorianCalendar;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class AddTask extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference taskDb;
    private Button submit_add_btn;
    private Button cancle_add_btn;
    private EditText title_et;
    private EditText detail_et;
    private TextView dueDate_tv;
    private TextView dueTime_tv;
    private AutoCompleteTextView classroom_sl;
    private ImageView dropdown_classroom;
    AutoCompleteTextView sl_course;
    private AutoCompleteTextView sl_classr;
    private ArrayAdapter<String> classroomAdapter;
    private DatabaseReference classroomDB;
    private LinearLayout sl_datetime;
    public String type;
    public String title;
    public String detail;
    public String classroom;
    public String dueDate;
    public String dueTime;
    public String udt;
    private MaterialSpinner udt_spinner;
    private MaterialSpinner type_spinner;
    Calendar calendar;
    public int year_now;
    public int month_now;
    public int day_now;
    public int hour_now;
    public int minute_now;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        title_et = (EditText) findViewById(R.id.title_t);
        detail_et = (EditText) findViewById(R.id.detail_t);
        classroom_sl = (AutoCompleteTextView) findViewById(R.id.sl_classroom);
        dueDate_tv = (TextView) findViewById(R.id.sl_date);
        dueTime_tv = (TextView) findViewById(R.id.sl_time);
        submit_add_btn = (Button) findViewById(R.id.submit_add_task);
        cancle_add_btn = (Button) findViewById(R.id.cancle_add_task);
        sl_course = (AutoCompleteTextView) findViewById(R.id.sl_course);
        sl_classr = (AutoCompleteTextView) findViewById(R.id.sl_classroom);
        dropdown_classroom = (ImageView) findViewById(R.id.dropdown_classr);
        sl_datetime = (LinearLayout) findViewById(R.id.sl_datetime);

        firebaseAuth = FirebaseAuth.getInstance();
        taskDb = FirebaseDatabase.getInstance().getReference("Tasks");

        set_type_spinner();
        set_udt_spinner();

        dropdown_classroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_classroom();
            }
        });

        sl_datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final GregorianCalendar now = new GregorianCalendar();
                year_now = now.get(now.YEAR);
                month_now = now.get(now.MONTH);
                day_now = now.get(now.DAY_OF_MONTH);
                hour_now = now.get(now.HOUR_OF_DAY);
                minute_now = now.get(now.MINUTE);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(AddTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final TimePickerDialog timePickerDialog = new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {

                                int month1 = month +1;

                                LocalDateTime dateTime = LocalDateTime.of(year,month1,dayOfMonth,hourOfDay,minutes);
                                LocalDateTime righnow = LocalDateTime.of(year_now, month_now + 1, day_now, hour_now, minute_now);

                                if (dateTime.isBefore(righnow)) {
                                    Toast.makeText(getApplicationContext(), "Please Select date again", Toast.LENGTH_LONG).show();
                                }
                                else {

                                    dueDate_tv.setText(dayOfMonth + "/" + month1 + "/" + year);
                                    dueTime_tv.setText(hourOfDay + ":" + minutes);

                                    type = type_spinner.getText().toString();
                                    title = title_et.getText().toString();
                                    detail = detail_et.getText().toString();
                                    classroom = classroom_sl.getText().toString();
                                    dueDate = dueDate_tv.getText().toString();
                                    dueTime = dueTime_tv.getText().toString();
                                    int udt1 = Integer.valueOf(udt = udt_spinner.getText().toString());

                                    GregorianCalendar calendar = new GregorianCalendar(year,month,dayOfMonth,hourOfDay,minutes);

                                    long timeInMillis = (calendar.getTimeInMillis() - udt1 * 60000);

                                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    Intent intent = new Intent(AddTask.this, NotificationTasks.class)
                                            .putExtra("title", title)
                                            .putExtra("detail", detail)
                                            .putExtra("type", type)
                                            .putExtra("classroom", classroom)
                                            .putExtra("dueDate", dueDate)
                                            .putExtra("dueTime", dueTime);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTask.this, 0, intent, 0);
                                    alarmManager.setRepeating(AlarmManager.RTC, timeInMillis,AlarmManager.INTERVAL_DAY, pendingIntent);

                                }
                            }
                        }, hour_now, minute_now, true);
                        timePickerDialog.show();
                    }
                }, year_now, month_now, day_now);
                datePickerDialog.show();
            }
        });


        cancle_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        submit_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addTask(view);
            }
        });
    }


    private void set_udt_spinner() {
        udt_spinner = (MaterialSpinner) findViewById(R.id.udt_spinner);
        String[] Udt = {"0","5","10","20","25", "30", "60"};
        udt_spinner.setItems(Udt);
        udt_spinner.setSelectedIndex(0);
    }

    private void set_type_spinner() {
        type_spinner = (MaterialSpinner) findViewById(R.id.type_spinner);
        final String[] Type = {"Assignment", "Exam", "Activity"};
        type_spinner.setItems(Type);
        type_spinner.setSelectedIndex(0);
    }

    private void addTask(final View view) {

        type = type_spinner.getText().toString();
        title = title_et.getText().toString();
        detail = detail_et.getText().toString();
        classroom = classroom_sl.getText().toString();
        dueDate = dueDate_tv.getText().toString();
        dueTime = dueTime_tv.getText().toString();
        udt = udt_spinner.getText().toString();

        String status = "Inprogress";


        if (TextUtils.isEmpty(title)) {
            title_et.setError("Please insert title");
        }

        if (TextUtils.isEmpty(detail)) {
            detail_et.setError("Please insert detail");
        }

        if (TextUtils.isEmpty(classroom)) {
            classroom_sl.setText("-");
        }

        if (TextUtils.isEmpty(dueDate) && TextUtils.isEmpty(dueTime)) {
            Toast.makeText(getApplicationContext(), "Please insert due Date, due Time", Toast.LENGTH_LONG).show();
        } else {

            Tasks task = new Tasks(type, title, detail, classroom, status, dueDate, dueTime, udt);
            taskDb.child(firebaseAuth.getCurrentUser().getUid().toString()).child(title).setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                    Snackbar.make(view, "Add new task successfully", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }



    @Override
    public void onBackPressed() {
        finish();
    }

    private void show_classroom() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        classroomDB = FirebaseDatabase.getInstance().getReference("Classrooms").child(currentUser);


        classroomDB.addValueEventListener(new ValueEventListener() {
            public ArrayList<String> classrList;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classrList = new ArrayList<String>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Classrooms classrooms = dataSnapshot1.getValue(Classrooms.class);
                    String room = classrooms.getRoom().toString();
                    String course = classrooms.getCourse().toString();

                    String roomClass = "Room " + room + " of " + course;
                    classrList.add(roomClass);
                }
                classroomAdapter = new ArrayAdapter<String>(AddTask.this, android.R.layout.simple_dropdown_item_1line, classrList);
                sl_classr.setAdapter(classroomAdapter);
                sl_classr.showDropDown();
                sl_classr.setThreshold(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
