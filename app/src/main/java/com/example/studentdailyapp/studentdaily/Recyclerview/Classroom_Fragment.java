package com.example.studentdailyapp.studentdaily.Recyclerview;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentdailyapp.studentdaily.Adapters.ClassroomsAdapter;
import com.example.studentdailyapp.studentdaily.Add.AddClassroom;
import com.example.studentdailyapp.studentdaily.Model.Classrooms;
import com.example.studentdailyapp.studentdaily.Model.Courses;
import com.example.studentdailyapp.studentdaily.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.time.LocalTime;
import java.util.ArrayList;

import co.dift.ui.SwipeToAction;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Classroom_Fragment extends Fragment {


    private FloatingActionButton fab;
    private RecyclerView rec;
    private DatabaseReference classroomDB;
    private ArrayList<Classrooms> classroomList;
    private ClassroomsAdapter classroomsAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    TextView room_ed;
    private EditText building_ed;
    private MaterialSpinner period_ed;
    private EditText place_ed;
    private EditText teacher_ed;
    private TextView starttime_ed;
    private TextView endtime_ed;
    private TextView day_ed;
    private Button submit;
    private Button cancle;
    private AutoCompleteTextView sl_course;
    private DatabaseReference courseDB;
    private ArrayAdapter<String> courseAdapter;

    public Classroom_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classroom_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Classrooms");

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddClassroom.class));
            }
        });

        rec = (RecyclerView) view.findViewById(R.id.classroom_rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));

        classroom_recyclerview();
    }

    private void classroom_recyclerview() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        classroomDB = FirebaseDatabase.getInstance().getReference("Classrooms").child(currentUser);


        classroomDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classroomList = new ArrayList<Classrooms>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Classrooms classrooms = dataSnapshot1.getValue(Classrooms.class);
                    classroomList.add(classrooms);
                }
                classroomsAdapter = new ClassroomsAdapter(classroomList,getContext());
                rec.setAdapter(classroomsAdapter);

                SwipeToAction swipeToAction = new SwipeToAction(rec, new SwipeToAction.SwipeListener<Classrooms>() {

                    @Override
                    public boolean swipeLeft(final Classrooms itemData) {

                        String room = itemData.getRoom();
                        final int pos = removeItem(itemData);
                        classroomDB.child(room).removeValue();
                        return true;
                    }

                    @Override
                    public boolean swipeRight(Classrooms itemData) {
                        editTask(itemData);
                        return true;
                    }

                    @Override
                    public void onClick(Classrooms itemData) {

                    }

                    @Override
                    public void onLongClick(Classrooms itemData) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void editTask(Classrooms itemData) {

        String room = itemData.getRoom();
        String building = itemData.getBuilding();
        String course_sl = itemData.getCourse();
        String  period = itemData.getPeriod();
        String place = itemData.getPlace();
        String teacher = itemData.getTeacher();
        String starttime = itemData.getStart_time();
        String endtime = itemData.getEnd_time();
        String day = itemData.getDate();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.edit_classroom, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        room_ed = (TextView) dialogView.findViewById(R.id.ed_room);
        building_ed = (EditText) dialogView.findViewById(R.id.ed_building);
        sl_course = (AutoCompleteTextView) dialogView.findViewById(R.id.ed_sl_course);
        period_ed = (MaterialSpinner) dialogView.findViewById(R.id.ed_period_spinner);
        place_ed = (EditText) dialogView.findViewById(R.id.ed_place);
        teacher_ed = (EditText) dialogView.findViewById(R.id.ed_teacher);
        starttime_ed = (TextView) dialogView.findViewById(R.id.ed_start_time_show);
        endtime_ed = (TextView) dialogView.findViewById(R.id.ed_end_time_show);
        day_ed = (TextView) dialogView.findViewById(R.id.ed_date_show);

        final MaterialSpinner period_spinner = (MaterialSpinner) dialogView.findViewById(R.id.ed_period_spinner);
        String[] period_sp = {"1", "2", "3", "4", "5", "6"};
        period_spinner.setItems(period_sp);
        period_spinner.setSelectedIndex(0);

        room_ed.setText(room);
        building_ed.setText(building);
        sl_course.setText(course_sl);
        period_ed.setText(period);
        place_ed.setText(place);
        teacher_ed.setText(teacher);
        starttime_ed.setText(starttime);
        endtime_ed.setText(endtime);
        day_ed.setText(day);

        sl_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_course();
            }
        });

        submit = (Button) dialogView.findViewById(R.id.ed_submit_classroom);
        cancle = (Button) dialogView.findViewById(R.id.ed_cancle_classroom);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mRoom = room_ed.getText().toString();
                String mBuilding = building_ed.getText().toString();
                String mCourse = sl_course.getText().toString();
                String mPeriod = period_ed.getText().toString();
                String mPlace = place_ed.getText().toString();
                String mTeacher = teacher_ed.getText().toString();
                String mStart_time = starttime_ed.getText().toString();
                String mEnd_time = endtime_ed.getText().toString();
                String mDate = day_ed.getText().toString();


                Classrooms classrooms = new Classrooms(mRoom, mBuilding, mPeriod,mStart_time, mEnd_time,mDate, mPlace, mTeacher,mCourse);


                if (TextUtils.isEmpty(mBuilding)) {
                    building_ed.setError("Please insert building");
                } else if (TextUtils.isEmpty(mPlace)) {
                    place_ed.setError("Please insert Course or place");
                } else if (TextUtils.isEmpty(mTeacher)) {
                    teacher_ed.setError("Please insert Course or teacher");
                } else if (TextUtils.isEmpty(mStart_time)) {
                    Toast.makeText(getContext(), "Please insert start time", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(mEnd_time)) {
                    Toast.makeText(getContext(), "Please insert end time", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(mDate)) {
                    Toast.makeText(getContext(), "Please insert Classroom due date", Toast.LENGTH_LONG).show();

                } else {

                    String[] s_time = mStart_time.split(":");
                    String[] e_time = mEnd_time.split(":");

                    Integer s_hour = Integer.parseInt(s_time[0]);
                    Integer s_minute = Integer.parseInt(s_time[1]);

                    Integer e_hour = Integer.parseInt(e_time[0]);
                    Integer e_minute= Integer.parseInt(e_time[1]);

                    LocalTime start_t = LocalTime.of(s_hour,s_minute);
                    LocalTime end_t = LocalTime.of(e_hour,e_minute);

                    if (start_t.isBefore(end_t)) {
                        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        classroomDB.child(currentUser).setValue(classrooms).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Add new classroom Successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (start_t.equals(end_t)|| start_t.isAfter(end_t)){
                        Toast.makeText(getContext(), "Incorrect, Please select start time and end time again", Toast.LENGTH_LONG).show();
                    }
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
                courseAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, courseList);
                sl_course.setAdapter(courseAdapter);
                sl_course.showDropDown();
                sl_course.setThreshold(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private int removeItem(Classrooms itemData) {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tablayout);

        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Make everything visible again
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tablayout);

        viewPager.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
    }
}
