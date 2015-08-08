package net.cashadmin.cashadmin.Activities.Database;

import net.cashadmin.cashadmin.Activities.Model.DBEntity;

public class IncomeHandler extends GenericHandler {
    public IncomeHandler(DBHandler handler) {

    }

    @Override
    public boolean insert(DBEntity entity) {
        return false;
    }

    @Override
    public boolean update(DBEntity entity) {
        return false;
    }

    @Override
    public DBEntity findById(int id) {
        return null;
    }

    @Override
    public boolean isIn(DBEntity entity) {
        return false;
    }

    @Override
    public boolean delete(DBEntity entity) {
        return false;
    }
}
