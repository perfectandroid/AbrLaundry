package com.example.lms.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.lms.R;
import com.example.lms.models.OrderDetails;
import com.example.lms.models.Price;
import com.example.lms.models.Settings;

import java.util.List;

public class OrderCartListAdapter extends ArrayAdapter {

    private AppConfig appConfig;
    private List<OrderDetails> items;
    private Context context;
    DatabaseHandler db ;
    private Settings settings = null;


   // EditText txtPrice;
    public OrderCartListAdapter(Context context, int resource, List<OrderDetails> items) {
        super(context, resource, items);
        this.items = items;
        this.context = context;
        appConfig = new AppConfig(context);
        db = new DatabaseHandler(context);
        settings = db.getSettings();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View v = convertView;
        final OrderDetails ODetails = items.get(position);


        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.order_cart_item, null);
        }


        if (ODetails != null) {
            TextView txtType = (TextView) v.findViewById(R.id.txt_oci_type);
            TextView txtItemName = (TextView) v.findViewById(R.id.txt_oci_item);
            final EditText txtUnitPrice=(EditText)v.findViewById(R.id.txt_oci_unit_price);
            final EditText edtQty = (EditText) v.findViewById(R.id.edt_oci_qty);
            final EditText txtPrice = (EditText) v.findViewById(R.id.txt_oci_price);
            final Price itemPrice = db.searchItemPrice(ODetails.getServiceId(), ODetails.getItemId());
            final Double fixedTotal = itemPrice.getPrice();
            txtType.setText(ODetails.getServiceTypeName());
            txtItemName.setText(ODetails.getItemName().toString());
            edtQty.setText(ODetails.getQty().toString());
            txtUnitPrice.setText(String.format("%.2f", ODetails.getUnitPrice()));
            //Double totalPrice = itemPrice.getPrice()*ODetails.getQty();
            txtPrice.setText(String.format("%.2f", ODetails.getPrice()));
            txtUnitPrice.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Do you want to reset this price?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Price itemPrice = db.searchItemPrice(ODetails.getServiceId(), ODetails.getItemId());
                        final Double fixedPrice = itemPrice.getPrice() * appConfig.parseDouble(edtQty.getText().toString());
                        txtPrice.setText(String.format("%.2f", fixedPrice));
                        ODetails.setErrorCode("0000");
                        notifyDataSetChanged();
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
        return false;
    }
});

        }
        Button btnRemove = (Button) v.findViewById(R.id.btn_oci_remove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure want to delete this item ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                remove(getItem(position));
                                ODetails.setErrorCode("0000");
                                notifyDataSetChanged();
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
            }


        });
//        notifyDataSetChanged();
        return v;

    }

}