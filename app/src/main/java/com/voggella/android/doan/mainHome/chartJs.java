package com.voggella.android.doan.mainHome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.voggella.android.doan.Database_Adapter.SQLiteHelper;
import com.voggella.android.doan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class chartJs extends AppCompatActivity {

    private BarChart barChart;
    private String phoneUser;
    private String userName;
    private SQLiteHelper dbHelper;
    private Spinner monthSpinner, yearSpinner;
    private FooterLayout footerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_screen);

        // Khởi tạo database
        dbHelper = new SQLiteHelper(this);

        // Khởi tạo BarChart
        barChart = findViewById(R.id.barChart);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
         phoneUser = intent.getStringExtra("USERS_SDT");
         userName = intent.getStringExtra("USER_FULL_NAME");

        // Khởi tạo FooterLayout và truyền dữ liệu vào
        footerLayout = findViewById(R.id.footerLayout);
        footerLayout.setUserData(phoneUser, userName);


        if (phoneUser == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cài đặt Spinner cho danh sách tháng
        monthSpinner = findViewById(R.id.monthSpinner);
        ArrayAdapter<CharSequence> adapterMonth = ArrayAdapter.createFromResource(this, R.array.months_array, R.layout.spinner_dropdown_item);
        adapterMonth.setDropDownViewResource(R.layout.spinner_dropdown_item);
        monthSpinner.setAdapter(adapterMonth);

        // Cài đặt Spinner cho danh sách năm
        yearSpinner = findViewById(R.id.yearSpinner);
        ArrayAdapter<CharSequence> adapterYear = ArrayAdapter.createFromResource(this, R.array.years_array, R.layout.spinner_dropdown_item);
        adapterYear.setDropDownViewResource(R.layout.spinner_dropdown_item);
        yearSpinner.setAdapter(adapterYear);

        // Lắng nghe sự kiện chọn tháng và năm
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateChartAndSummary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        };

        monthSpinner.setOnItemSelectedListener(listener);
        yearSpinner.setOnItemSelectedListener(listener);

        // Tải dữ liệu mặc định (tháng và năm hiện tại)
        Calendar calendar = Calendar.getInstance();
        monthSpinner.setSelection(calendar.get(Calendar.MONTH));
        yearSpinner.setSelection(calendar.get(Calendar.YEAR) - 2020); // Nếu năm bắt đầu từ 2020
        updateChartAndSummary();
    }

    private void updateChartAndSummary() {
        int selectedMonth = monthSpinner.getSelectedItemPosition() + 1; // Lấy tháng từ Spinner
        String selectedYear = yearSpinner.getSelectedItem().toString(); // Lấy năm từ Spinner

        // Lấy số năm thực sự, loại bỏ phần chữ "Năm " nếu có
        selectedYear = selectedYear.replace("Năm ", "");

        updateChartForMonthAndYear(selectedMonth, selectedYear);
        updateSummaryInfo(selectedMonth, selectedYear);
        getMostAndLeastFrequentTransactionTypes(phoneUser, selectedMonth, selectedYear);
    }


    private void updateChartForMonthAndYear(int month, String year) {
        Map<String, Double> totalAmountByType = dbHelper.getTotalAmountByTypeForMonthAndYear(phoneUser, month, Integer.parseInt(year));

        if (totalAmountByType == null && totalAmountByType.isEmpty()) {
            Toast.makeText(this, "Không có giao dịch cho tháng và năm này.", Toast.LENGTH_SHORT).show();
            barChart.clear();
            barChart.invalidate();
            return;
        }

        ArrayList<String> categories = new ArrayList<>(totalAmountByType.keySet());
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>(); // Danh sách màu sắc cho từng cột
        int i = 0;

        // Tạo danh sách màu sắc cho các cột
        for (Map.Entry<String, Double> entry : totalAmountByType.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue().floatValue()));

            // Thêm màu sắc cho từng cột. Bạn có thể thay đổi màu tùy ý.
            if (i % 2 == 0) {
                colors.add(Color.RED); // Màu đỏ cho cột chẵn
            } else {
                colors.add(Color.BLUE); // Màu xanh dương cho cột lẻ
            }

            i++;
        }
        BarDataSet barDataSet = new BarDataSet(entries, "Số tiền cho các giao dịch");
        barDataSet.setValueTextSize(10f);
        barDataSet.setValueTextColor(Color.MAGENTA);
        barDataSet.setColors(colors); // Áp dụng màu sắc cho các cột
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(categories));

        configureChartAppearance();
        barChart.animateXY(1000, 1000);
        barChart.invalidate();
    }

    private void updateSummaryInfo(int month, String year) {
        TextView viewThu = findViewById(R.id.viewThu);
        TextView viewChi = findViewById(R.id.viewChi);

        double totalThu = getTotalBudgetForMonthAndYear(phoneUser, month, year);
        double totalChi = getTotalTransactionForMonthAndYear(phoneUser, month, year);

        viewThu.setText("Tổng thu: " + totalThu + " VND");
        viewChi.setText("Tổng chi: " + totalChi + " VND");
    }

    private double getTotalBudgetForMonthAndYear(String phoneUser, int month, String year) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double total = 0;
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(" + SQLiteHelper.COLUMN_BUDGET_DATA + ") " +
                    "FROM " + SQLiteHelper.TB_Budget + " " +
                    "WHERE " + SQLiteHelper.COLUMN_BUDGET_USERS_SDT + " = ? " +
                    "AND strftime('%m', substr(" + SQLiteHelper.COLUMN_BUDGET_DATE + ", 7, 4) || '-' || substr(" + SQLiteHelper.COLUMN_BUDGET_DATE + ", 4, 2) || '-' || substr(" + SQLiteHelper.COLUMN_BUDGET_DATE + ", 1, 2)) = ? " +
                    "AND strftime('%Y', substr(" + SQLiteHelper.COLUMN_BUDGET_DATE + ", 7, 4) || '-' || substr(" + SQLiteHelper.COLUMN_BUDGET_DATE + ", 4, 2) || '-' || substr(" + SQLiteHelper.COLUMN_BUDGET_DATE + ", 1, 2)) = ?";

            String monthString = String.format("%02d", month);
            cursor = db.rawQuery(query, new String[]{phoneUser, monthString, year});

            if (cursor != null && cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("ChartJs", "Lỗi khi tính tổng ngân sách theo tháng và năm", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return total;
    }

    private double getTotalTransactionForMonthAndYear(String phoneUser, int month, String year) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double total = 0;
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(" + SQLiteHelper.COLUMN_TRANSACTION_AMOUNT + ") " +
                    "FROM " + SQLiteHelper.TB_Trans + " " +
                    "WHERE " + SQLiteHelper.COLUMN_TRANSACTION_USERS_SDT + " = ? " +
                    "AND strftime('%m', " + SQLiteHelper.COLUMN_TRANSACTION_DATE + ") = ? " +
                    "AND strftime('%Y', " + SQLiteHelper.COLUMN_TRANSACTION_DATE + ") = ?";

            String monthString = String.format("%02d", month);
            cursor = db.rawQuery(query, new String[]{phoneUser, monthString, year});

            if (cursor != null && cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("ChartJs", "Lỗi khi tính tổng giao dịch theo tháng và năm", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null)
                if (db != null) db.close();
        }
        return total;
    }

    private void configureChartAppearance() {
        barChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setTextSize(12f);
        barChart.getAxisLeft().setEnabled(true);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setTextSize(12f);
        barChart.getDescription().setEnabled(false);
    }

    private void getMostAndLeastFrequentTransactionTypes(String phoneUser, int month, String year) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + SQLiteHelper.COLUMN_TRANSACTION_TYPE + ", SUM(" + SQLiteHelper.COLUMN_TRANSACTION_AMOUNT + ") as total_amount " +
                    "FROM " + SQLiteHelper.TB_Trans + " " +
                    "WHERE " + SQLiteHelper.COLUMN_TRANSACTION_USERS_SDT + " = ? " +
                    "AND strftime('%m', " + SQLiteHelper.COLUMN_TRANSACTION_DATE + ") = ? " +
                    "AND strftime('%Y', " + SQLiteHelper.COLUMN_TRANSACTION_DATE + ") = ? " +
                    "GROUP BY " + SQLiteHelper.COLUMN_TRANSACTION_TYPE;

            String monthString = String.format("%02d", month);
            cursor = db.rawQuery(query, new String[]{phoneUser, monthString, year});

            String mostFrequentType = "";
            String leastFrequentType = "";
            double mostFrequentAmount = 0;
            double leastFrequentAmount = Double.MAX_VALUE;

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String transactionType = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TRANSACTION_TYPE));
                    @SuppressLint("Range") double totalAmount = cursor.getDouble(cursor.getColumnIndex("total_amount"));

                    if (totalAmount > mostFrequentAmount) {
                        mostFrequentAmount = totalAmount;
                        mostFrequentType = transactionType;
                    }

                    if (totalAmount < leastFrequentAmount) {
                        leastFrequentAmount = totalAmount;
                        leastFrequentType = transactionType;
                    }
                } while (cursor.moveToNext());
            }

            TextView viewThuMax = findViewById(R.id.viewChiNhieu);
            TextView viewThuMin = findViewById(R.id.viewChiIt);

            viewThuMax.setText("Chi nhiều nhất: " + (mostFrequentType.isEmpty() ? "Không có dữ liệu" : mostFrequentType ));
            viewThuMin.setText("Chi ít nhất: " + (leastFrequentType.isEmpty() ? "Không có dữ liệu" : leastFrequentType ));

        } catch (Exception e) {
            Log.e("ChartJs", "Lỗi khi tính toán loại giao dịch cao nhất và thấp nhất", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}
