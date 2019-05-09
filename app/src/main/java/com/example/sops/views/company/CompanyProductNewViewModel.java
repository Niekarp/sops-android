package com.example.sops.views.company;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.product.ProductRepository;
import com.example.sops.data.persistence.entities.productPicture.ProductPicture;
import com.example.sops.data.persistence.entities.productPicture.ProductPictureRepository;
import com.example.sops.utils.AppUtils;
import com.example.sops.utils.OnInserted;

import java.util.List;
import java.util.concurrent.Callable;

public class CompanyProductNewViewModel extends AndroidViewModel
{
    private ProductRepository mProductRepository;
    private ProductPictureRepository mProductPictureRepository;

    public CompanyProductNewViewModel(Application application)
    {
        super(application);
        mProductRepository = ProductRepository.getInstance(application);
        mProductPictureRepository = ProductPictureRepository.getInstance(application);
    }

    // Product
    void insertProduct(Product product, OnInserted onInserted)
    {
        mProductRepository.insert(product, onInserted);
    }

    LiveData<List<Product>> getCompanyProducts(int companyId)
    {
        return mProductRepository.getCompanyProducts(companyId);
    }

    public void downloadData(Callable<Void> onDataDownloaded)
    {
        mProductRepository.downloadProductsAsync(onDataDownloaded);
    }

    // Product picture
    void insertProductPicture(ProductPicture productPicture)
    {
        mProductPictureRepository.insert(productPicture);
    }
}
