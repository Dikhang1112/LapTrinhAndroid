package com.voggella.android.doan.GiaoDienLogin;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;

public class checkInfor  extends AppCompatActivity {
    private SQLiteHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_infor);
        //Khoi tạo Sql
        dbHelper = new SQLiteHelper(this);
        EditText edtFullName = findViewById(R.id.edtHoten);
        EditText edtAddress = findViewById(R.id.edtDiachi);
        EditText edtDateOfBirth = findViewById(R.id.edtDate);
        EditText edtCCCD = findViewById(R.id.edtCc);
        RadioGroup radioGender = findViewById(R.id.rdGroup);
        EditText edtNotes = findViewById(R.id.edtBoSung);
        Button btnSave = findViewById(R.id.btnConfirm);

        // Nhận dữ liệu từ Layout 1
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String password1 = intent.getStringExtra("password1");


        btnSave.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString();
            String address = edtAddress.getText().toString();
            String dob = edtDateOfBirth.getText().toString();
            String cccd = edtCCCD.getText().toString();
            String gender = ((RadioButton) findViewById(radioGender.getCheckedRadioButtonId())).getText().toString();
            String notes = edtNotes.getText().toString();
            if (phone.isEmpty() || password1.isEmpty() || fullName.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Lưu dữ liệu vào SQLite
            dbHelper.addUser(phone, password1, fullName, address, dob, cccd, gender,notes,"user");
            Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
            if (dbHelper.isUserExists(phone)) {
                Toast.makeText(this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (radioGender.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Vui lòng chọn giới tính!", Toast.LENGTH_SHORT).show();
                return;
            }
        });

//        Nút trở về
        Button btnBack = findViewById(R.id.btnBackSignUp);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(checkInfor.this,SignUp.class);
                startActivity(intentBack);
                finish();
            }
        });
    }
}