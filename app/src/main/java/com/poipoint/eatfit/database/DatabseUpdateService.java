package com.poipoint.eatfit.database;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.poipoint.eatfit.CategoreyActivity;
import com.poipoint.eatfit.JsonDownloaderAndParser;
import com.poipoint.eatfit.models.Category;
import com.poipoint.eatfit.models.Company;
import com.poipoint.eatfit.models.Ingredient;
import com.poipoint.eatfit.models.Product;
import com.poipoint.eatfit.models.SearchObject;
import com.poipoint.eatfit.models.Subcategory;

import java.io.IOException;
import java.util.List;

/**
 * Created by Tanmay on 2/14/2016.
 */
public class DatabseUpdateService extends IntentService {
    private static final String TAG="tagdtabaseupdateservice";
    private static final String EXTRA_CURRENTVERSION="com.poipoint.eatfit.currentVersion";
    private static final String EXTRA_LATESTVERSION="com.poipoint.eatfit.latestVersion";
    private DatabaseQuerySingleton databaseQuerySingleton=DatabaseQuerySingleton.get(getBaseContext());
    public static Intent makeIntent(Context context,int current_version, int latest_version)
    {   Intent i=new Intent(context,DatabseUpdateService.class);
        i.putExtra(EXTRA_CURRENTVERSION,current_version);
        i.putExtra(EXTRA_LATESTVERSION,latest_version);
        return i;

    }

    public DatabseUpdateService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onhandleIntent");
        databaseQuerySingleton = DatabaseQuerySingleton.get(getBaseContext());
        JsonDownloaderAndParser jsonDownloaderAndParser = new JsonDownloaderAndParser();
        int current_version = intent.getIntExtra(EXTRA_CURRENTVERSION, 0);
        int latest_version = intent.getIntExtra(EXTRA_LATESTVERSION, 0);
        //Toast.makeText(getBaseContext(), "Database updating current_version:" + current_version + " latest_version: " + latest_version, Toast.LENGTH_SHORT);
        Log.d(TAG,"Current version--->"+current_version);
        Log.d(TAG,"Latest version---->"+latest_version);
        String baseurl="http://demo0922168.mockable.io/update/v";

        while (current_version < latest_version) {
            Log.d(TAG,"insidewhile");
            String updateJSONString = "";
            String url = baseurl+new Integer(current_version+1).toString();
            Log.d(TAG,"Loading data from"+url);
            try {
                updateJSONString = new JsonDownloaderAndParser().getUrlString(url);
                Log.d(TAG, "Fetched json is" + updateJSONString);
            } catch (IOException ioe) {
                Log.e(TAG, "Json download unsuccessful: ", ioe);
            }


            if (!updateJSONString.equals("")) {
                List<Category> categoryList = jsonDownloaderAndParser.getCategoryFromJson(updateJSONString);
                Log.d(TAG, "No of new categories---->" + categoryList.size());
                databaseQuerySingleton.updateCategories(categoryList);

                List<Subcategory> subcategoriesList = jsonDownloaderAndParser.getSubcategoryFromJson(updateJSONString);
                Log.d(TAG, "No of new subcategories---->" + subcategoriesList.size());
                databaseQuerySingleton.updateSubcategory(subcategoriesList);

                List<Product> productList = jsonDownloaderAndParser.getProductFromJson(updateJSONString);
                Log.d(TAG, "No of new products--->" + productList.size());
                databaseQuerySingleton.updateProduct(productList);

                List<Company> companyList = jsonDownloaderAndParser.getCompanyFromJson(updateJSONString);
                Log.d(TAG, "No of new companies--->" + companyList.size());
                databaseQuerySingleton.updateCompany(companyList);

                List<Ingredient> ingredientList = jsonDownloaderAndParser.getIngredientFromJson(updateJSONString);
                Log.d(TAG, "No of new ingredients--->" + ingredientList.size());
                databaseQuerySingleton.updateIngredients(ingredientList);
            }
            current_version++;
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(CategoreyActivity.PREF_DB_VERSION, current_version).apply();
            Log.d(TAG, " DB version updated to" + PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(CategoreyActivity.PREF_DB_VERSION, 0));
            //Toast.makeText(getBaseContext(), "DB version updated to" + PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(CategoreyActivity.PREF_DB_VERSION, 0), Toast.LENGTH_SHORT);

            SearchObjectCursorWrapper searchObjectCursorWrapper1=databaseQuerySingleton.querySearch(null,null);
            searchObjectCursorWrapper1.moveToFirst();
            while (!searchObjectCursorWrapper1.isAfterLast())
            {
                SearchObject tempObject=searchObjectCursorWrapper1.getSearchObject();
                Log.d(TAG, "Name--->" + tempObject.getName() + " Type----->" + tempObject.getType() + " Table Id-------->" + tempObject.getTableId());
                searchObjectCursorWrapper1.moveToNext();
            }
            Log.d(TAG, "intent hadling done son!");
        }
    }


}

