package net.cashadmin.cashadmin.Activities.Model;


import android.content.ContentValues;
import android.database.Cursor;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.io.Serializable;
import java.util.Date;

public class Expense extends Transaction implements Serializable {

    public static final String TABLE_NAME = "expenses";
    public static final String COLUMN_CATEGORY = "category";

    public static final TypeEnum TYPE = TypeEnum.EXPENSE;

    public static String getTableCreator() {
        return "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOTAL + " INTEGER NOT NULL, " +
                COLUMN_LABEL + " VARCHAR(127), " +
                COLUMN_DATE + " FLOAT NOT NULL, " +
                COLUMN_CATEGORY + " INTEGER DEFAULT 1 REFERENCES " + Category.TABLE_NAME + "(" + Category.COLUMN_ID + ")" + " ON DELETE SET DEFAULT)";
    }

    /**
     * @var Category
     */
    private Category category;

    public Expense() {
        COLUMNS = new String[]{COLUMN_ID, COLUMN_TOTAL, COLUMN_LABEL, COLUMN_DATE, COLUMN_CATEGORY};
        COLUMN_ID_INDEX = 0;
    }

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
        COLUMNS = new String[]{COLUMN_ID, COLUMN_TOTAL, COLUMN_LABEL, COLUMN_DATE, COLUMN_CATEGORY};
        COLUMN_ID_INDEX = 0;
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
        values.put(COLUMN_CATEGORY, category.getId());
        return values;
    }

    @Override
    protected Entity createEntityFromCursor(Cursor c) {
        try {
            return new Expense(
                    c.getInt(c.getColumnIndex(COLUMN_ID)),
                    c.getFloat(c.getColumnIndex(COLUMN_TOTAL)),
                    c.getString(c.getColumnIndex(COLUMN_LABEL)),
                    new java.util.Date(c.getLong(c.getColumnIndex(COLUMN_DATE))),
                    (Category) DataManager.getDataManager().getById(Category.class, c.getInt(c.getColumnIndex(COLUMN_CATEGORY)))
            );
        }
        catch (DataNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
