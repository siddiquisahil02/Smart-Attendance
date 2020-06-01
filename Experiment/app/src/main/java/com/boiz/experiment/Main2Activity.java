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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class Main2Activity extends AppCompatActivity {

    private EditText name, email, password, re_password;
    private Button Register;
    private TextView already;
    private FirebaseAuth mAuth;
    private ProgressDialog pd2;

    public String user_n,user_em,user_pass,user_repass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);
        setupUIViews();

        mAuth = FirebaseAuth.getInstance();

        pd2 = new ProgressDialog(this);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate())
                {
                    String user_name = name.getText().toString();
                    String user_email = email.getText().toString().trim();
                    String user_password = password.getText().toString().trim();
                    String user_repassword = re_password.getText().toString().trim();

                    if(user_password.equals(user_repassword))
                    {
                        mAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    sendemailveri();
                                    Toast.makeText(Main2Activity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                    //senduserdata();
                                }
                                else
                                {
                                    Toast.makeText(Main2Activity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Main2Activity.this,"Password don't match", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,MainActivity.class));
                finish();
            }
        });

    }

    private void setupUIViews()
    {
        name = findViewById(R.id.etName_reg);
        email = findViewById(R.id.etEmail_reg);
        password = findViewById(R.id.etPass_reg);
        re_password = findViewById(R.id.etRe_Pass_reg);
        Register = findViewById(R.id.btnRegister);
        already = findViewById(R.id.tbAlready);
    }

    private Boolean validate()
    {
        Boolean result = Boolean.FALSE;
        user_n = name.getText().toString();
        user_em = email.getText().toString();
        user_pass = password.getText().toString();
        user_repass = re_password.getText().toString();

        if (user_n.isEmpty() || user_em.isEmpty() || user_pass.isEmpty() || user_repass.isEmpty())
        {
            Toast.makeText(this, "Enter all the details",Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = Boolean.TRUE;
        }
        return result;
    }

    private void sendemailveri()
    {
        pd2.setMessage("Validating, Please wait");
        pd2.show();
        final FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        //senduserdata();
                        pd2.dismiss();
                        Toast.makeText(getApplicationContext(),"Verification E-Mail sent",Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(Main2Activity.this,MainActivity.class));
                    }
                    else
                    {
                        pd2.dismiss();
                        Toast.makeText(getApplicationContext(),"Unable to send the verification E-Mail ",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
