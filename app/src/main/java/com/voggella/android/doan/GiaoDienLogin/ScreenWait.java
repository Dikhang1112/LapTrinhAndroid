package com.voggella.android.doan.GiaoDienLogin;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.voggella.android.doan.R;
import com.voggella.android.doan.mainHome.mainScreen;

public class ScreenWait extends AppCompatActivity {

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
                Intent intentMain = new Intent(ScreenWait.this, mainScreen.class);
                startActivity(intentMain);
                finish();
            }
        }.start();
    }
}
