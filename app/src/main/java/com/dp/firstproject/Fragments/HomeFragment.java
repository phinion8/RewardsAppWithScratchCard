package com.dp.firstproject.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dp.firstproject.R;
import com.dp.firstproject.ScratchActivity;
import com.dp.firstproject.SpinWheelActivity;
import com.dp.firstproject.WalletActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unity3d.ads.UnityAds;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class HomeFragment extends Fragment {

    CardView scratchView, wallet, dailyCheckIn, spinWheel;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore database;
    String GameID = "4733119";
    String AdID = "Interstitial_Android";
    boolean TestMode = false;



    public HomeFragment() {
        // Required empty public constructor
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        scratchView = view.findViewById(R.id.scratchView);
        wallet = view.findViewById(R.id.wallet);
        dailyCheckIn = view.findViewById(R.id.dailyCheckin);
        spinWheel = view.findViewById(R.id.spinWheel);

        UnityAds.initialize(getContext(), GameID, TestMode);

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

        getActivity().setTitle("Home");

        scratchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ScratchActivity.class));

            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WalletActivity.class));

            }
        });

        spinWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SpinWheelActivity.class));

            }
        });





        dailyCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UnityAds.show(getActivity(), AdID);

                Calendar calendar= Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);



                String todaystring= year+ "" + month + "" + day + "";

                SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
                boolean currentDay = preferences.getBoolean(todaystring, false);

                if(!currentDay){





                    database.collection("users")
                            .document(FirebaseAuth.getInstance().getUid())
                            .update("coins", FieldValue.increment(50));







                    Toast.makeText(getContext(), "Reward Granted", Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText("50 Coins is successfully added to your account")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    UnityAds.show(getActivity(), AdID);


                                }




                            })

                            .show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(todaystring, true);
                    editor.apply();
                }else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Sorry...")
                            .setContentText("You have already received your today's reward")
                            .show();
                    Toast.makeText(getContext(), "You have already received your today's reward", Toast.LENGTH_SHORT).show();
                }




            }
        });
        return view;
    }






}