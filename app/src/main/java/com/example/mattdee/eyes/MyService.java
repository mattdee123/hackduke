package com.example.mattdee.eyes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpService;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class MyService extends HttpService implements SensorEventListener {

    float[] grav = new float[3];
    float[] mag = new float[3];
    ImageView imageView;
    Activity activity;

    public MyService(HttpProcessor proc, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory,
                     ImageView imageView, Activity activity) {
        super(proc, connStrategy, responseFactory);
        this.imageView = imageView;
        this.activity = activity;
    }

    @Override
    protected void doService(final HttpRequest request, HttpResponse response, HttpContext context)
            throws HttpException, IOException {
        super.doService(request, response, context);

        Log.d("TAG", "IN REQUEST, " + request.getClass());
        Log.d("TAG", "request line=" + request.getRequestLine());
        if (request.getRequestLine().getMethod().equals("GET")) {

            float raw = getR()[0];
            int res = (int)(raw /Math.PI * 180);
            if (res < 0) res += 360;
            response.setStatusCode(200);
            response.setEntity(new StringEntity(res + "\n"));

        } else if (request.getRequestLine().getMethod().equals("POST")) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    EntityUtils.
//                    Bitmap bmp = BitmapFactory.decodeStream(request.)
//                    imageView.setImageBitmap(bmp);
                }
            });
        }

        response.setStatusCode(404);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mag = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            grav = event.values;
        }
        Log.i("SENSOR", (int)(getR()[0] /Math.PI * 180) + "");
    }

    private float[] getR() {
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, grav, mag);
        float[] vals = new float[3];
        SensorManager.getOrientation(R, vals);
        return vals;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("TAG", "accuracy changed; ignoring");
    }

}
