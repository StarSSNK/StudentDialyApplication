package com.example.studentdailyapp.studentdaily.Add;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alexzaitsev.meternumberpicker.MeterNumberPicker;
import com.alexzaitsev.meternumberpicker.MeterView;
import com.example.studentdailyapp.studentdaily.Dashboard;
import com.example.studentdailyapp.studentdaily.Model.YearSems;
import com.example.studentdailyapp.studentdaily.R;
import com.example.studentdailyapp.studentdaily.Recyclerview.YearSem_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;

public class AddYearSem extends AppCompatActivity {

    private TextView et_start_date;
    private TextView et_end_date;
    private Button cancle_ys_btn;
    private Button add_ys_btn;

    DatabaseReference yearSemDB;
    private MeterView year_meternumber;

    GregorianCalendar now = new GregorianCalendar();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_year_sem);

        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        yearSemDB = FirebaseDatabase.getInstance().getReference("YearSems").child(currentUser);


        et_start_date = (TextView) findViewById(R.id.start_date);
        et_end_date = (TextView) findViewById(R.id.end_date);
        add_ys_btn = (Button) findViewById(R.id.submit_add_yearSem);
        cancle_ys_btn = (Button) findViewById(R.id.cancle_add_yearSem);


        final MaterialSpinner semester_spinner = (MaterialSpinner) findViewById(R.id.semester_spinner);
        String[] semester = {"1", "2", "3"};
        semester_spinner.setItems(semester);
        semester_spinner.setSelectedIndex(0);

        int year_now = now.get(now.YEAR);
        year_meternumber = (MeterView) findViewById(R.id.year_meternumber);
        year_meternumber.setValue(year_now);


        et_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_date_dailog();
            }
        });
        et_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_date_dailog();
            }
        });


        add_ys_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String year = String.valueOf(year_meternumber.getValue());
                String semester = semester_spinner.getText().toString();
                final String ys = year + "-" + semester;
                final String start_date = et_start_date.getText().toString().trim();
                final String end_date = et_end_date.getText().toString().trim();

                YearSems yearSems = new YearSems(ys, start_date, end_date);

                if (TextUtils.isEmpty(start_date)) {
                    et_start_date.setError("Please insert start date");

                }
                if (TextUtils.isEmpty(end_date)) {
                    et_end_date.setError("Please insert end date");


                } else {

                    String[] s_date = start_date.split("/");
                    String[] e_date = end_date.split("/");

                    int s_day = Integer.valueOf(s_date[0]);
                    int s_month = Integer.valueOf(s_date[1]);
                    int s_year = Integer.valueOf(s_date[2]);

                    int e_day = Integer.valueOf(e_date[0]);
                    int e_month = Integer.valueOf(e_date[1]);
                    int e_year = Integer.valueOf(e_date[2]);


                    LocalDate start_d = LocalDate.of(s_year,s_month,s_day);
                    LocalDate end_d = LocalDate.of(e_year,e_month,e_day);


                    if (start_d.isBefore(end_d)){
                        yearSemDB.child(ys).setValue(yearSems).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Add new year & Semester Successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (start_d.isEqual(end_d) || start_d.isAfter(end_d)){
                        Toast.makeText(getApplicationContext(), "Incorrect, Please select start date and end date again", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        cancle_ys_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext(), Dashboard.class));
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void start_date_dailog() {
        GregorianCalendar now = new GregorianCalendar();

        int day = now.get(now.DATE);
        final int month = now.get(now.MONTH);
        int year = now.get(now.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfYear) {
                show_start_date(year, monthOfYear, dayOfYear);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void show_start_date(int year, int month, int date) {
        String sl_start_date = date + "/" + (month + 1) + "/" + year;
        et_start_date.setText(sl_start_date);
    }


    private void end_date_dailog() {

        GregorianCalendar now = new GregorianCalendar();

        int day = now.get(now.DATE);
        final int month = now.get(now.MONTH);
        int year = now.get(now.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfYear) {
                show_end_date(year, monthOfYear, dayOfYear);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void show_end_date(int year, int month, int date) {
        String sl_end_date = date + "/" + (month + 1) + "/" + year;
        et_end_date.setText(sl_end_date);

    }
}
