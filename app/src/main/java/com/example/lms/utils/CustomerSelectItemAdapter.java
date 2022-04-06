package com.example.lms.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.lms.R;
import com.example.lms.models.Customer;

import java.util.List;

/**
 * Created by admin on 10/30/2014.
 */
public class CustomerSelectItemAdapter extends ArrayAdapter {
    private List<Customer> customer;
    private int textViewResourceId;
    private Class navigation;
    private Boolean isOrderSearch;
    Button btnView;
    TextView txtName,txtArea,txtAdd1,txtAdd2,txtMobile;
    public CustomerSelectItemAdapter(Context context, int textViewResourceId, List<Customer> customer,Class<?> navigation,  Boolean isOrderSearch) {
        super(context, textViewResourceId, customer);
        this.customer = customer;
        this.textViewResourceId=textViewResourceId;
        this.navigation=navigation;
        this.isOrderSearch=isOrderSearch;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(textViewResourceId, null);
        }
        initControl(convertView);
        if(isOrderSearch){

            btnView.setBackgroundResource(R.drawable.icon_select);
        }else{
            btnView.setBackgroundResource(R.drawable.btn_buy);
        }

        final Customer customerDetails = customer.get(position);

        txtName.setText(customerDetails.getName());
        txtMobile.setText(customerDetails.getMobileNo());
        txtArea.setText(customerDetails.getName());

        txtAdd1.setText(customerDetails.getAddress1());

        txtAdd2.setText(customerDetails.getAddress2());

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), navigation);
                i.putExtra("CustomerId", customerDetails.getCustomerId());
                view.getContext().startActivity(i);
            }
        });
        return convertView;
    }
   private void initControl(View convertView){
       txtName = (TextView) convertView.findViewById(R.id.txt_csi_name);
       txtArea = (TextView) convertView.findViewById(R.id.txt_csi_area);
       txtAdd1 = (TextView) convertView.findViewById(R.id.txt_csi_address1);
       txtAdd2 = (TextView) convertView.findViewById(R.id.txt_csi_address2);
       btnView = (Button) convertView.findViewById(R.id.btn_csi_order);
       txtMobile = (TextView) convertView.findViewById(R.id.txt_csi_mobile);
    }
}