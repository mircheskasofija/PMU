package com.example.splash.guide;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.splash.LoginActivity;
import com.example.splash.R;
import com.example.splash.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GuideProfileFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceVisits;

    TextView info;
    Button logout, guideVisits;
    String userID;

    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_guide_profile, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser newUser = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users/"+newUser.getUid());
        info = inflate.findViewById(R.id.profile_guide);
        logout = inflate.findViewById(R.id.logout_button);
        guideVisits = inflate.findViewById(R.id.guideVisits);

        logout.setTranslationY(200);
        guideVisits.setTranslationY(200);
        logout.setAlpha(v);
        guideVisits.setAlpha(v);
        logout.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(200).start();
        guideVisits.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(200).start();



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });



        guideVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), ViewGuideVisits.class));
            }
        });


        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser newUser = mAuth.getCurrentUser();
        userID = newUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users/"+newUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value = snapshot.getValue(User.class);

                String source = ("Име и Фамилия: " + "<b>" + value.getName() + "</b>" + "<br>Емаил: "
                        + "<b>" + value.getEmail() + "</b>" + "<br>Тип на потребител: " + "<b>" + value.getType() + "</b>").toString();
                info.setText(Html.fromHtml(source));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}