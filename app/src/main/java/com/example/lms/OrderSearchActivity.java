package com.example.lms;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderSearchActivity extends Activity implements View.OnClickListener {

    private Button btnViewOrder, btnSelectCus;
    private DatabaseHandler db=new DatabaseHandler(this);
    private AppConfig config=new AppConfig(this);
    private Intent intent;
    EditText edtFromDate, edtToDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private SimpleDateFormat dateFormatter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.order_search);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        initControls();
        initEvents();
    }

    public void initControls() {
        btnViewOrder = (Button) findViewById(R.id.btn_osr_search);
        btnSelectCus = (Button) findViewById(R.id.btn_osr_choose);
        edtFromDate = (EditText) findViewById(R.id.edt_osr_from);
        edtToDate = (EditText) findViewById(R.id.edt_osr_to);
    }

    public void initEvents() {
        edtFromDate.setText(config.getCurrentDateString());
        edtToDate.setText(config.getCurrentDateString());
        btnViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(AppConfig.validateNullFields(edtFromDate,"Please select from date",OrderSearchActivity.this)
                    && AppConfig.validateNullFields(edtToDate,"Please select to date",OrderSearchActivity.this)){

                    intent = new Intent(OrderSearchActivity.this, OrderListActivity.class);
                    intent.putExtra("from_date"," ");
                    intent.putExtra("to_date"," no date");
                    intent.putExtra("from_date",edtFromDate.getText().toString());
                    intent.putExtra("to_date",edtToDate.getText().toString());
                    startActivity(intent);
                }
            }
        });

        btnSelectCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(OrderSearchActivity.this, CustomerSelectActivity.class);
                intent.putExtra("is_order_Search",true);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_osr_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderSearchActivity.this, HomeScreenActivity.class));
            }
        });
        edtToDate.setOnClickListener(this);
        edtFromDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_osr_to:
                showDatePicker(edtToDate);
                break;
            case R.id.edt_osr_from:
                showDatePicker(edtFromDate);
                break;
        }

    }

    private void showDatePicker(final EditText control) {
        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(OrderSearchActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        control.setText(dateFormatter.format(newDate.getTime()));
/*
                        control.setText(year + "-"+(monthOfYear + 1) + "-"
                                + dayOfMonth);*/

                    }
                }, mYear, mMonth, mDay);
        dpd.show();

    }
}