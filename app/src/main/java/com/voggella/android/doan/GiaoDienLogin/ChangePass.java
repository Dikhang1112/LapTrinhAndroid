package com.voggella.android.doan.GiaoDienLogin;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;

public class ChangePass extends AppCompatActivity {

    private EditText edtNewPass, edtConfirmPass;
    private Button btnChangePass, btnSignIn;
    private SQLiteHelper dbHelper;
    private String userPhone; // Số điện thoại lấy từ Sign In

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass);

        // Nhận số điện thoại từ Sign In
        Intent intent = getIntent();
        userPhone = intent.getStringExtra("USERS_SDT");

        // Khởi tạo UI và SQLiteHelper
        initUI();
        dbHelper = new SQLiteHelper(this);

        // Xử lý sự kiện thay đổi mật khẩu
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        // Xử lý sự kiện quay lại màn hình Sign In
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(ChangePass.this, SignIn.class);
                startActivity(signInIntent);
                finish();
            }
        });
    }

    private void initUI() {
        edtNewPass = findViewById(R.id.pass1_change);
        edtConfirmPass = findViewById(R.id.pass2_change);
        btnChangePass = findViewById(R.id.btnChange);
        btnSignIn = findViewById(R.id.btnSign);
    }

    private void changePassword() {
        String newPassword = edtNewPass.getText().toString().trim();
        String confirmPassword = edtConfirmPass.getText().toString().trim();

        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Hãy nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords không giống nhau", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện cập nhật mật khẩu trong SQLite
        boolean result = updatePassword(userPhone, newPassword);
        if (result) {
            Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean updatePassword(String phone, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_USER_PASSWORD, newPassword);
        if (!dbHelper.isUserExists(phone)) {
            Toast.makeText(this, "Số điện thoại không tồn tại!", Toast.LENGTH_SHORT).show();
            return false;
        }
        int rowsUpdated = db.update(SQLiteHelper.TB_USERS, values, SQLiteHelper.COLUMN_USER_SDT + "=?", new String[]{phone});
        db.close();

        if (rowsUpdated > 0) {
            Log.d("ChangePass", "Password updated for user: " + phone);
            return true;
        } else {
            Log.e("ChangePass", "Failed to update password for user: " + phone);
            return false;
        }
    }
}
