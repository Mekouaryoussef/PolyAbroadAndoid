package com.esisa.polyabroad.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.esisa.polyabroad.R;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class HomeFragment extends Fragment {

    private MqttAndroidClient client;
    private View view;
    private int spinnerChoice;

    private Spinner spinner;
    private ArrayAdapter adapter;

    public void setClient(MqttAndroidClient client) {
        this.client = client;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        spinner = view.findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        spinnerChoice = position;
                        break;
                    case 1:
                        spinnerChoice = position;
                        break;
                    case 2:
                        spinnerChoice = position;
                        break;
                    default:
                        spinnerChoice = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerChoice = 0;
            }
        });
        return view;
    }
}
