package net.cashadmin.cashadmin.Activities.Model;

import android.content.ContentValues;
import android.database.Cursor;

import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

public class Setting extends Entity {

    public static final String TABLE_NAME = "settings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_KEY = "keys";
    public static final String COLUMN_VALUE = "val";

    public static final TypeEnum TYPE = TypeEnum.SETTING;

    public static String getTableCreator() {
        return "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_KEY + " VARCHAR(128) UNIQUE, " +
                COLUMN_VALUE + " VARCHAR(128))";
    }

    /**
     * @var int
     */
    private int id;

    /**
     * @var String
     */
    private String key;

    /**
     * @var String
     */
    private String value;

    public Setting() {
        COLUMNS = new String[]{COLUMN_ID, COLUMN_KEY, COLUMN_VALUE};
        COLUMN_ID_INDEX = 0;
    }

    /**
     * @param key
     * @param value
     */
    public Setting(int id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
        COLUMNS = new String[]{COLUMN_ID, COLUMN_KEY, COLUMN_VALUE};
        COLUMN_ID_INDEX = 0;
    }

    /**
     * @return String
     */
    public String getKey() {
        return key;
    }

    /**
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public TypeEnum getType() {
        return TYPE;
    }

    @Override
    public String getColumnId() {
        return COLUMNS[COLUMN_ID_INDEX];
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_KEY, key);
        values.put(COLUMN_VALUE, value);
        return values;
    }

    @Override
    protected String getOrderByCondition() {
        return "";
    }

    @Override
    protected Entity createEntityFromCursor(Cursor c) {
        return new Setting(
                c.getInt(c.getColumnIndex(COLUMN_ID)),
                c.getString(c.getColumnIndex(COLUMN_KEY)),
                c.getString(c.getColumnIndex(COLUMN_VALUE))
        );
    }
}
