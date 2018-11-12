package com.example.studentdailyapp.studentdaily;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;


public class Schedule_Task extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RecyclerView item_rec;
    private MCalendarView calendarView;
    private DatabaseReference taskDB;
    private String currentUser;
    public int year;
    public int month;
    private TextView ym;
    public int year_now;
    public int month_now;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_schedule__task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ym = (TextView) view.findViewById(R.id.yearMonth);
        final GregorianCalendar now = new GregorianCalendar();
        year_now = now.get(now.YEAR);
        month_now = now.get(now.MONTH)+1;

        ym.setText(year_now + " / " + month_now);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        taskDB = FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser);

        item_rec = (RecyclerView) view.findViewById(R.id.item);
        item_rec.setLayoutManager(new LinearLayoutManager(getContext()));

        calendarView = (MCalendarView) view.findViewById(R.id.calendar);
        highligh();

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                year = date.getYear();
                month = date.getMonth();
                task(date.getYear(),date.getMonth(),date.getDay());
            }
        });


        calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                //Toast.makeText(getContext(), String.format("%d / %d", year, month), Toast.LENGTH_SHORT).show();
                ym.setText(String.format("%d / %d", year, month));
            }
        });
    }

    private void highligh() {

        taskDB.addValueEventListener(new ValueEventListener() {
            public ArrayList<DateData> dayList;
            public AllTask_Adapter allTaskAdapter;

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dayList = new ArrayList<DateData>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String dueDate[] = dataSnapshot1.child("dueDate").getValue().toString().split("/");

                    int day = Integer.valueOf(dueDate[0]);
                    int month = Integer.valueOf(dueDate[1]);
                    int year = Integer.valueOf(dueDate[2]);

                    dayList.add(new DateData(year,month,day));

                }
                for (int i = 0; i < dayList.size(); i++) {
                    calendarView.markDate(dayList.get(i).getYear(), dayList.get(i).getMonth(), dayList.get(i).getDay());//mark multiple dates with this code.
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void task(final int year_now, final int month_now, final int dayOfMonth_now) {

        taskDB.addValueEventListener(new ValueEventListener() {
            public ArrayList<Tasks> dayList;
            public AllTask_Adapter allTaskAdapter;

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dayList = new ArrayList<Tasks>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String dueDate[] = dataSnapshot1.child("dueDate").getValue().toString().split("/");

                    int day = Integer.valueOf(dueDate[0]);
                    int month = Integer.valueOf(dueDate[1]);
                    int year = Integer.valueOf(dueDate[2]);

                    LocalDate select_date = LocalDate.of(year_now, month_now, dayOfMonth_now);
                    LocalDate dateTime = LocalDate.of(year, month, day);

                    if (select_date.equals(dateTime)) {
                        Tasks tasks = dataSnapshot1.getValue(Tasks.class);
                        dayList.add(tasks);
                    }
                }
                allTaskAdapter = new AllTask_Adapter(dayList);
                item_rec.setAdapter(allTaskAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
