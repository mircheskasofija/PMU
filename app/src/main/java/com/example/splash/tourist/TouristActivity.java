package com.example.splash.tourist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.splash.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class TouristActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist);

        chipNavigationBar = findViewById(R.id.nav_menu_tourist);
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_fragment_tourist, new TouristHomeFragment()).commit();

        bottomMenuTourist();
    }

    private void bottomMenuTourist(){
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.bottom_nav_home_tourist:
                        fragment = new TouristHomeFragment();
                        break;
                    case R.id.bottom_nav_search_tourist:
                        fragment = new TouristSearchFragment();
                        break;
                    case R.id.bottom_nav_profile_tourist:
                        fragment = new TouristProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.navigation_fragment_tourist, fragment).commit();
            }
        });
    }

}