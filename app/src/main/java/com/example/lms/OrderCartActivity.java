package com.example.lms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.lms.models.Customer;
import com.example.lms.models.OrderDetails;
import com.example.lms.models.Price;
import com.example.lms.models.Settings;
import com.example.lms.utils.AppConfig;
import com.example.lms.utils.DatabaseHandler;
import com.example.lms.utils.OrderCartListAdapter;
import com.example.lms.utils.OrderDetailsParcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 10/22/2014.
 */
public class OrderCartActivity extends Activity {
    List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
    DatabaseHandler db = new DatabaseHandler(this);
    Spinner spinnerType;
    Button btnNext, btnAdd, btnClearAll,btnGoHome;
    EditText edtQty,edtUnitPrice;
    TextView errorText,textCustomerName;
    ListView cartItemList;
    AppConfig config = new AppConfig(this);
    OrderCartListAdapter adapter;
    AutoCompleteTextView autoEdtItem;
    List<Price> itemType;
    List<Price> itemService;
    Price selectedItem;
    Double selectedPrice;
    Long SelectedItemId;
    long customerId;
    private Boolean allowToProceed=true;
    private Settings settings = null;
    private AppConfig appConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_cart);
       /*Create Controls objects*/
        customerId = getIntent().getLongExtra("CustomerId", 0);
        settings = db.getSettings();
        appConfig = new AppConfig(this);

        initControls();

        initEvents();
        initAdapters();

        List<Price> priceList = db.getAllPrice();
        if (priceList.isEmpty()) {
            Toast.makeText(this, "Please add price list", Toast.LENGTH_SHORT).show();
        }


    }


    private void initAdapters() {
          /*set items for dropdown items*/
        itemType = db.getAllItemType();
         itemService = db.getAllServiceTypes();
        String[] itemNameList = new String[itemType.size()];
        String[] serviceNameList = new String[itemService.size() + 1];
      //  itemNameList[0] = "Please choose an item";
        serviceNameList[0] = "Please choose a service";

        if (itemService.size() > 0) {
            for (int i = 1; i <= itemService.size(); i++) {
                serviceNameList[i] = itemService.get(i - 1).getServiceName();

            }
        }
        if (itemType.size() > 0) {
            for (int i = 0; i < itemType.size(); i++) {
                itemNameList[i] = itemType.get(i).getItemName();

            }
        }

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemNameList);
        autoEdtItem.setAdapter(itemAdapter);
// specify the minimum type of characters before drop-down list is shown
        autoEdtItem.setThreshold(1);
        ArrayAdapter<String> serviceTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, serviceNameList);
        spinnerType.setAdapter(serviceTypeAdapter);


        // when the user clicks an item of the drop-down list

        autoEdtItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                SelectedItemId = getItemId(arg0.getItemAtPosition(arg2).toString());
                selectedItem = new Price();
                selectedItem.setItemId(SelectedItemId);
                selectedItem.setItemName(arg0.getItemAtPosition(arg2).toString());
                Price itemPrice = getItemPrice();
                selectedPrice = itemPrice.getPrice();
                edtUnitPrice.setText(itemPrice.getPrice().toString());
            }
        });

        //Settings settings = db.getSettings();
        if (settings != null && settings.getPriceChange() != null) {
            if (settings.getPriceChange() == 0) {
                edtUnitPrice.setEnabled(false);
            }
        }
           /* }else {
                config.showAlert("Price cannot be empty.please import required files", "OK", null);
            }*/
    }

    private Long getItemId(String itemAtName) {
        Long ItemId = 0l;
        for (int i = 0; i < itemType.size(); i++) {
            if (itemType.get(i).getItemName().equals(itemAtName)) {
                ItemId = itemType.get(i).getItemId();
                break;
            }
        }
        return ItemId;
    }

    /*get Service type id*/
    private Long getServiceItemId(String serviceItem) {
        Long serviceId = 0l;
        for (int i = 0; i < itemService.size(); i++) {
            if (itemService.get(i).getServiceName().equals(serviceItem)) {
                serviceId = itemService.get(i).getServiceId();
                break;
            }
        }
        return serviceId;
    }
    public void initControls() {
        cartItemList = (ListView) findViewById(R.id.list_oc_order_items);
        spinnerType = (Spinner) findViewById(R.id.sp_oc_type);
        textCustomerName = (TextView) findViewById(R.id.txt_oc_customer);
        btnNext = (Button) findViewById(R.id.btn_oc_next);
        edtQty = (EditText) findViewById(R.id.edt_oc_qty);

        btnAdd = (Button) findViewById(R.id.btn_oc_add);
        btnGoHome = (Button) findViewById(R.id.btn_oc_home);
        errorText = (TextView) findViewById(R.id.txt_oc_error_choose);
        btnClearAll = (Button) findViewById(R.id.btn_oc_clear);
        autoEdtItem = (AutoCompleteTextView) findViewById(R.id.auto_oc_item);
        edtUnitPrice = (EditText) findViewById(R.id.edt_oc_unit_price);
    }

    public Price getItemPrice()
    {
        Price itemPrice = null;
        if (selectedItem != null) {
            Long itemId = selectedItem.getItemId();
            Long serviceId = getServiceItemId(spinnerType.getSelectedItem().toString());
            itemPrice = db.searchItemPrice(serviceId, itemId);
        }
        return itemPrice;
    }


    public void initEvents() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConfig.validateNullFields(edtQty, null, null, OrderCartActivity.this)
                        && !autoEdtItem.getText().toString().isEmpty()
                        && AppConfig.validateDropdown(spinnerType)) {

                    /*Local Variables*/
                    if (selectedItem != null) {
                        String item = selectedItem.getItemName();
                        Long itemId = selectedItem.getItemId();
                        Long serviceId = getServiceItemId(spinnerType.getSelectedItem().toString());


                        Price itemPrice = getItemPrice();

                        Double grandTotal = null;
                        boolean isAllowToSave = true;
                        if (edtUnitPrice.getText().toString().length() > 0) {

                            if (settings != null && settings.getPriceChange() != null) {
                                grandTotal = appConfig.parseDouble(edtUnitPrice.getText().toString())*appConfig.parseDouble(edtQty.getText().toString());

                                if (settings.getPriceChange() == 2) {
                                    if (selectedPrice > appConfig.parseDouble(edtUnitPrice.getText().toString())) {
                                        Toast.makeText(OrderCartActivity.this, "you cant change total price less than actual amount please reset it", Toast.LENGTH_SHORT).show();
                                        edtUnitPrice.requestFocus();
                                        edtUnitPrice.setText(selectedPrice.toString());

                                    }else {
                                        if (itemPrice != null) {
                                            spinnerType.setEnabled(false);
                                            hideKeyboard();
                                            AddItemToCart(itemId, item, serviceId, Long.parseLong(edtQty.getText().toString()), spinnerType.getSelectedItem().toString(), itemPrice.getPrice());
                                        } else {
                                            config.showAlert("Cannot find the price of this service for this item. Please contact your administrator to import price list.", "OK", null);
                                        }
                                    }
                                }
                            }

                        }


                    } else {
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText("This item not found");
                    }
                }
                else {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("please provide all required fields");
                }
            }


        });
          /*Handle Next button*/
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderDetailsList.isEmpty()) {
                    Toast.makeText(OrderCartActivity.this, "You didn't add any order yet.", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(OrderCartActivity.this, OrderSubmitActivity.class);
                    i.putExtra("CustomerId", customerId);
                    OrderDetailsParcel orderDetailsParcel = new OrderDetailsParcel();
                    Bundle b = new Bundle();
                    int listCount = adapter.getCount();
                    List<OrderDetails> orderDetailsList1 = new ArrayList<OrderDetails>();
                    for (int count = 0; count < listCount; count++) {
                        OrderDetails od = (OrderDetails) adapter.getItem(count);
                        orderDetailsList1.add(od);
                    }
                    allowToProceed=true;
                    for (OrderDetails item : orderDetailsList1) {
                        if(item.getErrorCode()!=null){
                            if (item.getErrorCode().equals("1001")) {
                                allowToProceed = false;
                            }
                        }
                        orderDetailsParcel.add(item);
                    }
                    if (allowToProceed) {
                        b.putParcelable("OrderDetailsList", orderDetailsParcel); //Insert list in a Bundle object
                        i.putExtras(b); //Insert the Bundle object in the Intent' Extras
                        startActivity(i); //Start Activity
                    }else {
                        Toast.makeText(OrderCartActivity.this, "you cant change total price less than actual amount please reset it", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        findViewById(R.id.btn_oc_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderCartActivity.this, OrderCustomerActivity.class));
            }
        });
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderCartActivity.this, HomeScreenActivity.class));
            }
        });
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetEditTextValues();
                spinnerType.setSelection(0);
                if (!orderDetailsList.isEmpty()) {

                    spinnerType.setSelection(0);

                    orderDetailsList.clear();
                    setAdapterValue(orderDetailsList);
                }
            }
        });
        String customerName=((Customer)db.getCustomer(customerId)).getName();
        if(!customerName.isEmpty()) {
            textCustomerName.setText(customerName);
        }



    }

    private void AddItemToCart(final Long selectedItemId, final String selectedItemName, final Long serviceTypeId, final Long quantity, final String itemServiceType, Double itemPrice) {
        OrderDetails odObject = new OrderDetails();
        if(edtUnitPrice.getText() != null && !edtUnitPrice.getText().toString().trim().equalsIgnoreCase(""))
        {
            itemPrice = Double.valueOf(edtUnitPrice.getText().toString().trim());
        }
        odObject.setItemId(selectedItemId);
        odObject.setItemName(selectedItemName);
        odObject.setServiceId(serviceTypeId);
        odObject.setQty(quantity);
        odObject.setServiceTypeName(itemServiceType);
        odObject.setPrice(itemPrice * quantity);
        odObject.setUnitPrice(itemPrice);
        if(adapter != null && !adapter.isEmpty()) {
            int listCount = adapter.getCount();
            List<OrderDetails> orderDetailsList1 = new ArrayList<OrderDetails>();
            for (int count = 0; count < listCount; count++) {
                OrderDetails od = (OrderDetails) adapter.getItem(count);
                orderDetailsList1.add(od);
            }

            orderDetailsList = orderDetailsList1;
        }
        orderDetailsList.add(odObject);
        setAdapterValue(orderDetailsList);
        resetEditTextValues();
    }

    private void resetEditTextValues() {
        edtQty.setText("");
        edtUnitPrice.setText("");
      //  spinnerType.setEnabled(true);
        autoEdtItem.setText("");
        // spinnerType.setSelection(0);
      /*  spinnerItem.setSelection(0);*/
    }

    private void setAdapterValue(List<OrderDetails> orderItemsList) {
          /*set Adapter for list items*/
        adapter = new OrderCartListAdapter(this, R.layout.order_cart_item, orderItemsList);

        cartItemList.setAdapter(adapter);

        registerForContextMenu(cartItemList);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}