package com.esisa.polyabroad.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.esisa.polyabroad.R;
import com.esisa.polyabroad.adapters.FragmentAdapter;
import com.esisa.polyabroad.fragments.AlertFragment;
import com.esisa.polyabroad.fragments.HomeFragment;
import com.esisa.polyabroad.fragments.ReviewsFragment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {

    private AlertFragment alertFragment = new AlertFragment();
    private HomeFragment homeFragment = new HomeFragment();
    private ReviewsFragment reviewsFragment = new ReviewsFragment();
    private MqttAndroidClient client;
    private TabLayout tabLayout;
    private ViewPager vwPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vwPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://localhost:1883",
                clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Connected
                    homeFragment.setClient(client);
                    alertFragment.setClient(client);
                    reviewsFragment.setClient(client);
                    setupViewPager(vwPager);
                    tabLayout.setupWithViewPager(vwPager);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Error
                    System.exit(0);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment, "Accueil");
        adapter.addFragment(alertFragment, "Alertes");
        adapter.addFragment(reviewsFragment, "Avis");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
