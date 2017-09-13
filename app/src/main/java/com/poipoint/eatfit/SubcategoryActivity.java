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

import com.poipoint.eatfit.database.DatabaseQuerySingleton;
import com.poipoint.eatfit.database.EatfitDBSchema;
import com.poipoint.eatfit.database.SubcategoryCursorWrapper;
import com.poipoint.eatfit.models.Subcategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tanmay on 2/8/2016.
 */
public class SubcategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SubCategoryAdapter adapter;
    List<Subcategory> subcategoryList=new ArrayList<Subcategory>();
    List<String> wordList=new ArrayList<String>();
    private static final String EXTRA_CATEGORY_ID ="com.poipoint.eatfit.category_id";
    private static final String TAG="SubcategoryActivity";
    private Integer cat_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        cat_id=getIntent().getIntExtra(EXTRA_CATEGORY_ID,0);
        Log.d(TAG,"the cat_id is"+cat_id);
        recyclerView=(RecyclerView)findViewById(R.id.subcategory_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadSubcategories();
        adapter=new SubCategoryAdapter(subcategoryList);
        recyclerView.setAdapter(adapter);

        setupSupportActionBar();


    }

    private void loadSubcategories() {
        Subcategory subcategoryHold=null;
        DatabaseQuerySingleton databaseQuerySingleton=DatabaseQuerySingleton.get(getApplicationContext());
        SubcategoryCursorWrapper subcategoryCursorWrapper=null;
        if(cat_id==0)
        {
            subcategoryCursorWrapper= databaseQuerySingleton.querySubcategory(null, null);
        }
        if(cat_id>=1)
        {
            Log.d(TAG,"Inside second if");
            String whereclause= EatfitDBSchema.HealthSubcategory.Cols.CATEGORY_ID+"= ?";
            String[] whereargs={cat_id.toString()};
            subcategoryCursorWrapper=databaseQuerySingleton.querySubcategory(whereclause,whereargs);
            Log.d(TAG,"Query done son!");
        }
        try
        {
            Log.d(TAG,"inside try");
            subcategoryCursorWrapper.moveToFirst();
            while (!subcategoryCursorWrapper.isAfterLast())
            {
                subcategoryHold=subcategoryCursorWrapper.getSubcategory();
                subcategoryList.add(subcategoryHold);
                subcategoryCursorWrapper.moveToNext();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            subcategoryCursorWrapper.close();
        }
        Collections.sort(subcategoryList);
        for (Subcategory c: subcategoryList)
        {
            Log.d(TAG, "The name is-->" + c.getName() + "<--Order id is-->" + c.getOrderId() + "<--");
            wordList.add(c.getName());
        }
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

    public static Intent makeIntent(Context context,int category_id)
    {
        Intent i=new Intent(context,SubcategoryActivity.class);
        i.putExtra(EXTRA_CATEGORY_ID,category_id);
        return i;
    }
    private class SubCategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {   public TextView textView;
        public int subcategory_id;
        public SubCategoryHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.category_list_item_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i=ProductsActivity.makeIntent(getBaseContext(),subcategory_id);
            startActivity(i);
        }
    }

    private class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryHolder>
    {
        private List<Subcategory> subcategories;

        public SubCategoryAdapter(List<Subcategory> categories)
        {
            this.subcategories=categories;
        }

        @Override
        public SubCategoryHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater=getLayoutInflater();
            View v=layoutInflater.inflate(R.layout.category_list_item,viewGroup,false);
            return new SubCategoryHolder(v);
        }

        @Override
        public void onBindViewHolder(SubCategoryHolder categoryHolder, int i) {
            categoryHolder.textView.setText(subcategories.get(i).getName());
            categoryHolder.subcategory_id=subcategories.get(i).getId();
        }

        @Override
        public int getItemCount() {
            return subcategories.size();
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
