package com.example.studentdailyapp.studentdaily;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.studentdailyapp.studentdaily.Activity.Login;
import com.example.studentdailyapp.studentdaily.Activity.MainActivity;
import com.example.studentdailyapp.studentdaily.Adapters.AdminAdapter;
import com.example.studentdailyapp.studentdaily.Model.Members;
import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    private RecyclerView rec;
    private Toolbar toolbar;
    private DatabaseReference taskDB;
    private ArrayList<Members> memberList;
    private AdminAdapter adminAdapter;
    private DatabaseReference allMemberDB;
    private FloatingActionButton logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("List of all user");

        rec = (RecyclerView) findViewById(R.id.user_list);
        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rec.addItemDecoration(itemDecoration);

        logout_btn = (FloatingActionButton) findViewById(R.id.fab);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        memberRecycler();

    }

    public void memberRecycler() {
        allMemberDB = FirebaseDatabase.getInstance().getReference("Members");

        allMemberDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                memberList = new ArrayList<Members>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Members members = dataSnapshot1.getValue(Members.class);
                    memberList.add(members);
                }
                adminAdapter = new AdminAdapter(memberList);
                rec.setAdapter(adminAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
