package com.example.sops.views.products;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sops.R;
import com.example.sops.data.persistence.entities.company.Company;
import com.example.sops.data.web.api.SopsApi;

import java.util.ArrayList;

public class CompanyDetailsArrayAdapter extends ArrayAdapter<Company>
{
    private Context mContext;
    private ArrayList<Company> mCompanies;
    private ButtonClickedListener mOnButtonClicked;

    public CompanyDetailsArrayAdapter(Context context, ArrayList<Company> companies,
                                      ButtonClickedListener onButtonClicked)
    {
        super(context,0, companies);

        mContext = context;
        mCompanies = companies;
        mOnButtonClicked = onButtonClicked;
    }

    @Override
    public @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            convertView = inflater.inflate(R.layout.products_company_list_item,
                    parent, false);

            ViewGroup root = convertView.findViewById(R.id.list);
            View row0 = inflater.inflate(android.R.layout.simple_list_item_2, null);
            View row1 = inflater.inflate(android.R.layout.simple_list_item_2, null);
            View row2 = inflater.inflate(android.R.layout.simple_list_item_2, null);
            View row3 = inflater.inflate(android.R.layout.simple_list_item_2, null);
            View row4 = inflater.inflate(android.R.layout.simple_list_item_2, null);
            root.addView(row0, 0);
            root.addView(row1, 1);
            root.addView(row2, 2);
            root.addView(row3, 3);
            root.addView(row4, 4);

            View button = inflater.inflate(R.layout.products_view_company_button, null);
            root.addView(button, 5);
        }

        final Company currentCompany = mCompanies.get(position);

        ViewGroup root = convertView.findViewById(R.id.list);

        View row = root.getChildAt(0);
        ((TextView)row.findViewById(android.R.id.text1)).setText(mContext.getString(R.string.company_details_company_name_label));
        ((TextView)row.findViewById(android.R.id.text2)).setText(currentCompany.getName());
        row = root.getChildAt(1);
        ((TextView)row.findViewById(android.R.id.text1)).setText(mContext.getString(R.string.company_details_company_address_city_label));
        ((TextView)row.findViewById(android.R.id.text2)).setText(currentCompany.getAddressCity());
        row = root.getChildAt(2);
        ((TextView)row.findViewById(android.R.id.text1)).setText(mContext.getString(R.string.company_details_company_nip_label));
        ((TextView)row.findViewById(android.R.id.text2)).setText(currentCompany.getNip());
        row = root.getChildAt(3);
        ((TextView)row.findViewById(android.R.id.text1)).setText(mContext.getString(R.string.company_details_company_kind_label));
        ((TextView)row.findViewById(android.R.id.text2)).setText(currentCompany.getKind());
        row = root.getChildAt(4);
        ((TextView)row.findViewById(android.R.id.text1)).setText(mContext.getString(R.string.company_details_company_join_date_label));
        ((TextView)row.findViewById(android.R.id.text2)).setText(currentCompany.getJoinDate().toString());

        row = root.getChildAt(5);
        ((Button) row).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mOnButtonClicked.onButtonClicked(v, currentCompany.getId());
            }
        });

        return convertView;
    }

    public static interface ButtonClickedListener
    {
        public void onButtonClicked(View caller, int companyId);
    }

    public void changeDataSet(ArrayList<Company> companies)
    {
        mCompanies.clear();
        mCompanies.addAll(companies);
    }
}
