package com.example.lms.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.lms.R;
import com.example.lms.models.SalesDetails;

import java.util.List;

public class OrderDetailsListAdapter extends ArrayAdapter {

    private AppConfig appConfig;

    public OrderDetailsListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    private List<SalesDetails> items;
    private Context context;

    public OrderDetailsListAdapter(Context context, int resource, List<SalesDetails> items) {

        super(context, resource, items);
        this.items = items;
        this.context = context;
        appConfig = new AppConfig(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.order_details_item, null);
        }
        SalesDetails salesDetails = items.get(position);
        if (salesDetails != null) {
            TextView txtName = (TextView) v.findViewById(R.id.txt_odi_name);
            txtName.setText(salesDetails.getCustomerName());

            TextView txtBillNo = (TextView) v.findViewById(R.id.txt_odi_billNo);
            txtBillNo.setText(salesDetails.getBilNo());
            TextView txtDate = (TextView) v.findViewById(R.id.txt_odi_Date);
            txtDate.setText(salesDetails.getBilDate());
            TextView txtItems = (TextView) v.findViewById(R.id.txt_odi_items);
            txtItems.setText(salesDetails.getItem());
            TextView txtTotal = (TextView) v.findViewById(R.id.txt_odi_total);
            txtTotal.setText(salesDetails.getTotal().toString());
        }

        return v;

    }
}