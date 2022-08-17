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

import com.dp.firstproject.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    TextView login_text;
    Button signup_btn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore database;
    EditText email, pass, name;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup_btn = findViewById(R.id.signup_btn);
        email = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signup_pass);
        name = findViewById(R.id.signup_name);
        login_text = findViewById(R.id.login_btn2);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();



        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);

            }
        });



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating a new account...");

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1, pass1, name1;
                email1 = email.getText().toString().trim();
                pass1 = pass.getText().toString().trim();
                name1 = name.getText().toString();

                User user = new User(name1, email1, pass1);

                if(TextUtils.isEmpty(name1)){
                    Toast.makeText(SignUpActivity.this, "Name field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email1)){
                    Toast.makeText(SignUpActivity.this, "Email field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(pass1)){
                    Toast.makeText(SignUpActivity.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }



                progressDialog.show();
                progressDialog.setCancelable(false);
                firebaseAuth.createUserWithEmailAndPassword(email1, pass1)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    String uid = task.getResult().getUser().getUid();
                                    database.collection("users")
                                            .document(uid)
                                            .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    // Sign in success, update UI with the signed-in user's information




                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    // If sign in fails, display a message to the user.

                                }
                            }
                        });






            }
        });

    }
}