package com.example.studentdailyapp.studentdaily.Activity;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.studentdailyapp.studentdaily.Dashboard;
import com.example.studentdailyapp.studentdaily.Model.Tasks;
import com.example.studentdailyapp.studentdaily.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends AppCompatActivity {

    private ImageButton btn_log_out;
    private TextView showUsername;
    private TextView showEmail;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String currentId;
    private EditText et_add_new_username;
    private TextView btn_submit;
    private TextView btn_cancle;
    private EditText et_add_new_email;
    private TextView btn_change_pass;
    private EditText et_new_pass;
    String _username;
    String _email;
    private DatabaseReference usernameChild;
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference emailChild;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ImageButton btn_back;
    private CircleImageView picUser;
    private TextView uploadPic;
    private ImageView pic;
    private TextView choose_pic;
    private Button upload;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firebaseAuth = FirebaseAuth.getInstance();
        currentId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Members").child(currentId);

        ////////////////initailize////////////////////////////////
        picUser = (CircleImageView) findViewById(R.id.profile_pic);
        uploadPic = (TextView) findViewById(R.id.uploadPic);
        showUsername = (TextView) findViewById(R.id.show_username);
        showEmail = (TextView) findViewById(R.id.show_email);
        btn_log_out = (ImageButton) findViewById(R.id.log_out_btn);
        btn_change_pass = (TextView) findViewById(R.id.chang_pass_btn);
        btn_back = (ImageButton) findViewById(R.id.back);
        ////////////////////////////////////////////////////////////

        display_info();

        ////////////////////////////////////////////////////////////

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicture();
            }
        });

        showUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_username();
            }
        });

        showEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_email();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(getApplicationContext(), Dashboard.class)); }
        });

        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_pass();
            }
        });

        btn_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_out();
            }
        });

    }

    private void uploadPicture() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        LayoutInflater layoutInflater = getLayoutInflater();

        View dialogView = layoutInflater.inflate(R.layout.uploadpicture,null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        pic = (ImageView) dialogView.findViewById(R.id.pic);
        choose_pic = (TextView) dialogView.findViewById(R.id.choose_pic);
        upload = (Button) dialogView.findViewById(R.id.upload);

        choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("Image/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && requestCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                picUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void change_username() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.update_username_dailog,null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        et_add_new_username = (EditText) dialogView.findViewById(R.id.add_new_username);
        btn_submit = (TextView) dialogView.findViewById(R.id.submit_username_btn);
        btn_cancle = (TextView) dialogView.findViewById(R.id.cancle_username_btn);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usernameChild = FirebaseDatabase.getInstance().getReference("Members").child(currentUser).child("username");
                usernameChild.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String new_username = et_add_new_username.getText().toString().trim();
                        String old_username = dataSnapshot.getValue().toString();

                        if (TextUtils.isEmpty(new_username)) {
                            et_add_new_username.setError("Please Fill your username");
                            et_add_new_username.requestFocus();
                            return;
                        }

                        if (new_username.equals(old_username)) {
                            et_add_new_username.setError("It's not a new Username,Please insert your new username again");
                            et_add_new_username.requestFocus();
                        } else {

                            usernameChild.setValue(new_username);
                            Toast.makeText(getApplicationContext(), "Add your new Username Successfully", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void change_email() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.update_email_dailog,null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        et_add_new_email = (EditText) dialogView.findViewById(R.id.add_new_email);
        btn_submit = (TextView) dialogView.findViewById(R.id.submit_email_btn);
        btn_cancle = (TextView) dialogView.findViewById(R.id.cancle_email_btn);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailChild = FirebaseDatabase.getInstance().getReference("Members").child(currentUser).child("email");

                emailChild.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String new_email = et_add_new_email.getText().toString();
                        String old_email = dataSnapshot.getValue().toString();

                        if (TextUtils.isEmpty(new_email)) {
                            et_add_new_email.setError("Please Fill your email");
                            et_add_new_email.requestFocus();
                        } else if (new_email.equals(old_email)) {
                            et_add_new_email.setError("It's not a new email,Please insert your new email again");
                            et_add_new_email.requestFocus();
                        } else {

                            user.updateEmail(new_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        String new_email = et_add_new_email.getText().toString();
                                        emailChild.setValue(new_email);

                                        Toast.makeText(getApplicationContext(), "Your new Email is Updated", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    } else {

                                        et_add_new_email.setError("Unsuccessfully, Please login again");
                                        et_add_new_email.requestFocus();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void change_pass() {
       AlertDialog.Builder  builder = new AlertDialog.Builder(Profile.this);
       LayoutInflater inflater = getLayoutInflater();

       View dialogview = inflater.inflate(R.layout.update_pass_dailog,null);
        builder.setView(dialogview);

       final AlertDialog alertDialog = builder.create();
       alertDialog.show();


        et_new_pass = (EditText) dialogview.findViewById(R.id.add_new_password);
        btn_submit = (TextView) dialogview.findViewById(R.id.submit_pass_btn);
        btn_cancle = (TextView) dialogview.findViewById(R.id.cancle_pass_btn);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = et_new_pass.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword)) {
                    et_new_pass.setError("Please Fill your Password");
                    et_new_pass.requestFocus();
                }
                if (newPassword.length() < 6) {

                    et_new_pass.setError("Password length should more than 6");
                    et_new_pass.requestFocus();
                    return;
                } else {

                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Your new Password is Updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Unsuccessfully, Please login again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void log_out() {
        btn_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void display_info() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    _username = dataSnapshot.child("username").getValue().toString();
                    _email = dataSnapshot.child("email").getValue().toString();

                    showUsername.setText(_username);
                    showEmail.setText(_email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Somthing Wrong, Please try agian.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
