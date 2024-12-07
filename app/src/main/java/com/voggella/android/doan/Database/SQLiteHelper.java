package com.voggella.android.doan.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper
{
    // Tên cơ sở dữ liệu
    private static final String DATABASE_NAME = "ManagementFinancial.db";
    private static final int DATABASE_VERSION = 3; // Tăng giá trị khi thay đổi

    //Tên các bảng
    public static final String  TB_Account = "Account";
    public static final String  TB_USERS = "Users";
    public static final String  TB_Admin = "Admin";
    public static final String  TB_Trans = "Transac";
    public static final String  TB_Budget = "Budget";
    public static final String  TB_Cate = "Category";

     //Thuoc tinh cua bang Account
     public static String  TB_Account_Id = "Acccount_Id";
     public static String  TB_Account_Role = "Acccount_Role";
     public static String  TB_Account_UserSdt = "Users_SDT";

     //Thuoc tinh cua users
    public static final String COLUMN_USER_SDT = "Users_SDT";
    public static final String COLUMN_USER_NAME = "Users_Name";
    public static final String COLUMN_USER_PASSWORD = "Users_Password";
    public static final String COLUMN_USER_TYPE = "Users_Type";
    public static final String COLUMN_USER_CCCD = "Users_CCCD";
    public static final String COLUMN_USER_BIRTHDAY = "Users_BirthDay";
    public static final String COLUMN_USER_SEX = "Users_Sex";
    public static final String COLUMN_USER_ADDRESS = "Users_Address";
    public static final String COLUMN_USER_NOTE = "Users_Notes";

     //Thuoc tinh cua admin
     public static String  TB_Admin_Id = "Admin_Id";
     public static String  TB_Admin_Name = "Admin_Name";
     public static String  TB_Admin_Password = "Admin_Password";

     //Thuoc tinh cua Category
     public static String  TB_Cate_Id = "Category_Id";
     public static String  TB_Cate_AccountId = "Category_AccountId";
     public static String  TB_Cate_Name = "Category_Name";
     public static String  TB_Cate_Date = "Category_Date";

     //Thuoc tinh cua Transaction
     public static String  TB_Trans_Id = "Transaction_Id";
     public static String  TB_Trans_AccountId = "Transaction_AccountId";
     public static String  TB_Trans_Amount = "Transaction_Amount";
     public static String  TB_Trans_Type = "Transaction_Type";
     public static String  TB_Trans_Date = "Transaction_Date";
     public static String  TB_Trans_CateId = "Transaction_CateId";
     public static String  TB_Trans_Descrip = "Transaction_Description";

     //Thuoc tinh Budget
     public static String  TB_Budget_Id = "Budget_Id";
     public static String  TB_Budget_AccountId = "Budget_AccountId";
     public static String  TB_Budget_Data = "Budget_Data";
     public static String  TB_Budget_Date = "Budget_Date";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true); // Bật ràng buộc khóa ngoại
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        try {
            String CREATE_USERS_TABLE = "CREATE TABLE " + TB_USERS + "("
                    + COLUMN_USER_SDT + " TEXT PRIMARY KEY, "
                    + COLUMN_USER_NAME + " TEXT, "
                    + COLUMN_USER_PASSWORD + " TEXT, "
                    + COLUMN_USER_TYPE + " INTEGER, "
                    + COLUMN_USER_CCCD + " TEXT, "
                    + COLUMN_USER_BIRTHDAY + " TEXT, "
                    + COLUMN_USER_SEX + " INTEGER, "
                    + COLUMN_USER_ADDRESS + " TEXT, "
                    + COLUMN_USER_NOTE + " TEXT"
                    + ")";
            db.execSQL(CREATE_USERS_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_USERS + " created successfully");

            // Tạo bảng Account
            String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TB_Account + "("
                    + TB_Account_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TB_Account_Role + " TEXT, "
                    + TB_Account_UserSdt + " TEXT, "
                    + "FOREIGN KEY (" + TB_Account_UserSdt + ") REFERENCES " + TB_USERS + "(" + COLUMN_USER_SDT + ") ON DELETE CASCADE"
                    + ")";
            db.execSQL(CREATE_ACCOUNTS_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_Account + " created successfully");

            // Tạo bảng Admin
            String CREATE_ADMIN_TABLE = "CREATE TABLE " + TB_Admin + "("
                    + TB_Admin_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TB_Admin_Name + " TEXT, "
                    + TB_Admin_Password + " TEXT"
                    + ")";
            db.execSQL(CREATE_ADMIN_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_Admin + " created successfully");

            // Tạo bảng Category
            String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TB_Cate + "("
                    + TB_Cate_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TB_Cate_AccountId + " INTEGER, "
                    + TB_Cate_Name + " TEXT, "
                    + TB_Cate_Date + " TEXT, "
                    + "FOREIGN KEY (" + TB_Cate_AccountId + ") REFERENCES " + TB_Account + "(" + TB_Account_Id + ") ON DELETE CASCADE"
                    + ")";
            db.execSQL(CREATE_CATEGORY_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_Cate + " created successfully");

            // Tạo bảng Transaction
            String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TB_Trans + "("
                    + TB_Trans_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TB_Trans_AccountId + " INTEGER, "
                    + TB_Trans_Amount + " REAL, "
                    + TB_Trans_Type + " TEXT, "
                    + TB_Trans_Date + " TEXT, "
                    + TB_Trans_CateId + " INTEGER, "
                    + TB_Trans_Descrip + " TEXT, "
                    + "FOREIGN KEY (" + TB_Trans_AccountId + ") REFERENCES " + TB_Account + "(" + TB_Account_Id + ") ON DELETE CASCADE, "
                    + "FOREIGN KEY (" + TB_Trans_CateId + ") REFERENCES " + TB_Cate + "(" + TB_Cate_Id + ") ON DELETE CASCADE"
                    + ")";
            db.execSQL(CREATE_TRANSACTION_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_Trans + " created successfully");


            // Tạo bảng Budget
            String CREATE_BUDGET_TABLE = "CREATE TABLE " + TB_Budget + "("
                    + TB_Budget_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TB_Budget_AccountId + " INTEGER, "
                    + TB_Budget_Data + " REAL, "
                    + TB_Budget_Date + " TEXT, "
                    + "FOREIGN KEY (" + TB_Budget_AccountId + ") REFERENCES " + TB_Account + "(" + TB_Account_Id + ") ON DELETE CASCADE"
                    + ")";
            db.execSQL(CREATE_BUDGET_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_Budget + " created successfully");

        } catch (Exception e) {
            Log.e("SQLiteHelper", "Error creating tables: " + e.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ nếu cần nâng cấp cơ sở dữ liệu
        db.execSQL("DROP TABLE IF EXISTS " + TB_Budget);
        db.execSQL("DROP TABLE IF EXISTS " + TB_Trans);
        db.execSQL("DROP TABLE IF EXISTS " + TB_Cate);
        db.execSQL("DROP TABLE IF EXISTS " + TB_Admin);
        db.execSQL("DROP TABLE IF EXISTS " + TB_Account);
        db.execSQL("DROP TABLE IF EXISTS " + TB_USERS);
        Log.d("SQLiteHelper", "All tables dropped");
        onCreate(db);  // Tạo lại các bảng
    }


    public void addUser(String sdt, String password, String fullName, String address, String birthDate, String cccd, String sex, String type,String notes) {
        if (isUserExists(sdt)) {
            Log.e("SQLiteHelper", "Người dùng với số điện thoại " + sdt + " đã tồn tại!");
            return;
        }
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_SDT, sdt);
            values.put(COLUMN_USER_PASSWORD, password);
            values.put(COLUMN_USER_NAME, fullName);
            values.put(COLUMN_USER_ADDRESS, address);
            values.put(COLUMN_USER_BIRTHDAY, birthDate);
            values.put(COLUMN_USER_CCCD, cccd);
            values.put(COLUMN_USER_SEX, Integer.parseInt(sex));
            values.put(COLUMN_USER_TYPE, Integer.parseInt(type));
            values.put(COLUMN_USER_NOTE, notes);
            long result = db.insert(TB_USERS, null, values);
            if (result == -1) {
                Log.e("SQLiteHelper", "Failed to insert user with SDT: " + sdt);
            } else {
                Log.d("SQLiteHelper", "User added successfully with SDT: " + sdt);
            }
        } catch (Exception e) {
            Log.e("SQLiteHelper", "Error adding user: " + e.getMessage());
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean isUserExists(String sdt) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TB_USERS, new String[]{COLUMN_USER_SDT}, COLUMN_USER_SDT + "=?", new String[]{sdt}, null, null, null);
            return cursor.moveToFirst(); // Trả về true nếu có dữ liệu
        } finally {
            if (cursor != null) cursor.close();
            db.close();  // Đóng kết nối
        }
    }


    public boolean validateUser(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TB_USERS + " WHERE " + COLUMN_USER_SDT + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
            cursor = db.rawQuery(query, new String[]{phone, password});
            return cursor.moveToFirst(); // Trả về true nếu có dữ liệu
        } catch (Exception e) {
            Log.e("SQLiteHelper", "Error validating user: " + e.getMessage());
            return false; // Trả về false nếu có lỗi
        } finally {
            if (cursor != null) cursor.close();
            db.close();  // Đóng kết nối
        }
    }
    //Them thong tin bang TranSac
    public void addTransaction(int accountId, double amount, String type, String date, int categoryId, String description) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TB_Trans_AccountId, accountId);
            values.put(TB_Trans_Amount, amount);
            values.put(TB_Trans_Type, type);
            values.put(TB_Trans_Date, date);
            values.put(TB_Trans_CateId, categoryId);
            values.put(TB_Trans_Descrip, description);

            long result = db.insert(TB_Trans, null, values);
            if (result == -1) {
                Log.e("SQLiteHelper", "Failed to insert transaction");
            } else {
                Log.d("SQLiteHelper", "Transaction added successfully");
            }
        } catch (Exception e) {
            Log.e("SQLiteHelper", "Error adding transaction: " + e.getMessage());
        } finally {
            if (db != null) db.close();
        }
    }
}


