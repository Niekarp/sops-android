package com.example.sops.data.persistence.entities.scan;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sops.data.persistence.database.SopsRoomDatabase;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.product.ProductDao;
import com.example.sops.data.persistence.entities.product.ProductRepository;
import com.example.sops.data.web.api.SopsApi;
import com.example.sops.data.web.api.SopsApiManager;
import com.example.sops.data.web.models.ScanPostWeb;
import com.example.sops.utils.AppUtils;
import com.example.sops.utils.OnInserted;

import java.util.List;
import java.util.concurrent.Callable;

public class ScanRepository
{
    // Singleton
    private static ScanRepository mInstance = null;
    public static ScanRepository getInstance(Application application)
    {
        if (mInstance == null)
        {
            mInstance = new ScanRepository(application);
        }
        return mInstance;
    }

    // Class
    private ScanDao mScanDao;
    private Application mApplication;

    private ScanRepository(Application application)
    {
        SopsRoomDatabase db = SopsRoomDatabase.getDatabaseInstance(application);
        mScanDao = db.scanDao();

        if (AppUtils.isInternetConnection(application))
        {
            downloadScansAsync(null, AppUtils.getCurrentUserId(application));
        }
    }

    // Create
    public void insert(ScanPostWeb scan, OnInserted onInserted)
    {
        new ScanRepository.insertAsyncTask(mScanDao, onInserted).execute(scan);
    }
    private static class insertAsyncTask extends AsyncTask<ScanPostWeb, Void, Void>
    {
        private ScanDao mAsyncTaskDao;
        private SopsApi mSopsApi;
        private OnInserted mOnInserted;
        insertAsyncTask(ScanDao dao, OnInserted onInserted)
        {
            mAsyncTaskDao = dao;
            mSopsApi = SopsApiManager.getApiInstance();
            mOnInserted = onInserted;
        }
        @Override
        protected Void doInBackground(final ScanPostWeb... params)
        {
            try
            {
                Scan scan = mSopsApi.postScan(params[0]).execute().body();
                if (scan != null)
                {
                    mAsyncTaskDao.insert(scan);
                    if (mOnInserted != null)
                    {
                        mOnInserted.call(scan);
                    }
                }
                else
                {
                    Log.d("my", "scan from web is null");
                }
            }
            catch (Exception e)
            {
                Log.d("my", "post new scan failed: " + e.getMessage());
            }
            return null;
        }
    }

    // Read
    public LiveData<List<Scan>> getAll()
    {
        return mScanDao.getAll();
    }

    // Download
    public void downloadScansAsync(Callable<Void> onDataDownloaded, String userId)
    {
        new DownloadPScansTask(mScanDao, onDataDownloaded, userId).execute();
    }
    private static class DownloadPScansTask extends AsyncTask<Void, Void, Void>
    {
        private ScanDao mAsyncScanDao;
        private SopsApi mSopsApi;
        private Callable<Void> mOnDataDownloaded;
        private String mUserId;
        DownloadPScansTask(ScanDao scanDao, Callable<Void> onDataDownloaded, String userId)
        {
            mAsyncScanDao = scanDao;
            mSopsApi = SopsApiManager.getApiInstance();
            mOnDataDownloaded = onDataDownloaded;
            mUserId = userId;
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                List<Scan> scans = mSopsApi.getScans(mUserId).execute().body();
                mAsyncScanDao.insertMany(scans);
                if (mOnDataDownloaded != null)
                {
                    mOnDataDownloaded.call();
                }
            }
            catch (Exception e)
            {
                Log.d("my", "getScans or insertMany problem: " + e.getMessage());
            }
            return null;
        }
    }
}
