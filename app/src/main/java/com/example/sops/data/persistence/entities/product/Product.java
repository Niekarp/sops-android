package com.example.sops.data.persistence.entities.product;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "Products")
public class Product
{
    @PrimaryKey
    private int id;
    private String name;
    private String barcode;
    private String description;
    private int companyId;
    private String countryOfOrigin;
    private Date creationDate;
    private int defaultExpirationDateInMonths;
    private float suggestedPrice;

    public Product(int id, String name, String barcode, String description, int companyId, String countryOfOrigin, Date creationDate, int defaultExpirationDateInMonths, float suggestedPrice)
    {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.description = description;
        this.companyId = companyId;
        this.countryOfOrigin = countryOfOrigin;
        this.creationDate = creationDate;
        this.defaultExpirationDateInMonths = defaultExpirationDateInMonths;
        this.suggestedPrice = suggestedPrice;
    }

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

    public float getSuggestedPrice()
    {
        return suggestedPrice;
    }

    public void setSuggestedPrice(float suggestedPrice)
    {
        this.suggestedPrice = suggestedPrice;
    }
}
