package net.cashadmin.cashadmin.Activities.Database;

import android.content.Context;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.util.Date;
import java.util.List;

public class DataManager {

    private CategoryHandler mCategoryHandler;
    private IncomeHandler mIncomeHandler;
    private ExpenseHandler mExpenseHandler;

    public DataManager(Context context) {
        DBHandler db = new DBHandler(context, null, null, 0);
        mCategoryHandler = new CategoryHandler(db);
        mIncomeHandler = new IncomeHandler(db);
        mExpenseHandler = new ExpenseHandler(db);
    }

    public boolean insert(Entity entity) {
        switch (entity.getType()) {
            case CATEGORY:
                return mCategoryHandler.insert(entity);
            case EXPENSE:
                return mExpenseHandler.insert(entity);
            case INCOME:
                return mIncomeHandler.insert(entity);
            default:
                return false;
        }
    }

    public boolean update(Entity entity) {
        switch (entity.getType()) {
            case CATEGORY:
                return mCategoryHandler.update(entity);
            case EXPENSE:
                return mExpenseHandler.update(entity);
            case INCOME:
                return mIncomeHandler.update(entity);
            default:
                return false;
        }
    }

    public Entity get(TypeEnum type, int id) throws DataNotFoundException {
        switch (type) {
            case CATEGORY:
                return mCategoryHandler.findById(id);
            case EXPENSE:
                return mExpenseHandler.findById(id);
            case INCOME:
                return mIncomeHandler.findById(id);
            default:
                throw new DataNotFoundException("Database.DataManager : get(TypeEnum, int)");
        }
    }

    public List<Entity> getAll(TypeEnum type) throws DataNotFoundException {
        switch (type) {
            case CATEGORY:
                return mCategoryHandler.getAll(type);
            case EXPENSE:
                return mExpenseHandler.getAll(type);
            case INCOME:
                return mIncomeHandler.getAll(type);
            default:
                throw new DataNotFoundException("Database.DataManager : getAll(TypeEnum)");
        }

    }

    public List<Entity> getFromTo(TypeEnum type, int start, int end) {

    }

    public List<Entity> getByDate(TypeEnum type, Date startDate, Date endDate) throws IllegalTypeException {

    }

    public boolean isIn(Entity entity) {
        switch(entity.getType()){
            case CATEGORY:
                return mCategoryHandler.isIn(entity);
            case EXPENSE:
                return mExpenseHandler.isIn(entity);
            case INCOME:
                return mIncomeHandler.isIn(entity);
            default:
                return false;
        }
    }

    public boolean delete(Entity entity) {
        switch(entity.getType()){
            case CATEGORY:
                return mCategoryHandler.delete(entity);
            case EXPENSE:
                return mExpenseHandler.delete(entity);
            case INCOME:
                return mIncomeHandler.delete(entity);
            default:
                return false;
        }

    }

    public boolean deleteBy(TypeEnum type, String condition) {

    }

}
