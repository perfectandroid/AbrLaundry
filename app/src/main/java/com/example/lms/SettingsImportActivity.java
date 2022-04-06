package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.lms.models.*;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;
import com.filechooser.FileChooser;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 10/27/2014.
 */
public class SettingsImportActivity extends Activity {
    private static final int FILE_CHOOSER = 11;
    Spinner spTable;
    Button btnDone,chooseFile,btnImport;
    TextView txtSuccessMsg;
    String fileSelected;
    String errorMessage=null;
    DatabaseHandler db = new DatabaseHandler(this);
    AppConfig config=new AppConfig(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_import);
        initControls();
        initEvents();
        initAdapter();

    }

    private void initAdapter() {
         /*Create objects of controls*/

        final ArrayAdapter<CharSequence> tableAdapter = ArrayAdapter.createFromResource(this, R.array.import_table_list, android.R.layout.simple_list_item_1);
        spTable.setAdapter(tableAdapter);
    }

    public void initControls() {

        chooseFile =(Button) findViewById(R.id.btn_sti_choose_file);
        spTable = (Spinner) findViewById(R.id.sp_sti_tables);
//        btnDone = (Button) findViewById(R.id.btn_sti_done);
        btnImport = (Button) findViewById(R.id.btn_sti_import);
        txtSuccessMsg = (TextView) findViewById(R.id.txt_sti_success_message);
    }

    public void initEvents() {
          /*Manage footer Button*/
//        findViewById(R.id.btn_sti_done).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(SettingsImportActivity.this, ManagerHomeActivity.class);
//                startActivity(i);
//            }
//        });
        findViewById(R.id.btn_sti_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsImportActivity.this, SettingsHomeActivity.class));
            }
        });
        findViewById(R.id.btn_sti_import).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtSuccessMsg.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
            }
        });
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spTable.getSelectedItemPosition()==0) {
                    Toast.makeText(SettingsImportActivity.this,"Please choose a table to import",Toast.LENGTH_SHORT).show();
                } else{

                        doOpen();
                    }
            }
        });
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileSelected!=null) {
                    if (spTable.getSelectedItemId() == 1) {
                        readAreaFile(fileSelected);
                    } else if (spTable.getSelectedItemId() == 2) {
                        readBuildingFile(fileSelected);
                    } else if (spTable.getSelectedItemId() == 3) {
                        readPriceFile(fileSelected);
                    } else if (spTable.getSelectedItemId() == 4) {
                        readCustomerFile(fileSelected);
                    }
                    fileSelected = null;
                }else{

                    Toast.makeText(SettingsImportActivity.this,"Choose an appropriate file.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void doOpen() {
        Intent intent = new Intent(this, FileChooser.class);
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add(".csv");
        //extensions.add(".txt");
        intent.putStringArrayListExtra("filterFileExtension", extensions);
        startActivityForResult(intent, FILE_CHOOSER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
             fileSelected = data.getStringExtra("fileSelected");

            if(!checkChosenFile(spTable.getSelectedItem().toString())) {
                //Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
            //}else {
                fileSelected=null;
                Toast.makeText(this, "Choose an appropriate file.", Toast.LENGTH_SHORT).show();
            }


        }

    }

    /*check user choose proper file*/
    private Boolean checkChosenFile(String tableName){
        String[] fullPath=fileSelected.split("/");
       String fileName=fullPath[fullPath.length-1];
        return fileName.toLowerCase().contains(tableName.toLowerCase());
    }

    /*read customer csv file and add customer to db*/
   private void readCustomerFile(String fileSelected){
       List<String[]> list = new ArrayList<String[]>();

       File file = new File(fileSelected);
       BufferedReader br = null;
       try {
           br = new BufferedReader(new FileReader(file));
           CSVReader csvReader = new CSVReader(br);
           String[] line;

           // throw away the header
           csvReader.readNext();
           while ((line = csvReader.readNext()) != null) {
               list.add(line);
           }
        if(list.isEmpty()) {
            Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
        }else {
            Customer customer=new Customer();
            Area area = new Area();
            Building building = new Building();
            User user=new User();
            int TotalRecords=0;
            for(int i=0;i<list.size();i++) {
                String oneLine=list.get(i)[0].toString();
                String[] values=oneLine.split("\\|");
                customer.setCustomerId(Long.parseLong(values[0]));
                customer.setName(values[1]);
                area.setAreaId(Long.parseLong(values[2]));

                building.setBuildingId(Long.parseLong(values[3]));
        /*set customer details*/
                customer.setAddress1(values[4]);
                customer.setAddress2(values[5]);
                customer.setArea(area);
                customer.setMobileNo(values[6]);
                customer.setTelephoneNo(values[7]);
                customer.setEmail(values[8]);
                user.setId(Long.parseLong(values[9]));
                customer.setUser(user);
                customer.setBuilding(building);

                Long customerId = db.addCustomer(customer);

                if (customerId>0) {
                    TotalRecords++;

                }

            }

            Toast.makeText(this, "There are " + TotalRecords + " Customer records are updated successfully. ", Toast.LENGTH_LONG).show();
        }
       } catch (IOException e) {
           e.printStackTrace();
       }

    }

    /*read price csv file and add price items to db*/
    private void readPriceFile(String fileSelected){
        List<String[]> list = new ArrayList<String[]>();

        File file = new File(fileSelected);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            CSVReader csvReader = new CSVReader(br);
            String[] line;

            // throw away the header
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                list.add(line);
            }
            if(list.isEmpty()) {
                Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
            }else {
                Price price=new Price();
                int TotalRecords=0;
                int failedRecords=0;
                for(int i=0;i<list.size();i++) {
                    String[] values=list.get(i)[0].split("\\|");

                    price.setServiceId(Long.parseLong(values[0]));
                    price.setItemId(Long.parseLong(values[1]));
                    price.setServiceName(values[2]);
                    price.setItemName(values[3]);
                    price.setPrice(Double.parseDouble(values[4]));
                    price.setFavourite(values[5]);
                   Long priceId= db.addPrice(price);

                    if (priceId>0 && priceId!=null) {
                        TotalRecords++;

                    }else{
                        failedRecords++;
                    }

                }

                Toast.makeText(this, "There are " + TotalRecords + " Price records are updated successfully. ", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*read area csv file and add each area  to db*/
    private void readAreaFile(String fileSelected){
        List<String[]> list = new ArrayList<String[]>();

        File file = new File(fileSelected);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            CSVReader csvReader = new CSVReader(br);
            String[] line;

            // throw away the header
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                list.add(line);
            }
            if(list.isEmpty()) {
                Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
            }else {
                Area area=new Area();
                int TotalRecords=0;
                for(int i=0;i<list.size();i++) {
                    String[] values=list.get(i)[0].split("\\|");

                    area.setAreaId(Long.parseLong(values[0]));
                    area.setAreaName(values[1]);
                    area.setAreaId1(Long.parseLong(values[2]));

                    Long areaId= db.addArea(area);

                    if (areaId>0) {
                        TotalRecords++;

                    }

                }

                Toast.makeText(this, "There are " + TotalRecords + " Area records are updated successfully. ", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*read building csv file and add each building   to db*/
    private void readBuildingFile(String fileSelected){
        List<String[]> list = new ArrayList<String[]>();

        File file = new File(fileSelected);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            CSVReader csvReader = new CSVReader(br);
            String[] line;

            // throw away the header
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                list.add(line);
            }
            if(list.isEmpty()) {
                Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
            }else {
                Building building=new Building();
                int TotalRecords=0;
                for(int i=0;i<list.size();i++) {
                    String[] values=list.get(i)[0].split("\\|");

                    building.setBuildingId(Long.parseLong(values[0]));
                    building.setBuildingName(values[1]);
                    building.setBuildingId1(Long.parseLong(values[2]));
                    Long buildingId= db.addBuilding(building);

                    if (buildingId>0) {
                        TotalRecords++;

                    }

                }
                Toast.makeText(this, "There are " + TotalRecords + " Building records are updated successfully. ", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*read order csv file and add each order  to db*/
    private void readOrderFile(String fileSelected){
        List<String[]> list = new ArrayList<String[]>();

        File file = new File(fileSelected);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            CSVReader csvReader = new CSVReader(br);
            String[] line;

            // throw away the header
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                list.add(line);
            }
            if(list.isEmpty()) {
                Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
            }else {
                Order order=new Order();
                Customer customer=new Customer();
                Long orderId;
                int TotalRecords=0;
                for(int i=0;i<list.size();i++) {
                    String[] values=list.get(i)[0].split("\\|");

                    order.setOrderNo(values[1]);
                    order.setOrderDate(values[2]);
                    order.setOrderTime(values[3]);
                    customer.setCustomerId(Long.parseLong(values[4]));
                    order.setCustomer(customer);
                    //order.setTotalAmount(getNetAmount());
                    order.setAdditionalAmount(Math.round(Double.parseDouble(values[5])));
                     order.setOrderStatus(Long.parseLong(values[6]));

                    order.setExpressStatus(values[7]);
                    order.setRemarks(values[8]);
                    orderId=  db.addOrder(order);

                    if (orderId>0) {
                        TotalRecords++;

                    }

                }

                Toast.makeText(this, TotalRecords+" items inserted successfully ", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*read order Details csv file and add each order Details to db*/
    private void readOrderDetailsFile(String fileSelected){
        List<String[]> list = new ArrayList<String[]>();

        File file = new File(fileSelected);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            CSVReader csvReader = new CSVReader(br);
            String[] line;

            // throw away the header
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                list.add(line);
            }
            if(list.isEmpty()) {
                Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
            }else {

                Long orderDetailsId;
                int TotalRecords=0;
                for(int i=0;i<list.size();i++) {
                    String[] values=list.get(i)[0].split("\\|");

                    OrderDetails odObject = new OrderDetails();
                    odObject.setOrderId(Long.valueOf(values[0]));
                    odObject.setItemId(Long.valueOf(values[1]));
                    odObject.setServiceId(Long.valueOf(values[2]));
                    odObject.setQty(Long.valueOf(values[3]));
                    odObject.setPrice(Double.valueOf(values[4]));
                    odObject.setItemRemarks(values[5]);
                    orderDetailsId= db.addOrderDetails(odObject);

                    if (orderDetailsId>0) {
                        TotalRecords++;

                    }

                }

                Toast.makeText(this, TotalRecords+" items inserted successfully ", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}