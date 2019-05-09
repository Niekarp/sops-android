package com.example.sops.data.persistence.entities.scan;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ScanDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Scan scan);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Scan> scans);

    @Query("SELECT * FROM Scans")
    LiveData<List<Scan>> getAll();
}
