package com.example.sops.views.myProducts;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.scan.Scan;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProduct;
import com.example.sops.data.web.api.SopsApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyProductsScannAdapter extends RecyclerView.Adapter<MyProductsScannAdapter.ScanViewHolder>
{
    public static class ScanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ScanHolderClickListener mListener;
        public ImageView mProductImage;
        public ImageView mProductStar;
        public TextView mProductName;
        public TextView mAddress;
        public TextView mDate;

        public ScanViewHolder(View itemView, ScanHolderClickListener listener)
        {
            super(itemView);
            mListener = listener;
            mProductImage = itemView.findViewById(R.id.product_item_image);
            mProductStar = itemView.findViewById(R.id.product_item_star);
            mProductName = itemView.findViewById(R.id.product_item_product_name);
            mAddress = itemView.findViewById(R.id.product_item_address);
            mDate = itemView.findViewById(R.id.product_item_date);

            mProductStar.setOnClickListener(this);
            mProductName.setOnClickListener(this);
            mAddress.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            if (v instanceof ImageView)
            {
                mListener.onStarClicked(v, position);
            }
            else
            {
                mListener.onProductClicked(v, position);
            }
        }

        public static interface ScanHolderClickListener
        {
            public void onProductClicked(View caller, int position);
            public void onStarClicked(View caller, int position);
        }
    }

    private ScanViewHolder.ScanHolderClickListener mListener;
    private Context mContext;
    private ArrayList<Scan> mScans;
    private HashMap<Integer, Boolean> mWatchedProduct;

    public MyProductsScannAdapter(Context context,
                          ArrayList<Scan> scans,
                          ArrayList<WatchedProduct> watchedProducts,
                          ScanViewHolder.ScanHolderClickListener listener)
    {
        mListener = listener;
        mContext = context;
        mScans = scans;
        mWatchedProduct = productIdHashMap(watchedProducts);
    }

    @NonNull
    @Override
    public MyProductsScannAdapter.ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.my_products_scanns_recycler_view_item, parent, false);
        MyProductsScannAdapter.ScanViewHolder scanViewHolder =
                new MyProductsScannAdapter.ScanViewHolder(itemView, mListener);

        return scanViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsScannAdapter.ScanViewHolder scanViewHolder, int position)
    {
        Scan currentScan = mScans.get(position);

        String coverUrl = "http://" + SopsApi.IP + "/api/productpicture/"
                + currentScan.getProductId();
        Picasso.get().load(coverUrl).placeholder(R.drawable.ic_insert_photo_24dp).fit()
                .into(scanViewHolder.mProductImage);

        String noData = mContext.getString(R.string.no_data);
        String productName = noData;
        String scanDate = noData;
        String scanAddress = noData;

        if (currentScan.getProductName() != null)
        {
            productName = currentScan.getProductName();
        }
        if (currentScan.getScanDate() != null)
        {
            scanDate = currentScan.getScanDate().toString();
        }

        Address address = getScannAddress(currentScan.getScanLocationLatitude(),
                currentScan.getScanLocationLongitude());

        if (address != null)
        {
            scanAddress = address.getCountryName() + address.getLocality();
        }

        scanViewHolder.mProductName.setText(productName);
        scanViewHolder.mDate.setText(scanDate);
        scanViewHolder.mAddress.setText(scanAddress);

        boolean isWatched = mWatchedProduct.containsKey(currentScan.getProductId());
        if (isWatched)
        {
            scanViewHolder.mProductStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star_full_24dp));
        }
        else
        {
            scanViewHolder.mProductStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star_empty_24dp));
        }
    }

    @Override
    public int getItemCount()
    {
        return mScans.size();
    }

    public int getProductId(int position)
    {
        return mScans.get(position).getProductId();
    }

    public void replaceScanDataSet(ArrayList<Scan> scans)
    {
        mScans = scans;
    }
    public void replaceWatchedProductDataSet(ArrayList<WatchedProduct> watchedProducts)
    {
        mWatchedProduct = productIdHashMap(watchedProducts);
    }

    private Address getScannAddress(double latitude, double longnitude)
    {
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(mContext, Locale.getDefault());
        try
        {
            addresses = geocoder.getFromLocation(latitude, longnitude, 1);
        } catch (Exception e)
        {
        }

        if (addresses.size() == 0)
            return null;

        return addresses.get(0);
    }

    private HashMap<Integer, Boolean> productIdHashMap (List<WatchedProduct> watchedProducts)
    {
        HashMap<Integer, Boolean> watchedProductHashMap = new HashMap<>();
        for (WatchedProduct wp: watchedProducts)
        {
            watchedProductHashMap.put(wp.getProductId(), true);
        }
        return watchedProductHashMap;
    }
}
