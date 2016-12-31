package com.novaagrisystems.agrimeter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HumidityDetails extends AppCompatActivity {

    public static final String TAG = HumidityDetails.class.getSimpleName();

    @BindView(R.id.currentDate) TextView currentDate;
    @BindView(R.id.currentTime) TextView currentTime;
    @BindView(R.id.currentValue) TextView currentValue;
    @BindView(R.id.sensorGraph) GraphView sensorGraph;

    @BindView(R.id.summary) TextView summary;
    @BindView(R.id.valueDescription) TextView valueDescription;

    @BindView(R.id.humidityMeterLabel) TextView humidityMeterLabel;
    @BindView(R.id.humidityMeter) ProgressBar humidityMeter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference humiditySensorRef = database.getReference("sensorData").child("humidity");

    private LineGraphSeries<DataPoint> series;
    private List<SensorEvent> sensorEvents;
    private long minX;
    private long maxX;
    private ChildEventListener sensorEventListener;
    private SensorEvent previousSensorEvent = new SensorEvent(0L, 0F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_details);
        ButterKnife.bind(this);

        series = new LineGraphSeries<>(new DataPoint[] { new DataPoint(0, 0) });
        sensorGraph.addSeries(series);

        getAxisBoundaries();
    }

    private void getAxisBoundaries() {
        humiditySensorRef.limitToFirst(100)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "getAxisBoundaries: returned");

                if (dataSnapshot.getValue() == null) {
                    Log.d(TAG, "getAxisBoundaries: returned null");
                    return;
                }

                sensorEvents = new ArrayList<>();
                for (DataSnapshot sensorEventSnapshot : dataSnapshot.getChildren()) {
                    SensorEvent sensorEvent = sensorEventSnapshot.getValue(SensorEvent.class);
                    sensorEvents.add(sensorEvent);
                }

                Log.d(TAG, "getAxisBoundaries: " + sensorEvents.size());


                minX = sensorEvents.get(0).datetime * 1000L;
                maxX = sensorEvents.get(sensorEvents.size() - 1).datetime * 1000L;

                Log.d(TAG, "getAxisBoundaries: minX: " + minX);
                Log.d(TAG, "getAxisBoundaries: maxX: " + maxX);

                setSeriesProperties();
                setGraphProperties();
                setUpSensorEventListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setSeriesProperties() {
        series.setColor(Color.parseColor("#FFC200"));

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(HumidityDetails.this, dataPoint.getY() + "%",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setGraphProperties() {
        sensorGraph.getViewport().setYAxisBoundsManual(true);
        sensorGraph.getViewport().setMinY(0);
        sensorGraph.getViewport().setMaxY(100);

        sensorGraph.getViewport().setXAxisBoundsManual(true);
//        sensorGraph.getViewport().setMinX(minX);
//        sensorGraph.getViewport().setMaxX(maxX);
        sensorGraph.getGridLabelRenderer().setHumanRounding(false);

        sensorGraph.getViewport().setScalable(true);

        sensorGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        sensorGraph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
    }

    private void setUpSensorEventListener() {
        sensorEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorEvent sensorEvent = dataSnapshot.getValue(SensorEvent.class);
                Log.d(TAG, "onChildAdded:" + sensorEvent.datetime + " : " + sensorEvent.value);

                if (sensorEvent.datetime <= previousSensorEvent.datetime) {
                    return;
                }

                previousSensorEvent = sensorEvent;

                setSummary();
                currentDate.setText(getDate(sensorEvent.datetime));
                currentTime.setText(getTime(sensorEvent.datetime));

                currentValue.setText(sensorEvent.value.toString() + "%");
                setValueDescription();

                series.appendData(
                        new DataPoint(sensorEvent.datetime * 1000L, sensorEvent.value),
                        true,
                        100
                );

                humidityMeterLabel.setText(sensorEvent.value.toString() + "%");
                humidityMeter.setProgress(sensorEvent.value.intValue());
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
        humiditySensorRef.addChildEventListener(sensorEventListener);
    }

    public void setValueDescription() {
        valueDescription.setText("Your plants aren't getting enough light. Please consider a sunnier location or supplement with grow lights.");
    }

    public void setSummary() {
        summary.setText("Medium");
    }

    public static String getDate(long time) {
        //SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d");
        String dateString = formatter.format(new Date(time * 1000L));
        return dateString;
    }

    public static String getTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
        String dateString = formatter.format(new Date(time * 1000L));
        return dateString;
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Remove sensor value event listener
        if (sensorEventListener != null) {
            humiditySensorRef.removeEventListener(sensorEventListener);
        }
    }
}
