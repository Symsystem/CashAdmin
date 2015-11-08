package net.cashadmin.cashadmin.Activities.Model;

import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Frequency extends Entity {

    /**
     * @var int
     */
    private int id;

    /**
     * @var TypeEnum
     */
    private TypeEnum transactionType;

    /**
     * @var float
     */
    protected float total;

    /**
     * @var String
     */
    protected String label;

    /**
     * @var FrequencyEnum
     */
    protected FrequencyEnum frequency;

    /**
     * @var Date
     */
    protected Date dateFrequency;

    /**
     * @var Date
     */
    protected Date endDateFrequency;

    /**
     * @var Category
     */
    private Category category;

    /**
     * @param id
     * @param type
     * @param total
     * @param label
     * @param frequency
     * @param dateFrequency
     * @param endDateFrequency
     * @param category
     */
    public Frequency(int id, TypeEnum type, float total, String label, FrequencyEnum frequency, Date dateFrequency, Date endDateFrequency, Category category){
        this.id = id;
        mType = TypeEnum.FREQUENCY;
        this.total = total;
        this.label = label;
        this.transactionType = type;
        this.frequency = frequency;
        this.dateFrequency = dateFrequency;
        this.endDateFrequency = endDateFrequency;
        this.category = category;
    }

    /**
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * @return TypeEnum
     */
    public TypeEnum getTransactionType(){
        return transactionType;
    }

    /**
     * @return float
     */
    public float getTotal() {
        return total;
    }

    /**
     * @param total
     */
    public void setTotal(float total){
        this.total = total;
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


    /**
     * @return Date
     */
    public Date getDateFrequency(){
        return dateFrequency;
    }

    /**
     * @param dateFrequency
     */
    public void setDateFrequency(Date dateFrequency){
        this.dateFrequency = dateFrequency;
    }

    /**
     * @return Date
     */
    public Date getEndDateFrequency(){
        return endDateFrequency;
    }

    /**
     * @param endDateFrequency
     */
    public void setEndDateFrequency(Date endDateFrequency){
        this.endDateFrequency = endDateFrequency;
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
     * @return String
     */
    public String getStringDate() {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return formater.format(dateFrequency);
    }

    /**
     * @return String
     */
    public String getStringSQLDate(){
//        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        return formater.format(date);
        return String.valueOf(dateFrequency.getTime());
    }

    /**
     * @return String
     */
    public String getStringSQLEndDate(){
        return String.valueOf(endDateFrequency.getTime());
    }

    /**
     * @return java.sql.Date
     */
    public java.sql.Date getSQLDate(){
        return new java.sql.Date(this.dateFrequency.getTime());
    }
}
