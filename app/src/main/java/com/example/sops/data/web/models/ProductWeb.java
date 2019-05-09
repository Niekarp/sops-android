package com.example.sops.data.web.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ProductWeb
{
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("description")
    private String description;

    @SerializedName("companyId")
    private int companyId;

    @SerializedName("countryOfOrigin")
    private String countryOfOrigin;

    @SerializedName("creationDate")
    private Date creationDate;

    @SerializedName("defaultExpirationDateInMonths")
    private int defaultExpirationDateInMonths;

    @SerializedName("suggestedPrice")
    private int suggestedPrice;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }

    public String getCountryOfOrigin()
    {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin)
    {
        this.countryOfOrigin = countryOfOrigin;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public int getDefaultExpirationDateInMonths()
    {
        return defaultExpirationDateInMonths;
    }

    public void setDefaultExpirationDateInMonths(int defaultExpirationDateInMonths)
    {
        this.defaultExpirationDateInMonths = defaultExpirationDateInMonths;
    }

    public int getSuggestedPrice()
    {
        return suggestedPrice;
    }

    public void setSuggestedPrice(int suggestedPrice)
    {
        this.suggestedPrice = suggestedPrice;
    }
}

