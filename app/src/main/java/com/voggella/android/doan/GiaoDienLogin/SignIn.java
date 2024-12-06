package com.voggella.android.doan.GiaoDienLogin;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;
import com.voggella.android.doan.mainHome.mainScreen;

public class SignIn extends AppCompatActivity {
    @SuppressLint("Range")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        //Back login
        Button backHome = findViewById(R.id.backLogin);
        backHome.setOnClickListener(view -> {
            Intent intentHome = new Intent(SignIn.this, Login.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentHome);
            finish();
        });

        //Lay id những element khác
        EditText edtPhone = findViewById(R.id.user_Sdt);
        EditText edtPass = findViewById(R.id.pass_SignIn);
        Button signIn = findViewById(R.id.user_SignIn);

        signIn.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(SignIn.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteHelper dbHelper = new SQLiteHelper(SignIn.this);

            try {
                // Kiểm tra người dùng có hợp lệ hay không
                boolean isValid = dbHelper.validateUser(phone, pass);
                if (isValid) {
                    // Nếu hợp lệ, truy vấn để lấy họ và tên từ cơ sở dữ liệu
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.query(SQLiteHelper.TABLE_USERS,
                            new String[]{SQLiteHelper.COLUMN_USER_NAME},
                            SQLiteHelper.COLUMN_USER_SDT + " = ?",
                            new String[]{phone},
                            null, null, null);

                    String userFullName = "";
                    if (cursor != null && cursor.moveToFirst()) {
                        userFullName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_USER_NAME));
                        cursor.close();
                    }
                    // Chuyển sang màn hình chính và truyền họ và tên người dùng
                    Intent intent = new Intent(SignIn.this, mainScreen.class);
                    intent.putExtra("USER_FULL_NAME", userFullName);  // Truyền họ và tên
                    startActivity(intent);
                    finish();  // Để không quay lại màn hình đăng nhập
                } else {
                    Toast.makeText(SignIn.this, "Sai số điện thoại hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(SignIn.this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Button changePass = findViewById(R.id.changepass);
        changePass.setOnClickListener(view -> {
            String phone = edtPhone.getText().toString().trim();  // Lấy số điện thoại từ EditText
            if (phone.isEmpty()) {
                Toast.makeText(SignIn.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Truyền số điện thoại vào Intent khi chuyển sang màn hình ChangePass
            Intent intentChangePass = new Intent(SignIn.this, ChangePass.class);
            intentChangePass.putExtra("USERS_SDT", phone);  // Truyền số điện thoại vào Intent
            intentChangePass.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentChangePass);
        });
    }
}
