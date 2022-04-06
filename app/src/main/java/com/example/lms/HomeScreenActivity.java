package com.example.lms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lms.models.Customer;
import com.example.lms.models.Order;
import com.example.lms.models.OrderDetails;
import com.example.lms.models.Settings;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;
import com.opencsv.CSVWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HomeScreenActivity extends Activity {
    private Button btnNewOrder;
    private Button btnViewSales;
    private Intent intent;
    DatabaseHandler db = new DatabaseHandler(this);
    private TextView txtUsername, txtRoutName;
    private AppConfig config = new AppConfig(this);
    String exc="none";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        controlsBySettings();
        initControls();
        initEvents();
        txtUsername.setText(config.getUsername());
        //txtTodayDate.setText(config.getCurrentDateString());
        txtRoutName.setText(config.getRoutName());
        // txtTodayDate.setText(config.getCurrentDateString(config.getCurrentDate()));
    }

    public void initControls() {
        btnNewOrder = (Button) findViewById(R.id.btn_hm_new_order);
        btnViewSales = (Button) findViewById(R.id.btn_hm_view_order);
        txtUsername = (TextView) findViewById(R.id.txt_hm_user_name);
        //txtTodayDate = (TextView) findViewById(R.id.txt_hm_date);
        txtRoutName = (TextView) findViewById(R.id.txt_hm_rout);
    }

    public void initEvents() {
        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeScreenActivity.this, OrderCustomerActivity.class);
                startActivity(intent);
            }
        });

        btnViewSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeScreenActivity.this, OrderSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.menu_export:
                new TableExportHelper().execute("");
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*controls according to settings*/
    private void controlsBySettings() {
        Settings settings = db.getSettings();
        if (settings != null && settings.getRouteCode() == null) {
            config.clearAllLoginValues();
            config.showAlert("Application settings are not configured. Please contact administrator", "Logout",MainActivity.class);
        }


    }

    @Override
    public void onBackPressed() {
        config.exitApp();
    }


    public class TableExportHelper extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(HomeScreenActivity.this);
        boolean memoryErr = false;

        protected void onPreExecute() {
            dialog.setMessage("Exporting Table...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(final String... args) {
            return exportToCSV(args[0]);
        }

        protected void onPostExecute(Boolean status) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (status) {
                Toast.makeText(getApplicationContext(), "The data has been successfully exported to 'lms-files' directory in the internal storage.", Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "The data export process failed, "+ exc, Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), exc+"===="+newfie, Toast.LENGTH_LONG).show();
                return;
            }
        }


        public Boolean exportToCSV(String tableName) {
            String msg = "";
            CSVWriter csvwriter;
            try {

                File file = new File((Environment.getExternalStorageDirectory()+"/lms-files"), "");
                if (!file.exists()) {
                    file.mkdirs(); }
                String path = (new StringBuilder()).append(file.getPath()).append("/").append(tableName.toLowerCase()).toString();

                String customerFilePath = path + "Customers.csv";
                String orderFilePath = path + "Orders.csv";
                String orderDetailsFilePath = path + "OrderDetails.csv";


                /************************** Exporting Customer Details *********************/
                StringBuilder customersText = new StringBuilder();
                customersText.append("Customer ID|Customer Name|AreaID|Building ID|Address 1|Address 2|Mobile No|Telephone No|Email\r\n");

                List<Customer> customerList = db.getAllCustomer();
                if(customerList != null) {
                    for (Customer customer : customerList) {
                        if (customer != null) {
                            customersText.append(customer.getCustomerId() != null ? customer.getCustomerId(): 0);
                            customersText.append("|");
                            customersText.append(customer.getName().isEmpty() ? "-":customer.getName());
                            customersText.append("|");
                            customersText.append(customer.getArea() != null && customer.getArea().getAreaId() != null? customer.getArea().getAreaId(): 0);
                            customersText.append("|");
                            customersText.append(customer.getBuilding() != null && customer.getBuilding().getBuildingId() != null ? customer.getBuilding().getBuildingId(): 0);
                            customersText.append("|");
                            customersText.append(customer.getAddress1().isEmpty() == false?  customer.getAddress1(): "-");
                            customersText.append("|");
                            customersText.append(customer.getAddress2().isEmpty() == false?  customer.getAddress2(): "-");
                            customersText.append("|");
                            customersText.append(customer.getMobileNo().isEmpty() == false?  customer.getMobileNo(): "-");
                            customersText.append("|");
                            customersText.append(customer.getTelephoneNo().isEmpty() == false?  customer.getTelephoneNo(): "-");
                            customersText.append("|");
                            customersText.append(customer.getEmail().isEmpty() == false?  customer.getEmail(): "-");
                            customersText.append("\r\n");
                        }
                    }
                    msg += generateCsvFile(customersText, customerFilePath);
                }

                /************************** Exporting Orders  *********************/
                StringBuilder ordersText = new StringBuilder();
                ordersText.append("OrderID|OrderNo|OrderDate|OrderTime|CustomerID|AdditionalAmount|OrderStatus|Express Status|Remarks|UserID\r\n");

                List<Order> ordersList = db.getAllOrder();
                if(ordersList !=null) {
                    for (Order order : ordersList) {
                        ordersText.append(order.getOrderId() != null ? order.getOrderId(): 0);
                        ordersText.append("|");
                        ordersText.append(order.getOrderNo() != null ? order.getOrderNo(): 0);
                        ordersText.append("|");

                        if (order.getOrderDate() != null) {
                            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                            DateTime dt = formatter.parseDateTime(order.getOrderDate());
                            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                            String orderDate = fmt.print(dt);
                            ordersText.append(orderDate);
                            ordersText.append("|");
                        }
                        else
                        {
                            ordersText.append("-|");
                        }

                        ordersText.append(order.getOrderTime() != null ? order.getOrderTime(): "-");
                        ordersText.append("|");
                        ordersText.append(order.getCustomerId() != null ? order.getCustomerId(): 0);
                        ordersText.append("|");
                        ordersText.append(order.getAdditionalAmount() != null ? String.valueOf(order.getAdditionalAmount()) + ".0": "0.0");
                        ordersText.append("|");
                        ordersText.append(order.getOrderStatus() != null ? String.valueOf(order.getOrderStatus()): "-");
                        ordersText.append("|");
                        ordersText.append(order.getExpressStatus() != null ? String.valueOf(order.getExpressStatus()): "-");
                        ordersText.append("|");
                        ordersText.append(order.getRemarks() != null ? order.getRemarks(): "-");
                        ordersText.append("|");
                        ordersText.append(config.getUserId() != null ? config.getUserId(): 0);
                        ordersText.append("\r\n");

                    }
                    msg += generateCsvFile(ordersText, orderFilePath);
                }

                /************************** Exporting Order Details  *********************/
                StringBuilder orderDetails = new StringBuilder();
                orderDetails.append("OrderID|ItemID|ServiceID|Unit Price|Qty|Total Price|Item Remarks\r\n");

                List<OrderDetails> orderDetailsList =  db.getAllOrderDetails();
                if(orderDetailsList != null) {
                    for (OrderDetails orderdetails : orderDetailsList) {
                        orderDetails.append(orderdetails.getOrderId() != null ? orderdetails.getOrderId(): 0);
                        orderDetails.append("|");
                        orderDetails.append(orderdetails.getItemId() != null? orderdetails.getItemId(): "0");
                        orderDetails.append("|");
                        orderDetails.append(orderdetails.getServiceId() != null? orderdetails.getServiceId(): "0");
                        orderDetails.append("|");
                        orderDetails.append(orderdetails.getUnitPrice() != null? orderdetails.getUnitPrice(): "0.0");
                        orderDetails.append("|");
                        orderDetails.append(orderdetails.getQty() != null? orderdetails.getQty(): "0");
                        orderDetails.append("|");
                        orderDetails.append(orderdetails.getPrice() != null? orderdetails.getPrice(): "0.0");
                        orderDetails.append("|");
                        orderDetails.append(orderdetails.getItemRemarks().isEmpty()? "-":  orderdetails.getItemRemarks());
                        orderDetails.append("\r\n");
                    }
                    msg += generateCsvFile(orderDetails,orderDetailsFilePath);
                }
            } catch (Exception e) {
                msg = e.getMessage();
                exc = e.toString();
            }
            return msg == "";
        }

        public String generateCsvFile(StringBuilder text, String sFileName)
        {
            String msg = "";

            try {
                if(text != null && text.length() > 0)
                {
                    FileWriter writer = new FileWriter(sFileName);
                    writer.append(text.toString());
                    writer.flush();
                    writer.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                msg = e.getMessage();
            }

            return msg;
        }
    }
}