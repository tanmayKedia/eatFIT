package com.poipoint.eatfit.models;

import com.poipoint.eatfit.database.IngredientCursorWrapper;

/**
 * Created by Tanmay on 2/13/2016.
 */
public class Ingredient implements Comparable<Ingredient> {

    private int id;
    private String name1;
    private String name2;
    private String name3;
    private String symbol;
    private String description;
    private int danger_level;

    public Ingredient(int id, String name1, String name2, String name3, String symbol, String description, int danger_level) {
        this.id = id;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.symbol = symbol;
        this.description = description;
        this.danger_level = danger_level;
    }

    public int getId() {
        return id;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getName3() {
        return name3;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }

    public int getDanger_level() {
        return danger_level;
    }

    @Override
    public int compareTo(Ingredient otherObject)
    {
        return this.name1.compareTo(otherObject.name1);
    }
}
