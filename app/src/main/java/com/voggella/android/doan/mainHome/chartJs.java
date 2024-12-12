    package com.voggella.android.doan.mainHome;

    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.TextView;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;
    import com.github.mikephil.charting.charts.BarChart;
    import com.github.mikephil.charting.data.BarData;
    import com.github.mikephil.charting.data.BarDataSet;
    import com.github.mikephil.charting.data.BarEntry;
    import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
    import com.voggella.android.doan.Database.SQLiteHelper;
    import com.voggella.android.doan.R;

    import java.util.ArrayList;
    import java.util.Map;

    public class ChartJs extends AppCompatActivity {

        private BarChart barChart;
        private String phoneUser;
        private SQLiteHelper dbHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.chart_screen);

            //Khoi tao database
            dbHelper = new SQLiteHelper(this);

            // Khởi tạo BarChart
            barChart = findViewById(R.id.barChart);

            // Lấy số điện thoại của người dùng từ Intent
            phoneUser = getIntent().getStringExtra("USERS_SDT");

            if (phoneUser == null) {
                Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy dữ liệu tổng Amount theo từng loại giao dịch
            Map<String, Double> totalAmountByType = getTotalAmountByType(phoneUser);

            if (totalAmountByType == null || totalAmountByType.isEmpty()) {
                Toast.makeText(this, "Không có giao dịch cho người dùng này.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Tìm loại giao dịch có số tiền cao nhất và thấp nhất
            String maxType = null, minType = null;
            double maxAmount = Double.MIN_VALUE;
            double minAmount = Double.MAX_VALUE;

            for (Map.Entry<String, Double> entry : totalAmountByType.entrySet()) {
                double amount = entry.getValue();
                if (amount > maxAmount) {
                    maxAmount = amount;
                    maxType = entry.getKey();
                }
                if (amount < minAmount) {
                    minAmount = amount;
                    minType = entry.getKey();
                }
            }
            TextView viewChiMax = findViewById(R.id.viewChiNhieu);
            TextView viewChiMin = findViewById(R.id.viewChiIt);

            if (maxType != null && minType != null) {
                viewChiMax.setText("Chi nhiều nhất: " + maxType);
                viewChiMin.setText("Chi ít nhất: " + minType);
            }

            // Danh sách các loại giao dịch (categories) để hiển thị trên trục X
            ArrayList<String> categories = new ArrayList<>(totalAmountByType.keySet());

            // Dữ liệu cho biểu đồ (tạo BarEntry cho mỗi loại giao dịch)
            ArrayList<BarEntry> entries = new ArrayList<>();
            int i = 0;
            for (Map.Entry<String, Double> entry : totalAmountByType.entrySet()) {
                // Thêm vào BarEntry (i là vị trí cột, entry.getValue() là giá trị tổng Amount)
                entries.add(new BarEntry(i, entry.getValue().floatValue()));
                i++;
            }
            Log.d("ChartJs", "Số lượng cột: " + entries.size());
            // Tạo BarDataSet
            BarDataSet barDataSet = new BarDataSet(entries, "Số tiền cho các giao dịch");
            barDataSet.setColor(getResources().getColor(R.color.colorPrimary));
            // Ẩn các nhãn trên các cột
            barDataSet.setDrawValues(false);
            // Tạo BarData
            BarData barData = new BarData(barDataSet);
            // Thiết lập các thuộc tính của biểu đồ
            configureChartAppearance();
            // Cài đặt dữ liệu cho biểu đồ
            barChart.setData(barData);
            // Tạo hiệu ứng động cho biểu đồ
            barChart.animateXY(2000,2000); // Tạo hiệu ứng động theo chiều Y trong 1000ms (1 giây)
            barChart.invalidate(); // Cập nhật lại biểu đồ
            //Khoi tao cac textView
            TextView viewThu = findViewById(R.id.viewThu);
            TextView viewChi = findViewById(R.id.viewChi);

             //Set cac gia tri cho text View
            double totalThu = getTotalBudget(phoneUser);
            double totalChi = getTotalTransaction(phoneUser);
            String totalBudget = "Tổng thu:" + totalThu + " vnd";
            String totalTran = "Tổng chi:" + totalChi + " vnd";
            viewThu.setText(totalBudget);
            viewChi.setText(totalTran);
        }

        private Map<String, Double> getTotalAmountByType(String sdt) {
            SQLiteHelper dbHelper = new SQLiteHelper(this);
            Map<String, Double> result = dbHelper.getTotalAmountByType(sdt);

            if (result == null || result.isEmpty()) {
                Log.d("ChartJs", "Không có dữ liệu giao dịch.");
            } else {
                Log.d("ChartJs", "Dữ liệu giao dịch: " + result.toString());
            }
            return result;
        }
        // Phương thức cấu hình giao diện biểu đồ
        private void configureChartAppearance() {
            // Điều chỉnh trục X và Y để hiển thị rõ hơn
            barChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);  // Đảm bảo mỗi cột có nhãn riêng
            barChart.getXAxis().setTextSize(12f);  // Thay đổi kích thước chữ trên trục X
            // Thiết lập trục Y
            barChart.getAxisLeft().setEnabled(true);  // Hiển thị trục Y trái
            barChart.getAxisRight().setEnabled(false);  // Tắt trục Y phải
            barChart.getAxisLeft().setTextSize(12f); // Kích thước chữ trục Y
            barChart.getAxisLeft().setGranularity(1f); // Đảm bảo các nhãn trục Y hiển thị đúng
            // Thiết lập trục X (nhãn cho các loại giao dịch)
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new ArrayList<>(getTotalAmountByType(phoneUser).keySet())));
            // Tắt phần mô tả dưới biểu đồ
            barChart.getDescription().setEnabled(false);

        }
        //Tinh tong thu
        private double getTotalBudget(String phoneUser) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            double total = 0;
            Cursor cursor = null;

            try {
                String query = "SELECT SUM(" + SQLiteHelper.COLUMN_BUDGET_DATA + ") FROM " + SQLiteHelper.TB_Budget + " WHERE " + SQLiteHelper.COLUMN_BUDGET_USERS_SDT + " = ?";
                cursor = db.rawQuery(query, new String[]{phoneUser});

                if (cursor != null && cursor.moveToFirst()) {
                    total = cursor.getDouble(0);
                }
            } catch (Exception e) {
                Log.e("mainScreen", "Lỗi khi tính tổng ngân sách", e);
            } finally {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }

            return total;
        }

        // Lấy tổng tiền trong bảng Transaction
        private double getTotalTransaction(String phoneUser) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            double total = 0;
            Cursor cursor = null;

            try {
                String query = "SELECT SUM(" + SQLiteHelper.COLUMN_TRANSACTION_AMOUNT + ") FROM " + SQLiteHelper.TB_Trans + " WHERE " + SQLiteHelper.COLUMN_TRANSACTION_USERS_SDT + " = ?";
                cursor = db.rawQuery(query, new String[]{phoneUser});

                if (cursor != null && cursor.moveToFirst()) {
                    total = cursor.getDouble(0);
                }
            } catch (Exception e) {
                Log.e("mainScreen", "Lỗi khi tính tổng giao dịch", e);
            } finally {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }
            return total;
        }
    }
