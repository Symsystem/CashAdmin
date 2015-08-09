package net.cashadmin.cashadmin.Activities.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;

import java.util.ArrayList;
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

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            Expense expense = new Expense(
                    Integer.parseInt(cursor.getString(0)),
                    Float.parseFloat(cursor.getString(1)),
                    new Date(Long.parseLong(cursor.getString(2)) * 1000),
                    Integer.parseInt(cursor.getString(3))
            );
            cursor.close();
            db.close();
            return expense;
        }
        db.close();
        throw new DataNotFoundException("Database.ExpenseHandler : findById(int)");
    }

    @Override
    public List<Entity> getAll(TypeEnum type) {
        String query = "SELECT * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Entity> list = new ArrayList<>();

        while (!(cursor.isAfterLast())) {
            Expense expense = new Expense(
                    Integer.parseInt(cursor.getString(0)),
                    Float.parseFloat(cursor.getString(1)),
                    new Date(Long.parseLong(cursor.getString(2)) * 1000),
                    Integer.parseInt(cursor.getString(3))
            );
            list.add(expense);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }
}
