package com.example.sops.views.company;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProduct;
import com.example.sops.utils.AppUtils;
import com.example.sops.views.other.ProductAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CompanyProductFragment extends Fragment
{
    private Activity mActivity;
    private CompanyProductFragmentViewModel mViewModel;
    private ProductAdapter mRecyclerViewAdapter;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProductAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefresh;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public CompanyProductFragment()
    {
    }

    @SuppressWarnings("unused")
    public static CompanyProductFragment newInstance(int columnCount)
    {
        CompanyProductFragment fragment = new CompanyProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

        if (getArguments() != null)
        {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        // mRecyclerViewAdapter = new CompanyProductRecyclerViewAdapter(mListener);
        // mRecyclerViewAdapter = new ProductAdapter(this.getContext(), null, null);
        mViewModel = ViewModelProviders.of(this).get(CompanyProductFragmentViewModel.class);
        mViewModel.getCompanyProducts(AppUtils.getCurrentUserCompanyId(getActivity()))
                .observe(this, new Observer<List<Product>>()
                {
                    @Override
                    public void onChanged(@Nullable List<Product> products)
                    {
                        ((ProductAdapter) mAdapter).replaceProductDataSet((ArrayList<Product>) products);
                        mAdapter.notifyDataSetChanged();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_company_product_list,
                container, false);

        View view = rootView.findViewById(R.id.list);
        mSwipeRefresh = rootView.findViewById(R.id.company_product_swipe_refresh);

        // Set the adapter
        if (view instanceof RecyclerView)
        {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else
            {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mRecyclerViewAdapter);
        }

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (AppUtils.isInternetConnection(mActivity))
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
                    AppUtils.showSnackbar(mActivity,
                            R.id.company_product_swipe_refresh,
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
            mRecyclerView = this.getActivity().findViewById(R.id.list);
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
                        AppUtils.showSnackbar(getActivity(), R.id.company_product_swipe_refresh, R.string.no_internet_connection);
                    }
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener)
        {
            mListener = (OnListFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Object item);
    }
}
