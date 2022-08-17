package com.dp.firstproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dp.firstproject.LogInActivity;
import com.dp.firstproject.Models.User;
import com.dp.firstproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    TextView profile_name, profile_email;
    Button logOutBtn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore database;
    User user;




    public ProfileFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        profile_name = view.findViewById(R.id.profile_name);
        profile_email = view.findViewById(R.id.profile_email);
        logOutBtn = view.findViewById(R.id.logOutBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        getActivity().setTitle("Profile");

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                profile_name.setText(String.valueOf(user.getName()));
                profile_email.setText(String.valueOf(user.getEmail()));
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                getActivity().finish();
                signOutUser();
            }

            private void signOutUser() {
                startActivity(new Intent(getContext(), LogInActivity.class));
                getActivity().finish();
            }
        });



        return view;
    }
}