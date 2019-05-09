package com.example.sops.data.web.models;

import com.google.gson.annotations.SerializedName;

public class WatchedProductStatusWeb
{
    @SerializedName("isWatched")
    private boolean isWatched;

    public boolean isWatched()
    {
        return isWatched;
    }

    public void setWatched(boolean watched)
    {
        isWatched = watched;
    }
}
