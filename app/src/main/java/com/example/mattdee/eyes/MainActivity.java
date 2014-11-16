package com.example.mattdee.eyes;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpProcessor;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wifi.getConnectionInfo().getIpAddress());
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(ip);
        Log.d("TAG", "doing it");

        HttpProcessor httpproc = new BasicHttpProcessor();
        MyService service = new MyService(httpproc,new DefaultConnectionReuseStrategy(),
                new DefaultHttpResponseFactory(), null, null); //TODO fix
        NetworkTask networkTask = new NetworkTask(service);


        SensorManager sensors = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor accel = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor mag = sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensors.registerListener(service, mag, SensorManager.SENSOR_DELAY_GAME);
        sensors.registerListener(service, accel, SensorManager.SENSOR_DELAY_GAME);
        new Thread(networkTask).start();
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
}
