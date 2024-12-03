package com.voggella.android.doan.mainHome;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import com.voggella.android.doan.R;
import java.util.Calendar;

public class add_Money extends AppCompatActivity {
    private Spinner spinnerGroups;
    private TextView tvCate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus_cate);

        // Khai báo các View
        ImageView ivDatePicker = ViewUtils.findView(this,R.id.img_date); // ImageView để mở DatePickerDialog
        TextView tvSelectedDate = ViewUtils.findView(this,R.id.tv_date); // TextView hiển thị ngày đã chọn
        // Thiết lập sự kiện OnClick cho ImageView
        ivDatePicker.setOnClickListener(v -> {
            // Lấy ngày hiện tại
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            // Hiển thị DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    add_Money.this, // context (Activity)
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Cập nhật ngày đã chọn vào TextView
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        tvSelectedDate.setText(selectedDate); // Hiển thị ngày trong TextView
                    },
                    year, month, day // Giá trị ngày, tháng, năm hiện tại
            );

            datePickerDialog.show(); // Hiển thị hộp thoại
        });
        String[] groups = {"Nhóm 1", "Nhóm 2", "Nhóm 3", "Nhóm 4"};
//        Xu li su kien cate
        spinnerGroups = ViewUtils.findView(add_Money.this,R.id.spinner_groups);
        tvCate = ViewUtils.findView(add_Money.this,R.id.tv_cate);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, groups);
        spinnerGroups.setAdapter(adapter);
        // Gắn ArrayAdapter vào Spinner
        spinnerGroups.setAdapter(adapter);

//        Xử lí sự kiện nút Hủy
        TextView tvHuy = ViewUtils.findView(add_Money.this,R.id.tv_cancel);
        tvHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cbMain = new Intent(add_Money.this,mainScreen.class);
                startActivity(cbMain);
                finish();
            }
        });
    }
    public static class ViewUtils {

        // Hàm này giúp tìm view từ ID trong Activity
        public static <T extends View> T findView(Activity activity, @IdRes int id) {
            return activity.findViewById(id);
        }

        // Hàm này giúp tìm view từ ID trong Fragment
        public static <T extends View> T findView(View rootView, @IdRes int id) {
            return rootView.findViewById(id);
        }
    }
}

