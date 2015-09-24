package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Transaction;

import java.util.Date;
import java.util.List;


public abstract class TransactionHandler extends GenericHandler {

    private final String TABLE_NAME;
    protected static final String COLUMN_ID = "id";
    protected static final String COLUMN_TOTAL = "total";
    protected static final String COLUMN_DATE = "date";
    protected static final String COLUMN_CATEGORY = "category";

    protected final CategoryHandler mCategoryHandler;

    public TransactionHandler(DBHandler handler, String tableName, CategoryHandler catHandler) {
        mDBHandler = handler;
        TABLE_NAME = tableName;
        mCategoryHandler = catHandler;

        this.setTableCreator("CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOTAL + " INTEGER NOT NULL, " +
                COLUMN_DATE + " DATETIME NOT NULL, " +
                COLUMN_CATEGORY + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY + ") REFERENCES " + CategoryHandler.TABLE_NAME + ")");
    }

    @Override
    public boolean insert(Entity entity) {
        Transaction transaction = (Transaction) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL, transaction.getTotal());
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_CATEGORY, transaction.getCategory().getId());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
        return true;
    }

    @Override
    public boolean update(Entity entity) {
        Transaction transaction = (Transaction) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL, transaction.getTotal());
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_CATEGORY, transaction.getCategory().getId());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(transaction.getId())});
        db.close();
        return true;
    }

    @Override
    public abstract Entity findById(int id) throws DataNotFoundException;

    @Override
    public abstract Entity getLast(TypeEnum type) throws DataNotFoundException;

    @Override
    public abstract List<Entity> getAll(TypeEnum type);

    @Override
    public abstract List<Entity> getFromTo(TypeEnum type, int start, int end);

    @Override
    public abstract List<Entity> getByDate(TypeEnum type, Date startDate, Date endDate);

    @Override
    public boolean isIn(Entity entity) {
        String query = "SELECT id FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + ((Transaction) entity).getId();

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

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ((Transaction) entity).getId();

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
    public boolean deleteBy(TypeEnum type, String condition){
        boolean result = false;

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + condition;

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while(!(cursor.isAfterLast())){
            db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{cursor.getString(0)});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    @Override
    protected Entity createEntityFromCursor(Cursor c) {
        return null;
    }
}
