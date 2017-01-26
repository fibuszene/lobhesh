package com.example.android.lobhesh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherResultActivity extends AppCompatActivity {
    private WeatherResult result;
    private static final String RESULT_EXTRA = "com.example.android.lobhesh.WEATHERRESULT_EXTRA";
    private TextView temp, lo, ln;
    private TextView city;
    private TextView pressure;
    private TextView humidity;
    private TextView windspeed;
    private TextView min_temp;
    private TextView max_temp;
    private TextView weathertype;
    private ImageView image;


    public static void startActivity(Context context, WeatherResult result) {
        Intent intent = new Intent(context, WeatherResultActivity.class);
        intent.putExtra(RESULT_EXTRA, result); //this is qhy result needs to be serializable
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        temp = (TextView) findViewById(R.id.temp);
        city = (TextView) findViewById(R.id.location);
        pressure = (TextView) findViewById(R.id.pressure);
        humidity = (TextView) findViewById(R.id.humidity);
        min_temp = (TextView) findViewById(R.id.min_temp);
        max_temp = (TextView) findViewById(R.id.max_temp);
        windspeed = (TextView) findViewById(R.id.windspeed);
        lo = (TextView) findViewById(R.id.lon);
        ln = (TextView) findViewById(R.id.lan);
        weathertype = (TextView) findViewById(R.id.weatherType);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RESULT_EXTRA)) {
            result = (WeatherResult) intent.getSerializableExtra(RESULT_EXTRA);
        } else {
            finish(); //end the indent because there is no data to display
        }

        city.setText(result.getName());
        temp.setText(result.getTemp() + "");
        pressure.setText("P " + result.getPressure() + " hPa");
        humidity.setText("" + result.getHumidity() + "%");
        min_temp.setText("MIN  " + result.getTemp_min() + "°C");
        max_temp.setText("MAX " + result.getTemp_max() + "°C");
        windspeed.setText("" + result.getSpeed() + " mph");
        weathertype.setText(result.getType() + "");
        lo.setText(result.getLon() + "");
        ln.setText(result.getLat() + "");
    }

}
