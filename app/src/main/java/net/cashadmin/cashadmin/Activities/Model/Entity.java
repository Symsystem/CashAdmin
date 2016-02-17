package net.cashadmin.cashadmin.Activities.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity implements Serializable {

    protected String[] COLUMNS;
    protected int COLUMN_ID_INDEX;

    public abstract String getTableName();
    public abstract String[] getColumns();
    public abstract TypeEnum getType();
    public abstract String getColumnId();

    public void insert(SQLiteOpenHelper db) {
        SQLiteDatabase database = db.getWritableDatabase();
        database.insert(getTableName(), null, getValues());
        database.close();
    }

    public void update(SQLiteOpenHelper db) {
        SQLiteDatabase database = db.getWritableDatabase();
        database.update(getTableName(), getValues(), getColumnId() + " = ?", new String[]{String.valueOf(getId())});
        database.close();
    }

    public void delete(SQLiteOpenHelper db) {
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(getTableName(), getColumnId() + " = ?", new String[]{String.valueOf(getId())});
        database.close();
    }

    public abstract int getId();

    public abstract ContentValues getValues();

    public List<Entity> get(SQLiteOpenHelper db, String whereCondition, String footRequest) {
        String query = "SELECT * FROM " + getTableName();
        if (!whereCondition.equals("")) {
            query += " WHERE " + whereCondition;
        }
        query += getOrderByCondition();
        if (!footRequest.equals("")) {
            query += footRequest;
        }
        query += ";";

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

    protected abstract String getOrderByCondition();

    protected abstract Entity createEntityFromCursor(Cursor c);

}
