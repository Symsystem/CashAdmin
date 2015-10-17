package net.cashadmin.cashadmin.Activities.Model;

import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Transaction extends Entity {

    /**
     * @var int
     */
    protected int id;

    /**
     * @var float
     */
    protected float total;

    /**
     * @var String
     */
    protected String label;

    /**
     * @var Date
     */
    protected Date date;

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
     * @return java.util.Date
     */
    public Date getDate(){
        return this.date;
    }

    /**
     * @return String
     */
    public String getStringDate() {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return formater.format(date);
    }

    /**
     * @return String
     */
    public String getStringSQLDate(){
//        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        return formater.format(date);
        return String.valueOf(date.getTime());
    }

    /**
     * @return java.sql.Date
     */
    public java.sql.Date getSQLDate(){
        return new java.sql.Date(this.date.getTime());
    }
}
