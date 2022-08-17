package com.dp.firstproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dp.firstproject.Models.User;
import com.dp.firstproject.SpinWheel.LuckyWheelView;
import com.dp.firstproject.SpinWheel.model.LuckyItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SpinWheelActivity extends AppCompatActivity {

    SweetAlertDialog pDialog;
    int count = 1;
    String GameID = "4733119";
    String AdID = "Interstitial_Android";
    boolean TestMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinwheel);

        ImageView gobackbtn = findViewById(R.id.gobackbtn);


        Button spin_btn = findViewById(R.id.spin_btn);


        UnityAds.initialize(SpinWheelActivity.this, GameID, TestMode);
        loadInterstitialAd();









        gobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SpinWheelActivity.this, MainActivity.class));
                finish();
            }
        });

        pDialog = new SweetAlertDialog(SpinWheelActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#E84F97"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);


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













        LuckyWheelView wheelView = findViewById(R.id.wheelview);


















        List<LuckyItem> data = new ArrayList<>();

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = "5";
        luckyItem1.secondaryText = "COINS";
        luckyItem1.textColor = Color.parseColor("#212121");
        luckyItem1.color = Color.parseColor("#eceff1");
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = "10";
        luckyItem2.secondaryText = "COINS";
        luckyItem2.color = Color.parseColor("#00cf00");
        luckyItem2.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = "15";
        luckyItem3.secondaryText = "COINS";
        luckyItem3.textColor = Color.parseColor("#212121");
        luckyItem3.color = Color.parseColor("#eceff1");
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = "20";
        luckyItem4.secondaryText = "COINS";
        luckyItem4.color = Color.parseColor("#7f00d9");
        luckyItem4.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = "25";
        luckyItem5.secondaryText = "COINS";
        luckyItem5.textColor = Color.parseColor("#212121");
        luckyItem5.color = Color.parseColor("#eceff1");
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = "30";
        luckyItem6.secondaryText = "COINS";
        luckyItem6.color = Color.parseColor("#dc0000");
        luckyItem6.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.topText = "35";
        luckyItem7.secondaryText = "COINS";
        luckyItem7.textColor = Color.parseColor("#212121");
        luckyItem7.color = Color.parseColor("#eceff1");
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.topText = "0";
        luckyItem8.secondaryText = "COINS";
        luckyItem8.color = Color.parseColor("#008bff");
        luckyItem8.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem8);


        wheelView.setData(data);
        wheelView.setRound(5);

        spin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Random r = new Random();
                int randomNumber = r.nextInt(8);
                wheelView.startLuckyWheelWithTargetIndex(randomNumber);












            }




        });



        wheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override

            public void LuckyRoundItemSelected(int index) {
                updateCash(index);

                pDialog.show();

















            }


        });









    }




    void updateCash(int index){
        long cash = 0;
        switch (index){
            case 0:
                cash = 5;
                break;
            case 1:
                cash = 10;
                break;
            case 2:
                cash = 15;
                break;
            case 3:
                cash = 20;
                break;
            case 4:
                cash = 25;
                break;
            case 5:
                cash = 30;
                break;
            case 6:
                cash = 35;
                break;
            case 7:
                cash = 0;
                break;
        }

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        long finalCash = cash;
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(cash)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {



                pDialog.dismiss();

                User user12 = new User(count);

                FirebaseFirestore database = FirebaseFirestore.getInstance();

                database.collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .update("numberOfSpins", FieldValue.increment(user12.getNumberOfSpins()));












                SweetAlertDialog dialog = new SweetAlertDialog(SpinWheelActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText("Success!");
                dialog.setCancelable(false);
                dialog.setContentText(finalCash + " Coins Added In Your Account Successfully.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();



                                UnityAds.show(SpinWheelActivity.this, AdID);




                            }


                        })

                        .show();





                Toast.makeText(SpinWheelActivity.this, finalCash + " Coins Added In Your Account Successfully. ", Toast.LENGTH_SHORT).show();
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