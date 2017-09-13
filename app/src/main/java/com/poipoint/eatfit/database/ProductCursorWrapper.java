package com.poipoint.eatfit.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.poipoint.eatfit.models.Product;
import com.poipoint.eatfit.database.EatfitDBSchema.HealthProduct;

/**
 * Created by Tanmay on 2/12/2016.
 */
public class ProductCursorWrapper extends CursorWrapper {

    public ProductCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Product getProduct()
    {
        int id=getInt(getColumnIndex(EatfitDBSchema.HealthProduct.Cols.ID));
        int category_id=getInt(getColumnIndex(EatfitDBSchema.HealthProduct.Cols.CATEGORY_ID));
        int subcategoryId=getInt(getColumnIndex(HealthProduct.Cols.SUBCATEGORY_ID));
        String name=getString(getColumnIndex(EatfitDBSchema.HealthProduct.Cols.NAME));
        String company_name=getString(getColumnIndex(HealthProduct.Cols.COMPANY));
        String ingredients=getString(getColumnIndex(HealthProduct.Cols.INGREDIENTS));
        return new Product(id,category_id,subcategoryId,name,company_name,ingredients);
    }
}
