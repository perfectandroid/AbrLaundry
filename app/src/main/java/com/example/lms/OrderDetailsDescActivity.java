package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.lms.models.Customer;
import com.example.lms.models.Order;
import com.example.lms.models.OrderDetails;
import com.example.lms.models.Settings;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;
import com.example.lms.utils.OrderDetailsDescAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 10/21/2014.
 */
public class     OrderDetailsDescActivity extends Activity {
    EditText txtOrderNo,txtDateTime,txtCustomer,txtTotal,txtAdditional,txtPriority,txtPaymentStatus,txtDiscount, mEdtTxtNetAmount;
    ListView list;
    OrderDetailsDescAdapter adapter;
    Spinner spinnerOrderStatus;
    private Settings settings = null;
    Order order=new Order();
    Long orderId=0l;
    Button btnCancelButton,btnPrint;
    DatabaseHandler db=new DatabaseHandler(this);
    AppConfig config=new AppConfig(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_desc);

        controlsBySettings();

        initControls();

        initEvents();

        setSpinnerItems();
        Intent i=getIntent();
        orderId=i.getLongExtra("order_id", 0l);
        order=db.getOrder(orderId);
        hideControls();

        fillOrderData(order);

    }

    private void hideControls() {
        if(!order.getOrderStatus().equals(2l)&& config.getBillCancellation()){
            btnCancelButton.setVisibility(View.VISIBLE);
        }

        settings = db.getSettings();

        if (settings != null && settings.getEnableDiscount()!=null) {
        /*    if (settings.getEnableSettlement()) {
                btnCancelButton.setVisibility(View.VISIBLE);
            } else {
                btnCancelButton.setVisibility(View.INVISIBLE);
            }
*/
        } else {
            config.showAlert("Please contact administrator and add settings", "Logout", MainActivity.class);
        }
    }

    private void setSpinnerItems() {
          /*Configure adapter for settlement status spinner*/
          ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.settlement_status, android.R.layout.simple_list_item_1);
        spinnerOrderStatus.setAdapter(arrayAdapter);
    }

    private void fillOrderDetails(List<OrderDetails> orderDetailList) {
        adapter = new OrderDetailsDescAdapter(this, R.layout.order_details_desc_item, orderDetailList);
        list.setAdapter(adapter);
    }

    private void initControls() {
        spinnerOrderStatus = (Spinner) findViewById(R.id.sp_odd_change_status);
        list = (ListView) findViewById(R.id.list_odd_order_items);
        txtOrderNo = (EditText) findViewById(R.id.edt_odd_oder_no);

        txtDateTime = (EditText) findViewById(R.id.edt_odd_date);

        txtCustomer = (EditText) findViewById(R.id.edt_odd_customer);

        txtTotal = (EditText) findViewById(R.id.edt_odd_toatal);

        txtAdditional = (EditText) findViewById(R.id.edt_odd_additional);

        txtPriority = (EditText) findViewById(R.id.edt_odd_priority);
        txtDiscount = (EditText) findViewById(R.id.edt_odd_discount);
        txtPaymentStatus = (EditText) findViewById(R.id.edt_odd_pay_status);
        btnCancelButton =(Button)findViewById(R.id.btn_oc_cancel_order);
        btnPrint=(Button) findViewById(R.id.btn_odd_print);
        mEdtTxtNetAmount = (EditText ) findViewById( R.id.edt_odd_net_amount);
    }



    public void initEvents() {
        findViewById(R.id.btn_odd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        findViewById(R.id.btn_odd_change_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rowUpdated= db.updateOrderStatus(orderId,spinnerOrderStatus.getSelectedItemId());
                if(rowUpdated>0){
                    Toast.makeText(OrderDetailsDescActivity.this,"Order status updated",Toast.LENGTH_LONG).show();
                }else if(rowUpdated<0){
                    Toast.makeText(OrderDetailsDescActivity.this,"internal problem",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int rowUpdated= db.updateOrderStatus(orderId,2l);
                if(rowUpdated>0){
                    btnCancelButton.setVisibility(View.INVISIBLE);
                    Toast.makeText(OrderDetailsDescActivity.this,"This order canceled",Toast.LENGTH_LONG).show();
                }else if(rowUpdated<0){
                    Toast.makeText(OrderDetailsDescActivity.this,"internal problem",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(OrderDetailsDescActivity.this, PrintBillActivity.class);
                i.putExtra("printMessage",config.getBillFormat(orderId));
                i.putExtra("orderId",orderId);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    /*controls according to settings*/
    private void controlsBySettings() {
        settings = db.getSettings();
        if (settings != null && settings.getEnableSettlement() != null) {
            if (settings.getEnableSettlement()) {
                findViewById(R.id.lnr_odd_order_status).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.lnr_odd_order_status).setVisibility(View.INVISIBLE);
            }
        } else {
            config.showAlert("Application settings are not configured. Please contact administrator", "Logout", MainActivity.class);
        }


    }

    private void fillOrderData(Order order) {
            Customer customerDetails = order.getCustomer();

            txtOrderNo.setText(config.addOrderPrefix(order.getOrderId()));
            //String dateString=order.getOrderDate().toString();
            txtDateTime.setText(order.getOrderDate().toString());
            String customerAddress=customerDetails.getName() + "," + customerDetails.getAddress1();
            int CUSTOMER_DETAILS_LENGTH=20;
            if(customerAddress.length()>CUSTOMER_DETAILS_LENGTH)
                customerAddress= customerAddress.substring(0, customerAddress.length()- (customerAddress.length()-CUSTOMER_DETAILS_LENGTH))+"...";
            txtCustomer.setText(customerAddress);
            txtTotal.setText(String.format("%.2f",order.getTotalAmount()));
//
            txtAdditional.setText(order.getAdditionalAmount().toString() + ".00");
            if(Boolean.parseBoolean(order.getExpressStatus())) {
                txtPriority.setText("Express order");
            }else{
                txtPriority.setText("Normal order");
            }
            if(order.getOrderStatus() != null && order.getOrderStatus() <2 ) {
                spinnerOrderStatus.setSelection(order.getOrderStatus().intValue());
            }
            else {
                spinnerOrderStatus.setSelection(0);
            }
            int orderStatusId=Integer.parseInt(order.getOrderStatus().toString());


            txtPaymentStatus.setText(setPaymentStatus(orderStatusId));
             txtDiscount.setText(String.format("%.2f",order.getDiscount()));
             mEdtTxtNetAmount.setText(Double.toString(order.getNetVatAmt()));
            fillOrderDetails(order.getOrderDetails());
        }

    private String setPaymentStatus(int statusId) {
        String[] orderStatusArray=getResources().getStringArray(R.array.settlement_status);
        String status="";
            switch (statusId){
                case 2:
                    status="Order Canceled";
                    break;
                case 3:
                    status="Settled Order";
                    break;
                default:
                    status=orderStatusArray[statusId];
                    break;

            }
         return status;
    }



}
