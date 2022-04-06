package com.example.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderClearActivity extends Activity implements View.OnClickListener {

    private Button btnClearOrder;
    private DatabaseHandler db = new DatabaseHandler(this);
    private AppConfig config = new AppConfig(this);
    private Intent intent;
    EditText edtFromDate, edtToDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private SimpleDateFormat dateFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.order_clear);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        initControls();
        initEvents();
    }

    public void initControls() {
        btnClearOrder = (Button) findViewById(R.id.btn_order_clear);
        edtFromDate = (EditText) findViewById(R.id.edt_osr_from);
        edtToDate = (EditText) findViewById(R.id.edt_osr_to);
    }

    public void initEvents() {
        edtFromDate.setText(config.getCurrentDateString());
        edtToDate.setText(config.getCurrentDateString());
        btnClearOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConfig.validateNullFields(edtFromDate, "Please select from date", OrderClearActivity.this)
                        && AppConfig.validateNullFields(edtToDate, "Please select to date", OrderClearActivity.this)) {
                    try {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderClearActivity.this);
                        builder1.setMessage("Do you want to delete orders ?");
                        builder1.setTitle(R.string.app_name);
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Integer count = db.clearOrderByDate(edtFromDate.getText().toString(), edtToDate.getText().toString());
                                        if (count > 0) {
                                            String msg = count + " Orders deleted successfully";
                                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "No Orders Found", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        builder1.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        findViewById(R.id.btn_osr_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderClearActivity.this, ManagerHomeActivity.class));
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
        DatePickerDialog dpd = new DatePickerDialog(OrderClearActivity.this,
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