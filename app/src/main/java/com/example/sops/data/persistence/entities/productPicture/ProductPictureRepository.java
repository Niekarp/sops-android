package com.example.sops.data.persistence.entities.productPicture;

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
import com.example.sops.utils.AppUtils;

import java.nio.file.Files;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPictureRepository
{
    // Singleton
    private static ProductPictureRepository mInstance = null;
    public static ProductPictureRepository getInstance(Application application)
    {
        if (mInstance == null)
        {
            mInstance = new ProductPictureRepository(application);
        }
        return mInstance;
    }

    // Class
    private ProductPictureDao mProductPictureDao;

    public ProductPictureRepository(Application application)
    {
        SopsRoomDatabase db = SopsRoomDatabase.getDatabaseInstance(application);
        mProductPictureDao = db.productPictureDao();

        if (AppUtils.isInternetConnection(application))
        {
            // downloadProductPicturesAsync(null);
        }
    }

    // Create
    public void insert(ProductPicture productPicture)
    {
        new insertAsyncTask(mProductPictureDao).execute(productPicture);
    }
    private static class insertAsyncTask extends AsyncTask<ProductPicture, Void, Void>
    {
        private ProductPictureDao mAsyncProductPictureDao;
        private SopsApi mSopsApi;
        insertAsyncTask(ProductPictureDao dao)
        {
            mAsyncProductPictureDao = dao;
            mSopsApi = SopsApiManager.getApiInstance();
        }
        @Override
        protected Void doInBackground(final ProductPicture... params)
        {
            try
            {
                // byte[] fileContent = Files.readAllBytes(file.toPath());

                RequestBody body = RequestBody.create(MediaType.get("image/png"),
                        params[0].getContent());

                SopsApiManager.getApiInstance().postProductPicture(params[0].getProductId(), body)
                        .execute();

                mAsyncProductPictureDao.insert(params[0]);
            }
            catch (Exception e)
            {
                Log.d("my", "post new productPicture failed: " + e.getMessage());
            }
            return null;
        }
    }

    // Read
    public LiveData<ProductPicture> getByProductId(int productId)
    {
        return mProductPictureDao.get(productId);
    }
}
