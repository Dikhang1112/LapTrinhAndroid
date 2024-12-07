// File: adapter/TransactionAdapter.java
package com.voggella.android.doan.Database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.voggella.android.doan.R;
import com.voggella.android.doan.Database.Transaction;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.type.setText(transaction.getType());

        // Hiển thị dấu trừ nếu loại giao dịch là "Expense"
        if (transaction.getType().equalsIgnoreCase("Expense")) {
            holder.amount.setText("-" + String.valueOf(transaction.getAmount())); // Thêm dấu trừ
        } else {
            holder.amount.setText(String.valueOf(transaction.getAmount()));
        }

        holder.date.setText(transaction.getDate());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView type, amount, date;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.tv_type);
            amount = itemView.findViewById(R.id.tv_amount);
            date = itemView.findViewById(R.id.tv_dateTrans);
        }
    }
}
