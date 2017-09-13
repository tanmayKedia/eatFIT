package com.poipoint.eatfit.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.poipoint.eatfit.models.SearchObject;

/**
 * Created by Tanmay on 2/20/2016.
 */
public class SearchObjectCursorWrapper extends CursorWrapper {

    public SearchObjectCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public SearchObject getSearchObject()
    {
        String name=getString(getColumnIndex(EatfitDBSchema.HealthSearch.Cols.NAME));
        int type=getInt(getColumnIndex(EatfitDBSchema.HealthSearch.Cols.TYPE));
        int tableid=getInt(getColumnIndex(EatfitDBSchema.HealthSearch.Cols.TABLEID));
        return new SearchObject(name,type,tableid);
    }
}
