package com.example.studentdailyapp.studentdaily.TabSelect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.example.studentdailyapp.studentdaily.R;
import com.google.firebase.database.DatabaseReference;


import java.util.List;

import co.dift.ui.SwipeToAction;

public class TaskAdapter_complete extends RecyclerView.Adapter<TaskAdapter_complete.MyTaskViewHolder> {

    List<Tasks> listData;

    public TaskAdapter_complete(List<Tasks> List) {
        listData = List;
    }

    @NonNull
    @Override
    public MyTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_complete, parent, false);
        return new MyTaskViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyTaskViewHolder holder, int position) {

        Tasks items = listData.get(position);
        MyTaskViewHolder taskViewHolder = (MyTaskViewHolder) holder;

        taskViewHolder.tv_status.setText(items.getStatus());
        taskViewHolder.tv_type.setText(items.getType());
        taskViewHolder.tv_title.setText(items.getTitle());
        taskViewHolder.tv_dueDate.setText(items.getDueDate());
        taskViewHolder.tv_dueTime.setText(items.getDueTime());

        taskViewHolder.data = items;

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class MyTaskViewHolder extends SwipeToAction.ViewHolder<Tasks> {

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
            tv_dueDate = (TextView) itemView.findViewById(R.id.dueDate_R);
            tv_dueTime = (TextView) itemView.findViewById(R.id.dueTime_R);

        }
    }
}