package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import net.cashadmin.cashadmin.Activities.Adapter.TransactionHistoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Model.Enum.HistoricEntryEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Transaction;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HistoricActivity extends AppCompatActivity {

    private static final int MOVE_DURATION = 250;

    private DataManager mDataManager;
    private TransactionHistoryAdapter mAdapter;
    private ArrayList<Transaction> mExpenses;
    private ArrayList<Transaction> mIncomes;
    private ArrayList<Transaction> transactions;

    @InjectView(R.id.historyList)
    ListView mHistoryList;
    @InjectView(R.id.checkDepense)
    CheckBox mCheckExpense;
    @InjectView(R.id.checkRevenu)
    CheckBox mCheckIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        ButterKnife.inject(this);

        mCheckExpense.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkedChange(mCheckExpense);
            }
        });
        mCheckIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkedChange(mCheckIncome);
            }
        });

        mDataManager = DataManager.getDataManager();
        Intent intent = getIntent();

        try {
            HistoricEntryEnum entryType = HistoricEntryEnum.detachFrom(intent);

            switch (entryType){
                case All:
                    mExpenses = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getAll(TypeEnum.EXPENSE);
                    mIncomes = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getAll(TypeEnum.INCOME);
                    break;
                case ByCategory:
                    mExpenses = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getWhere(TypeEnum.EXPENSE, intent.getStringExtra("expenseCondition"));
                    mIncomes = new ArrayList<Transaction>();
                    break;
            }
        } catch (IllegalTypeException e) {
            e.printStackTrace();
        }
        transactions = mergeExpenseIncome(mExpenses, mIncomes);
        mAdapter = new TransactionHistoryAdapter(HistoricActivity.this, transactions, mOnClickDeleteListener);

        checkedChange(mCheckExpense);
        mHistoryList.setAdapter(mAdapter);
    }

    private View.OnClickListener mOnClickDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    animateRemoval(mHistoryList, view);

                    // Delete the item from the adapter
                    Transaction t = mAdapter.getItem(mHistoryList.getPositionForView(view));
                    mDataManager.delete(t);
                    mAdapter.remove(t);
                }
            };
            r.run();
        }
    };

    private void checkedChange(CheckBox c){
        if (mCheckExpense.isChecked() && mCheckIncome.isChecked()){
            mAdapter.refreshAdapter(mergeExpenseIncome(mExpenses, mIncomes));
        } else if (mCheckExpense.isChecked()){
            mAdapter.refreshAdapter(mExpenses);
        } else if (mCheckIncome.isChecked()){
            mAdapter.refreshAdapter(mIncomes);
        } else {
            if (c == mCheckExpense)
                mCheckIncome.setChecked(true);
            else
                mCheckExpense.setChecked(true);
        }
    }

    /**
     * This method animates all other views in the ListView container (not including ignoreView)
     * into their final positions. It is called after ignoreView has been removed from the
     * adapter, but before layout has been run. The approach here is to figure out where
     * everything is now, then allow layout to run, then figure out where everything is after
     * layout, and then to run animations between all of those start/end positions.
     */
    private void animateRemoval(final ListView listview, View viewToRemove) {
        final HashMap<Long, Integer> itemIdTopMap = new HashMap<>(); // Map pour connaitre les
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = mAdapter.getItemId(position);
                itemIdTopMap.put(itemId, child.getTop());
            }
        }

        final int position = mHistoryList.getPositionForView(viewToRemove);
        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = position; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = mAdapter.getItemId(position);
                    Integer startTop = itemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        break;
                    }
                }
                return true;
            }
        });
    }

    private ArrayList<Transaction> mergeExpenseIncome(List<Transaction> l1, List<Transaction> l2){
        ArrayList<Transaction> transactions = new ArrayList<>();
        Transaction t1, t2;
        int i1 = 0, i2 = 0;

        while(i1 < l1.size() && i2 < l2.size()){
            t1 = l1.get(i1);
            t2 = l2.get(i2);

            if(t1.getDate().compareTo(t2.getDate()) >= 0){
                transactions.add(t1);
                i1++;
            }
            else {
                transactions.add(t2);
                i2++;
            }
        }

        for(; i1 < l1.size(); i1++)
            transactions.add((Transaction) l1.get(i1));

        for(;i2 < l2.size(); i2++)
            transactions.add((Transaction) l2.get(i2));

        return transactions;
    }
}
