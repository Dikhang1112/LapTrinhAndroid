package com.voggella.android.doan.GiaoDienLogin;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.voggella.android.doan.R;
import com.voggella.android.doan.mainHome.mainScreen;

public class ScreenWait extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_wait);

        // Nhận họ và tên người dùng va sdt từ Intent
        String userFullName = getIntent().getStringExtra("USER_FULL_NAME");
        String phoneUser = getIntent().getStringExtra("USERS_SDT");

        //Kiem tra da co thong tin hay chua
        if (phoneUser == null || phoneUser.isEmpty()) {
            Log.e("ScreenWait", "Thông tin người dùng không được truyền.");
            Toast.makeText(ScreenWait.this, "Lỗi: Không có thông tin người dùng!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ScreenWait.this, "Chuyển qua trang chủ sau 5 giây...", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(ScreenWait.this, "Chuyển qua trang chủ sau 5 giây...", Toast.LENGTH_SHORT).show();

        new CountDownTimer(5000, 500) {
            public void onTick(long millisUntilFinished) {
                // Có thể hiển thị thời gian còn lại ở đây nếu cần
            }

            public void onFinish() {
                // Chuyển sang màn hình chính và truyền họ và tên người dùng
                Intent intentMain = new Intent(ScreenWait.this, mainScreen.class);
                intentMain.putExtra("USER_FULL_NAME", userFullName);  // Truyền họ và tên người dùng
                intentMain.putExtra("USERS_SDT", phoneUser);
                startActivity(intentMain);
                finish();  // Đóng màn hình chờ
            }
        }.start();
    }
}

