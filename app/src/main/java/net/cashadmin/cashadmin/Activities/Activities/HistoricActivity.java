package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.cashadmin.cashadmin.Activities.Adapter.EndlessScrollListener;
import net.cashadmin.cashadmin.Activities.Adapter.TransactionHistoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.HistoricEntryEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TransactionEntryEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Income;
import net.cashadmin.cashadmin.Activities.Model.Transaction;
import net.cashadmin.cashadmin.Activities.Utils.Counter;
import net.cashadmin.cashadmin.Activities.Utils.Utils;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HistoricActivity extends AppCompatActivity {

    public static final int NUMBER_LOADED = 12;

    private DataManager mDataManager;
    private TransactionHistoryAdapter mAdapter;
    private List<Transaction> mExpenses;
    private List<Transaction> mIncomes;
    private List<Transaction> transactions;

    private Counter mExpenseCounter, mIncomeCounter;
    private HistoricEntryEnum entryType;

    @InjectView(R.id.topBar)
    RelativeLayout relativeLayoutTopBar;
    @InjectView(R.id.categoryLabelTopBar)
    TextView categoryLabelTopBar;
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

        mDataManager = DataManager.getDataManager();
        Intent intent = getIntent();

        transactions = new ArrayList<>();

        entryType = HistoricEntryEnum.detachFrom(intent);
        switch (entryType) {

            case ByCategory :
                Category c = (Category) intent.getSerializableExtra("category");
                relativeLayoutTopBar.setVisibility(View.GONE);
                categoryLabelTopBar.setVisibility(View.VISIBLE);
                categoryLabelTopBar.setText(c.getLabel());
                categoryLabelTopBar.setBackgroundColor(c.getColor());
                mExpenses = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getWhere(Expense.class, intent.getStringExtra("expenseCondition"));
                mIncomes = new ArrayList<>();
                transactions.addAll(mExpenses);
                break;

            case All :
                mExpenseCounter = new Counter();
                mIncomeCounter = new Counter();
                transactions.addAll(getTransactions(NUMBER_LOADED));
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
                break;
        }

        mAdapter = new TransactionHistoryAdapter(HistoricActivity.this, transactions, mOnClickDeleteListener, mOnClickEditListener);

        mHistoryList.setAdapter(mAdapter);
        mHistoryList.setOnScrollListener(mEndlessScrollListener);
    }

    private EndlessScrollListener mEndlessScrollListener = new EndlessScrollListener() {
        @Override
        public boolean onLoadMore(int page, int totalItemsCount) {
            if (entryType != HistoricEntryEnum.All) {
                // Doesn't load something more if we don't want to display the all list
                return false;
            }
            List<Transaction> newTrans = getTransactions(totalItemsCount + NUMBER_LOADED);
            transactions.addAll(newTrans);
            mAdapter.notifyDataSetChanged();
            return true;
        }
    };

    private View.OnClickListener mOnClickDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Utils.animateRemoval(mHistoryList, view, mAdapter, mHistoryList);

                    // Delete the item from the adapter
                    Transaction t = mAdapter.getItem(mHistoryList.getPositionForView(view));
                    mDataManager.delete(t);
                    mAdapter.remove(t);
                }
            };
            r.run();
        }
    };

    private View.OnClickListener mOnClickEditListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Transaction t = mAdapter.getItem(mHistoryList.getPositionForView(view));
            Intent intent = null;
            switch (t.getType()) {
                case EXPENSE :
                    intent = new Intent(HistoricActivity.this, ExpenseActivity.class);
                    intent.putExtra("expenseId", t.getId());
                    break;
                case INCOME :
                    intent = new Intent(HistoricActivity.this, IncomeActivity.class);
                    intent.putExtra("incomeId", t.getId());
                    break;
            }
            TransactionEntryEnum.Edit.attachTo(intent);
            startActivity(intent);
        }
    };

    private List<Transaction> getTransactions(int numberLoaded) {
        List<Transaction> t = new ArrayList<>();

        if (entryType == HistoricEntryEnum.All) {
            if (mCheckExpense.isChecked() && mCheckIncome.isChecked()) {
                mExpenses = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getFromTo(Expense.class, mExpenseCounter.getCounterInt(), mExpenseCounter.getCounterInt() + NUMBER_LOADED);
                mIncomes = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getFromTo(Income.class, mIncomeCounter.getCounterInt(), mIncomeCounter.getCounterInt() + NUMBER_LOADED);
                t = mergeExpenseIncome(mExpenses, mIncomes, numberLoaded - (mExpenseCounter.getCounterInt() + mIncomeCounter.getCounterInt()));
            } else if (mCheckExpense.isChecked()) {
                t = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getFromTo(Expense.class, mExpenseCounter.getCounterInt(), mExpenseCounter.getCounterInt() + NUMBER_LOADED);
                mExpenseCounter.add(NUMBER_LOADED);
            } else if (mCheckIncome.isChecked()) {
                t = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getFromTo(Income.class, mIncomeCounter.getCounterInt(), mIncomeCounter.getCounterInt() + NUMBER_LOADED);
                mIncomeCounter.add(NUMBER_LOADED);
            }
        }
        return t;
    }

    private synchronized void checkedChange(CheckBox c) {
        mExpenseCounter = new Counter();
        mIncomeCounter = new Counter();
        mEndlessScrollListener.reset();
        if (!mCheckExpense.isChecked() && !mCheckIncome.isChecked()) {
            if (c == mCheckExpense) {
                mCheckIncome.setChecked(true);
            }
            else {
                mCheckExpense.setChecked(true);
            }
            return;
        }
        mAdapter.refreshAdapter(getTransactions(NUMBER_LOADED));
    }

    private List<Transaction> mergeExpenseIncome(List<Transaction> l1, List<Transaction> l2, int numberToLoad){
        List<Transaction> transactions = new ArrayList<>();
        Transaction t1, t2;
        int i1 = 0, i2 = 0;

        while(i1 < l1.size() && i2 < l2.size() && ((i1 + i2) < numberToLoad || numberToLoad < 0)){
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

        if (i1 + i2 < numberToLoad || numberToLoad < 0) {
            for (; i1 < l1.size(); i1++)
                transactions.add(l1.get(i1));

            for (; i2 < l2.size(); i2++)
                transactions.add(l2.get(i2));
        }
        mExpenseCounter.add(i1);
        mIncomeCounter.add(i2);
        return transactions;
    }
}
