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
    private static final int DATABASE_VERSION = 2; // Tăng giá trị khi thay đổi

    //Tên các bảng
    public static String  TB_Account = "Acccount";
    public static final String   TABLE_USERS = "Users";
    public static String  TB_Admin = "Admin";
    public static String  TB_Trans = "Transaction";
    public static String  TB_Budget = "Budget";
    public static String  TB_Cate = "Category";

     //Thuoc tinh cua bang Account
     public static String  TB_Account_Id = "Acccount_Id";
     public static String  TB_Account_Name = "Acccount_Name";
     public static String  TB_Account_Type = "Acccount_Type";
     public static String  TB_Account_Role = "Acccount_Role";

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users (bỏ user_id)
        try{
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_SDT + " TEXT PRIMARY KEY, "  // Đặt Users_SDT là khóa chính
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_PASSWORD + " TEXT, "
                + COLUMN_USER_TYPE + " TEXT, "
                + COLUMN_USER_CCCD + " TEXT, "
                + COLUMN_USER_BIRTHDAY + " TEXT, "
                + COLUMN_USER_SEX + " TEXT, "
                + COLUMN_USER_ADDRESS + " TEXT,"
                + COLUMN_USER_NOTE +   " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
        Log.d("SQLiteHelper", "Table " + TABLE_USERS + " created successfully");
    } catch (Exception e) {
    Log.e("SQLiteHelper", "Error creating table: " + e.getMessage());
}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ và tạo lại nếu có thay đổi phiên bản
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        Log.d("SQLiteHelper", "Database created");
        onCreate(db);
    }

    public void addUser(String sdt, String password, String fullName, String address, String birthDate, String cccd, String sex, String type,String notes) {
        if (isUserExists(sdt)) {
            Log.e("SQLiteHelper", "Người dùng với số điện thoại " + sdt + " đã tồn tại!");
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_SDT, sdt); // Số điện thoại
        values.put(COLUMN_USER_PASSWORD, password); // Mật khẩu
        values.put(COLUMN_USER_NAME, fullName); // Họ và tên
        values.put(COLUMN_USER_ADDRESS, address); // Địa chỉ
        values.put(COLUMN_USER_BIRTHDAY, birthDate); // Ngày sinh
        values.put(COLUMN_USER_CCCD, cccd); // CCCD
        values.put(COLUMN_USER_SEX, sex); // Giới tính
        values.put(COLUMN_USER_TYPE, type); // Loại người dùng (user/admin)
        values.put(COLUMN_USER_NOTE,notes); //Ghi chu them
        db.insert(TABLE_USERS, null, values);
        long result = db.insert(TABLE_USERS, null, values);
        if (result == -1) {
            Log.e("SQLiteHelper", "Failed to insert user with SDT: " + sdt);
        } else {
            Log.d("SQLiteHelper", "User added successfully with SDT: " + sdt);
        }
        db.close();
    }

    public boolean isUserExists(String sdt) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_SDT}, COLUMN_USER_SDT + "=?", new String[]{sdt}, null, null, null);
        boolean exists = cursor.moveToFirst();
        Log.d("SQLiteHelper", "isUserExists: " + sdt + " exists? " + exists);
        cursor.close();
        return exists;
    }

}