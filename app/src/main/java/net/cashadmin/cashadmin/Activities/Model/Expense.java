package net.cashadmin.cashadmin.Activities.Model;


import java.util.Date;

public class Expense extends Transaction {

    public Expense(int id, float total, Date date, int category){
        this.id = id;
        this.total = total;
        this.date = date;
        this.category = category;
    }
}
