package com.example.sops.views.company;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.product.Product;
import com.example.sops.data.persistence.entities.productPicture.ProductPicture;
import com.example.sops.data.web.api.SopsApi;
import com.example.sops.data.web.api.SopsApiManager;
import com.example.sops.utils.AppUtils;
import com.example.sops.utils.OnInserted;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyProfileNewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyProfileNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyProfileNewFragment extends Fragment
{
    private static final int CAMERA_REQUEST = 1888;
    private Application mApplication;
    private Activity mActivity;

    private CompanyProductNewViewModel mViewModel;
    private ImageView mProductImageView;
    private EditText mProductNameEdit;
    private EditText mProductExpirationEdit;
    private EditText mProductCountryEdit;
    private EditText mProductPriceEdit;
    private Button mTakePhotoButton;
    private Button mSaveProductButton;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public CompanyProfileNewFragment()
    {
    }

    public static CompanyProfileNewFragment newInstance(String param1, String param2)
    {
        CompanyProfileNewFragment fragment = new CompanyProfileNewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mApplication = getActivity().getApplication();
        mActivity = getActivity();

        mViewModel = new CompanyProductNewViewModel(mApplication);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_company_profile_new,
                container, false);

        mProductImageView = root.findViewById(R.id.company_new_image_view);
        mProductNameEdit = root.findViewById(R.id.company_new_product_name_edit);
        mProductExpirationEdit = root.findViewById(R.id.company_new_product_expiration_edit);
        mProductCountryEdit = root.findViewById(R.id.company_new_product_country_edit);
        mProductPriceEdit = root.findViewById(R.id.company_new_product_price_edit);
        mTakePhotoButton = root.findViewById(R.id.company_new_take_photo_button);
        mSaveProductButton = root.findViewById(R.id.company_new_product_save);

        mTakePhotoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
            }
        });
        mSaveProductButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Product product = new Product(
                        0,
                        mProductNameEdit.getText().toString(),
                        "123",
                        "desc",
                        AppUtils.getCurrentUserCompanyId(mActivity),
                        mProductCountryEdit.getText().toString(),
                        new Date(21),
                        Integer.parseInt(mProductExpirationEdit.getText().toString()),
                        Float.parseFloat(mProductPriceEdit.getText().toString())
                );
                mViewModel.insertProduct(product, new OnInserted()
                {
                    @Override
                    public void call(Object insertedObject)
                    {
                        Product insertedProduct = (Product) insertedObject;
                        byte[] fileContent = null;
                        try
                        {
                            OutputStream os = new ByteArrayOutputStream();
                            // File imageFile = new File(mCurrentPhotoPath);
                            // fileContent = Files.readAllBytes(imageFile.toPath());
                            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                                    bitmap, 256, 256, false);
                            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

                            fileContent = ((ByteArrayOutputStream) os).toByteArray();
                        }
                        catch (Exception e)
                        {
                            Log.d("my", "couldn't transform file to byte array ");
                        }

                        mViewModel.insertProductPicture(new ProductPicture(
                                insertedProduct.getId(),
                                fileContent
                        ));

                        AppUtils.showSnackbar(mActivity, R.id.company_new_layout,
                                R.string.company_new_product_added);
                    }
                });
            }
        });

        return root;
    }

    // Product image
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = this.getActivity().getApplication()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,   /* prefix */
                ".png",    /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex)
            {
                Log.d("my", "error occurred while creating product image file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(
                        this.getActivity(),
                        "com.example.sops.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            try
            {
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                FileOutputStream fos = new FileOutputStream(mCurrentPhotoPath);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                        bitmap, 1250, 1250, false);
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }
            catch (Exception e)
            {
                Log.d("my", "image resize problem");
            }
            File imageFile = new File(mCurrentPhotoPath);

            mProductImageView.setImageURI(FileProvider.getUriForFile(
                    this.getActivity(),
                    "com.example.sops.fileprovider",
                    imageFile));
        }
    }

    // Some code
    // TODO: Rename method, update argument and hook method into UI event
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
    }
}
