package com.example.splash;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.splash.guide.GuideAddFragment;

public class MapHelper extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new GuideAddFragment()).commit();
        }
    }
}