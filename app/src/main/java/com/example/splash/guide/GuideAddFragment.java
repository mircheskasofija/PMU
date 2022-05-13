package com.example.splash.guide;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.splash.MapActivity;
import com.example.splash.R;
import com.example.splash.Visits;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class GuideAddFragment extends Fragment {

    private Calendar calendar;
    final Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat;
    private String dateForm;
    EditText city, name, description;
    TextView date, location;
    Button map, add;
    String _city, _name, _description, _date, _location;

    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_guide_add, container, false);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateForm = dateFormat.format(calendar.getTime());

        city = inflate.findViewById(R.id.city_guide);
        name = inflate.findViewById(R.id.nameOfVisit_guide);
        description = inflate.findViewById(R.id.description_guide);
        date = inflate.findViewById(R.id.date_guide);
        location = inflate.findViewById(R.id.locationAddress);
        map = inflate.findViewById(R.id.buttonSetLocation);
        add = inflate.findViewById(R.id.addVisit);

        city.setTranslationX(800);
        name.setTranslationX(800);
        description.setTranslationX(800);
        date.setTranslationX(800);
        location.setTranslationX(800);
        map.setTranslationY(200);
        add.setTranslationY(400);

        city.setAlpha(v);
        name.setAlpha(v);
        description.setAlpha(v);
        date.setAlpha(v);
        location.setAlpha(v);
        map.setAlpha(v);
        add.setAlpha(v);

        city.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        name.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        description.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        date.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        location.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        map.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        add.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(1300).start();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("visits");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat);
                date.setText(dateFormat.format(myCalendar.getTime())  );
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),datePicker, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                _city = city.getText().toString();
                _name = name.getText().toString();
                _description = description.getText().toString();
                _date = date.getText().toString();
                _location = location.getText().toString();
                String _status = "Активна";
                Visits visit = new Visits(user, _city, _name, _location, _date, _status, "", _description);

                FirebaseDatabase.getInstance().getReference("visits").child(UUID.randomUUID().toString()).setValue(visit).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Офертата е успешно добавена!", Toast.LENGTH_SHORT).show();
                            city.getText().clear();
                            name.getText().clear();
                            description.getText().clear();
                            date.setText(null);
                            location.setText(null);

                            startActivity(new Intent(getActivity(), GuideActivity.class));

                        }
                        else{
                            Toast.makeText(getActivity(), "Неуспешно!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        location.setText(getActivity().getIntent().getStringExtra("extra"));

    }
}