package net.cashadmin.cashadmin.Activities.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Income;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IncomeHandler extends TransactionHandler {

    private static final String TABLE_INCOMES = "incomes";

    public IncomeHandler(DBHandler handler) {
        super(handler, TABLE_INCOMES);
    }

    @Override
    public Entity findById(int id) throws DataNotFoundException {
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
        throw new DataNotFoundException("Database.IncomeHandler : findById(int)");
    }

    @Override
    public List<Entity> getAll(TypeEnum type) {
        String query = "SELECT * FROM " + TABLE_INCOMES;

        SQLiteDatabase db = mDBHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Entity> list = new ArrayList<>();

        while (!(cursor.isAfterLast())) {
            Income income = new Income(
                    Integer.parseInt(cursor.getString(0)),
                    Float.parseFloat(cursor.getString(1)),
                    new Date(Long.parseLong(cursor.getString(2)) * 1000),
                    Integer.parseInt(cursor.getString(3))
            );
            list.add(income);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }
}
