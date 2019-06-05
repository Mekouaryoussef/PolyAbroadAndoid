package com.esisa.polyabroad.fragments;

import android.support.v4.app.Fragment;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class AlertFragment extends Fragment {

    private MqttAndroidClient client;

    public void setClient(MqttAndroidClient client) {
        this.client = client;
    }
}
