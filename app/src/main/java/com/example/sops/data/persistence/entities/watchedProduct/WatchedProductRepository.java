package com.example.sops.data.persistence.entities.watchedProduct;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sops.data.persistence.database.SopsRoomDatabase;
import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.data.persistence.entities.company.CompanyDao;
import com.example.sops.data.persistence.entities.company.CompanyRepository;
import com.example.sops.data.web.api.SopsApi;
import com.example.sops.data.web.api.SopsApiManager;
import com.example.sops.data.web.models.WatchedProductStatusWeb;
import com.example.sops.utils.AppUtils;
import com.example.sops.utils.OnInserted;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Response;

public class WatchedProductRepository
{
    // Singleton
    private static WatchedProductRepository mInstance = null;
    public static WatchedProductRepository getInstance(Application application)
    {
        if (mInstance == null)
        {
            mInstance = new WatchedProductRepository(application);
        }
        return mInstance;
    }

    // Class
    private WatchedProductDao mWatchedProductDao;

    public WatchedProductRepository(Application application)
    {
        SopsRoomDatabase db = SopsRoomDatabase.getDatabaseInstance(application);
        mWatchedProductDao = db.watchedProductDao();

        if (AppUtils.isInternetConnection(application))
        {
            downloadWatchedProductsAsync(null);
        }
    }

    // Toggle
    public void toggle(WatchedProduct watchedProduct, OnInserted onInserted)
    {
        new toggleAsyncTask(mWatchedProductDao, onInserted).execute(watchedProduct);
    }
    private static class toggleAsyncTask extends AsyncTask<WatchedProduct, Void, Void>
    {
        private WatchedProductDao mAsyncWatchedPproductDao;
        private SopsApi mSopsApi;
        private OnInserted mOnInserted;
        toggleAsyncTask(WatchedProductDao watchedProductDao, OnInserted onInserted)
        {
            mAsyncWatchedPproductDao = watchedProductDao;
            mSopsApi = SopsApiManager.getApiInstance();
            mOnInserted = onInserted;
        }
        @Override
        protected Void doInBackground(final WatchedProduct... params)
        {
            try
            {
                WatchedProductStatusWeb watchedProductStatus = mSopsApi
                        .getWatchedProductCheck(params[0].getProductId()).execute().body();

                if (watchedProductStatus != null)
                {
                    if (watchedProductStatus.isWatched())
                    {
                        Response<Void> response = mSopsApi.deleteWatchedProduct(params[0].getProductId()).execute();
                        if (response.isSuccessful())
                        {
                            mAsyncWatchedPproductDao.delete(params[0]);
                        }
                        else
                        {
                            Log.d("my", "couldn't delete watchedProduct on server");
                        }
                    }
                    else
                    {
                        Response<Void> response = mSopsApi.postWatchedProduct(params[0].getProductId()).execute();
                        if (response.isSuccessful())
                        {
                            mAsyncWatchedPproductDao.insert(params[0]);
                        }
                        else
                        {
                            Log.d("my", "couldn't create watchedProduct on server");
                        }
                    }

                    if (mOnInserted != null)
                    {
                        mOnInserted.call(params[0]);
                    }
                }
                else
                {
                    Log.d("my", "isWatched from web is null");
                }
            }
            catch (Exception e)
            {
                Log.d("my", "get watchedProduct check failed: " + e.getMessage());
            }
            return null;
        }
    }

    // Create
    public void insert(WatchedProduct watchedProduct, OnInserted onInserted)
    {
        new insertAsyncTask(mWatchedProductDao, onInserted).execute(watchedProduct);
    }
    private static class insertAsyncTask extends AsyncTask<WatchedProduct, Void, Void>
    {
        private WatchedProductDao mAsyncWatchedPproductDao;
        private SopsApi mSopsApi;
        private OnInserted mOnInserted;
        insertAsyncTask(WatchedProductDao watchedProductDao, OnInserted onInserted)
        {
            mAsyncWatchedPproductDao = watchedProductDao;
            mSopsApi = SopsApiManager.getApiInstance();
            mOnInserted = onInserted;
        }
        @Override
        protected Void doInBackground(final WatchedProduct... params)
        {
            try
            {
                Response<Void> postResponse = mSopsApi.postWatchedProduct(params[0].getProductId())
                        .execute();
                if (postResponse.isSuccessful())
                {
                    mAsyncWatchedPproductDao.insert(params[0]);
                    if (mOnInserted != null)
                    {
                        mOnInserted.call(params[0]);
                    }
                }
                else
                {
                    Log.d("my", "watchedProduct from web is null");
                }
            }
            catch (Exception e)
            {
                Log.d("my", "post new watchedProduct failed: " + e.getMessage());
            }
            return null;
        }
    }

    // Read
    public LiveData<List<WatchedProduct>> getAll()
    {
        return mWatchedProductDao.getAll();
    }

    // Download
    public void downloadWatchedProductsAsync(Callable<Void> onDataDownloaded)
    {
        new WatchedProductRepository.DownloadWatchedProductsTask(mWatchedProductDao, onDataDownloaded).execute();
    }
    private static class DownloadWatchedProductsTask extends AsyncTask<Void, Void, Void>
    {
        private WatchedProductDao mAsyncWatchedProductDao;
        private SopsApi mSopsApi;
        private Callable<Void> mOnDataDownloaded;
        DownloadWatchedProductsTask(WatchedProductDao watchedProductDao, Callable<Void> onDataDownloaded)
        {
            mAsyncWatchedProductDao = watchedProductDao;
            mSopsApi = SopsApiManager.getApiInstance();
            mOnDataDownloaded = onDataDownloaded;
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                List<WatchedProduct> watchedProducts = mSopsApi.getWatchedProducts().execute().body();
                mAsyncWatchedProductDao.insertMany(watchedProducts);
                if (mOnDataDownloaded != null)
                {
                    mOnDataDownloaded.call();
                }
            }
            catch (Exception e)
            {
                Log.d("my", "getWatchedProducts or insertMany problem: " + e.getMessage());
            }
            return null;
        }
    }
}
