package net.cashadmin.cashadmin.Activities.Model;


import java.util.Date;

public class Income extends Transaction{

    public Income(int id, float total, Date date, Category category){
        this.id = id;
        this.total = total;
        this.date = date;
        this.category = category;
    }
}
