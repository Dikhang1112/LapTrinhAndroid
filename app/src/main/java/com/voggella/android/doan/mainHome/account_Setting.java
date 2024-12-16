package com.voggella.android.doan.mainHome;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.voggella.android.doan.Database_Adapter.SQLiteHelper;
import com.voggella.android.doan.GiaoDienLogin.Login;
import com.voggella.android.doan.GiaoDienLogin.AdminManagementActivity;
import com.voggella.android.doan.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class account_Setting extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_PERMISSION_CODE = 1;
    private TextView tvSdt, tvSex, tvBirth;
    private SQLiteHelper dbHelper;
    private String phoneUser;
    private String userName;
    private CircleImageView profileImage;
    private FooterLayout footerLayout;
    private ImageView crownImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_view);
        // Khởi tạo UI
        TextView logout = findViewById(R.id.tvLogout);
        profileImage = findViewById(R.id.profileImage);

        // Khởi tạo database
        dbHelper = new SQLiteHelper(this);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
         phoneUser = intent.getStringExtra("USERS_SDT");
         userName = intent.getStringExtra("USER_FULL_NAME");

        // Khởi tạo FooterLayout và truyền dữ liệu vào
        footerLayout = findViewById(R.id.footerLayout);
        footerLayout.setUserData(phoneUser, userName);

        TextView username = findViewById(R.id.tvUsername);
        String fullName = getUserFullName(phoneUser);
        username.setText(fullName != null ? fullName : "Người dùng không xác định");

        // Hiển thị ảnh đại diện nếu đã lưu
        loadUserProfileImage(phoneUser);

        // Xu li hien thi thong tin nguoi dung
        tvSdt = findViewById(R.id.tvViewSdt);
        tvSex = findViewById(R.id.tvViewSex);
        tvBirth = findViewById(R.id.tvViewBirth);

        if (phoneUser != null) {
            // Truy vấn thông tin người dùng
            Cursor cursor = dbHelper.getUserInfo(phoneUser);

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy dữ liệu từ Cursor
                String sdt = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_USER_SDT));
                String sex = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_USER_SEX));
                String birth = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_USER_BIRTHDAY));
                // Hiển thị dữ liệu lên TextView
                if(sex == null && birth == null )
                {
                    tvBirth.setText("Ban chưa nhập thông tin");
                    tvSex.setText("Click vào đây để bổ sung thông tin");
                }
                else {
                    tvSex.setText("Giới tính: " + sex);
                    tvBirth.setText("Ngày sinh " + birth);
                }
                tvSdt.setText("SDT: " + sdt);
                cursor.close(); // Đóng Cursor
            } else {
                Toast.makeText(this, "User không có thông tin ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không nhận được thông tin user", Toast.LENGTH_SHORT).show();
        }
        //Xu li su kien an hien ImageView
        crownImage = findViewById(R.id.crownImage);
        int userType = dbHelper.getUserType(phoneUser);
        // Kiểm tra user_type để ẩn/hiện ImageView
        if (userType == 0) {
            crownImage.setVisibility(ImageView.GONE); // Ẩn vương miện
        } else if (userType == 1) {
            crownImage.setVisibility(ImageView.VISIBLE); // Hiển thị vương miện
        }
        // Xử lý sự kiện đăng xuất
        logout.setOnClickListener(v -> {
            Intent intentLog = new Intent(account_Setting.this, Login.class);
            startActivity(intentLog);
            finish();
        });

        // Xử lý sự kiện chọn ảnh đại diện
        profileImage.setOnClickListener(v -> openImagePicker());

    }
    // Kiểm tra và yêu cầu quyền
    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_CODE);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        }
    }

    // Mở thư viện ảnh
    private void openImagePicker() {
        // Kiểm tra và yêu cầu quyền
        checkAndRequestPermissions();
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
            try {
                // Lưu ảnh vào bộ nhớ cục bộ
                String localImagePath = saveImageLocally(imageUri);

                if (localImagePath != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localImagePath);
                    profileImage.setImageBitmap(bitmap); // Hiển thị ảnh đại diện

                    // Lưu URI cục bộ vào cơ sở dữ liệu
                    saveUserProfileImage(phoneUser, localImagePath);
                }
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi tải ảnh", Toast.LENGTH_SHORT).show();
                Log.e("ProfileImageError", "Error loading image", e);
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
            Log.e("DatabaseError", "Error saving image path", e);
        }
    }

    // Tải ảnh đại diện từ cơ sở dữ liệu
    private void loadUserProfileImage(String phoneUser) {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + SQLiteHelper.COLUMN_USER_IMAGE + " FROM " + SQLiteHelper.TB_USERS + " WHERE " + SQLiteHelper.COLUMN_USER_SDT + " = ?", new String[]{phoneUser})) {

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String imagePath = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_USER_IMAGE));
                if (imagePath != null) {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        profileImage.setImageBitmap(bitmap);
                    } else {
                        profileImage.setImageResource(R.drawable.ht2); // Ảnh mặc định
                    }
                }
            }
        } catch (Exception e) {
            profileImage.setImageResource(R.drawable.ht2); // Ảnh mặc định khi lỗi
            Toast.makeText(this, "Lỗi khi tải ảnh đại diện", Toast.LENGTH_SHORT).show();
            Log.e("ProfileImageError", "Error loading profile image", e);
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
            Log.e("DatabaseError", "Error fetching user name", e);
        }
        return null;
    }

    // Lưu ảnh vào bộ nhớ cục bộ
    private String saveImageLocally(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                File localFile = new File(getFilesDir(), "profile_" + phoneUser + ".jpg");

                try (FileOutputStream out = new FileOutputStream(localFile)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                }
                return localFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
