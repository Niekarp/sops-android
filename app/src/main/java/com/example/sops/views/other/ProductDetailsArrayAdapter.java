package com.example.sops.views.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sops.R;
import com.example.sops.data.web.api.SopsApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductDetailsArrayAdapter extends ArrayAdapter<LabelValuePair>
{
    private Context mContext;
    private ArrayList<LabelValuePair> mLabelValuePairs;

    public ProductDetailsArrayAdapter(Context context, ArrayList<LabelValuePair> labelValuePairs)
    {
        super(context,0, labelValuePairs);

        mContext = context;
        mLabelValuePairs = labelValuePairs;
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        if (position == 0)
        {
            if (convertView == null)
            {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.single_image_view, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.single_image);

            String coverUrl = "http://" + SopsApi.IP + "/api/productpicture/"
                    + mLabelValuePairs.get(position).getValue();
            Picasso.get().load(coverUrl).placeholder(R.drawable.ic_insert_photo_24dp).fit()
                    .into(imageView);

            return convertView;
        }

        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext)
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        LabelValuePair currentPair = mLabelValuePairs.get(position);

        TextView label = convertView.findViewById(android.R.id.text1);
        label.setText(currentPair.getLabel());

        TextView value = convertView.findViewById(android.R.id.text2);
        value.setText(currentPair.getValue());

        return convertView;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == 0)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    public void changeDataSet(ArrayList<LabelValuePair> labelValuePairs)
    {
        mLabelValuePairs .clear();
        mLabelValuePairs.addAll(labelValuePairs);
    }
}
