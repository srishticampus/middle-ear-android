package com.project.adersh.middleearpathology;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //private static final String BASE_URL = "http://10.0.2.2:8000/api/";
    private static final String BASE_URL = "https://python.sicsglobal.com/ear_pathology/api/";


    // private static final String BASE_URL_VIDEO = "http://10.0.2.2:8000/v2m/";
    private static final String BASE_URL_VIDEO = "https://python.sicsglobal.com/ear_pathology/v2m/";

    private static Retrofit retrofit = null;
    private static Retrofit retrofitForVideo = null;

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor) // same for .addInterceptor(...)
            .connectTimeout(30, TimeUnit.SECONDS) //Backend is really slow
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientForVideo() {
        if (retrofitForVideo == null) {
            retrofitForVideo = new Retrofit.Builder()
                    .baseUrl(BASE_URL_VIDEO)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofitForVideo;
    }
}
