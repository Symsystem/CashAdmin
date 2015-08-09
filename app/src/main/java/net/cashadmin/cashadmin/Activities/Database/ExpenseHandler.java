package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Model.DBEntity;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Transaction;

import java.util.Date;

public class ExpenseHandler extends TransactionHandler {

    private static final String TABLE_EXPENSES = "expenses";

    public ExpenseHandler(DBHandler handler) {
        super(handler, TABLE_EXPENSES);
    }

    @Override
    public DBEntity findById(int id) {
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
        return null;
    }
}
