package com.voggella.android.doan.mainHome;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;

import java.lang.reflect.Field;
import java.util.List;

public class trans_Detail extends AppCompatActivity {

    private SQLiteHelper dbHelper; // Giả sử bạn đã định nghĩa SQLiteHelper
    private String phoneUser;      // Số điện thoại người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_detail);

        // Khởi tạo database
        dbHelper = new SQLiteHelper(this);

        // Khởi tạo UI
        Spinner spinnerTransac = findViewById(R.id.spinnerTransac);
        Button btnBackHome = findViewById(R.id.btnBackMain);

        // Nhận số điện thoại người dùng từ Intent
        phoneUser = getIntent().getStringExtra("USERS_SDT");
        if (phoneUser == null || phoneUser.isEmpty()) {
            Log.e("mainScreen", "Số điện thoại không hợp lệ.");
            Toast.makeText(this, "Không nhận được thông tin người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Tạo adapter cho Spinner
        ArrayAdapter<SQLiteHelper.CategoryName> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, SQLiteHelper.CategoryName.values()
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTransac.setAdapter(adapter);


        spinnerTransac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy giá trị được chọn dưới dạng CategoryName (enum)
                SQLiteHelper.CategoryName selectedType = (SQLiteHelper.CategoryName) parent.getItemAtPosition(position);

                // Gọi phương thức với giá trị đã chọn
                List<TransactionShow> transactions = dbHelper.getTransactionsByType(phoneUser, selectedType.toString()); // .toString() chuyển enum thành chuỗi

                // Cập nhật RecyclerView
                updateRecyclerView(transactions);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });

        // Đảm bảo menu thả xuống hiển thị bên dưới Spinner
//        spinnerTransac.post(() -> {
//            try {
//                Field popup = Spinner.class.getDeclaredField("mPopup");
//                popup.setAccessible(true);
//                android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinnerTransac);
//                if (popupWindow != null) {
//                    popupWindow.setHeight(500); // Đặt chiều cao menu dropdown theo pixel (có thể thay đổi)
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

        // Sự kiện khi nhấn nút quay lại
        btnBackHome.setOnClickListener(v -> finish());
    }
    private void updateRecyclerView(List<TransactionShow> transactions) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTransactions);
        TransactionAdapter adapter = new TransactionAdapter(transactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}
