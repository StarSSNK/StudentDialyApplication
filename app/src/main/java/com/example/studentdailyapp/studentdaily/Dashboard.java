package com.example.studentdailyapp.studentdaily;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentdailyapp.studentdaily.Activity.Profile;
import com.example.studentdailyapp.studentdaily.Add.AddTask;
import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.example.studentdailyapp.studentdaily.Recyclerview.Classroom_Fragment;
import com.example.studentdailyapp.studentdaily.Recyclerview.Course_Fragment;
import com.example.studentdailyapp.studentdaily.Recyclerview.YearSem_Fragment;
import com.example.studentdailyapp.studentdaily.TabSelect.PagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ImageView userPic;
    private TextView userUsername;
    private TextView userEmail;
    private DatabaseReference memberDB;
    private View header;
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    FragmentTransaction fragmentTransaction;
    Toolbar toolbar = null;
    NavigationView navigationView;
    DrawerLayout drawer;
    FloatingActionButton fab;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;
    private FragmentManager fragmentManager;
    private LinearLayout head_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddTask.class));
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        header = navigationView.getHeaderView(0);
        userPic = (ImageView) header.findViewById(R.id.pic_nav);
        userUsername = (TextView) header.findViewById(R.id.username_nav);
        userEmail = (TextView) header.findViewById(R.id.email_nav);
        display_userinfo_navHeader();

        head_profile = (LinearLayout) header.findViewById(R.id.head_profile);
        head_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Profile.class));
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentManager = getSupportFragmentManager();
        pagerAdapter = new PagerAdapter(fragmentManager);

        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    public void display_userinfo_navHeader() {

        memberDB = FirebaseDatabase.getInstance().getReference("Members").child(currentUser);
        memberDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String _username = dataSnapshot.child("username").getValue().toString();
                    String _email = dataSnapshot.child("email").getValue().toString();

                    userUsername.setText(_username);
                    userEmail.setText(_email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Somthing Wrong, Please try agian.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (id) {
            case R.id.nav_dashboard:
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                break;
            case R.id.nav_allTask:
                tabLayout.removeAllTabs();
                fragmentTransaction.replace(R.id.content_dashboard, new Schedule_Task());
                fragmentTransaction.commit();
                break;
            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), Profile.class));
                break;

            case R.id.nav_yearSem:
                tabLayout.removeAllTabs();
                fragmentTransaction.replace(R.id.content_dashboard, new YearSem_Fragment());
                fragmentTransaction.commit();
                break;

            case R.id.nav_course:
                tabLayout.removeAllTabs();
                fragmentTransaction.replace(R.id.content_dashboard, new Course_Fragment());
                fragmentTransaction.commit();
                break;

            case R.id.nav_classroom:
                tabLayout.removeAllTabs();
                fragmentTransaction.replace(R.id.content_dashboard, new Classroom_Fragment());
                fragmentTransaction.commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

  }
