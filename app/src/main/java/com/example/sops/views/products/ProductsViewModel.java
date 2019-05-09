package com.example.sops.views.products;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.data.persistence.entities.company.CompanyRepository;

import java.util.List;

public class ProductsViewModel extends AndroidViewModel
{
    private CompanyRepository mCompanyRepository;

    public ProductsViewModel(Application application)
    {
        super(application);
        mCompanyRepository = CompanyRepository.getInstance(application);
    }

    LiveData<List<Company>> getCompanies()
    {
        return mCompanyRepository.getAll();
    }
}
