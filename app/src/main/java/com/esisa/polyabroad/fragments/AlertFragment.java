package com.esisa.polyabroad.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esisa.polyabroad.R;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class AlertFragment extends Fragment {

    private MqttAndroidClient client;
    private View view;

    public void setClient(MqttAndroidClient client) {
        this.client = client;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alert_fragment, container, false);
        return view;
    }


}
