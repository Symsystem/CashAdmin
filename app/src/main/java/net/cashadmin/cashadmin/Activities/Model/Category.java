package net.cashadmin.cashadmin.Activities.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;

import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

public class Category extends Entity {

    public static final String TABLE_NAME = "categories";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_COLOR = "color";

    public static final TypeEnum TYPE = TypeEnum.CATEGORY;

    public static String getTableCreator() {
        return "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LABEL + " VARCHAR(255) NOT NULL, " +
                COLUMN_COLOR + " VARCHAR(11) NOT NULL)";
    }

    /**
     * @var int
     */
    private int id;

    /**
     * @var String
     */
    private String label;

    /**
     * @var int
     */
    private int color;

    public Category() {
        COLUMNS = new String[]{COLUMN_ID, COLUMN_LABEL, COLUMN_COLOR};
        COLUMN_ID_INDEX = 0;
    }

    /**
     * @param id
     * @param label
     * @param color - Android's color format
     */
    public Category(int id, String label, int color) {
        this.id = id;
        this.label = label;
        this.color = color;
        COLUMNS = new String[]{COLUMN_ID, COLUMN_LABEL, COLUMN_COLOR};
        COLUMN_ID_INDEX = 0;
    }

    /**
     * @param id
     * @param label
     * @param color - RGB format e.g. #FFFF00
     */
    public Category(int id, String label, String color) {
        this.id = id;
        this.label = label;
        this.color = Color.parseColor(color);
        COLUMNS = new String[]{COLUMN_ID, COLUMN_LABEL, COLUMN_COLOR};
        COLUMN_ID_INDEX = 0;
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

    /**
     * @return int
     */
    public int getId() {
        return id;
    }

    @Override
    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_LABEL, label);
        values.put(COLUMN_COLOR, getRGBColor());
        return values;
    }

    @Override
    protected String getOrderByCondition() {
        return " ORDER BY " + COLUMN_ID;
    }

    @Override
    protected Entity createEntityFromCursor(Cursor c) {
        return new Category(
                c.getInt(c.getColumnIndex(COLUMN_ID)),
                c.getString(c.getColumnIndex(COLUMN_LABEL)),
                c.getString(c.getColumnIndex(COLUMN_COLOR))
        );
    }

    /**
     * @return String
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return Color
     */
    public int getColor() {
        return color;
    }

    /**
     * @return Color
     */
    public String getRGBColor() {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    /**
     * @param color - Android's color format
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * @param color - RGB format e.g. #FFFF00
     */
    public void setColor(String color) {
        this.color = Color.parseColor(color);
    }


}
