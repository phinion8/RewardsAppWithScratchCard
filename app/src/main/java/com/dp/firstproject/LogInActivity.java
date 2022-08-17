package com.dp.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    TextView textView;
    EditText email , pass;
    Button login_button;
    String email1 , pass1;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        textView = findViewById(R.id.signup_btn2);

        email = findViewById(R.id.login_email);

        pass = findViewById(R.id.login_pass);

        login_button = findViewById(R.id.login_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");





        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this , SignUpActivity.class);
                startActivity(intent);
                finish();

            }

        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progressDialog.show();
                progressDialog.setCancelable(false);

                email1 = email.getText().toString().trim();
                pass1 = pass.getText().toString().trim();

                if (TextUtils.isEmpty(email1)){
                    Toast.makeText(LogInActivity.this, "Email field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass1)){
                    Toast.makeText(LogInActivity.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(LogInActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this , MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(LogInActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });



    }


}