package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.lms.models.*;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class CustomerCreateActivity extends Activity {
    ResponseBase errorMessage;
    List<EditText> edtList = new ArrayList< EditText >();
    /*Create Controls Object*/
    EditText edtName, edtAddress1, edtAddress2, edtMobile;
    EditText edtTelephone;
    EditText edtEmail;
    TextView edtError;
    AutoCompleteTextView autoEdtArea;
    AutoCompleteTextView autoEdtBuilding;
    /*Create Model Objects*/
    DatabaseHandler db = new DatabaseHandler(this);
    Long SelectedAreaId;
    Long SelectedBuildingId;
    Area selectedArea;
    Building selectedBuilding;
    Customer customer = new Customer();
    List<Area> allArea = new ArrayList<Area>();
    List<Building> allBuilding = new ArrayList<Building>();

    AppConfig config = new AppConfig(this);

    /*Settings settings=db.getSettings();*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.customer_add);
        /*init Controls*/
        initControls();
        /* init Events*/
        initEvents();
        initAreaAdapter();
    }

    private Long getAreaId(String areaAtName) {
        Long areaId = 0l;
        for (int i = 0; i < allArea.size(); i++) {
            if (allArea.get(i).getAreaName().equals(areaAtName)) {
                areaId = allArea.get(i).getAreaId();
                break;
            }
        }
        return areaId;
    }

    private Long getBuildingId(String buildingAtName) {
        Long buildingId = 0l;
        for (int i = 0; i < allBuilding.size(); i++) {
            if (allBuilding.get(i).getBuildingName().equals(buildingAtName)) {
                buildingId = allBuilding.get(i).getBuildingId();
                break;
            }
        }
        return buildingId;
    }

    private void initAreaAdapter() {
        /*fill area names to spinner adapter*/
        allArea = db.getAllArea();
        String[] areaList = new String[allArea.size()];
        if (allArea.size() > 0) {
            for (int i = 0; i < allArea.size(); i++) {
                areaList[i] = allArea.get(i).getAreaName();
            }
        }

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, areaList);
        autoEdtArea.setAdapter(itemAdapter);
        autoEdtArea.setThreshold(1);
        autoEdtArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                SelectedAreaId = getAreaId(arg0.getItemAtPosition(arg2).toString());
                selectedArea = new Area();
                selectedArea.setAreaId(SelectedAreaId);
                selectedArea.setAreaName(arg0.getItemAtPosition(arg2).toString());
            }
        });

        allBuilding = db.getAllBuilding();
        String[] buildingList = new String[allBuilding.size()];
        if (allBuilding.size() > 0) {
            for (int i = 0; i < allBuilding.size(); i++) {
                buildingList[i] = allBuilding.get(i).getBuildingName();
            }
        }

        ArrayAdapter<String> itemAdapterB = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildingList);
        autoEdtBuilding.setAdapter(itemAdapterB);
        autoEdtBuilding.setThreshold(1);
        autoEdtBuilding.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                SelectedBuildingId = getBuildingId(arg0.getItemAtPosition(arg2).toString());
                selectedBuilding = new Building();
                selectedBuilding.setBuildingId(SelectedBuildingId);
                selectedBuilding.setBuildingName(arg0.getItemAtPosition(arg2).toString());
            }
        });
    }

    public void initControls() {
        edtName = (EditText) findViewById(R.id.edt_ca_name);
        edtAddress1 = (EditText) findViewById(R.id.edt_ca_address1);
        edtAddress2 = (EditText) findViewById(R.id.edt_ca_address2);
        edtMobile = (EditText) findViewById(R.id.edt_ca_mobile);
        edtTelephone = (EditText) findViewById(R.id.edt_ca_telephone);
        edtEmail = (EditText) findViewById(R.id.edt_ca_email);
        edtError = (TextView) findViewById(R.id.txt_ca_error);
        autoEdtArea = (AutoCompleteTextView) findViewById(R.id.auto_edt_area);
        autoEdtBuilding = (AutoCompleteTextView) findViewById(R.id.auto_edt_building);
    }

    public void initEvents() {
        findViewById(R.id.btn_ca_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerCreateActivity.this, HomeScreenActivity.class));
            }
        });
        findViewById(R.id.btn_ca_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtList.add(edtName);
                edtList.add(edtAddress1);
                edtList.add(edtAddress2);
                if (AppConfig.validateNullFields(edtList) && !autoEdtArea.getText().toString().isEmpty() && !autoEdtBuilding.getText().toString().isEmpty()) {
                    if (edtEmail.getText().toString().trim().length() > 0) {
                        if (config.emailValidator(edtEmail, edtEmail.getText().toString())) {
                            saveToDb();
                        } else {
                            edtError.setText( "Please provide a valid email" );
                        }
                    } else {
                        saveToDb();
                    }
                } else {
                    edtError.setText( "Please fill all the required fields" );
                }


            }
        });
    }

    private void saveToDb() {

        try {
        /*set customer details*/

            customer.setAddress1(edtAddress1.getText().toString());
            customer.setAddress2(edtAddress2.getText().toString());
            customer.setArea(selectedArea);
            customer.setName(edtName.getText().toString());
            customer.setBuilding(selectedBuilding);
            customer.setEmail(edtEmail.getText().toString());
            customer.setTelephoneNo(edtTelephone.getText().toString());
            customer.setMobileNo(edtMobile.getText().toString());
            if (config.getUserId() != null) {
                User user = db.getUser(config.getUserId());
                customer.setUser(user);
            }
            Long customerId = db.addCustomer(customer);
            if (customerId > 0) {
                Toast.makeText(this, "Customer added successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, OrderCartActivity.class);
                i.putExtra("CustomerId", customerId);
                startActivity(i);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}