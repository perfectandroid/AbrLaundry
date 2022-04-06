package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.example.lms.models.*;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;
import com.example.lms.utils.OrderDetailsParcel;

/**
 * Created by admin on 10/21/2014.
 */
public class OrderSubmitActivity extends Activity {

    Switch switchPriority;
    EditText edtExtraCharge, edtDiscount,edtRemarks;
    TextView txtTotal,txtNetAmount, mTxtVatAmount, mTxtNetAmount;
    Button btnResetAmount;
    CheckBox checkSettle;
    Spinner SpinnerPaymentStatus;
    long customerId;
    double netAmt = 0;
    Long orderId=0l;

    Order order = new Order();
    DatabaseHandler db = new DatabaseHandler(this);
    private OrderDetailsParcel orderDetails;
    private Customer customer = new Customer();
    private Settings settings = null;
    AppConfig config = new AppConfig(this);
    Double fixedGrandTotal=0.0;
    private Double totalAmount=0.0,discount=0.0,extraCharge=0.0;
    private static int MAXIMUM_CHAR_PER_LINE=46;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_submit);
        Bundle b = getIntent().getExtras(); //Get the intent's extras

        orderDetails = b.getParcelable("OrderDetailsList");
        totalAmount=getTotalPrice(orderDetails);

        customerId = getIntent().getLongExtra("CustomerId", 0);


        initControls();
        initEvents();
        setSpinnerItems();
        controlsBySettings();

    }
    /*controls according to settings*/
    private void controlsBySettings() {
        settings = db.getSettings();
        if (settings != null && settings.getEnableDiscount() != null) {
           /* if (settings.getEnableDiscount()) {
                findViewById(R.id.tr_os_discount).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.tr_os_discount).setVisibility(View.INVISIBLE);
            }*/
            if (settings.getEnableSettlement()) {

                findViewById(R.id.tr_os_settlement).setVisibility(View.VISIBLE);
                findViewById(R.id.lnr_os_payment_status).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.lnr_os_payment_status).setVisibility(View.INVISIBLE);

                findViewById(R.id.tr_os_settlement).setVisibility(View.INVISIBLE);
            }
        } else {
            config.showAlert("Application settings are not configured. Please contact administrator", "Logout", MainActivity.class);
        }


    }

    public void initControls() {
         /*create objects of controls*/
        switchPriority = (Switch) findViewById(R.id.switch_os_priority);
        edtExtraCharge = (EditText) findViewById(R.id.edt_os_extra_charge);
        checkSettle = (CheckBox) findViewById(R.id.chk_os_settlement);
        edtDiscount = (EditText) findViewById(R.id.edt_os_discount);
        edtRemarks=(EditText) findViewById(R.id.edt_os_remark);
        txtTotal=(TextView) findViewById(R.id.txt_os_total);
        txtNetAmount=(TextView) findViewById(R.id.txt_os_net_amount);
        SpinnerPaymentStatus=(Spinner)findViewById(R.id.spinner_os_status );
        btnResetAmount=(Button)findViewById(R.id.btn_os_net_reeset_amount );
        mTxtVatAmount = (TextView)findViewById( R.id.txt_os_vat_amount );
        mTxtNetAmount = (TextView) findViewById( R.id.txt_os_total_vat_included_net_amount );
        /*set events to control*/
    }

    public void initEvents() {
        txtNetAmount.setText(String.format("%.2f", totalAmount));
        findViewById(R.id.btn_os_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
               // startActivity(new Intent(OrderSubmitActivity.this, OrderCartActivity.class));
            }
        });
        findViewById(R.id.btn_os_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderSubmitActivity.this, OrderCustomerActivity.class));
            }
        });
        switchPriority.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean chosenPriority) {
                if (chosenPriority) {
                    findViewById(R.id.tr_os_extra_charge).setVisibility(View.VISIBLE);
                    //Toast.makeText(compoundButton.getContext(), "you chosen urgent mode", Toast.LENGTH_SHORT).show();
                } else {
                    edtExtraCharge.setText("");
                    txtNetAmount.setText(String.format("%.2f",getNetAmount()));
                    fixedGrandTotal=getNetAmount();
                    findViewById(R.id.tr_os_extra_charge).setVisibility(View.INVISIBLE);
                    getVatAmount();
                    //Toast.makeText(compoundButton.getContext(), "you chosen normal mode", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*save to database*/
        findViewById(R.id.btn_os_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkSettings()){
                    fillOrderData();
                }


            }
        });
        findViewById(R.id.btn_os_save_print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkSettings()){
                    fillOrderData();
                    if(orderId>0) {
                        Intent i = new Intent(OrderSubmitActivity.this, PrintBillActivity.class);
                        i.putExtra("printMessage", config.getBillFormat(orderId));
                        i.putExtra("orderId", orderId);
                        startActivity(i);
                    }
                }


            }
        });

        txtTotal.setText(String.format("%.2f",totalAmount));
        txtNetAmount.setText(String.format("%.2f",getNetAmount()));
        fixedGrandTotal= getNetAmount();
        getVatAmount();
        edtDiscount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                    txtNetAmount.setText(String.format("%.2f",getNetAmount()));

                    fixedGrandTotal=getNetAmount();
                getVatAmount();

            }
        });
       /* edtDiscount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                txtNetAmount.setText(getNetAmount().toString());

                fixedGrandTotal=getNetAmount();
                return false;
            }
        });*/
        /*edtExtraCharge.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                txtNetAmount.setText(getNetAmount().toString());

                fixedGrandTotal=getNetAmount();
                return false;
            }
        });*/
        edtExtraCharge.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                    txtNetAmount.setText(String.format("%.2f",getNetAmount()));

                    fixedGrandTotal=getNetAmount();
                getVatAmount();

            }
        });
        checkSettle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    int rowUpdated= db.updateOrderStatus(orderId,2l);
                    if(rowUpdated>0){
                        Toast.makeText(OrderSubmitActivity.this,"This order completed",Toast.LENGTH_LONG).show();
                    }else if(rowUpdated<0){
                        Toast.makeText(OrderSubmitActivity.this,"internal problem",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnResetAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNetAmount.setText(String.format("%.2f",fixedGrandTotal));
            }
        });
    }
    private Boolean checkSettings(){
        Boolean flag=true;


        if(switchPriority.isChecked()) {
            flag=  validateNullFields(edtExtraCharge, "0", "Please provide extra charge");
        }
        if (findViewById(R.id.tr_os_discount).getVisibility()==View.VISIBLE){


                if (discount > totalAmount) {
                    Toast.makeText(this, "Discount is greater than actual amount", Toast.LENGTH_SHORT).show();
                    flag= false;
                } else {
                    flag = true;
                }
        }
        return flag;
    }
    private boolean validateNullFields(EditText textField, String defaultValue, String errorMessage) {
        Boolean flag=false;
        if (textField.getText().toString().trim().length() > 0) {
            flag= true;
        } else if(textField.getText().toString().equals("0")||textField.getText().toString().trim().equals("")){

                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                textField.requestFocus();

        }
            return flag;

    }

    private void fillOrderData() {
        Double grandTotal=config.parseDouble(txtNetAmount.getText().toString());

            order.setOrderDate(config.getCurrentDateString());
            order.setOrderTime(config.getCurrentTime());
            customer.setCustomerId(customerId);
            order.setCustomer(customer);
            order.setTotalAmount(grandTotal);
            order.setNetVatAmt(netAmt);
            Long extraChargeValue =  Math.round(totalAmount*(config.parseDouble(edtExtraCharge.getText().toString() )/100.0f));
            order.setAdditionalAmount(extraChargeValue);
            order.setNetVatAmt(netAmt);
            if (checkSettle.isChecked()) {
                order.setOrderStatus(3l);
            } else {
                order.setOrderStatus(SpinnerPaymentStatus.getSelectedItemId());
            }
            order.setExpressStatus(getExpressStatus());
            order.setRemarks(edtRemarks.getText().toString());
            order.setDiscount(config.parseDouble(edtDiscount.getText().toString()));
            saveOrder();

    }
    private void saveOrder(){
        if(config.getUserId() != null)
        {
            User user = db.getUser(config.getUserId());
            order.setUser(user);
        }
        orderId = db.addOrder(order);
        for (OrderDetails item : orderDetails) {
            item.setOrderId(orderId);
            item.setUser(order.getUser());
            item.setPrice(item.getNetVatAmt());
            item.setUnitPrice(item.getUnitPrice());
            db.addOrderDetails(item);
        }
        Toast.makeText(this,"Your order has been saved.",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,HomeScreenActivity.class));
    }

    private String getExpressStatus() {

        return String.valueOf(switchPriority.isChecked());
    }
    private void setSpinnerItems() {
          /*Configure adapter for settlement status spinner*/
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(OrderSubmitActivity.this, R.array.settlement_status, android.R.layout.simple_list_item_1);
        SpinnerPaymentStatus.setAdapter(arrayAdapter);
    }


    /*Total amount of cart item*/
    private Double getTotalPrice(OrderDetailsParcel orderDetails) {
        Double totalPrice=0.0;
        for(OrderDetails item:orderDetails){
            totalPrice+=item.getPrice();
        }
        return totalPrice;
    }
        /*final amount of  this service which customer should pay*/
    private Double getNetAmount() {
        /*Double netTotal=0.0 ;
        Long extraChargeValue =  Math.round(totalAmount*(config.parseDouble(edtExtraCharge.getText().toString() )/100.0f));

        netTotal = (totalAmount + extraChargeValue) - config.parseDouble(edtDiscount.getText().toString());

        txtNetAmount.setText(String.format("%.2f",netTotal));
        fixedGrandTotal=netTotal;


         netTotal = ((totalAmount*(config.parseDouble(edtExtraCharge.getText().toString() )/100.0f))) - config.parseDouble(edtDiscount.getText().toString())+ totalAmount;

        netTotal= Double.valueOf(netTotal);
        getVatAmount();
        return netTotal;*/

        Double netTotal=0.0 ;

        Double extraChargeValue =  totalAmount*(config.parseDouble(edtExtraCharge.getText().toString() )/100.0f);

        netTotal = (totalAmount + extraChargeValue) - config.parseDouble(edtDiscount.getText().toString());

        txtNetAmount.setText(String.format("%.2f",netTotal));
        fixedGrandTotal=netTotal;


        netTotal = ((totalAmount*(config.parseDouble(edtExtraCharge.getText().toString() )/100.0f))) - config.parseDouble(edtDiscount.getText().toString())+ totalAmount;

        netTotal= Double.valueOf(netTotal);
        getVatAmount();
        return netTotal;
    }
    private void getVatAmount(){

        Double netVat = fixedGrandTotal*(config.parseDouble( Float.toString(LmsConstants.vatPercentage) )/100.0f);
        mTxtVatAmount.setText( String.format("%.2f",netVat) );
        netAmt = Math.round(netVat + fixedGrandTotal);
        mTxtNetAmount.setText( String.format("%.2f", netAmt ));

    }
    public  boolean validateNullFields(EditText textField) {
        if (textField.getText().toString().trim().length() > 0) {
            return true;
        }else{
            textField.requestFocus();

            return false;
        }
    }

}