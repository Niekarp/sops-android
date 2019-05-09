package com.example.sops.views.profile;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.sops.data.persistence.entities.user.User;
import com.example.sops.data.persistence.entities.user.UserRepository;

import java.util.concurrent.Callable;

import javax.inject.Inject;

public class ProfileViewModel extends AndroidViewModel
{
    private UserRepository mUserRepository;

    public ProfileViewModel(Application application)
    {
        super(application);
        mUserRepository = UserRepository.getInstance(application);
    }

    LiveData<User> getUserById (String id)
    {
        return mUserRepository.getUserById(id);
    }

    public void downloadData (Callable<Void> onDataDownloaded)
    {
        mUserRepository.downloadUsersAsync(onDataDownloaded);
    }
}
