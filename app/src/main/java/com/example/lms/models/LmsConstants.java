package com.example.lms.models;

public final class LmsConstants {

    //General terms
    public static final String KEY_ID = "id";

    // Database Version
    public static final int DATABASE_VERSION = /*1*/3;

    // Database Name
    public static final String DATABASE_NAME = "lmsdb";

    //tables
    public static final String TABLE_AREA = "lms_area";
    public static final String TABLE_BUILDING = "lms_building";
    public static final String TABLE_CUSTOMER = "lms_customer";
    public static final String TABLE_ORDER = "lms_order";
    public static final String TABLE_ORDER_DETAILS = "lms_order_details";
    public static final String TABLE_PRICE = "lms_price";
    public static final String TABLE_SETTINGS = "lms_settings";
    public static final String TABLE_USER = "lms_user";
    public static final String TABLE_ADMIN_USER = "lms_admin_user";

    // User Table Columns names
    public static final String KEY_USER_CODE = "user_code";
    public static final String KEY_NAME = "name";
    public static final String KEY_USER_POWER = "user_power";
    public static final String KEY_PIN_NO = "pin_no";
    public static final String KEY_USER = "user";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_PASSWORD = "password";

    //    Customer table Column names
    public static final String KEY_CUSTOMER_ID = "customer_id";
    public static final String KEY_CUSTOMER_NAME = "customer_name";
    public static final String KEY_AREA = "area";
    public static final String KEY_BUILDING_NAME = "building_name";
    public static final String KEY_ADDRESS1 = "address1";
    public static final String KEY_ADDRESS2 = "address2";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_TELEPHONE_NO = "telephone_no";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_COMPANY_ADDRESS = "address";

    //    Price table Column names
    public static final String KEY_SERVICE_ID = "service_id";
    public static final String KEY_ITEM_ID = "item_id";
    public static final String KEY_SERVICE_NAME = "service_name";
    public static final String KEY_ITEM_NAME = "item_name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_UNIT_PRICE = "unit_price";
    public static final String KEY_FAVOURITE = "favourite";

    //    Area table Column names
    public static final String KEY_AREA_ID = "area_id";
    public static final String KEY_AREA_NAME = "area_name";
    public static final String KEY_AREA_ID1 = "area_id1";

    //    Building table Column names
    public static final String KEY_BUILDING_ID = "building_id";
    public static final String KEY_BUILDING_ID1 = "building_id1";

    //    Order table Column names
    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_ORDER_NO = "order_no";
    public static final String KEY_ORDER_DATE = "order_date";
    public static final String KEY_ORDER_TIME = "order_time";
    public static final String KEY_TOTAL_AMOUNT = "total_amount";
    public static final String KEY_ADDITIONAL_AMOUNT = "additional_amount";
    public static final String KEY_ORDER_STATUS = "order_status";
    public static final String KEY_EXPRESS_STATUS = "express_status";
    public static final String KEY_REMARKS = "remarks";
    public static final String KEY_DISCOUNT= "discount";
    public static final String KEY_NET_VAT_AMOUNT = "net_vat_amt";

    //    Order table Column names
    public static final String KEY_QTY = "qty";
    public static final String KEY_ITEM_REMARKS = "item_remarks";

    //    Settings table Column names
    public static final String KEY_ROUTE_CODE = "route_code";
    public static final String KEY_ROUTE_NAME = "route_name";
    public static final String KEY_NO_OF_BILL_COPIES = "no_of_bill_copies";
    public static final String KEY_PRINTER_PAGE_SIZE = "printer_page_size";
    public static final String KEY_ENABLE_SETTLEMENT = "enable_settlement";
    public static final String KEY_PRICE_CHANGE = "price_change";
    public static final String KEY_ENABlE_DISCOUNT = "enable_discount";
    public static final String KEY_BILL_CANCELLATION = "bill_cancellation";

    //Activities
    public static final String ACTIVITY_MAIN_ACTIVITY = "MainActivity";

    //Vat percentage
    public static final float vatPercentage = 5;
}
