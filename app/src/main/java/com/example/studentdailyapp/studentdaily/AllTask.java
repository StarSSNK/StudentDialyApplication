package com.example.studentdailyapp.studentdaily;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.example.studentdailyapp.studentdaily.TabSelect.TaskAdapter_Incomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class AllTask extends Fragment {


    private RecyclerView today_rec;
    private RecyclerView tomorrow_rec;
    private RecyclerView soon_rec;
    private DatabaseReference taskDB;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public int day_now;
    public int month_now;
    public int year_now;
    private TextView righnow_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_all_task, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        task_rec();

        today_rec = (RecyclerView) view.findViewById(R.id.today);
        today_rec.setLayoutManager(new LinearLayoutManager(getContext()));

        tomorrow_rec = (RecyclerView) view.findViewById(R.id.tomorrow);
        tomorrow_rec.setLayoutManager(new LinearLayoutManager(getContext()));

        soon_rec = (RecyclerView) view.findViewById(R.id.soon);
        soon_rec.setLayoutManager(new LinearLayoutManager(getContext()));

        righnow_date = (TextView) view.findViewById(R.id.righnow_date);


        GregorianCalendar now = new GregorianCalendar();
        day_now = now.get(now.DATE);
        month_now = now.get(now.MONTH) + 1;
        year_now = now.get(now.YEAR);

        String today = day_now+"/"+month_now+"/"+year_now;
        righnow_date.setText(today);

    }

    private void task_rec() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        taskDB = FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser);

        taskDB.addValueEventListener(new ValueEventListener() {
            public ArrayList<Tasks> soonList;
            public ArrayList<Tasks> tomorrowList;
            public ArrayList<Tasks> todayList;
            public AllTask_Adapter allTaskAdapter;

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todayList = new ArrayList<Tasks>();
                tomorrowList = new ArrayList<Tasks>();
                soonList = new ArrayList<Tasks>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String dueDate[] = dataSnapshot1.child("dueDate").getValue().toString().split("/");

                    int day = Integer.valueOf(dueDate[0]);
                    int month = Integer.valueOf(dueDate[1]);
                    int year = Integer.valueOf(dueDate[2]);

                    LocalDate righnow = LocalDate.of(year_now, month_now, day_now);
                    LocalDate dateTime = LocalDate.of(year, month, day);
                    Tasks tasks = dataSnapshot1.getValue(Tasks.class);

                    if (righnow.equals(dateTime)) {
                        todayList.add(tasks);
                    }
                    else if (righnow.plusDays(1).equals(dateTime)) {
                        tomorrowList.add(tasks);
                    } else {
                        soonList.add(tasks);

                    }
                }
                allTaskAdapter = new AllTask_Adapter(todayList);
                today_rec.setAdapter(allTaskAdapter);

                allTaskAdapter = new AllTask_Adapter(tomorrowList);
                tomorrow_rec.setAdapter(allTaskAdapter);

                allTaskAdapter = new AllTask_Adapter(soonList);
                soon_rec.setAdapter(allTaskAdapter);
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
