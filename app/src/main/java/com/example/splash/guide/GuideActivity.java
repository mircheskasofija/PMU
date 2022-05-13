package com.example.splash.guide;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.splash.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class GuideActivity extends AppCompatActivity{

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        chipNavigationBar = findViewById(R.id.nav_menu_guide);
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_fragment_guide, new GuideHomeFragment()).commit();

        bottomMenuGuide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        chipNavigationBar = findViewById(R.id.nav_menu_guide);
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_fragment_guide, new GuideHomeFragment()).commit();

        bottomMenuGuide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chipNavigationBar = findViewById(R.id.nav_menu_guide);
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_fragment_guide, new GuideHomeFragment()).commit();

        bottomMenuGuide();
    }

    private void bottomMenuGuide() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.bottom_nav_home_guide:
                        fragment = new GuideHomeFragment();
                        break;
                    case R.id.bottom_nav_add_guide:
                        fragment = new GuideAddFragment();
                        break;
                    case R.id.bottom_nav_profile_guide:
                        fragment = new GuideProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.navigation_fragment_guide, fragment).commit();
            }
        });
    }


}
