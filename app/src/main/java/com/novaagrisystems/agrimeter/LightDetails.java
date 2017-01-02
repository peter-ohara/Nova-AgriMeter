package com.novaagrisystems.agrimeter;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LightDetails extends AppCompatActivity {

    public static final String TAG = LightDetails.class.getSimpleName();

    @BindView(R.id.currentValue) TextView currentValue;
    @BindView(R.id.sensorGraph) ValueLineChart sensorGraph;

    @BindView(R.id.summary) TextView summary;
    @BindView(R.id.valueDescription) TextView valueDescription;

    @BindView(R.id.lightMeterLabel) TextView lightMeterLabel;
    @BindView(R.id.lightMeter) ProgressBar lightMeter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference lightSensorRef = database.getReference("sensorData").child("light");

    private ValueLineSeries series = new ValueLineSeries();
    private ValueEventListener sensorEventListener;
    private SensorEvent previousSensorEvent = new SensorEvent(0L, 0F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_details);
        ButterKnife.bind(this);

        series.setColor(ResourcesCompat.getColor(getResources(), R.color.graphLineColor, null));


        getAxisBoundaries();
    }

    private void getAxisBoundaries() {
        sensorEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "getAxisBoundaries: " + dataSnapshot.getKey());

                if (dataSnapshot.getValue() == null) {
                    Log.d(TAG, "getAxisBoundaries: value is null");
                    return;
                }

                List<SensorEvent> sensorEvents = new ArrayList<>();
                for (DataSnapshot sensorEventSnapshot : dataSnapshot.getChildren()) {
                    SensorEvent sensorEvent = sensorEventSnapshot.getValue(SensorEvent.class);
                    Log.d(TAG, "onChildAdded:" + sensorEvent.datetime + " : " + sensorEvent.value);

                    if (sensorEvent.datetime <= previousSensorEvent.datetime) {
                        continue;
                    }
                    sensorEvents.add(sensorEvent);
                    series.addPoint(new ValueLinePoint(Helpers.getTime(previousSensorEvent.datetime), sensorEvent.value));

                    previousSensorEvent = sensorEvent;
                }

                currentValue.setText(previousSensorEvent.value.intValue() + getString(R.string.light_units));

                summary.setText(Helpers.getLightSummary(LightDetails.this, previousSensorEvent.value));
                valueDescription.setText(Helpers.getLightMessage(LightDetails.this, previousSensorEvent.value));

                lightMeterLabel.setText(previousSensorEvent.value.intValue() + getString(R.string.light_units));
                lightMeter.setProgress(previousSensorEvent.value.intValue());

                sensorGraph.addSeries(series);
                sensorGraph.startAnimation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        lightSensorRef.limitToFirst(144)
                .addValueEventListener(sensorEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Remove sensor value event listener
        if (sensorEventListener != null) {
            lightSensorRef.removeEventListener(sensorEventListener);
        }
    }
}
