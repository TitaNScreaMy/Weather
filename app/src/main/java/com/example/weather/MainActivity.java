package com.example.weather;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.PortUnreachableException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Date d = new Date();
    String[] city={"Pune","Mumbai","Delhi","Kolkata","Chennai","Indore","Nashik","Nagpur","Shrinagar"};
    String url="http://api.openweathermap.org/data/2.5/weather?q=Pune&APPID=ea574594b9d36ab688642d5fbeab847e";


    String TAG="MAIN ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Today's Forecast");
        Spinner spin=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,city);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String cityname=city[i];
                Toast.makeText(MainActivity.this, cityname, Toast.LENGTH_SHORT).show();
                url="http://api.openweathermap.org/data/2.5/weather?q="+cityname+"&APPID=ea574594b9d36ab688642d5fbeab847e";
                new YourAsyncTask(MainActivity.this).execute("Felix IT");


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        new YourAsyncTask(MainActivity.this).execute("Felix IT");
    }


    private class YourAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        public YourAsyncTask(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }



        protected String doInBackground(String... args) {


           // String url = "http://api.openweathermap.org/data/2.5/weather?q=Pune&APPID=ea574594b9d36ab688642d5fbeab847e";
            String jsonStr = "";
            try {
                // Making a request to url and getting response
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                jsonStr = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }


            return jsonStr;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                Weather weather = new Weather();

                JSONObject mainObj = new JSONObject(s);
                JSONObject jsonObject1 = mainObj.getJSONObject("coord");

                weather.setLon(jsonObject1.getDouble("lon"));
                weather.setLon(jsonObject1.getDouble("lat"));

                JSONObject jsonObject2= mainObj.getJSONObject("main");

                weather.setTemp(jsonObject2.getDouble("temp"));
                weather.setPressure(jsonObject2.getDouble("pressure"));
                weather.setHumidity(jsonObject2.getDouble("humidity"));
                weather.setTemp_min(jsonObject2.getDouble("temp_min"));
                weather.setTemp_max(jsonObject2.getDouble("temp_max"));

                JSONObject jsonObject3 = mainObj.getJSONObject("wind");

                weather.setSpeed(jsonObject3.getDouble("speed"));
                weather.setDeg(jsonObject3.getDouble("deg"));



                TextView textViewDate=(TextView)findViewById(R.id.date);
                TextView textViewTime=(TextView)findViewById(R.id.time);
                TextView textViewLon = (TextView)findViewById(R.id.textView2);
                TextView textViewLat= (TextView)findViewById(R.id.textView1);
                TextView textViewTemp=(TextView)findViewById(R.id.textView11);
                TextView textViewPressure=(TextView)findViewById(R.id.textView4);
                TextView textViewHumidity=(TextView)findViewById(R.id.textView5);
                TextView textViewTempmin=(TextView)findViewById(R.id.textView6);
                TextView textViewTempmax=(TextView)findViewById(R.id.textView7);
                TextView textViewSpeed=(TextView)findViewById(R.id.textView8);
                TextView textViewDeg=(TextView)findViewById(R.id.textView9);

                String currentDateString = DateFormat.getDateInstance().format(new Date());
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                String currentDateTimeString =sdf.format(d);


                textViewTime.setText(currentDateTimeString);
                textViewDate.setText(currentDateString);

                textViewLon.setText(String.valueOf(jsonObject1.getDouble("lon")));
                textViewLat.setText(String.valueOf(jsonObject1.getDouble("lat")));
                textViewTemp.setText(String.valueOf(jsonObject2.getDouble("temp")-273.15 +"\u2103"));
                textViewPressure.setText(String.valueOf(jsonObject2.getDouble("pressure")));
                textViewHumidity.setText(String.valueOf(jsonObject2.getDouble("humidity")));
                textViewTempmin.setText(String.valueOf(jsonObject2.getDouble("temp_min")-273.15 +"\u2103"));
                textViewTempmax.setText(String.valueOf(jsonObject2.getDouble("temp_max")-273.15+ "\u2103"));
                textViewSpeed.setText(String.valueOf(jsonObject3.getDouble("speed")));
                textViewDeg.setText(String.valueOf(jsonObject3.getDouble("deg")));





            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}



