package com.example.sops.views.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.user.User;
import com.example.sops.utils.AppUtils;

import java.util.concurrent.Callable;

public class ProfileActivity extends AppCompatActivity
{
    private ProfileViewModel mProfileViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mRoleValue;
    private TextView mUsernameValue;
    private TextView mNameValue;
    private TextView mSurnameValue;
    private TextView mPhoneValue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        mSwipeRefreshLayout = findViewById(R.id.profile_swipe_refresh);
        mRoleValue = findViewById(R.id.profile_role_value);
        mUsernameValue = findViewById(R.id.profile_username_value);
        mNameValue = findViewById(R.id.profile_name_value);
        mSurnameValue = findViewById(R.id.profile_surname_value);
        mPhoneValue = findViewById(R.id.profile_phone_value);

        mProfileViewModel.getUserById(AppUtils.getCurrentUserId(this))
                .observe(this, new Observer<User>()
                {
                    @Override
                    public void onChanged(@Nullable User user)
                    {
                        if (user == null)
                        {
                            Log.d("my", "getUserByUsername returned null");
                            return;
                        }

                        if (AppUtils.getCurrentUserCompanyId(ProfileActivity.this) >= 0)
                        {
                            mRoleValue.setText(R.string.role_employee);
                        }
                        else
                        {
                            mRoleValue.setText(R.string.role_user);
                        }

                        mUsernameValue.setText(user.getUserName());
                        mNameValue.setText(user.getName());
                        mSurnameValue.setText(user.getSurname());
                        mPhoneValue.setText(user.getPhoneNumber());
                    }
                });

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener()
                {
                    @Override
                    public void onRefresh()
                    {
                        if (AppUtils.isInternetConnection(ProfileActivity.this))
                        {
                            mProfileViewModel.downloadData(new Callable<Void>()
                            {
                                @Override
                                public Void call() throws Exception
                                {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    return null;
                                }
                            });
                        }
                        else
                        {
                            AppUtils.showSnackbar(ProfileActivity.this,
                                    R.id.profile_swipe_refresh,
                                    R.string.no_internet_connection);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }
}
