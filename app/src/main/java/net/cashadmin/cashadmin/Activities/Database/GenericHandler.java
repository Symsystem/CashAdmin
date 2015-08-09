package net.cashadmin.cashadmin.Activities.Database;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.util.List;

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
     * @parem type
     */
    public abstract List<Entity> getAll(TypeEnum type);

    /**
     * @param entity
     */
    public abstract boolean isIn(Entity entity);

    /**
     * @param entity
     */
    public abstract boolean delete(Entity entity);
}
