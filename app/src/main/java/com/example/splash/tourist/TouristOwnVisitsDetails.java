package com.example.splash.tourist;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splash.R;
import com.example.splash.User;
import com.example.splash.Visits;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TouristOwnVisitsDetails extends AppCompatActivity {

    TextView city, name, description, date, location, info, text;
    String key;
    Button finish;
    /*private*/ DatabaseReference databaseReferenceVisits, databaseReferenceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_own_visits_details);

        info = findViewById(R.id.visit_details);
        city = findViewById(R.id.city_details);
        name = findViewById(R.id.nameOfVisit_details);
        description = findViewById(R.id.description_details);
        date = findViewById(R.id.date_details);
        key=getIntent().getStringExtra("ownVisit");
        location = findViewById(R.id.locationAddress_details);
        text = findViewById(R.id.text);
        finish = findViewById(R.id.acceptvisit);

        databaseReferenceVisits = FirebaseDatabase.getInstance().getReference("visits/" + key);
        databaseReferenceVisits.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Visits value = snapshot.getValue(Visits.class);

                databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users/" + value.getUser());
                databaseReferenceUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String source = "<b> Име и фамилия: </b>" + user.getName() + "<br><b> Емаил: </b>" +user.getEmail();
                        info.setText(Html.fromHtml(source));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TouristOwnVisitsDetails.this, "Неуспешно!", Toast.LENGTH_SHORT).show();
                    }
                });

                city.setText("Град: " + value.getCity());
                name.setText("Наименование: " + value.getName());
                description.setText("Описание: " + value.getDescription());
                date.setText("Дата: " + value.getDate());
                location.setText("Адреса: " + value.getLocationOnMap());

                if(value.getStatus().equals("Приета")){
                    finish.setVisibility(View.VISIBLE);
                    finish.setText("Офертата е приключила");
                    finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(TouristOwnVisitsDetails.this, "Офертата е приключила!", Toast.LENGTH_SHORT).show();
                            databaseReferenceVisits.child("status").setValue("Завършена");
                            finish.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                if(value.getStatus().equals("Завършена")){
                    text.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TouristOwnVisitsDetails.this, "Неуспешно!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}