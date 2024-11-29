package com.voggella.android.doan.GiaoDienLogin;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.voggella.android.doan.R;

public class SignUp extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Button backSign = findViewById(R.id.back_SignIn);
         backSign.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intentComeSign = new Intent(SignUp.this, SignIn.class);
                 intentComeSign.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(intentComeSign);
                 finish();
             }
         });
    }
}
