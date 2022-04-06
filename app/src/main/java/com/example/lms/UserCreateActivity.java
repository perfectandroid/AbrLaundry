package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.lms.models.User;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 10/25/2014.
 */
public class UserCreateActivity extends Activity {
    DatabaseHandler db = new DatabaseHandler(this);
    AppConfig config= new AppConfig(this);
    List<EditText> edtList = new ArrayList<EditText>();
    User userDetails = new User();
    Button btnCreate;
    EditText edtName;
    EditText edtPin;
    EditText edtUserCode;
    TextView txtError;
    TextView txtTitle;
    String toastMessage;
    private Switch switchBillCancellation;
    Boolean isUserExist=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_add);
        initControls();
        initEvents();
        Intent intent = getIntent();
        Long userId = intent.getLongExtra("user_id",0);
        userDetails=db.getUser(userId);
        if(userDetails.getName()!=null){
            isUserExist=true;
            txtTitle.setText("User Details");
            edtName.setText(userDetails.getName());
            edtPin.setText(userDetails.getPinNo());
            edtUserCode.setText(userDetails.getUserCode());
            switchBillCancellation.setChecked(userDetails.getBillCancellation());
            btnCreate.setText("Update");
        }else{
            txtTitle.setText("New User");
            isUserExist=false;
        }
    }

    public void initControls() {
        btnCreate = (Button) findViewById(R.id.btn_ua_register);
        edtName = (EditText) findViewById(R.id.edt_ua_name);
        edtPin = (EditText) findViewById(R.id.edt_ua_pincode);
        edtUserCode = (EditText) findViewById(R.id.edt_ua_user_code);
        txtError = (TextView) findViewById(R.id.txt_ua_error);
        txtTitle = (TextView) findViewById(R.id.txt_ua_head);
        switchBillCancellation = (Switch) findViewById(R.id.switch_ua_bill_cancalation);
    }

    public void initEvents() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDb();
            }
        });
        findViewById(R.id.btn_ua_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserCreateActivity.this, UserListActivity.class));
            }
        });
    }

    private void saveToDb() {

        edtList.add(edtName);
        edtList.add(edtUserCode);
        edtList.add(edtPin);
        Long userId=null;
        if (AppConfig.validateNullFields(edtList)) {
            userDetails.setName(edtName.getText().toString());
            userDetails.setPinNo(edtPin.getText().toString());
            userDetails.setUserCode(edtUserCode.getText().toString());
            userDetails.setUserPower("user");
            userDetails.setBillCancellation(switchBillCancellation.isChecked());
            if(isUserExist){
                userId= db.updateUser(userDetails);
                toastMessage="updated";
            }else {
                userId= db.addUser(userDetails);
                toastMessage="created";
            }
            config.setUsername(userDetails.getName());
            if(userDetails.getId()!=null) {
                config.setUserId(userDetails.getId());
            }
            if(userId!=null) {
                if(userId>0) {
                    Toast.makeText(this, "User " + userDetails.getName().toString() + " " + toastMessage, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, UserListActivity.class);
                    startActivity(i);
                }
            }

        } else {
            txtError.setText("Please fill all required fields");
        }
    }


}