package net.cashadmin.cashadmin.Activities.Database;

import net.cashadmin.cashadmin.Activities.Model.DBEntity;

public abstract class GenericHandler {

    protected String tableCreator = "";

    public String getTableCreator() {
        return tableCreator;
    }

    protected void setTableCreator(String s) {
        tableCreator = s;
    }

    /**
     * @param entity
     */
    public abstract boolean insert(DBEntity entity);

    /**
     * @param entity
     */
    public abstract boolean update(DBEntity entity);

    /**
     * @param id
     */
    public abstract DBEntity findById(int id);

    /**
     * @param entity
     */
    public abstract boolean isIn(DBEntity entity);

    /**
     * @param entity
     */
    public abstract boolean delete(DBEntity entity);
}
