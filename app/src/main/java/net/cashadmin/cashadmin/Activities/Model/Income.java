package net.cashadmin.cashadmin.Activities.Model;


import android.content.ContentValues;
import android.database.Cursor;

import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.io.Serializable;
import java.util.Date;

public class Income extends Transaction implements Serializable {

    public static final String TABLE_NAME = "incomes";

    public static final TypeEnum TYPE = TypeEnum.INCOME;

    public static String getTableCreator() {
        return "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOTAL + " INTEGER NOT NULL, " +
                COLUMN_LABEL + " VARCHAR(127), " +
                COLUMN_DATE + " FLOAT NOT NULL" + ")";
    }

    public Income() {
        COLUMNS = new String[]{COLUMN_ID, COLUMN_TOTAL, COLUMN_LABEL, COLUMN_DATE};
        COLUMN_ID_INDEX = 0;
    }

    /**
     * @param id
     * @param total
     * @param date
     * @param label
     */
    public Income(int id, float total, String label, Date date) {
        this.id = id;
        this.total = total;
        this.label = label;
        this.date = date;
        COLUMNS = new String[]{COLUMN_ID, COLUMN_TOTAL, COLUMN_LABEL, COLUMN_DATE};
        COLUMN_ID_INDEX = 0;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public TypeEnum getType() {
        return TYPE;
    }

    @Override
    public String getColumnId() {
        return COLUMNS[COLUMN_ID_INDEX];
    }

    @Override
    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_TOTAL, total);
        values.put(COLUMN_LABEL, label);
        values.put(COLUMN_DATE, getStringSQLDate());
        return values;
    }

    @Override
    protected Entity createEntityFromCursor(Cursor c) {
        return new Income(
                c.getInt(c.getColumnIndex(COLUMN_ID)),
                c.getFloat(c.getColumnIndex(COLUMN_TOTAL)),
                c.getString(c.getColumnIndex(COLUMN_LABEL)),
                new java.util.Date(c.getLong(c.getColumnIndex(COLUMN_DATE)))
        );
    }
}
