package com.example.sops.data.persistence.entities.user;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sops.data.persistence.database.SopsRoomDatabase;
import com.example.sops.data.web.api.SopsApi;
import com.example.sops.data.web.api.SopsApiManager;
import com.example.sops.utils.AppUtils;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository
{
    // Singleton
    private static UserRepository mInstance = null;
    public static UserRepository getInstance(Application application)
    {
        if (mInstance == null)
        {
            mInstance = new UserRepository(application);
        }
        return mInstance;
    }

    // Class
    private UserDao mUserDao;

    private UserRepository(Application application)
    {
        SopsRoomDatabase db = SopsRoomDatabase.getDatabaseInstance(application);
        mUserDao = db.userDao();

        if (AppUtils.isInternetConnection(application))
        {
            downloadUsersAsync(null);
        }
    }

    // Create
    public void insert(User user)
    {
        new insertAsyncTask(mUserDao).execute(user);
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void>
    {
        private UserDao mAsyncUserDao;

        insertAsyncTask(UserDao dao) {
            mAsyncUserDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncUserDao.insert(params[0]);
            return null;
        }
    }

    // Read
    public LiveData<User> getUserById(String id)
    {
        return mUserDao.getUserById(id);
    }

    // Download
    public void downloadUsersAsync(Callable<Void> onDataDownloaded)
    {
        new DownloadUsersTask(mUserDao, onDataDownloaded).execute();
    }
    private static class DownloadUsersTask extends AsyncTask<Void, Void, Void>
    {
        private UserDao mAsyncUserDao;
        private SopsApi mSopsApi;
        private Callable<Void> mOnDataDownloaded;
        DownloadUsersTask(UserDao userDao, Callable<Void> onDataDownloaded)
        {
            mAsyncUserDao = userDao;
            mSopsApi = SopsApiManager.getApiInstance();
            mOnDataDownloaded = onDataDownloaded;
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                List<User> users = mSopsApi.getUsers().execute().body();
                mAsyncUserDao.insertMany(users);
                if (mOnDataDownloaded != null)
                {
                    mOnDataDownloaded.call();
                }
            }
            catch (Exception e)
            {
                Log.d("my", "getUsers or insertMany problem: " + e.getMessage());
            }

            return null;
        }
    }
}
