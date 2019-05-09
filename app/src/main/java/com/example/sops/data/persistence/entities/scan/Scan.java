package com.example.sops.data.persistence.entities.scan;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "Scans")
public class Scan
{
    @PrimaryKey
    @NonNull
    private int existingProductId;
    private String productName;
    private String productBarcode;
    private String productDescription;
    private String productCountryOfOrigin;
    private String companyName;
    private String companyAddressStreet;
    private String companyAddressZipCode;
    private String companyAddressCity;
    private String companyEmail;
    private String companyNIP;
    private String companyREGON;
    private Date companyJoinDate;
    private Date scanDate;
    private Date existingProductExpirationDate;
    private Date existingProductCreationDate;
    private Date productCreationDate;
    private int productId;
    private int productDefaultExpirationDateInMonths;
    private float productSuggestedPrice;
    private double scanLocationLongitude;
    private double scanLocationLatitude;
    private boolean isWatched;

    public Scan(int existingProductId, String productName, String productBarcode, String productDescription, String productCountryOfOrigin, String companyName, String companyAddressStreet, String companyAddressZipCode, String companyAddressCity, String companyEmail, String companyNIP, String companyREGON, Date companyJoinDate, Date scanDate, Date existingProductExpirationDate, Date existingProductCreationDate, Date productCreationDate, int productId, int productDefaultExpirationDateInMonths, float productSuggestedPrice, double scanLocationLongitude, double scanLocationLatitude, boolean isWatched)
    {
        this.existingProductId = existingProductId;
        this.productName = productName;
        this.productBarcode = productBarcode;
        this.productDescription = productDescription;
        this.productCountryOfOrigin = productCountryOfOrigin;
        this.companyName = companyName;
        this.companyAddressStreet = companyAddressStreet;
        this.companyAddressZipCode = companyAddressZipCode;
        this.companyAddressCity = companyAddressCity;
        this.companyEmail = companyEmail;
        this.companyNIP = companyNIP;
        this.companyREGON = companyREGON;
        this.companyJoinDate = companyJoinDate;
        this.scanDate = scanDate;
        this.existingProductExpirationDate = existingProductExpirationDate;
        this.existingProductCreationDate = existingProductCreationDate;
        this.productCreationDate = productCreationDate;
        this.productId = productId;
        this.productDefaultExpirationDateInMonths = productDefaultExpirationDateInMonths;
        this.productSuggestedPrice = productSuggestedPrice;
        this.scanLocationLongitude = scanLocationLongitude;
        this.scanLocationLatitude = scanLocationLatitude;
        this.isWatched = isWatched;
    }

    public int getExistingProductId()
    {
        return existingProductId;
    }

    public void setExistingProductId(int existingProductId)
    {
        this.existingProductId = existingProductId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductBarcode()
    {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode)
    {
        this.productBarcode = productBarcode;
    }

    public String getProductDescription()
    {
        return productDescription;
    }

    public void setProductDescription(String productDescription)
    {
        this.productDescription = productDescription;
    }

    public String getProductCountryOfOrigin()
    {
        return productCountryOfOrigin;
    }

    public void setProductCountryOfOrigin(String productCountryOfOrigin)
    {
        this.productCountryOfOrigin = productCountryOfOrigin;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getCompanyAddressStreet()
    {
        return companyAddressStreet;
    }

    public void setCompanyAddressStreet(String companyAddressStreet)
    {
        this.companyAddressStreet = companyAddressStreet;
    }

    public String getCompanyAddressZipCode()
    {
        return companyAddressZipCode;
    }

    public void setCompanyAddressZipCode(String companyAddressZipCode)
    {
        this.companyAddressZipCode = companyAddressZipCode;
    }

    public String getCompanyAddressCity()
    {
        return companyAddressCity;
    }

    public void setCompanyAddressCity(String companyAddressCity)
    {
        this.companyAddressCity = companyAddressCity;
    }

    public String getCompanyEmail()
    {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail)
    {
        this.companyEmail = companyEmail;
    }

    public String getCompanyNIP()
    {
        return companyNIP;
    }

    public void setCompanyNIP(String companyNIP)
    {
        this.companyNIP = companyNIP;
    }

    public String getCompanyREGON()
    {
        return companyREGON;
    }

    public void setCompanyREGON(String companyREGON)
    {
        this.companyREGON = companyREGON;
    }

    public Date getCompanyJoinDate()
    {
        return companyJoinDate;
    }

    public void setCompanyJoinDate(Date companyJoinDate)
    {
        this.companyJoinDate = companyJoinDate;
    }

    public Date getScanDate()
    {
        return scanDate;
    }

    public void setScanDate(Date scanDate)
    {
        this.scanDate = scanDate;
    }

    public Date getExistingProductExpirationDate()
    {
        return existingProductExpirationDate;
    }

    public void setExistingProductExpirationDate(Date existingProductExpirationDate)
    {
        this.existingProductExpirationDate = existingProductExpirationDate;
    }

    public Date getExistingProductCreationDate()
    {
        return existingProductCreationDate;
    }

    public void setExistingProductCreationDate(Date existingProductCreationDate)
    {
        this.existingProductCreationDate = existingProductCreationDate;
    }

    public Date getProductCreationDate()
    {
        return productCreationDate;
    }

    public void setProductCreationDate(Date productCreationDate)
    {
        this.productCreationDate = productCreationDate;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public int getProductDefaultExpirationDateInMonths()
    {
        return productDefaultExpirationDateInMonths;
    }

    public void setProductDefaultExpirationDateInMonths(int productDefaultExpirationDateInMonths)
    {
        this.productDefaultExpirationDateInMonths = productDefaultExpirationDateInMonths;
    }

    public float getProductSuggestedPrice()
    {
        return productSuggestedPrice;
    }

    public void setProductSuggestedPrice(float productSuggestedPrice)
    {
        this.productSuggestedPrice = productSuggestedPrice;
    }

    public boolean isWatched()
    {
        return isWatched;
    }

    public void setWatched(boolean watched)
    {
        isWatched = watched;
    }

    public double getScanLocationLongitude()
    {
        return scanLocationLongitude;
    }

    public void setScanLocationLongitude(double scanLocationLongitude)
    {
        this.scanLocationLongitude = scanLocationLongitude;
    }

    public double getScanLocationLatitude()
    {
        return scanLocationLatitude;
    }

    public void setScanLocationLatitude(double scanLocationLatitude)
    {
        this.scanLocationLatitude = scanLocationLatitude;
    }
}
