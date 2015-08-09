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

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        handlers.put("category", new CategoryHandler(this));
        handlers.put("income", new IncomeHandler(this));
        handlers.put("expense", new ExpenseHandler(this));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        for (Iterator it = handlers.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            db.execSQL(((GenericHandler) handlers.get(key)).getTableCreator());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);
    }


    public GenericHandler getHandler(String key) {
        return handlers.get(key);
    }

}
