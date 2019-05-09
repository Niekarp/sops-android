package com.example.sops.data.persistence.entities.product;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;

import com.example.sops.data.persistence.database.SopsRoomDatabase;
import com.example.sops.data.web.api.SopsApi;
import com.example.sops.data.web.api.SopsApiManager;
import com.example.sops.utils.AppUtils;
import com.example.sops.utils.OnInserted;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Singleton;

@Singleton
public class ProductRepository
{
    // Singleton
    private static ProductRepository mInstance = null;
    public static ProductRepository getInstance(Application application)
    {
        if (mInstance == null)
        {
            mInstance = new ProductRepository(application);
        }
        return mInstance;
    }

    // Class
    private ProductDao mProductDao;

    public ProductRepository(Application application)
    {
        SopsRoomDatabase db = SopsRoomDatabase.getDatabaseInstance(application);
        mProductDao = db.productDao();

        if (AppUtils.isInternetConnection(application))
        {
            downloadProductsAsync(null);
        }
    }

    // Create
    public void insert(Product product, OnInserted onInserted)
    {
        new insertAsyncTask(mProductDao, onInserted).execute(product);
    }
    private static class insertAsyncTask extends AsyncTask<Product, Void, Void>
    {
        private ProductDao mAsyncTaskDao;
        private SopsApi mSopsApi;
        private OnInserted mOnInserted;
        insertAsyncTask(ProductDao dao, OnInserted onInserted)
        {
            mAsyncTaskDao = dao;
            mSopsApi = SopsApiManager.getApiInstance();
            mOnInserted = onInserted;
        }
        @Override
        protected Void doInBackground(final Product... params)
        {
            try
            {
                Product product = mSopsApi.postProduct(params[0]).execute().body();
                if (product != null)
                {
                    mAsyncTaskDao.insert(product);
                    if (mOnInserted != null)
                    {
                        mOnInserted.call(product);
                    }
                }
                else
                {
                    Log.d("my", "product from web is null");
                }
            }
            catch (Exception e)
            {
                Log.d("my", "post new product failed: " + e.getMessage());
            }
            return null;
        }
    }

    // Read
    public LiveData<List<Product>> getAllProducts()
    {
        return mProductDao.getAll();
    }

    public LiveData<List<Product>> getCompanyProducts(int companyId)
    {
        return mProductDao.getCompanyProducts(companyId);
    }

    public LiveData<List<Product>> getProductByIds(List<Integer> ids)
    {
        return mProductDao.get(ids);
    }

    public LiveData<Product> getProductById(int productId)
    {
        return mProductDao.get(productId);
    }

    // Update
    public void update(Product product)
    {
        new updateAsyncTask(mProductDao).execute(product);
    }
    private static class updateAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao mAsyncTaskDao;

        updateAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    // Delete
    public void delete(Product product)
    {
        new deleteAsyncTask(mProductDao).execute(product);
    }
    private static class deleteAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao mAsyncTaskDao;

        deleteAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    // Download
    public void downloadProductsAsync(Callable<Void> onDataDownloaded)
    {
        new DownloadProductsTask(mProductDao, onDataDownloaded).execute();
    }
    private static class DownloadProductsTask extends AsyncTask<Void, Void, Void>
    {
        private ProductDao mAsyncProductDao;
        private SopsApi mSopsApi;
        private Callable<Void> mOnDataDownloaded;
        DownloadProductsTask(ProductDao productDao, Callable<Void> onDataDownloaded)
        {
            mAsyncProductDao = productDao;
            mSopsApi = SopsApiManager.getApiInstance();
            mOnDataDownloaded = onDataDownloaded;
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                List<Product> products = mSopsApi.getAllProducts().execute().body();
                mAsyncProductDao.insertMany(products);
                if (mOnDataDownloaded != null)
                {
                    mOnDataDownloaded.call();
                }
            }
            catch (Exception e)
            {
                Log.d("my", "getProducts or insertMany problem: " + e.getMessage());
            }
            return null;
        }
    }
}
