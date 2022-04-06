package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;

public class ChangePasswordActivity extends Activity {

    private EditText edtPassword, edtOldPass,edtConfirmPass;
    private Button btnChange;
    private TextView txtErrorMessage;
    private String oldPass, newPass,confirmPass;
    DatabaseHandler db = new DatabaseHandler(this);
    AppConfig config = new AppConfig(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        initControls();
        initEvents();
    }

    public void initControls() {
        btnChange = (Button) findViewById(R.id.btn_change_pass);
        edtOldPass = (EditText) findViewById(R.id.edt_old_pass);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfirmPass = (EditText) findViewById(R.id.edt_confirm_pass);
        txtErrorMessage = (TextView) findViewById(R.id.txt_error);
        txtErrorMessage.setVisibility(View.INVISIBLE);
    }

    public void initEvents() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConfig.validateNullFields(edtOldPass, null, "Please provide correct Password", ChangePasswordActivity.this)
                        && AppConfig.validateNullFields(edtPassword, null, "Please provide new Password", ChangePasswordActivity.this)
                        && AppConfig.validateNullFields(edtConfirmPass, null, "Please provide confirm Password", ChangePasswordActivity.this)) {

                    oldPass = edtOldPass.getText().toString().trim();
                    newPass = edtPassword.getText().toString().trim();
                    confirmPass = edtConfirmPass.getText().toString().trim();
                    if(!newPass.equals(confirmPass))
                    {
                        txtErrorMessage.setText("Password/Confirm Password Incorrect");
                        txtErrorMessage.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                       Long status = db.updateAdminPassword(newPass,oldPass);
                        if(status > 0)
                        {
                            Toast.makeText(getApplicationContext(),"Password Changed Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePasswordActivity.this,ManagerHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Old Password is Incorrect", Toast.LENGTH_SHORT).show();
                            txtErrorMessage.setText("Old Password is Incorrect");
                            txtErrorMessage.setVisibility(View.INVISIBLE);
                        }
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
