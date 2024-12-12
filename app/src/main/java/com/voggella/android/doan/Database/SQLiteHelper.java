package com.voggella.android.doan.Database;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.voggella.android.doan.mainHome.TransactionShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteHelper extends SQLiteOpenHelper
{
    // Tên cơ sở dữ liệu
    private static final String DATABASE_NAME = "ManagementFinancial.db";
    private static final int DATABASE_VERSION = 1   ; // Tăng giá trị khi thay đổi

    //Tên các bảng
    public static final String  TB_Account = "Account";
    public static final String  TB_USERS = "Users";
    public static final String  TB_Admin = "Admin";
    public static final String  TB_Trans = "Transac";
    public static final String  TB_Budget = "Budget";
    public static final String  TB_Cate = "Category";

    //Thuoc tinh cua bang Account
    public static final String  COLUMN_ACCOUNT_ID = "Account_Id";
    public static final String  COLUMN_ACCOUNT_ROLE = "Account_Role";
    public static final String  COLUMN_ACCOUNT_USERS_SDT = "Account_Users_SDT";

    //Thuoc tinh cua Users
    public static final String COLUMN_USER_SDT = "Users_SDT";
    public static final String COLUMN_USER_NAME = "Users_Name";
    public static final String COLUMN_USER_PASSWORD = "Users_Password";
    public static final String COLUMN_USER_TYPE = "Users_Type";
    public static final String COLUMN_USER_CCCD = "Users_CCCD";
    public static final String COLUMN_USER_BIRTHDAY = "Users_BirthDay";
    public static final String COLUMN_USER_SEX = "Users_Sex";
    public static final String COLUMN_USER_ADDRESS = "Users_Address";
    public static final String COLUMN_USER_NOTE = "Users_Notes";

    //Thuoc tinh cua Admin
    public static final String  COLUMN_ADMIN_ID = "Admin_Id";
    public static final String  COLUMN_ADMIN_NAME = "Admin_Name";
    public static final String  COLUMN_ADMIN_PASSWORD = "Admin_Password";

    //Thuoc tinh cua Category
    public static final String  COLUMN_CATEGORY_ID = "Category_Id";
    public static final String  COLUMN_CATEGORY_USERSDT = "Category_UserSdt";
    public static final String  COLUMN_CATEGORY_NAME = "Category_Name";
    public static final String  COLUMN_CATEGORY_DATE = "Category_Date";
     // Account Role
     public enum CategoryName{
         TRANSPORT("Transport"),
         EDUCATION("Education"),
         SHOPPING("Shopping"),
         MEDICINE("Medicine"),
         FOOD("Food");
         private String name;

         CategoryName(String name) {
             this.name = name;
         }

         public String getName() {
             return name;
         }
     }
    public enum CategoryNameChart {
        TRANSPORT("Transport"),
        EDUCATION("Education"),
        SHOPPING("Shopping"),
        MEDICINE("Medicine"),
        FOOD("Food");

        private final String displayName;

        CategoryNameChart(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }


    //Thuoc tinh cua Transaction
    public static final String  COLUMN_TRANSACTION_ID = "Transaction_Id";
    public static final String  COLUMN_TRANSACTION_USERS_SDT = "Transaction_UsersSdt";
    public static final String  COLUMN_TRANSACTION_AMOUNT = "Transaction_Amount";
    public static final String  COLUMN_TRANSACTION_TYPE = "Transaction_Type";
    public static final String  COLUMN_TRANSACTION_DATE = "Transaction_Date";
    public static final String  COLUMN_TRANSACTION_CATEGORY_ID = "Transaction_CateId";
    public static final String  COLUMN_TRANSACTION_DESCRIPTION = "Transaction_Description";

    //Thuoc tinh Budget
    public static final String  COLUMN_BUDGET_ID = "Budget_Id";
    public static final String  COLUMN_BUDGET_USERS_SDT = "Budget_UserSdt";
    public static final String  COLUMN_BUDGET_DATA = "Budget_Data";
    public static final String  COLUMN_BUDGET_DATE = "Budget_Date";


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
                    + COLUMN_USER_CCCD + " TEXT, "
                    + COLUMN_USER_BIRTHDAY + " TEXT, "
                    + COLUMN_USER_SEX + " TEXT,"
                    + COLUMN_USER_TYPE + " INTEGER DEFAULT 0, "
                    + COLUMN_USER_ADDRESS + " TEXT, "
                    + COLUMN_USER_NOTE + " TEXT"
                    + ")";
            db.execSQL(CREATE_USERS_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_USERS + " created successfully");

            // Tạo bảng Account
            String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TB_Account + "("
                    + COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_ACCOUNT_ROLE + " TEXT CHECK(" + COLUMN_ACCOUNT_ROLE + " IN ('User', 'Admin')),"
                    + COLUMN_ACCOUNT_USERS_SDT + " TEXT, "
                    + "FOREIGN KEY (" + COLUMN_ACCOUNT_USERS_SDT + ") REFERENCES " + TB_USERS + "(" + COLUMN_USER_SDT + ") ON DELETE CASCADE"
                    + ")";
            db.execSQL(CREATE_ACCOUNTS_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_Account + " created successfully");

            // Tạo bảng Admin
            String CREATE_ADMIN_TABLE = "CREATE TABLE " + TB_Admin + "("
                    + COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_ADMIN_NAME + " TEXT, "
                    + COLUMN_ADMIN_PASSWORD + " TEXT"
                    + ")";
            db.execSQL(CREATE_ADMIN_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_Admin + " created successfully");

            // Tạo bảng Category
            String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TB_Cate + "("
                    + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_CATEGORY_USERSDT + " TEXT, "
                    + COLUMN_CATEGORY_NAME + " TEXT,"
                    + COLUMN_CATEGORY_DATE + " TEXT, "
                    + "FOREIGN KEY (" + COLUMN_CATEGORY_USERSDT + ") REFERENCES " + TB_USERS + "(" + COLUMN_USER_SDT + ") ON DELETE CASCADE"
                    + ")";
            db.execSQL(CREATE_CATEGORY_TABLE);
            Log.d("SQLiteHelper", "Table " + TB_Cate + " created successfully");

            // Tạo bảng TRANSACTION
            String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TB_Trans + "("
                    + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_TRANSACTION_USERS_SDT + " TEXT, "
                    + COLUMN_TRANSACTION_AMOUNT + " REAL, "
                    + COLUMN_TRANSACTION_TYPE + " TEXT,"
                    + COLUMN_TRANSACTION_DATE + " TEXT, "
                    + COLUMN_TRANSACTION_CATEGORY_ID + " INTEGER, "
                    + COLUMN_TRANSACTION_DESCRIPTION + " TEXT, "
                    + "FOREIGN KEY (" + COLUMN_TRANSACTION_USERS_SDT + ") REFERENCES " + TB_USERS + "(" + COLUMN_USER_SDT + ") ON DELETE CASCADE, "
                    + "FOREIGN KEY (" + COLUMN_TRANSACTION_CATEGORY_ID + ") REFERENCES " + TB_Cate + "(" + COLUMN_CATEGORY_ID + ") ON DELETE CASCADE"
                    + ")";
            db.execSQL(CREATE_TRANSACTION_TABLE);
        // Tạo bảng Budget
            String CREATE_BUDGET_TABLE = "CREATE TABLE " + TB_Budget + "("
                    + COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_BUDGET_USERS_SDT + " TEXT, "
                    + COLUMN_BUDGET_DATA + " REAL, "
                    + COLUMN_BUDGET_DATE + " TEXT, "
                    + "FOREIGN KEY (" + COLUMN_BUDGET_USERS_SDT + ") REFERENCES " + TB_USERS + "(" + COLUMN_USER_SDT + ") ON DELETE CASCADE"
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
    //Them thong tin vao bang Users
    public void addUser(String sdt, String password, String fullName, String address, String birthDate, String cccd, String sex,String notes, String type) {
        if (sdt == null || sdt.isEmpty()) {
            Log.e("SQLiteHelper", "SDT cannot be null or empty!");
            return;
        }
        if (isUserExists(sdt)) {
            Log.e("SQLiteHelper", "Người dùng với số điện thoại " + sdt + " đã tồn tại!");
            return;
        }

        int userType = 0; // Giá trị mặc định là false
        if ("1".equals(type) || "true".equalsIgnoreCase(type)) {
            userType = 1; // Đặt giá trị true
        } else if (!"0".equals(type) && !"false".equalsIgnoreCase(type)) {
            Log.e("SQLiteHelper", "Invalid user type: " + type);
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
            values.put(COLUMN_USER_SEX, sex);
            values.put(COLUMN_USER_NOTE, notes != null ? notes : "");
            values.put(COLUMN_USER_TYPE, userType); // Chỉ nhận 0 hoặc 1

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
    //Kiem tra xem sdt da ton tai chua
    public boolean isUserExists(String sdt) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TB_USERS, new String[]{COLUMN_USER_SDT}, COLUMN_USER_SDT + "=?", new String[]{sdt}, null, null, null);
            return cursor.moveToFirst(); // Trả về true nếu có dữ liệu
        } finally {
            if (cursor != null) cursor.close();
        }
    }
    //Kim tra Sdt va pass nguoi dung
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

    //Them Budget vao Database
    public void addBudget(String userPhone, double data, String date) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            // Kiểm tra xem số điện thoại người dùng có tồn tại
            if (!isUserExists(userPhone)) {
                Log.e("SQLiteHelper", "User with phone number " + userPhone + " không tồn tại");
                return;
            }

            // Chèn thông tin vào bảng Budget
            ContentValues values = new ContentValues();
            values.put(COLUMN_BUDGET_USERS_SDT, userPhone); // Tên cột sửa đúng theo cấu trúc bảng
            values.put(COLUMN_BUDGET_DATA, data);
            values.put(COLUMN_BUDGET_DATE, date);

            long result = db.insert(TB_Budget, null, values);
            if (result == -1) {
                Log.e("SQLiteHelper", "Failed to insert budget");
            } else {
                Log.d("SQLiteHelper", "Budget inserted successfully with ID: " + result);
            }
        } catch (SQLException e) {
            Log.e("SQLiteHelper", "SQL error adding budget: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e("SQLiteHelper", "Unexpected error adding budget: " + e.getMessage(), e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close(); // Đảm bảo đóng kết nối cơ sở dữ liệu
            }
        }
    }
    //Them sdt User vao budget
    public void insertBudget(String userSDT) {
        SQLiteDatabase db = null;
        try {
            // Mở cơ sở dữ liệu
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_BUDGET_USERS_SDT, userSDT);
            // Chèn dữ liệu vào bảng BUDGET
            db.insert(SQLiteHelper.TB_Budget, null, values);
        } catch (Exception e) {
            e.printStackTrace();  // Xử lý lỗi nếu có
        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // Đảm bảo đóng kết nối cơ sở dữ liệu sau khi xong
            }
        }
    }
    //Them sdt User vao Transac
    public void insertTran(String userSDT) {
        SQLiteDatabase db = null;
        try {
            // Mở cơ sở dữ liệu
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_TRANSACTION_USERS_SDT, userSDT);
            // Chèn dữ liệu vào bảng BUDGET
            db.insert(SQLiteHelper.TB_Trans, null, values);
        } catch (Exception e) {
            e.printStackTrace();  // Xử lý lỗi nếu có
        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // Đảm bảo đóng kết nối cơ sở dữ liệu sau khi xong
            }
        }
    }
    //Them thong tin vao bang Account
    public void insertAccount(String userSDT) {
        SQLiteDatabase db = null;
        try {
            // Mở cơ sở dữ liệu
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_ACCOUNT_ROLE,"User");
            values.put(SQLiteHelper.COLUMN_ACCOUNT_USERS_SDT, userSDT);
            // Chèn dữ liệu vào bảng BUDGET
            db.insert(SQLiteHelper.TB_Account, null, values);
        } catch (Exception e) {
            e.printStackTrace();  // Xử lý lỗi nếu có
        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // Đảm bảo đóng kết nối cơ sở dữ liệu sau khi xong
            }
        }
    }
    public void insertCate(String userSDT) {
        SQLiteDatabase db = null;
        try {
            // Mở cơ sở dữ liệu
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_CATEGORY_USERSDT, userSDT);
            // Chèn dữ liệu vào bảng BUDGET
            db.insert(SQLiteHelper.TB_Cate, null, values);
        } catch (Exception e) {
            e.printStackTrace();  // Xử lý lỗi nếu có
        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // Đảm bảo đóng kết nối cơ sở dữ liệu sau khi xong
            }
        }
    }
    // Giả sử trong SQLiteHelper của bạn có phương thức như sau:
    public List<TransactionShow> getTransactionsByType(String userPhone, String transactionType) {
        List<TransactionShow> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_TRANSACTION_DESCRIPTION + ", "
                + COLUMN_TRANSACTION_AMOUNT + ", "
                + COLUMN_TRANSACTION_DATE +
                " FROM " + TB_Trans +
                " WHERE " + COLUMN_TRANSACTION_USERS_SDT + " = ? AND " + COLUMN_TRANSACTION_TYPE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{userPhone, transactionType});

        if (cursor.moveToFirst()) {
            do {
                TransactionShow transaction = new TransactionShow();
                transaction.setDescription(cursor.getString(0)); // Note
                transaction.setAmount(cursor.getDouble(1));     // Amount
                transaction.setDate(cursor.getString(2));       // Date
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }
    public Map<String, Double> getTotalAmountByType(String sdt) {
        Map<String, Double> totalAmountByType = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn để nhóm theo Type và tính tổng Amount
        String query = "SELECT " + COLUMN_TRANSACTION_TYPE + ", SUM(" + COLUMN_TRANSACTION_AMOUNT + ") AS total_amount " +
                "FROM " + TB_Trans + " WHERE " + COLUMN_TRANSACTION_USERS_SDT + " = ? GROUP BY " + COLUMN_TRANSACTION_TYPE;

        Cursor cursor = db.rawQuery(query, new String[] { sdt });

        if (cursor.moveToFirst()) {
            do {
                // Lấy Type và Tổng Amount
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSACTION_TYPE));
                @SuppressLint("Range") double totalAmount = cursor.getDouble(cursor.getColumnIndex("total_amount"));

                // Lưu vào Map (type => tổng Amount)
                totalAmountByType.put(type, totalAmount);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return totalAmountByType;
    }
}


