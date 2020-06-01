package com.boiz.experiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText useremail,userpass;
    private Button login;
    private TextView signin,forgot;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }

        useremail = (EditText) findViewById(R.id.etEntrEmail);
        userpass = (EditText) findViewById(R.id.etEntrPassword);
        login = (Button) findViewById(R.id.btnLogin);
        signin = (TextView) findViewById(R.id.tvRegisterhere);
        forgot = (TextView) findViewById(R.id.tvNeedHelp);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = useremail.getText().toString();
                String pass = userpass.getText().toString();

                validate2(email,pass);

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });

    }

    private void validate2(String ema , String pas)
    {
        progressDialog.setMessage("Validating, Please wait");
        progressDialog.show();
        final Boolean[] result = new Boolean[1];
        mAuth.signInWithEmailAndPassword(ema,pas).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    checkEmailver();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Invalid Credentials...Try Again",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void checkEmailver() {
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();
        if (emailFlag)
        {
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.signOut();
            Toast.makeText(this, "Verify your E-MAIL first", Toast.LENGTH_SHORT).show();
        }
    }

}
