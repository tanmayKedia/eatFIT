package com.poipoint.eatfit.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.poipoint.eatfit.models.Company;

/**
 * Created by Tanmay on 2/12/2016.
 */
public class CompanyCursorWrapper extends CursorWrapper{

    public CompanyCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Company getCompany()
    {
        int id=getInt(getColumnIndex(EatfitDBSchema.HealthFirmy.Cols.ID));
        String company_name=getString(getColumnIndex(EatfitDBSchema.HealthFirmy.Cols.COMPANY));

        return new Company(id,company_name);
    }
}
