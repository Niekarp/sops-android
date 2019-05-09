package com.example.sops.views.scan;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.sops.data.persistence.entities.scan.Scan;
import com.example.sops.data.persistence.entities.scan.ScanRepository;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProduct;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProductRepository;
import com.example.sops.data.web.models.ScanPostWeb;
import com.example.sops.utils.AppUtils;
import com.example.sops.utils.OnInserted;

import java.util.List;
import java.util.concurrent.Callable;

public class ScanViewModel extends AndroidViewModel
{
    private ScanRepository mScanRepository;
    private WatchedProductRepository mWatchedProductRepository;

    public ScanViewModel(Application application)
    {
        super(application);
        mScanRepository = ScanRepository.getInstance(application);
        mWatchedProductRepository = WatchedProductRepository.getInstance(application);
    }

    public void insert(ScanPostWeb scanPostWeb, OnInserted onInserted)
    {
        mScanRepository.insert(scanPostWeb, onInserted);
    }

    public LiveData<List<Scan>> getAll()
    {
        return mScanRepository.getAll();
    }

    public void toggleWatchedProduct(WatchedProduct watchedProduct, OnInserted onInserted)
    {
        mWatchedProductRepository.toggle(watchedProduct, onInserted);
    }

    public void downloadData(Callable<Void> onDataDownloaded)
    {
        mScanRepository.downloadScansAsync(onDataDownloaded, AppUtils.getCurrentUserId(getApplication()));
        mWatchedProductRepository.downloadWatchedProductsAsync(onDataDownloaded);
    }

    public LiveData<List<WatchedProduct>> getAllWatchedProducts()
    {
        return mWatchedProductRepository.getAll();
    }
}
