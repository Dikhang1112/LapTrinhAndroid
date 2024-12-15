package com.voggella.android.doan.mainHome;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.GiaoDienLogin.Login;
import com.voggella.android.doan.R;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class account_Setting extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private SQLiteHelper dbHelper;
    private String phoneUser;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_view);

        // Khởi tạo UI
        TextView logout = findViewById(R.id.tvLogout);
        TextView homeMain = findViewById(R.id.tvHome);
        profileImage = findViewById(R.id.profileImage);

        // Khởi tạo database
        dbHelper = new SQLiteHelper(this);

        // Lấy thông tin người dùng
        phoneUser = getIntent().getStringExtra("USERS_SDT");
        TextView userName = findViewById(R.id.tvUsername);
        String fullName = getUserFullName(phoneUser);
        userName.setText(fullName != null ? fullName : "Người dùng không xác định");

        // Hiển thị ảnh đại diện nếu đã lưu
        loadUserProfileImage(phoneUser);

        // Xử lý sự kiện đăng xuất
        logout.setOnClickListener(v -> {
            Intent intent = new Intent(account_Setting.this, Login.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện chọn ảnh đại diện
        profileImage.setOnClickListener(v -> openImagePicker());
    }

    // Mở thư viện ảnh
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    // Nhận kết quả sau khi chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    profileImage.setImageBitmap(bitmap); // Hiển thị ảnh đại diện

                    // Lưu ảnh vào cơ sở dữ liệu
                    saveUserProfileImage(phoneUser, imageUri.toString());
                }
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi tải ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Lưu đường dẫn ảnh vào cơ sở dữ liệu
    private void saveUserProfileImage(String phoneUser, String imagePath) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_USER_IMAGE, imagePath);

            int rows = db.update(SQLiteHelper.TB_USERS, values, SQLiteHelper.COLUMN_USER_SDT + "=?", new String[]{phoneUser});
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cập nhật ảnh đại diện thất bại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi lưu ảnh vào cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    // Tải ảnh đại diện từ cơ sở dữ liệu
    private void loadUserProfileImage(String phoneUser) {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + SQLiteHelper.COLUMN_USER_IMAGE + " FROM " + SQLiteHelper.TB_USERS + " WHERE " + SQLiteHelper.COLUMN_USER_SDT + " = ?", new String[]{phoneUser})) {

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String imagePath = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_USER_IMAGE));
                if (imagePath != null) {
                    profileImage.setImageURI(Uri.parse(imagePath));
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải ảnh đại diện", Toast.LENGTH_SHORT).show();
        }
    }

    // Lấy tên người dùng từ cơ sở dữ liệu
    @SuppressLint("Range")
    private String getUserFullName(String phoneUser) {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + SQLiteHelper.COLUMN_USER_NAME + " FROM " + SQLiteHelper.TB_USERS + " WHERE " + SQLiteHelper.COLUMN_USER_SDT + " = ?", new String[]{phoneUser})) {

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_USER_NAME));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi lấy tên người dùng", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
