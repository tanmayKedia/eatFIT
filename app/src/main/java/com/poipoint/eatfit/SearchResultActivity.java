package com.poipoint.eatfit;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poipoint.eatfit.database.DatabaseQuerySingleton;
import com.poipoint.eatfit.database.EatfitDBSchema;
import com.poipoint.eatfit.database.ProductCursorWrapper;
import com.poipoint.eatfit.models.Ingredient;
import com.poipoint.eatfit.models.Product;
import com.poipoint.eatfit.models.SearchObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tanmay on 2/21/2016.
 */
public class SearchResultActivity extends ProductsActivity {
    private static final String EXTRA_TYPE="com.poipoint.eatfit.typeextra";
    private static final String EXTRA_TABLE_ID="com.poipoint.eatfir.tableidextra";
    private static final String EXTRA_SHOW_ALL="showall";
    private String ingredient_id_column;
    private List<String> ingredient_id_list=new ArrayList<String>();
    private List<Product> listOfProducts=new ArrayList<Product>();
    ProgressBar progressBar;
    TextView loadingtextView;
    Integer table_id;
    int showAll=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        databaseQuerySingleton= DatabaseQuerySingleton.get(getApplicationContext());
        recyclerView=(RecyclerView)findViewById(R.id.products_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar=(ProgressBar)findViewById(R.id.product_activity_progress_bar);
        loadingtextView=(TextView)findViewById(R.id.product_activity_loading_text);
        showAll=getIntent().getIntExtra(EXTRA_SHOW_ALL,0);
        if(showAll>0)
        {
            loadAllProducts();
            indicateParentActivity=1;
        }
        else
        {
            loadProducts();
            indicateParentActivity=2;
        }
        adapter=new ProductAdapter(listOfProducts);
        recyclerView.setAdapter(adapter);

        setupSupportActionBar();
    }

    public static Intent makeIntent(Context context,int type,int table_id)
    {
        Intent intent=new Intent(context,SearchResultActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_TABLE_ID, table_id);
        return intent;
    }
    public static Intent makeIntent(Context context,int showAll)
    {
        Intent intent=new Intent(context,SearchResultActivity.class);
        intent.putExtra(EXTRA_SHOW_ALL,showAll);
        return intent;
    }

    private void loadAllProducts()
    {
        ProductCursorWrapper productCursorWrapper=databaseQuerySingleton.queryProduct(null,null);
        productCursorWrapper.moveToFirst();

        while (!productCursorWrapper.isAfterLast())
        {
            Product tempProduct = productCursorWrapper.getProduct();
            listOfProducts.add(tempProduct);
            productCursorWrapper.moveToNext();
        }
        Collections.sort(listOfProducts);
    }

    private void loadProducts()
    {
        int type=getIntent().getIntExtra(EXTRA_TYPE,0);
         table_id=getIntent().getIntExtra(EXTRA_TABLE_ID, 0);


        if (type==2)
        {   //This is for products containing certain ingredient.
            new SearchLoadTask().execute();
        }

        if(type==3)
        {
            ProductCursorWrapper productCursorWrapper=databaseQuerySingleton.queryProduct(EatfitDBSchema.HealthProduct.Cols.COMPANY+"= ?",new String[]{table_id.toString()});
            productCursorWrapper.moveToFirst();

            while (!productCursorWrapper.isAfterLast())
            {
                Product tempProduct = productCursorWrapper.getProduct();
                listOfProducts.add(tempProduct);
                productCursorWrapper.moveToNext();
            }
        }
    }

    private void loadProductsFromIngredient(Integer table_id) {
        ProductCursorWrapper productCursorWrapper=databaseQuerySingleton.queryProduct(null, null);
        productCursorWrapper.moveToFirst();

        while (!productCursorWrapper.isAfterLast())
        {   Log.d(TAG, "Inside while with ingredient id--->" + table_id);
            Product tempProduct=productCursorWrapper.getProduct();
            ingredient_id_column="";
            ingredient_id_column=tempProduct.getIngredients();
            Log.d(TAG,"Ingredient id coloumn value---->"+ingredient_id_column);
            ingredient_id_list.clear();
            makeIngredientIdArray();
            for(int i=0;i<ingredient_id_list.size();i++)
            {
                //Log.d(TAG,"inside for with ingredient list ids ---->"+ingredient_id_list);
                if(ingredient_id_list.get(i).equals(table_id.toString()))
                {
                    listOfProducts.add(tempProduct);
                }
            }
            Log.d(TAG,"AFTER FOR LOOP");
            productCursorWrapper.moveToNext();
        }
    }

    private void makeIngredientIdArray()
    {   Character char_hold;
        Character comma=new Character(',');
        String id_hold="";
        int ingredient_id_array_index=0;
        for(int i=0;i< ingredient_id_column.length();i++)
        {
            Log.d(TAG, "inside for i value is--->" + new Integer(i).toString());
            char_hold= ingredient_id_column.charAt(i);
            if(Character.isDigit(char_hold))
            {
                id_hold=id_hold+char_hold;
            }
            Log.d(TAG,"After if id_hold value--->"+id_hold);
            if(char_hold.compareTo(comma)==0)
            {   Log.d(TAG,"Inside comma if");
                //this is to check that the value in the column doesn't start with a
                if(id_hold!="")
                {
                    Log.d(TAG,"inside id_hold!=nil with id_hold value-->"+id_hold);
                    ingredient_id_list.add(id_hold);
                    id_hold="";
                }
            }
        }
        if(id_hold!="")
        {
            ingredient_id_list.add(id_hold);
        }
        Log.d(TAG,"Id array is--->"+ ingredient_id_list);
    }
    private class SearchLoadTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            loadProductsFromIngredient(table_id);
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            loadingtextView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            progressBar.setVisibility(View.GONE);
            loadingtextView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button in case of <API 16
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
