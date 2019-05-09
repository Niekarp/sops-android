package com.example.sops.data.persistence.entities.company;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CompanyDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Company> companies);

    @Query("SELECT * FROM Companies")
    LiveData<List<Company>> getAll();
}
