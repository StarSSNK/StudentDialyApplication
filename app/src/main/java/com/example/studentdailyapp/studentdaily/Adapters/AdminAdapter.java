package com.example.studentdailyapp.studentdaily.Adapters;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.studentdailyapp.studentdaily.Model.Members;
import com.example.studentdailyapp.studentdaily.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.graphics.Color.GREEN;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyAdminViewHolder> {

    List<Members> listMember;
    private DatabaseReference memberDb;

    public AdminAdapter(List<Members> listMember) {
        this.listMember = listMember;
    }

    @NonNull
    @Override
    public MyAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new MyAdminViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyAdminViewHolder holder, int position) {

        holder.username.setText(listMember.get(position).getUsername());
        holder.email.setText(listMember.get(position).getEmail());
        final Members members = listMember.get(position);

        holder.disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberDb = FirebaseDatabase.getInstance().getReference("Members");
                memberDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (members.activated.equals(true)) {
                            memberDb.child(members.getId_user()).child("activated").setValue(false);
                        }
                        if (members.activated.equals(false)){
                            memberDb.child(members.getId_user()).child("activated").setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        if (members.activated.equals(false)){
            holder.disable.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
            holder.disable.setText("Enable");
        }

    }

    @Override
    public int getItemCount() {
        return listMember.size();
    }

    ///////////////////////////////////////////////////////////////////////////

    public class MyAdminViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView pic;
        private final TextView username;
        private final TextView email;
        private final Button disable;


        public MyAdminViewHolder(View itemView) {
            super(itemView);

            pic = (CircleImageView) itemView.findViewById(R.id.pic_userlist);
            username = (TextView) itemView.findViewById(R.id.username_userlist);
            email = (TextView) itemView.findViewById(R.id.email_userlist);
            disable = (Button) itemView.findViewById(R.id.disable_btn);
        }
    }
}
