package com.example.studentdailyapp.studentdaily.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentdailyapp.studentdaily.Model.YearSems;
import com.example.studentdailyapp.studentdaily.R;

import java.util.List;

import co.dift.ui.SwipeToAction;


public class YearSemsAdapter extends RecyclerView.Adapter<YearSemsAdapter.MyYearSemViewHolder> {

    List<YearSems> listYearsem;
    Context context;


    public YearSemsAdapter(List<YearSems> listYearsem, Context context) {
        this.listYearsem = listYearsem;
        this.context = context;
    }

    @NonNull
    @Override
    public MyYearSemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yearsem, parent, false);
        return new MyYearSemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyYearSemViewHolder holder, int position) {

        final YearSems items = listYearsem.get(position);
        MyYearSemViewHolder yearSemViewHolder = (MyYearSemViewHolder) holder;

        yearSemViewHolder.yearsem.setText(items.getYearSem());
        yearSemViewHolder.startDue.setText(items.getStart_date());
        yearSemViewHolder.endDue.setText(items.getEnd_date());
        yearSemViewHolder.data = items;
    }

    @Override
    public int getItemCount() {
        return listYearsem.size();
    }

    public class MyYearSemViewHolder extends SwipeToAction.ViewHolder {
        private final TextView yearsem;
        private final TextView startDue;
        private final TextView endDue;

        public MyYearSemViewHolder(View itemView) {
            super(itemView);

            yearsem = (TextView) itemView.findViewById(R.id.yearsem_item);
            startDue = (TextView) itemView.findViewById(R.id.startDate_item);
            endDue = (TextView) itemView.findViewById(R.id.endDate_item);
        }
    }
}
