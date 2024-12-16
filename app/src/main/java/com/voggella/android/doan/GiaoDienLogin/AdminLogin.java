package com.voggella.android.doan.GiaoDienLogin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.voggella.android.doan.Database_Adapter.SQLiteHelper;
import com.voggella.android.doan.R;

public class AdminLogin  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singin_admin);
        Button btnLogin = findViewById(R.id.btnAdminLogin);
        EditText etAdminName = findViewById(R.id.etAdminName);
        EditText etPassword = findViewById(R.id.etPassword);

// Khởi tạo DatabaseHelper
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        btnLogin.setOnClickListener(v -> {
            String name = etAdminName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                boolean isValid = dbHelper.checkAdmin(name, password);
                if (isValid) {
                    // Chuyển sang Intent tiếp theo
                    Intent intent = new Intent(this, AdminManagementActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc Activity hiện tại
                } else {
                    // Hiển thị thông báo lỗi
                    Toast.makeText(this, "Tên hoặc mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
