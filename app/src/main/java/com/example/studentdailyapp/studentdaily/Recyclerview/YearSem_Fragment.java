package com.example.studentdailyapp.studentdaily.Recyclerview;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.example.studentdailyapp.studentdaily.Adapters.YearSemsAdapter;
import com.example.studentdailyapp.studentdaily.Add.AddYearSem;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import co.dift.ui.SwipeToAction;


public class YearSem_Fragment extends Fragment {


    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fab;
    DatabaseReference yearSemDB;
    ArrayList<YearSems> yearSemsList;
    YearSemsAdapter yearSemsAdapter;
    RecyclerView rec;
    private TextView yearSem;
    private TextView start_date;
    private TextView end_date;
    private Button submit;
    private Button cancle;

    public YearSem_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_year_sem_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Years and semesters");

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddYearSem.class));
            }
        });

        rec = (RecyclerView) view.findViewById(R.id.yearsem_rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));

        yearSem_recyclerview();

    }

    private void yearSem_recyclerview() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        yearSemDB = FirebaseDatabase.getInstance().getReference("YearSems").child(currentUser);

        yearSemDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yearSemsList = new ArrayList<YearSems>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    YearSems yearSems = dataSnapshot1.getValue(YearSems.class);
                    yearSemsList.add(yearSems);
                }
                yearSemsAdapter = new YearSemsAdapter(yearSemsList,getContext());
                rec.setAdapter(yearSemsAdapter);

                SwipeToAction swipeToAction = new SwipeToAction(rec, new SwipeToAction.SwipeListener<YearSems>() {

                    @Override
                    public boolean swipeLeft(final YearSems itemData) {

                        String yearSem = itemData.getYearSem();
                        final int pos = removeItem(itemData);
                        yearSemDB.child(yearSem).removeValue();
                        return true;
                    }

                    @Override
                    public boolean swipeRight(YearSems itemData) {

                        editTask(itemData);

                        return true;
                    }

                    @Override
                    public void onClick(YearSems itemData) {

                    }

                    @Override
                    public void onLongClick(YearSems itemData) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void editTask(YearSems itemData) {
        final String yearSem1 = itemData.getYearSem();
        String start_date1 = itemData.getStart_date();
        String end_date1 = itemData.getEnd_date();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.edit_yearsem, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        yearSem = (TextView) dialogView.findViewById(R.id.ed_yearSem);
        start_date = (TextView) dialogView.findViewById(R.id.ed_start_date);
        end_date = (TextView) dialogView.findViewById(R.id.ed_end_date);

        yearSem.setText(yearSem1);
        start_date.setText(start_date1);
        end_date.setText(end_date1);

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date_dailog();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_date_dailog();
            }
        });

        submit = (Button) dialogView.findViewById(R.id.submit_edit_yearSem);
        cancle = (Button) dialogView.findViewById(R.id.cancle_edit_yearSem);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ys = yearSem.getText().toString();
                String sd = start_date.getText().toString();
                String ed = end_date.getText().toString();


                YearSems yearSems = new YearSems(ys, sd, ed);

                if (TextUtils.isEmpty(sd)) {
                    Toast.makeText(getContext(), "Please insert start date", Toast.LENGTH_LONG).show();

                }
                if (TextUtils.isEmpty(ed)) {
                    Toast.makeText(getContext(), "Please insert end date", Toast.LENGTH_LONG).show();
                } else {

                    String[] s_date = sd.split("/");
                    String[] e_date = ed.split("/");

                    int s_day = Integer.valueOf(s_date[0]);
                    int s_month = Integer.valueOf(s_date[1]);
                    int s_year = Integer.valueOf(s_date[2]);

                    int e_day = Integer.valueOf(e_date[0]);
                    int e_month = Integer.valueOf(e_date[1]);
                    int e_year = Integer.valueOf(e_date[2]);


                    LocalDate start_d = LocalDate.of(s_year,s_month,s_day);
                    LocalDate end_d = LocalDate.of(e_year,e_month,e_day);


                    if (start_d.isBefore(end_d)){
                        yearSemDB.child(ys).setValue(yearSems).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                alertDialog.dismiss();
                                Toast.makeText(getContext(), "Add new year & Semester Successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (start_d.isEqual(end_d) || start_d.isAfter(end_d)){
                        Toast.makeText(getContext(), "Incorrect, Please select start date and end date again", Toast.LENGTH_LONG).show();
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


    private int removeItem(YearSems itemData) {
        return 0;
    }

    private void start_date_dailog() {
        GregorianCalendar now = new GregorianCalendar();

        int day = now.get(now.DATE);
        final int month = now.get(now.MONTH);
        int year = now.get(now.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfYear) {
                show_start_date(year, monthOfYear, dayOfYear);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void show_start_date(int year, int month, int date) {
        String sl_start_date = date + "/" + (month + 1) + "/" + year;
        start_date.setText(sl_start_date);
    }


    private void end_date_dailog() {

        GregorianCalendar now = new GregorianCalendar();

        int day = now.get(now.DATE);
        final int month = now.get(now.MONTH);
        int year = now.get(now.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfYear) {
                show_end_date(year, monthOfYear, dayOfYear);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void show_end_date(int year, int month, int date) {
        String sl_end_date = date + "/" + (month + 1) + "/" + year;
        end_date.setText(sl_end_date);

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

