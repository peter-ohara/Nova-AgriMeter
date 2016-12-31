package com.novaagrisystems.agrimeter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();


    @BindView(R.id.humidityImageView) ImageView humidityImageView;
    @BindView(R.id.humidityValue) TextView humidityValue;

    @BindView(R.id.currentDate1) TextView currentDate;
    @BindView(R.id.currentTime1) TextView currentTime;

    @BindView(R.id.temperatureImageView) ImageView temperatureImageView;
    @BindView(R.id.moistureImageView) ImageView moistureImageView;
    @BindView(R.id.lightImageView) ImageView lightImageView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference humiditySensorRef = database.getReference("sensorData").child("humidity");
    private ChildEventListener humidityEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRoundColoredIcons();

        getHumidity();
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


    @OnClick(R.id.humidityCard)
    public void openhumidityCard() {
        Intent intent = new Intent(MainActivity.this, HumidityDetails.class);
        startActivity(intent);
    }

    @OnClick(R.id.temperatureCard)
    public void openLemperatureCard() {
        Toast.makeText(this, "Opening the temperatureCard", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.moistureCard)
    public void openMoistureCard() {
        Toast.makeText(this, "Opening the moistureCard", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.lightCard)
    public void openLightCard() {
        Toast.makeText(this, "Opening the lightCard", Toast.LENGTH_SHORT).show();
    }

    private void getHumidity() {
        humidityEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorEvent sensorEvent = dataSnapshot.getValue(SensorEvent.class);
                Log.d(TAG, "onChildAdded:" + sensorEvent.datetime + " : " + sensorEvent.value);

                humidityValue.setText(sensorEvent.value.toString() + "%");
                currentDate.setText(HumidityDetails.getDate(sensorEvent.datetime));
                currentTime.setText(HumidityDetails.getTime(sensorEvent.datetime));
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

    @Override
    protected void onStop() {
        super.onStop();

        if (humidityEventListener != null) {
            humiditySensorRef.removeEventListener(humidityEventListener);
        }
    }
}
