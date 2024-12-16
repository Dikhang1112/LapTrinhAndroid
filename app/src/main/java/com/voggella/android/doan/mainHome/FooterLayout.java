package com.voggella.android.doan.mainHome;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.voggella.android.doan.R;

public class FooterLayout extends LinearLayout {

    private String phoneUser;
    private String userName;

    private ImageView btnHome, btnDashboard, btnAdd, btnCate, btnProfile;

    public FooterLayout(Context context) {
        super(context);
        init(context);
    }

    public FooterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FooterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.footer, this, true);

        // Get references to the ImageView elements
        btnHome = findViewById(R.id.btn_home);
        btnDashboard = findViewById(R.id.btn_dashboard);
        btnAdd = findViewById(R.id.btn_add);
        btnCate = findViewById(R.id.btn_cate);
        btnProfile = findViewById(R.id.btn_profile);

        // Set onClickListeners for each ImageView
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when home button is clicked
                Toast.makeText(context, "Home clicked", Toast.LENGTH_SHORT).show();
                highlightSelectedImageView(btnHome);
                if (phoneUser != null && userName != null) {
                    Intent intent = new Intent(getContext(), mainScreen.class);
                    intent.putExtra("USERS_SDT", phoneUser);
                    intent.putExtra("USER_FULL_NAME", userName);
                    getContext().startActivity(intent);
                } else {
                    Toast.makeText(context, "Error: Missing user data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDashboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when dashboard button is clicked
                highlightSelectedImageView(btnDashboard);
                // Kiểm tra phoneUser và userName trước khi truyền vào Intent
                if (phoneUser != null && userName != null) {
                    Intent intent = new Intent(getContext(), chartJs.class);
                    intent.putExtra("USERS_SDT", phoneUser);
                    intent.putExtra("USER_FULL_NAME", userName);
                    getContext().startActivity(intent);
                } else {
                    Toast.makeText(context, "Error: Missing user data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when add button is clicked
                highlightSelectedImageView(btnAdd);
                // Start the add transaction activity
                Intent intent = new Intent(getContext(), add_Trans.class);
                intent.putExtra("USERS_SDT", phoneUser);
                intent.putExtra("USER_FULL_NAME", userName);
                getContext().startActivity(intent);
            }
        });

        btnCate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when category button is clicked
                highlightSelectedImageView(btnCate);

                // Start the transaction detail activity
                Intent intent = new Intent(getContext(), trans_Detail.class);
                intent.putExtra("USERS_SDT", phoneUser);
                intent.putExtra("USER_FULL_NAME", userName);
                getContext().startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when profile button is clicked
                highlightSelectedImageView(btnProfile);
                // Start the account setting activity
                Intent intent = new Intent(getContext(), account_Setting.class);
                intent.putExtra("USERS_SDT", phoneUser);
                intent.putExtra("USER_FULL_NAME", userName);
                getContext().startActivity(intent);
            }
        });
    }

    // Method to set user data (phone and name) to the FooterLayout
    public void setUserData(String phoneUser, String userName) {
        this.phoneUser = phoneUser;
        this.userName = userName;
    }

    // Helper method to highlight the selected ImageView
    private void highlightSelectedImageView(ImageView selectedImageView) {
        // Reset all image views background to transparent
        resetImageViews();

        // Set background for the selected ImageView
        selectedImageView.setBackgroundResource(R.drawable.select_footer);  // Ensure you have a selected background drawable
    }

    // Method to reset background for all ImageViews
    private void resetImageViews() {
        btnHome.setBackgroundResource(android.R.color.transparent);
        btnDashboard.setBackgroundResource(android.R.color.transparent);
        btnAdd.setBackgroundResource(android.R.color.transparent);
        btnCate.setBackgroundResource(android.R.color.transparent);
        btnProfile.setBackgroundResource(android.R.color.transparent);
    }
}
