package com.example.sops.views.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.sops.R;
import com.example.sops.data.web.api.SopsApi;
import com.example.sops.data.web.api.SopsApiManager;
import com.example.sops.data.web.models.CurrentUserInfoWeb;
import com.example.sops.data.web.models.TokenWeb;
import com.example.sops.data.web.models.UserCredentialsWeb;
import com.example.sops.utils.AppUtils;
import com.example.sops.views.home.HomeActivity;

import java.util.concurrent.Callable;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    private SharedPreferences mSharedPreferences;
    private SopsApi mSopsApi;

    private AppCompatEditText mEmailInput;
    private AppCompatEditText mPasswordInput;
    private AppCompatButton mLoginButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.activity_title_login);

        mSharedPreferences = getSharedPreferences(getString(R.string.shared_prefs_file_user), Context.MODE_PRIVATE);
        mSopsApi = SopsApiManager.getApiInstance();

        mEmailInput = findViewById(R.id.login_email_input);
        mPasswordInput = findViewById(R.id.login_password_input);
        mLoginButton = findViewById(R.id.login_login_button);
        mProgressBar = findViewById(R.id.login_progress_bar);

        // Check if user is logged in
        final String token = getSavedToken();

        if (isTokenNotEmpty(token))
        {
            // user is logged in
            SopsApiManager.setTokenInEveryHeader(token);
            openHomeActivity();
        }

        // User tries to log in
        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isUsernameOrPasswordFieldEmpty())
                {
                    AppUtils.showSnackbar(LoginActivity.this, R.id.login_layout,
                            R.string.input_fields_empty);
                    return;
                }

                if (AppUtils.isInternetConnection(LoginActivity.this) == false)
                {
                    AppUtils.showSnackbar(LoginActivity.this, R.id.login_layout,
                            R.string.no_internet_connection);
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                UserCredentialsWeb userCredentialsWeb = new UserCredentialsWeb();
                userCredentialsWeb.username = mEmailInput.getText().toString();
                userCredentialsWeb.password = mPasswordInput.getText().toString();

                Call<TokenWeb> tokenWebCall = mSopsApi.postToken(
                        userCredentialsWeb.getGrantType(),
                        userCredentialsWeb.username,
                        userCredentialsWeb.password);

                tokenWebCall.enqueue(new Callback<TokenWeb>()
                {
                    @Override
                    public void onResponse(Call<TokenWeb> call, Response<TokenWeb> response)
                    {
                        if (response.isSuccessful())
                        {
                            if (response.body() != null)
                            {
                                saveReceivedToken(response.body().accessToken);
                                SopsApiManager.setTokenInEveryHeader(response.body().accessToken);
                                downloadAndSaveUserInfo(new Callable<Void>()
                                {
                                    // onDownloaded
                                    @Override
                                    public Void call() throws Exception
                                    {
                                        openHomeActivity();
                                        return null;
                                    }
                                });
                            }
                        }
                        else
                        {
                            AppUtils.logDetails(call, response);
                            AppUtils.showSnackbar(LoginActivity.this, R.id.login_layout,
                                    R.string.login_response_unsuccessful);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenWeb> call, Throwable t)
                    {
                        AppUtils.logDetails(call, t);
                        AppUtils.showSnackbar(LoginActivity.this, R.id.login_layout,
                                R.string.internal_error_message);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus)
        {
            Blurry.with(getApplicationContext())
                    .radius(2)
                    .sampling(4)
                    .color(Color.argb(1, 120, 120, 120))
                    .async()
                    .animate(500)
                    .onto((FrameLayout) findViewById(R.id.login_layout_background));
        }
    }

    // Helper methods
    private String getSavedToken()
    {
        return mSharedPreferences.getString
                (
                        getString(R.string.shared_prefs_token_key),
                        getString(R.string.shared_prefs_token_default)
                );
    }

    private void saveReceivedToken(String receivedToken)
    {
        mSharedPreferences.edit()
                .putString(getString(R.string.shared_prefs_token_key), receivedToken)
                .apply();
    }

    private void downloadAndSaveUserInfo(final Callable<Void> onDownloaded)
    {
        mSopsApi = SopsApiManager.getApiInstance();
        mSopsApi.getCurrentUserInfo().enqueue(new Callback<CurrentUserInfoWeb>()
        {
            @Override
            public void onResponse(Call<CurrentUserInfoWeb> call, Response<CurrentUserInfoWeb> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {
                        mSharedPreferences.edit()
                                .putString(
                                        getString(R.string.shared_prefs_user_id_key),
                                        response.body().getId())
                                .apply();

                        mSharedPreferences.edit()
                                .putString(
                                        getString(R.string.shared_prefs_company_id_key),
                                        Integer.toString(response.body().getCompanyId()))
                                .apply();

                        try
                        {
                            onDownloaded.call();
                        }
                        catch (Exception e)
                        {
                            Log.d("my", e.getMessage());
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                    else
                    {
                        AppUtils.logDetails(call, response);
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    AppUtils.logDetails(call, response);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CurrentUserInfoWeb> call, Throwable t)
            {
                AppUtils.logDetails(call, t);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean isTokenNotEmpty(String token)
    {
        return (token.equals(getString(R.string.shared_prefs_token_default)) == false);
    }

    private boolean isUsernameOrPasswordFieldEmpty()
    {
        if (mEmailInput.getText().toString().isEmpty())
        {
            return true;
        }
        else if (mPasswordInput.getText().toString().isEmpty())
        {
            return true;
        }

        return false;
    }

    private void openHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
