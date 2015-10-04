package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Income;

import java.sql.Date;
import java.util.List;

public class IncomeHandler extends GenericHandler {

    public static final String TABLE_NAME = "incomes";
    protected static final String COLUMN_ID = "id";
    protected static final String COLUMN_TOTAL = "total";
    protected static final String COLUMN_LABEL = "label";
    protected static final String COLUMN_DATE = "date";
    protected static final String COLUMN_FREQUENCY = "frequency";

    public IncomeHandler(DBHandler handler) {
        mDBHandler = handler;

        this.setTableCreator("CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOTAL + " INTEGER NOT NULL, " +
                COLUMN_LABEL + " VARCHAR(127), " +
                COLUMN_DATE + " FLOAT NOT NULL, " +
                COLUMN_FREQUENCY + " VARCHAR(16) NOT NULL" + ")");
    }

    @Override
    public boolean insert(Entity entity) {
        Income income = (Income) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL, income.getTotal());
        values.put(COLUMN_LABEL, income.getLabel());
        values.put(COLUMN_DATE, income.getStringSQLDate());
        values.put(COLUMN_FREQUENCY, income.getFrequency().toString());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
        return true;
    }

    @Override
    public boolean update(Entity entity) {
        Income income = (Income) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL, income.getTotal());
        values.put(COLUMN_LABEL, income.getLabel());
        values.put(COLUMN_DATE, income.getStringSQLDate());
        values.put(COLUMN_FREQUENCY, income.getFrequency().toString());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(income.getId())});
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
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE + " DESC LIMIT 1";

        List<Entity> l = createEntityListFromQuery(query);

        if (l.size() > 0)
            return l.get(0);

        throw new DataNotFoundException("Database.IncomeHandler : getLast(TypeEnum)");
    }

    @Override
    public List<Entity> getAll(TypeEnum type) {
        String query = "SELECT * FROM " + TABLE_NAME  + " ORDER BY " + COLUMN_DATE + " DESC";

        return createEntityListFromQuery(query);
    }

    @Override
    public List<Entity> getFromTo(TypeEnum type, int start, int end) {
        String query = "SELECT * FROM " + TABLE_NAME + " LIMIT " + start + ", " + end;

        return createEntityListFromQuery(query);
    }

    @Override
    public List<Entity> getByDate(TypeEnum type, Date startDate, Date endDate) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " BETWEEN " + startDate.getTime() + " and " + endDate.getTime() + "' ORDER BY " + COLUMN_DATE + " DESC";

        return createEntityListFromQuery(query);
    }

    @Override
    public boolean isIn(Entity entity) {
        String query = "SELECT id FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + ((Income) entity).getId();

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

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ((Income) entity).getId();

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
        return new Income(
                c.getInt(c.getColumnIndex(COLUMN_ID)),
                c.getFloat(c.getColumnIndex(COLUMN_TOTAL)),
                c.getString(c.getColumnIndex(COLUMN_LABEL)),
                new java.util.Date(c.getLong(c.getColumnIndex(COLUMN_DATE))),
                FrequencyEnum.valueOf(c.getString(c.getColumnIndex(COLUMN_FREQUENCY)))
        );
    }
}
