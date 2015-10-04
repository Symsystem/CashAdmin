package net.cashadmin.cashadmin.Activities.Model;


import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.util.Date;

public class Income extends Transaction {

    /**
     * @param id
     * @param total
     * @param date
     * @param label
     * @param frequency
     */
    public Income(int id, float total,String label, Date date, FrequencyEnum frequency) {
        this.id = id;
        this.total = total;
        this.label = label;
        this.date = date;
        this.frequency = frequency;

        this.mType = TypeEnum.INCOME;
    }
}
