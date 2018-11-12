package com.example.studentdailyapp.studentdaily;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.studentdailyapp.studentdaily.Model.Tasks;
import java.util.List;

public class AllTask_Adapter extends RecyclerView.Adapter<AllTask_Adapter.MyAllTaskViewHolder> {

    List<Tasks> listData;

    public AllTask_Adapter(List<Tasks> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public MyAllTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alltask, parent, false);
        return new AllTask_Adapter.MyAllTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAllTaskViewHolder holder, int position) {

        final Tasks items = listData.get(position);
        MyAllTaskViewHolder allTaskViewHolder = (AllTask_Adapter.MyAllTaskViewHolder) holder;

        allTaskViewHolder.date.setText(items.getDueDate());
        allTaskViewHolder.time.setText(items.getDueTime());
        allTaskViewHolder.title.setText(items.getType());
        allTaskViewHolder.detail.setText(items.getTitle());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MyAllTaskViewHolder extends RecyclerView.ViewHolder {


        private final TextView title;
        private final TextView detail;
        private final TextView date;
        private final TextView time;

        public MyAllTaskViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            title = (TextView) itemView.findViewById(R.id.title);
            detail = (TextView) itemView.findViewById(R.id.detail);

        }
    }
}
