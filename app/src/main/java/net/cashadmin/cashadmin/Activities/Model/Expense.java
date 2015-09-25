package net.cashadmin.cashadmin.Activities.Model;


import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense extends Entity {

    /**
     * @var int
     */
    private int id;

    /**
     * @var float
     */
    private float total;

    /**
     * @var String
     */
    private String label;

    /**
     * @var Date
     */
    private Date date;

    /**
     * @var Category
     */
    private Category category;

    /**
     * @var FrequencyEnum
     */
    private FrequencyEnum frequency;

    /**
     *
     * @param id
     * @param total
     * @param label
     * @param date
     * @param category
     * @param frequency
     */
    public Expense(int id, float total, String label, Date date, Category category, FrequencyEnum frequency) {
        this.id = id;
        this.total = total;
        this.label = label;
        this.date = date;
        this.category = category;
        this.frequency = frequency;
        this.mType = TypeEnum.EXPENSE;
    }

    /*public Expense(int id, float total, Date date, int category) {

    }*/

    /**
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * @return float
     */
    public float getTotal() {
        return total;
    }

    /**
     * @return String
     */
    public String getLabel(){
        return label;
    }

    /**
     * @param label
     */
    public void setLabel(String label){
        this.label = label;
    }

    /**
     * @return String
     */
    public String getDate() {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        return formater.format(date);
    }

    /**
     * @return Category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return FrenquencyEnum
     */
    public FrequencyEnum getFrequency(){
        return frequency;
    }

    /**
     * @parem frenquency
     */
    public void setFrenquency(FrequencyEnum frenquency){
        this.frequency = frenquency;
    }
}
