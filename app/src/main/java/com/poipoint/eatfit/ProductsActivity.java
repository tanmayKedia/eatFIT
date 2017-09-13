package com.poipoint.eatfit;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.poipoint.eatfit.database.CompanyCursorWrapper;
import com.poipoint.eatfit.database.DatabaseQuerySingleton;
import com.poipoint.eatfit.database.EatfitDBSchema;
import com.poipoint.eatfit.database.ProductCursorWrapper;
import com.poipoint.eatfit.database.SubcategoryCursorWrapper;
import com.poipoint.eatfit.models.Company;
import com.poipoint.eatfit.models.Product;
import com.poipoint.eatfit.models.Subcategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tanmay on 2/7/2016.
 */
public class ProductsActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected ProductAdapter adapter;
    protected List<Product> productList=new ArrayList<Product>();
    protected static final String TAG="bluesclues";
    protected DatabaseQuerySingleton databaseQuerySingleton;
    protected     int indicateParentActivity=0;
    private static final String  EXTRA_SUBCATEGORY_ID="com.popoint.eatfit.subcategoryid";
    private Integer subcategory_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        databaseQuerySingleton=DatabaseQuerySingleton.get(getApplicationContext());
        subcategory_id=getIntent().getIntExtra(EXTRA_SUBCATEGORY_ID,0);
        recyclerView=(RecyclerView)findViewById(R.id.products_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();
        adapter=new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);

        setupSupportActionBar();

    }

    public static Intent makeIntent(Context context, int subcat_id)
    {
        Intent i=new Intent(context,ProductsActivity.class);
        i.putExtra(EXTRA_SUBCATEGORY_ID,subcat_id);
        return i;
    }

    private void loadProducts() {
        Product productHold=null;
        ProductCursorWrapper productCursorWrapper= databaseQuerySingleton.queryProduct(EatfitDBSchema.HealthProduct.Cols.SUBCATEGORY_ID+"= ?", new String[]{subcategory_id.toString()});

        try
        {   Log.d(TAG,"Subcategory_id is-->"+subcategory_id);
            Log.d(TAG,"inside try");
            productCursorWrapper.moveToFirst();
            while (!productCursorWrapper.isAfterLast())
            {   Log.d(TAG,"Inside while");
                productHold=productCursorWrapper.getProduct();
                productList.add(productHold);
                Log.d(TAG,"Product added--->"+productHold.getName());
                productCursorWrapper.moveToNext();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            productCursorWrapper.close();
        }

    }

    //This method setups all actionbar customization
        protected void setupSupportActionBar() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_title);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {   public TextView product_name;
        public TextView company_name;
        public int product_id;
        public ProductHolder(View itemView) {
            super(itemView);
            product_name=(TextView)itemView.findViewById(R.id.product_list_item_product_name_text_view);
            company_name=(TextView)itemView.findViewById(R.id.product_list_item_company_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i=ProductInfoActivity.makeIntent(getBaseContext(),product_id,indicateParentActivity);
            Log.d(TAG,"starting productinfoactivity with indicateparentActivityas--->"+indicateParentActivity);
            startActivity(i);
        }
    }

    protected class ProductAdapter extends RecyclerView.Adapter<ProductHolder>
    {
        private List<Product> productList;

        public ProductAdapter(List<Product> productList)
        {
            this.productList=productList;
        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater=getLayoutInflater();
            View v=layoutInflater.inflate(R.layout.product_list_item,viewGroup,false);

            return new ProductHolder(v);
        }

        @Override
        public void onBindViewHolder(ProductHolder categoryHolder, int i) {
            categoryHolder.product_name.setText(productList.get(i).getName());
            CompanyCursorWrapper companyCursorWrapper=databaseQuerySingleton.queryCompany(EatfitDBSchema.HealthFirmy.Cols.ID+"= ?",new String[]{productList.get(i).getCompany_id()});
            companyCursorWrapper.moveToFirst();
            Company company=companyCursorWrapper.getCompany();
            categoryHolder.company_name.setText(company.getName());
            categoryHolder.product_id=productList.get(i).getId();
        }

        @Override
        public int getItemCount() {
            return productList.size();
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