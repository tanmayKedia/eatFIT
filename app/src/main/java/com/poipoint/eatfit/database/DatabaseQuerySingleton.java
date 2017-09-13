package com.poipoint.eatfit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.poipoint.eatfit.models.Category;
import com.poipoint.eatfit.models.Company;
import com.poipoint.eatfit.models.Ingredient;
import com.poipoint.eatfit.models.Product;
import com.poipoint.eatfit.models.SearchObject;
import com.poipoint.eatfit.models.Subcategory;

import java.util.List;

/**
 * Created by Tanmay on 2/10/2016.
 */
public class DatabaseQuerySingleton {
    private static DatabaseQuerySingleton singleton;
    private SQLiteDatabase eatfitDatabase;
    public static DatabaseQuerySingleton get(Context context)
    {
        if(singleton==null)
        {
            singleton=new DatabaseQuerySingleton(context);
        }
        return singleton;
    }

    private DatabaseQuerySingleton(Context context)
    {
        eatfitDatabase=new EatfitDBHelper(context).getWritableDatabase();
    }

    public CategoryCursorWrapper queryCategory(String whereClause, String[] whereArgs)
    {
        Cursor cursor=eatfitDatabase.query(EatfitDBSchema.HealthCategory.NAME,null,whereClause,whereArgs,null,null,null);
        return new CategoryCursorWrapper(cursor);
    }

    public SubcategoryCursorWrapper querySubcategory(String whereClause, String[] whereArgs)
    {
        Cursor cursor=eatfitDatabase.query(EatfitDBSchema.HealthSubcategory.NAME,null,whereClause,whereArgs,null,null,null);
        return new SubcategoryCursorWrapper(cursor);
    }

    public ProductCursorWrapper queryProduct(String whereClause,String[] whereArgs)
    {
        Cursor cursor=eatfitDatabase.query(EatfitDBSchema.HealthProduct.NAME,null,whereClause,whereArgs,null,null,null);
        return new ProductCursorWrapper(cursor);
    }

    public CompanyCursorWrapper queryCompany(String whereClause,String[] whereArgs)
    {
        Cursor cursor=eatfitDatabase.query(EatfitDBSchema.HealthFirmy.NAME,null,whereClause,whereArgs,null,null,null);
        return new CompanyCursorWrapper(cursor);
    }

    public IngredientCursorWrapper queryIngredient(String whereClause,String[] whereArgs)
    {
        Cursor cursor=eatfitDatabase.query(EatfitDBSchema.HealthIngredients.NAME,null,whereClause,whereArgs,null,null,null);
        return new IngredientCursorWrapper(cursor);
    }

    public SearchObjectCursorWrapper querySearch(String whereClause,String[] whereArgs)
    {
        Cursor cursor=eatfitDatabase.query(EatfitDBSchema.HealthSearch.NAME,null,whereClause,whereArgs,null,null,null);
        return new SearchObjectCursorWrapper(cursor);
    }

    private void insertCategory(Category category)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(EatfitDBSchema.HealthCategory.Cols.ID,category.getId());
        contentValues.put(EatfitDBSchema.HealthCategory.Cols.ID_ORDER,category.getOrder_id());
        contentValues.put(EatfitDBSchema.HealthCategory.Cols.NAME,category.getName());
        contentValues.put(EatfitDBSchema.HealthCategory.Cols.ICON,category.getIcon_url());
        eatfitDatabase.insert(EatfitDBSchema.HealthCategory.NAME,null,contentValues);
    }

    private void insertSubcategory(Subcategory subcategory)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(EatfitDBSchema.HealthSubcategory.Cols.ID,subcategory.getId());
        contentValues.put(EatfitDBSchema.HealthSubcategory.Cols.CATEGORY_ID,subcategory.getCategoryId());
        contentValues.put(EatfitDBSchema.HealthSubcategory.Cols.NAME,subcategory.getName());
        contentValues.put(EatfitDBSchema.HealthSubcategory.Cols.ORDER_ID,subcategory.getOrderId());
        eatfitDatabase.insert(EatfitDBSchema.HealthSubcategory.NAME,null,contentValues);
    }

    private void insertProduct(Product product)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(EatfitDBSchema.HealthProduct.Cols.ID,product.getId());
        contentValues.put(EatfitDBSchema.HealthProduct.Cols.CATEGORY_ID,product.getCategory_id());
        contentValues.put(EatfitDBSchema.HealthProduct.Cols.SUBCATEGORY_ID,product.getSubcategoryId());
        contentValues.put(EatfitDBSchema.HealthProduct.Cols.NAME,product.getName());
        contentValues.put(EatfitDBSchema.HealthProduct.Cols.COMPANY,product.getCompany_id());
        contentValues.put(EatfitDBSchema.HealthProduct.Cols.INGREDIENTS,product.getIngredients());
        contentValues.put(EatfitDBSchema.HealthProduct.Cols.BARCODE,0);
        eatfitDatabase.insert(EatfitDBSchema.HealthProduct.NAME,null,contentValues);
    }

    private void insertCompany(Company company)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(EatfitDBSchema.HealthFirmy.Cols.ID,company.getId());
        contentValues.put(EatfitDBSchema.HealthFirmy.Cols.COMPANY,company.getName());
        eatfitDatabase.insert(EatfitDBSchema.HealthFirmy.NAME,null,contentValues);
    }

    private void insertIngredient(Ingredient ingredient)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(EatfitDBSchema.HealthIngredients.Cols.ID,ingredient.getId());
        contentValues.put(EatfitDBSchema.HealthIngredients.Cols.NAME1,ingredient.getName1());
        contentValues.put(EatfitDBSchema.HealthIngredients.Cols.NAME2,ingredient.getName2());
        contentValues.put(EatfitDBSchema.HealthIngredients.Cols.NAME3,ingredient.getName3());
        contentValues.put(EatfitDBSchema.HealthIngredients.Cols.SYMBOL,ingredient.getSymbol());
        contentValues.put(EatfitDBSchema.HealthIngredients.Cols.DESCRIPTION,ingredient.getDescription());
        contentValues.put(EatfitDBSchema.HealthIngredients.Cols.DANGER_LEVEL,ingredient.getDanger_level());
        eatfitDatabase.insert(EatfitDBSchema.HealthIngredients.NAME,null,contentValues);
    }

    public void insertSearchObject(SearchObject searchObject)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(EatfitDBSchema.HealthSearch.Cols.NAME,searchObject.getName());
        contentValues.put(EatfitDBSchema.HealthSearch.Cols.TYPE,searchObject.getType());
        contentValues.put(EatfitDBSchema.HealthSearch.Cols.TABLEID,searchObject.getTableId());
        eatfitDatabase.insert(EatfitDBSchema.HealthSearch.NAME,null,contentValues);
    }

    public void updateCategories(List<Category> categoryList)
    {
        for(int i=0;i<categoryList.size();i++)
        {
            CategoryCursorWrapper categoryCursorWrapper=singleton.queryCategory(EatfitDBSchema.HealthCategory.Cols.ID+"= ?",new String[]{new Integer(categoryList.get(i).getId()).toString()});
            categoryCursorWrapper.moveToFirst();
            if(categoryCursorWrapper.isAfterLast())
            {
                insertCategory(categoryList.get(i));
            }
        }
    }

    public void updateSubcategory(List<Subcategory> subcategoryList)
    {
        for(int i=0;i<subcategoryList.size();i++)
        {
            SubcategoryCursorWrapper subcategoryCursorWrapper=singleton.querySubcategory(EatfitDBSchema.HealthSubcategory.Cols.ID + "= ?", new String[]{new Integer(subcategoryList.get(i).getId()).toString()});
            subcategoryCursorWrapper.moveToFirst();
            if(subcategoryCursorWrapper.isAfterLast())
            {
                insertSubcategory(subcategoryList.get(i));
            }
        }
    }

    public void updateProduct(List<Product> productList)
    {
        for(int i=0;i<productList.size();i++)
        {
            ProductCursorWrapper productCursorWrapper=singleton.queryProduct(EatfitDBSchema.HealthProduct.Cols.ID + "= ?", new String[]{new Integer(productList.get(i).getId()).toString()});
            productCursorWrapper.moveToFirst();
            if(productCursorWrapper.isAfterLast())
            {   Product tempProduct=productList.get(i);
                insertProduct(tempProduct);
                insertSearchObject(new SearchObject(tempProduct.getName(), 1, tempProduct.getId()));
            }
        }
    }

    public void updateCompany(List<Company> companyList)
    {
        for(int i=0;i<companyList.size();i++)
        {
            CompanyCursorWrapper companyCursorWrapper=singleton.queryCompany(EatfitDBSchema.HealthFirmy.Cols.ID + "= ?", new String[]{new Integer(companyList.get(i).getId()).toString()});
            companyCursorWrapper.moveToFirst();
            if(companyCursorWrapper.isAfterLast())
            {
                Company tempCompany=companyList.get(i);
                insertCompany(tempCompany);
                insertSearchObject(new SearchObject(tempCompany.getName(), 3, tempCompany.getId()));
            }
        }
    }

    public void updateIngredients(List<Ingredient> ingredientList)
    {
        for(int i=0;i<ingredientList.size();i++)
        {
            IngredientCursorWrapper ingredientCursorWrapper=singleton.queryIngredient(EatfitDBSchema.HealthIngredients.Cols.ID + "= ?", new String[]{new Integer(ingredientList.get(i).getId()).toString()});
            ingredientCursorWrapper.moveToFirst();
            if(ingredientCursorWrapper.isAfterLast())
            {   Ingredient tempIngredient=ingredientList.get(i);
                insertIngredient(tempIngredient);
                if(!TextUtils.isEmpty(tempIngredient.getName1()))
                {
                    insertSearchObject(new SearchObject(tempIngredient.getName1(),2,tempIngredient.getId()));
                }
                if(!TextUtils.isEmpty(tempIngredient.getName2()))
                {
                    insertSearchObject(new SearchObject(tempIngredient.getName2(),2,tempIngredient.getId()));
                }
                if(!TextUtils.isEmpty(tempIngredient.getName3()))
                {
                    insertSearchObject(new SearchObject(tempIngredient.getName3(),2,tempIngredient.getId()));
                }
                if(!TextUtils.isEmpty(tempIngredient.getSymbol()))
                {
                    insertSearchObject(new SearchObject(tempIngredient.getSymbol(),2,tempIngredient.getId()));
                }
            }
        }
    }
}
