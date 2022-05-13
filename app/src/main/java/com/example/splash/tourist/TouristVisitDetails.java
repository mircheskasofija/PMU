package com.example.splash.tourist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splash.R;
import com.example.splash.Visits;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TouristVisitDetails extends AppCompatActivity {

    TextView city, name, description, date, location;
    String key;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_visit_details);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        city = findViewById(R.id.city_tourist_details);
        name = findViewById(R.id.nameOfVisit_tourist_details);
        description = findViewById(R.id.description_tourist_details);
        date = findViewById(R.id.date_tourist_details);
        location = findViewById(R.id.locationAddress_details);

        key = getIntent().getStringExtra("visit");
        databaseReference = FirebaseDatabase.getInstance().getReference("visits/" + key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Visits value = snapshot.getValue(Visits.class);

                city.setText("Град: " + value.getCity());
                name.setText("Наименовани: " + value.getName());
                description.setText("Описание: " + value.getDescription());
                date.setText("Дата: " + value.getDate());
                location.setText("Адреса: " + value.getLocationOnMap());

                if(value.getStatus().equals("Активна")){
                    Button btn = findViewById(R.id.accept_visit);
                    btn.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(TouristVisitDetails.this, "Приета!", Toast.LENGTH_SHORT).show();
                            databaseReference.child("tourist").setValue(user.getUid());
                            databaseReference.child("status").setValue("Резервирана");
                            startActivity(new Intent(getApplicationContext(), TouristProfileFragment.class));
                        }
                    });
                }
                else {
                    TextView textView = new TextView(getApplicationContext());
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                    textView.setTextSize(18);
                    textView.setText("Офертата е недостъпна");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TouristVisitDetails.this, "Неуспешно!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}