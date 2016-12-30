package com.novaagrisystems.agrimeter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    @BindView(R.id.toolbar) Toolbar toolbar;
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
    private double graph2LastXValue = 5d;
    private List<SensorEvent> sensorEvents;
    private long minX;
    private long maxX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        series = new LineGraphSeries<>(new DataPoint[] { new DataPoint(0, 0) });
        sensorGraph.addSeries(series);

        getAxisBoundaries();
    }

    private void getAxisBoundaries() {
        humiditySensorRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.DATE, -7);
        Date lastWeek = calendar.getTime();

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
        humiditySensorRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorEvent sensorEvent = dataSnapshot.getValue(SensorEvent.class);
                Log.d(TAG, "onChildAdded:" + sensorEvent.datetime + " : " + sensorEvent.value);


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
        });
    }

    private void setValueDescription() {
        valueDescription.setText("Your plants aren't getting enough light. Please consider a sunnier location or supplement with grow lights.");
    }

    private void setSummary() {
        summary.setText("Medium");
    }

    @OnClick(R.id.currentValue)
    public void update() {
        Log.d(TAG, "update: ");
        graph2LastXValue += 1d;
        series.appendData(
                new DataPoint(graph2LastXValue, getRandom()),
                true,
                40
        );
    }

    private String getDate(long time) {
        //SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d");
        String dateString = formatter.format(new Date(time * 1000L));
        return dateString;
    }

    private String getTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
        String dateString = formatter.format(new Date(time * 1000L));
        return dateString;
    }


    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double f = mRand.nextDouble()*0.15+0.3;
            double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }

}
