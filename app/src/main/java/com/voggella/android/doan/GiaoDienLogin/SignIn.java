package com.voggella.android.doan.GiaoDienLogin;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.voggella.android.doan.R;

public class SignIn  extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        Button backHome = findViewById(R.id.backLogin);

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(SignIn.this, Login.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentHome);
                finish();
            }
        });

        Button changePass = findViewById(R.id.changepass);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentChangePass = new Intent(SignIn.this, ChangePass.class);
                intentChangePass.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentChangePass);
                finish();
            }
        });

        Button screenWait = findViewById(R.id.user_SignIn);
        screenWait.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intentScreenWait = new Intent(SignIn.this, ScreenWait.class);
        intentScreenWait.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentScreenWait);
        finish();
    }
});


    }
}
