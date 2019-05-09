package com.example.sops.data.web.api;

import android.arch.persistence.room.Delete;

import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.scan.Scan;
import com.example.sops.data.persistence.entities.user.User;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProduct;
import com.example.sops.data.web.models.CurrentUserInfoWeb;
import com.example.sops.data.web.models.ProductWeb;
import com.example.sops.data.web.models.ScanPostWeb;
import com.example.sops.data.web.models.TokenWeb;
import com.example.sops.data.web.models.UserCredentialsWeb;
import com.example.sops.data.web.models.WatchedProductStatusWeb;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SopsApi
{
    // Emulator
    // public String BASE_URL = "http://10.0.2.2:56971/api/";

    public String IP = "192.168.42.209:56971";
    public String BASE_URL = "http://" + IP + "/api/";
    public String TOKEN_URL = "http://" + IP + "/token";

    // Token
    // @POST("http://10.0.2.2:56971/token")
    @POST(TOKEN_URL)
    @FormUrlEncoded
    Call<TokenWeb> postToken(@Field("grant_type") String grantType,
                             @Field("username") String username,
                             @Field("password") String password);

    // User
    @GET("User")
    Call<List<User>> getUsers();

    // Current user
    @GET("Account/UserInfo")
    Call<CurrentUserInfoWeb> getCurrentUserInfo();

    // Product
    @GET("Product")
    Call<List<Product>> getAllProducts();

    @POST("Product")
    Call<Product> postProduct(@Body Product product);

    @GET("Product/{id}")
    Call<ProductWeb> getProduct(@Path("id") int id);

    // Product picture
    @POST("ProductPicture/{id}")
    Call<Void> postProductPicture(@Path("id") int id, @Body RequestBody bytes);

    // Employee
    @GET("Employee/Products")
    Call<List<ProductWeb>> getEmployeeCompanyProducts();

    // Scan
    @GET("Scan/{id}")
    Call<List<Scan>> getScans(@Path("id") String userId);

    @POST("Scan")
    Call<Scan> postScan(@Body ScanPostWeb scanPostWeb);

    // Company
    @GET("Company")
    Call<List<Company>> getCompanies();

    // Watched Product
    @GET("WatchedProduct")
    Call<List<WatchedProduct>> getWatchedProducts();

    @GET("WatchedProduct/Check")
    Call<WatchedProductStatusWeb> getWatchedProductCheck(@Query("id") int productId);

    @POST("WatchedProduct/{id}")
    Call<Void> postWatchedProduct(@Query("id") int productId);

    @DELETE("WatchedProduct/{id}")
    Call<Void> deleteWatchedProduct(@Path("id") int productId);
}
