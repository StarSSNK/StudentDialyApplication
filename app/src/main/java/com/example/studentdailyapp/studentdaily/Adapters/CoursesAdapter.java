package com.example.studentdailyapp.studentdaily.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studentdailyapp.studentdaily.Model.Courses;
import com.example.studentdailyapp.studentdaily.Model.YearSems;
import com.example.studentdailyapp.studentdaily.R;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;
import de.hdodenhof.circleimageview.CircleImageView;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.MyCourseViewHolder> {

    List<Courses> listCourse;
    Context context;


    public CoursesAdapter(List<Courses> listCourse, Context context) {
        this.listCourse = listCourse;
        this.context = context;
    }

    @NonNull
    @Override
    public MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new MyCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCourseViewHolder holder, int position) {

        final Courses items = listCourse.get(position);
        MyCourseViewHolder courseViewHolder = (MyCourseViewHolder) holder;

        courseViewHolder.id_subject.setText(items.getId_subject());
        courseViewHolder.subject.setText(items.getSubject());
        courseViewHolder.yearsem.setText(items.getYearSem_id());
        courseViewHolder.colour.setBackgroundColor(items.getColor());
        courseViewHolder.data = items;
    }

    @Override
    public int getItemCount() {
        return listCourse.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class MyCourseViewHolder extends SwipeToAction.ViewHolder {
        private final TextView id_subject;
        private final TextView subject;
        private final CircleImageView colour;
        private final TextView yearsem;


        public MyCourseViewHolder(View itemView) {
            super(itemView);

            id_subject = (TextView) itemView.findViewById(R.id.id_subject_item);
            subject = (TextView) itemView.findViewById(R.id.subject_item);
            yearsem = (TextView) itemView.findViewById(R.id.c_yearsem_item);
            colour = (CircleImageView) itemView.findViewById(R.id.color_item);
        }
    }
}
