package com.voggella.android.doan.mainHome;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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
import com.voggella.android.doan.Database_Adapter.SQLiteHelper;
import com.voggella.android.doan.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class add_Trans extends AppCompatActivity {

    private Spinner spinnerGroupsCate;
    private TextView tvCate, tvDate, tvCancel;
    private EditText etAmount, etNote;
    private Button btnSaveTransaction;
    private ImageView datePickerIcon;
    private SQLiteHelper.CategoryName selectedCategory;
    private Calendar calendar;
    private String phoneUser;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus_cate);

        Intent intent = getIntent();
        phoneUser = intent.getStringExtra("USERS_SDT");
        userName = intent.getStringExtra("USER_FULL_NAME");

        // Initialize UI elements
        spinnerGroupsCate = findViewById(R.id.spinner_groupsCate);
        tvCate = findViewById(R.id.tv_cate);
        etAmount = findViewById(R.id.et_amount);
        etNote = findViewById(R.id.et_note);
        tvDate = findViewById(R.id.tv_date);
        datePickerIcon = findViewById(R.id.img_date);
        btnSaveTransaction = findViewById(R.id.btn_SaveTransac);
        tvCancel = findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backMain = new Intent(add_Trans.this,mainScreen.class);
                backMain.putExtra("USERS_SDT",phoneUser);
                startActivity(backMain);
                finish();
            }
        });

        // Initialize calendar to get current date
        calendar = Calendar.getInstance();

        // Setup Spinner for Categories
        ArrayAdapter<SQLiteHelper.CategoryName> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SQLiteHelper.CategoryName.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupsCate.setAdapter(adapter);

        // Spinner item selection listener
        spinnerGroupsCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = (SQLiteHelper.CategoryName) parentView.getItemAtPosition(position);
                tvCate.setText(selectedCategory.name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected
            }
        });

        // Date picker icon click event to show DatePickerDialog
        datePickerIcon.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create and show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    add_Trans.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Set the selected date in the TextView
                        calendar.set(year1, monthOfYear, dayOfMonth);
                        String selectedDate = getFormattedDate(calendar);
                        tvDate.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Save button click event
        btnSaveTransaction.setOnClickListener(v -> {
            String amount = etAmount.getText().toString();
            String note = etNote.getText().toString();
            String date = tvDate.getText().toString();

            // Check if fields are filled
            if (amount.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Insert transaction into the database
            insertTransaction(phoneUser,amount, selectedCategory,note,date);
            Intent backLoad = new Intent(add_Trans.this,mainScreen.class);
            backLoad.putExtra("USERS_SDT",phoneUser);
            backLoad.putExtra("USER_FULL_NAME",userName);
            startActivity(backLoad);
            finish();
        });
    }

    // Method to format the date
    private String getFormattedDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    // Method to insert transaction into the database
    private void insertTransaction(String phoneUser ,String amount, SQLiteHelper.CategoryName category, String note, String date) {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_TRANSACTION_USERS_SDT,phoneUser);
        values.put(SQLiteHelper.COLUMN_TRANSACTION_AMOUNT, amount);
        values.put(SQLiteHelper.COLUMN_TRANSACTION_TYPE, category.name());
        values.put(SQLiteHelper.COLUMN_TRANSACTION_DESCRIPTION, note);
        values.put(SQLiteHelper.COLUMN_TRANSACTION_DATE, date);
        // Insert into TRANSACTION table
        long rowId = db.insert(SQLiteHelper.TB_Trans, null, values);
        if (rowId != -1) {
            Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
