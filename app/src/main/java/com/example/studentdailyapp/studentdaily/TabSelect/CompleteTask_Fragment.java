package com.example.studentdailyapp.studentdaily.TabSelect;



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.studentdailyapp.studentdaily.Activity.Profile;
import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.example.studentdailyapp.studentdaily.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import co.dift.ui.SwipeToAction;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteTask_Fragment extends Fragment {


    RecyclerView rec;
    ArrayList<Tasks> tasksList;
    DatabaseReference taskDB;
    TaskAdapter_complete taskAdapter;


    public CompleteTask_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_task_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TaskCompleteRecycler();

        rec = (RecyclerView) view.findViewById(R.id.complete_task_rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    public void TaskCompleteRecycler() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        taskDB = FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser);


        taskDB.addValueEventListener(new ValueEventListener() {
            public Button no;
            public Button yes;

            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                tasksList = new ArrayList<Tasks>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String status = dataSnapshot1.child("status").getValue().toString();

                    if (status.equals("Complete")) {

                        Tasks tasks = dataSnapshot1.getValue(Tasks.class);
                        tasksList.add(tasks);
                    }
                }

                taskAdapter = new TaskAdapter_complete(tasksList);
                rec.setAdapter(taskAdapter);

                SwipeToAction swipeToAction = new SwipeToAction(rec, new SwipeToAction.SwipeListener<Tasks>() {

                    @Override
                    public boolean swipeLeft(final Tasks itemData) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = getLayoutInflater();

                        View dialogView = inflater.inflate(R.layout.delete,null);
                        builder.setView(dialogView);

                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        yes = (Button) dialogView.findViewById(R.id.yes);
                        no = (Button) dialogView.findViewById(R.id.no);

                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String title = itemData.getTitle();
                                final int pos = removeItem(itemData);
                                taskDB.child(title).removeValue();
                            }
                        });

                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        return false;
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