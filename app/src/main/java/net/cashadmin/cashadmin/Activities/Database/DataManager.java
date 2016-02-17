package net.cashadmin.cashadmin.Activities.Database;

import android.content.Context;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Transaction;
import net.cashadmin.cashadmin.Activities.Utils.Counter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DataManager {

    private static DataManager dataManager = null;
    private HashMap<TypeEnum, Counter> mAutoIncrementNumbers;
    private DBHandler db;

    private DataManager(Context context) {
        db = new DBHandler(context);
        this.mAutoIncrementNumbers = db.getAutoIncrementNumbers();
    }

    public static DataManager getDataManager(Context context) {
        if (dataManager == null) {
            dataManager = new DataManager(context);
        }
        return dataManager;
    }

    public static DataManager getDataManager() {
        if (dataManager == null) {
            return null;
        }
        return dataManager;
    }

    public int getNextId(TypeEnum type) {
        if (mAutoIncrementNumbers.get(type) != null)
            return mAutoIncrementNumbers.get(type).getCounterInt();
        else
            return 1;
    }

    public void insert(Entity data) {
        data.insert(db);
    }

    public void update(Entity data) {
        data.update(db);
    }

    public void delete(Entity data) {
        data.delete(db);
    }

    public Entity getById(Class<? extends Entity> stockClass, int id) throws DataNotFoundException {
        try {
            Entity s = stockClass.newInstance();
            return s.get(db, s.getColumnId() + " = " + id, "").get(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new DataNotFoundException("Data not found with this id ! DataManager : getById()");
    }

    public Entity getLast(Class<? extends Entity> stockClass) {
        try {
            Entity s = stockClass.newInstance();
            return s.get(db, "", " LIMIT 1").get(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Entity> getAll(Class<? extends Entity> stockClass) {
        List<Entity> result = new ArrayList<>();
        try {
            Entity s = stockClass.newInstance();
            result = s.get(db, "", "");
       } catch (InstantiationException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Entity> getFromTo(Class<? extends Entity> stockClass, int from, int to) {
        List<Entity> result = new ArrayList<>();
        try {
            Entity s = stockClass.newInstance();
            result = s.get(db, "", " LIMIT " + from + ", " + to);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Entity> getByDate(Class<? extends Transaction> trans, Date startDate, Date endDate) {
        List<Entity> result = new ArrayList<>();
        try {
            Transaction t = trans.newInstance();
            result = t.getByDate(db, startDate, endDate);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Entity> getWhere(Class<? extends Entity> stockClass, String whereCondition) {
        List<Entity> result = new ArrayList<>();
        try {
            Entity s = stockClass.newInstance();
            result = s.get(db, whereCondition, "");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

}
