package net.cashadmin.cashadmin.Activities.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Transaction implements DBEntity {

    /**
     * @var int
     */
    protected int id;

    /**
     * @var float
     */
    protected float total;

    /**
     * @var Date
     */
    protected Date date;

    /**
     * @var Category
     */
    protected Category category;

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
}
