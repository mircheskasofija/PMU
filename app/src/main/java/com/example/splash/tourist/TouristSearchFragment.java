package com.example.splash.tourist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.splash.R;


public class TouristSearchFragment extends Fragment {

    Button visits, activeVisits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_tourist_search, container, false);

        visits = inflate.findViewById(R.id.guideVisits);
        activeVisits = inflate.findViewById(R.id.activeVisits);

        visits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TouristViewAllVisits.class));
            }
        });

        activeVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActiveVisits.class));
            }
        });

        return inflate;
    }
}