package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderCustomerActivity extends Activity {
    private Button btnNewCustomer;
    private Button btnExistingCustomer;
    private Button btnBack;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.order_customer);
        initControls();
        initEvents();
    }

    public void initControls() {
        btnNewCustomer = (Button) findViewById(R.id.btn_ocust_new_cust);
        btnExistingCustomer = (Button) findViewById(R.id.btn_ocust_existing_cust);
        btnBack = (Button) findViewById(R.id.btn_ocust_back);
    }

    public void initEvents() {
        btnNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(OrderCustomerActivity.this, CustomerCreateActivity.class);
                startActivity(intent);
            }
        });

        btnExistingCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(OrderCustomerActivity.this, CustomerSelectActivity.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderCustomerActivity.this, OrderSearchActivity.class));
            }
        });
    }
}