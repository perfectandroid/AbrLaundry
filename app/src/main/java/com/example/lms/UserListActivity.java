package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.lms.models.User;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;
import com.example.lms.utils.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 10/29/2014.
 */
public class UserListActivity extends Activity {
    DatabaseHandler db = new DatabaseHandler(this);
    UserListAdapter adapter;
    private AppConfig config=new AppConfig(this);
    ListView userListView;
    Button btnAddNewUser;
    LinearLayout lnrListHeader;
    TextView txtEmptyMessage;

    List<User> userList=new ArrayList<User>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        initControls();
        initEvents();
        userList = db.getAllUsers();
        if(userList.size()>0) {
            txtEmptyMessage.setVisibility(View.GONE);
            fillAdapter(userList);
        }else{
            userListView.setVisibility(View.GONE);
            lnrListHeader.setVisibility(View.GONE);
            txtEmptyMessage.setVisibility(View.VISIBLE);
        }


    }

    private void initControls() {
        userListView = (ListView) findViewById(R.id.lst_user_view);
        lnrListHeader =(LinearLayout)findViewById(R.id.lnr_user_list_list_head);
        txtEmptyMessage=(TextView)findViewById(R.id.txt_user_list_empty_message);
        btnAddNewUser=(Button)findViewById(R.id.btn_user_list_add_new_user);
    }

    private void fillAdapter(List<User> userListModel) {
        userList = db.getAllUsers();
        adapter = new UserListAdapter(this, R.layout.user_list_item, userListModel);
        userListView.setAdapter(adapter);
    }

    public void initEvents() {
        findViewById(R.id.btn_ol_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserListActivity.this, ManagerHomeActivity.class));
            }
        });
        btnAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserListActivity.this, UserCreateActivity.class));
            }
        });
    }


}