package com.voggella.android.doan.Database_Adapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.voggella.android.doan.Database_Adapter.UserModel;
import com.voggella.android.doan.R;
import java.util.ArrayList;

public class UserListAdapter extends ArrayAdapter<UserModel> {
    private Context context;
    private ArrayList<UserModel> users;
    private int selectedPosition = -1; // Vị trí được chọn (-1 nghĩa là chưa có mục nào được chọn)


    public UserListAdapter(Context context, ArrayList<UserModel> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        }

        UserModel user = users.get(position);

        TextView tvItemTitle = convertView.findViewById(R.id.tvItemTitle);
        TextView tvSubItem = convertView.findViewById(R.id.tvSubItem);

        String vipStatus = user.isVip() ? " 👑 VIP" : "";
        tvItemTitle.setText(user.getName() + vipStatus);
        tvSubItem.setText(user.getPhone());

        // Đổi màu nền nếu là mục được chọn
        if (position == selectedPosition) {
            convertView.setBackgroundColor(Color.parseColor("#D3D3D3")); // Màu nền khi được chọn
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT); // Màu nền mặc định
        }

        // Thay đổi màu nền cho VIP user (ưu tiên nếu không được chọn)
        if (user.isVip() && position != selectedPosition) {
            convertView.setBackgroundColor(Color.parseColor("#FFF8DC")); // Màu nền cho VIP
        }

        return convertView;
    }
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged(); // Yêu cầu ListView cập nhật giao diện
    }
}