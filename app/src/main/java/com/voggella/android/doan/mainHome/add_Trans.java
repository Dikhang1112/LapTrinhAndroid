package com.voggella.android.doan.mainHome;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class add_Trans extends AppCompatActivity {
    private Spinner spinnerCategory;
    private TextView tvCate;
    private SQLiteHelper dbHelper;
    private EditText etAmount, etNote;
    private Button btnSaveTransaction;
    private TextView tvSelectedDate; // Hiển thị ngày đã chọn
    private ImageView ivDatePicker; // ImageView để mở DatePickerDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus_cate);

        dbHelper = new SQLiteHelper(this);
        // Binding các phần tử giao diện
        ivDatePicker = findViewById(R.id.img_date); // ImageView để mở DatePickerDialog
        tvSelectedDate = findViewById(R.id.tv_date); // TextView hiển thị ngày đã chọn
        etAmount = findViewById(R.id.et_amount);
        etNote = findViewById(R.id.et_note);
        spinnerCategory = findViewById(R.id.spinner_groupsCate);
        btnSaveTransaction = findViewById(R.id.btn_SaveTransac);
        tvCate = findViewById(R.id.tv_cate); // TextView để hiển thị tên category đã chọn

        // Thiết lập sự kiện OnClick cho ImageView (DatePicker)
        ivDatePicker.setOnClickListener(v -> {
            // Lấy ngày hiện tại
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Hiển thị DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    add_Trans.this, // context (Activity)
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Cập nhật ngày đã chọn vào TextView
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        tvSelectedDate.setText(selectedDate); // Hiển thị ngày trong TextView
                    },
                    year, month, day // Giá trị ngày, tháng, năm hiện tại
            );

            datePickerDialog.show(); // Hiển thị hộp thoại
        });

        // Xử lý sự kiện nút Lưu giao dịch
        btnSaveTransaction.setOnClickListener(v -> {
            // Lấy dữ liệu từ giao diện
            String amountStr = etAmount.getText().toString();
            String note = etNote.getText().toString();
            String date = tvSelectedDate.getText().toString();
            String categoryName = spinnerCategory.getSelectedItem().toString();

            // Kiểm tra dữ liệu
            if (amountStr.isEmpty() || date.isEmpty() || categoryName.isEmpty()) {
                Toast.makeText(add_Trans.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            int categoryId = getCategoryIdFromName(categoryName);

            if (categoryId == -1) {
                Toast.makeText(add_Trans.this, "Danh mục không tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy đối tượng SQLiteDatabase từ dbHelper
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Chèn giao dịch vào cơ sở dữ liệu
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.TB_Trans_AccountId, 1);  // Chắc chắn rằng bạn có accountId hợp lệ
            values.put(SQLiteHelper.TB_Trans_Amount, amount);
            values.put(SQLiteHelper.TB_Trans_Type, categoryName);
            values.put(SQLiteHelper.TB_Trans_Date, date);
            values.put(SQLiteHelper.TB_Trans_CateId, categoryId);
            values.put(SQLiteHelper.TB_Trans_Descrip, note);

            long result = db.insert(SQLiteHelper.TB_Trans, null, values);  // Chèn vào bảng giao dịch

            if (result == -1) {
                Toast.makeText(add_Trans.this, "Lưu giao dịch thất bại", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(add_Trans.this, "Giao dịch đã được lưu", Toast.LENGTH_SHORT).show();
            }
            db.close();  // Đảm bảo đóng cơ sở dữ liệu sau khi sử dụng
            // Quay lại màn hình chính
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent); // Để cập nhật RecyclerView
            finish();
        });



        // Lấy danh sách Category và gán cho Spinner
        loadCategories();

        // Lắng nghe sự kiện chọn item trong Spinner
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Khi chọn 1 mục trong Spinner, cập nhật vào TextView tv_cate
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                tvCate.setText(selectedCategory); // Cập nhật TextView với tên danh mục đã chọn
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không làm gì nếu không có mục nào được chọn
            }
        });
    }

    // Phương thức lấy ID danh mục từ tên danh mục
    private int getCategoryIdFromName(String categoryName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLiteHelper.TB_Cate, new String[]{SQLiteHelper.TB_Cate_Id},
                SQLiteHelper.TB_Cate_Name + "=?", new String[]{categoryName},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.TB_Cate_Id));
            cursor.close();
            db.close();
            return categoryId;
        }
        db.close();
        return -1; // Trả về -1 nếu không tìm thấy danh mục
    }

    // Phương thức để load danh sách danh mục vào Spinner
    private void loadCategories() {
        List<String> categoryList = getCategoryList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    // Lấy danh sách Category từ cơ sở dữ liệu
    @SuppressLint("Range")
    private List<String> getCategoryList() {
        List<String> categoryList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLiteHelper.TB_Cate, new String[]{SQLiteHelper.TB_Cate_Name},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                categoryList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.TB_Cate_Name)));
            }
            cursor.close();
        }
        db.close();
        return categoryList;
    }
}



