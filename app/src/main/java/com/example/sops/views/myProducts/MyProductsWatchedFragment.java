package com.example.sops.views.myProducts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MyProductsWatchedFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;
    public MyProductsWatchedFragment()
    {
    }

    public static MyProductsWatchedFragment newInstance()
    {
        return new MyProductsWatchedFragment();
    }

    private CompanyProductFragmentViewModel mViewModel;
    private ProductAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(CompanyProductFragmentViewModel.class);

        mViewModel.getWatchedProducts().observe(this, new Observer<List<WatchedProduct>>()
        {
            @Override
            public void onChanged(@Nullable List<WatchedProduct> watchedProducts)
            {
                List<Integer> watchedProductIds = new ArrayList<Integer>(watchedProducts.size());
                for (WatchedProduct wp: watchedProducts)
                {
                    watchedProductIds.add(wp.getProductId());
                }
                mViewModel.getProductById(watchedProductIds).observe(MyProductsWatchedFragment.this,
                        new Observer<List<Product>>()
                {
                    @Override
                    public void onChanged(@Nullable List<Product> products)
                    {
                        ((ProductAdapter) mAdapter).replaceProductDataSet((ArrayList<Product>) products);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
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

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if(view != null)
        {
            mRecyclerView = this.getActivity().findViewById(R.id.my_products_watched_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            ArrayList<Product> books = new ArrayList<>();
            books.add(new Product(1062, "Produkt", "123", "desc", 1, "pl", new Date(123), 2, 1.4f));
            mAdapter = new ProductAdapter(this.getContext(), books,
                    new ArrayList<Company>(), new ArrayList<WatchedProduct>(),
                    new ProductAdapter.ProductViewHolder.ProductHolderClickListener()
                    {
                        @Override
                        public void onProductClicked(View caller, int position)
                        {
                            Log.d("my", "on product " + mAdapter.getProductId(position) + " clicked!");

                            Intent intent = new Intent(getActivity(), CompanyProductDetails.class);
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
                                    AppUtils.getCurrentUserId(getActivity())
                            );

                            if (AppUtils.isInternetConnection(getContext()))
                            {
                                mViewModel.toggleWatchedProduct(watchedProduct, null);
                            }
                            else
                            {
                                AppUtils.showSnackbar(getActivity(), R.id.my_products_watched_swipe_refresh, R.string.no_internet_connection);
                            }
                        }
                    });
            mRecyclerView.setAdapter(mAdapter);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_my_products_watched, container, false);

        mSwipeRefresh = rootView.findViewById(R.id.my_products_watched_swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (AppUtils.isInternetConnection(getActivity()))
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
                    AppUtils.showSnackbar(getActivity(),
                            R.id.my_products_watched_swipe_refresh,
                            R.string.no_internet_connection);
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });

        return rootView;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
