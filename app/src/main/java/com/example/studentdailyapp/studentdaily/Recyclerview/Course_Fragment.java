package com.example.studentdailyapp.studentdaily.Recyclerview;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentdailyapp.studentdaily.Adapters.CoursesAdapter;
import com.example.studentdailyapp.studentdaily.Add.AddCourse;
import com.example.studentdailyapp.studentdaily.Add.AddYearSem;
import com.example.studentdailyapp.studentdaily.Model.Courses;
import com.example.studentdailyapp.studentdaily.Model.YearSems;
import com.example.studentdailyapp.studentdaily.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.dift.ui.SwipeToAction;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Course_Fragment extends Fragment {


    private FloatingActionButton fab;
    private RecyclerView rec;
    private DatabaseReference courseDB;
    private ArrayList<Courses> courseList;
    private CoursesAdapter coursesAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView idSj;
    private EditText sj;
    private AutoCompleteTextView ys;
    private TextView cl;
    private Button submit;
    private Button cancle;
    private int colorDefault;
    private DatabaseReference yearSemDB;
    private ArrayAdapter<String> yearSemAdapter;

    public Course_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Course");

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddCourse.class));
            }
        });

        rec = (RecyclerView) view.findViewById(R.id.course_rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));

        course_recyclerview();
    }

    private void course_recyclerview() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        courseDB = FirebaseDatabase.getInstance().getReference("Courses").child(currentUser);


        courseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList = new ArrayList<Courses>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Courses courses = dataSnapshot1.getValue(Courses.class);
                    courseList.add(courses);
                }
                coursesAdapter = new CoursesAdapter(courseList, getContext());
                rec.setAdapter(coursesAdapter);

                SwipeToAction swipeToAction = new SwipeToAction(rec, new SwipeToAction.SwipeListener<Courses>() {

                    @Override
                    public boolean swipeLeft(final Courses itemData) {

                        String id_subject = itemData.getId_subject();
                        final int pos = removeItem(itemData);
                        courseDB.child(id_subject).removeValue();
                        return true;
                    }

                    @Override
                    public boolean swipeRight(Courses itemData) {

                        editTask(itemData);

                        return true;
                    }

                    @Override
                    public void onClick(Courses itemData) {

                    }

                    @Override
                    public void onLongClick(Courses itemData) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void editTask(Courses itemData) {
        final String id_subject = itemData.getId_subject();
        String subject = itemData.getSubject();
        String id_yearsem = itemData.getYearSem_id();
        int color = itemData.getColor();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.edit_course, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        idSj = (TextView) dialogView.findViewById(R.id.ed_id_subject);
        sj = (EditText) dialogView.findViewById(R.id.ed_subject);
        ys = (AutoCompleteTextView) dialogView.findViewById(R.id.ed_yearSem_autoCom);
        cl = (TextView) dialogView.findViewById(R.id.ed_color);
        cl.setBackgroundColor(color);

        idSj.setText(id_subject);
        sj.setText(subject);
        ys.setText(id_yearsem);

        ys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               select_yearSem();
            }
        });


        submit = (Button) dialogView.findViewById(R.id.submit_edit_course);
        cancle = (Button) dialogView.findViewById(R.id.cancle_edit_course);

        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id_subject = idSj.getText().toString();
                String subject = sj.getText().toString();
                int color = colorDefault;
                String id_yearSem = ys.getText().toString();


                Courses courses = new Courses(id_subject, subject, id_yearSem, color);

                if (TextUtils.isEmpty(subject)) {
                    Toast.makeText(getContext(), "Please insert subject", Toast.LENGTH_LONG).show();
                }

                courseDB.child(id_subject).setValue(courses).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        alertDialog.dismiss();
                        Toast.makeText(getContext(), "Add new Course Successfully", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

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
                yearSemAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, yearSemList);
                ys.setAdapter(yearSemAdapter);
                ys.showDropDown();
                ys.setThreshold(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private int removeItem(Courses itemData) {
        return 0;
    }

    private void colorPicker() {
        AmbilWarnaDialog warnaDialog = new AmbilWarnaDialog(getContext(), colorDefault, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                startActivity(new Intent(getContext(), AddCourse.class));
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                colorDefault = color;
                cl.setBackgroundColor(colorDefault);
            }
        });
        warnaDialog.show();
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
