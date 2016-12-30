package com.novaagrisystems.agrimeter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.humidityImageView) ImageView humidityImageView;
    @BindView(R.id.temperatureImageView) ImageView temperatureImageView;
    @BindView(R.id.moistureImageView) ImageView moistureImageView;
    @BindView(R.id.lightImageView) ImageView lightImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @OnClick(R.id.humidityCard)
    public void openhumidityCard() {
        Toast.makeText(this, "Opening the humidityCard", Toast.LENGTH_SHORT).show();
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

}
