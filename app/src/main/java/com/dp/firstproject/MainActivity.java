package com.dp.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.dp.firstproject.Fragments.AboutFragment;
import com.dp.firstproject.Fragments.ContactFragment;
import com.dp.firstproject.Fragments.HomeFragment;
import com.dp.firstproject.Fragments.ProfileFragment;
import com.dp.firstproject.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    Fragment fragment = null;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigation_View);
        navigationView.setItemIconTintList(null);
        drawerLayout = findViewById(R.id.drawer);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

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



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, new HomeFragment());
        fragmentTransaction.commit();

       View header = navigationView.getHeaderView(0);
       TextView header_email = header.findViewById(R.id.header_email);
       TextView header_name = header.findViewById(R.id.header_username);


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            String email = firebaseAuth.getCurrentUser().getEmail();
            header_email.setText(email);
        }


        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                header_name.setText(String.valueOf(user.getName()));

            }
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.home:
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.about:
                        fragment = new AboutFragment();
                        loadFragment(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.contact:
                        fragment = new ContactFragment();
                        loadFragment(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.profile:
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.privacy:
                        Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);


                }
                return true;
            }
        });

    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentTransaction.addToBackStack(null);
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure you want to exit?")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .setConfirmText("Yes").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finish();
                System.exit(0);
            }
        })
                .show();


    }


}