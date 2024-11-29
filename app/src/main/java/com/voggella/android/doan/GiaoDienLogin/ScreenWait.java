package com.voggella.android.doan.GiaoDienLogin;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.voggella.android.doan.R;

public class ScreenWait extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_wait);
        Toast.makeText(ScreenWait.this,
                "Chuyển sau " + " 5 giây",
                Toast.LENGTH_SHORT).show();
        new CountDownTimer(5000, 500) {
            public void onTick(long millisUntilFinished) {
                // Hiển thị Toast mỗi giây

            }
            public void onFinish() {
                // Khi đếm ngược kết thúc, chuyển sang layout 2
                setContentView(R.layout.main_screen);
            }
        }.start();
    }
}
