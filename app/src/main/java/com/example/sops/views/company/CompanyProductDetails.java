package com.example.sops.views.company;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.product.ProductRepository;
import com.example.sops.views.other.LabelValuePair;
import com.example.sops.views.other.ProductDetailsArrayAdapter;

import java.util.ArrayList;

public class CompanyProductDetails extends AppCompatActivity
{
    private CompanyProductFragmentViewModel mViewModel;
    private ProductDetailsArrayAdapter mProductDetailsArrayAdapter;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_product_details);

        Bundle b = getIntent().getExtras();
        int productId = b.getInt("productId");

        mViewModel = new CompanyProductFragmentViewModel(getApplication());

        mProductDetailsArrayAdapter = new ProductDetailsArrayAdapter(
                this, new ArrayList<LabelValuePair>());
        mListView = findViewById(R.id.company_product_details_list_view);
        mListView.setAdapter(mProductDetailsArrayAdapter);

        mViewModel.getProductById(productId).observe(this, new Observer<Product>()
        {
            @Override
            public void onChanged(@Nullable Product product)
            {
                ArrayList<LabelValuePair> labelValuePairs = new ArrayList<>();
                labelValuePairs.add(new LabelValuePair(
                        "productId", Integer.toString(product.getId())
                ));
                labelValuePairs.add(new LabelValuePair(
                        getString(R.string.product_details_product_name_label), product.getName()
                ));
                labelValuePairs.add(new LabelValuePair(
                        getString(R.string.product_details_product_description_label), product.getDescription()
                ));
                labelValuePairs.add(new LabelValuePair(
                        getString(R.string.product_details_product_price_label), Float.toString(product.getSuggestedPrice())
                ));
                labelValuePairs.add(new LabelValuePair(
                        getString(R.string.product_details_product_barcode_label), product.getBarcode()
                ));
                labelValuePairs.add(new LabelValuePair(
                        getString(R.string.product_details_product_country_label), product.getCountryOfOrigin()
                ));

                mProductDetailsArrayAdapter.changeDataSet(labelValuePairs);
                mProductDetailsArrayAdapter.notifyDataSetChanged();
            }
        });
    }
}
