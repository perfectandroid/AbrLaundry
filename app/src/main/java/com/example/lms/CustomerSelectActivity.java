package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.lms.models.Customer;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.CustomerSelectItemAdapter;
import com.example.lms.utils.DatabaseHandler;

import java.util.List;

/**
 * Created by admin on 10/21/2014.
 */
public class CustomerSelectActivity extends Activity {
    ListView customerListView;
    EditText edtSearchBox;
    TextView txtErrorMessage;
    Button btnSearch;
    Boolean isOrderSearch;
    DatabaseHandler db = new DatabaseHandler(this);
    CustomerSelectItemAdapter adapter;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.customer_select);
        Intent i=getIntent();
        isOrderSearch=i.getBooleanExtra("is_order_Search",false);
        initControls();
        initEvents();
    }

    public void initControls() {
        edtSearchBox = (EditText) findViewById(R.id.edt_cs_name);
        customerListView = (ListView) findViewById(R.id.list_cs_result);
        txtErrorMessage = (TextView) findViewById(R.id.txt_cs_empty_message);
        btnSearch = (Button) findViewById(R.id.btn_cs_search);
    }

    public void initEvents() {

        findViewById(R.id.btn_cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerSelectActivity.this, HomeScreenActivity.class));

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConfig.validateNullFields(edtSearchBox, "Please provide any of the details", CustomerSelectActivity.this)) {
                    fillCustomerDetails(edtSearchBox.getText().toString());
                }
            }
        });
        edtSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtErrorMessage.setVisibility(View.GONE);
                customerListView.setVisibility(View.GONE);
                if(!edtSearchBox.getText().toString().trim().isEmpty()) {
                    fillCustomerDetails(edtSearchBox.getText().toString());
                }else{
                    customerListView.setVisibility(View.GONE);
                    txtErrorMessage.setVisibility(View.VISIBLE);

                }
            }
        });
       /* edtSearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                txtErrorMessage.setVisibility(View.GONE);
                customerListView.setVisibility(View.GONE);
                if(!edtSearchBox.getText().toString().trim().isEmpty()) {
                    fillCustomerDetails(edtSearchBox.getText().toString());
                }else{
                    customerListView.setVisibility(View.GONE);
                    txtErrorMessage.setVisibility(View.VISIBLE);

                }
                return false;
            }
        });*/
    }

    private void fillCustomerDetails(String searchKey) {
        List<Customer> customerList;
        customerList = db.searchCustomer(searchKey);
        if (customerList.isEmpty()) {
            customerListView.setVisibility(View.GONE);
            txtErrorMessage.setVisibility(View.VISIBLE);
        } else {
            txtErrorMessage.setVisibility(View.GONE);
            customerListView.setVisibility(View.VISIBLE);

            if(isOrderSearch) {
                adapter = new CustomerSelectItemAdapter(this, R.layout.customer_select_item, customerList, OrderListActivity.class,true);
                customerListView.setAdapter(adapter);
            }else{
                adapter = new CustomerSelectItemAdapter(this, R.layout.customer_select_item, customerList,OrderCartActivity.class,false);
                customerListView.setAdapter(adapter);
            }
        }

    }


}