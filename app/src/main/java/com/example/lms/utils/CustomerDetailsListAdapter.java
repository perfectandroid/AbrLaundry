package com.example.lms.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.lms.OrderCartActivity;
import com.example.lms.R;
import com.example.lms.models.Customer;

import java.util.List;

public class CustomerDetailsListAdapter extends ArrayAdapter {

    private AppConfig appConfig;

    public CustomerDetailsListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    private List<Customer> items;
    private Context context;

    public CustomerDetailsListAdapter(Context context, int resource, List<Customer> items) {

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
            v = vi.inflate(R.layout.customer_details_item, null);
        }
        Customer custDetails = items.get(position);
        if (custDetails != null) {

            TextView txtName = (TextView) v.findViewById(R.id.txt_cdi_name);
            txtName.setText(custDetails.getName());
            TextView txtAdd1 = (TextView) v.findViewById(R.id.txt_cdi_address1);
            txtAdd1.setText(custDetails.getAddress1());
            TextView txtAdd2 = (TextView) v.findViewById(R.id.txt_cdi_address2);
            txtAdd2.setText(custDetails.getAddress2());
            Button btnView = (Button) v.findViewById(R.id.btn_cdi_view);
            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(getContext(), OrderCartActivity.class);
                    view.getContext().startActivity(i);
                }
            });
        }

        return v;

    }
}