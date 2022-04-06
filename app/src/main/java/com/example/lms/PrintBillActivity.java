package com.example.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.example.lms.models.*;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.BTDeviceList;
import com.example.lms.utils.DatabaseHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PrintBillActivity extends Activity {


    private static BluetoothSocket blueThSocket;
    private static OutputStream blueThOutputStream;
    private Long printCount = 1l;
    private Long printedCount = 0l;
    Button printBtn;
    byte FONT_TYPE;
    DatabaseHandler db;
    AppConfig config = new AppConfig(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getApplicationContext());
        connect();

    }

    protected void connect() {
        Settings settings = db.getSettings();
        if (settings != null) {
            if (settings.getNoOfBillCopies() != null) {
                printCount = settings.getNoOfBillCopies();
            }
        }
        if (blueThSocket == null) {
            Intent BTIntent = new Intent(getApplicationContext(), BTDeviceList.class);
            startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        } else {
            OutputStream outputStream = null;
            try {
                outputStream = blueThSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            blueThOutputStream = outputStream;


            new AsyncPrintBill().execute();
        }
    }


    private class AsyncPrintBill extends AsyncTask<String, Void, Boolean> {
        Exception mException = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.show();
            this.mException = null;
        }

        @Override
        protected Boolean doInBackground(String... vals) {
            Boolean isPrint = printDetails();
            if (isPrint)
                printedCount++;
            return isPrint;
        }

        @Override
        protected void onPostExecute(Boolean isPrint) {
            super.onPostExecute(isPrint);
            //Toast.makeText(getApplicationContext(), "Printed", Toast.LENGTH_LONG).show();
            if (printedCount < printCount) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PrintBillActivity.this);
                builder1.setMessage("Do you want to continue printing?");
                builder1.setTitle(R.string.app_name);
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                new AsyncPrintBill().execute();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Print completed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
            }
        }
    }


    private Boolean printDetails() {
        try {

            blueThOutputStream = blueThSocket.getOutputStream();
//            String msg = getIntent().getStringExtra("printMessage");
            Long orderId = getIntent().getLongExtra("orderId", 0);
            Settings st = db.getSettings();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            byte[] arrayOfByte1 = {27, 33, 0};
            byte[] format = {27, 33, 0};
            byte[] printFormat = {0x1B, 0x21, FONT_TYPE};
//            for (int count = 0; count < printCount; count++) {
            if (orderId > 0) {
                Long totalQuantity = 0l;
                Double totalItemPrice = 0.0;
                User u = db.getUser(orderId);
                String serviceType = "";
                List<OrderDetails> orderDetailsFromDb = db.orderDetailsByOrderId(orderId);
                if (!orderDetailsFromDb.isEmpty()) {
                    Price itemPrice = db.searchItemPrice(orderDetailsFromDb.get(0).getServiceId(), orderDetailsFromDb.get(0).getItemId());
                    serviceType = itemPrice.getServiceName();
                }
                Order orderFromDb = db.getOrder(orderId);
                blueThOutputStream.write(printFormat);
                String lineSep = "---------------------------------------------\n";
                format[2] = ((byte) (0x10 | arrayOfByte1[2]));
                blueThOutputStream.write(format);
                String companyName = config.getCenteredString(st.getCompanyname());
                blueThOutputStream.write(companyName.getBytes(), 0, companyName.length());
                blueThOutputStream.write(printFormat);
                String printItem = config.getCenteredString(st.getCompanyaddress());
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                printItem = config.getCenteredString(st.getAddress1());
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                printItem = config.getCenteredString(st.getAddress2());
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(format);
                printItem = "Salesman: " + config.getUsername() + "   Route Code: " + st.getRouteCode() + "\n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(printFormat);
                blueThOutputStream.write(lineSep.getBytes(), 0, lineSep.length());
                printItem = "                   INVOICE                   \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(lineSep.getBytes(), 0, lineSep.length());
                blueThOutputStream.write(format);
                printItem = " Bill No         : " + config.addOrderPrefix(orderFromDb.getOrderId()) + "\n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(printFormat);
                printItem = " Date            : " + orderFromDb.getOrderDate() + "    \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                printItem = " Service         : " + serviceType + "      \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                printItem = " Customer        : " + orderFromDb.getCustomer().getName() + "                       \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());

                String address1 = orderFromDb.getCustomer().getAddress1().trim().replace("-","");
                String address2 = orderFromDb.getCustomer().getAddress2().trim().replace("-","");

                if(!address1.isEmpty() || !address1.isEmpty()) {

                    printItem = " Address         : " + address1 + (!address2.isEmpty() ? ", " + orderFromDb.getCustomer().getAddress2().trim() : "") + "\n";
                    blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                }
                printItem = " Mobile          : " + orderFromDb.getCustomer().getMobileNo() + "             \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                printItem = " Telephone       : " + orderFromDb.getCustomer().getTelephoneNo() + "             \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(lineSep.getBytes(), 0, lineSep.length());
                printItem = " Item             Qty      Price    Amount   \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(lineSep.getBytes(), 0, lineSep.length());
//
                for (OrderDetails item : orderDetailsFromDb) {
                    totalQuantity += item.getQty();

                    Price itemPrice = db.searchItemPrice(item.getServiceId(), item.getItemId());
                    totalItemPrice += item.getPrice();
                    printItem = " " + config.alignLine(itemPrice.getItemName() + "|" + item.getQty() + "|" + String.format("%.2f", item.getUnitPrice()) + "|" + String.format("%.2f", item.getPrice())) + "\n";
                    blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                    //}
                }
                blueThOutputStream.write(lineSep.getBytes(), 0, lineSep.length());
                printItem = "                         Total     : " + String.format("%.2f", totalItemPrice) + " \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(lineSep.getBytes(), 0, lineSep.length());
                printItem = "                     Total Qty     : " + totalQuantity + "  \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                printItem = "                     Additions     : " + orderFromDb.getAdditionalAmount() + ".0" + " \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(lineSep.getBytes(), 0, lineSep.length());
                printItem = " Net Amount                         " + String.format("%.2f", orderFromDb.getNetVatAmt()) + " \n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());

                printItem = LmsConstants.vatPercentage + "% VAT included \n";
                blueThOutputStream.write( printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(lineSep.getBytes(), 0, lineSep.length());
                format[2] = ((byte) (0x1 | arrayOfByte1[2]));
                blueThOutputStream.write(format);
                printItem = "\nTERMS & CONDITIONS\n" +
                        "For any complaint, please contact the Manager between 9AM - 12 Noon within 1 day. The laundry is not responsible for shrinkage or fastness of colour. In case of loss or damage the laundry will be liable for payment by 10 times the cost of cleaning of the Lost or Damaged clothes. 99% removal of stains guaranteed.\n\n" +
                        "                THANK YOU FOR YOUR BUSINESS         \n" +
                        "                \"Discover the Difference\"         \n" +
                        "                                             \n\n";
                blueThOutputStream.write(printItem.getBytes(), 0, printItem.length());
                blueThOutputStream.write(lineSep.getBytes(), 0, lineSep.length());
            }
//            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if ( blueThSocket != null ) {
                blueThOutputStream.close();
                blueThSocket.close();
                blueThSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            blueThSocket = BTDeviceList.getSocket();
            if (blueThSocket != null) {
                new AsyncPrintBill().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}         
