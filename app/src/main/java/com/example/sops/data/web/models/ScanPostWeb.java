package com.example.sops.data.web.models;

import com.google.gson.annotations.SerializedName;

public class ScanPostWeb
{
    @SerializedName("existingProductId")
    private int    existingProductId;
    @SerializedName("existingProductSecret")
    private String existingProductSecret;
    @SerializedName("locationLongitude")
    private double locationLongitude;
    @SerializedName("locationLatitude")
    private double locationLatitude;

    public int getExistingProductId()
    {
        return existingProductId;
    }

    public void setExistingProductId(int existingProductId)
    {
        this.existingProductId = existingProductId;
    }

    public String getExistingProductSecret()
    {
        return existingProductSecret;
    }

    public void setExistingProductSecret(String existingProductSecret)
    {
        this.existingProductSecret = existingProductSecret;
    }

    public double getLocationLongitude()
    {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude)
    {
        this.locationLongitude = locationLongitude;
    }

    public double getLocationLatitude()
    {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude)
    {
        this.locationLatitude = locationLatitude;
    }
}
