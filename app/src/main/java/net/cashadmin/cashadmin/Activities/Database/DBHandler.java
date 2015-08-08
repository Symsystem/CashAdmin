package net.cashadmin.cashadmin.Activities.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Iterator;


public class DBHandler extends SQLiteOpenHelper {

    HashMap<String, GenericHandler> handlers = new HashMap<>();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashAdminDB.db";

    private static final String TABLE_INCOMES = "incomes";
    private static final String COLUMN_INCOMES_ID = "id";
    private static final String COLUMN_INCOMES_TOTAL = "total";
    private static final String COLUMN_INCOMES_DATE = "date";
    private static final String COLUMN_INCOMES_CATEGORY = "category";

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_EXPENSES_ID = "id";
    private static final String COLUMN_EXPENSES_TOTAL = "total";
    private static final String COLUMN_EXPENSES_DATE = "date";
    private static final String COLUMN_EXPENSES_CATEGORY = "category";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        handlers.put("category", new CategoryHandler(this));

        // TODO : create request to create tables (see beneath)
        //handlers.put("income", new IncomeHandler(this));
        //handlers.put("expense", new ExpenseHandler(this));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        for (Iterator it = handlers.keySet().iterator(); it.hasNext();) {
            String key = (String)it.next();
            db.execSQL(((GenericHandler) handlers.get(key)).getTableCreator());
        }

//        db.execSQL(handlers.get("category").getTableCreator());
//        db.execSQL("CREATE TABLE " +
//                TABLE_INCOMES + "(" +
//                COLUMN_INCOMES_ID + " INTEGER PRIMARY KEY, " +
//                COLUMN_INCOMES_TOTAL + "INTEGER NOT NULL, " +
//                COLUMN_INCOMES_DATE + "DATETIME NOT NULL, " +
//                COLUMN_INCOMES_CATEGORY + "INTEGER NOT NULL REFERENCES " + TABLE_CATEGORIES + ")");
//        db.execSQL("CREATE TABLE " +
//                TABLE_EXPENSES + "(" +
//                COLUMN_EXPENSES_ID + " INTEGER PRIMARY KEY, " +
//                COLUMN_EXPENSES_TOTAL + "INTEGER NOT NULL, " +
//                COLUMN_EXPENSES_DATE + "DATETIME NOT NULL, " +
//                COLUMN_EXPENSES_CATEGORY + "INTEGER NOT NULL REFERENCES " + TABLE_CATEGORIES + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);
    }


    public GenericHandler getHandler(String key){
        return handlers.get(key);
    }

}
