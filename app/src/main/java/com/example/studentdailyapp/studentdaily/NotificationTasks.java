package com.example.studentdailyapp.studentdaily;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.studentdailyapp.studentdaily.Activity.PendingIntentTask;
import com.example.studentdailyapp.studentdaily.Model.Tasks;
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

public class NotificationTasks extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Notificationtask", Toast.LENGTH_LONG).show();

        String id = "main_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Channel name";
            String description = "Channel Description";
            int importent = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(id, name, importent);
            notificationChannel.setDescription(description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Bundle bundle = intent.getExtras();
        String titleTask = bundle.getString("title");
        String detailTask = bundle.getString("detail");
        String type = bundle.getString("type");
        String classroom = bundle.getString("classroom");
        String dueDate = bundle.getString("dueDate");
        String dueTime = bundle.getString("dueTime");

        Intent intent1 = new Intent(context, PendingIntentTask.class)
                .putExtra("title", titleTask)
                .putExtra("detail", detailTask)
                .putExtra("type", type)
                .putExtra("classroom", classroom)
                .putExtra("dueDate", dueDate)
                .putExtra("dueTime", dueTime);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 123, new Intent[]{intent1}, PendingIntent.FLAG_CANCEL_CURRENT);


        Notification.InboxStyle style = new Notification.InboxStyle()
                .setBigContentTitle(titleTask)
                .addLine(detailTask);

        Notification.Builder builder = new Notification.Builder(context, id);
        builder.setSmallIcon(R.drawable.logo2)
                .setStyle(style)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1000, builder.build());
    }
}


























  /*

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {




        String id = "main_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Channel name";
            String description = "Channel Description";
            int importent = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(id, name, importent);
            notificationChannel.setDescription(description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Bundle bundle = intent.getExtras();
        String titleTask = bundle.getString("title");
        String detailTask = bundle.getString("detail");
        String type = bundle.getString("type");
        String classroom = bundle.getString("classroom");
        String dueDate = bundle.getString("dueDate");
        String dueTime = bundle.getString("dueTime");

        Intent intent1 = new Intent(context, PendingIntentTask.class)
                .putExtra("title", titleTask)
                .putExtra("detail", detailTask)
                .putExtra("type", type)
                .putExtra("classroom", classroom)
                .putExtra("dueDate", dueDate)
                .putExtra("dueTime", dueTime);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 123, new Intent[]{intent1}, PendingIntent.FLAG_CANCEL_CURRENT);


        Notification.InboxStyle style = new Notification.InboxStyle()
                .setBigContentTitle(titleTask)
                .addLine(detailTask);

        Notification.Builder builder = new Notification.Builder(context, id);
        builder.setSmallIcon(R.drawable.logo2)
                .setStyle(style)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1000, builder.build());

        Toast.makeText(context, "Invoke background service onStartCommand method.", Toast.LENGTH_LONG).show();
    }*/


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


   /* @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

         Toast.makeText(this, "Invoke background service onStartCommand method.", Toast.LENGTH_LONG).show();

        for (int i = 0; i < 100; i++) {
            Toast.makeText(this, "i = " + i, Toast.LENGTH_LONG).show();
            calculateTasks();
        }

       // onTaskRemoved(intent);
       // calculateTasks();


        return Service.START_STICKY;
    }

    private void calculateTasks() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        taskDb = FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser);

        taskDb.addValueEventListener(new ValueEventListener() {
            public ArrayList<Tasks> tasksList;


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tasksList = new ArrayList<Tasks>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String titleTask = dataSnapshot1.child("title").getValue().toString();
                    String detailTask = dataSnapshot1.child("detail").getValue().toString();
                    int udt = Integer.valueOf(dataSnapshot1.child("udt").getValue().toString());

                    String[] dueDate = dataSnapshot1.child("dueDate").getValue().toString().split("/");
                    String[] dueTime = dataSnapshot1.child("dueTime").getValue().toString().split(":");


                    int day = Integer.valueOf(dueDate[0]);
                    int month = Integer.valueOf(dueDate[1]);
                    int year = Integer.valueOf(dueDate[2]);
                    int hour = Integer.valueOf(dueTime[0]);
                    int minute = Integer.valueOf(dueTime[1]);

                    GregorianCalendar now = new GregorianCalendar();

                    final int day_now = now.get(now.DATE);
                    final int month_now = now.get(now.MONTH);
                    final int year_now = now.get(now.YEAR);
                    int hour_now = now.get(now.HOUR_OF_DAY);
                    int minute_now = now.get(now.MINUTE);

                    LocalDateTime userSetDateTime = LocalDateTime.of(year, month, day, hour, minute);
                    // userSetDateTime = userSetDateTime.minus(udt, ChronoUnit.MINUTES);

                   // LocalDateTime righnow = LocalDateTime.of(year_now, month_now, day_now, hour_now, minute_now);
                    LocalDateTime righnow = LocalDateTime.of(2018, 10, 10, 17, 37);


                    if (userSetDateTime.equals(righnow)) {


                        showNotification(titleTask, detailTask);

                        // Toast.makeText(getApplicationContext(),"date"+ day+"/"+month+"/"+year,Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showNotification(String titleTask, String detailTask) {
        String id = "main_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Channel name";
            String description = "Channel Description";
            int importent = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(id, name, importent);
            notificationChannel.setDescription(description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        builder.setSmallIcon(R.drawable.logo2)
                .setContentTitle(titleTask)
                .setContentTitle(detailTask);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1000, builder.build());


    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);

        super.onTaskRemoved(rootIntent);
    } */
