package com.example.sops.data.persistence.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.sops.data.persistence.converters.Converters;
import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.data.persistence.entities.company.CompanyDao;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.product.ProductDao;
import com.example.sops.data.persistence.entities.productPicture.ProductPicture;
import com.example.sops.data.persistence.entities.productPicture.ProductPictureDao;
import com.example.sops.data.persistence.entities.scan.Scan;
import com.example.sops.data.persistence.entities.scan.ScanDao;
import com.example.sops.data.persistence.entities.user.User;
import com.example.sops.data.persistence.entities.user.UserDao;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProduct;
import com.example.sops.data.persistence.entities.watchedProduct.WatchedProductDao;

import java.util.Date;

@Database(entities =
        {
            User.class,
            Product.class,
            ProductPicture.class,
            Scan.class,
            Company.class,
            WatchedProduct.class
        },
        version = 11)
@TypeConverters({Converters.class})
public abstract class SopsRoomDatabase extends RoomDatabase
{
    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract ProductPictureDao productPictureDao();
    public abstract ScanDao scanDao();
    public abstract CompanyDao companyDao();
    public abstract WatchedProductDao watchedProductDao();

    // Singleton
    private static volatile SopsRoomDatabase mInstance;

    public static SopsRoomDatabase getDatabaseInstance(final Context context)
    {
        if (mInstance == null)
        {
            synchronized (SopsRoomDatabase.class)
            {
                if (mInstance == null)
                {
                    mInstance = Room.databaseBuilder
                            (
                            context.getApplicationContext()
                            ,SopsRoomDatabase.class, "product_database"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return mInstance;
    }

    // Do on database open
    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback()
            {
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db)
                {
                    super.onOpen(db);
                    // new PopulateDbAsync(mInstance).execute();
                }
            };

    // Seed data
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>
    {
        private final ProductDao mProductDao;

        PopulateDbAsync(SopsRoomDatabase db)
        {
            mProductDao = db.productDao();
        }

        @Override
        protected Void doInBackground(final Void... params)
        {
            mProductDao.deleteAll();

            /*Product product0 = new Product(0,
                    "newProductName0",
                    "123",
                    "dw",
                    0,
                    "pl",
                    new Date(10),
                    2,
                    13);
            mProductDao.update(product0);*/

            return null;
        }
    }
}
