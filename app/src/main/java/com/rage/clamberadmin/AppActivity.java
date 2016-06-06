package com.rage.clamberadmin;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity to support Walls, Wall Sections and ClimbUpdate Fragments
 */
public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.app_activity_frame_layout, WallsFragment.newInstance());
        transaction.commit();
    }
}
