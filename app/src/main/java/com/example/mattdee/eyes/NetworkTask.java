package com.example.mattdee.eyes;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.apache.http.HttpException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class NetworkTask implements Runnable {

    private final MyService service;

    public NetworkTask(MyService service) {
        this.service = service;
    }

    @Override
    public void run() {


        int port = 8000;

        ServerSocket serverSocket = null;
        Log.d("TAG", "trying");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                conn.bind(socket, new BasicHttpParams());
                service.handleRequest(conn, new BasicHttpContext(null));
                Log.d("TAG", "done request");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (HttpException e) {
                e.printStackTrace();
            }
        }
    }

}
