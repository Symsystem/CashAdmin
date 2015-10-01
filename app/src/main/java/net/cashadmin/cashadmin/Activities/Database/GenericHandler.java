package net.cashadmin.cashadmin.Activities.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Exception.InvalidQueryException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public abstract class GenericHandler {

    protected String tableCreator = "";
    protected DBHandler mDBHandler;

    public String getTableCreator() {
        return tableCreator;
    }

    protected void setTableCreator(String s) {
        tableCreator = s;
    }

    /**
     * @param entity Entity
     */
    public abstract boolean insert(Entity entity);

    /**
     * @param entity Entity
     */
    public abstract boolean update(Entity entity);

    /**
     * @param id int
     */
    public abstract Entity findById(int id) throws DataNotFoundException;

    /**
     * @param type TypeEnum
     */
    public abstract Entity getLast(TypeEnum type) throws DataNotFoundException;

    /**
     * @param type TypeEnum
     */
    public abstract List<Entity> getAll(TypeEnum type) throws DataNotFoundException;

    /**
     * @param type TypeEnum
     * @param start,end int
     */
    public abstract List<Entity> getFromTo(TypeEnum type, int start, int end) throws DataNotFoundException;

    /**
     * @param type TypeEnum
     * @param startDate,endDate Date
     */
    public List<Entity> getByDate(TypeEnum type, Date startDate, Date endDate) throws DataNotFoundException{
        return null;
    }

    /**
     * @param entity Entity
     */
    public abstract boolean isIn(Entity entity);

    /**
     * @param entity Entity
     */
    public abstract boolean delete(Entity entity);

    /**
     * @param type TypeEnum
     * @param condition String
     */
    public boolean deleteBy(TypeEnum type, String condition) throws InvalidQueryException{
        return true;
    }

    /**
     * @param query String
     */
    protected List<Entity> createEntityListFromQuery(String query){
        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Entity> list = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()) {
            do{
                list.add(createEntityFromCursor(cursor));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * @param c Cursor
     * @return Entity
     */
    protected abstract Entity createEntityFromCursor(Cursor c);
}
