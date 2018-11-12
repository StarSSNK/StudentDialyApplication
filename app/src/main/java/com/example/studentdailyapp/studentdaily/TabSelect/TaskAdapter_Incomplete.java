package com.example.studentdailyapp.studentdaily.TabSelect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.example.studentdailyapp.studentdaily.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import co.dift.ui.SwipeToAction;

public class TaskAdapter_Incomplete extends RecyclerView.Adapter<TaskAdapter_Incomplete.MyTaskViewHolder> {

    List<Tasks> listData;

    private DatabaseReference taskDB;

    public TaskAdapter_Incomplete(List<Tasks> List) {
        listData = List;
    }

    @NonNull
    @Override
    public MyTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_incomplete, parent, false);
        return new MyTaskViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyTaskViewHolder holder, int position) {

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        taskDB = FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser);


        final Tasks items = listData.get(position);
        MyTaskViewHolder taskViewHolder = (MyTaskViewHolder) holder;

        taskViewHolder.tv_status.setText(items.getStatus());
        taskViewHolder.tv_type.setText(items.getType());
        taskViewHolder.tv_title.setText(items.getTitle());
        taskViewHolder.tv_detail.setText(items.getDetail());
        taskViewHolder.tv_dueDate.setText(items.getDueDate());
        taskViewHolder.tv_dueTime.setText(items.getDueTime());
        taskViewHolder.tv_classroom.setText(items.getClassroom());
        taskViewHolder.cb_taskComp.setChecked(items.isSelected());
        taskViewHolder.data = items;


        taskViewHolder.cb_taskComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.setSelected(!items.isSelected());
                holder.cb_taskComp.setChecked(items.isSelected());
                taskDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        taskDB = FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser);

                        taskDB.child(items.getTitle()).child("status").setValue("Complete");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MyTaskViewHolder extends SwipeToAction.ViewHolder<Tasks> {


        private final CheckBox cb_taskComp;
        private final TextView tv_classroom;
        private final TextView tv_detail;
        private TextView tv_status;
        private TextView tv_type;
        private TextView tv_title;
        private TextView tv_dueDate;
        private TextView tv_dueTime;

        public MyTaskViewHolder(View itemView) {
            super(itemView);

            tv_status = (TextView) itemView.findViewById(R.id.status_R);
            tv_type = (TextView) itemView.findViewById(R.id.type_R);
            tv_title = (TextView) itemView.findViewById(R.id.title_R);
            tv_detail = (TextView) itemView.findViewById(R.id.detail_R);
            tv_dueDate = (TextView) itemView.findViewById(R.id.dueDate_R);
            tv_dueTime = (TextView) itemView.findViewById(R.id.dueTime_R);
            tv_classroom = (TextView) itemView.findViewById(R.id.classroom_R);
            cb_taskComp = (CheckBox) itemView.findViewById(R.id.cb_taskComp);


        }
    }
}
