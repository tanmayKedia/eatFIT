package com.poipoint.eatfit;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poipoint.eatfit.database.DatabaseQuerySingleton;
import com.poipoint.eatfit.database.EatfitDBSchema;
import com.poipoint.eatfit.database.IngredientCursorWrapper;
import com.poipoint.eatfit.models.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tanmay on 2/23/2016.
 */
public class IngredientListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private IngredientsAdapter adapter;
    private Integer product_id;
    private DatabaseQuerySingleton databaseQuerySingleton;
    private List<Ingredient> ingredientList=new ArrayList<Ingredient>();
    private IngredientCursorWrapper ingredientCursorWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);
        databaseQuerySingleton=DatabaseQuerySingleton.get(getBaseContext());
        recyclerView=(RecyclerView)findViewById(R.id.ingredient_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_title);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadIngredients();
        adapter=new IngredientsAdapter(ingredientList);
        recyclerView.setAdapter(adapter);
    }

    private void loadIngredients()
    {

        ingredientCursorWrapper=databaseQuerySingleton.queryIngredient(null,null);
        ingredientCursorWrapper.moveToFirst();
        while (ingredientCursorWrapper.isAfterLast()==false)
        {   Ingredient ingredientHold;
            ingredientHold=ingredientCursorWrapper.getIngredient();
            ingredientList.add(ingredientHold);
            ingredientCursorWrapper.moveToNext();
        }
        Collections.sort(ingredientList);
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
            Intent i=IngredientInfoActivity.makeIntent(getBaseContext(),ingredient_id.toString(),2);
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
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
