package com.voggella.android.doan.mainHome;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.voggella.android.doan.R;

public class mainScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Button k = findViewById(R.id.btnKHang);

        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mainScreen.this, "KKKKKK", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
