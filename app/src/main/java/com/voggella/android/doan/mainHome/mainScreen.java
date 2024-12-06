package com.voggella.android.doan.mainHome;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.voggella.android.doan.GiaoDienLogin.ScreenWait;
import com.voggella.android.doan.R;

public class mainScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
       //Chuyen qua cac layout khac
        ImageView imgChart = findViewById(R.id.btn_dashboard);
        ImageView imgPlus = findViewById(R.id.btn_add);
        ImageView imgCate = findViewById(R.id.btn_cate);
        ImageView imgAccount = findViewById(R.id.btn_profile);
         //Chuyen qua layout Chart
        imgChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentChart = new Intent(mainScreen.this,chartJs.class);
                startActivity(intentChart);
                finish();
            }
        });
        //Chuyen qua layout Plus
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(mainScreen.this,add_Money.class);
                startActivity(intentAdd);
                finish();
            }
        });
       //Chuyen qua layout Cate
        imgCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCate = new Intent(mainScreen.this,cate_view.class);
                startActivity(intentCate);
                finish();

            }
        });
        imgAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAccount = new Intent(mainScreen.this,account_Setting.class);
                startActivity(intentAccount);
                finish();
            }
        });
        // Nhận dữ liệu từ Intent
        String userFullName = getIntent().getStringExtra("USER_FULL_NAME");

        // Lấy TextView để hiển thị họ và tên người dùng
        TextView viewName = findViewById(R.id.ViewName);

        // Hiển thị họ và tên người dùng lên màn hình
        if (userFullName != null) {
            viewName.setText(userFullName);
        }

    }
}
