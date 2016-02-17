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

import net.cashadmin.cashadmin.Activities.Adapter.TransactionHistoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.HistoricEntryEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TransactionEntryEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Income;
import net.cashadmin.cashadmin.Activities.Model.Transaction;
import net.cashadmin.cashadmin.Activities.Utils.Utils;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HistoricActivity extends AppCompatActivity {

    private DataManager mDataManager;
    private TransactionHistoryAdapter mAdapter;
    private ArrayList<Transaction> mExpenses;
    private ArrayList<Transaction> mIncomes;
    private ArrayList<Transaction> transactions;

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

        HistoricEntryEnum entryType = HistoricEntryEnum.detachFrom(intent);

        switch (entryType) {
            case All:
                mExpenses = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getAll(Expense.class);
                mIncomes = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getAll(Income.class);
                break;
            case ByCategory:
                mExpenses = (ArrayList<Transaction>) (ArrayList<?>) mDataManager.getWhere(Expense.class, intent.getStringExtra("expenseCondition"));
                mIncomes = new ArrayList<>();
                Category c = (Category) intent.getSerializableExtra("category");
                relativeLayoutTopBar.setVisibility(View.GONE);
                categoryLabelTopBar.setVisibility(View.VISIBLE);
                categoryLabelTopBar.setText(c.getLabel());
                categoryLabelTopBar.setBackgroundColor(c.getColor());
                break;
        }
        transactions = mergeExpenseIncome(mExpenses, mIncomes);
        mAdapter = new TransactionHistoryAdapter(HistoricActivity.this, transactions, mOnClickDeleteListener, mOnClickEditListener);

        checkedChange(mCheckExpense);
        mHistoryList.setAdapter(mAdapter);
    }

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
