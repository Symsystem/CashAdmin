package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import net.cashadmin.cashadmin.Activities.Alarm.AlarmReceiver;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.HistoricEntryEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TransactionEntryEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.UI.CircleChart;
import net.cashadmin.cashadmin.R;

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private DataManager mDataManager;
    private CircleChart mHistoryChart;

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
        mHistoryChart.setChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Intent intent = new Intent(MainActivity.this, HistoricActivity.class);

                HistoricEntryEnum.ByCategory.attachTo(intent);
                Category c = mHistoryChart.getCategories().get(e.getXIndex());
                intent.putExtra("category", c);
                intent.putExtra("expenseCondition", Expense.COLUMN_CATEGORY + " = " + c.getId());
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {}
        });

        AlarmReceiver alarm = new AlarmReceiver();
        alarm.setAlarm(this);
    }

    @OnClick(R.id.addExpenseButton)
    public void onClickExpenseButton() {
        startActivity(new Intent(MainActivity.this, SelectCategoryActivity.class));
    }

    @OnClick(R.id.addIncomeButton)
    public void onClickIncomeButton(){
        Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
        TransactionEntryEnum.New.attachTo(intent);
        startActivity(intent);
    }

    @OnClick(R.id.historyButton)
    public void onClickHistoryButton() {

        Intent intent = new Intent(MainActivity.this, HistoricActivity.class);

        HistoricEntryEnum.All.attachTo(intent);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu item) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_button, item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_cat:
                startActivity(new Intent(MainActivity.this, EditCategoryActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.permanentOrder:
                startActivity(new Intent(MainActivity.this, FrequencyTransactionActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
