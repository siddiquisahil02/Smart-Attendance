package com.boiz.experiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SecondActivity extends AppCompatActivity {

    private EditText present,total,Goal;
    private  TextView per,need;
    private Button logout;
    private ImageButton yesbtn,nobtn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private ProgressDialog progressDialog2;

    int goal,z=1;
    int no_present,no_total;
    float percentage=0;

    UserProfile userProfile;

    Boolean temp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        progressDialog2 = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        //metadata = mAuth.getCurrentUser().getMetadata();


        Goal = findViewById(R.id.etGoal);
        logout = findViewById(R.id.btnLogout);
        present = findViewById(R.id.etPresent);
        total = findViewById(R.id.etTotal);
        per = findViewById(R.id.tvCurrentPer);
        need = findViewById(R.id.tvNeed);
        yesbtn = findViewById(R.id.imgbtnYES);
        nobtn = findViewById(R.id.imgbtnNO);

        progressDialog2.setMessage("Please Wait");
        progressDialog2.show();



        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserProfile userProfile = dataSnapshot.child("user : " + mAuth.getUid()).getValue(UserProfile.class);

                progressDialog2.dismiss();

                if (userProfile != null) {
                    no_present = userProfile.getUserPresent();
                    no_total = userProfile.getUserTotal();
                    goal = userProfile.getUserGoal();

                    present.setText(String.valueOf(userProfile.getUserPresent()));
                    total.setText(String.valueOf(userProfile.getUserTotal()));
                    Goal.setText(String.valueOf(userProfile.getUserGoal()));

                    showclass();
                }

                present.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            no_present = Integer.parseInt(present.getText().toString());
                            showclass();
                        }
                        catch (NumberFormatException ignored) { }
                    }
                });

                total.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            no_total = Integer.parseInt(total.getText().toString());
                            showclass();
                        }catch (NumberFormatException ignored) {}

                    }
                });

                Goal.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        try {

                        } catch (NumberFormatException ignored) { }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        {
                            try {
                                goal = Integer.parseInt(s.toString());
                                showclass();
                            } catch (NumberFormatException ignored) {
                            }
                        }


                    }
                });

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        no_present = no_present + 1;
                        no_total = no_total + 1;
                        int temp1 = no_present;
                        int temp2 = no_total;
                        present.setText(String.valueOf(temp1));
                        total.setText(String.valueOf(temp2));
                        senduserdata();
                        showclass();
                    }
                });

                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        no_total = no_total + 1;
                        int temp2 = no_total;
                        total.setText(String.valueOf(temp2));
                        senduserdata();
                        showclass();
                    }
                });


                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        senduserdata();
                        mAuth.signOut();
                        startActivity(new Intent(SecondActivity.this, MainActivity.class));
                        SecondActivity.this.finish();

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", "onCancelled: " + databaseError.toException());

            }

        });
    }

    private float percent(float no_present,float no_total)
    {
        float p;
        p = (no_present/no_total)*100;
        return p;
    }

    private void needtoattend(float no_present,float no_total, int goal)
    {
        int i;
        double result =0;
        result = Math.ceil(((goal*no_total)-(no_present*100))/(100-goal));
        i = (int)result;
        need.setText("You need to ATTEND " + i + " more classes");
    }

    private void leaveclass(float no_present, float no_total, int goal)
    {
        int i;
        double result = 0;
        result = Math.ceil(((no_present*100)/goal)-no_total);
        i = (int)result;
        need.setText("You can LEAVE " + i + " more classes");
    }

    private void showclass()
    {
         if (no_total == 0 && (Integer.parseInt(total.getText().toString())==0) && total.getText().toString().isEmpty())
            {
                total.setText("");
                //Goal.setText("");
                per.setText("Attendance");
                need.setText("NO. OF CLASSES NEEDED");
            }
            else
                {
                percentage = percent(no_present, no_total);
                per.setText(percentage + "%");

                int pp = (int) Math.floor(percentage);
                if (pp < goal)
                    needtoattend(no_present, no_total, goal);
                else if (pp > goal)
                    leaveclass(no_present, no_total, goal);
                else
                    need.setText("You are on track, you can't miss any class now");
                }
    }

    private void senduserdata()
    {
        userProfile = new UserProfile(no_present,no_total,goal);
        myRef.child("user : " + mAuth.getUid()).setValue(userProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SecondActivity.this, "Data Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        senduserdata();
    }
}
