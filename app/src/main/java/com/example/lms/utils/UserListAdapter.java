package com.example.lms.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.lms.OrderDetailsDescActivity;
import com.example.lms.R;
import com.example.lms.UserCreateActivity;
import com.example.lms.models.Order;
import com.example.lms.models.User;

import java.util.List;

public class UserListAdapter extends ArrayAdapter {
    List<User> userList;
    AppConfig config = new AppConfig(getContext());

    public UserListAdapter(Context context, int textViewResourceId, List<User> userList) {

        super(context, textViewResourceId, userList);
        this.userList = userList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.user_list_item, null);
        }

        final User userObj = userList.get(position);
        if (userObj != null) {
            TextView txtUserCode = (TextView) v.findViewById(R.id.txt_user_code);
            txtUserCode.setText(userObj.getUserCode());
            TextView txtUserName = (TextView) v.findViewById(R.id.txt_user_name);
            txtUserName.setText(userObj.getName());

            v.findViewById(R.id.btn_oli_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), UserCreateActivity.class);
                    i.putExtra("user_id",userObj.getId());
                    view.getContext().startActivity(i);
                }
            });
        }
        return v;
    }
}