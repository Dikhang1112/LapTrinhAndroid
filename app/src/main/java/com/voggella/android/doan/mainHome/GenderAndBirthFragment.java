package com.voggella.android.doan.mainHome;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.voggella.android.doan.R;

import java.util.Calendar;

public class GenderAndBirthFragment extends DialogFragment {
    private EditText editTextGender;
    private TextView textViewDateOfBirth;
    private Button buttonSave;

    private OnDataSavedListener listener;

    public interface OnDataSavedListener {
        void onDataSaved(String gender, String dateOfBirth);
    }

    public static GenderAndBirthFragment newInstance(String currentGender, String currentDateOfBirth) {
        GenderAndBirthFragment fragment = new GenderAndBirthFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gender_and_birth, container, false);



        // Khởi tạo các view
        editTextGender = ((View) view).findViewById(R.id.editTextGender);
        textViewDateOfBirth = view.findViewById(R.id.textViewDateOfBirth);
        buttonSave = view.findViewById(R.id.buttonSave);


        // Hiển thị DatePickerDialog khi người dùng nhấn vào TextView để chọn ngày sinh
        textViewDateOfBirth.setOnClickListener(v -> {
            showDatePicker();
        });

        // Lắng nghe sự kiện click vào nút Lưu
        buttonSave.setOnClickListener(v -> {
            String gender = editTextGender.getText().toString();
            String dateOfBirth = textViewDateOfBirth.getText().toString();
            if (listener != null) {
                listener.onDataSaved(gender, dateOfBirth); // Trả dữ liệu về activity
                dismiss(); // Đóng fragment
            }
        });

        return view;
    }

    // Hiển thị DatePickerDialog để chọn ngày sinh
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textViewDateOfBirth.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    // Cung cấp listener để Activity nhận dữ liệu từ Fragment
    public void setOnDataSavedListener(OnDataSavedListener listener) {
        this.listener = listener;
    }
}

