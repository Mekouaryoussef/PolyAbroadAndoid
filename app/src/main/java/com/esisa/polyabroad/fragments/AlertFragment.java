package com.esisa.polyabroad.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.esisa.polyabroad.R;
import com.esisa.polyabroad.adapters.AlertRVAdapter;
import com.esisa.polyabroad.models.ReviewModel;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlertFragment extends Fragment {

    private RecyclerView recyclerView;

    private MqttAndroidClient client;
    private View view;
    private AlertRVAdapter adapter;
    private List<ReviewModel> list = new ArrayList<>();
    private String spinnerChoice;
    private Spinner spinner;
    private ArrayAdapter adapterSpin;

    public void setClient(MqttAndroidClient client) {
        this.client = client;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alert_fragment, container, false);
        recyclerView = view.findViewById(R.id.reviewlist);
        spinner = view.findViewById(R.id.spinner);
        adapterSpin = ArrayAdapter.createFromResource(this.getContext(),
                R.array.spinner, android.R.layout.simple_spinner_item);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        spinnerChoice = "BAT";
                        break;
                    case 1:
                        spinnerChoice = "SI";
                        break;
                    case 2:
                        spinnerChoice = "MAM";
                        break;
                    default:
                        spinnerChoice = "BAT";
                }
                sendMajor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerChoice = "BAT";
                sendMajor();
            }
        });
        adapter = new AlertRVAdapter(this.getContext(), list);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void sendMajor() {
        String topic = "/android/request/alerts";
        try {
            MqttMessage message = new MqttMessage(spinnerChoice.getBytes());
            client.publish(topic, message);
            list.clear();
            getList();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void getList() {
        String topic = "/android/answer/alerts";
        try {
            IMqttToken subToken = client.subscribe(topic, 2);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    client.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            JSONArray jsonArray = new JSONArray(message.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = jsonArray.getJSONObject(i);
                                int id = o.getInt("id");
                                String student = o.getString("email");
                                String university = o.getString("universityId");
                                String text = o.getString("Description");
                                double rate = o.getDouble("Rate");
                                //String destination = o.getString("destination");
                                ReviewModel review = new ReviewModel(id, student, university, rate, text, "bonjour");
                                list.add(review);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });
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

}
