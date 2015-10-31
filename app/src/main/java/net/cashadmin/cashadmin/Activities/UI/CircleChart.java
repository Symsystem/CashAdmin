package net.cashadmin.cashadmin.Activities.UI;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.PercentFormatter;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
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

        this.data = new ArrayList<Entity>();

        mChart.setUsePercentValues(true);
        mChart.setDescription(title);
        mChart.setDescriptionColor(R.color.primary_dark);
        mChart.setDragDecelerationFrictionCoef(0.95f);
    }

    public void setData(int numberLoaded){
        try {
            data = mDataManager.getFromTo(TypeEnum.EXPENSE, 0, numberLoaded);
        } catch (IllegalTypeException e) {
            e.printStackTrace();
        }
        this.setData();
    }

    public void setData(int from, int to){
        try {
            data = mDataManager.getFromTo(TypeEnum.EXPENSE, from, to);
        } catch (IllegalTypeException e) {
            e.printStackTrace();
        }
        this.setData();
    }

    public void setData(Date from, Date to){
        try{
            data = mDataManager.getByDate(TypeEnum.EXPENSE, from, to);
        } catch (IllegalTypeException e){
            e.printStackTrace();
        }
        this.setData();
    }

    private void setData(){
        cats = new ArrayList<Category>();

        try{
            cats = (ArrayList<Category>) (ArrayList<?>) mDataManager.getAll(TypeEnum.CATEGORY);
        } catch (IllegalTypeException e){
            e.printStackTrace();
        }

        ArrayList<String> categories = new ArrayList<>();
        HashMap<Integer, Counter> expenseByCategory = new HashMap<>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (Category cat : cats){
            categories.add(cat.getLabel());
            expenseByCategory.put(cat.getId(), new Counter());
            colors.add(cat.getColor());
        }

        float totalAmount = 0;

        for (Entity ent : data){
            Expense exp = (Expense) ent;
            Counter c = expenseByCategory.get(exp.getCategory().getId());
            c.add(exp.getTotal());
            totalAmount += exp.getTotal();
        }

        ArrayList<Entry> yVals = new ArrayList<>();
        int i = 0;
        for (Entity entity : cats)
        {
            Category cat = (Category)entity;
            yVals.add(new Entry(expenseByCategory.get(cat.getId()).getCounterFloat(), i));
            i++;
        }

        PieDataSet dataSet = new PieDataSet(yVals, mContext.getString(R.string.labelCategory));

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
