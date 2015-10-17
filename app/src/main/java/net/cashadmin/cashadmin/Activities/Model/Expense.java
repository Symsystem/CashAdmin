package net.cashadmin.cashadmin.Activities.Model;


import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.util.Date;

public class Expense extends Transaction {

    /**
     * @var Category
     */
    private Category category;

    /**
     * @param id
     * @param total
     * @param label
     * @param date
     * @param category
     */
    public Expense(int id, float total, String label, Date date, Category category) {
        this.id = id;
        this.total = total;
        this.label = label;
        this.date = date;
        this.category = category;
        this.mType = TypeEnum.EXPENSE;
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
