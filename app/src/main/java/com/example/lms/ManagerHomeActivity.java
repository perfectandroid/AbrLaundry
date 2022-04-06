package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.lms.utils.AppConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by admin on 10/25/2014.
 */
public class ManagerHomeActivity extends Activity {

    AppConfig config = new AppConfig(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_home);
        initEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manager_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        config.logout(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_settigs:
                Intent i = new Intent(this, SettingsHomeActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_logout:
                config.logout(this);
                return true;
            case R.id.export_db:
                exportDB();
                return true;
            case R.id.change_password:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_clear_orders:
                Intent in = new Intent(this, OrderClearActivity.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initEvents() {
        findViewById(R.id.btn_mh_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManagerHomeActivity.this, ManagerSettingsActivity.class);
                startActivity(i);

            }
        });

        findViewById(R.id.btn_mh_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManagerHomeActivity.this, UserListActivity.class);
                startActivity(i);
            }
        });
    }

    private void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;

//        String currentDBPath = "/com.example.lms/databases/lmsdb.db";
        //String currentDBPath = getApplicationContext().getDatabasePath("lmsdb").getAbsolutePath();
        String currentDBPath = "/data/com.example.lms/databases/lmsdb";

        File file = new File((Environment.getExternalStorageDirectory()+"/lms-files"), "");
        if (!file.exists()) {
            file.mkdirs(); }
        String backupDBPath = (new StringBuilder()).append(file.getPath()).append("/").toString();
//        String backupDBPath = "lmsdb";
        File currentDB = new File(data, currentDBPath);
//        File currentDB = new File(data, currentDBPath);
        File backupDB = new File((Environment.getExternalStorageDirectory()+"/lms-files/ABR_DATABASE.sqlite"), "");

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Database has been exported to 'lms-files' directory in the internal storage.", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}