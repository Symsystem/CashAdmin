package net.cashadmin.cashadmin.Activities.Model;


import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.util.Date;

public class Expense extends Transaction {

    public Expense(int id, float total, Date date, Category category) {
        this.id = id;
        this.total = total;
        this.date = date;
        this.category = category;
        this.mType = TypeEnum.EXPENSE;
    }

    /*public Expense(int id, float total, Date date, int category) {

    }*/
}
