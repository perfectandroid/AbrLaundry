package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.lms.models.Settings;
import com.example.lms.models.User;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;

public class MainActivity extends Activity {

    private EditText edtUserCode, edtPinCode;

    private Button btnLogin;
    private TextView txtErrorMessage;
    private String userCode, pinCode;
    DatabaseHandler db = new DatabaseHandler(this);
    AppConfig config = new AppConfig(this);
    User userModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (config.isLoggedIn()) {
            if (config.isManager()) {
                Intent i = new Intent(this, ManagerHomeActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this, HomeScreenActivity.class);
                startActivity(i);
            }
        }

        initControls();
        initEvents();
        db.isAdminUserTableExists();
    }

    public void initControls() {
        btnLogin = (Button) findViewById(R.id.btn_mn_login);
        edtUserCode = (EditText) findViewById(R.id.edt_mn_user_code);
        edtPinCode = (EditText) findViewById(R.id.edt_mn_pin_no);
        txtErrorMessage = (TextView) findViewById(R.id.txt_mn_error);
        txtErrorMessage.setVisibility(View.INVISIBLE);
    }

    public void initEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

     /*validateNullFields(EditText textField, String defaultValue, String errorMessage, Context pContext)*/
                if (AppConfig.validateNullFields(edtUserCode, null, "Please provide user code", MainActivity.this)

                        && AppConfig.validateNullFields(edtPinCode, null, "Please provide Pin code", MainActivity.this)
                        ) {

                    userCode = edtUserCode.getText().toString().trim();
                    pinCode = edtPinCode.getText().toString().trim();
                    Settings settings = db.getSettings();
                    userModel = db.validateUser(userCode, pinCode);
                    if (userModel.getPinNo() != null) {
                        config.setUsername(userModel.getName());
                        config.setUserId(userModel.getId());
                        config.setBillCancellation(userModel.getBillCancellation());
                        config.setRoutCode(000l);
                        config.setRoutName("Route not available");
                        if (settings != null) {
                            if (settings.getRouteCode() != null) {
                                config.setRoutCode(settings.getRouteCode());
                            }
                            if (settings.getRouteName() != null) {
                                config.setRoutName(settings.getRouteName());

                            }
                        }

                        config.setLoggedIn(true);
                        config.setIsManager(false);
                        Intent i = new Intent(MainActivity.this, HomeScreenActivity.class);
                        startActivity(i);
                        /*for super Admin*/
                    }
                    else if (userCode.equals("admin")) {
                        userCode = edtUserCode.getText().toString().trim();
                        pinCode = edtPinCode.getText().toString().trim();
                        Boolean isValidUser = db.validateAdminUser(userCode, pinCode);
                        if (isValidUser) {
                            config.setIsManager(true);
                            Intent i = new Intent(MainActivity.this, ManagerHomeActivity.class);
                            startActivity(i);
                        } else {
                            txtErrorMessage.setVisibility(View.VISIBLE);
                            txtErrorMessage.setText("Invalid user credential ");
                        }
                    }
                    else {
                        txtErrorMessage.setVisibility(View.VISIBLE);
                        txtErrorMessage.setText("Invalid user credential ");
                    }
                }

            }

        });
    }

    @Override
    public void onBackPressed() {
        config.exitApp();
    }

}
