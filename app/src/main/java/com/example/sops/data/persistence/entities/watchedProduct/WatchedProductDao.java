package com.example.sops.data.persistence.entities.watchedProduct;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WatchedProductDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<WatchedProduct> watchedProducts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WatchedProduct watchedProduct);

    @Query("SELECT * FROM WatchedProducts")
    LiveData<List<WatchedProduct>> getAll();

    @Delete
    void delete(WatchedProduct watchedProduct);
}
