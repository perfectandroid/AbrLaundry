package com.example.lms.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.lms.R;
import com.example.lms.models.Price;

import java.util.List;

/**
 * Created by admin on 12/23/2014.
 */
public class ItemAdapter extends ArrayAdapter {

    private Activity context;
    private List<Price> priceList;
    private int recourseId;
    public ItemAdapter (Activity context,int textViewResourceId,List<Price> priceList){
        super(context, textViewResourceId, priceList);
        this.context=context;
       this.priceList=priceList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //RelativeLayout item= (RelativeLayout)context.getLayoutInflater().inflate(R.layout.item_names_list, null);
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();

            convertView = inflater.inflate(R.layout.item_names_list,parent,false);

        }

        TextView txtItemName = (TextView) convertView.findViewById(R.id.txt_inl_item_name);
        txtItemName.setText(priceList.get(position).getItemName());
        //set your data to layout components

        convertView.setTag(priceList.get(position));

        return convertView;
    }
}