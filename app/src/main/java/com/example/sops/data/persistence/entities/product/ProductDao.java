package com.example.sops.data.persistence.entities.product;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ProductDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Product> products);

    @Query("SELECT * FROM Products")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM Products WHERE companyId = :companyId")
    LiveData<List<Product>> getCompanyProducts(int companyId);

    @Query("SELECT * FROM Products WHERE id = :productId")
    LiveData<Product> get(int productId);

    @Query("SELECT * FROM Products WHERE id IN (:ids)")
    LiveData<List<Product>> get(List<Integer> ids);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM Products")
    void deleteAll();
}
