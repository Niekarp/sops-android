package com.example.sops.data.persistence.entities.productPicture;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ProductPictures")
public class ProductPicture
{
    @PrimaryKey
    private int productId;
    private byte[] content;

    public ProductPicture(int productId, byte[] content)
    {
        this.productId = productId;
        this.content = content;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public byte[] getContent()
    {
        return content;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }
}
