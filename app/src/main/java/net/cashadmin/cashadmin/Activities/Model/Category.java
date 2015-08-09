package net.cashadmin.cashadmin.Activities.Model;

import android.graphics.Color;

import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

public class Category extends Entity {

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

    /**
     * @param id
     * @param label
     * @param color - Android's color format
     */
    public Category(int id, String label, int color) {
        this.id = id;
        this.label = label;
        this.color = color;
        this.mType = TypeEnum.CATEGORY;
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
    }

    /**
     * @return int
     */
    public int getId() {
        return id;
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
