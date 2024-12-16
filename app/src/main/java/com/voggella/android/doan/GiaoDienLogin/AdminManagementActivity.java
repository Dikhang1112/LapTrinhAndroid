package com.voggella.android.doan.GiaoDienLogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.voggella.android.doan.Database_Adapter.UserListAdapter;
import com.voggella.android.doan.Database_Adapter.UserModel;
import com.voggella.android.doan.R;
import com.voggella.android.doan.Database_Adapter.SQLiteHelper;
import com.voggella.android.doan.Database_Adapter.UserModel;
import com.voggella.android.doan.Database_Adapter.UserListAdapter;
import java.util.ArrayList;

public class AdminManagementActivity extends AppCompatActivity {
    private ListView listViewUsers;
    private Button btnThem, btnSua, btnXoa;
    private SQLiteHelper dbHelper;
    private UserListAdapter userListAdapter; // Khai báo adapter toàn cục
    private UserListAdapter adapter;
    private UserModel selectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Log.d("AdminActivity", "Setting content view...");
            setContentView(R.layout.admin_management_view);

            Log.d("AdminActivity", "Initializing UI...");
            initUI();

            Log.d("AdminActivity", "Creating database helper...");
            dbHelper = new SQLiteHelper(this);

            Log.d("AdminActivity", "Loading users list...");
            loadUsersList();

            Log.d("AdminActivity", "Setting up listeners...");
            setupListeners();

            Toast.makeText(this, "Đã vào trang Admin", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("AdminActivity", "Error in onCreate: " + e.getMessage(), e);
            e.printStackTrace(); // In stack trace đầy đủ
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initUI() {
        listViewUsers = findViewById(R.id.listViewUsers);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);

        // Thêm xử lý cho nút logout
        ImageButton btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdminManagementActivity.this)
                        .setTitle("Xác nhận đăng xuất")
                        .setMessage("Bạn có chắc muốn đăng xuất?")
                        .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Chuyển về màn hình đăng nhập
                                Intent intent = new Intent(AdminManagementActivity.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
    }

    private void loadUsersList() {
        try {
            Log.d("AdminActivity", "Getting users from database...");
            ArrayList<UserModel> usersList = dbHelper.getAllUsers(); // Lấy danh sách người dùng từ database

            Log.d("AdminActivity", "Creating adapter...");
            userListAdapter = new UserListAdapter(this, usersList); // Khởi tạo adapter với danh sách người dùng

            Log.d("AdminActivity", "Setting adapter to ListView...");
            listViewUsers.setAdapter(userListAdapter); // Gán adapter cho ListView

            Log.d("AdminActivity", "Users list loaded successfully with " + usersList.size() + " users");
        } catch (Exception e) {
            Log.e("AdminActivity", "Error loading users list: " + e.getMessage(), e);
            throw e; // Ném lại exception để onCreate có thể bắt được
        }
    }

    //Xu li sụ kien chon user
    private void setupListeners() {
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = (UserModel) parent.getItemAtPosition(position);

                Toast.makeText(AdminManagementActivity.this,
                        "Đã chọn: " + selectedUser.getName(),
                        Toast.LENGTH_SHORT).show();

                if (userListAdapter != null) { // Kiểm tra userListAdapter không null
                    userListAdapter.setSelectedPosition(position);
                } else {
                    Log.e("AdminActivity", "userListAdapter is null in setupListeners");
                }
            }
        });
    // Nút thêm user
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });

        // Nút sửa user
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUser != null) {
                    showEditUserDialog(selectedUser);
                } else {
                    Toast.makeText(AdminManagementActivity.this,
                            "Vui lòng chọn người dùng cần sửa",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút xóa user
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUser != null) {
                    showDeleteConfirmDialog();
                } else {
                    Toast.makeText(AdminManagementActivity.this,
                            "Vui lòng chọn người dùng cần xóa",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_users_edit, null);

        final EditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        final EditText edtName = dialogView.findViewById(R.id.edtName);
        final EditText edtPassword = dialogView.findViewById(R.id.edtPassword);

        builder.setView(dialogView)
                .setTitle("Thêm người dùng mới")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phone = edtPhone.getText().toString().trim();
                        String name = edtName.getText().toString().trim();
                        String password = edtPassword.getText().toString().trim();


                        if (!phone.isEmpty() && !name.isEmpty() && !password.isEmpty()) {
                            UserModel newUser = new UserModel(phone, name, password);
                            long result = dbHelper.addUser(newUser);
                            if (result != -1) {
                                Toast.makeText(AdminManagementActivity.this,
                                        "Thêm người dùng thành công",
                                        Toast.LENGTH_SHORT).show();
                                loadUsersList(); // Refresh danh sách
                            } else {
                                Toast.makeText(AdminManagementActivity.this,
                                        "Thêm người dùng thất bại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AdminManagementActivity.this,
                                    "Vui lòng nhập đầy đủ thông tin",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void showEditUserDialog(final UserModel user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_users_edit, null);

        final EditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        final EditText edtName = dialogView.findViewById(R.id.edtName);
        final EditText edtPassword = dialogView.findViewById(R.id.edtPassword);
        final CheckBox cbVip = dialogView.findViewById(R.id.cbVip);

        // Điền thông tin hiện tại của user
        edtPhone.setText(user.getPhone());
        edtPhone.setEnabled(false);
        edtName.setText(user.getName());
        edtPassword.setText(user.getPassword());
        // Log trạng thái VIP trước khi set
        Log.d("AdminActivity", "Setting VIP status for user " + user.getName() + ": " + user.isVip());
        cbVip.setChecked(user.isVip());

        builder.setView(dialogView)
                .setTitle("Sửa thông tin người dùng")
                .setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = edtName.getText().toString().trim();
                        String password = edtPassword.getText().toString().trim();
                        boolean isVip = cbVip.isChecked();

                        Log.d("AdminActivity", "Updating user " + user.getName() +
                                " - New VIP status: " + isVip);

                        if (!name.isEmpty() && !password.isEmpty()) {
                            user.setName(name);
                            user.setPassword(password);
                            user.setVip(isVip);

                            // Cập nhật thông tin cơ bản
                            int result = dbHelper.updateUser(user);

                            // Cập nhật trạng thái VIP
                            boolean vipUpdateResult = dbHelper.updateUserVipStatus(user.getPhone(), isVip);

                            Log.d("AdminActivity", "Update results - Basic info: " + result +
                                    ", VIP status: " + vipUpdateResult);

                            if (result > 0 && vipUpdateResult) {
                                Toast.makeText(AdminManagementActivity.this,
                                        "Cập nhật thành công",
                                        Toast.LENGTH_SHORT).show();
                                loadUsersList();
                            } else {
                                Toast.makeText(AdminManagementActivity.this,
                                        "Cập nhật thất bại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AdminManagementActivity.this,
                                    "Vui lòng nhập đầy đủ thông tin",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa người dùng này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int result = dbHelper.deleteUser(selectedUser.getPhone());
                        if (result > 0) {
                            Toast.makeText(AdminManagementActivity.this,
                                    "Xóa thành công",
                                    Toast.LENGTH_SHORT).show();
                            selectedUser = null;
                            loadUsersList(); // Refresh danh sách
                        } else {
                            Toast.makeText(AdminManagementActivity.this,
                                    "Xóa thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

}

