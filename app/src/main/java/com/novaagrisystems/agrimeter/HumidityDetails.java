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

public class HumidityDetails extends AppCompatActivity {

    public static final String TAG = HumidityDetails.class.getSimpleName();

    @BindView(R.id.currentValue) TextView currentValue;
    @BindView(R.id.sensorGraph) ValueLineChart sensorGraph;

    @BindView(R.id.summary) TextView summary;
    @BindView(R.id.valueDescription) TextView valueDescription;

    @BindView(R.id.humidityMeterLabel) TextView humidityMeterLabel;
    @BindView(R.id.humidityMeter) ProgressBar humidityMeter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference humiditySensorRef = database.getReference("sensorData").child("humidity");

    private ValueLineSeries series = new ValueLineSeries();
    private ValueEventListener sensorEventListener;
    private SensorEvent previousSensorEvent = new SensorEvent(0L, 0F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_details);
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

                currentValue.setText(previousSensorEvent.value.intValue() + getString(R.string.humidity_units));
                setValueDescription(previousSensorEvent.value);

                humidityMeterLabel.setText(previousSensorEvent.value.intValue() + getString(R.string.humidity_units));
                humidityMeter.setProgress(previousSensorEvent.value.intValue());

                sensorGraph.addSeries(series);
//                sensorGraph.addStandardValue(50);
//                sensorGraph.addStandardValue(80);
                sensorGraph.startAnimation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        humiditySensorRef.limitToFirst(144)
                .addValueEventListener(sensorEventListener);
    }

    public void setValueDescription(Float humidity) {
        if (humidity < 40) {
            summary.setText(getString(R.string.humidity_low_summary));
            valueDescription.setText(getString(R.string.humidity_low_message));
        } else if (humidity > 90) {
            summary.setText(getString(R.string.humidity_high_summary));
            valueDescription.setText(getString(R.string.humidity_high_message));
        } else {
            summary.setText(getString(R.string.humidity_ok_summary));
            valueDescription.setText(getString(R.string.humidity_ok_message));
        }
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
