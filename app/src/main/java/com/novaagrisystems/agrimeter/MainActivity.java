package com.novaagrisystems.agrimeter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @BindView(R.id.humidityImageView) ImageView humidityImageView;
    @BindView(R.id.currentHumidityValue) TextView currentHumidityValue;
    @BindView(R.id.humidityCurrentDate) TextView humidityCurrentDate;
    @BindView(R.id.humidityCurrentTime) TextView humidityCurrentTime;
    private DatabaseReference humiditySensorRef;
    private ChildEventListener humidityEventListener;


    @BindView(R.id.temperatureImageView) ImageView temperatureImageView;
    @BindView(R.id.currentTemperatureValue) TextView currentTemperatureValue;
    @BindView(R.id.temperatureCurrentDate) TextView temperatureCurrentDate;
    @BindView(R.id.temperatureCurrentTime) TextView temperatureCurrentTime;
    private DatabaseReference temperatureSensorRef;
    private ChildEventListener temperatureEventListener;

    @BindView(R.id.moistureImageView) ImageView moistureImageView;
    @BindView(R.id.currentMoistureValue) TextView currentMoistureValue;
    @BindView(R.id.moistureCurrentDate) TextView moistureCurrentDate;
    @BindView(R.id.moistureCurrentTime) TextView moistureCurrentTime;
    private DatabaseReference moistureSensorRef;
    private ChildEventListener moistureEventListener;

    @BindView(R.id.lightImageView) ImageView lightImageView;
    @BindView(R.id.currentLightValue) TextView currentLightValue;
    @BindView(R.id.lightCurrentDate) TextView lightCurrentDate;
    @BindView(R.id.lightCurrentTime) TextView lightCurrentTime;
    private DatabaseReference lightSensorRef;
    private ChildEventListener lightEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        database.setPersistenceEnabled(true);

        humiditySensorRef =  database.getReference("sensorData").child("humidity");
        temperatureSensorRef =  database.getReference("sensorData").child("temperature");
        moistureSensorRef =  database.getReference("sensorData").child("soilMoisture");
        lightSensorRef =  database.getReference("sensorData").child("light");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRoundColoredIcons();

        setHumiditySensorListener();
        setTemperatureSensorListener();
        setMoistureSensorListener();
        setLightSensorListener();
    }

    private void setRoundColoredIcons() {
        TextDrawable humidityDrawable = TextDrawable.builder()
                .buildRound("H", Color.RED);
        humidityImageView.setImageDrawable(humidityDrawable);

        TextDrawable temperatureDrawable = TextDrawable.builder()
                .buildRound("T", Color.BLUE);
        temperatureImageView.setImageDrawable(temperatureDrawable);

        TextDrawable moistureDrawable = TextDrawable.builder()
                .buildRound("M", Color.GREEN);
        moistureImageView.setImageDrawable(moistureDrawable);

        TextDrawable lightDrawable = TextDrawable.builder()
                .buildRound("L", Color.MAGENTA);
        lightImageView.setImageDrawable(lightDrawable);
    }


    private void setHumiditySensorListener() {
        humidityEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorEvent sensorEvent = dataSnapshot.getValue(SensorEvent.class);
                Log.d(TAG, "onChildAdded:" + sensorEvent.datetime + " : " + sensorEvent.value);

                currentHumidityValue.setText(sensorEvent.value.intValue() + "%");
                humidityCurrentDate.setText(HumidityDetails.getDate(sensorEvent.datetime));
                humidityCurrentTime.setText(HumidityDetails.getTime(sensorEvent.datetime));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        humiditySensorRef.addChildEventListener(humidityEventListener);
    }

    private void setTemperatureSensorListener() {
        temperatureEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorEvent sensorEvent = dataSnapshot.getValue(SensorEvent.class);
                Log.d(TAG, "onChildAdded:" + sensorEvent.datetime + " : " + sensorEvent.value);

                currentTemperatureValue.setText(sensorEvent.value.intValue() + "%");
                temperatureCurrentDate.setText(TemperatureDetails.getDate(sensorEvent.datetime));
                temperatureCurrentTime.setText(TemperatureDetails.getTime(sensorEvent.datetime));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        temperatureSensorRef.addChildEventListener(temperatureEventListener);
    }

    private void setMoistureSensorListener() {
        moistureEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorEvent sensorEvent = dataSnapshot.getValue(SensorEvent.class);
                Log.d(TAG, "onChildAdded:" + sensorEvent.datetime + " : " + sensorEvent.value);

                currentMoistureValue.setText(sensorEvent.value.intValue() + "%");
                moistureCurrentDate.setText(MoistureDetails.getDate(sensorEvent.datetime));
                moistureCurrentTime.setText(MoistureDetails.getTime(sensorEvent.datetime));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        moistureSensorRef.addChildEventListener(moistureEventListener);
    }

    private void setLightSensorListener() {
        lightEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorEvent sensorEvent = dataSnapshot.getValue(SensorEvent.class);
                Log.d(TAG, "onChildAdded:" + sensorEvent.datetime + " : " + sensorEvent.value);

                currentLightValue.setText(sensorEvent.value.intValue() + "%");
                lightCurrentDate.setText(LightDetails.getDate(sensorEvent.datetime));
                lightCurrentTime.setText(LightDetails.getTime(sensorEvent.datetime));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        lightSensorRef.addChildEventListener(lightEventListener);
    }



    @OnClick(R.id.humidityCard)
    public void openhumidityCard() {
        Intent intent = new Intent(MainActivity.this, HumidityDetails.class);
        startActivity(intent);
    }

    @OnClick(R.id.temperatureCard)
    public void openTemperatureCard() {
        Intent intent = new Intent(MainActivity.this, TemperatureDetails.class);
        startActivity(intent);
    }

    @OnClick(R.id.moistureCard)
    public void openMoistureCard() {
        Intent intent = new Intent(MainActivity.this, MoistureDetails.class);
        startActivity(intent);
    }

    @OnClick(R.id.lightCard)
    public void openLightCard() {
        Intent intent = new Intent(MainActivity.this, LightDetails.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (humidityEventListener != null) {
            humiditySensorRef.removeEventListener(humidityEventListener);
        }
        if (temperatureEventListener != null) {
            temperatureSensorRef.removeEventListener(temperatureEventListener);
        }
        if (moistureEventListener != null) {
            moistureSensorRef.removeEventListener(moistureEventListener);
        }
        if (lightEventListener != null) {
            lightSensorRef.removeEventListener(lightEventListener);
        }
    }
}
