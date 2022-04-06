package com.example.lms.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.lms.OrderDetailsDescActivity;
import com.example.lms.R;
import com.example.lms.models.Order;

import java.util.List;

/**
 * Created by admin on 10/29/2014.
 */
public class OrderListAdapter extends ArrayAdapter {
    List<Order> orderList;
    AppConfig config = new AppConfig(getContext());
    DatabaseHandler db = new DatabaseHandler(getContext());

    public OrderListAdapter(Context context, int textViewResourceId, List<Order> orderListModel) {
        super(context, textViewResourceId, orderListModel);
        this.orderList = orderListModel;
        db = new DatabaseHandler(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.order_list_item, null);
        }

        final Order orderListObj = orderList.get(position);
        if (orderListObj != null) {
            TextView txtCustomer = (TextView) v.findViewById(R.id.txt_oli_customer);
            if (orderListObj.getCustomer() != null) {
                txtCustomer.setText(orderListObj.getCustomer().getName().toString());
            }
            TextView txtDate = (TextView) v.findViewById(R.id.txt_oli_date);
            txtDate.setText(orderListObj.getOrderDate());
            TextView txtOrderNo = (TextView) v.findViewById(R.id.txt_oli_order_no);
            if (orderListObj.getOrderId() != null) {
                txtOrderNo.setText(orderListObj.getOrderNo().toString());
            }
            TextView txtStatus = (TextView) v.findViewById(R.id.txt_oli_status);
            if (orderListObj.getOrderStatus() != null) {
                txtStatus.setText(setPaymentStatus(parent, Integer.parseInt(orderListObj.getOrderStatus().toString())));
            }
            TextView txtTotal = (TextView) v.findViewById(R.id.txt_oli_total);
            txtTotal.setText(String.format("%.2f", orderListObj.getNetVatAmt()));
            v.findViewById(R.id.btn_oli_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), OrderDetailsDescActivity.class);
                    i.putExtra("order_id", orderListObj.getOrderId());
                    view.getContext().startActivity(i);
                }
            });
        }

        return v;
    }

    private String setPaymentStatus(ViewGroup parent, int statusId) {
        String[] orderStatusArray = parent.getResources().getStringArray(R.array.settlement_status);
        String status = "";
        switch (statusId) {
            case 2:
                status = "Order Canceled";
                break;
            case 3:
                status = "Settled Order";
                break;
            default:
                if (statusId < 2) {
                    status = orderStatusArray[statusId];
                } else {
                    status = "";
                }
                break;
        }
        return status;
    }
}