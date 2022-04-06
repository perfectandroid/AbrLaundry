package com.example.lms.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import com.example.lms.models.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.lms.models.LmsConstants.*;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context context;
    private AppConfig config = new AppConfig(context);

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_CODE + " TEXT UNIQUE,"
                + KEY_NAME + " TEXT ," + KEY_USER_POWER + " INTEGER," + KEY_PIN_NO + " TEXT UNIQUE, " + KEY_BILL_CANCELLATION + " INTEGER" + ")";

        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ROUTE_CODE + " TEXT UNIQUE," + KEY_ROUTE_NAME + " TEXT,"
                + KEY_NO_OF_BILL_COPIES + " INTEGER," + KEY_PRINTER_PAGE_SIZE + " INTEGER ," + KEY_ENABLE_SETTLEMENT + " BOOLEAN,"
                + KEY_PRICE_CHANGE + " INTEGER," + KEY_ENABlE_DISCOUNT + " TEXT,"
                + KEY_COMPANY + " TEXT," + KEY_COMPANY_ADDRESS + " TEXT,"
                + KEY_ADDRESS1 + " TEXT," + KEY_ADDRESS2 + " TEXT)";

        String CREATE_BUILDING_TABLE = "CREATE TABLE " + TABLE_BUILDING + "("
                + KEY_BUILDING_ID + " INTEGER PRIMARY KEY," + KEY_BUILDING_NAME + " TEXT UNIQUE,"
                + KEY_BUILDING_ID1 + " INTEGER"
                + ")";

        String CREATE_AREA_TABLE = "CREATE TABLE " + TABLE_AREA + "("
                + KEY_AREA_ID + " INTEGER PRIMARY KEY," + KEY_AREA_NAME + " TEXT UNIQUE,"
                + KEY_AREA_ID1 + " INTEGER"
                + ")";
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMER + "("
                + KEY_CUSTOMER_ID + " INTEGER PRIMARY KEY," + KEY_CUSTOMER_NAME + " TEXT,"
                + KEY_AREA + " INTEGER NOT NULL," + KEY_BUILDING_ID + " INTEGER NOT NULL,"
                + KEY_ADDRESS1 + " TEXT," + KEY_ADDRESS2 + " TEXT," + KEY_MOBILE_NO + " TEXT,"
                + KEY_TELEPHONE_NO + " TEXT," + KEY_EMAIL + " TEXT," + KEY_USER + " INTEGER,"
                + " FOREIGN KEY(" + KEY_AREA + ")REFERENCES " + TABLE_AREA + "(" + KEY_AREA_ID + ")"
                + " )";


        String CREATE_PRICE_TABLE = "CREATE TABLE " + TABLE_PRICE + "("
                + KEY_ID + " INTEGER PRIMARY KEY ,"
                + KEY_SERVICE_ID + " INTEGER NOT NULL," + KEY_ITEM_ID + " INTEGER NOT NULL,"
                + KEY_SERVICE_NAME + " TEXT," + KEY_ITEM_NAME + " TEXT," + KEY_PRICE + " INTEGER,"
                + KEY_FAVOURITE + " TEXT "
                + " )";

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER + "("
                + KEY_ORDER_ID + " INTEGER PRIMARY KEY, "  + KEY_ORDER_NO + " TEXT,"
                + KEY_ORDER_DATE + " DATE," + KEY_ORDER_TIME + " TEXT," + KEY_CUSTOMER_ID + " INTEGER,"
                + KEY_TOTAL_AMOUNT + " INTEGER," + KEY_ADDITIONAL_AMOUNT + " INTEGER,"
                + KEY_ORDER_STATUS + " INTEGER," + KEY_EXPRESS_STATUS + " TEXT," + KEY_REMARKS + " TEXT,"
                + KEY_USER + " INTEGER," + KEY_DISCOUNT + " INTEGER,"
                + LmsConstants.KEY_NET_VAT_AMOUNT +" INTEGER"
                + ")";
        String CREATE_ORDER_DETAIL_TABLE = "CREATE TABLE " + TABLE_ORDER_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ORDER_ID + " INTEGER NOT NULL," + KEY_ITEM_ID + " INTEGER NOT NULL,"
                + KEY_USER + " INTEGER,"
                + KEY_SERVICE_ID + " INTEGER NOT NULL," + KEY_QTY + " INTEGER," + KEY_PRICE + " INTEGER,"
                + KEY_ITEM_REMARKS + " TEXT," + KEY_UNIT_PRICE + " INTEGER"
                + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_SETTINGS_TABLE);
        db.execSQL(CREATE_BUILDING_TABLE);
        db.execSQL(CREATE_AREA_TABLE);
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_PRICE_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_ORDER_DETAIL_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        // Create tables again
        onCreate(db);
    }


    public void isAdminUserTableExists()
    {
        Boolean isExist = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_ADMIN_USER+"'", null);
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                    cursor.close();
                    isExist = true;
                }
                cursor.close();
            }
            if(!isExist) {
                createAdminUserTable(db);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void createAdminUserTable(SQLiteDatabase db)
    {
        String CREATE_ADMIN_USER_TABLE = "CREATE TABLE " + TABLE_ADMIN_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_NAME + " TEXT," + KEY_PASSWORD + " TEXT)";
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN_USER);
        db.execSQL(CREATE_ADMIN_USER_TABLE);
        addAdminUser(db);
    }

    // Adding new ADMIN USER
    public Long addAdminUser(SQLiteDatabase db) {
        Long userId = null;
        ContentValues values = new ContentValues();
        try {
            values.put(KEY_USER_NAME, "admin");
            values.put(KEY_PASSWORD, "1111");
            Cursor cur = getLastInsertedId(db);
            db.insertOrThrow(TABLE_ADMIN_USER, null, values);
            userId = cur.getLong(0);
            cur.close();
        } catch (SQLiteConstraintException ex) {
            if (ex.getMessage().contains("may not be NULL")) {
                Toast.makeText(context, "Some required fields are empty in customer file", Toast.LENGTH_SHORT).show();
            } else if (ex.getMessage().contains("is not unique")) {
                Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } finally {
            db.close();
            return userId;
        }
    }

    //validate user
    public Boolean validateAdminUser(String userName, String pin_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_VALIDATE_USER_QUERY = "SELECT * FROM " + TABLE_ADMIN_USER + " WHERE " + KEY_USER_NAME + " = '" + userName + "' AND " + KEY_PASSWORD + " = '" + pin_code + "'";
        Cursor cursor = db.rawQuery(SQL_VALIDATE_USER_QUERY, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    // Updating user
    public Long updateAdminPassword(String password,String oldPassword) {
        SQLiteDatabase db = getWritableDatabase();
        int userId = 0;
        Boolean isExist = validateAdminUser("admin", oldPassword.trim());
        if(!isExist)
            return 0l;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PASSWORD, password.trim());
            // updating row
            userId = db.update(TABLE_ADMIN_USER, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(1)});
        } catch (SQLiteConstraintException exp) {
            if (exp.getMessage().contains("may not be NULL")) {
                Toast.makeText(context, "Some required fields are empty in customer file", Toast.LENGTH_SHORT).show();
            } else if (exp.getMessage().contains("is not unique")) {
                Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, exp.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } finally {
            db.close();
            return Long.valueOf(userId);
        }
    }

    // Adding new USER
    public Long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long userId = null;
        ContentValues values = new ContentValues();
        try {
            values.put(KEY_USER_CODE, user.getUserCode());
            values.put(KEY_NAME, user.getName().toUpperCase());
            values.put(KEY_USER_POWER, user.getUserPower());
            values.put(KEY_PIN_NO, user.getPinNo());
            values.put(KEY_BILL_CANCELLATION, user.getBillCancellation());
            db.insertOrThrow(TABLE_USER, null, values);
            Cursor cur = getLastInsertedId(db);
            userId = cur.getLong(0);
            cur.close();
        } catch (SQLiteConstraintException ex) {
            if (ex.getMessage().contains("may not be NULL")) {
                Toast.makeText(context, "Some required fields are empty in customer file", Toast.LENGTH_SHORT).show();
            } else if (ex.getMessage().contains("is not unique")) {
                Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();

            }


        } finally {
            db.close();
            return userId;
        }

    }


    //USER OPERATIONS
    // Getting user
    public User getUser(Long id) {
        SQLiteDatabase db = getReadableDatabase();
        User user = new User();
        try {
            Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID,
                            KEY_USER_CODE, KEY_NAME, KEY_USER_POWER, KEY_PIN_NO, KEY_BILL_CANCELLATION}, KEY_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                user.setId(Long.parseLong(cursor.getString(0)));
                user.setUserCode(cursor.getString(1));
                user.setName(cursor.getString(2));
                user.setUserPower(cursor.getString(3));
                user.setPinNo(cursor.getString(4));
                user.setBillCancellation(config.parseBoolean(cursor.getString(5)));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // Getting All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Long.parseLong(cursor.getString(0)));
                user.setUserCode(cursor.getString(1));
                user.setName(cursor.getString(2));
                user.setUserPower(cursor.getString(3));
                user.setPinNo(cursor.getString(4));
                user.setBillCancellation(config.parseBoolean(cursor.getString(5)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    // Updating user
    public Long updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        int userId = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_CODE, user.getUserCode());
            values.put(KEY_NAME, user.getName().toUpperCase());
            values.put(KEY_USER_POWER, user.getUserPower());
            values.put(KEY_PIN_NO, user.getPinNo());
            values.put(KEY_BILL_CANCELLATION, user.getBillCancellation());
            // updating row
            userId = db.update(TABLE_USER, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(user.getId())});
        } catch (SQLiteConstraintException exp) {
            if (exp.getMessage().contains("may not be NULL")) {
                Toast.makeText(context, "Some required fields are empty in customer file", Toast.LENGTH_SHORT).show();
            } else if (exp.getMessage().contains("is not unique")) {
                Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, exp.getMessage(), Toast.LENGTH_SHORT).show();

            }


        } finally {
            db.close();
            return Long.valueOf(userId);
        }
    }

    // Deleting user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // Getting user Count
    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    //validate user
    public User validateUser(String user_code, String pin_code) {
        SQLiteDatabase db = this.getReadableDatabase();

        String SQL_VALIDATE_USER_QUERY = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_USER_CODE + " = '" + user_code + "' AND " + KEY_PIN_NO + " = '" + pin_code + "'";

        Cursor cursor = db.rawQuery(SQL_VALIDATE_USER_QUERY, null);
        User user = new User();
        if (cursor != null && cursor.moveToFirst()) {


            user.setId(Long.parseLong(cursor.getString(0)));
            user.setUserCode(cursor.getString(1));
            user.setName(cursor.getString(2));
            user.setUserPower(cursor.getString(3));
            user.setPinNo(cursor.getString(4));
            user.setBillCancellation(config.parseBoolean(cursor.getString(5)));
        }
        cursor.close();
        db.close();
        return user;
    }

    //SETTINGS OPERATIONS
    // Adding new Settings
    public Long addSettings(Settings settings) {
        Long rowUpdated = 0l;
        SQLiteDatabase db = getWritableDatabase();
        Settings checkSettings = getSettings();
        try {
            if (checkSettings == null || checkSettings.getEnableDiscount() == null) {
                ContentValues values = new ContentValues();
                values.put(KEY_ROUTE_CODE, settings.getRouteCode());
                values.put(KEY_ROUTE_NAME, settings.getRouteName().toUpperCase());
                values.put(KEY_NO_OF_BILL_COPIES, settings.getNoOfBillCopies());
                values.put(KEY_PRINTER_PAGE_SIZE, settings.getPrinterPageSize());
                values.put(KEY_ENABLE_SETTLEMENT, settings.getEnableSettlement());
                values.put(KEY_PRICE_CHANGE, settings.getPriceChange());
                values.put(KEY_ENABlE_DISCOUNT, settings.getEnableDiscount());
                values.put(KEY_COMPANY, settings.getCompanyname().toUpperCase());
                values.put(KEY_COMPANY_ADDRESS, settings.getCompanyaddress());
                values.put(KEY_ADDRESS1, settings.getAddress1());
                values.put(KEY_ADDRESS2, settings.getAddress2());

                rowUpdated = db.insert(TABLE_SETTINGS, null, values);
            } else {
                settings.setId(checkSettings.getId());
                rowUpdated = updateSettings(settings);
            }
        } catch (SQLiteConstraintException ex) {
            Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();

            return rowUpdated;
        }
    }

    public Long updateSettings(Settings settings) {
        int rowsUpdated = 0;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ROUTE_CODE, settings.getRouteCode());
            values.put(KEY_ROUTE_NAME, settings.getRouteName().toUpperCase());
            values.put(KEY_NO_OF_BILL_COPIES, settings.getNoOfBillCopies());
            values.put(KEY_PRINTER_PAGE_SIZE, settings.getPrinterPageSize());
            values.put(KEY_ENABLE_SETTLEMENT, settings.getEnableSettlement());
            values.put(KEY_PRICE_CHANGE, settings.getPriceChange());
            values.put(KEY_ENABlE_DISCOUNT, settings.getEnableDiscount());
            values.put(KEY_COMPANY, settings.getCompanyname().toUpperCase());
            values.put(KEY_COMPANY_ADDRESS, settings.getCompanyaddress());
            values.put(KEY_ADDRESS1, settings.getAddress1());
            values.put(KEY_ADDRESS2, settings.getAddress2());
            rowsUpdated = db.update(TABLE_SETTINGS, values, KEY_ID + "=" + settings.getId(), null);

        } catch (Exception e) {
            rowsUpdated = 0;
        } finally {
            db.close();
        }
        return Long.valueOf(rowsUpdated);
    }

    // Getting settings
    public Settings getSettings() {
        SQLiteDatabase db = getReadableDatabase();
        Settings settings = new Settings();
        try {
            Cursor cursor = db.query(TABLE_SETTINGS, new String[]{KEY_ID,
                            KEY_ROUTE_CODE, KEY_ROUTE_NAME, KEY_NO_OF_BILL_COPIES, KEY_PRINTER_PAGE_SIZE, KEY_ENABLE_SETTLEMENT, KEY_PRICE_CHANGE, KEY_ENABlE_DISCOUNT, KEY_COMPANY, KEY_COMPANY_ADDRESS, KEY_ADDRESS1, KEY_ADDRESS2},
                    null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {

                try {
                    settings.setId(Long.parseLong(cursor.getString(0)));
                    settings.setRouteCode(Long.parseLong(cursor.getString(1)));
                    settings.setRouteName(cursor.getString(2).toUpperCase());
                    settings.setNoOfBillCopies(Long.parseLong(cursor.getString(3)));
                    settings.setPrinterPageSize(Long.parseLong(cursor.getString(4)));
                    settings.setEnableSettlement(config.parseBoolean(cursor.getString(5)));
                    settings.setPriceChange(Long.parseLong(cursor.getString(6)));

                    settings.setEnableDiscount(config.parseBoolean(cursor.getString(7)));
                    settings.setCompanyname(cursor.getString(8).toUpperCase());
                    settings.setCompanyaddress(cursor.getString(9));
                    settings.setAddress1(cursor.getString(10));
                    settings.setAddress2(cursor.getString(11));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
            //db.close();
        } catch (Exception ex) {
            Toast.makeText(context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
        }
        return settings;
    }

    // Getting All Settings
    public List<Settings> getAllSettings() {
        List<Settings> settingsList = new ArrayList<Settings>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Settings settings = new Settings();
                settings.setId(Long.parseLong(cursor.getString(0)));
                settings.setRouteCode(Long.parseLong(cursor.getString(1)));
                settings.setRouteName(cursor.getString(2));
                settings.setNoOfBillCopies(Long.parseLong(cursor.getString(3)));
                settings.setPrinterPageSize(Long.parseLong(cursor.getString(4)));
                settings.setEnableSettlement(config.parseBoolean(cursor.getString(5)));
                settings.setPriceChange(Long.parseLong(cursor.getString(6)));
                settings.setEnableDiscount(config.parseBoolean(cursor.getString(7)));
                settings.setCompanyname(cursor.getString(8));
                settings.setCompanyaddress(cursor.getString(9));
                settings.setAddress1(cursor.getString(10));
                settings.setAddress2(cursor.getString(11));
                settingsList.add(settings);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return settingsList;
    }


    //BUILDING OPERATIONS
    // Adding new BUILDING
    public Long addBuilding(Building building) {
        Long buildingId = 0l;
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BUILDING_NAME, building.getBuildingName().toUpperCase());
        values.put(KEY_BUILDING_ID1, building.getBuildingId1());
        try {
            db.insertOrThrow(TABLE_BUILDING, null, values);
            Cursor cur = getLastInsertedId(db);
            buildingId = cur.getLong(0);
            cur.close();
        } catch (SQLiteConstraintException ex) {
            Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
            return buildingId;
        }
    }

    // Getting BUILDING
    public Building getBuilding(long id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_BUILDING, new String[]{KEY_BUILDING_ID,
                        KEY_BUILDING_NAME, KEY_BUILDING_ID1}, KEY_BUILDING_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        Building building = new Building();
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                building = new Building(
                        Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        Long.parseLong(cursor.getString(2))
                );
            }
        }
        cursor.close();
        db.close();
        return building;
    }

    // Getting All Building
    public List<Building> getAllBuilding() {
        List<Building> buildingList = new ArrayList<Building>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BUILDING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Building building = new Building();
                building.setBuildingId(Long.parseLong(cursor.getString(0)));
                building.setBuildingName(cursor.getString(1));
                building.setBuildingId1(Long.parseLong(cursor.getString(0)));
                buildingList.add(building);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return buildingList;
    }

    //AREA OPERATIONS
    // Adding new Area
    public Long addArea(Area area) {

        Long areaId = 0l;
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        try {
            values.put(KEY_AREA_NAME, area.getAreaName().toUpperCase());
            values.put(KEY_AREA_ID1, area.getAreaId1());

            db.insertOrThrow(TABLE_AREA, null, values);

            Cursor cur = getLastInsertedId(db);
            areaId = cur.getLong(0);
            cur.close();
        } catch (SQLiteConstraintException exp) {
            Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
            return areaId;
        }
    }


    // Getting Area
    public Area getArea(long id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_AREA, new String[]{KEY_AREA_ID,
                        KEY_AREA_NAME, KEY_AREA_ID1}, KEY_AREA_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        Area area = new Area();
        if (cursor != null)
            if (cursor.moveToFirst()) {
                area.setAreaId(Long.parseLong(cursor.getString(0)));
                area.setAreaName(cursor.getString(1));
                area.setAreaId1(Long.parseLong(cursor.getString(2))
                );
            }
        cursor.close();
        db.close();
        return area;

    }

    // Getting All  Area
    public List<Area> getAllArea() {
        List<Area> areaList = new ArrayList<Area>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_AREA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Area area = new Area();
                area.setAreaId(Long.parseLong(cursor.getString(0)));
                area.setAreaName(cursor.getString(1));
                area.setAreaId1(Long.parseLong(cursor.getString(0)));
                areaList.add(area);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return areaList;
    }


    //CUSTOMER OPERATIONS
    // Adding new CUSTOMER
    public Long addCustomer(Customer customer) {

        SQLiteDatabase db = getWritableDatabase();
        Long customerId = 0l;
        db.beginTransaction();
        try {
           /* Long areaId = addArea(customer.getArea());

            Long buildingId = addBuilding(customer.getBuilding());*/
            ContentValues values = new ContentValues();
            values.put(KEY_CUSTOMER_NAME, customer.getName().toUpperCase());
            values.put(KEY_AREA, customer.getArea().getAreaId());
            values.put(KEY_BUILDING_ID, customer.getBuilding().getBuildingId());
            values.put(KEY_ADDRESS1, customer.getAddress1().toString().toUpperCase());
            values.put(KEY_ADDRESS2, customer.getAddress2().toUpperCase());
            values.put(KEY_MOBILE_NO, customer.getMobileNo());
            values.put(KEY_TELEPHONE_NO, customer.getTelephoneNo());
            values.put(KEY_EMAIL, customer.getEmail());
            if (customer.getUser() != null) {
                values.put(KEY_USER, customer.getUser().getId());
            }
            db.insertOrThrow(TABLE_CUSTOMER, null, values);
            Cursor cur = getLastInsertedId(db);
            customerId = cur.getLong(0);
            db.setTransactionSuccessful();
            cur.close();
        } catch (SQLiteConstraintException ex) {
            if (ex.getMessage().contains("may not be NULL")) {
                Toast.makeText(context, "Some required fields are empty in customer file", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
            db.close();
        }
        return customerId;
    }

    // Getting Customer
    public Customer getCustomer(Long id) {
        SQLiteDatabase db = getReadableDatabase();
        Customer customer = new Customer();
        Cursor cursorCus = db.query(TABLE_CUSTOMER, new String[]{KEY_CUSTOMER_ID,
                        KEY_CUSTOMER_NAME, KEY_AREA, KEY_BUILDING_ID,
                        KEY_ADDRESS1, KEY_ADDRESS2, KEY_MOBILE_NO, KEY_TELEPHONE_NO,
                        KEY_EMAIL, KEY_USER}, KEY_CUSTOMER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursorCus != null && cursorCus.moveToFirst()) {


            customer.setCustomerId(Long.parseLong(cursorCus.getString(0)));
            customer.setName(cursorCus.getString(1));
            if (cursorCus.getString(2) != null) {
                Long areaId = cursorCus.getLong(2);
                Area area = getArea(areaId);
                if (area != null) {
                    customer.setArea(area);
                }
            }
//            customer.setArea(getArea(Long.parseLong(cursorCus.getString(2))));
            customer.setBuilding(getBuilding(Long.parseLong(cursorCus.getString(0))));
            customer.setAddress1(cursorCus.getString(4));
            customer.setAddress2(cursorCus.getString(5));
            customer.setMobileNo(cursorCus.getString(6));
            customer.setTelephoneNo(cursorCus.getString(7));
            customer.setEmail(cursorCus.getString(8));
            if (cursorCus.getString(9) != null) {
                User user = getUser(cursorCus.getLong(9));
                customer.setUser(user);
            }

            cursorCus.close();
            db.close();
        }
        return customer;
    }

    /*search customer by giving mobile or email or name */
    public List<Customer> searchCustomer(String searchKey) {
        List<Customer> customerList = new ArrayList<Customer>();
        Area area = new Area();
        Building building = new Building();
       /*Select all customer Query*/
        String searchQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_CUSTOMER_NAME + " LIKE '%" + searchKey + "%' OR " + KEY_EMAIL + " LIKE '%" + searchKey + "%' OR " + KEY_MOBILE_NO + " LIKE '%" + searchKey + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(searchQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setCustomerId(cursor.getLong(0));
                customer.setName(cursor.getString(1));
                area.setAreaId(cursor.getLong(2));
                customer.setArea(area);
                building.setBuildingId(cursor.getLong(3));
                customer.setBuilding(building);
                customer.setAddress1(cursor.getString(4));
                customer.setAddress2(cursor.getString(5));
                customer.setMobileNo(cursor.getString(6));
                customer.setTelephoneNo(cursor.getString(7));
                customer.setEmail(cursor.getString(8));
                if (cursor.getString(9) != null) {
                    User user = getUser(cursor.getLong(9));
                    customer.setUser(user);
                }
                customerList.add(customer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customerList;
    }

    // Getting All Customer
    public List<Customer> getAllCustomer() {
        List<Customer> customerList = new ArrayList<Customer>();

        SQLiteDatabase db = getWritableDatabase();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setCustomerId(Long.valueOf(cursor.getString(0)));
                    customer.setName(cursor.getString(1));
                    if (cursor.getString(2) != null) {
                        Area area = getArea(cursor.getLong(2));
                        customer.setArea(area);
                    }
                    if (cursor.getString(3) != null) {
                        Building building = getBuilding(cursor.getLong(3));
                        customer.setBuilding(building);
                    }
                    customer.setAddress1(cursor.getString(4));
                    customer.setAddress2(cursor.getString(5));
                    customer.setMobileNo(cursor.getString(6));
                    customer.setTelephoneNo(cursor.getString(7));
                    customer.setEmail(cursor.getString(8));
                    if (cursor.getString(9) != null) {
                        User user = getUser(cursor.getLong(9));
                        customer.setUser(user);
                    }
                    customerList.add(customer);
                } while (cursor.moveToNext());

            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return customerList;
    }

    //PRICE OPERATIONS
    // Adding new price
    public Long addPrice(Price price) {
        SQLiteDatabase db = getWritableDatabase();
        Long priceId = null;
        ContentValues values = new ContentValues();

        Price priceCheck = searchItemPrice(price.getServiceId(), price.getItemId());

        try {
            values.put(KEY_SERVICE_ID, price.getServiceId());
            values.put(KEY_ITEM_ID, price.getItemId());
            values.put(KEY_SERVICE_NAME, price.getServiceName().toUpperCase());
            values.put(KEY_ITEM_NAME, price.getItemName().toUpperCase());
            values.put(KEY_PRICE, price.getPrice());
            values.put(KEY_FAVOURITE, price.getFavourite());
            if (priceCheck == null) {

                db.insertOrThrow(TABLE_PRICE, null, values);

                Cursor cur = getLastInsertedId(db);
                priceId = cur.getLong(0);
                cur.close();
            } else {
                priceId = priceCheck.getId();
                updatePrice(priceCheck.getId(), price.getPrice());
            }
        } catch (SQLiteConstraintException exp) {
            if (exp.getMessage().contains("may not be NULL")) {
                Toast.makeText(context, "Some required fields are empty in customer file", Toast.LENGTH_SHORT).show();
            } else if (exp.getMessage().contains("may not be NULL")) {
                //columns item_id, service_id are not unique (code 19)
            } else {
                Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();

            }
            Toast.makeText(context, "This records already exist", Toast.LENGTH_SHORT).show();

        } finally {
            //db.close();
            return priceId;
        }

    }

    // Getting Price
    public Price getPrice(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRICE, new String[]{KEY_ID, KEY_SERVICE_ID,
                        KEY_ITEM_ID, KEY_SERVICE_NAME, KEY_ITEM_NAME,
                        KEY_PRICE, KEY_FAVOURITE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Price price = new Price(
                Long.parseLong(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                Long.parseLong(cursor.getString(2)),
                cursor.getString(3),
                cursor.getString(4), Double.parseDouble(cursor.getString(5)),
                cursor.getString(6));
        cursor.close();
        //db.close();
        return price;
    }

//    // Getting Price
//    public Price getModifiedPrice(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_ORDER_DETAILS, new String[]{KEY_ID, KEY_SERVICE_ID,
//                        KEY_ITEM_ID,
//                        KEY_PRICE, KEY_FAVOURITE}, KEY_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//        Price price = new Price();
//        price.set
//
//        Price price = new Price(
//                Long.parseLong(cursor.getString(0)),
//                Long.parseLong(cursor.getString(1)),
//                Long.parseLong(cursor.getString(2)),
//                Long.parseLong(cursor.getString(2)),
//                cursor.getString(3),
//                cursor.getString(4), Double.parseDouble(cursor.getString(5)),
//                cursor.getString(6));
//        cursor.close();
//        return price;
//    }


    // search Price of an item
    public Price searchItemPrice(Long serviceId, Long itemId) {
        SQLiteDatabase db = getReadableDatabase();
        Price price = null;
        Cursor cursor = db.query(TABLE_PRICE, new String[]{KEY_ID, KEY_SERVICE_ID,
                        KEY_ITEM_ID, KEY_SERVICE_NAME, KEY_ITEM_NAME,
                        KEY_PRICE, KEY_FAVOURITE}, KEY_SERVICE_ID + "=? AND " + KEY_ITEM_ID + "=?",
                new String[]{Long.toString(serviceId), Long.toString(itemId)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            price = new Price(
                    Long.parseLong(cursor.getString(0)),
                    Long.parseLong(cursor.getString(1)),
                    Long.parseLong(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4),
                    Double.parseDouble(cursor.getString(5)),
                    cursor.getString(6));
        }
        cursor.close();
        //db.close();
        return price;
    }

    // get all service types
    public List<Price> getAllServiceTypes() {
        SQLiteDatabase db = getReadableDatabase();
        List<Price> priceList = new ArrayList<Price>();

        String SQL_SERVICE_TYPES = "SELECT DISTINCT  " + KEY_SERVICE_NAME + "," + KEY_SERVICE_ID + " FROM " + TABLE_PRICE + " group by " + KEY_SERVICE_NAME + "";
        Cursor cursor = db.rawQuery(SQL_SERVICE_TYPES, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Price price = new Price();
                price.setServiceName(cursor.getString(0));
                price.setServiceId(Long.parseLong(cursor.getString(1)));
                priceList.add(price);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return priceList;
    }

    // get all item types
    public List<Price> getAllItemType() {
        SQLiteDatabase db = getReadableDatabase();
        List<Price> priceList = new ArrayList<Price>();

        String SQL_SERVICE_TYPES = "SELECT DISTINCT  " + KEY_ITEM_NAME + "," + KEY_ITEM_ID + " FROM " + TABLE_PRICE + " group by " + KEY_ITEM_NAME + "";
        Cursor cursor = db.rawQuery(SQL_SERVICE_TYPES, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Price price = new Price();
                price.setItemName(cursor.getString(0));
                price.setItemId(Long.parseLong(cursor.getString(1)));
                priceList.add(price);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return priceList;
    }

    // Getting All Price
    public List<Price> getAllPrice() {
        List<Price> priceList = new ArrayList<Price>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRICE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Price price = new Price();
                price.setId(Long.valueOf(cursor.getString(0)));
                price.setServiceId(Long.valueOf(cursor.getString(1)));
                price.setItemId(Long.valueOf(cursor.getString(2)));
                price.setServiceName(cursor.getString(3));
                price.setItemName(cursor.getString(4));
                price.setPrice(Double.valueOf(cursor.getString(5)));
                price.setFavourite(cursor.getString(6));
                priceList.add(price);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return priceList;
    }

    /*UPDATE Price*/
    public int updatePrice(Long priceId, Double price) {
        int rowUpdated = 0;
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRICE, price);

        rowUpdated = db.update(TABLE_PRICE, values, KEY_ID + "=" + priceId, null);
        return rowUpdated;
    }

    //ORDER OPERATIONS
    // Adding new ORDER and return order id
    public Long addOrder(Order order) {
        Long orderId = 0l;

        ContentValues values = new ContentValues();

        List<Order> orders = getAllOrder();

        if(orders.size() == 0)
        {
            orderId = this.getSettings().getPrinterPageSize();
        }
        else
        {
            orderId = orders.get(orders.size() - 1).getOrderId() + 1;
        }

        values.put(KEY_ORDER_ID, orderId);
        values.put(KEY_ORDER_NO, order.getOrderNo());
        values.put(KEY_ORDER_DATE, config.getCurrentDateString());
        values.put(KEY_ORDER_TIME, order.getOrderTime());
        values.put(KEY_CUSTOMER_ID, order.getCustomer().getCustomerId());
        values.put(KEY_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(KEY_ADDITIONAL_AMOUNT, order.getAdditionalAmount());
        values.put(KEY_ORDER_STATUS, order.getOrderStatus());
        values.put(KEY_EXPRESS_STATUS, order.getExpressStatus());
        values.put(KEY_REMARKS, order.getRemarks().toUpperCase());
        values.put(KEY_NET_VAT_AMOUNT, order.getNetVatAmt());
        if (order.getUser() != null) {
            values.put(KEY_USER, order.getUser().getId());
        }
        values.put(KEY_DISCOUNT, order.getDiscount());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_ORDER, null, values);

        //db.close();
        return orderId;
    }

    // Getting Order
    public Order getOrder(long orderId) {
        SQLiteDatabase db = getReadableDatabase();
        Order order = new Order();
        Cursor cursor = db.query(TABLE_ORDER, new String[]{KEY_ORDER_ID, KEY_ORDER_NO,
                        KEY_ORDER_DATE, KEY_ORDER_TIME, KEY_CUSTOMER_ID,
                        KEY_TOTAL_AMOUNT, KEY_ADDITIONAL_AMOUNT, KEY_ORDER_STATUS,
                        KEY_EXPRESS_STATUS, KEY_REMARKS, KEY_USER, KEY_DISCOUNT, KEY_NET_VAT_AMOUNT}, KEY_ORDER_ID + "=?",
                new String[]{String.valueOf(orderId)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            order.setOrderId(Long.parseLong(cursor.getString(0)));
            order.setOrderNo(config.addOrderPrefix(cursor.getLong(0)));
            order.setOrderDate(cursor.getString(2));
            order.setOrderTime(cursor.getString(3));
            order.setCustomer(getCustomer(Long.parseLong(cursor.getString(4))));
            order.setTotalAmount(Double.parseDouble(cursor.getString(5)));
            Long additionalAmount = Math.round(Double.parseDouble(cursor.getString(6)));
            order.setAdditionalAmount(additionalAmount);
            order.setOrderStatus(Long.parseLong(cursor.getString(7)));
            order.setExpressStatus(cursor.getString(8));
            order.setRemarks(cursor.getString(9));
            if (cursor.getString(10) != null) {
                order.setUser(getUser(cursor.getLong(10)));
            }
            order.setDiscount(cursor.getDouble(11));
            order.setNetVatAmt( cursor.getInt(12));
            order.setOrderDetails(orderDetailsByOrderId(Long.parseLong(cursor.getString(0))));
        }
        cursor.close();
        //db.close();
        return order;
    }

    // Getting All Order
    public List<Order> getAllOrder() {
        List<Order> orderList = new ArrayList<Order>();
        // Select All Query
        String selectQuery = "SELECT order_id, order_no, order_date, order_time, customer_id, total_amount, additional_amount, order_status, express_status, remarks,user, discount from " + TABLE_ORDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderId(Long.valueOf(cursor.getString(0)));
                order.setOrderNo(config.addOrderPrefix(cursor.getLong(0)));
                order.setOrderDate(cursor.getString(2));
                order.setOrderTime(cursor.getString(3));
                if (cursor.getString(4) != null) {
                    Long customerId = cursor.getLong(4);
                    order.setCustomer(getCustomer(customerId));
                    order.setCustomerId(customerId);
                }
                order.setTotalAmount(Double.valueOf(cursor.getString(5)));
                Long additionalAmount = Math.round(Double.parseDouble(cursor.getString(6)));
                order.setAdditionalAmount(additionalAmount);
                order.setOrderStatus(Long.valueOf(cursor.getString(7)));
                order.setExpressStatus(cursor.getString(8));
                order.setRemarks(cursor.getString(9));
                if (cursor.getString(10) != null) {
                    order.setUser(getUser(cursor.getLong(10)));
                }
                order.setDiscount(cursor.getDouble(11));
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return orderList;
    }

    /*Search order by providing date*/
    public List<Order> getOrdersByDate(String fromDateTS, String toDateTS) {
        /*if(fromDateTS.equalsIgnoreCase(toDateTS)) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime dt = formatter.parseDateTime(fromDateTS);
            dt = dt.minusDays(1);
            fromDateTS  = formatter.print(dt);
        }*/
        List<Order> orderList = new ArrayList<Order>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_ORDER+" ORDER BY "+KEY_ORDER_ID+ " DESC";

        String selectQuery =
                fromDateTS.equalsIgnoreCase(toDateTS) ?
                        "SELECT  * FROM " + TABLE_ORDER + " WHERE " + KEY_ORDER_DATE + " = '" + fromDateTS + "'" :
                        "SELECT  * FROM " + TABLE_ORDER + " WHERE " + KEY_ORDER_DATE + " BETWEEN '" + fromDateTS + "' AND '" + toDateTS + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order();
                    order.setOrderId(Long.valueOf(cursor.getString(0)));
                    order.setOrderNo(config.addOrderPrefix(cursor.getLong(0)));
                    order.setOrderDate(cursor.getString(2));
                    order.setOrderTime(cursor.getString(3));
                    if (cursor.getString(4) != null) {
                        Long customerId = cursor.getLong(4);
                        order.setCustomer(getCustomer(customerId));
                        order.setCustomerId(customerId);
                    }
                    order.setTotalAmount(Double.valueOf(cursor.getString(5)));
                    Long additionalAmount = Math.round(Double.parseDouble(cursor.getString(6)));
                    order.setAdditionalAmount(additionalAmount);
                    order.setOrderStatus(Long.valueOf(cursor.getString(7)));
                    order.setExpressStatus(cursor.getString(8));
                    order.setRemarks(cursor.getString(9));
                    if (cursor.getString(10) != null) {
                        order.setUser(getUser(cursor.getLong(10)));
                    }
                    order.setDiscount(cursor.getDouble(11));
                    order.setNetVatAmt(cursor.getDouble(12));
                    orderList.add(order);

                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return orderList;

    }


    /*Search order by providing date*/
    public Integer clearOrderByDate(String fromDateTS, String toDateTS) {

        //int date = Integer.valueOf(fromDateTS.split("-")[2]) - 1;
        //fromDateTS = (fromDateTS.substring(0, fromDateTS.length() - 2)) + date;
        String selectQuery = "SELECT  count(*) FROM " + TABLE_ORDER + " WHERE " + KEY_ORDER_DATE + " BETWEEN '" + fromDateTS + "' AND '" + toDateTS + "'";

        //String deleteOrderQuery = "DELETE  * FROM " + TABLE_ORDER + " WHERE " + KEY_ORDER_DATE + " BETWEEN '" + fromDateTS + "' AND '" + toDateTS + "'";
        //String deleteOrderDetailsQuery = "DELETE  * FROM " + TABLE_ORDER_DETAILS + " WHERE " + KEY_ORDER_ID + " IN(SELECT  " + KEY_ID + " FROM " + TABLE_ORDER + " WHERE " + KEY_ORDER_DATE + " BETWEEN '" + fromDateTS + "' AND '" + toDateTS + "')";

        SQLiteDatabase db = this.getWritableDatabase();
        Integer count = 0;
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

        if(count > 0) {
            try {

                db.delete(TABLE_ORDER_DETAILS, KEY_ORDER_ID + " IN(SELECT  " + KEY_ID + " FROM " + TABLE_ORDER + " WHERE " + KEY_ORDER_DATE + " BETWEEN '" + fromDateTS + "' AND '" + toDateTS + "')", null);
                db.delete(TABLE_ORDER, KEY_ORDER_DATE + " BETWEEN '" + fromDateTS + "' AND '" + toDateTS + "'", null);
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                count = 0;
            }
        }
        //db.close();

        return count;

    }


    /*get All Order*/
    public List<Order> getAllOrders() {

        List<Order> orderList = new ArrayList<Order>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER + " ORDER BY " + KEY_ORDER_ID + " DESC";
        // String selectQuery = "SELECT  * FROM " + TABLE_ORDER+" WHERE "+KEY_ORDER_DATE+" >= Datetime('"+fromDateTS.toString()+"') AND "+KEY_ORDER_DATE+"<= Datetime('"+toDateTS.toString()+"')";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order();
                    order.setOrderId(Long.valueOf(cursor.getString(0)));
                    order.setOrderNo(config.addOrderPrefix(cursor.getLong(0)));
                    //                order.setOrderDate(cursor.getString(2));
                    order.setOrderTime(cursor.getString(3));
                    if (cursor.getString(4) != null) {
                        Long customerId = cursor.getLong(4);
                        order.setCustomer(getCustomer(customerId));
                        order.setCustomerId(customerId);
                    }
                    order.setTotalAmount(Double.valueOf(cursor.getString(5)));
                    Long additionalAmount = Math.round(Double.parseDouble(cursor.getString(6)));
                    order.setAdditionalAmount(additionalAmount);
                    order.setOrderStatus(Long.valueOf(cursor.getString(7)));
                    order.setExpressStatus(cursor.getString(8));
                    order.setRemarks(cursor.getString(9));
                    if (cursor.getString(10) != null) {
                        order.setUser(getUser(cursor.getLong(10)));
                    }
                    order.setDiscount(cursor.getDouble(11));
                    orderList.add(order);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return orderList;

    }

    /*get order by customer*/
    public List<Order> searchOrderByCustomer(Long customerId) {
        List<Order> orderList = new ArrayList<Order>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER + " WHERE " + KEY_CUSTOMER_ID + " = " + customerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderId(cursor.getLong(0));
                order.setOrderNo(config.addOrderPrefix(cursor.getLong(0)));
                order.setOrderDate(cursor.getString(2));
                order.setOrderTime(cursor.getString(3));
                if (cursor.getString(4) != null) {
                    Long customerId1 = cursor.getLong(4);
                    order.setCustomer(getCustomer(customerId1));
                    order.setCustomerId(customerId1);
                }
                order.setTotalAmount(Double.valueOf(cursor.getString(5)));
                Long additionalAmount = Math.round(Double.parseDouble(cursor.getString(6)));
                order.setAdditionalAmount(additionalAmount);
                order.setOrderStatus(Long.valueOf(cursor.getString(7)));
                order.setExpressStatus(cursor.getString(8));
                order.setRemarks(cursor.getString(9));
                if (cursor.getString(10) != null) {
                    order.setUser(getUser(cursor.getLong(10)));
                }
                order.setDiscount(cursor.getDouble(11));
                order.setNetVatAmt( cursor.getDouble(12));
                orderList.add(order);
            } while (cursor.moveToNext());

        }
        cursor.close();
        //db.close();
        return orderList;
    }

    /*UPDATE Order status by order*/
    public int updateOrderStatus(Long orderId, Long statusId) {
        int rowUpdated = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_STATUS, statusId);

        rowUpdated = db.update(TABLE_ORDER, values, KEY_ORDER_ID + "=" + orderId, null);
        return rowUpdated;
    }

    // ORDER DETAILS OPERATIONS
    // Adding new ORDER DETAILS
    public Long addOrderDetails(OrderDetails orderDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, orderDetails.getOrderId());
        values.put(KEY_ITEM_ID, orderDetails.getItemId());
        values.put(KEY_SERVICE_ID, orderDetails.getServiceId());
        values.put(KEY_QTY, orderDetails.getQty());
        values.put(KEY_PRICE, orderDetails.getPrice());
        values.put(KEY_UNIT_PRICE, orderDetails.getUnitPrice());

        if (orderDetails.getUser() != null) {
            values.put(KEY_USER, orderDetails.getUser().getId());
        }

        db.insert(TABLE_ORDER_DETAILS, null, values);

        final String GET_ORDER_ID_QUERY = "SELECT last_insert_rowid()";
        Cursor cur = db.rawQuery(GET_ORDER_ID_QUERY, null);
        cur.moveToFirst();
        Long orderDetailId = cur.getLong(0);
        cur.close();
        //db.close();
        return orderDetailId;

    }

    // Getting Order
    public OrderDetails getOrderDetails(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDER_DETAILS, new String[]{KEY_ID, KEY_ORDER_ID,
                        KEY_ITEM_ID, KEY_SERVICE_ID, KEY_QTY, KEY_PRICE, KEY_ITEM_REMARKS, KEY_USER, KEY_UNIT_PRICE
                }, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        User user = null;
        if (cursor.getString(7) != null) {
            user = getUser(cursor.getLong(7));
        }
        OrderDetails orderDetails = new OrderDetails(
                Long.parseLong(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                Long.parseLong(cursor.getString(2)),
                Long.parseLong(cursor.getString(3)),
                Long.parseLong(cursor.getString(4)),
                Double.parseDouble(cursor.getString(5)),
                cursor.getString(6),
                user
        );
        orderDetails.setUnitPrice(Double.parseDouble(cursor.getString(8)));
        cursor.close();
        //db.close();
        return orderDetails;
    }

    // Getting All Order
    public List<OrderDetails> getAllOrderDetails() {
        List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
        // Select All Query
        String selectQuery = "SELECT id, order_id, item_id, user, service_id, qty, price, item_remarks, unit_price FROM " + TABLE_ORDER_DETAILS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setId(cursor.getLong(0));
                orderDetails.setOrderId(cursor.getLong(1));
                orderDetails.setItemId(cursor.getLong(2));
                orderDetails.setUserId(cursor.getLong(3));
                orderDetails.setServiceId(cursor.getLong(4));
                orderDetails.setQty(cursor.getLong(5));
                orderDetails.setPrice(cursor.getDouble(6));
                orderDetails.setItemRemarks(cursor.getString(7));
                orderDetails.setUnitPrice(cursor.getDouble(8));

                User user = null;
                if (cursor.getString(3) != null) {
                    user = getUser(cursor.getLong(3));
                }
                orderDetails.setUser(user);
                orderDetailsList.add(orderDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return orderDetailsList;
    }

    // Getting All Order by order id
    public List<OrderDetails> orderDetailsByOrderId(Long orderId) {
        List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER_DETAILS + " WHERE " + KEY_ORDER_ID + " = " + orderId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setId(cursor.getLong(0));
                orderDetails.setOrderId(cursor.getLong(1));
                orderDetails.setItemId(cursor.getLong(2));
                orderDetails.setServiceId(cursor.getLong(4));
                orderDetails.setQty(cursor.getLong(5));
                orderDetails.setPrice(cursor.getDouble(6));
                orderDetails.setItemRemarks(cursor.getString(7));
                orderDetails.setUnitPrice(cursor.getDouble(8));

                if (cursor.getString(3) != null) {
                    orderDetails.setUser(getUser(cursor.getLong(3)));
                }
                orderDetailsList.add(orderDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return orderDetailsList;
    }

    /*get Id of last inserted item..
    please use in a transaction for reliability
    can access PRIMARY KEY*/
    private Cursor getLastInsertedId(SQLiteDatabase db) {
        final String GET_ROW_ID_QUERY = "SELECT last_insert_rowid()";
        Cursor cur = db.rawQuery(GET_ROW_ID_QUERY, null);
        cur.moveToFirst();
        return cur;
    }

/*
    public void getItemName(Long itemId) {
        final String GET_ITEM_NAME = "SELECT "+KEY_ITEM_NAME+" FROM "+TABLE_;
        Cursor cur = db.rawQuery(GET_ROW_ID_QUERY, null);
        cur.moveToFirst();
    }*/
}
