package com.example.sops.views.scan;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.sops.R;
import com.example.sops.data.other.Qr;
import com.example.sops.data.persistence.entities.scan.Scan;
import com.example.sops.data.web.api.SopsApiManager;
import com.example.sops.data.web.models.ScanPostWeb;
import com.example.sops.utils.AppUtils;
import com.example.sops.utils.OnInserted;
import com.example.sops.views.other.LabelValuePair;
import com.example.sops.views.other.ProductDetailsArrayAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity
{
    static final int REQUEST_SCAN_QR = 0;

    ScanViewModel mViewModel;

    private ProductDetailsArrayAdapter mProductDetailsArrayAdapter;
    private ListView mListView;
    private Button mNewScanButton;

    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mViewModel = ViewModelProviders.of(this).get(ScanViewModel.class);

        mProductDetailsArrayAdapter = new ProductDetailsArrayAdapter(
                this, new ArrayList<LabelValuePair>());
        mListView = findViewById(R.id.scan_list_view);
        mListView.setAdapter(mProductDetailsArrayAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

        try
        {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

            startActivityForResult(intent, REQUEST_SCAN_QR);

        } catch (Exception e)
        {

            Log.d("my", "user zxing scanner not found");
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SCAN_QR)
        {
            if (resultCode == RESULT_OK)
            {
                String contents = data.getStringExtra("SCAN_RESULT");

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .create();
                Qr qr = gson.fromJson(contents, Qr.class);

                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("my", "no permission for location");
                    return;
                }

                Location location = getLastKnownLocation();

                ScanPostWeb scanPostWeb = new ScanPostWeb();
                scanPostWeb.setExistingProductId(qr.existingProductId);
                scanPostWeb.setExistingProductSecret(qr.secret);
                scanPostWeb.setLocationLatitude(location.getLatitude());
                scanPostWeb.setLocationLongitude(location.getLongitude());

                mViewModel.insert(scanPostWeb, new OnInserted()
                {
                    @Override
                    public void call(Object insertedObject)
                    {
                        Scan scan = (Scan) insertedObject;

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(ScanActivity.this, Locale.getDefault());
                        try
                        {
                            addresses = geocoder.getFromLocation(scan.getScanLocationLatitude(),
                                    scan.getScanLocationLongitude(), 1);
                            String address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();

                            ArrayList<LabelValuePair> labelValuePairs = new ArrayList<>();
                            labelValuePairs.add(new LabelValuePair(
                                    "productId", Integer.toString(scan.getProductId())
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Product Name", scan.getProductName()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Company Name", scan.getCompanyName()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Expiration Date", scan.getExistingProductExpirationDate().toString()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Scan Address", address
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Barcode", scan.getProductBarcode()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Creation Date", scan.getExistingProductCreationDate().toString()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Made in", scan.getProductCountryOfOrigin()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Description", scan.getProductDescription()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Price", Float.toString(scan.getProductSuggestedPrice())
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Company Email", scan.getCompanyEmail()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Company City", scan.getCompanyAddressCity()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Company NIP", scan.getCompanyNIP()
                            ));
                            labelValuePairs.add(new LabelValuePair(
                                    "Company join", scan.getCompanyJoinDate().toString()
                            ));

                            mProductDetailsArrayAdapter.changeDataSet(labelValuePairs);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mProductDetailsArrayAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        catch (Exception e){}
                    }
                });
            }
            if(resultCode == RESULT_CANCELED)
            {
                finish();
            }
        }
    }

    private Location getLastKnownLocation()
    {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
