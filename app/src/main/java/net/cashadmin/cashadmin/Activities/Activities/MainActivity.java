package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.UI.CircleChart;
import net.cashadmin.cashadmin.R;

import java.util.Calendar;
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
    @InjectView(R.id.historyChart)
    PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mDataManager = DataManager.getDataManager(this);
        mHistoryChart = new CircleChart(mPieChart, this, mDataManager, "");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        mHistoryChart.setData(cal.getTime(), new Date());
    }

    @OnClick(R.id.addExpenseButton)
    public void onClickExpenseButton() {
        startActivity(new Intent(MainActivity.this, SelectCategoryActivity.class));
    }

    @OnClick(R.id.addIncomeButton)
    public void onClickIncomeButton(){
        startActivity(new Intent(MainActivity.this, NewIncomeActivity.class));
    }

    private void drawPieChart(){

    }
}
