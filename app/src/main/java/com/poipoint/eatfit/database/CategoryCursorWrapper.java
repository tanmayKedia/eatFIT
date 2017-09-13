package com.poipoint.eatfit.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.poipoint.eatfit.database.EatfitDBSchema.HealthCategory;

import com.poipoint.eatfit.models.Category;

/**
 * Created by Tanmay on 2/10/2016.
 */
public class CategoryCursorWrapper extends CursorWrapper {

    public CategoryCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Category getCategory()
    {
        int id=getInt(getColumnIndex(HealthCategory.Cols.ID));
        int order_id=getInt(getColumnIndex(HealthCategory.Cols.ID_ORDER));
        String name=getString(getColumnIndex(HealthCategory.Cols.NAME));
        String icon_url=getString(getColumnIndex(HealthCategory.Cols.ICON));
        return new Category(id,order_id,name,icon_url);
    }
}
