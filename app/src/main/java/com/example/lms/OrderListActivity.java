package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.lms.models.Order;
import com.example.lms.models.OrderList;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;
import com.example.lms.utils.OrderListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 10/29/2014.
 */
public class OrderListActivity extends Activity {
    DatabaseHandler db = new DatabaseHandler(this);
    OrderListAdapter adapter;
    TextView txtErrorMessage;
    TextView txtTotal;
    String errorMessage;
    LinearLayout lnrTotal;
    private AppConfig config = new AppConfig(this);
    ListView orderList;
    Date fromDate;
    Date toDate;
    List<Order> orderListModel = new ArrayList<Order>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);
        initControls();
        initEvents();

        Intent intent = getIntent();
        String intent_from_date = intent.getStringExtra("from_date");
        String intent_to_date = intent.getStringExtra("to_date");
        Long customerId = intent.getLongExtra("CustomerId", 0l);
        if (intent_from_date != null && intent_to_date != null) {
            fromDate = config.convertToDate(intent_from_date);
            toDate = config.convertToDate(intent_to_date);
            errorMessage = "No order found in between " + intent_from_date + " to " + intent_to_date;
            orderListModel = db.getOrdersByDate(intent_from_date, intent_to_date);
        } else {
            orderListModel = db.searchOrderByCustomer(customerId);
        }
        if (orderListModel.isEmpty()) {
            txtErrorMessage.setText(errorMessage);
            orderList.setVisibility(View.GONE);
            txtErrorMessage.setVisibility(View.VISIBLE);
            txtErrorMessage.setText("No orders found");
            lnrTotal.setVisibility(View.GONE);
        } else {
            txtErrorMessage.setVisibility(View.GONE);
            orderList.setVisibility(View.VISIBLE);
            Double total = 0.0;
            for (int i = 0; i < orderListModel.size(); i++) {
                total = total + orderListModel.get(i).getNetVatAmt();
            }
            txtTotal.setText(String.format("%.2f", total));
            fillAdapter(orderListModel);
            lnrTotal.setVisibility(View.VISIBLE);
        }

    }

    private void initControls() {
        txtErrorMessage = (TextView) findViewById(R.id.txt_ol_empty_message);
        txtTotal = (TextView) findViewById(R.id.txt_ol_TotalAmt);
        orderList = (ListView) findViewById(R.id.list_ol_orders);
        lnrTotal = (LinearLayout) findViewById(R.id.linear_ol_total);
    }

    private void fillAdapter(List<Order> orderListModel) {

//        List<Order> orderListModelNew = orderListModel;
//        int count =0;
//        try {
//            for(Order order:orderListModel)
//            {
//               if(order.getCustomerId() != null)
//               {
//                   Customer customer = db.getCustomer(order.getCustomerId());
//                   orderListModelNew.get(count).setCustomer(customer);
//                   Toast.makeText(getApplicationContext(),customer.getName(),Toast.LENGTH_LONG).show();
//               }
//               count++;
//            }
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
//        }
        adapter = new OrderListAdapter(this, R.layout.order_list_item, orderListModel);
        orderList.setAdapter(adapter);
    }

    public void initEvents() {
        findViewById(R.id.btn_ol_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


}