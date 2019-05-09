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
import com.example.sops.data.persistence.entities.scan.Scan;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProduct;
import com.example.sops.utils.AppUtils;
import com.example.sops.views.company.CompanyProductDetails;
import com.example.sops.views.other.ProductAdapter;
import com.example.sops.views.scan.ScanViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class MyProductsScannedFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;
    public MyProductsScannedFragment()
    {
    }

    public static MyProductsScannedFragment newInstance()
    {
        return new MyProductsScannedFragment();
    }

    private ScanViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyProductsScannAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ScanViewModel.class);

        mViewModel.getAll().observe(this, new Observer<List<Scan>>()
        {
            @Override
            public void onChanged(@Nullable List<Scan> scans)
            {
                mAdapter.replaceScanDataSet((ArrayList<Scan>)scans);
                mAdapter.notifyDataSetChanged();
            }
        });
        mViewModel.getAllWatchedProducts().observe(this, new Observer<List<WatchedProduct>>()
        {
            @Override
            public void onChanged(@Nullable List<WatchedProduct> watchedProducts)
            {
                mAdapter.replaceWatchedProductDataSet((ArrayList<WatchedProduct>) watchedProducts);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_my_products_scanned, container, false);

        mSwipeRefresh = rootView.findViewById(R.id.my_products_scanned_swipe_refresh);
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
                            R.id.my_products_scanned_swipe_refresh,
                            R.string.no_internet_connection);
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if(view != null)
        {
            mRecyclerView = this.getActivity().findViewById(R.id.my_products_scanned_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new MyProductsScannAdapter(this.getContext(),
                    new ArrayList<Scan>(),
                    new ArrayList<WatchedProduct>(),
                    new MyProductsScannAdapter.ScanViewHolder.ScanHolderClickListener()
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
                                AppUtils.showSnackbar(getActivity(), R.id.my_products_scanned_swipe_refresh, R.string.no_internet_connection);
                            }
                        }
                    });
            mRecyclerView.setAdapter(mAdapter);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
