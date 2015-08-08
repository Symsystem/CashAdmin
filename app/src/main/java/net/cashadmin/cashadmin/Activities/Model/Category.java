package net.cashadmin.cashadmin.Activities.Model;

import android.graphics.Color;

public class Category {

    /**
     * @var int
     */
    private int id;

    /**
     * @var String
     */
    private String label;

    /**
     * @var Color
     */
    private Color color;

    /**
     * @param id
     * @param label
     * @param color
     */
    public Category(int id, String label, Color color){
        this.id = id;
        this.label = label;
        this.color = color;
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
    public Color getColor() {
        return color;
    }

    /**
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
