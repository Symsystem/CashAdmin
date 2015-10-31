package net.cashadmin.cashadmin.Activities.Model;


import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.io.Serializable;
import java.util.Date;

public class Income extends Transaction implements Serializable {

    /**
     * @param id
     * @param total
     * @param date
     * @param label
     */
    public Income(int id, float total,String label, Date date) {
        this.id = id;
        this.total = total;
        this.label = label;
        this.date = date;
        this.mType = TypeEnum.INCOME;
    }
}
