package net.cashadmin.cashadmin.Activities.Database;

import android.database.sqlite.SQLiteOpenHelper;


public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashAdminDB.db";

    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LABEL = "label";
    private static final String COLUMN_COLOR = "color";

    private static final String TABLE_INCOMES = "incomes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TOTAL = "total";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY = "category";

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TOTAL = "total";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY = "category";




}
