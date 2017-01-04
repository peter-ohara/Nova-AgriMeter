package com.novaagrisystems.agrimeter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String SENSOR_DATA = "sensorData";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ValueEventListener sensorEventListener;
    private DatabaseReference sensorEventRef;

    @BindView(R.id.location) TextView location;

    @BindView(R.id.lastUpdateTime) TextView lastUpdateTime;
    @BindView(R.id.currentHumidityValue) TextView currentHumidityValue;

    @BindView(R.id.humidityCurrentDate) TextView humidityCurrentDate;
    @BindView(R.id.currentTemperatureValue) TextView currentTemperatureValue;

    @BindView(R.id.temperatureCurrentDate) TextView temperatureCurrentDate;
    @BindView(R.id.currentMoistureValue) TextView currentMoistureValue;

    @BindView(R.id.moistureCurrentDate) TextView moistureCurrentDate;
    @BindView(R.id.currentLightValue) TextView currentLightValue;
    @BindView(R.id.lightCurrentDate) TextView lightCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        database.setPersistenceEnabled(true);
        sensorEventRef = database.getReference(SENSOR_DATA);

        // Keep the last 144 items synced for the detail views
        sensorEventRef.limitToLast(144).keepSynced(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        location.setText(R.string.farm_name);

        createSensorEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addSensorEventListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeSensorEventListener();
    }



    private void createSensorEventListener() {
        sensorEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sensorEventSnapshot : dataSnapshot.getChildren()) {
                    SensorEvent sensorEvent = sensorEventSnapshot.getValue(SensorEvent.class);
                    Log.d(TAG, "Fetched latest sensorEvent:" + sensorEvent);

                    lastUpdateTime.setText(Helpers.getTimeRelativeToNow(MainActivity.this, sensorEvent.timestamp));

                    currentHumidityValue.setText(sensorEvent.humidity.intValue() + getString(R.string.humidity_units));
                    humidityCurrentDate.setText(Helpers.getHumiditySummary(MainActivity.this, sensorEvent.humidity));


                    currentTemperatureValue.setText(sensorEvent.temperature.intValue() + getString(R.string.temperature_units));
                    temperatureCurrentDate.setText(Helpers.getTemperatureSummary(MainActivity.this, sensorEvent.temperature));

                    currentMoistureValue.setText(sensorEvent.soilMoisture.intValue() + getString(R.string.moisture_units));
                    moistureCurrentDate.setText(Helpers.getMoistureSummary(MainActivity.this, sensorEvent.soilMoisture));

                    currentLightValue.setText(sensorEvent.light.intValue() + getString(R.string.light_units));
                    lightCurrentDate.setText(Helpers.getLightSummary(MainActivity.this, sensorEvent.light));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void addSensorEventListener() {
        Log.d(TAG, "Adding sensorEventListener...");
        sensorEventRef.limitToLast(1).addValueEventListener(sensorEventListener);
    }

    private void removeSensorEventListener() {
        if (sensorEventListener != null) {
            sensorEventRef.removeEventListener(sensorEventListener);
        }
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

}
