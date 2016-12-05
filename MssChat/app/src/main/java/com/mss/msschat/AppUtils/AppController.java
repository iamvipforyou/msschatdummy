package com.mss.msschat.AppUtils;

import android.support.multidex.MultiDexApplication;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;



public class AppController extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();
    }
        private Socket mSocket;
        {
            try {
                mSocket = IO.socket(Constants.SOCKET_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }


    public Socket getSocket() {
        return mSocket;
    }

}
