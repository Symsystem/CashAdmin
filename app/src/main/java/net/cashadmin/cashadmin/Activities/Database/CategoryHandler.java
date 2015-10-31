package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.util.List;

public class CategoryHandler extends GenericHandler {

    public static final String TABLE_NAME = "categories";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_COLOR = "color";

    public CategoryHandler(DBHandler handler) {
        mDBHandler = handler;

        this.setTableCreator("CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LABEL + " VARCHAR(255) NOT NULL, " +
                COLUMN_COLOR + " VARCHAR(11) NOT NULL)");
    }

    @Override
    public boolean insert(Entity entity) {
        Category cat = (Category) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL, cat.getLabel());
        values.put(COLUMN_COLOR, cat.getRGBColor());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
        return true;
    }

    @Override
    public boolean update(Entity entity) {
        Category cat = (Category) entity;
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL, cat.getLabel());
        values.put(COLUMN_COLOR, cat.getRGBColor());

        SQLiteDatabase db = mDBHandler.getWritableDatabase();

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(cat.getId())});
        db.close();
        return true;
    }

    @Override
    public Entity findById(int id) throws DataNotFoundException {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;

        List<Entity> l = createEntityListFromQuery(query);

        if(l.size() > 0)
            return l.get(0);

        throw new DataNotFoundException("Database.CategoryHandler : findById(int)");
    }

    @Override
    public Entity getLast(TypeEnum type) throws DataNotFoundException{
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";

        List<Entity> l = createEntityListFromQuery(query);

        if(l.size() > 0)
            return l.get(0);

        throw new DataNotFoundException("Database.CategoryHandler : getLast(TypeEnum)");
    }

    @Override
    public List<Entity> getAll(TypeEnum type) {
        String query = "SELECT * FROM " + TABLE_NAME;

        return createEntityListFromQuery(query);
    }

    @Override
    public List<Entity> getFromTo(TypeEnum type, int start, int end) {
        String query = "SELECT * FROM " + TABLE_NAME + " LIMIT " + start + ", " + end;

        return createEntityListFromQuery(query);
    }

    @Override
    public List<Entity> getWhere(String where) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + where;

        return createEntityListFromQuery(query);
    }

    @Override
    public boolean isIn(Entity entity) {
        String query = "Select id FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ((Category) entity).getId();

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

        String query = "Select id FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ((Category) entity).getId();

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

    @Override
    protected Entity createEntityFromCursor(Cursor c){
        return new Category(
            c.getInt(c.getColumnIndex(COLUMN_ID)),
            c.getString(c.getColumnIndex(COLUMN_LABEL)),
            c.getString(c.getColumnIndex(COLUMN_COLOR))
        );
    }
}