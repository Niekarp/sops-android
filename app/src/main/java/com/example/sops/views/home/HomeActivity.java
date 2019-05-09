package com.example.sops.views.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.sops.R;
import com.example.sops.views.company.CompanyActivity;
import com.example.sops.views.login.LoginActivity;
import com.example.sops.views.myProducts.MyProductsActivity;
import com.example.sops.views.products.ProductsActivity;
import com.example.sops.views.profile.ProfileActivity;
import com.example.sops.views.scan.ScanActivity;

import jp.wasabeef.blurry.Blurry;

public class HomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FrameLayout mProfileButton = findViewById(R.id.home_profile_button);
        FrameLayout mLogoutButton = findViewById(R.id.home_logout_button);
        FrameLayout mCompanyButton = findViewById(R.id.home_company_button);
        FrameLayout mScanButton = findViewById(R.id.home_scan_button);
        FrameLayout mMyProductsButton = findViewById(R.id.home_my_products_button);
        FrameLayout mProductsButton = findViewById(R.id.home_products_button);

        mProfileButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                clearUserSharedPreferences();
                startActivity(intent);
            }
        });
        mCompanyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, CompanyActivity.class);
                startActivity(intent);
            }
        });
        mScanButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });
        mMyProductsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, MyProductsActivity.class);
                startActivity(intent);
            }
        });
        mProductsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, ProductsActivity.class);
                startActivity(intent);
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
                    .onto((FrameLayout) findViewById(R.id.home_layout_background));
        }
    }

    // Helper methods
    private void clearUserSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.shared_prefs_file_user), Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
