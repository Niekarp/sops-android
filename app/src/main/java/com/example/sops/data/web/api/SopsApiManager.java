package com.example.sops.data.web.api;

import android.content.Context;

import com.example.sops.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SopsApiManager
{
    private static String mToken = "null";
    private static SopsApi mInstance = null;

    public static SopsApi getApiInstance()
    {
        if (mInstance == null)
        {
            // TODO: more secure creation
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.callTimeout(30, TimeUnit.SECONDS);
            if (mToken.equals("null") == false)
            {
                httpClient.addInterceptor(new Interceptor()
                {
                    @Override
                    public Response intercept(Chain chain) throws IOException
                    {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Authorization", "Bearer " + mToken)
                                .build();
                        return chain.proceed(request);
                    }
                });
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SopsApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
            mInstance = retrofit.create(SopsApi.class);
        }

        return mInstance;
    }

    public static void setTokenInEveryHeader(String token)
    {
        mToken = token;
        mInstance = null;
        getApiInstance();
    }
}
