package com.example.android.lobhesh;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {
    private Button bt;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button) findViewById(R.id.btn);
        input = (EditText) findViewById(R.id.input_location);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String location = input.getText().toString();
                new JsonTask().execute("http://api.openweathermap.org/data/2.5/weather?APPID=924811d4d4cb3f3219d7a95c2f0fc61a&q=" + location + "&mode=json");

            }
        });
    }


    public void displayError(WeatherResult result) {
        if (result != null) {
            Toast.makeText(this, result.getError(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "An unknown Error occured", Toast.LENGTH_SHORT).show();
        }
    }

    public class JsonTask extends AsyncTask<String, String, WeatherResult> {

        @Override
        protected WeatherResult doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            WeatherResult result = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                result = new WeatherResult();

                result.setHttpResponseCode(connection.getResponseCode());

                if (connection.getResponseCode() == 200) { //See HTTP Status Code 200

                    String finalJson = readFromStream(connection.getInputStream());
                    //Log the result to the standart out this way you know what your are getting as response from the server
                    Log.d(getClass().getCanonicalName(), finalJson);


                    ///// extraction data from JSON Format Text /////


                    JSONObject parent = new JSONObject(finalJson);
                    JSONArray array = parent.getJSONArray("weather");
                    JSONObject description = array.getJSONObject(0);

                    JSONObject system = parent.getJSONObject("sys");
                    JSONObject tempdata = parent.getJSONObject("main");
                    JSONObject cod = parent.getJSONObject("coord");

                    JSONObject wind = parent.getJSONObject("wind");

                    result.setName(parent.getString("name"));
                    result.setCountry(system.getString("country"));
                    result.setType(system.getInt("type"));
                    result.setTemp(tempdata.getDouble("temp") - 273);
                    result.setPressure(tempdata.getDouble("pressure"));
                    result.setHumidity(tempdata.getDouble("humidity"));
                    result.setTemp_min(tempdata.getDouble("temp_min") - 275);
                    result.setTemp_max(tempdata.getDouble("temp_max") - 268);
                    result.setSpeed(wind.getInt("speed"));
                    result.setDescription(description.getString("description"));
                    result.setLat(cod.getDouble("lon"));
                    result.setLat(cod.getDouble("lat"));
                } else {
                    String error = readFromStream(connection.getErrorStream());
                    Log.d(getClass().getCanonicalName(), error);

                    JSONObject jsonError = new JSONObject(error);
                    result.setResponseCode(jsonError.getInt("cod"));
                    result.setError(jsonError.getString("message"));

                }
                //connection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {


                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Look up "code smells"
            //a method should only have one return value
            return result;
        }

        @Override
        protected void onPostExecute(final WeatherResult result) {
            super.onPostExecute(result);
            if (result == null || result.getHttpResponseCode() != 200) {
                displayError(result);
            } else {
                WeatherResultActivity.startActivity(MainActivity.this, result);
            }
        }
    }

    public String readFromStream(InputStream stream) throws IOException {
        ;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuffer buffer = new StringBuffer();
        String line;

        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        return buffer.toString();
    }


}