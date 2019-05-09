package com.example.sops.data.persistence.entities.productPicture;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface ProductPictureDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductPicture productPicture);

    @Query("SELECT * FROM PRODUCTPICTURES WHERE productId = :productId")
    LiveData<ProductPicture> get(int productId);
}
