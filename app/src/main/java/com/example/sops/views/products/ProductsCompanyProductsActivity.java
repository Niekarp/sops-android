package com.example.sops.views.products;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProduct;
import com.example.sops.utils.AppUtils;
import com.example.sops.views.company.CompanyProductDetails;
import com.example.sops.views.company.CompanyProductFragmentViewModel;
import com.example.sops.views.other.ProductAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class ProductsCompanyProductsActivity extends AppCompatActivity
{
    private CompanyProductFragmentViewModel mViewModel;
    private ProductAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_company_products);

        Bundle b = getIntent().getExtras();
        int companyId = b.getInt("companyId");

        mSwipeRefresh = findViewById(R.id.products_company_products_swipe_refresh);

        mViewModel = ViewModelProviders.of(this).get(CompanyProductFragmentViewModel.class);

        mViewModel.getCompanies().observe(this, new Observer<List<Company>>()
        {
            @Override
            public void onChanged(@Nullable List<Company> companies)
            {
                ((ProductAdapter) mAdapter).replaceCompanyDataSet((ArrayList<Company>) companies);
                mAdapter.notifyDataSetChanged();
            }
        });
        mViewModel.getWatchedProducts().observe(this, new Observer<List<WatchedProduct>>()
        {
            @Override
            public void onChanged(@Nullable List<WatchedProduct> watchedProducts)
            {
                ((ProductAdapter) mAdapter).replaceWatchedProductDataSet((ArrayList<WatchedProduct>) watchedProducts);
                mAdapter.notifyDataSetChanged();
            }
        });
        mViewModel.getCompanyProducts(companyId).observe(this, new Observer<List<Product>>()
        {
            @Override
            public void onChanged(@Nullable List<Product> products)
            {
                ((ProductAdapter) mAdapter).replaceProductDataSet((ArrayList<Product>) products);
                mAdapter.notifyDataSetChanged();
            }
        });


        mRecyclerView = this.findViewById(R.id.products_company_products_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Product> books = new ArrayList<>();
        books.add(new Product(1062, "Produkt", "123", "desc", 1, "pl", new Date(123), 2, 1.4f));
        mAdapter = new ProductAdapter(this, books,
                new ArrayList<Company>(), new ArrayList<WatchedProduct>(),
                new ProductAdapter.ProductViewHolder.ProductHolderClickListener()
                {
                    @Override
                    public void onProductClicked(View caller, int position)
                    {
                        Log.d("my", "on product " + mAdapter.getProductId(position) + " clicked!");

                        Intent intent = new Intent(ProductsCompanyProductsActivity.this, CompanyProductDetails.class);
                        Bundle b = new Bundle();
                        b.putInt("productId", mAdapter.getProductId(position));
                        intent.putExtras(b);
                        startActivity(intent);
                    }

                    @Override
                    public void onStarClicked(View caller, int position)
                    {
                        Log.d("my", "on star clicked!");

                        WatchedProduct watchedProduct = new WatchedProduct(
                                mAdapter.getProductId(position),
                                AppUtils.getCurrentUserId(ProductsCompanyProductsActivity.this)
                        );

                        if (AppUtils.isInternetConnection(
                                ProductsCompanyProductsActivity.this))
                        {
                            mViewModel.toggleWatchedProduct(watchedProduct, null);
                        }
                        else
                        {
                            AppUtils.showSnackbar(ProductsCompanyProductsActivity.this, R.id.products_company_products_swipe_refresh, R.string.no_internet_connection);
                        }
                    }
                });
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (AppUtils.isInternetConnection(getApplicationContext()))
                {
                    mViewModel.downloadData(new Callable<Void>()
                    {
                        @Override
                        public Void call() throws Exception
                        {
                            mSwipeRefresh.setRefreshing(false);
                            return null;
                        }
                    });
                }
                else
                {
                    AppUtils.showSnackbar(ProductsCompanyProductsActivity.this,
                            R.id.products_company_products_swipe_refresh,
                            R.string.no_internet_connection);
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }
}
