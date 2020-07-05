package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button signin;
    private Button signup;

    private FirebaseAuth mauth;
    private ProgressDialog mdia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.email_login);
        password=findViewById(R.id.password_login);
        signin=findViewById(R.id.btnsignin);
        signup=findViewById(R.id.btnsignup);

        mauth=FirebaseAuth.getInstance();

        if(mauth.getCurrentUser()!=null) {

            startActivity(new Intent(getApplicationContext(),HomeActivity.class   ));

        }


        mdia=new ProgressDialog(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String me=email.getText().toString().trim();
                String mp=password.getText().toString().trim();
                if(TextUtils.isEmpty(me))
                {
                    email.setError("Required Field");
                    return;
                }
                if(TextUtils.isEmpty(mp))
                {
                    password.setError("Required Field");
                    return;
                }

                mdia.setMessage("Processing");
                mdia.show();
                mauth.signInWithEmailAndPassword(me,mp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mdia.dismiss();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            Toast.makeText(getApplicationContext(), "Login Completed", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mdia.dismiss();
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });






            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegActivity.class));
            }
        });

    }
}