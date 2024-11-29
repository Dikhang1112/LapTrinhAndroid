package com.voggella.android.doan.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.voggella.android.doan.R;

public class createDatabase extends SQLiteOpenHelper
{
public static String  TB_Account = "Acccount";
public static String  TB_Users = "Users";
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
     public static String  TB_Users_SDT = "Users_SDT";
     public static String  TB_Users_Name = "Users_Name";
     public static String  TB_Users_Password = "Users_PassWord";
     public static String  TB_Users_Type = "Users_Type";
     public static String  TB_Users_CCCD = "Users_CCCD";
     public static String  TB_Users_Birthday = "Users_BirthDay";
     public static String  TB_Users_Sex = "Users_Sex";

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


    public createDatabase( Context context) {
        super(context, "Mangage Finance", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

