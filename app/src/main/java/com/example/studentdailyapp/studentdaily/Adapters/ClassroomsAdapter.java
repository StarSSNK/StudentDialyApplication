package com.example.studentdailyapp.studentdaily.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentdailyapp.studentdaily.Model.Classrooms;
import com.example.studentdailyapp.studentdaily.R;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;

public class ClassroomsAdapter extends RecyclerView.Adapter<ClassroomsAdapter.MyClassroomViewHolder>  {

    List<Classrooms> listClassroom;
    Context context;

    public ClassroomsAdapter(List<Classrooms> listClassroom, Context context) {
        this.listClassroom = listClassroom;
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classroom, parent, false);
        return new MyClassroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassroomViewHolder holder, int position) {

        final Classrooms items = listClassroom.get(position);
        MyClassroomViewHolder classroomViewHolder = (MyClassroomViewHolder) holder;

        classroomViewHolder.room.setText(items.getRoom());
        classroomViewHolder.building.setText(items.getBuilding());
        classroomViewHolder.course.setText(items.getCourse());
        classroomViewHolder.period.setText(items.getPeriod());
        classroomViewHolder.start_date.setText(items.getStart_time());
        classroomViewHolder.end_date.setText(items.getEnd_time());
        classroomViewHolder.date.setText(items.getDate());
        classroomViewHolder.place.setText(items.getPlace());
        classroomViewHolder.teacher.setText(items.getTeacher());
        classroomViewHolder.data = items;
    }

    @Override
    public int getItemCount() {
        return listClassroom.size();
    }

    public class MyClassroomViewHolder extends SwipeToAction.ViewHolder {
        private final TextView room;
        private final TextView building;
        private final TextView period;
        private final TextView start_date;
        private final TextView end_date;
        private final TextView date;
        private final TextView place;
        private final TextView teacher;
        private final TextView course;

        public MyClassroomViewHolder(View itemView) {
            super(itemView);

            room = (TextView) itemView.findViewById(R.id.room_item);
            building = (TextView) itemView.findViewById(R.id.building_item);
            course = (TextView) itemView.findViewById(R.id.course_item);
            period = (TextView) itemView.findViewById(R.id.period_item);
            start_date = (TextView) itemView.findViewById(R.id.start_time_item);
            end_date = (TextView) itemView.findViewById(R.id.end_time_item);
            date = (TextView) itemView.findViewById(R.id.date_item);
            place = (TextView) itemView.findViewById(R.id.place_item);
            teacher = (TextView) itemView.findViewById(R.id.teacher_item);


        }
    }
}
