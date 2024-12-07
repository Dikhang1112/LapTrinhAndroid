package com.voggella.android.doan.Database;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.voggella.android.doan.R;

public class BudgetFragment extends Fragment {

    private EditText inputData, inputDate;
    private Button btnAddBudget;
    private SQLiteHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View rootView = inflater.inflate(R.layout.fragment_budget, container, false);

        // Khởi tạo các view
        inputData = rootView.findViewById(R.id.inputData);
        inputDate = rootView.findViewById(R.id.inputDate);
        btnAddBudget = rootView.findViewById(R.id.btnAddBudget);

        // Khởi tạo SQLiteHelper để thao tác với cơ sở dữ liệu
        dbHelper = new SQLiteHelper(getContext());

        // Đặt sự kiện cho nút Lưu
        btnAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các EditText
                String data = inputData.getText().toString();
                String date = inputDate.getText().toString();

                if (!data.isEmpty() && !date.isEmpty()) {
                    // Chèn dữ liệu vào bảng Budget
                    insertBudgetData(data, date);
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    // Hàm chèn dữ liệu vào bảng Budget
    private void insertBudgetData(String data, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("data", Double.parseDouble(data));  // Đảm bảo data là kiểu số (Real)
        values.put("date", date);

        // Chèn dữ liệu vào bảng Budget
        long result = db.insert("Budget", null, values);

        if (result != -1) {
            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();

            // Cập nhật ViewBudget trong màn hình chính
            updateMainViewBudget(data);

            // Quay lại màn hình chính
            getFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    // Cập nhật ViewBudget trong màn hình chính
    private void updateMainViewBudget(String data) {
        // Lấy TextView ViewBudget trong màn hình chính
        TextView viewBudget = getActivity().findViewById(R.id.ViewBudget);
        viewBudget.setText("Số dư: " + data + "đ");
    }
}
