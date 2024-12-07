// File: mainHome/mainScreen.java
package com.voggella.android.doan.mainHome;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.voggella.android.doan.Database.BudgetFragment;
import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;
import com.voggella.android.doan.Database.TransactionAdapter; // Import TransactionAdapter
import com.voggella.android.doan.Database.Transaction; // Import Transaction
import java.util.ArrayList;
import java.util.List;

    public class mainScreen extends AppCompatActivity {

        private RecyclerView recyclerView;
        private TransactionAdapter transactionAdapter;
        private List<Transaction> transactionList;
        private SQLiteHelper dbHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_screen);

            dbHelper = new SQLiteHelper(this);

            // Setup RecyclerView
            recyclerView = findViewById(R.id.viewTrans); // Giả sử ID của RecyclerView là recycler_view
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Lấy dữ liệu giao dịch từ cơ sở dữ liệu
            loadTransactions();
        }

        private void loadTransactions() {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // Chỉ lấy 3 trường cần thiết: Trans_Type, Trans_Amount, Trans_Date
            String[] columns = {
                    SQLiteHelper.TB_Trans_Type,
                    SQLiteHelper.TB_Trans_Amount,
                    SQLiteHelper.TB_Trans_Date
            };

            Cursor cursor = db.query(SQLiteHelper.TB_Trans, columns, null, null, null, null, null);

            transactionList = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Lấy dữ liệu chỉ từ các trường cần thiết
                    @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(SQLiteHelper.TB_Trans_Type));
                    @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.TB_Trans_Amount));
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.TB_Trans_Date));

                    // Thêm vào danh sách transaction
                    transactionList.add(new Transaction(type, amount, date));
                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();

            // Cập nhật RecyclerView
            transactionAdapter = new TransactionAdapter(transactionList);
            recyclerView.setAdapter(transactionAdapter);

            //Cap nhat Bugget
            TextView viewBudget = findViewById(R.id.ViewBudget);

// Đặt sự kiện OnTouchListener để thay đổi màu sắc khi người dùng chạm vào TextView
            viewBudget.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Khi người dùng bắt đầu chạm, thay đổi màu sắc (mô phỏng hover)
                            viewBudget.setTextColor(getResources().getColor(R.color.red)); // Đổi màu chữ thành đỏ
                            return true;
                        case MotionEvent.ACTION_UP:
                            // Khi người dùng thả tay, trở lại màu sắc ban đầu
                            viewBudget.setTextColor(getResources().getColor(R.color.white)); // Trở lại màu trắng
                            return true;
                        default:
                            return false;
                    }
                }
            });

// Đặt sự kiện OnClickListener để xử lý khi người dùng nhấn vào TextView
            viewBudget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Mở Fragment để người dùng nhập số dư mới
                    // Kiểm tra FragmentManager và FragmentTransaction
                    Log.d("FragmentTest", "Opening BudgetFragment");
                    BudgetFragment budgetFragment = new BudgetFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.fragment_container, budgetFragment); // FrameLayout chứa Fragment
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

        }
    }
