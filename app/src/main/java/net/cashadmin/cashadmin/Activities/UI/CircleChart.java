package net.cashadmin.cashadmin.Activities.UI;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Utils.Counter;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CircleChart {

    private PieChart mChart;
    private String mTitle;
    private Context mContext;
    private DataManager mDataManager;

    private ArrayList<Category> cats;

    private List<Entity> data;

    public CircleChart(PieChart pc, Context context, DataManager dm, String title){
        this.mChart = pc;
        this.mContext = context;
        this.mDataManager = dm;
        this.mTitle = title;

        this.data = new ArrayList<>();

        mChart.setUsePercentValues(true);
        mChart.setDescription(title);
        mChart.setDescriptionColor(R.color.primary_dark);
        mChart.setDragDecelerationFrictionCoef(0.95f);
    }

    public void setData(int numberLoaded){
        data = mDataManager.getFromTo(Expense.class, 0, numberLoaded);
        this.setData();
    }

    public void setData(int from, int to){
        data = mDataManager.getFromTo(Expense.class, from, to);
        this.setData();
    }

    public void setData(Date from, Date to) {
        data = mDataManager.getByDate(Expense.class, from, to);
        this.setData();
    }

    private void setData() {
        cats = (ArrayList<Category>) (ArrayList<?>) mDataManager.getAll(Category.class);

        HashMap<Integer, Counter> expenseByCategory = new HashMap<>();

        for (Category cat : cats) {
            expenseByCategory.put(cat.getId(), new Counter());
        }

        float totalAmount = 0;

        for (Entity ent : data) {
            Expense exp = (Expense) ent;
            Counter c = expenseByCategory.get(exp.getCategory().getId());
            c.add(exp.getTotal());
            totalAmount += exp.getTotal();
        }

        ArrayList<String> categories = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        int i = 0;
        for (Category cat : cats) {
            float count = expenseByCategory.get(cat.getId()).getCounterFloat();
            if (count != 0) {
                yVals.add(new Entry(count, i));
                categories.add(cat.getLabel());
                colors.add(cat.getColor());
                i++;
            }
        }

        PieDataSet dataSet = new PieDataSet(yVals, "");

        dataSet.setColors(colors);

        PieData data = new PieData(categories, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();

        mChart.setExtraOffsets(0, 0, 75f, 0);
        mChart.setCenterText("Total " + totalAmount);
        mChart.setDrawSliceText(false);

        Legend legend = mChart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        legend.setMaxSizePercent(0.95f);
    }

    public void setChartValueSelectedListener(OnChartValueSelectedListener listener){
        mChart.setOnChartValueSelectedListener(listener);
    }

    public ArrayList<Category> getCategories(){
        return this.cats;
    }

    public void updateData(){}

    public void draw(){

    }
}
