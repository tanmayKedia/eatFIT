package com.poipoint.eatfit.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.poipoint.eatfit.models.Subcategory;
import com.poipoint.eatfit.database.EatfitDBSchema.HealthSubcategory;

/**
 * Created by Tanmay on 2/11/2016.
 */
public class SubcategoryCursorWrapper extends CursorWrapper {

    public SubcategoryCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Subcategory getSubcategory()
    {
        int id=getInt(getColumnIndex(HealthSubcategory.Cols.ID));
        int categoryId=getInt(getColumnIndex(HealthSubcategory.Cols.CATEGORY_ID));
        String name=getString(getColumnIndex(HealthSubcategory.Cols.NAME));
        int orderId=getInt(getColumnIndex(HealthSubcategory.Cols.ORDER_ID));

        return new Subcategory(id,categoryId,name,orderId);
    }
}
