package net.cashadmin.cashadmin.Activities.Database;

import android.database.Cursor;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;

import java.util.Date;
import java.util.List;

public class ExpenseHandler extends TransactionHandler {

    private static final String TABLE_EXPENSES = "expenses";

    public ExpenseHandler(DBHandler handler) {
        super(handler, TABLE_EXPENSES);
    }

    @Override
    public Entity findById(int id) throws DataNotFoundException {
        String query = "SELECT id FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_ID + " = " + id;

        List<Entity> l = createEntityListFromQuery(query);

        if(l.size() > 0)
            return l.get(0);

        throw new DataNotFoundException("Database.ExpenseHandler : findById(int)");
    }

    @Override
    public Entity getLast(TypeEnum type) throws DataNotFoundException {
        String query = "SELECT * FROM " + TABLE_EXPENSES + " ORDER BY " + COLUMN_DATE + " DESC LIMIT 1";

        List<Entity> l = createEntityListFromQuery(query);

        if(l.size() > 0)
            return l.get(0);

        throw new DataNotFoundException("Database.ExpenseHandler : getLast(TypeEnum)");
    }

    @Override
    public List<Entity> getAll(TypeEnum type) {
        String query = "SELECT * FROM " + TABLE_EXPENSES;

        return createEntityListFromQuery(query);
    }

    @Override
    public List<Entity> getFromTo(TypeEnum type, int start, int end) {
        String query = "SELECT * FROM " + TABLE_EXPENSES + " LIMIT " + start + "," + end;

        return createEntityListFromQuery(query);
    }

    @Override
    public List<Entity> getByDate(TypeEnum type, Date startDate, Date endDate) {
        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_DATE + " BETWEEN " + startDate + " and " + endDate;

        return createEntityListFromQuery(query);
    }

    @Override
    protected Entity createEntityFromCursor(Cursor c){
        return new Expense(
            c.getInt(c.getColumnIndex(COLUMN_ID)),
            c.getFloat(c.getColumnIndex(COLUMN_TOTAL)),
            new Date(c.getLong(c.getColumnIndex(COLUMN_DATE)) * 1000),
            c.getInt(c.getColumnIndex(COLUMN_CATEGORY))
        );
    }
}
