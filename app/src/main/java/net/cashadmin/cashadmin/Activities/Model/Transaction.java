package net.cashadmin.cashadmin.Activities.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class Transaction extends Entity implements Serializable{

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_DATE = "date";

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

    public List<Entity> getByDate(SQLiteOpenHelper db, Date startDate, Date endDate) {
        String query = "SELECT * FROM " + getTableName()
                + " WHERE " + COLUMN_DATE
                + " BETWEEN '" + startDate.getTime() + "' and '" + endDate.getTime()
                + "' ORDER BY " + COLUMN_DATE + " DESC";
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        List<Entity> list = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(createEntityFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return list;
    }

    @Override
    protected String getOrderByCondition() {
        return " ORDER BY " + COLUMN_DATE + " DESC";
    }
}
