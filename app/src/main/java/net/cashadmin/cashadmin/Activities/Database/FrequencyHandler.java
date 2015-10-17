package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Frequency;

import java.sql.Date;
import java.util.List;

public class FrequencyHandler extends GenericHandler {

    public static final String TABLE_NAME = "frequencies";
    protected static final String COLUMN_ID = "id";
    protected static final String COLUMN_TYPE_TRANSACTION = "typeTransaction";
    protected static final String COLUMN_TOTAL = "total";
    protected static final String COLUMN_LABEL = "label";
    protected static final String COLUMN_FREQUENCY = "frequency";
    protected static final String COLUMN_DATE_FREQUENCY = "dateFrequency";
    protected static final String COLUMN_CATEGORY = "category";

    protected final CategoryHandler mCategoryHandler;

    public FrequencyHandler(DBHandler handler, CategoryHandler catHandler){
        mDBHandler = handler;
        mCategoryHandler = catHandler;

        this.setTableCreator("CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE_TRANSACTION + " VARCHAR(16) NOT NULL CHECK(" + COLUMN_TYPE_TRANSACTION + " in " +
                "('" + TypeEnum.values()[1].toString() +
                "','" + TypeEnum.values()[2].toString() + "')), " +
                COLUMN_TOTAL + " INTEGER NOT NULL, " +
                COLUMN_LABEL + " VARCHAR(127), " +
                COLUMN_FREQUENCY + " VARCHAR(16) NOT NULL CHECK(" + COLUMN_FREQUENCY + " in " +
                "('" + FrequencyEnum.values()[1].toString() +
                "','" + FrequencyEnum.values()[2].toString() +
                "','" + FrequencyEnum.values()[3].toString() +
                "','" + FrequencyEnum.values()[4].toString() + "')), " +
                COLUMN_DATE_FREQUENCY + " FLOAT NOT NULL, " +
                COLUMN_CATEGORY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY + ") REFERENCES " + CategoryHandler.TABLE_NAME +")");
    }

    @Override
    public boolean insert(Entity entity) {
        Frequency frequency = (Frequency) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE_TRANSACTION, frequency.getTransactionType().toString());
        values.put(COLUMN_TOTAL, frequency.getTotal());
        values.put(COLUMN_LABEL, frequency.getLabel());
        values.put(COLUMN_FREQUENCY, frequency.getFrequency().toString());
        values.put(COLUMN_DATE_FREQUENCY, frequency.getStringSQLDate());
        values.put(COLUMN_CATEGORY, frequency.getCategory().getId());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
        return true;
    }

    @Override
    public boolean update(Entity entity) {
        Frequency frequency = (Frequency) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE_TRANSACTION, frequency.getTransactionType().toString());
        values.put(COLUMN_TOTAL, frequency.getTotal());
        values.put(COLUMN_LABEL, frequency.getLabel());
        values.put(COLUMN_FREQUENCY, frequency.getFrequency().toString());
        values.put(COLUMN_DATE_FREQUENCY, frequency.getStringSQLDate());
        values.put(COLUMN_CATEGORY, frequency.getCategory().getId());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(frequency.getId())});
        db.close();
        return true;
    }

    @Override
    public Entity findById(int id) throws DataNotFoundException {

        String query = "SELECT id FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;

        List<Entity> l = createEntityListFromQuery(query);

        if (l.size() > 0)
            return l.get(0);

        throw new DataNotFoundException("Database.IncomeHandler : findById(int)");
    }

    @Override
    public Entity getLast(TypeEnum type) throws DataNotFoundException {
        return null;
    }

    @Override
    public List<Entity> getAll(TypeEnum type) {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE_FREQUENCY + " DESC";

        return createEntityListFromQuery(query);
    }

    @Override
    public List<Entity> getFromTo(TypeEnum type, int start, int end) {
        String query = "SELECT * FROM " + TABLE_NAME + " LIMIT " + start + ", " + end;

        return createEntityListFromQuery(query);
    }

    @Override
    public List<Entity> getByDate(TypeEnum type, Date startDate, Date endDate) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE_FREQUENCY + " BETWEEN " + startDate.getTime() + " and " + endDate.getTime() + "' ORDER BY " + COLUMN_DATE_FREQUENCY + " DESC";

        return createEntityListFromQuery(query);
    }

    @Override
    public boolean isIn(Entity entity) {
        String query = "SELECT id FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + ((Frequency) entity).getId();

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    @Override
    public boolean delete(Entity entity) {
        boolean result = false;

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ((Frequency) entity).getId();

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{cursor.getString(0)});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    @Override
    public boolean deleteBy(TypeEnum type, String condition) {
        boolean result = false;

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + condition;

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while (!(cursor.isAfterLast())) {
            db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{cursor.getString(0)});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    @Override
    protected Entity createEntityFromCursor(Cursor c) {
        try {
            return new Frequency(
                    c.getInt(c.getColumnIndex(COLUMN_ID)),
                    TypeEnum.valueOf(c.getString(c.getColumnIndex(COLUMN_TYPE_TRANSACTION))),
                    c.getFloat(c.getColumnIndex(COLUMN_TOTAL)),
                    c.getString(c.getColumnIndex(COLUMN_LABEL)),
                    FrequencyEnum.valueOf(c.getString(c.getColumnIndex(COLUMN_FREQUENCY))),
                    new java.util.Date(c.getLong(c.getColumnIndex(COLUMN_DATE_FREQUENCY))),
                    (Category) mCategoryHandler.findById(c.getInt(c.getColumnIndex(COLUMN_CATEGORY)))
            );
        }catch (DataNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
