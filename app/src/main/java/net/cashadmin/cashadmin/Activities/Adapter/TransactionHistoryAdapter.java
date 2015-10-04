package net.cashadmin.cashadmin.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Transaction;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;

public class TransactionHistoryAdapter extends ArrayAdapter<Transaction> {

    private ArrayList<Transaction> mTransactions;

    public TransactionHistoryAdapter(Context context, ArrayList<Transaction> objects) {
        super(context, R.layout.history_item, objects);
        mTransactions = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.history_item, null);
            holder = new Holder();
            holder.mColorCategory = (View) convertView.findViewById(R.id.colored_bar);
            holder.mLabel = (TextView) convertView.findViewById(R.id.label);
            holder.mCategory = (TextView) convertView.findViewById(R.id.category);
            holder.mAmount = (TextView) convertView.findViewById(R.id.amount);

            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }

        Transaction t = mTransactions.get(position);
        switch(t.getType()){
            case EXPENSE:
                Expense e = (Expense) t;
                holder.mCategory.setText(e.getCategory().getLabel());
                holder.mColorCategory.setBackgroundColor(e.getCategory().getColor());
                holder.mAmount.setText("- " + String.valueOf(t.getTotal()) + " €");
                holder.mAmount.setTextColor(this.getContext().getResources().getColor(R.color.red_dark));
                break;
            case INCOME:
                holder.mCategory.setText(this.getContext().getString(R.string.income));
                holder.mColorCategory.setBackgroundResource(R.color.green);
                holder.mAmount.setText("+ " + String.valueOf(t.getTotal()) + " €");
                holder.mAmount.setTextColor(this.getContext().getResources().getColor(R.color.green));
                break;
        }
        holder.mLabel.setText(t.getLabel());

        return convertView;
    }

    private static class Holder{
        private View mColorCategory;
        private TextView mLabel;
        private TextView mCategory;
        private TextView mAmount;
    }
}
