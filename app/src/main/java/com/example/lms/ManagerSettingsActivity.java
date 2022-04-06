package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.lms.models.Price;
import com.example.lms.models.Settings;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;

import java.util.List;

/**
 * Created by admin on 10/27/2014.
 */
public class ManagerSettingsActivity extends Activity {
    private DatabaseHandler db = new DatabaseHandler(this);
    private AppConfig config;
    private Settings settings = new Settings();
    private EditText edtRoute, edtNo_Bill, edtRouteName, startBillNum,edtcomp,edtcomadd,edtAddress1,edtAddress2;

    private Switch switchSettlement, switchEnableDiscount;

    private Spinner spnPriceChange;
    private Boolean isUpdate = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_settings);
        initControls();
        initEvents();

        /*ArrayAdapter*/
        ArrayAdapter<CharSequence> priceChangeAdapter = ArrayAdapter.createFromResource(this, R.array.price_change_list, android.R.layout.simple_list_item_1);
        /*Assign Objects for controls*/

        spnPriceChange.setAdapter(priceChangeAdapter);
        setSettingValues();


    }

    public void setSettingValues() {
        Settings settingsNew = db.getSettings();
        Button btnSave = (Button) findViewById(R.id.btn_st_save);

        if (settingsNew != null) {
            if (settingsNew.getId() != null) {
                settings.setId(settingsNew.getId());
                isUpdate = true;
                btnSave.setText("Update");
                if (settingsNew.getRouteCode() != null) {
                    edtRoute.setText(settingsNew.getRouteCode().toString());
                }

                if (settingsNew.getRouteName() != null) {
                    edtRouteName.setText(settingsNew.getRouteName());
                }


                if (settingsNew.getPrinterPageSize() != null) {
                    startBillNum.setText(settingsNew.getPrinterPageSize().toString());
                }
                if (settingsNew.getNoOfBillCopies() != null) {
                    edtNo_Bill.setText(settingsNew.getNoOfBillCopies().toString());
                }
                if (settingsNew.getEnableSettlement() != null) {
                    switchSettlement.setChecked(settingsNew.getEnableSettlement());
                }

                if (settingsNew.getEnableDiscount() != null) {
                    switchEnableDiscount.setChecked(settingsNew.getEnableDiscount());
                }
                if(settingsNew.getCompanyname() != null)
                {
                    edtcomp.setText(settingsNew.getCompanyname());
                }
                if(settingsNew.getCompanyaddress() !=null)
                {
                    edtcomadd.setText(settingsNew.getCompanyaddress());
                }
                if(settingsNew.getAddress1() !=null)
                {
                    edtAddress1.setText(settingsNew.getAddress1());
                }
                if(settingsNew.getAddress2() !=null)
                {
                    edtAddress2.setText(settingsNew.getAddress2());
                }
                if (settingsNew.getPriceChange() != null) {
                    int pos = 0;
                    if (settingsNew.getPriceChange() != null && settingsNew.getPriceChange() > 0) {
                        long posi = settingsNew.getPriceChange();
                        pos = (int) posi;
                    }
                    spnPriceChange.setSelection(pos);
                }
            }
        }

    }

    public void initControls() {
        final Spinner spPriceChange = (Spinner) findViewById(R.id.sp_st_price_change);
        edtRoute = (EditText) findViewById(R.id.edt_st_route);
        edtRouteName = (EditText) findViewById(R.id.edt_st_route_name);
        edtNo_Bill = (EditText) findViewById(R.id.edt_st_bill_count);
        edtcomp=(EditText)findViewById(R.id.edt_company_name);
        edtcomadd=(EditText)findViewById(R.id.edt_company_address);
        edtAddress1=(EditText)findViewById(R.id.edt_address1);
        edtAddress2=(EditText)findViewById(R.id.edt_address2);
        switchSettlement = (Switch) findViewById(R.id.switch_st_settlement);

        spnPriceChange = (Spinner) findViewById(R.id.sp_st_price_change);
        switchEnableDiscount = (Switch) findViewById(R.id.switch_st_discount);
        startBillNum = (EditText) findViewById(R.id.edt_st_page_size);//Start Bill Number
    }

    public void initEvents() {
        findViewById(R.id.btn_st_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConfig.validateNullFields(edtRoute, "Please provide route code", ManagerSettingsActivity.this)
                        && AppConfig.validateNullFields(edtRouteName, "Please provide route Name", ManagerSettingsActivity.this)
                        && AppConfig.validateNullFields(edtcomp, "Please provide Company Name", ManagerSettingsActivity.this)
                        && AppConfig.validateNullFields(edtcomadd, "Please provide Company Address", ManagerSettingsActivity.this)
                        && AppConfig.validateNullFields(edtAddress1, "Please provide Address1", ManagerSettingsActivity.this)
                        && AppConfig.validateNullFields(edtAddress2, "Please provide Address2", ManagerSettingsActivity.this)
                        && AppConfig.validateNullFields(edtNo_Bill, "2", "", ManagerSettingsActivity.this)
                        ) {
                    settings.setRouteCode(Long.parseLong(edtRoute.getText().toString()));
                    settings.setRouteName(edtRouteName.getText().toString());
                    settings.setPrinterPageSize(Long.parseLong(startBillNum.getText().toString().trim() == ""? "1":startBillNum.getText().toString().trim()));
                    settings.setNoOfBillCopies(Long.parseLong(edtNo_Bill.getText().toString()));
                    settings.setEnableSettlement(switchSettlement.isChecked());
                    settings.setPriceChange(spnPriceChange.getSelectedItemId());
                    settings.setEnableDiscount(switchEnableDiscount.isChecked());
                    settings.setCompanyname(edtcomp.getText().toString());
                    settings.setCompanyaddress(edtcomadd.getText().toString());
                    settings.setAddress1(edtAddress1.getText().toString());
                    settings.setAddress2(edtAddress2.getText().toString());
                    Long status = 0l;
                    if (isUpdate) {
                        status = db.updateSettings(settings);
                    } else {
                        status = db.addSettings(settings);
                    }
                    if (status > 0) {
                        Toast.makeText(ManagerSettingsActivity.this, "Application Settings updated", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ManagerSettingsActivity.this, ManagerHomeActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(ManagerSettingsActivity.this, "Failed to update", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        findViewById(R.id.btn_st_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerSettingsActivity.this, ManagerHomeActivity.class));
                List<Settings> settings = db.getAllSettings();
            }
        });


    }

    private void fillPrice() {


        String[] serviceListArray = getResources().getStringArray(R.array.service_type_list);
        String[] itemListArray = getResources().getStringArray(R.array.item_list);
        for (int j = 1; j < itemListArray.length; j++)
            for (int i = 1; i < serviceListArray.length; i++) {
                Price price = new Price();
                price.setItemId(Long.valueOf(j));
                price.setPrice(i * 100.0);
                price.setServiceId(Long.valueOf(i));
                price.setServiceName(serviceListArray[i]);
                price.setItemName(itemListArray[j]);
                price.setFavourite("");
                db.addPrice(price);
            }
    }
}