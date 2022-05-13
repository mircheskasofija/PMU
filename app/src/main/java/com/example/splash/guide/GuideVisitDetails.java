package com.example.splash.guide;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class GuideVisitDetails extends AppCompatActivity {

    TextView city, name, description, date, location, details;
    Button accept, reject, delete;
    String key;
    private DatabaseReference databaseReferenceVisits, databaseReferenceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_visit_details);

        city = findViewById(R.id.city_guide_details);
        name = findViewById(R.id.nameOfVisit_guide_details);
        description = findViewById(R.id.description_guide_details);
        date = findViewById(R.id.date_guide_details);
        location = findViewById(R.id.locationAddress_details);
        details = findViewById(R.id.visit_guide_details);
        key = getIntent().getStringExtra("visit");
        LinearLayout layout = findViewById(R.id.accept_reject);
        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        delete = findViewById(R.id.delete);
        databaseReferenceVisits = FirebaseDatabase.getInstance().getReference("visits/" + key);
        databaseReferenceVisits.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Visits value = snapshot.getValue(Visits.class);

                databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users/" + value.getTourist());

                if(value.getStatus().equals("Active") == false){
                    databaseReferenceUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            String source = "<b> Име: </b>" + user.getName() + "<br><b> Е-маил: </b>" + user.getEmail();
                            details.setText(Html.fromHtml(source));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(GuideVisitDetails.this, "Неуспешно!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                city.setText("Град: " + value.getCity());
                name.setText("Наименование: " + value.getName());
                description.setText("Описание: " + value.getDescription());
                date.setText("Дата: " + value.getDate());
                location.setText("Адреса: " + value.getLocationOnMap());

                if(value.getStatus().equals("Активна") || value.getStatus().equals("Завършена")){
                    layout.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("visits/" + key);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().removeValue();
                                    Intent intent = new Intent(GuideVisitDetails.this, ViewGuideVisits.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }

                if(value.getStatus().equals("Резервирана")){
                    layout.setVisibility(View.VISIBLE);
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReferenceVisits.child("status").setValue("Приета");
                            startActivity(new Intent(GuideVisitDetails.this, GuideActivity.class));
                        }
                    });

                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReferenceVisits.child("status").setValue("Активна");
                            startActivity(new Intent(getApplicationContext(), GuideActivity.class));

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GuideVisitDetails.this, "Неуспешно!", Toast.LENGTH_SHORT).show();

            }
        });

    }
}