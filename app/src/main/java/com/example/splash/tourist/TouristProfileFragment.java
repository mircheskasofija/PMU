package com.example.splash.tourist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class TouristProfileFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView info;
    Button logout, visits;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_tourist_profile, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser newUser = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users/"+newUser.getUid());
        info = inflate.findViewById(R.id.profile_tourist);
        logout = inflate.findViewById(R.id.logout_button);
        visits = inflate.findViewById(R.id.touristVisits);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        visits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewTouristVisits.class));
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

                String source = ("<b> Име и фамилия: </b>" + value.getName() + "<br><b> Емаил: </b>"
                        + value.getEmail() + "<br><b> Тип на потребител: </b>" + value.getType()).toString();
                info.setText(Html.fromHtml(source));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Неуспешно!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}