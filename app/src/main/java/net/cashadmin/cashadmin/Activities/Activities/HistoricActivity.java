package net.cashadmin.cashadmin.Activities.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import net.cashadmin.cashadmin.Activities.Adapter.TransactionHistoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Transaction;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HistoricActivity extends AppCompatActivity {

    private DataManager mDataManager;

    @InjectView(R.id.historyList)
    ListView mHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        ButterKnife.inject(this);
        mDataManager = DataManager.getDataManager();
        try {
            ArrayList<Transaction> transactions = mergeExpenseIncome(
                    mDataManager.getAll(TypeEnum.EXPENSE),
                    mDataManager.getAll(TypeEnum.INCOME));

            TransactionHistoryAdapter transactionAdapter = new TransactionHistoryAdapter(HistoricActivity.this, transactions);
            mHistoryList.setAdapter(transactionAdapter);
        } catch (IllegalTypeException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Transaction> mergeExpenseIncome(List<Entity> l1, List<Entity> l2){
        ArrayList<Transaction> transactions = new ArrayList<>();
        Transaction t1, t2;
        int i1 = 0, i2 = 0;

        while(i1 < l1.size() && i2 < l2.size()){
            t1 = (Transaction) l1.get(i1);
            t2 = (Transaction) l2.get(i2);

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
