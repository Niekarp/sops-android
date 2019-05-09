package com.example.sops.views.products;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.views.company.CompanyProductDetails;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity
{
    private ProductsViewModel mViewModel;
    private CompanyDetailsArrayAdapter mCompanyDetailsArrayAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        mViewModel = new ProductsViewModel(getApplication());

        mCompanyDetailsArrayAdapter = new CompanyDetailsArrayAdapter(
                this, new ArrayList<Company>(),
                new CompanyDetailsArrayAdapter.ButtonClickedListener()
                {
                    @Override
                    public void onButtonClicked(View caller, int companyId)
                    {
                        Intent intent = new Intent(ProductsActivity.this,
                                ProductsCompanyProductsActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("companyId", companyId);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
        mListView = findViewById(R.id.products_company_list_view);
        mListView.setAdapter(mCompanyDetailsArrayAdapter);

        mViewModel.getCompanies().observe(this, new Observer<List<Company>>()
        {
            @Override
            public void onChanged(@Nullable List<Company> companies)
            {
                mCompanyDetailsArrayAdapter.changeDataSet((ArrayList<Company>) companies);
                mCompanyDetailsArrayAdapter.notifyDataSetChanged();
            }
        });
    }
}
