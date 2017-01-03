package com.novaagrisystems.agrimeter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

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

    FirebaseDatabase database;

    @BindView(R.id.location) TextView location;
    @BindView(R.id.lastUpdateTime) TextView lastUpdateTime;


    @BindView(R.id.currentHumidityValue) TextView currentHumidityValue;
    @BindView(R.id.humidityCurrentDate) TextView humidityCurrentDate;
    private DatabaseReference humiditySensorRef;
    private ChildEventListener humidityEventListener;


    @BindView(R.id.currentTemperatureValue) TextView currentTemperatureValue;
    @BindView(R.id.temperatureCurrentDate) TextView temperatureCurrentDate;
    private DatabaseReference temperatureSensorRef;
    private ChildEventListener temperatureEventListener;

    @BindView(R.id.currentMoistureValue) TextView currentMoistureValue;
    @BindView(R.id.moistureCurrentDate) TextView moistureCurrentDate;
    private DatabaseReference moistureSensorRef;
    private ChildEventListener moistureEventListener;

    @BindView(R.id.currentLightValue) TextView currentLightValue;
    @BindView(R.id.lightCurrentDate) TextView lightCurrentDate;
    private DatabaseReference lightSensorRef;
    private ChildEventListener lightEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }

        humiditySensorRef =  database.getReference("sensorData").child("humidity");
        temperatureSensorRef =  database.getReference("sensorData").child("temperature");
        moistureSensorRef =  database.getReference("sensorData").child("soilMoisture");
        lightSensorRef =  database.getReference("sensorData").child("light");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        location.setText(R.string.farm_name);

        setHumiditySensorListener();
        setTemperatureSensorListener();
        setMoistureSensorListener();
        setLightSensorListener();
    }


    private void setHumiditySensorListener() {
        humidityEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorEvent sensorEvent = dataSnapshot.getValue(SensorEvent.class);
                Log.d(TAG, "onChildAdded:" + sensorEvent.datetime + " : " + sensorEvent.value);

                currentHumidityValue.setText(sensorEvent.value.intValue() + getString(R.string.humidity_units));
                humidityCurrentDate.setText(Helpers.getHumiditySummary(MainActivity.this, sensorEvent.value));
                lastUpdateTime.setText(Helpers.getTimeRelativeToNow(MainActivity.this, sensorEvent.datetime));
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

                currentTemperatureValue.setText(sensorEvent.value.intValue() + getString(R.string.temperature_units));
                temperatureCurrentDate.setText(Helpers.getTemperatureSummary(MainActivity.this, sensorEvent.value));
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

                currentMoistureValue.setText(sensorEvent.value.intValue() + getString(R.string.moisture_units));
                moistureCurrentDate.setText(Helpers.getMoistureSummary(MainActivity.this, sensorEvent.value));
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

                currentLightValue.setText(sensorEvent.value.intValue() + getString(R.string.light_units));
                lightCurrentDate.setText(Helpers.getLightSummary(MainActivity.this, sensorEvent.value));
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



    @OnClick(R.id.overviewCard)
    public void openOverviewCard() {
        Uri gmmIntentUri = Uri.parse(getString(R.string.farm_location));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @OnClick(R.id.humidityCard)
    public void openHumidityCard() {
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
