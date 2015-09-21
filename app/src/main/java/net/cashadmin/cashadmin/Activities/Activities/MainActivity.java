package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Income;
import net.cashadmin.cashadmin.R;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    private DataManager mDataManager;

    @InjectView(R.id.addExpenseButton)
    TextView mAddExpenseButton;
    @InjectView(R.id.addIncomeButton)
    TextView mAddIncomeButton;
    @InjectView(R.id.lastExpense)
    TextView mLastExpense;
    @InjectView(R.id.lastIncome)
    TextView mLastIncome;
    @InjectView(R.id.totalExpense)
    TextView mTotalExpense;
    @InjectView(R.id.totalIncome)
    TextView mTotalIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mDataManager = new DataManager(this);

        setHistory();
    }

    @OnClick(R.id.addExpenseButton)
    public void onClickExpenseButton(){
        startActivity(new Intent(MainActivity.this, SelectCategoryActivity.class));
    }

    private void setHistory()
    {
        Date currentDate = new Date();

        Expense lastExpense = null;
        Income lastIncome = null;

        try{
            lastExpense = (Expense)mDataManager.getLast(TypeEnum.EXPENSE);
            lastIncome = (Income)mDataManager.getLast(TypeEnum.INCOME);

        } catch (DataNotFoundException e){
            e.printStackTrace();
        }

        mLastExpense.setText(lastExpense != null ? lastExpense.getTotal() + " €" : "Pas de dépense");
        mLastIncome.setText(lastIncome != null ? lastIncome.getTotal() + " €" : "Pas de revenu");

        List<Entity> expenseCycle = null;
        List<Entity> incomeCycle = null;

        try{
            expenseCycle = mDataManager.getAll(TypeEnum.EXPENSE);
            incomeCycle = mDataManager.getAll(TypeEnum.INCOME);
        } catch (IllegalTypeException e){
            e.printStackTrace();
        }

        float sumExpenses = 0;
        if(!(expenseCycle.isEmpty())){
            for(Iterator it=expenseCycle.iterator(); it.hasNext();){
                Expense ex = (Expense)it.next();
                sumExpenses = sumExpenses + ex.getTotal();
            }
            mTotalExpense.setText(sumExpenses + " €");
        }

        float sumIncomes = 0;
        if(!(incomeCycle.isEmpty())){
            for(Iterator it=incomeCycle.iterator(); it.hasNext();){
                Income in = (Income)it.next();
                sumIncomes = sumIncomes + in.getTotal();
            }
            mTotalIncome.setText(sumIncomes + " €");
        }
    }
}
