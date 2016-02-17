package net.cashadmin.cashadmin.Activities.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Frequency;
import net.cashadmin.cashadmin.Activities.Model.Income;
import net.cashadmin.cashadmin.Activities.Utils.Counter;
import net.cashadmin.cashadmin.R;

import java.util.HashMap;


public class DBHandler extends SQLiteOpenHelper {

    HashMap<String, String> handlers = new HashMap<>();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashAdminDB.db";

    private Context mContext;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
        handlers.put("category", Category.getTableCreator());
        handlers.put("income", Income.getTableCreator());
        handlers.put("expense", Expense.getTableCreator());
        handlers.put("frequency", Frequency.getTableCreator());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String key : handlers.keySet()) {
            db.execSQL(handlers.get(key));
        }
        ContentValues values = new ContentValues();
        values.put(Category.COLUMN_LABEL, mContext.getString(R.string.others));
        values.put(Category.COLUMN_COLOR, "#c4c4c4");
        db.insert(Category.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);
    }


    public HashMap<String, Counter> getAutoIncrementNumbers() {
        String query = "SELECT * FROM SQLITE_SEQUENCE";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        HashMap<String, Counter> autoIncList = new HashMap<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                switch (cursor.getString(cursor.getColumnIndex("name"))) {
                    case Category.TABLE_NAME:
                        autoIncList.put(Category.class.getName(), new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq"))) + 1));
                        break;
                    case Expense.TABLE_NAME:
                        autoIncList.put(Expense.class.getName(), new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq"))) + 1));
                        break;
                    case Income.TABLE_NAME:
                        autoIncList.put(Income.class.getName(), new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq"))) + 1));
                        break;
                    case Frequency.TABLE_NAME:
                        autoIncList.put(Frequency.class.getName(), new Counter(Integer.valueOf(cursor.getString(cursor.getColumnIndex("seq"))) +1 ));
                        break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();


        return autoIncList;
    }
}
