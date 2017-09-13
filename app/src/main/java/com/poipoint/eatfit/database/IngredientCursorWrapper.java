package com.poipoint.eatfit.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.poipoint.eatfit.database.EatfitDBSchema.HealthIngredients;
import com.poipoint.eatfit.models.Ingredient;

/**
 * Created by Tanmay on 2/13/2016.
 */
public class IngredientCursorWrapper extends CursorWrapper {

    public IngredientCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Ingredient getIngredient()
    {
        int id=getInt(getColumnIndex(HealthIngredients.Cols.ID));
        String name1=getString(getColumnIndex(HealthIngredients.Cols.NAME1));
        String name2=getString(getColumnIndex(HealthIngredients.Cols.NAME2));
        String name3=getString(getColumnIndex(HealthIngredients.Cols.NAME3));
        String symbol=getString(getColumnIndex(HealthIngredients.Cols.SYMBOL));
        String description=getString(getColumnIndex(HealthIngredients.Cols.DESCRIPTION));
        int danger_level=getInt(getColumnIndex(HealthIngredients.Cols.DANGER_LEVEL));

        return new Ingredient(id,name1,name2,name3,symbol,description,danger_level);
    }
}
