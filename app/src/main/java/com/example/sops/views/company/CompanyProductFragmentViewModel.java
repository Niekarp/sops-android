package com.example.sops.views.company;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.widget.ListView;

import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.data.persistence.entities.company.CompanyRepository;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.product.ProductRepository;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProduct;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProductRepository;
import com.example.sops.utils.OnInserted;

import java.util.List;
import java.util.concurrent.Callable;

public class CompanyProductFragmentViewModel extends AndroidViewModel
{
    private ProductRepository mProductRepository;
    private CompanyRepository mCompanyRepository;
    private WatchedProductRepository mWatchedProductRepository;

    public CompanyProductFragmentViewModel(Application application)
    {
        super(application);
        mProductRepository = ProductRepository.getInstance(application);
        mCompanyRepository = CompanyRepository.getInstance(application);
        mWatchedProductRepository = WatchedProductRepository.getInstance(application);
    }

    public LiveData<List<Product>> getCompanyProducts(int companyId)
    {
        return mProductRepository.getCompanyProducts(companyId);
    }

    public LiveData<Product> getProductById(int productId)
    {
        return mProductRepository.getProductById(productId);
    }

    public LiveData<List<Product>> getProductById(List<Integer> ids)
    {
        return mProductRepository.getProductByIds(ids);
    }

    public LiveData<List<Product>> getProductByCompanyId(int companyId)
    {
        return mProductRepository.getCompanyProducts(companyId);
    }

    public LiveData<List<Company>> getCompanies()
    {
        return mCompanyRepository.getAll();
    }

    public void downloadData(Callable<Void> onDataDownloaded)
    {
        mProductRepository.downloadProductsAsync(onDataDownloaded);
        mCompanyRepository.downloadCompaniesAsync(onDataDownloaded);
        mWatchedProductRepository.downloadWatchedProductsAsync(onDataDownloaded);
    }

    // Watched Product
    public LiveData<List<WatchedProduct>> getWatchedProducts()
    {
        return mWatchedProductRepository.getAll();
    }

    public void toggleWatchedProduct(WatchedProduct watchedProduct, OnInserted onInserted)
    {
        mWatchedProductRepository.toggle(watchedProduct, onInserted);
    }
}
