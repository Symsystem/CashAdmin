package net.cashadmin.cashadmin.Activities.UI;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.Map;

public class CircleChart {

    private PieChart mChart;
    private String mTitle;
    private DataManager mDataManager;

    private List<Entity> data;

    public CircleChart(PieChart pc, DataManager dm, String title){
        this.mChart = pc;
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
        List<Entity> cats = new ArrayList<Entity>();

        try{
            cats = mDataManager.getAll(TypeEnum.CATEGORY);
        } catch (IllegalTypeException e){
            e.printStackTrace();
        }

        ArrayList<String> categories = new ArrayList<>();
        HashMap<Integer, Counter> expenseByCategory = new HashMap<>();

        for (Entity cat : cats){
            int id = ((Category)cat).getId();
            categories.add(((Category) cat).getLabel());
            expenseByCategory.put(id, new Counter());
        }

        for (Entity exp : data){
            Counter c = (Counter)expenseByCategory.get(((Expense)exp).getCategory().getId());
            c.addOne();
        }

        ArrayList<Entry> yVals = new ArrayList<>();
        int i = 0;
        for (Map.Entry<Integer, Counter> e : expenseByCategory.entrySet())
        {
            yVals.add(new Entry(e.getValue().getCounter().floatValue(), i));
            i++;
        }

        PieDataSet dataSet = new PieDataSet(yVals, "Historique des dépenses");

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(categories, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    public void updateData(){}

    public void draw(){

    }
}
