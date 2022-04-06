package com.example.lms.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.lms.MainActivity;
import com.example.lms.R;
import com.example.lms.models.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppConfig {

    private SharedPreferences settings;
    private Context context;

    private String username;
    private String password;
    private Long userId;
    private Long routCode;
    private String routName;

    public AppConfig(Context context) {

        this.context = context;
    }

    public void updateValue(String key, Object value) {
        getAppPreference();
        SharedPreferences.Editor editor = settings.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + value.getClass().getSimpleName());
        }
        editor.commit();
    }

    private void getAppPreference() {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /*get preferences*/
    public SharedPreferences getPreferenceValue() {
        return settings;
    }

    /* stored config values */

    public Long getUserId() {
        Long userId=0l;
        getAppPreference();
        if(settings.contains("UserId")) {
            userId = settings.getLong("UserId", 0);
        }
        return userId;
    }

    public void setUserId(Long userId) {
        getAppPreference();
        updateValue("UserId", userId);
    }

    public void setLoggedIn(Boolean status)
    {
        getAppPreference();
        updateValue("isLoggedIn", status);
    }

    public Boolean isLoggedIn()
    {
        getAppPreference();
        return settings.getBoolean("isLoggedIn",false);
    }

    public void setIsManager(Boolean status)
    {
        getAppPreference();
        updateValue("isManager", status);
    }

    public Boolean isManager()
    {
        getAppPreference();
        return settings.getBoolean("isManager",false);
    }

    public String getUsername() {
        getAppPreference();
        return settings.getString("Username", "Guest");
    }

    public void setUsername(String username) {
        getAppPreference();
        updateValue("Username", username);
    }
    public boolean getBillCancellation() {
        getAppPreference();
        return settings.getBoolean("billCancellation", false);
    }

    public void setBillCancellation(Boolean isAllowed) {
        getAppPreference();
        updateValue("billCancellation", isAllowed);
    }
    public Long getRoutCode() {
        getAppPreference();
        return settings.getLong("routCode", 0);

}

    public void setRoutCode(Long routCode) {
        getAppPreference();
        updateValue("routCode", routCode);

    }

    public String getRoutName() {
        getAppPreference();
        return settings.getString("routName", "");
    }

    public void setRoutName(String routName) {
        getAppPreference();
        updateValue("routName", routName);
    }

    public String getPassword() {
        return settings.getString("Password", "");
    }

    public void setPassword(String password) {
        updateValue("Password", password);
    }

    /*check string for null value*/
    public static boolean validateNullFields(String valueString, String defaultValue) {
        if (valueString.equals("") || valueString == null || valueString.equals(0)) {

            return false;
        } else {
            return true;
        }
    }

    /*check string for Empty TextBoxes*/
    public static boolean validateNullFields(EditText textField, String defaultValue, String errorMessage, Context pContext) {
        if (textField.getText().toString().trim().length() > 0) {
            return true;
        } else {
            if (defaultValue == null) {

                textField.requestFocus();
            } else {
                textField.setText(defaultValue);
                return true;
            }

            return false;
        }
    }

    public static boolean validateNullFields(EditText textField, String errorMessage, Context pContext) {
        if (textField.getText().toString().trim().length() > 0) {
            return true;
        } else if(errorMessage==null||errorMessage=="") {
            textField.requestFocus();
            return false;

        }else{
            textField.requestFocus();
            Toast.makeText(pContext, errorMessage, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static boolean validateNullFields(List<EditText> edtList) {
        boolean flag = true;
        for (EditText textField : edtList) {
            if (textField.getText().toString().trim().length() <= 0) {

                flag = false;
                textField.requestFocus();
                break;
            }
        }
        return flag;
    }

    /*VALIDATE dropdown*/
    public static boolean validateDropdown(Spinner spinnerItem) {
        boolean flag = true;
        if (spinnerItem.getSelectedItemId() <= 0) {
            flag = false;
            spinnerItem.requestFocus();
        }
        return flag;
    }

    public Date convertToDate(String dateString) {
        Date convertedDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try

        {
            convertedDate = format.parse(dateString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
    public Date convertToSQLDate(String dateString) {

        DateFormat inputFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inputFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String output = outputFormatter.format(date); // Output : 01/20/2012
        return date;
    }
    public Date getCurrentDate() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return convertToDate(date);
    }

    public String getCurrentTime() {
        DateTime dateTime = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
        String time = fmt.print(dateTime);
//        Calendar c = Calendar.getInstance();
//        return c.getTime().toString();
        return time;
    }

    public String getCurrentDateString() {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");


        String formatted = format1.format(cal.getTime());

        return formatted;
    }

    public void logout(Context context) {
    clearAllLoginValues();

        showConform("Do you really want to logout","Logout","No",MainActivity.class,null);

    }

    public void clearAllLoginValues() {
        setUsername("");
        setPassword("");
        setIsManager(false);
        setLoggedIn(false);
    }

    public void showConform(String message, String positiveButton, String negativeButton, final Class<?> navigationOnPositive, final Class<?> navigationOnNegative) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setTitle(R.string.app_name);
        builder1.setCancelable(true);
        builder1.setPositiveButton(positiveButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if(navigationOnPositive!=null) {
                            context.startActivity(new Intent(context, navigationOnPositive));
                        }
                    }
                });
        builder1.setNegativeButton(negativeButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if(navigationOnNegative!=null) {
                            context.startActivity(new Intent(context, navigationOnNegative));
                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void showAlert(String message, String buttonName, final Class<?> positiveNavigation) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(R.string.app_name);

        // Setting Dialog Message
        alertDialog.setMessage(message);




            // Setting OK Button
            alertDialog.setButton(buttonName, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    if(positiveNavigation!=null) {
                    context.startActivity(new Intent(context, positiveNavigation));
                    }else{
                        alertDialog.setCanceledOnTouchOutside(false);
                    }
                }
            });

        // Showing Alert Message
        alertDialog.show();

    }

    public Boolean parseBoolean(String value) {
        if (value.equals("1") || value.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public Double parseDouble(String value) {
        Double result=0.0;
        if(value.trim() != "") {
            try {
                result = Double.parseDouble(value);
            } catch (Exception ex) {
                result = 0.0;
            }
        }
        return result;
    }
    public boolean emailValidator(EditText edtEmail, String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        if(!matcher.matches())
        edtEmail.requestFocus();
        return matcher.matches();
    }

    public void exitApp() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
         builder1.setTitle(R.string.app_name);

        builder1.setMessage("Do you want to exit ?.");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                    }
                });
        builder1.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    public String getBillFormat(Long orderId) {
        String bill = null;
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        if(orderId>0) {
            Long totalQuantity = 0l;
            Double totalItemPrice=0.0;
            DatabaseHandler db = new DatabaseHandler(context);
            Settings st = db.getSettings();
            User u=db.getUser(orderId);
            String serviceType = "";
            List<OrderDetails> orderDetailsFromDb = db.orderDetailsByOrderId(orderId);

            if (!orderDetailsFromDb.isEmpty()) {
                Price itemPrice = db.searchItemPrice(orderDetailsFromDb.get(0).getServiceId(), orderDetailsFromDb.get(0).getItemId());
                serviceType = itemPrice.getServiceName();
            }
            Order orderFromDb = db.getOrder(orderId);

            bill =
                    getCenteredString(st.getCompanyaddress())+
                    getCenteredString(st.getAddress1())+
                    getCenteredString(st.getAddress2())+
                    "Salesman  : " + getUsername() + "    Route code: " + st.getRouteCode() + "\n" +
                    "---------------------------------------------\n" +
                    "                   INVOICE                   \n" +
                    "---------------------------------------------\n" +
                    " Bill No         : " + addOrderPrefix(orderFromDb.getOrderId()) + "               \n" +
                    " Date            : " + formattedDate + "    \n" +
                    " Service         : " + serviceType + "      \n" +
                    " Customer        : " + orderFromDb.getCustomer().getName() + "                       \n" +
                    " Mobile          : " + orderFromDb.getCustomer().getMobileNo() + "             \n" +
                    " Telephone       : " + orderFromDb.getCustomer().getTelephoneNo() + "             \n" +
                    "---------------------------------------------\n" +
                    " Item             Qty      Price    Amount   \n" +
                    "---------------------------------------------\n";


            for (OrderDetails item : orderDetailsFromDb) {
                totalQuantity += item.getQty();

                Price itemPrice = db.searchItemPrice(item.getServiceId(), item.getItemId());
                totalItemPrice+=item.getPrice();
      /* db.getItemName(item.getItemId());*/
                bill += " " + alignLine(itemPrice.getItemName() + "|" + item.getQty() + "|" + String.format("%.2f",item.getUnitPrice()) + "|" +  String.format("%.2f",item.getPrice())) + "\n";
            }

            bill += "---------------------------------------------\n" +
                    "                         Total     : "+String.format("%.2f",totalItemPrice)+" \n" +
                    "---------------------------------------------\n" +

                    "                     Total Qty     : " + totalQuantity + "  \n" +

                    "                     Additions     : " + orderFromDb.getAdditionalAmount() + ".0" + " \n" +
                 /*   "                     Discount      : -" + String.format("%.2f",orderFromDb.getDiscount()) + "   \n" +*/
                    "---------------------------------------------\n" +
                    " Net Amount                         " + String.format("%.2f",orderFromDb.getTotalAmount()) + " \n" +
                    "---------------------------------------------\n\n\n" +
                    "TERMS & CONDITIONS\n"+
                    "For any complaint, please contact the Manager between 9AM - 12 Noon within 1 day. The laundry is not responsible for shrinkage or fastness of colour. In case of loss or damage the laundry will be liable for payment by 10 times the cost of cleaning of the Lost or Damaged clothes. 99% removal of stains guaranteed.\n\n" +
                    "         THANK YOU FOR YOUR BUSINESS         \n"+
                    "         \"Discover the Difference\"         \n"+
                    "                                             \n";
        }
        return bill;
    }
      /*Add prefix to order Number to get a six digit order number */
    public String addOrderPrefix(Long orderId) {
        String Result="0";

        for (int i=orderId.toString().length();i<=4;i++){
            Result+="0";
        }
        return Result+orderId;
    }

    public String getCenteredString(String value)
    {
        int fullLength = 45; //full width
        int spaces = (45 - value.length())/2;
        String resultSpaces = "";
        if(spaces > 0)
        {
            for (int i=0;i<spaces;i++){
                resultSpaces+=" ";
            }
        }
        return resultSpaces +value + "\n";
    }

    public String alignLine(String oneLine){
        String[] lines=oneLine.split("[\\r\\n]+");
        String result=null;

        for(String item:lines){
            String[] l_itemArray  =item.split("\\|");


            result=l_itemArray[0]+getSpace(l_itemArray[0].length(),1)+l_itemArray[1];
            result+=getSpace(l_itemArray[1].length(), 2)+l_itemArray[2];
            result+=getSpace(l_itemArray[2].length(), 3)+l_itemArray[3];
        }
        return result;
    }

    private String getSpace(int length, int position) {
        int maxSizeWithSpace=0;
        String resultSpaces="";
        switch (position){
            case 1:
                maxSizeWithSpace=18;
                break;
            case 2:
                maxSizeWithSpace=8;
                break;
            case 3:
                maxSizeWithSpace=8;
                break;
        }
        for (int i=length;i<maxSizeWithSpace;i++){
            resultSpaces+=" ";
        }
        return resultSpaces;
    }


}

