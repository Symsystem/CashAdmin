package net.cashadmin.cashadmin.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Frequency;
import net.cashadmin.cashadmin.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditFrequencyAdapter extends ArrayAdapter<Frequency> {

    private HashMap<Frequency, Integer> mIdMap = new HashMap<Frequency, Integer>();
    private ArrayList<Frequency> mFrequencies;
    View.OnClickListener mOnClickDeleteListener;
    View.OnClickListener mOnClickEditListener;

    public EditFrequencyAdapter(Context context, ArrayList<Frequency> objects, View.OnClickListener onClickDeleteListener, View.OnClickListener onClickEditListener) {
        super(context, R.layout.frequency_item, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
        mFrequencies = objects;
        mOnClickDeleteListener = onClickDeleteListener;
        mOnClickEditListener = onClickEditListener;
    }

    @Override
    public long getItemId(int position) {
        Frequency item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.frequency_item, null);
            holder = new Holder();
            holder.mSwipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeHistoricLayout);
            holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            holder.mEditButton = (RelativeLayout) convertView.findViewById(R.id.editItemButton);
            holder.mDeleteButton = (RelativeLayout) convertView.findViewById(R.id.deleteItemButton);
            holder.mColorCategory = convertView.findViewById(R.id.colored_bar);
            holder.mLabel = (TextView) convertView.findViewById(R.id.label);
            holder.mCategory = (TextView) convertView.findViewById(R.id.category);
            holder.mAmount = (TextView) convertView.findViewById(R.id.amount);
            holder.mFrequency = (TextView) convertView.findViewById(R.id.frequency);
            holder.mEndDate = (TextView) convertView.findViewById(R.id.endDateFrequency);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        DateFormat yearFormat = new SimpleDateFormat("dd MMMM", Locale.FRENCH);
        DateFormat monthFormat = new SimpleDateFormat("dd");
        DateFormat weekFormat = new SimpleDateFormat("EEEE", Locale.FRENCH);
        Map<String, String> frequencyMap = new HashMap<>();
        frequencyMap.put(FrequencyEnum.NEVER.toString(), "");
        frequencyMap.put(FrequencyEnum.DAILY.toString(), "Tous les jours");
        frequencyMap.put(FrequencyEnum.WEEKLY.toString(), "Toutes les semaines");
        frequencyMap.put(FrequencyEnum.MONTHLY.toString(), "Tous les mois");
        frequencyMap.put(FrequencyEnum.YEARLY.toString(), "Tous les ans");

        final Frequency f = mFrequencies.get(position);
        switch (f.getTransactionType()) {
            case EXPENSE:
                holder.mCategory.setText(f.getCategory().getLabel());
                holder.mColorCategory.setBackgroundColor(f.getCategory().getColor());
                holder.mAmount.setText(new StringBuilder("- " + String.valueOf(f.getTotal()) + " €"));
                holder.mAmount.setTextColor(this.getContext().getResources().getColor(R.color.red_dark));
                break;
            case INCOME:
                holder.mCategory.setText(this.getContext().getString(R.string.income));
                holder.mColorCategory.setBackgroundResource(R.color.green);
                holder.mAmount.setText(new StringBuilder("+ " + String.valueOf(f.getTotal()) + " €"));
                holder.mAmount.setTextColor(this.getContext().getResources().getColor(R.color.green));
                break;
        }
        if (dateFormat.format(f.getEndDateFrequency()).equals("31/12/69")) {
            holder.mEndDate.setText("...");
        } else {
            holder.mEndDate.setText(dateFormat.format(f.getEndDateFrequency()));
        }
        switch (f.getFrequency()){
            case NEVER:
                holder.mFrequency.setText(new StringBuilder("Le " + dateFormat.format(f.getDateFrequency())));
                break;
            case DAILY:
                holder.mFrequency.setText(frequencyMap.get(f.getFrequency().toString()));
                break;
            case WEEKLY:
                holder.mFrequency.setText(new StringBuilder(frequencyMap.get(f.getFrequency().toString()) + " (" + weekFormat.format(f.getDateFrequency()) + ")"));
                break;
            case MONTHLY:
                holder.mFrequency.setText(new StringBuilder(frequencyMap.get(f.getFrequency().toString()) + " (" + monthFormat.format(f.getDateFrequency()) + ")"));
                break;
            case YEARLY:
                holder.mFrequency.setText(new StringBuilder(frequencyMap.get(f.getFrequency().toString()) + " (" + yearFormat.format(f.getDateFrequency()) + ")"));
                break;
        }
        holder.mLabel.setText(f.getLabel());

        holder.mEditButton.setOnClickListener(mOnClickEditListener);

        holder.mDeleteButton.setOnClickListener(mOnClickDeleteListener);

        return convertView;
    }

    public synchronized void refreshAdapter(ArrayList<Frequency> frequencies) {
        mFrequencies.clear();
        mFrequencies.addAll(frequencies);
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
        private TextView mFrequency;
        private TextView mEndDate;
    }
}
