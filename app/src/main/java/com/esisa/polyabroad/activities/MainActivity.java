package com.esisa.polyabroad.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;

import com.esisa.polyabroad.R;
import com.esisa.polyabroad.adapters.FragmentAdapter;
import com.esisa.polyabroad.fragments.Alertes;
import com.esisa.polyabroad.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private Bundle mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager vwPager;

        if (savedInstanceState != null)
            mState = savedInstanceState;

        final Intent myIntent = getIntent();
        if (savedInstanceState != null) {
            //connectedProfile = savedInstanceState.getParcelable("currentProfile");
        }

        vwPager = findViewById(R.id.container);
        setupViewPager(vwPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vwPager);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the state
        //savedInstanceState.putParcelable("currentProfile", connectedProfile);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        HomeFragment homeFragment = new HomeFragment();
        if (mState != null) {
            //connectedProfile = mState.getParcelable("currentProfile");
        }
        adapter.addFragment(homeFragment, "Home");

        Alertes alertes = new Alertes();
        adapter.addFragment(alertes, "Alertes");

        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
