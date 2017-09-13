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

import com.poipoint.eatfit.database.CompanyCursorWrapper;
import com.poipoint.eatfit.database.DatabaseQuerySingleton;
import com.poipoint.eatfit.database.EatfitDBSchema;
import com.poipoint.eatfit.database.IngredientCursorWrapper;
import com.poipoint.eatfit.database.ProductCursorWrapper;
import com.poipoint.eatfit.models.Company;
import com.poipoint.eatfit.models.Ingredient;
import com.poipoint.eatfit.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 2/8/2016.
 */
public class ProductInfoActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private IngredientsAdapter adapter;
    private static final String EXTRA_PRODUCT_ID="com.poipoint.eatfit.product_id";
    private static final String EXTRA_PARENT_INDICATIOR="parentindicator";
    private static final String TAG="tagproductinforactivity";
    private Integer product_id;
    private DatabaseQuerySingleton databaseQuerySingleton;
    private Product product;
    private TextView productName;
    private TextView companyName;
    private TextView safetyLevel;
    private String ingredient_id_column;
    private List<String> ingredient_id_list=new ArrayList<String>();
    private List<Ingredient> ingredientList=new ArrayList<Ingredient>();
    private String danger_level="Safe";
    private int parentIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        product_id=getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);
        parentIndicator=getIntent().getIntExtra(EXTRA_PARENT_INDICATIOR,0);
        databaseQuerySingleton=DatabaseQuerySingleton.get(getBaseContext());

        loadProduct();
        recyclerView=(RecyclerView)findViewById(R.id.ingredients_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productName=(TextView)findViewById(R.id.product_info_product_name_text_view);
        companyName=(TextView)findViewById(R.id.product_info_company_name_text_view);
        safetyLevel=(TextView)findViewById(R.id.product_info_danger_level_text_view);
        ingredient_id_column =product.getIngredients();
        makeIngredientIdArray();
        loadIngredients();
        productName.setText(product.getName());
        CompanyCursorWrapper companyCursorWrapper=databaseQuerySingleton.queryCompany(EatfitDBSchema.HealthFirmy.Cols.ID+"= ?",new String[]{product.getCompany_id()});
        companyCursorWrapper.moveToFirst();
        Company company=companyCursorWrapper.getCompany();
        companyName.setText(company.getName());
        safetyLevel.setText(danger_level);

        adapter=new IngredientsAdapter(ingredientList);
        recyclerView.setAdapter(adapter);

        setupSupportActionBar();

    }

    private void loadProduct() {
        ProductCursorWrapper productCursorWrapper=databaseQuerySingleton.queryProduct(EatfitDBSchema.HealthCategory.Cols.ID + "= ?", new String[]{product_id.toString()});
        productCursorWrapper.moveToFirst();
        product=productCursorWrapper.getProduct();
    }

    private void loadIngredients()
    {
        IngredientCursorWrapper ingredientCursorWrapper;
        Ingredient ingredientHold;
        for(int i=0;i<ingredient_id_list.size();i++)
        {
            ingredientCursorWrapper=databaseQuerySingleton.queryIngredient(EatfitDBSchema.HealthIngredients.Cols.ID+"= ?",new String[]{ingredient_id_list.get(i)});
            ingredientCursorWrapper.moveToFirst();
            if(ingredientCursorWrapper.isAfterLast()==false)
            {
                ingredientHold=ingredientCursorWrapper.getIngredient();
                ingredientList.add(ingredientHold);
                if(ingredientHold.getDanger_level()==2&&!danger_level.equals("Very unsafe"))
                {
                    danger_level="Little unsafe";
                }
                if(ingredientHold.getDanger_level()==3)
                {
                    danger_level="Very unsafe";
                }
            }
        }
    }
    private void makeIngredientIdArray()
    {   Character char_hold;
        Character comma=new Character(',');
        String id_hold="";
        int ingredient_id_array_index=0;
        for(int i=0;i< ingredient_id_column.length();i++)
        {
            Log.d(TAG,"inside for i value is--->"+new Integer(i).toString());
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

    public static Intent makeIntent(Context context,int product_id)
    {
        Intent i=new Intent(context,ProductInfoActivity.class);
        i.putExtra(EXTRA_PRODUCT_ID, product_id);
        return i;
    }
    public static Intent makeIntent(Context context,int product_id,int parentIndicator)
    {
        Intent i=new Intent(context,ProductInfoActivity.class);
        i.putExtra(EXTRA_PRODUCT_ID,product_id);
        i.putExtra(EXTRA_PARENT_INDICATIOR,parentIndicator);
        return i;
    }
    //This method setups all actionbar customization
        private void setupSupportActionBar() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_title);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private class IngredientsHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {   public TextView textView;
        public Integer ingredient_id;
        public IngredientsHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.ingredient_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i=IngredientInfoActivity.makeIntent(getBaseContext(),ingredient_id.toString());
            startActivity(i);
        }
    }
    private class IngredientsAdapter extends RecyclerView.Adapter<IngredientsHolder>
    {
        private List<Ingredient> ingredient_list;

        public IngredientsAdapter(List<Ingredient> ingredient_list)
        {
            this.ingredient_list=ingredient_list;
        }

        @Override
        public IngredientsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater=getLayoutInflater();
            View v=layoutInflater.inflate(R.layout.ingridient_list_item,viewGroup,false);
            return new IngredientsHolder(v);
        }

        @Override
        public void onBindViewHolder(IngredientsHolder categoryHolder, int i) {
            categoryHolder.textView.setText(ingredient_list.get(i).getName1());
            categoryHolder.ingredient_id=ingredient_list.get(i).getId();
        }

        @Override
        public int getItemCount() {
            return ingredient_list.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button in case of <API 16
            case android.R.id.home:
            if(parentIndicator==0)
            {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            else
            {
                Intent i=new Intent(getBaseContext(),SearchResultActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
