package com.example.sops.data.persistence.entities.company;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sops.data.persistence.database.SopsRoomDatabase;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.web.api.SopsApi;
import com.example.sops.data.web.api.SopsApiManager;
import com.example.sops.utils.AppUtils;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Singleton;

@Singleton
public class CompanyRepository
{
    // Singleton
    private static CompanyRepository mInstance = null;
    public static CompanyRepository getInstance(Application application)
    {
        if (mInstance == null)
        {
            mInstance = new CompanyRepository(application);
        }
        return mInstance;
    }

    // Class
    private CompanyDao mCompanyDao;

    public CompanyRepository(Application application)
    {
        SopsRoomDatabase db = SopsRoomDatabase.getDatabaseInstance(application);
        mCompanyDao = db.companyDao();

        if (AppUtils.isInternetConnection(application))
        {
            downloadCompaniesAsync(null);
        }
    }

    // Read
    public LiveData<List<Company>> getAll()
    {
        return mCompanyDao.getAll();
    }

    // Download
    public void downloadCompaniesAsync(Callable<Void> onDataDownloaded)
    {
        new DownloadCompaniesTask(mCompanyDao, onDataDownloaded).execute();
    }
    private static class DownloadCompaniesTask extends AsyncTask<Void, Void, Void>
    {
        private CompanyDao mAsyncCompanyDao;
        private SopsApi mSopsApi;
        private Callable<Void> mOnDataDownloaded;
        DownloadCompaniesTask(CompanyDao companyDao, Callable<Void> onDataDownloaded)
        {
            mAsyncCompanyDao = companyDao;
            mSopsApi = SopsApiManager.getApiInstance();
            mOnDataDownloaded = onDataDownloaded;
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                List<Company> companies = mSopsApi.getCompanies().execute().body();
                mAsyncCompanyDao.insertMany(companies);
                if (mOnDataDownloaded != null)
                {
                    mOnDataDownloaded.call();
                }
            }
            catch (Exception e)
            {
                Log.d("my", "getCompanies or insertMany problem: " + e.getMessage());
            }
            return null;
        }
    }
}
