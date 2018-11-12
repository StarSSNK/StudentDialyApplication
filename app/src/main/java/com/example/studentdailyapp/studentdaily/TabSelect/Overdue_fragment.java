package com.example.studentdailyapp.studentdaily.TabSelect;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.example.studentdailyapp.studentdaily.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import co.dift.ui.SwipeToAction;


/**
 * A simple {@link Fragment} subclass.
 */
public class Overdue_fragment extends Fragment {


    private RecyclerView rec;
    private DatabaseReference taskDB;
    private ArrayList<Tasks> tasksList;
    private TaskAdapter_complete taskAdapter;

    public Overdue_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overdue_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rec = (RecyclerView) view.findViewById(R.id.overdue_task_rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));


        TaskCompleteRecycler();
    }

    public void TaskCompleteRecycler() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        taskDB = FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser);


        taskDB.addValueEventListener(new ValueEventListener() {
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
                    int  minute = Integer.valueOf(dueTime[1]);

                    LocalDateTime righnow = LocalDateTime.of(year_now, month_now, day_now, hour_now, minute_now);
                    LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);


                    if (status.equals("Inprogress")) {
                        if (righnow.isAfter(dateTime)) {
                            Tasks tasks = dataSnapshot1.getValue(Tasks.class);
                            String title = tasks.getTitle();
                           taskDB.child(title).child("status").setValue("Overdue");
                        }
                    }
                    if (status.equals("Overdue")){
                        Tasks tasks = dataSnapshot1.getValue(Tasks.class);
                        tasksList.add(tasks);
                    }
                }
                taskAdapter = new TaskAdapter_complete(tasksList);
                rec.setAdapter(taskAdapter);

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

    private int removeItem(Tasks itemData) {
        int pos = tasksList.indexOf(itemData);
        tasksList.remove(itemData);
        taskAdapter.notifyItemRemoved(pos);
        return pos;
    }
}