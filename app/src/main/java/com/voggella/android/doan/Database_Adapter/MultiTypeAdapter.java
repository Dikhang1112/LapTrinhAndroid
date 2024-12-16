package com.voggella.android.doan.Database_Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.voggella.android.doan.R;

import java.util.List;

public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TRANSACTION = 0;
    private static final int TYPE_BUDGET = 1;

    private List<Object> itemList;

    // Constructor
    public MultiTypeAdapter(List<Object> itemList) {
        this.itemList = itemList;
    }

    // Phương thức xác định ViewType cho từng item
    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof Transaction) {
            return TYPE_TRANSACTION; // Đối với Transaction
        } else if (itemList.get(position) instanceof Budget) {
            return TYPE_BUDGET; // Đối với Budget
        }
        return -1;
    }

    // Phương thức tạo ViewHolder tương ứng với loại mục
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TRANSACTION:
                View transactionView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_transaction, parent, false);
                return new TransactionViewHolder(transactionView);
            case TYPE_BUDGET:
                View budgetView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_budget, parent, false);
                return new BudgetViewHolder(budgetView);
            default:
                return null;
        }
    }

    // Phương thức bind dữ liệu cho từng ViewHolder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TransactionViewHolder) {
            Transaction transaction = (Transaction) itemList.get(position);
            ((TransactionViewHolder) holder).bind(transaction);
        } else if (holder instanceof BudgetViewHolder) {
            Budget budget = (Budget) itemList.get(position);
            ((BudgetViewHolder) holder).bind(budget);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder cho Transaction
    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvType, tvAmount, tvDate;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvDate = itemView.findViewById(R.id.tv_dateTrans);
        }

        public void bind(Transaction transaction) {
            tvType.setText(transaction.getType());
            tvAmount.setText(String.valueOf(transaction.getAmount()));
            tvDate.setText(transaction.getDate());
        }
    }

    // ViewHolder cho Budget
    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUpdate, tvAmount, tvDate;

        public BudgetViewHolder(View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_budget_amount);
            tvDate = itemView.findViewById(R.id.tv_budget_date);
        }

        public void bind(Budget budget) {
            tvAmount.setText(String.valueOf(budget.getAmount()));
            tvDate.setText(budget.getDate());
        }
    }
}

