package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.DBEntity;

public class CategoryHandler extends GenericHandler {

    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LABEL = "label";
    private static final String COLUMN_COLOR = "color";

    private DBHandler mDBHandler;

    public CategoryHandler(DBHandler handler) {
        mDBHandler = handler;

        this.setTableCreator("CREATE TABLE " +
                TABLE_CATEGORIES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_LABEL + "VARCHAR(255) NOT NULL, " +
                COLUMN_COLOR + "VARCHAR(11) NOT NULL)");
    }

    @Override
    public boolean insert(DBEntity entity) {
        Category cat = (Category) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, cat.getId());
        values.put(COLUMN_LABEL, cat.getLabel());
        values.put(COLUMN_COLOR, cat.getRGBColor());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
        return true;
    }

    @Override
    public boolean update(DBEntity entity) {
        return true;
    }

    @Override
    public Category findById(int id) {
        String query = "Select * FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_ID + " = " + id;

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            Category category = new Category(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2)
            );

            db.close();
            return category;
        }
        db.close();
        return null;
    }

    @Override
    public boolean isIn(DBEntity entity) {
        String query = "Select id FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_ID + " = " + ((Category) entity).getId();

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

        String query = "Select id FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_ID + " = " + ((Category) entity).getId();

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete(TABLE_CATEGORIES, COLUMN_ID + " = ?", new String[]{cursor.getString(0)});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
