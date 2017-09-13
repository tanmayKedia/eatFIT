package com.poipoint.eatfit;

import android.util.Log;

import com.poipoint.eatfit.models.Category;
import com.poipoint.eatfit.models.Company;
import com.poipoint.eatfit.models.Ingredient;
import com.poipoint.eatfit.models.Product;
import com.poipoint.eatfit.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 2/13/2016.
 */
public class JsonDownloaderAndParser {
    private static String TAG="json son";
    public String getUrlString(String urlSpec) throws IOException
    {
        URL url=new URL(urlSpec);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();

        try
        {
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            InputStream in=connection.getInputStream();

            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK)
            {
                throw new IOException(connection.getResponseMessage()+": with "+urlSpec);
            }

            int bytesRead=0;
            byte[] buffer=new byte[1024];
            while((bytesRead=in.read(buffer))>0)
            {
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return new String(out.toByteArray());
        }
        finally {
            connection.disconnect();
        }
    }

    public int latestVersion()
    {   int latestversion=0;
        try {
            String updateJSON = new JsonDownloaderAndParser().getUrlString("http://demo0922168.mockable.io/latestversion");
            Log.d(TAG, "Fetched json is" + updateJSON);
            JSONObject updateJSONobject=new JSONObject(updateJSON);
            if(updateJSONobject.has("latestversion"))
            {
                latestversion=updateJSONobject.getInt("latestversion");
            }
        }
        catch (JSONException je)
        {
            Log.e(TAG,"Failed to parse json",je);
        }
        catch (IOException e)
        {
            Log.e(TAG,"Failed to fetch json",e);
        }


        return latestversion;
    }

    public List<Category> getCategoryFromJson(String updateJSONString) {
        List<Category> categoryList=new ArrayList<Category>();
        try {
            JSONObject updateJSONobject=new JSONObject(updateJSONString);
            if(updateJSONobject.has("category"))
            {
                JSONArray categoryJSONArray=updateJSONobject.getJSONArray("category");

                for(int i=0;i<categoryJSONArray.length();i++)
                {
                    JSONObject categoryJson=categoryJSONArray.getJSONObject(i);
                    int id=categoryJson.getInt("id");
                    int order_id=categoryJson.getInt("id_order");
                    String nazwa=categoryJson.getString("nazwa");
                    String icon=categoryJson.getString("icon");
                    categoryList.add(new Category(id,order_id,nazwa,icon));
                }
            }
        }
        catch (JSONException je)
        {
            Log.e(TAG,"Failed to parse json",je);
        }

        return categoryList;
    }

    public List<Subcategory> getSubcategoryFromJson(String updateJSONString) {
        List<Subcategory> subcategoryList=new ArrayList<Subcategory>();
        try {
            JSONObject updateJSONobject=new JSONObject(updateJSONString);
            if(updateJSONobject.has("subcategory"))
            {
                JSONArray subcategoryJSONArray=updateJSONobject.getJSONArray("subcategory");

                for(int i=0;i<subcategoryJSONArray.length();i++)
                {
                    JSONObject subcategoryJson=subcategoryJSONArray.getJSONObject(i);
                    int id=subcategoryJson.getInt("id");
                    int cat_1=subcategoryJson.getInt("cat1");
                    String nazwa=subcategoryJson.getString("name");
                    int order_id=subcategoryJson.getInt("id_order");
                    subcategoryList.add(new Subcategory(id, cat_1, nazwa, order_id));
                }
            }
        }
        catch (JSONException je)
        {
            Log.e(TAG,"Failed to parse json",je);
        }
        return subcategoryList;
    }

    public List<Product> getProductFromJson(String updateJSONString) {
        List<Product> productList=new ArrayList<Product>();
        try {
            JSONObject updateJSONobject=new JSONObject(updateJSONString);
            if(updateJSONobject.has("product"))
            {
                JSONArray productJSONArray=updateJSONobject.getJSONArray("product");

                for(int i=0;i<productJSONArray.length();i++)
                {
                    JSONObject productJson=productJSONArray.getJSONObject(i);
                    int id=productJson.getInt("id");
                    int main_cat=productJson.getInt("main_cat");
                    int second_cat=productJson.getInt("second_cat");
                    String nazwa=productJson.getString("nazwa");
                    String company=productJson.getString("firma");
                    String ingredient=productJson.getString("sklad");
                    int barcode=productJson.getInt("stan");
                    productList.add(new Product(id, main_cat, second_cat, nazwa, company, ingredient));
                }
            }
        }
        catch (JSONException je)
        {
            Log.e(TAG,"Failed to parse json",je);
        }
        return productList;
    }

    public List<Company> getCompanyFromJson(String updateJSONString) {
        List<Company> companyList=new ArrayList<Company>();
        try {
            JSONObject updateJSONobject=new JSONObject(updateJSONString);
            if(updateJSONobject.has("company"))
            {
                JSONArray companyJSONArray=updateJSONobject.getJSONArray("company");

                for(int i=0;i<companyJSONArray.length();i++)
                {
                    JSONObject productJson=companyJSONArray.getJSONObject(i);
                    int id=productJson.getInt("id");
                    String company_name=productJson.getString("firma");
                    companyList.add(new Company(id, company_name));
                }
            }
        }
        catch (JSONException je)
        {
            Log.e(TAG,"Failed to parse json",je);
        }
        return companyList;
    }

    public List<Ingredient> getIngredientFromJson(String updateJSONString) {
        List<Ingredient> ingredientList=new ArrayList<Ingredient>();
        try {
            JSONObject updateJSONobject=new JSONObject(updateJSONString);
            if(updateJSONobject.has("ingredients"))
            {
                JSONArray ingredientJSONArray=updateJSONobject.getJSONArray("ingredients");

                for(int i=0;i<ingredientJSONArray.length();i++)
                {
                    JSONObject productJson=ingredientJSONArray.getJSONObject(i);
                    int id=productJson.getInt("id");
                    String nazwa=productJson.getString("nazwa");
                    String nazwa2=productJson.getString("nazwa2");
                    String nazwa3=productJson.getString("nazwa3");
                    String symbol=productJson.getString("symbol");
                    String description=productJson.getString("opis");
                    int danger=productJson.getInt("danger");
                    ingredientList.add(new Ingredient(id,nazwa,nazwa2,nazwa3,symbol,description,danger));
                }
            }
        }
        catch (JSONException je)
        {
            Log.e(TAG,"Failed to parse json",je);
        }
        return ingredientList;
    }
}
