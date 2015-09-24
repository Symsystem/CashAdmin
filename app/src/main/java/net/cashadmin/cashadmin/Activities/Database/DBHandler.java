package net.cashadmin.cashadmin.Activities.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Utils.Counter;

import java.util.HashMap;
import java.util.Iterator;


public class DBHandler extends SQLiteOpenHelper {

    HashMap<String, GenericHandler> handlers = new HashMap<>();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashAdminDB.db";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        CategoryHandler catHandler = new CategoryHandler(this);
        handlers.put("category", catHandler);
        handlers.put("income", new IncomeHandler(this, catHandler));
        handlers.put("expense", new ExpenseHandler(this, catHandler));
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

    public HashMap<TypeEnum, Counter> getAutoIncrementNumbers() {
        String query = "SELECT * FROM SQLITE_SEQUENCE";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        HashMap<TypeEnum, Counter> autoIncList = new HashMap<>();
        if (cursor != null && cursor.moveToFirst()){
            do{
                switch(cursor.getString(cursor.getColumnIndex("name"))){
                    case "categories": autoIncList.put(TypeEnum.CATEGORY, new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq")))));
                        break;
                    case "expenses": autoIncList.put(TypeEnum.EXPENSE, new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq")))));
                        break;
                    case "incomes": autoIncList.put(TypeEnum.INCOME, new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq")))));
                        break;
                }
            }while (cursor.moveToNext());
        }

        cursor.close();

        return autoIncList;
    }
}
