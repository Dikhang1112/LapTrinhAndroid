package com.voggella.android.doan.mainHome;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.voggella.android.doan.R;
import java.util.ArrayList;

public class chartJs extends AppCompatActivity {
    private BarChart barChart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_screen);
        barChart = findViewById(R.id.barChart);

        // Tạo dữ liệu cho BarChart
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(0f, 1f));
//        entries.add(new BarEntry(1f, 2f));
//        entries.add(new BarEntry(2f, 0f));
//        entries.add(new BarEntry(3f, 4f));
//        entries.add(new BarEntry(4f, 3f));
//
//        // Tạo BarDataSet (dữ liệu cho BarChart)
//        BarDataSet dataSet = new BarDataSet(entries, "Sample Bar Chart");
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Đặt màu sắc cho các cột
//        dataSet.setValueTextColor(Color.BLACK); // Đặt màu chữ cho giá trị
//
//        // Tạo BarData và set cho BarChart
//        BarData barData = new BarData(dataSet);
//        barChart.setData(barData);
//
//        // Cập nhật biểu đồ
//        barChart.invalidate(); // Cập nhật đồ họa của biểu đồ
    }
}
