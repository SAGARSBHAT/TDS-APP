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

public class RegActivity extends AppCompatActivity {
    private EditText email;
    private  EditText pass;
    private Button signin;
    private Button signup;
    //Firebase...
    private FirebaseAuth mauth;

    private ProgressDialog mdia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        mauth=FirebaseAuth.getInstance();
        mdia= new ProgressDialog(this);

        email=findViewById(R.id.email_login);
        pass=findViewById(R.id.password_login);
        signin=findViewById(R.id.btnsignin);
        signup=findViewById(R.id.btnsignup);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String me = email.getText().toString().trim();
                String mp = pass.getText().toString().trim();
                if (TextUtils.isEmpty(me)) {
                    email.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(mp)) {
                    pass.setError("Required Field");
                    return;
                }

                mdia.setMessage("Processing.....");
                mdia.show();
                mauth.createUserWithEmailAndPassword(me,mp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mdia.dismiss();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            Toast.makeText(getApplicationContext(), "Sucessfully Created", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                                mdia.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed To Create", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}