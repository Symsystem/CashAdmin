package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.UI.CircleChart;
import net.cashadmin.cashadmin.R;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    private DataManager mDataManager;
    private CircleChart mHistoryChart;

    @InjectView(R.id.addExpenseButton)
    TextView mAddExpenseButton;
    @InjectView(R.id.addIncomeButton)
    TextView mAddIncomeButton;
    /*@InjectView(R.id.lastExpense)
    TextView mLastExpense;
    @InjectView(R.id.lastIncome)
    TextView mLastIncome;
    @InjectView(R.id.totalExpense)
    TextView mTotalExpense;
    @InjectView(R.id.totalIncome)
    TextView mTotalIncome;*/
    @InjectView(R.id.historyChart)
    PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mDataManager = new DataManager(this);
        mHistoryChart = new CircleChart(mPieChart, mDataManager, "Historique des dépenses");

        Category cat1 = new Category(mDataManager.getNextId(TypeEnum.CATEGORY), "Test1", "#ff8d00");
        Category cat2 = new Category(mDataManager.getNextId(TypeEnum.CATEGORY), "Test2", "#7e0000");
        Category cat3 = new Category(mDataManager.getNextId(TypeEnum.CATEGORY), "Test3", "#f9bc00");

        Expense exp1 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 10f, new Date(), cat1);
        Expense exp2 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 5f, new Date(), cat1);
        Expense exp3 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 6f, new Date(), cat1);
        Expense exp4 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 13f, new Date(), cat2);
        Expense exp5 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 17f, new Date(), cat2);
        Expense exp6 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 3f, new Date(), cat2);
        Expense exp7 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 4f, new Date(), cat3);
        Expense exp8 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 8f, new Date(), cat3);
        Expense exp9 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 9f, new Date(), cat3);
        Expense exp10 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 11f, new Date(), cat3);
        Expense exp11 = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), 23f, new Date(), cat3);

        mDataManager.insert(cat1);
        mDataManager.insert(cat2);
        mDataManager.insert(cat3);

        mDataManager.insert(exp1);
        mDataManager.insert(exp2);
        mDataManager.insert(exp3);
        mDataManager.insert(exp4);
        mDataManager.insert(exp5);
        mDataManager.insert(exp6);
        mDataManager.insert(exp7);
        mDataManager.insert(exp8);
        mDataManager.insert(exp9);
        mDataManager.insert(exp10);
        mDataManager.insert(exp11);

        mHistoryChart.setData(10);
    }

    @OnClick(R.id.addExpenseButton)
    public void onClickExpenseButton() {
        startActivity(new Intent(MainActivity.this, SelectCategoryActivity.class));
    }

    private void drawPieChart(){

    }


    /*private void setHistory() {
        Date currentDate = new Date();

        Expense lastExpense = null;
        Income lastIncome = null;

        try {
            lastExpense = (Expense) mDataManager.getLast(TypeEnum.EXPENSE);
            lastIncome = (Income) mDataManager.getLast(TypeEnum.INCOME);

        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }

        mLastExpense.setText(lastExpense != null ? lastExpense.getTotal() + " €" : getString(R.string.noExpense));
        mLastIncome.setText(lastIncome != null ? lastIncome.getTotal() + " €" : getString(R.string.noIncome));

        List<Entity> expenseCycle = null;
        List<Entity> incomeCycle = null;

        try {
            expenseCycle = mDataManager.getAll(TypeEnum.EXPENSE);
            incomeCycle = mDataManager.getAll(TypeEnum.INCOME);
        } catch (IllegalTypeException e) {
            e.printStackTrace();
        }

        float sumExpenses = 0;
        if (!(expenseCycle.isEmpty())) {
            for (Iterator it = expenseCycle.iterator(); it.hasNext(); ) {
                Expense ex = (Expense) it.next();
                sumExpenses = sumExpenses + ex.getTotal();
            }
            mTotalExpense.setText(sumExpenses + " €");
        }

        float sumIncomes = 0;
        if (!(incomeCycle.isEmpty())) {
            for (Iterator it = incomeCycle.iterator(); it.hasNext(); ) {
                Income in = (Income) it.next();
                sumIncomes = sumIncomes + in.getTotal();
            }
            mTotalIncome.setText(sumIncomes + " €");
        }
    }*/
}
