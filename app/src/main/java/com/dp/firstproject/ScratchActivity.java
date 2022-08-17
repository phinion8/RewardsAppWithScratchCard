package com.dp.firstproject;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anupkumarpanwar.scratchview.ScratchView;

import com.dp.firstproject.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unity3d.ads.UnityAds;

import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;



public class ScratchActivity extends AppCompatActivity {

    ScratchView scratchView;
    TextView textView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore database;
    ImageView backButton;
    int count  = 0;
    String GameID = "4733119";
    String AdID = "Interstitial_Android";
    boolean TestMode = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        textView = findViewById(R.id.randomCoins);
        Random random = new Random();
        int randomCoin = random.nextInt(20);
        textView.setText(Integer.toString(randomCoin));
        backButton = findViewById(R.id.back_btn4);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScratchActivity.this, MainActivity.class));
                finish();
            }
        });


        UnityAds.initialize(ScratchActivity.this, GameID, TestMode);
        loadInterstitialAd();





        scratchView = findViewById(R.id.scratch_view);
        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                Toast.makeText(getApplicationContext(), "You Won " + randomCoin + " Coins" , Toast.LENGTH_LONG).show();
                scratchView.setVisibility(View.INVISIBLE);
                count++;

                UnityAds.show(ScratchActivity.this, AdID);



                SweetAlertDialog pDialog = new SweetAlertDialog(ScratchActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();


                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
                    SweetAlertDialog alertDialog = new SweetAlertDialog(ScratchActivity.this, SweetAlertDialog.ERROR_TYPE);
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

                database.collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .update("coins", FieldValue.increment(randomCoin)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        pDialog.dismiss();


                        User user = new User();
                        user.setNumberOfScratches(count);
                        database.collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update("numberOfScratches", FieldValue.increment(user.getNumberOfScratches()));


                       SweetAlertDialog sDialog =  new SweetAlertDialog(ScratchActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                sDialog.setTitleText("Success!");
                                sDialog.setCancelable(false);
                                sDialog.setContentText(randomCoin + " Coins Added In Your Account Successfully.");
                                sDialog.setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sDialog.dismissWithAnimation();
                               startActivity(new Intent(ScratchActivity.this, MainActivity.class));
                               finish();

                            }
                        })
                                .show();



                    }
                });





            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent>=0.5) {
                    Log.d("Reveal Percentage", "onRevealPercentChangedListener: " + String.valueOf(percent));

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