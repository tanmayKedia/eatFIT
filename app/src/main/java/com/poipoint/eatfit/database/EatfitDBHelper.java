package com.poipoint.eatfit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.poipoint.eatfit.R;
import com.poipoint.eatfit.database.EatfitDBSchema.HealthCategory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.poipoint.eatfit.database.EatfitDBSchema.HealthSubcategory;
import com.poipoint.eatfit.database.EatfitDBSchema.HealthProduct;
import com.poipoint.eatfit.database.EatfitDBSchema.HealthFirmy;
import com.poipoint.eatfit.database.EatfitDBSchema.HealthIngredients;
/**
 * Created by Tanmay on 2/9/2016.
 */
public class EatfitDBHelper extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String DATABASE_NAME="eatfitdatabase.db";
    Context context;
    private static String TAG="voldemort";
    public EatfitDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + HealthCategory.NAME + "("
                        + HealthCategory.Cols.ID + " int(50) NOT NULL PRIMARY KEY,"
                        + HealthCategory.Cols.ID_ORDER + " int(15) NOT NULL,"
                        + HealthCategory.Cols.NAME + " varchar(100) NOT NULL,"
                        + HealthCategory.Cols.ICON + " varchar(150) NOT NULL);"
        );
        InputStream insertsStream = context.getResources().openRawResource(R.raw.cat_val);
        insertRows(db, insertsStream, HealthCategory.NAME);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + HealthSubcategory.NAME + " ("
                        + HealthSubcategory.Cols.ID + " int(50) NOT NULL PRIMARY KEY,"
                        + HealthSubcategory.Cols.CATEGORY_ID + " int(15) NOT NULL,"
                        + HealthSubcategory.Cols.NAME + " varchar(100) NOT NULL,"
                        + HealthSubcategory.Cols.ORDER_ID + " int(150) NOT NULL);"
        );
        InputStream subcategoryStream = context.getResources().openRawResource(R.raw.subcategory_insert);
        insertRows(db, subcategoryStream, HealthSubcategory.NAME);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + HealthProduct.NAME + " ("
                        + HealthProduct.Cols.ID + " int(255) NOT NULL PRIMARY KEY,"
                        + HealthProduct.Cols.CATEGORY_ID + " int(255) NOT NULL,"
                        + HealthProduct.Cols.SUBCATEGORY_ID + " int(255) NOT NULL,"
                        + HealthProduct.Cols.NAME + " varchar(100) NOT NULL,"
                        + HealthProduct.Cols.COMPANY + " varchar(100) NOT NULL,"
                        + HealthProduct.Cols.INGREDIENTS + " text NOT NULL,"
                        + HealthProduct.Cols.BARCODE + " int(1) NOT NULL);"
        );

        InputStream productStream=context.getResources().openRawResource(R.raw.prod_ins);
        insertRows(db, productStream, HealthProduct.NAME);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + HealthFirmy.NAME + " ("
                        + HealthFirmy.Cols.ID + " int(255) NOT NULL PRIMARY KEY,"
                        + HealthFirmy.Cols.COMPANY + "  varchar(100) NOT NULL);"
        );
        InputStream companyStream=context.getResources().openRawResource(R.raw.company_ins);
        insertRows(db,companyStream,HealthFirmy.NAME);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + HealthIngredients.NAME + " ("
                        + HealthIngredients.Cols.ID + " int(255) NOT NULL PRIMARY KEY,"
                        + HealthIngredients.Cols.NAME1 + " varchar(100) NOT NULL,"
                        + HealthIngredients.Cols.NAME2 + " varchar(50) NOT NULL,"
                        + HealthIngredients.Cols.NAME3 + " varchar(50) NOT NULL,"
                        + HealthIngredients.Cols.SYMBOL + " varchar(50) NOT NULL,"
                        + HealthIngredients.Cols.DESCRIPTION + " text NOT NULL,"
                        + HealthIngredients.Cols.DANGER_LEVEL + " int(1) NOT NULL);"
        );

        InputStream ingredientStream=context.getResources().openRawResource(R.raw.ingredients_ins);
        insertRows(db, ingredientStream, HealthIngredients.NAME);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + EatfitDBSchema.HealthSearch.NAME + " ("
                        + EatfitDBSchema.HealthSearch.Cols.NAME + " varchar(100) NOT NULL,"
                        + EatfitDBSchema.HealthSearch.Cols.TYPE + " int(1) NOT NULL,"
                        + EatfitDBSchema.HealthSearch.Cols.TABLEID + " int(255) NOT NULL);"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void insertRows(SQLiteDatabase db, InputStream insertsStream, String tableName)
    {
        int result = 0;
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
        try {
            while (insertReader.ready()) {
                String line=insertReader.readLine();
                String insertStmt ="INSERT INTO "+tableName+" VALUES"+line;
                db.execSQL(insertStmt);
                result++;
               /* if(tableName.equals(HealthProduct.NAME))
                {
                    String table_id="";
                    int index=0;
                    while(line.charAt(index)!=',')
                    {   Character c=line.charAt(index);
                        if(Character.isDigit(c))
                        {
                            table_id=table_id+c;
                        }
                    }

                }*/

            }
            insertReader.close();
        }
        catch (IOException ioe)
        {
            System.out.println(ioe);
        }
        Log.d(TAG, result + " rows inserted");
    }


}
