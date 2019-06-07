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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.esisa.polyabroad.R;
import com.esisa.polyabroad.models.ReviewModel;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;

public class ReviewsFragment extends Fragment {
    private MqttAndroidClient client;
    private View view;

    private Button delete;
    private Button validate;
    private Spinner spinner;
    private ArrayAdapter adapterSpin;
    private String spinnerChoice;
    private ReviewModel review;
    private TextView avis;
    private TextView university;
    private TextView nom;
    private TextView date;

    public void setClient(MqttAndroidClient client) {
        this.client = client;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.avis_fragment, container, false);
        spinner = view.findViewById(R.id.spinner);
        avis = view.findViewById(R.id.avis);
        university = view.findViewById(R.id.university);
        nom = view.findViewById(R.id.nom);
        date = view.findViewById(R.id.dates);
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
        delete = view.findViewById(R.id.cancelButton);
        validate = view.findViewById(R.id.add_dataButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review != null) {
                    rejectReview(review);
                }
            }
        });
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review != null) {
                    acceptReview(review);
                }
            }
        });
        return view;
    }

    private void sendMajor() {
        String topic = "/android/request/peekreview";
        try {
            MqttMessage message = new MqttMessage(spinnerChoice.getBytes());
            client.publish(topic, message);
            getReview();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void getReview() {
        String topic = "/android/answer/peekreview";
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

                            review = null;
                            nom.setText(null);
                            university.setText(null);
                            avis.setText(null);
                            date.setText(null);

                            JSONObject o = new JSONObject(message.toString());
                            System.out.println(o);
                            String id = o.getString("id");

                            JSONObject profile = o.getJSONObject("profile");
                            String student = profile.getString("firstName") + " " + profile.getString("lastName");
                            String email = profile.getString("email");

                            JSONObject univ = o.getJSONObject("university");
                            String nameUniv = univ.getString("name");

                            String text = o.getString("Description");
                            double rate = o.getDouble("Rate");
                            String date2 = o.getString("Date");
                            String destination = univ.getString("city");

                            ReviewModel r = new ReviewModel(id, student, email, nameUniv, rate, text, destination, date2);

                            review = r;

                            nom.setText(review.getStudent());
                            university.setText(review.getUniversity());
                            avis.setText(review.getReview());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                            String formattedString = review.getDate().format(formatter);
                            date.setText(formattedString);
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
        } catch (
                MqttException e) {
            e.printStackTrace();
        }

    }

    private void acceptReview(ReviewModel reviewModel) {
        String topic = "/android/action/verify";
        try {
            Gson gson = new Gson();
            String json = gson.toJson(reviewModel);
            MqttMessage message = new MqttMessage(json.getBytes());
            client.publish(topic, message);
            sendMajor();
            getReview();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void rejectReview(ReviewModel reviewModel) {
        String topic = "/android/action/reject";
        try {
            Gson gson = new Gson();
            String json = gson.toJson(reviewModel);
            MqttMessage message = new MqttMessage(json.getBytes());
            client.publish(topic, message);
            sendMajor();
            getReview();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
