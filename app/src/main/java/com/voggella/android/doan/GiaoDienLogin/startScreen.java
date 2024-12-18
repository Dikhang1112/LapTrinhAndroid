package com.voggella.android.doan.GiaoDienLogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.voggella.android.doan.R;

public class startScreen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000; // Thời gian hiển thị 3 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        // Chờ 3 giây và chuyển sang Activity chính
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(startScreen.this, Login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Hiệu ứng chuyển Activity
            finish();
        }, SPLASH_TIME_OUT);

    }
}
