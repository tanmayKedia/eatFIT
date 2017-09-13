package com.poipoint.eatfit;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.poipoint.eatfit.database.DatabaseQuerySingleton;
import com.poipoint.eatfit.database.EatfitDBSchema;
import com.poipoint.eatfit.database.IngredientCursorWrapper;
import com.poipoint.eatfit.models.Ingredient;

/**
 * Created by Tanmay on 2/8/2016.
 */
public class IngredientInfoActivity extends AppCompatActivity{

    private static final String EXTRA_INGREDIENT_ID="com.poipoint.eatfit.ingredient_id";
    private static final String EXTRA_PARENT_INDICATOR="parent_indicator";
    private DatabaseQuerySingleton databaseQuerySingleton;
    private Ingredient ingredient;
    private TextView name;
    private TextView alternative_names;
    private TextView symbol;
    private TextView safety_level;
    private TextView description;
    private String alternative_names_string="";
    private int parent_indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_info);
        parent_indicator=getIntent().getIntExtra(EXTRA_PARENT_INDICATOR,0);
        databaseQuerySingleton=DatabaseQuerySingleton.get(getBaseContext());
        IngredientCursorWrapper ingredientCursorWrapper=databaseQuerySingleton.queryIngredient(EatfitDBSchema.HealthIngredients.Cols.ID+"= ?",new String[]{getIntent().getStringExtra(EXTRA_INGREDIENT_ID)});
        ingredientCursorWrapper.moveToFirst();
        ingredient=ingredientCursorWrapper.getIngredient();
        setupSupportActionBar();

        name=(TextView)findViewById(R.id.ingredient_info_ingredient_name_text_view);
        safety_level=(TextView)findViewById(R.id.ingredient_info_ingredient_safety_text_view);
        description=(TextView)findViewById(R.id.ingredient_info_description);
        alternative_names=(TextView)findViewById(R.id.alternate_names_text_view_ingredient_info);
        symbol=(TextView)findViewById(R.id.symbol_text_view_ingredient_info);

        name.setText(ingredient.getName1());
        if(!ingredient.getName2().equals(""))
        {   alternative_names.setVisibility(View.VISIBLE);
            alternative_names_string="("+ingredient.getName2();
                if(!ingredient.getName3().equals(""))
                {
                    alternative_names_string=alternative_names_string+", "+ingredient.getName3()+")";
                }
                else
                {
                    alternative_names_string=alternative_names_string+")";
                }
        }
        if(alternative_names_string.equals("")&&!ingredient.getName3().equals(""))
        {  alternative_names.setVisibility(View.VISIBLE);
            alternative_names_string="("+ingredient.getName3()+")";
        }
        alternative_names.setText(alternative_names_string);

        if(!ingredient.getSymbol().equals(""))
        {
            symbol.setVisibility(View.VISIBLE);
            symbol.setText(ingredient.getSymbol());
        }

        if(ingredient.getDanger_level()==3)
        {
            safety_level.setText("Very unsafe");
        }
        else if(ingredient.getDanger_level()==2)
        {
            safety_level.setText("Little unsafe");
        }
        else
        {
            safety_level.setText("Safe");
        }

        if(!ingredient.getDescription().equals(""))
        {
            description.setText(ingredient.getDescription());
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

    public static Intent makeIntent(Context context,String ingredient_id)
    {
        Intent i=new Intent(context,IngredientInfoActivity.class);
        i.putExtra(EXTRA_INGREDIENT_ID, ingredient_id);
        return i;
    }

    public static Intent makeIntent(Context context,String ingredient_id,int parentIndicator)
    {
        Intent i=new Intent(context,IngredientInfoActivity.class);
        i.putExtra(EXTRA_INGREDIENT_ID, ingredient_id);
        i.putExtra(EXTRA_PARENT_INDICATOR,parentIndicator);
        return i;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button in case of <API 16
            case android.R.id.home:
                if(parent_indicator==0)
                {

                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                else if (parent_indicator==2)
                {
                    Intent i=new Intent(getBaseContext(),IngredientListActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
