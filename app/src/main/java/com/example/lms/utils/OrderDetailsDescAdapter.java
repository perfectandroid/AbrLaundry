package com.example.lms.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.lms.R;
import com.example.lms.models.OrderDetails;
import com.example.lms.models.Price;

import java.util.List;

/**
 * Created by admin on 10/29/2014.
 */
public class OrderDetailsDescAdapter extends ArrayAdapter {

    private List<OrderDetails> orderItems;
    DatabaseHandler db ;
    public OrderDetailsDescAdapter(Context context, int textViewResourceId, List<OrderDetails> orderModel) {

        super(context, textViewResourceId, orderModel);
        this.orderItems = orderModel;
        db = new DatabaseHandler(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.order_details_desc_item, null);

        }


        OrderDetails ODetails = orderItems.get(position);
        Price itemPrice = db.searchItemPrice(ODetails.getServiceId(), ODetails.getItemId());

        TextView txtItemName = (TextView) v.findViewById(R.id.txt_oddi_item);
        txtItemName.setText(itemPrice.getItemName());

        TextView txtQty = (TextView) v.findViewById(R.id.txt_oddi_qty);
        txtQty.setText(ODetails.getQty().toString());

        TextView txtServiceType = (TextView) v.findViewById(R.id.txt_oddi_service);
        txtServiceType.setText(itemPrice.getServiceName());

        TextView txtTotal = (TextView) v.findViewById(R.id.txt_oddi_remark);
        txtTotal.setText( String.format("%.2f",ODetails.getPrice()));
       /* TextView txtAdd1 = (TextView) v.findViewById(R.id.txt_cdi_address1);
        txtAdd1.setText(orderDetails.getOrder());
        TextView txtAdd2 = (TextView) v.findViewById(R.id.txt_cdi_address2);
        txtAdd2.setText(custDetails.getAddress2());*/
        return v;
    }
}