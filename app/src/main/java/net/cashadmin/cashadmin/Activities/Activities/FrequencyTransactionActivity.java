package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import net.cashadmin.cashadmin.Activities.Adapter.EditFrequencyAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Enum.TransactionEntryEnum;
import net.cashadmin.cashadmin.Activities.Model.Frequency;
import net.cashadmin.cashadmin.Activities.Utils.Utils;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FrequencyTransactionActivity extends AppCompatActivity {

    private DataManager mDataManager;
    private ArrayList<Frequency> mFrequencies;
    private EditFrequencyAdapter mAdapter;

    @InjectView(R.id.frequencyList)ListView mFrequencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_transaction);

        ButterKnife.inject(this);

        mDataManager = DataManager.getDataManager();

        mFrequencies = (ArrayList<Frequency>) (ArrayList<?>) mDataManager.getAll(Frequency.class);

        mAdapter = new EditFrequencyAdapter(FrequencyTransactionActivity.this, mFrequencies, mOnClickDeleteListener, mOnClickEditListener);
        
    }

    private View.OnClickListener mOnClickDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Utils.animateRemoval(mFrequencyList, view, mAdapter, mFrequencyList);

                    // Delete the item from the adapter
                    Frequency f = mAdapter.getItem(mFrequencyList.getPositionForView(view));
                    mDataManager.delete(f);
                    mAdapter.remove(f);
                }
            };
            r.run();
        }
    };

    private View.OnClickListener mOnClickEditListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Frequency f = mAdapter.getItem(mFrequencyList.getPositionForView(view));
            Intent intent = null;
            switch (f.getTransactionType()) {
                case EXPENSE :
                    intent = new Intent(FrequencyTransactionActivity.this, ExpenseActivity.class);
                    intent.putExtra("expenseId", f.getId());
                    break;
                case INCOME :
                    intent = new Intent(FrequencyTransactionActivity.this, IncomeActivity.class);
                    intent.putExtra("incomeId", f.getId());
                    break;
            }
            TransactionEntryEnum.Edit.attachTo(intent);
            startActivity(intent);
        }
    };
}
