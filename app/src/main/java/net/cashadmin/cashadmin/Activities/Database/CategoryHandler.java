package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Exception.InvalidQueryException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                COLUMN_LABEL + " VARCHAR(255) NOT NULL, " +
                COLUMN_COLOR + " VARCHAR(11) NOT NULL)");
    }

    @Override
    public boolean insert(Entity entity) {
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
    public boolean update(Entity entity) {
        Category cat = (Category) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, cat.getId());
        values.put(COLUMN_LABEL, cat.getLabel());
        values.put(COLUMN_COLOR, cat.getRGBColor());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.update(TABLE_CATEGORIES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(cat.getId())});
        db.close();
        return true;
    }

    @Override
    public Category findById(int id) throws DataNotFoundException {
        String query = "Select id FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_ID + " = " + id;

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            Category category = new Category(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            cursor.close();
            db.close();
            return category;
        }
        db.close();
        throw new DataNotFoundException("Database.CategoryHandler : findById(int)");
    }

    @Override
    public List<Entity> getAll(TypeEnum type) {
        String query = "SELECT * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Entity> list = new ArrayList<>();

        while (!(cursor.isAfterLast())) {
            Category cat = new Category(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            list.add(cat);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public List<Entity> getFromTo(TypeEnum type, int start, int end) {
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " LIMIT " + start + "," + end;

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Entity> list = new ArrayList<>();

        while (!(cursor.isAfterLast())) {
            Category cat = new Category(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            list.add(cat);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isIn(Entity entity) {
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
    public boolean delete(Entity entity) {
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