package com.voggella.android.doan.mainHome;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.voggella.android.doan.GiaoDienLogin.Login;
import com.voggella.android.doan.R;

public class account_Setting extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_view);

        TextView logout = findViewById(R.id.tvLogout);
        TextView homeMain = findViewById(R.id.tvHome);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account_Setting.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
