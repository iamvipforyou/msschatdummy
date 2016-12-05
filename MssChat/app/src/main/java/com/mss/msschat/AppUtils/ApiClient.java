package com.mss.msschat.AppUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mss on 25/11/16.
 */

public class ApiClient {


 //  public static final String BASE_URL = "http://mastersoftwaretechnologies.com:9007/";
    public static final String BASE_URL = "http://192.168.0.53:9007/";

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
