package com.example.sops.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.example.sops.R;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;

public class AppUtils
{
    public static boolean isInternetConnection (Context appContext)
    {
        ConnectivityManager connectivityManager;
        connectivityManager = appContext.getSystemService(ConnectivityManager.class);

        boolean isInternetConnected = connectivityManager.getActiveNetworkInfo() != null;
        return isInternetConnected;
    }

    public static <T> T translateObject (Object source, Class<T> destinationClass)
    {
        Gson gson = new Gson();

        String jsonRepresentation = gson.toJson(source);

        T destinationObject = gson.fromJson(jsonRepresentation, destinationClass);
        return destinationObject;
    }

    public static <T> void logDetails (Call<T> call, Throwable throwable)
    {
        Log.d("my", "Call enqueue onFailed:");
        Log.d("my", "failed request: " + call.request().toString());
        Log.d("my", "failed request body: " + call.request().body());
        Log.d("my", "throwable message: " + throwable.getMessage());
        Log.d("my", "stack trace:");
        throwable.printStackTrace();
    }

    public static <T> void logDetails (Call<T> call, Response<T> response)
    {
        Log.d("my", "Call enqueue onResponse not successful:");
        Log.d("my", "request: " + call.request().toString());
        if (call.request().headers() != null)
        {
            Log.d("my", "request headers: " + call.request().headers().toString());
        }
        if (call.request().body() != null)
        {
            Log.d("my", "request body: " + call.request().body().toString());
        }
        Log.d("my", "response code: " + response.code());
        if (response.body() != null)
        {
            Log.d("my", "response body: " + response.body().toString());
        }
        Log.d("my", "response: " + response.toString());
    }

    public static void showSnackbar (Activity context, int viewResId, int strResId)
    {
        Snackbar.make(
                context.findViewById(viewResId),
                context.getString(strResId),
                Snackbar.LENGTH_SHORT)
                .show();
    }

    public static String getCurrentUserId(Activity activity)
    {
        SharedPreferences sharedPreferences;
        sharedPreferences = activity.getSharedPreferences(
                activity.getString(R.string.shared_prefs_file_user),
                Context.MODE_PRIVATE);

        return sharedPreferences.getString
                (
                        activity.getString(R.string.shared_prefs_user_id_key),
                        activity.getString(R.string.shared_prefs_user_id_default)
                );
    }

    public static String getCurrentUserId(Application activity)
    {
        SharedPreferences sharedPreferences;
        sharedPreferences = activity.getSharedPreferences(
                activity.getString(R.string.shared_prefs_file_user),
                Context.MODE_PRIVATE);

        return sharedPreferences.getString
                (
                        activity.getString(R.string.shared_prefs_user_id_key),
                        activity.getString(R.string.shared_prefs_user_id_default)
                );
    }

    public static int getCurrentUserCompanyId(Activity activity)
    {
        SharedPreferences sharedPreferences;
        sharedPreferences = activity.getSharedPreferences(
                activity.getString(R.string.shared_prefs_file_user),
                Context.MODE_PRIVATE);

        String companyIdStr = sharedPreferences.getString
                (
                        activity.getString(R.string.shared_prefs_company_id_key),
                        activity.getString(R.string.shared_prefs_company_id_default)
                );

        if (companyIdStr.equals(activity.getString(R.string.shared_prefs_company_id_default)))
        {
            companyIdStr = "-1";
        }

        return Integer.parseInt(companyIdStr);
    }

    public static int getCurrentUserCompanyId(Application activity)
    {
        SharedPreferences sharedPreferences;
        sharedPreferences = activity.getSharedPreferences(
                activity.getString(R.string.shared_prefs_file_user),
                Context.MODE_PRIVATE);

        String companyIdStr = sharedPreferences.getString
                (
                        activity.getString(R.string.shared_prefs_company_id_key),
                        activity.getString(R.string.shared_prefs_company_id_default)
                );

        if (companyIdStr.equals(activity.getString(R.string.shared_prefs_company_id_default)))
        {
            companyIdStr = "-1";
        }

        return Integer.parseInt(companyIdStr);
    }
}
