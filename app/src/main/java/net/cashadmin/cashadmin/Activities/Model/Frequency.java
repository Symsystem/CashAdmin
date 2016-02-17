package net.cashadmin.cashadmin.Activities.Model;

import android.content.ContentValues;
import android.database.Cursor;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Frequency extends Entity {

    public static final String TABLE_NAME = "frequencies";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE_TRANSACTION = "typeTransaction";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_DATE_FREQUENCY = "dateFrequency";
    public static final String COLUMN_END_DATE_FREQUENCY = "endDateFrequency";
    public static final String COLUMN_CATEGORY = "category";

    public static final TypeEnum TYPE = TypeEnum.FREQUENCY;

    public static String getTableCreator() {
        return "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE_TRANSACTION + " VARCHAR(16) NOT NULL CHECK(" + COLUMN_TYPE_TRANSACTION + " in " +
                "('" + TypeEnum.values()[1].toString() +
                "','" + TypeEnum.values()[2].toString() + "')), " +
                COLUMN_TOTAL + " INTEGER NOT NULL, " +
                COLUMN_LABEL + " VARCHAR(127), " +
                COLUMN_FREQUENCY + " VARCHAR(16) NOT NULL CHECK(" + COLUMN_FREQUENCY + " in " +
                "('" + FrequencyEnum.values()[1].toString() +
                "','" + FrequencyEnum.values()[2].toString() +
                "','" + FrequencyEnum.values()[3].toString() +
                "','" + FrequencyEnum.values()[4].toString() + "')), " +
                COLUMN_DATE_FREQUENCY + " FLOAT NOT NULL, " +
                COLUMN_END_DATE_FREQUENCY + " FLOAT, " +
                COLUMN_CATEGORY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY + ") REFERENCES " + Category.TABLE_NAME + ")";
    }

    /**
     * @var int
     */
    private int id;

    /**
     * @var TypeEnum
     */
    private TypeEnum transactionType;

    /**
     * @var float
     */
    protected float total;

    /**
     * @var String
     */
    protected String label;

    /**
     * @var FrequencyEnum
     */
    protected FrequencyEnum frequency;

    /**
     * @var Date
     */
    protected Date dateFrequency;

    /**
     * @var Date
     */
    protected Date endDateFrequency;

    /**
     * @var Category
     */
    private Category category;

    public Frequency() {
        COLUMNS = new String[]{COLUMN_ID, COLUMN_TYPE_TRANSACTION, COLUMN_TOTAL, COLUMN_LABEL,
                COLUMN_FREQUENCY, COLUMN_DATE_FREQUENCY, COLUMN_END_DATE_FREQUENCY, COLUMN_CATEGORY};
        COLUMN_ID_INDEX = 0;
    }

    /**
     * @param id
     * @param type
     * @param total
     * @param label
     * @param frequency
     * @param dateFrequency
     * @param endDateFrequency
     * @param category
     */
    public Frequency(int id, TypeEnum type, float total, String label, FrequencyEnum frequency, Date dateFrequency, Date endDateFrequency, Category category){
        this.id = id;
        this.total = total;
        this.label = label;
        this.transactionType = type;
        this.frequency = frequency;
        this.dateFrequency = dateFrequency;
        this.endDateFrequency = endDateFrequency;
        this.category = category;
        COLUMNS = new String[]{COLUMN_ID, COLUMN_TYPE_TRANSACTION, COLUMN_TOTAL, COLUMN_LABEL,
            COLUMN_FREQUENCY, COLUMN_DATE_FREQUENCY, COLUMN_END_DATE_FREQUENCY, COLUMN_CATEGORY};
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
        values.put(COLUMN_TYPE_TRANSACTION, transactionType.toString());
        values.put(COLUMN_TOTAL, total);
        values.put(COLUMN_LABEL, label);
        values.put(COLUMN_FREQUENCY, frequency.toString());
        values.put(COLUMN_DATE_FREQUENCY, getStringSQLDate());
        values.put(COLUMN_END_DATE_FREQUENCY, getStringSQLEndDate());
        if (transactionType.equals(TypeEnum.values()[2].toString())){
            values.put(COLUMN_CATEGORY, category.getId());
        }
        return values;
    }

    @Override
    protected String getOrderByCondition() {
        return  " ORDER BY " + COLUMN_DATE_FREQUENCY + " DESC";
    }

    @Override
    protected Entity createEntityFromCursor(Cursor c) {
        try {
            return new Frequency(
                    c.getInt(c.getColumnIndex(COLUMN_ID)),
                    TypeEnum.valueOf(c.getString(c.getColumnIndex(COLUMN_TYPE_TRANSACTION))),
                    c.getFloat(c.getColumnIndex(COLUMN_TOTAL)),
                    c.getString(c.getColumnIndex(COLUMN_LABEL)),
                    FrequencyEnum.valueOf(c.getString(c.getColumnIndex(COLUMN_FREQUENCY))),
                    new java.util.Date(c.getLong(c.getColumnIndex(COLUMN_DATE_FREQUENCY))),
                    new java.util.Date(c.getLong(c.getColumnIndex(COLUMN_END_DATE_FREQUENCY))),
                    (Category) DataManager.getDataManager().getById(Category.class, c.getInt(c.getColumnIndex(COLUMN_CATEGORY)))
            );
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return TypeEnum
     */
    public TypeEnum getTransactionType(){
        return transactionType;
    }

    /**
     * @return float
     */
    public float getTotal() {
        return total;
    }

    /**
     * @param total
     */
    public void setTotal(float total){
        this.total = total;
    }

    /**
     * @return String
     */
    public String getLabel(){
        return label;
    }

    /**
     * @param label
     */
    public void setLabel(String label){
        this.label = label;
    }

    /**
     * @return FrenquencyEnum
     */
    public FrequencyEnum getFrequency(){
        return frequency;
    }

    /**
     * @parem frenquency
     */
    public void setFrenquency(FrequencyEnum frenquency){
        this.frequency = frenquency;
    }


    /**
     * @return Date
     */
    public Date getDateFrequency(){
        return dateFrequency;
    }

    /**
     * @param dateFrequency
     */
    public void setDateFrequency(Date dateFrequency){
        this.dateFrequency = dateFrequency;
    }

    /**
     * @return Date
     */
    public Date getEndDateFrequency(){
        return endDateFrequency;
    }

    /**
     * @param endDateFrequency
     */
    public void setEndDateFrequency(Date endDateFrequency){
        this.endDateFrequency = endDateFrequency;
    }

    /**
     * @return Category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return String
     */
    public String getStringDate() {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return formater.format(dateFrequency);
    }

    /**
     * @return String
     */
    public String getStringSQLDate(){
        return String.valueOf(dateFrequency.getTime());
    }

    /**
     * @return String
     */
    public String getStringSQLEndDate(){
        return String.valueOf(endDateFrequency.getTime());
    }

    /**
     * @return java.sql.Date
     */
    public java.sql.Date getSQLDate(){
        return new java.sql.Date(this.dateFrequency.getTime());
    }
}
