package net.cashadmin.cashadmin.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Transaction;
import net.cashadmin.cashadmin.Activities.Utils.Utils;
import net.cashadmin.cashadmin.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionHistoryAdapter extends ArrayAdapter<Transaction> {

    private List<Transaction> mTransactions;
    private View.OnClickListener mOnClickDeleteListener;
    private View.OnClickListener mOnClickEditListener;

    public TransactionHistoryAdapter(Context context, List<Transaction> objects, View.OnClickListener onClickDeleteListener, View.OnClickListener onClickEditListener) {
        super(context, R.layout.historic_item, objects);
        mTransactions = objects;
        mOnClickDeleteListener = onClickDeleteListener;
        mOnClickEditListener = onClickEditListener;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.historic_item, null);
            holder = new Holder();
            holder.mSwipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeHistoricLayout);
            holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            holder.mEditButton = (RelativeLayout) convertView.findViewById(R.id.editItemButton);
            holder.mDeleteButton = (RelativeLayout) convertView.findViewById(R.id.deleteItemButton);
            holder.mColorCategory = convertView.findViewById(R.id.colored_bar);
            holder.mLabel = (TextView) convertView.findViewById(R.id.label);
            holder.mCategory = (TextView) convertView.findViewById(R.id.category);
            holder.mAmount = (TextView) convertView.findViewById(R.id.amount);
            holder.mDate = (TextView) convertView.findViewById(R.id.dateTransaction);

            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        DateFormat hourFormat = new SimpleDateFormat("HH:mm");

        final Transaction t = mTransactions.get(position);
        switch (t.getType()) {
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
        if (Math.abs(t.getDate().getTime() - new Date().getTime()) > Utils.MILLIS_PER_DAY) {
            holder.mDate.setText(dateFormat.format(t.getDate()));
        } else {
            holder.mDate.setText(hourFormat.format(t.getDate()));
        }
        holder.mEditButton.setOnClickListener(mOnClickEditListener);

        holder.mDeleteButton.setOnClickListener(mOnClickDeleteListener);

        return convertView;
    }

    public void refreshAdapter(List<Transaction> transactions){
        mTransactions.clear();
        mTransactions.addAll(transactions);
        notifyDataSetChanged();
    }

    private static class Holder {
        private SwipeLayout mSwipeLayout;
        private RelativeLayout mEditButton;
        private RelativeLayout mDeleteButton;
        private View mColorCategory;
        private TextView mLabel;
        private TextView mCategory;
        private TextView mAmount;
        private TextView mDate;
    }
}
