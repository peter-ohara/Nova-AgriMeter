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

public class TemperatureDetails extends AppCompatActivity {

    public static final String TAG = TemperatureDetails.class.getSimpleName();

    @BindView(R.id.currentValue) TextView currentValue;
    @BindView(R.id.sensorGraph) ValueLineChart sensorGraph;

    @BindView(R.id.summary) TextView summary;
    @BindView(R.id.valueDescription) TextView valueDescription;

    @BindView(R.id.temperatureMeterLabel) TextView temperatureMeterLabel;
    @BindView(R.id.temperatureMeter) ProgressBar temperatureMeter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference sensorEventRef = database.getReference("sensorData");

    private ValueLineSeries series = new ValueLineSeries();
    private ValueEventListener sensorEventListener;
    private SensorEvent previousSensorEvent = new SensorEvent(0L,0D,0L,0L,0L);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_details);
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
                    Log.d(TAG, "onChildAdded:" + sensorEvent.timestamp + " : " + sensorEvent.temperature);

                    if (sensorEvent.timestamp <= previousSensorEvent.timestamp) {
                        continue;
                    }
                    sensorEvents.add(sensorEvent);
                    series.addPoint(new ValueLinePoint(Helpers.getTime(previousSensorEvent.timestamp), sensorEvent.temperature.floatValue()));

                    previousSensorEvent = sensorEvent;
                }

                currentValue.setText(previousSensorEvent.temperature.intValue() + getString(R.string.temperature_units));

                summary.setText(Helpers.getTemperatureSummary(TemperatureDetails.this, previousSensorEvent.temperature));
                valueDescription.setText(Helpers.getTemperatureMessage(TemperatureDetails.this, previousSensorEvent.temperature));

                temperatureMeterLabel.setText(previousSensorEvent.temperature.intValue() + getString(R.string.temperature_units));
                temperatureMeter.setProgress(previousSensorEvent.temperature.intValue());

                sensorGraph.addSeries(series);
                sensorGraph.startAnimation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        sensorEventRef.limitToFirst(144)
                .addValueEventListener(sensorEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Remove sensor value event listener
        if (sensorEventListener != null) {
            sensorEventRef.removeEventListener(sensorEventListener);
        }
    }
}
