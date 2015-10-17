package net.cashadmin.cashadmin.Activities.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Utils.Counter;

import java.util.HashMap;


public class DBHandler extends SQLiteOpenHelper {

    HashMap<String, GenericHandler> handlers = new HashMap<>();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashAdminDB.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        CategoryHandler catHandler = new CategoryHandler(this);
        handlers.put("category", catHandler);
        handlers.put("income", new IncomeHandler(this));
        handlers.put("expense", new ExpenseHandler(this, catHandler));
        handlers.put("frequencies", new FrequencyHandler(this, catHandler));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        for (String key : handlers.keySet()) {
            db.execSQL(handlers.get(key).getTableCreator());
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

    public HashMap<TypeEnum, Counter> getAutoIncrementNumbers() {
        String query = "SELECT * FROM SQLITE_SEQUENCE";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        HashMap<TypeEnum, Counter> autoIncList = new HashMap<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                switch (cursor.getString(cursor.getColumnIndex("name"))) {
                    case CategoryHandler.TABLE_NAME:
                        autoIncList.put(TypeEnum.CATEGORY, new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq"))) + 1));
                        break;
                    case ExpenseHandler.TABLE_NAME:
                        autoIncList.put(TypeEnum.EXPENSE, new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq"))) + 1));
                        break;
                    case IncomeHandler.TABLE_NAME:
                        autoIncList.put(TypeEnum.INCOME, new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq"))) + 1));
                        break;
                    case FrequencyHandler.TABLE_NAME:
                        autoIncList.put(TypeEnum.FREQUENCY, new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq"))) +1 ));
                        break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();


        return autoIncList;
    }
}
