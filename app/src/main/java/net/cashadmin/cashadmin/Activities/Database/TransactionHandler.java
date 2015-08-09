package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Model.DBEntity;
import net.cashadmin.cashadmin.Activities.Model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TransactionHandler extends GenericHandler {

    private static final String TABLE_CATEGORIES = "categories";

    private final String TABLE_NAME;
    protected static final String COLUMN_ID = "id";
    protected static final String COLUMN_TOTAL = "total";
    protected static final String COLUMN_DATE = "date";
    protected static final String COLUMN_CATEGORY = "category";

    protected DBHandler mDBHandler;

    public TransactionHandler(DBHandler handler, String tableName) {
        mDBHandler = handler;
        TABLE_NAME = tableName;

        this.setTableCreator("CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TOTAL + "INTEGER NOT NULL, " +
                COLUMN_DATE + "DATETIME NOT NULL, " +
                COLUMN_CATEGORY + "INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES + ")");
    }

    @Override
    public boolean insert(DBEntity entity) {
        Transaction transaction = (Transaction) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, transaction.getId());
        values.put(COLUMN_TOTAL, transaction.getTotal());
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_CATEGORY, transaction.getCategory().toString());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
        ;
        return true;
    }

    @Override
    public boolean update(DBEntity entity) {
        Transaction transaction = (Transaction) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, transaction.getId());
        values.put(COLUMN_TOTAL, transaction.getTotal());
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_CATEGORY, transaction.getCategory().toString());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(transaction.getId())});
        db.close();
        return true;
    }

    @Override
    public DBEntity findById(int id) {
        return null;
    }

    @Override
    public boolean isIn(DBEntity entity) {
        String query = "SELECT id FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + ((Transaction) entity).getId();

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    @Override
    public boolean delete(DBEntity entity) {
        boolean result = false;

        String query = "Select id FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ((Transaction) entity).getId();

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{cursor.getString(0)});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
