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

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    public static MqttAndroidClient client;
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

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://localhost:1883",
                clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Connected

                    //TO PUBLISH
                    String topic = "/foo/bar";
                    String payload = "the payload";
                    try {
                        MqttMessage message = new MqttMessage(payload.getBytes());
                        message.setQos(2);
                        message.setRetained(false);
                        client.publish(topic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    //TO SUBSCRIBE
                    try {
                        IMqttToken subToken = client.subscribe(topic, 2);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // The message was published
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Error
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
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
