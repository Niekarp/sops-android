package com.example.sops.views.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProduct;
import com.example.sops.data.web.api.SopsApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>
{
    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ProductHolderClickListener mListener;
        public ImageView mProductImage;
        public ImageView mProductStar;
        public TextView mProductName;
        public TextView mCompanyName;

        public ProductViewHolder(View itemView, ProductHolderClickListener listener)
        {
            super(itemView);
            mListener = listener;
            mProductImage = itemView.findViewById(R.id.product_item_image);
            mProductStar = itemView.findViewById(R.id.product_item_star);
            mProductName = itemView.findViewById(R.id.product_item_product_name);
            mCompanyName = itemView.findViewById(R.id.product_item_company_name);

            mProductStar.setOnClickListener(this);
            mProductName.setOnClickListener(this);
            mCompanyName.setOnClickListener(this);
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

        public static interface ProductHolderClickListener
        {
            public void onProductClicked(View caller, int productId);
            public void onStarClicked(View caller, int productId);
        }
    }

    private ProductViewHolder.ProductHolderClickListener mListener;
    private Context mContext;
    private ArrayList<Product> mProducts;
    private HashMap<Integer, Company> mCompanies;
    private HashMap<Integer, Boolean> mWatchedProduct;

    public ProductAdapter(Context context,
                          ArrayList<Product> products, ArrayList<Company> companies,
                          ArrayList<WatchedProduct> watchedProducts,
                          ProductViewHolder.ProductHolderClickListener listener)
    {
        mListener = listener;
        mContext = context;
        mProducts = products;
        mCompanies = produceIdHashMap(companies);
        mWatchedProduct = productIdHashMap(watchedProducts);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.product_recycler_view_item, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(itemView, mListener);

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int position)
    {
        Product currentProduct = mProducts.get(position);

        String coverUrl = "http://" + SopsApi.IP + "/api/productpicture/"
                + currentProduct.getId();
        Picasso.get().load(coverUrl).placeholder(R.drawable.ic_insert_photo_24dp).fit()
                .into(productViewHolder.mProductImage);

        String noData = mContext.getString(R.string.no_data);
        String productName = noData;
        String companyName = noData;

        if (currentProduct.getName() != null)
        {
            productName = currentProduct.getName();
        }
        if (mCompanies.containsKey(currentProduct.getCompanyId()))
        {
            companyName = mCompanies.get(currentProduct.getCompanyId()).getName();
        }

        productViewHolder.mProductName.setText(productName);
        productViewHolder.mCompanyName.setText(companyName);

        boolean isWatched = mWatchedProduct.containsKey(currentProduct.getId());
        if (isWatched)
        {
            productViewHolder.mProductStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star_full_24dp));
        }
        else
        {
            productViewHolder.mProductStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star_empty_24dp));
        }
    }

    @Override
    public int getItemCount()
    {
        return mProducts.size();
    }

    public int getProductId(int position)
    {
        return mProducts.get(position).getId();
    }

    public void replaceProductDataSet(ArrayList<Product> products)
    {
        mProducts = products;
    }
    public void replaceCompanyDataSet(ArrayList<Company> companies)
    {
        mCompanies = produceIdHashMap(companies);
    }
    public void replaceWatchedProductDataSet(ArrayList<WatchedProduct> watchedProducts)
    {
        mWatchedProduct = productIdHashMap(watchedProducts);
    }

    private HashMap<Integer, Company> produceIdHashMap (List<Company> companies)
    {
        HashMap<Integer, Company> companyHashMap = new HashMap<>();
        for (Company c: companies)
        {
           companyHashMap.put(c.getId(), c);
        }
        return companyHashMap;
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
