package com.voggella.android.doan.mainHome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.voggella.android.doan.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<TransactionShow> transactionList;

    public TransactionAdapter(List<TransactionShow> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_of_type, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionShow transaction = transactionList.get(position);
        holder.note.setText(transaction.getDescription());
        holder.amount.setText(String.valueOf(transaction.getAmount()));
        holder.date.setText(transaction.getDate());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView note, amount, date;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.itemTransactionNote);
            amount = itemView.findViewById(R.id.itemTransactionAmount);
            date = itemView.findViewById(R.id.itemTransactionDate);
        }
    }
}
