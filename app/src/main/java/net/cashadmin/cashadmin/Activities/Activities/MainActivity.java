package net.cashadmin.cashadmin.Activities.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;

import net.cashadmin.cashadmin.Activities.Alarm.AlarmReceiver;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Functor.TransactionFunctor;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Transaction;
import net.cashadmin.cashadmin.Activities.UI.CircleChart;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
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

        AlarmReceiver alarm = new AlarmReceiver();
        alarm.setAlarm(this);
    }

    @OnClick(R.id.addExpenseButton)
    public void onClickExpenseButton() {
        startActivity(new Intent(MainActivity.this, SelectCategoryActivity.class));
    }

    @OnClick(R.id.addIncomeButton)
    public void onClickIncomeButton(){
        startActivity(new Intent(MainActivity.this, NewIncomeActivity.class));
    }

    @OnClick(R.id.historyButton)
    public void onClickHistoryButton(){
        TransactionFunctor exp = new TransactionFunctor() {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }

            @Override
            public ArrayList<Transaction> getList() throws IllegalTypeException {
                return (ArrayList<Transaction>)(ArrayList<?>) mDataManager.getFromTo(TypeEnum.EXPENSE, 0, 25);
            }
        };
        TransactionFunctor inc = new TransactionFunctor() {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }

            @Override
            public ArrayList<Transaction> getList() throws IllegalTypeException {
                return (ArrayList<Transaction>)(ArrayList<?>) mDataManager.getFromTo(TypeEnum.INCOME, 0, 25);
            }
        };


        Intent intent = new Intent(MainActivity.this, HistoricActivity.class);
        intent.putExtra("expenses", exp);
        intent.putExtra("incomes", inc);

        startActivity(intent);
    }
}
