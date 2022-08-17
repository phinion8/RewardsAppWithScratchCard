package com.dp.firstproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.firstproject.Models.User;
import com.dp.firstproject.Models.WithdrawalRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unity3d.ads.UnityAds;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WalletActivity extends AppCompatActivity {

    ImageView back_btn;
    TextView coinText;
    EditText withdrawInfo;
    Button requestBtn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore database;
    User user;
    CollectionReference collectionReference;
    String GameID = "4733119";
    String AdID = "Interstitial_Android";
    boolean TestMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        back_btn = findViewById(R.id.back_btn3);
        coinText = findViewById(R.id.coinText);
        withdrawInfo = findViewById(R.id.withdrawinfo);
        requestBtn = findViewById(R.id.submitWithdraw);


        UnityAds.initialize(WalletActivity.this, GameID, TestMode);
        loadInterstitialAd();


        SweetAlertDialog pDialog = new SweetAlertDialog(WalletActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();



        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            alertDialog.setTitleText("Oops...");
            alertDialog.setContentText("Something went wrong! Please Check Your Internet Connection." );
            alertDialog.setCancelable(false);
            alertDialog.setConfirmText("Retry")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            recreate();
                        }
                    });
            alertDialog.show();
        }


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WalletActivity.this, MainActivity.class));
                finish();
            }
        });

        String uid = FirebaseAuth.getInstance().getUid();

        database.collection("users")
                .document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pDialog.dismissWithAnimation();
                user = documentSnapshot.toObject(User.class);
                coinText.setText(String.valueOf(user.getCoins()));
                UnityAds.show(WalletActivity.this, AdID);
            }
        });


        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pDialog.show();




                if (user.getCoins() >= 5000){

                    requestBtn.setEnabled(false);

                    String withdrawEmail = withdrawInfo.getText().toString();

                    if (TextUtils.isEmpty(withdrawEmail)){

                        pDialog.dismiss();
                        requestBtn.setEnabled(true);
                        Toast.makeText(WalletActivity.this, "Please enter your email or phone number or upi id", Toast.LENGTH_SHORT).show();
                        return;

                    }

                    String uid = FirebaseAuth.getInstance().getUid();

                    WithdrawalRequest withdrawalRequest = new WithdrawalRequest(withdrawEmail, uid, user.getEmail(), user.getNumberOfSpins(), user.getNumberOfScratches());

                    collectionReference = database.collection("withdraws");

                    collectionReference.add(withdrawalRequest).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            pDialog.dismiss();
                            SweetAlertDialog sDialog =  new SweetAlertDialog(WalletActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            sDialog.setTitleText("Success!");
                                    sDialog.setCancelable(false);
                                    sDialog.setContentText("You will receive your money in 24 hours.");
                                    sDialog.setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sDialog.dismissWithAnimation();
                                    startActivity(new Intent(WalletActivity.this, MainActivity.class));
                                    finish();

                                }
                            })
                                    .show();
                        }
                    });

                    Toast.makeText(WalletActivity.this, "Request sent successfully.", Toast.LENGTH_SHORT).show();

                    database.collection("users")
                            .document(FirebaseAuth.getInstance().getUid())
                            .update("coins", FieldValue.increment(-5000));


                } else {
                    pDialog.dismiss();

                    SweetAlertDialog alertDialog = new SweetAlertDialog(WalletActivity.this, SweetAlertDialog.ERROR_TYPE);
                    alertDialog.setTitleText("Sorry...");
                    alertDialog.setContentText("You need at least 5000 coins to withdraw");
                    alertDialog.setCancelable(false);
                    alertDialog.show();


                    Toast.makeText(WalletActivity.this, "You need at at least 5000 coins to withdraw", Toast.LENGTH_SHORT).show();


                }


            }
        });




    }


    private void loadInterstitialAd() {

        if (UnityAds.isInitialized()){
            UnityAds.load(AdID);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    UnityAds.load(AdID);
                }
            },5000);
        }

    }



}