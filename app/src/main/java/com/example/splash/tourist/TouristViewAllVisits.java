package com.example.splash.tourist;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TouristViewAllVisits extends AppCompatActivity {
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_view_all_visits);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot dataSnapshot = snapshot.child("visits");
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot ds : children){
                    Drawable drawable = getDrawable(R.drawable.white_outline);
                    Drawable buttonDrawable = getDrawable(R.drawable.button_bg);
                    LinearLayout layout = new LinearLayout(getApplicationContext());
                    layout.setBackground(drawable);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    TextView text = new TextView(getApplicationContext());
                    Button detailsButton = new Button(getApplicationContext());
                    detailsButton.setText("Click for details");
                    detailsButton.setBackground(buttonDrawable);
                    detailsButton.setTextColor(Color.parseColor("#FFFFFF"));
                    text.setTextColor(Color.parseColor("#FFFFFF"));
                    text.setTextSize(18);
                    text.setPadding(50,50,50,40);
                    String source = "<br><b>Град: </b>" + ds.child("city").getValue(String.class)
                            + "<br><b>Наименование: </b>" + ds.child("name").getValue(String.class) +
                            "<br><b>Описание: </b>" + ds.child("description").getValue(String.class)
                            + "<br><b>Дата: </b>" + ds.child("date").getValue(String.class) +
                            "<br><b>Адреса: </b>" + ds.child("locationOnMap").getValue(String.class)
                            + "<br><b>Статус: </b>" + ds.child("status").getValue(String.class);
                    text.setText(Html.fromHtml(source));
                    layout.addView(text);
                    layout.addView(detailsButton);
                    linearLayout.addView(layout);
                    detailsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), TouristVisitDetails.class);
                            intent.putExtra("visit", ds.getKey());
                            startActivity(intent);
                            //startActivity(new Intent(getApplicationContext(), GuideVisitDetails.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TouristViewAllVisits.this, "Неуспешно!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = new Bundle();
        TouristSearchFragment fragment = new TouristSearchFragment();
    }
}