package com.voggella.android.doan.GiaoDienLogin;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.voggella.android.doan.R;

public class SignUp extends AppCompatActivity{
    private EditText edtPhone, edtPassword1, edtPassword2;
    private Button btnRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        edtPhone = findViewById(R.id.phone_signup);
        edtPassword1 = findViewById(R.id.pass1_signup);
        edtPassword2 = findViewById(R.id.pass2_signup);
        btnRegister = findViewById(R.id.signup);
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
    public void onRegisterClick(View view) {
        String phone = edtPhone.getText().toString();
        String password1 = edtPassword1.getText().toString();
        String password2 = edtPassword2.getText().toString();

        // Kiểm tra thông tin hợp lệ trước khi chuyển màn hình
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password1)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password1.equals(password2)) {
            // Nếu mật khẩu khớp, chuyển sang layout khác
            Intent intent = new Intent(SignUp.this,checkInfor.class);
            intent.putExtra("phone", edtPhone.getText().toString());
            intent.putExtra("password1", password1);
            startActivity(intent); // Chuyển sang màn hình UserInfoActivity
            finish(); // Kết thúc RegisterActivity
        } else {
            // Nếu mật khẩu không khớp, hiển thị thông báo
            Toast.makeText(SignUp.this, "Mật khẩu không khớp. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }
}


