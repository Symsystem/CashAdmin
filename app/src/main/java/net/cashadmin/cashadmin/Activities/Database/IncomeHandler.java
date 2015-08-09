package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Model.DBEntity;
import net.cashadmin.cashadmin.Activities.Model.Income;
import net.cashadmin.cashadmin.Activities.Model.Transaction;

import java.util.Date;

public class IncomeHandler extends TransactionHandler {

    private static final String TABLE_INCOMES = "incomes";

    public IncomeHandler(DBHandler handler) {
        super(handler, TABLE_INCOMES);
    }

    @Override
    public DBEntity findById(int id) {
        String query = "SELECT id FROM " + TABLE_INCOMES + " WHERE " + COLUMN_ID + " = " + id;

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            Income income = new Income(
                    Integer.parseInt(cursor.getString(0)),
                    Float.parseFloat(cursor.getString(1)),
                    new Date(Long.parseLong(cursor.getString(2)) * 1000),
                    Integer.parseInt(cursor.getString(3))
            );
            cursor.close();
            db.close();
            return income;
        }
        db.close();
        return null;
    }
}
