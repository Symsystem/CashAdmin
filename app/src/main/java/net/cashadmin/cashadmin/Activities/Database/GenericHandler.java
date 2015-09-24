package net.cashadmin.cashadmin.Activities.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Exception.InvalidQueryException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.util.ArrayList;
import java.util.Date;
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
     * @param entity
     */
    public abstract boolean insert(Entity entity);

    /**
     * @param entity
     */
    public abstract boolean update(Entity entity);

    /**
     * @param id
     */
    public abstract Entity findById(int id) throws DataNotFoundException;

    /**
     * @param type
     */
    public abstract Entity getLast(TypeEnum type) throws DataNotFoundException;

    /**
     * @param type
     */
    public abstract List<Entity> getAll(TypeEnum type) throws DataNotFoundException;

    /**
     * @param type,start,end
     */
    public abstract List<Entity> getFromTo(TypeEnum type, int start, int end) throws DataNotFoundException;

    /**
     * @param type,startDate,endDate
     */
    public List<Entity> getByDate(TypeEnum type, Date startDate, Date endDate) throws DataNotFoundException{
        return null;
    }

    /**
     * @param entity
     */
    public abstract boolean isIn(Entity entity);

    /**
     * @param entity
     */
    public abstract boolean delete(Entity entity);

    /**
     * @param type,condition
     */
    public boolean deleteBy(TypeEnum type, String condition) throws InvalidQueryException{
        return true;
    }

    /**
     * @param query
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
     * @param c
     * @return Entity
     */
    protected abstract Entity createEntityFromCursor(Cursor c);
}
