package com.voggella.android.doan.mainHome;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.voggella.android.doan.Database.MultiTypeAdapter;
import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;
import com.voggella.android.doan.Database.Transaction; // Import Transaction
import com.voggella.android.doan.Database.Budget; // Import Budget
import java.util.ArrayList;
import java.util.List;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class mainScreen extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> itemList; // Danh sách chứa cả Transaction và Budget
    private SQLiteHelper dbHelper;
    private String phoneUser;
    private ActivityResultLauncher<Intent> addTransactionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        dbHelper = new SQLiteHelper(this);

        ImageView btnDashboard = findViewById(R.id.btn_dashboard);
        ImageView btnAdd = findViewById(R.id.btn_add);
        ImageView btnCate = findViewById(R.id.btn_cate);
        ImageView btnProfile = findViewById(R.id.btn_profile);

        // Gắn sự kiện onClick cho từng nút
        btnDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChartJs.class);
            intent.putExtra("USERS_SDT",phoneUser);
            startActivity(intent);
        });

        btnCate.setOnClickListener(v -> {
            Intent intent = new Intent(this, trans_Detail.class);
            intent.putExtra("USERS_SDT",phoneUser);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, account_Setting.class);
            startActivity(intent);
        });

        // Nhận thông tin người dùng từ Intent
        String userFullName = getIntent().getStringExtra("USER_FULL_NAME");
         phoneUser = getIntent().getStringExtra("USERS_SDT");
        if (phoneUser == null || phoneUser.isEmpty()) {
            Log.e("mainScreen", "Số điện thoại không hợp lệ.");
            Toast.makeText(this, "Không nhận được thông tin người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        addTransactionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        refreshData(); // Gọi refreshData để cập nhật RecyclerView
                        Toast.makeText(this, "Giao dịch đã được thêm.", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(this, "Đã hủy thêm giao dịch", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        btnAdd.setOnClickListener(v -> {
            Intent intentAdd = new Intent(mainScreen.this, add_Trans.class);
            intentAdd.putExtra("USERS_SDT", phoneUser);
           startActivity(intentAdd);
           finish();
        });

        // Lấy TextView name
        TextView viewName = findViewById(R.id.ViewName);
        String fullName = getUserFullName(phoneUser);
        if (fullName != null) {
            viewName.setText(fullName);
        } else {
            viewName.setText("Người dùng không xác định");
        }

        // Setup RecyclerView
        recyclerView = findViewById(R.id.viewTrans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load dữ liệu
        loadData(phoneUser);

        // Setup TextView hover effect
        TextView viewBudget = findViewById(R.id.ViewBudget);
        viewBudget.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    viewBudget.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    viewBudget.setBackgroundColor(Color.TRANSPARENT);
                    break;
            }
            return false;
        });

        // Hiển thị Dialog khi ấn vào TextView
        viewBudget.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_budget, null);
            builder.setView(dialogView);

            EditText edtAmount = dialogView.findViewById(R.id.edtAmount);
            EditText edtDate = dialogView.findViewById(R.id.edtDate);
            Button btnSave = dialogView.findViewById(R.id.btnSave);
            AlertDialog dialog = builder.create();
            dialog.show();

            btnSave.setOnClickListener(saveView -> {
                String amount = edtAmount.getText().toString().trim();
                String date = edtDate.getText().toString().trim();
                if (amount.isEmpty() || date.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.addBudget(phoneUser, Double.parseDouble(amount), date);
                String balanceText = "Số dư: " + amount + " vnd";
                viewBudget.setText(balanceText);
                Toast.makeText(this, "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                loadData(phoneUser); // Cập nhật lại RecyclerView
                recreate();
            });
        });
    }
    private void refreshData() {
        if (itemList != null && multiTypeAdapter != null) {
            itemList.clear();
            loadData(phoneUser);
            multiTypeAdapter.notifyDataSetChanged();
        }
    }
    // Phương thức load dữ liệu cả Transaction và Budget
    private void loadData(String phoneUser) {
        itemList = new ArrayList<>();

        // Load Transaction data
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] transactionColumns = {
                SQLiteHelper.COLUMN_TRANSACTION_TYPE,
                SQLiteHelper.COLUMN_TRANSACTION_AMOUNT,
                SQLiteHelper.COLUMN_TRANSACTION_DATE
        };
        Cursor transactionCursor = db.query(SQLiteHelper.TB_Trans, transactionColumns, SQLiteHelper.COLUMN_TRANSACTION_USERS_SDT + " = ?", new String[]{phoneUser}, null, null, null);

        if (transactionCursor != null) {
            while (transactionCursor.moveToNext()) {
                @SuppressLint("Range") String type = transactionCursor.getString(transactionCursor.getColumnIndex(SQLiteHelper.COLUMN_TRANSACTION_TYPE));
                @SuppressLint("Range") double amount = transactionCursor.getDouble(transactionCursor.getColumnIndex(SQLiteHelper.COLUMN_TRANSACTION_AMOUNT));
                @SuppressLint("Range") String date = transactionCursor.getString(transactionCursor.getColumnIndex(SQLiteHelper.COLUMN_TRANSACTION_DATE));

                itemList.add(new Transaction(type, amount, date));
            }
            transactionCursor.close();
        }

        // Load Budget data
        String[] budgetColumns = {
                SQLiteHelper.COLUMN_BUDGET_DATA,
                SQLiteHelper.COLUMN_BUDGET_DATE
        };
        Cursor budgetCursor = db.query(SQLiteHelper.TB_Budget, budgetColumns, SQLiteHelper.COLUMN_BUDGET_USERS_SDT+ " = ?", new String[]{phoneUser}, null, null, null);

        if (budgetCursor != null) {
            while (budgetCursor.moveToNext()) {
                @SuppressLint("Range") double amount = budgetCursor.getDouble(budgetCursor.getColumnIndex(SQLiteHelper.COLUMN_BUDGET_DATA));
                @SuppressLint("Range") String date = budgetCursor.getString(budgetCursor.getColumnIndex(SQLiteHelper.COLUMN_BUDGET_DATE));

                itemList.add(new Budget(amount, date));
            }
            budgetCursor.close();
        }
        db.close();

        // Cập nhật RecyclerView với adapter MultiTypeAdapter
        if (multiTypeAdapter == null) {
            multiTypeAdapter = new MultiTypeAdapter(itemList);
            recyclerView.setAdapter(multiTypeAdapter);
        } else {
            multiTypeAdapter.notifyDataSetChanged();
        }

        // Cập nhật lại ViewBudget
        updateViewBudget(phoneUser);
    }

    // Phương thức cập nhật ViewBudget
    private void updateViewBudget(String phoneUser) {
        double totalBudget = getTotalBudget(phoneUser); // Tổng tiền trong bảng Budget
        double totalTransaction = getTotalTransaction(phoneUser); // Tổng tiền trong bảng Transaction

        // Cập nhật ViewBudget với giá trị tổng ngân sách - tổng giao dịch
        TextView viewBudget = findViewById(R.id.ViewBudget);
        double remainingBalance = totalBudget - totalTransaction;

        // Hiển thị số dư còn lại
        String balanceText = "Số dư " + remainingBalance + " vnd";
        viewBudget.setText(balanceText);
    }

    // Lấy tổng tiền trong bảng Budget
    private double getTotalBudget(String phoneUser) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double total = 0;
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(" + SQLiteHelper.COLUMN_BUDGET_DATA + ") FROM " + SQLiteHelper.TB_Budget + " WHERE " + SQLiteHelper.COLUMN_BUDGET_USERS_SDT + " = ?";
            cursor = db.rawQuery(query, new String[]{phoneUser});

            if (cursor != null && cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("mainScreen", "Lỗi khi tính tổng ngân sách", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return total;
    }

    // Lấy tổng tiền trong bảng Transaction
    private double getTotalTransaction(String phoneUser) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double total = 0;
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(" + SQLiteHelper.COLUMN_TRANSACTION_AMOUNT + ") FROM " + SQLiteHelper.TB_Trans + " WHERE " + SQLiteHelper.COLUMN_TRANSACTION_USERS_SDT + " = ?";
            cursor = db.rawQuery(query, new String[]{phoneUser});

            if (cursor != null && cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("mainScreen", "Lỗi khi tính tổng giao dịch", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return total;
    }

    // Lấy tên đầy đủ của người dùng
    @SuppressLint("Range")
    private String getUserFullName(String phoneUser) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + SQLiteHelper.COLUMN_USER_NAME + " FROM " + SQLiteHelper.TB_USERS + " WHERE " + SQLiteHelper.COLUMN_USER_SDT + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phoneUser});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_USER_NAME));
        }
        return null;
    }

}
