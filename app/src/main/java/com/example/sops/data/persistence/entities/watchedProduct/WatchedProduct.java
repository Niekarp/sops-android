package com.example.sops.data.persistence.entities.watchedProduct;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "WatchedProducts")
public class WatchedProduct
{
    @PrimaryKey
    private int productId;
    private String userId;

    public WatchedProduct(int productId, String userId)
    {
        this.productId = productId;
        this.userId = userId;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
