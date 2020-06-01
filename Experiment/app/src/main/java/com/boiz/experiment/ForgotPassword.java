package com.boiz.experiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText FPemail;
    private Button FPcontinue;
    private TextView FPsignin;
    private FirebaseAuth mAuth;
    private ProgressDialog pd3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        pd3 = new ProgressDialog(this);

        FPemail = findViewById(R.id.etFPemail);
        FPsignin = findViewById(R.id.tvFPsigninhere);
        FPcontinue = findViewById(R.id.btnContinue);
        mAuth = FirebaseAuth.getInstance();

        FPcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resetemail = FPemail.getText().toString().trim();
                if (resetemail.isEmpty())
                {
                    Toast.makeText(ForgotPassword.this, "Please enter your E-Mail ID", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pd3.setMessage("Validating, Please Wait");
                    pd3.show();
                    mAuth.sendPasswordResetEmail(resetemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                pd3.dismiss();
                                Toast.makeText(ForgotPassword.this, "Check your E-mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                            }
                            else
                            {
                                pd3.dismiss();
                                Toast.makeText(ForgotPassword.this, "Wrong E-mail. Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        FPsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
            }
        });


    }
}
