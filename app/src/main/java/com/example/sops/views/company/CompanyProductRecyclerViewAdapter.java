package com.example.sops.views.company;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.views.company.CompanyProductFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CompanyProductRecyclerViewAdapter extends
        RecyclerView.Adapter<CompanyProductRecyclerViewAdapter.ProductViewHolder>
{
    public class ProductViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView mProductName;

        private ProductViewHolder(View itemView)
        {
            super(itemView);
            mProductName = (TextView) itemView.findViewById(R.id.content);
        }
    }

    private List<Product> mProducts;
    private final OnListFragmentInteractionListener mListener;

    public CompanyProductRecyclerViewAdapter(OnListFragmentInteractionListener listener)
    {
        mListener = listener;
        mProducts = null;
    }

    void setProducts(List<Product> products)
    {
        this.mProducts = products;
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_company_product, parent, false);
        return new ProductViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position)
    {
        if (mProducts != null)
        {
            final Product current = mProducts.get(position);
            holder.mProductName.setText(current.getName());
        }
    }

    @Override
    public int getItemCount()
    {
        if (mProducts == null)
        {
            return 0;
        }
        return mProducts.size();
    }
}
