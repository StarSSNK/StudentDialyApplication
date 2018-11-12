package com.example.studentdailyapp.studentdaily.TabSelect;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.studentdailyapp.studentdaily.Model.Classrooms;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import co.dift.ui.SwipeToAction;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Incomplete_Fragment extends Fragment {

    RecyclerView rec;
    ArrayList<Tasks> tasksList;
    DatabaseReference taskDB;
    TaskAdapter_Incomplete taskAdapterIncomplete;
    public EditText detail_et;
    public AutoCompleteTextView classroom_sl;
    public TextView dueDate_tv;
    public TextView dueTime_tv;
    private Button submit;
    private Button cancle;
    public TextView title_tv;
    private LinearLayout datetime;
    private DatabaseReference classroomDB;
    public String udtTask;
    private String typetask;
    private ArrayAdapter<String> classroomAdapter;


    public Incomplete_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_incomplete_, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TaskRecycler();
        rec = (RecyclerView) view.findViewById(R.id.incomplete_task_rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    public void TaskRecycler() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        taskDB = FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser);


        taskDB.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tasksList = new ArrayList<Tasks>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    GregorianCalendar now = new GregorianCalendar();

                    int day_now = now.get(now.DATE);
                    final int month_now = now.get(now.MONTH) + 1;
                    int year_now = now.get(now.YEAR);
                    int hour_now = now.get(now.HOUR_OF_DAY);
                    int minute_now = now.get(now.MINUTE);


                    String status = dataSnapshot1.child("status").getValue().toString();
                    String dueDate[] = dataSnapshot1.child("dueDate").getValue().toString().split("/");
                    String dueTime[] = dataSnapshot1.child("dueTime").getValue().toString().split(":");


                    int day = Integer.valueOf(dueDate[0]);
                    int month = Integer.valueOf(dueDate[1]);
                    int year = Integer.valueOf(dueDate[2]);
                    int hour = Integer.valueOf(dueTime[0]);
                    int minute = Integer.valueOf(dueTime[1]);

                    LocalDateTime righnow = LocalDateTime.of(year_now, month_now, day_now, hour_now, minute_now);
                    LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);


                    if (status.equals("Inprogress")) {
                        if (righnow.isBefore(dateTime)) {
                            Tasks tasks = dataSnapshot1.getValue(Tasks.class);
                            tasksList.add(tasks);
                        }
                    }
                }
                taskAdapterIncomplete = new TaskAdapter_Incomplete(tasksList);
                rec.setAdapter(taskAdapterIncomplete);

                SwipeToAction swipeToAction = new SwipeToAction(rec, new SwipeToAction.SwipeListener<Tasks>() {

                    @Override
                    public boolean swipeLeft(final Tasks itemData) {

                        String title = itemData.getTitle();
                        final int pos = removeItem(itemData);
                        taskDB.child(title).removeValue();
                        return true;
                    }

                    @Override
                    public boolean swipeRight(Tasks itemData) {

                        editTask(itemData);

                        return true;
                    }

                    @Override
                    public void onClick(Tasks itemData) {

                    }

                    @Override
                    public void onLongClick(Tasks itemData) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void editTask(Tasks itemData) {

        typetask = itemData.getType();
        udtTask = itemData.getUdt();
        String titleTask = itemData.getTitle();
        String detailTask = itemData.getDetail();
        String classroomTask = itemData.getClassroom();
        String dueDateTask = itemData.getDueDate();
        String dueTimeTask = itemData.getDueTime();
        final String status = itemData.getStatus();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.edit_task, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        title_tv = (TextView) dialogView.findViewById(R.id.ed_title_t);
        detail_et = (EditText) dialogView.findViewById(R.id.ed_detail_t);
        classroom_sl = (AutoCompleteTextView) dialogView.findViewById(R.id.ed_sl_classroom);
        dueDate_tv = (TextView) dialogView.findViewById(R.id.ed_sl_date);
        dueTime_tv = (TextView) dialogView.findViewById(R.id.ed_sl_time);
        datetime = (LinearLayout) dialogView.findViewById(R.id.sl_datetime);
        submit = (Button) dialogView.findViewById(R.id.submit_edit_task);
        cancle = (Button) dialogView.findViewById(R.id.cancle_edit_task);


        title_tv.setText(titleTask);
        detail_et.setText(detailTask);
        classroom_sl.setText(classroomTask);
        dueDate_tv.setText(dueDateTask);
        dueTime_tv.setText(dueTimeTask);


        final MaterialSpinner type_spinner = (MaterialSpinner) dialogView.findViewById(R.id.ed_type_spinner);
        final String[] Type = {"Assignment", "Exam", "Activity"};
        type_spinner.setItems(Type);
        if (typetask.equals(Type[0])) {
            type_spinner.setSelectedIndex(0);
        } else if (typetask.equals(Type[1])) {
            type_spinner.setSelectedIndex(1);
        } else if (typetask.equals(Type[2])) {
            type_spinner.setSelectedIndex(2);
        }

        final MaterialSpinner udt_spinner = (MaterialSpinner) dialogView.findViewById(R.id.ed_udt_spinner);
        String[] Udt = {"5 minutes", "10 minutes", "30 minutes", "1 hour"};
        udt_spinner.setItems(Udt);
        udt_spinner.setText(udtTask);

        datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datetimeSl();
            }
        });

        classroom_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_classroom();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            public DatabaseReference taskDb;

            @Override
            public void onClick(View v) {

                String type = type_spinner.getText().toString();
                String title = title_tv.getText().toString();
                String detail = detail_et.getText().toString();
                String classrrom = classroom_sl.getText().toString();
                String dueDate = dueDate_tv.getText().toString();
                String dueTime = dueTime_tv.getText().toString();
                String udt = udt_spinner.getText().toString();


                if (TextUtils.isEmpty(detail)) {
                    detail_et.setError("Please insert detail");
                }
                if (TextUtils.isEmpty(classrrom)) {
                    classroom_sl.setError("Please select course or classroom");
                }
                if (TextUtils.isEmpty(dueDate) && TextUtils.isEmpty(dueTime)) {
                    Toast.makeText(getContext(), "Please insert due Date, due Time", Toast.LENGTH_LONG).show();
                }
                else {

                    Tasks task = new Tasks(type, title, detail, classrrom, status, dueDate, dueTime, udt);
                    taskDb = FirebaseDatabase.getInstance().getReference("Tasks");
                    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    taskDb.child(currentUser).child(title).setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            alertDialog.dismiss();
                            Snackbar.make(getView(), "Update your task successfully", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void datetimeSl() {
        final GregorianCalendar now = new GregorianCalendar();
        final int year_now = now.get(now.YEAR);
        final int month_now = now.get(now.MONTH);
        final int day_now = now.get(now.DAY_OF_MONTH);
        final int hour_now = now.get(now.HOUR_OF_DAY);
        final int minute_now = now.get(now.MINUTE);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {

                        LocalDateTime dateTime = LocalDateTime.of(year,month,dayOfMonth,hourOfDay,minutes);
                        LocalDateTime righnow = LocalDateTime.of(year_now, month_now, day_now, hour_now, minute_now);

                        if (dateTime.isBefore(righnow)) {
                            Toast.makeText(getContext(), "Please Select date again", Toast.LENGTH_LONG).show();
                        }else {

                            dueDate_tv.setText(dayOfMonth + "/" + month + "/" + year);
                            dueTime_tv.setText(hourOfDay + ":" + minutes);

                            GregorianCalendar calendar = new GregorianCalendar(year,month,dayOfMonth,hourOfDay,minutes);

                            int udt = Integer.valueOf(udtTask);
                            String title = title_tv.getText().toString();
                            String detail = detail_et.getText().toString();
                            String classroom = classroom_sl.getText().toString();
                            String dueDate = dueDate_tv.getText().toString();
                            String dueTime = dueTime_tv.getText().toString();



                            long timeInMillis = (calendar.getTimeInMillis() - udt * 60000);

                            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(getContext(), NotificationTasks.class)
                                    .putExtra("title", title)
                                    .putExtra("detail", detail)
                                    .putExtra("type", typetask)
                                    .putExtra("classroom", classroom)
                                    .putExtra("dueDate", dueDate)
                                    .putExtra("dueTime", dueTime);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
                            alarmManager.setRepeating(AlarmManager.RTC, timeInMillis,AlarmManager.INTERVAL_DAY, pendingIntent);

                        }
                    }
                }, hour_now, minute_now, true);
                timePickerDialog.show();
            }
        }, year_now, month_now, day_now);
        datePickerDialog.show();
    }

    private int removeItem(Tasks itemData) {
        int pos = tasksList.indexOf(itemData);
        tasksList.remove(itemData);
        taskAdapterIncomplete.notifyItemRemoved(pos);
        return pos;
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
                classroomAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, classrList);
                classroom_sl.setAdapter(classroomAdapter);
                classroom_sl.showDropDown();
                classroom_sl.setThreshold(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
