package com.poipoint.eatfit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.poipoint.eatfit.database.CategoryCursorWrapper;
import com.poipoint.eatfit.database.CompanyCursorWrapper;
import com.poipoint.eatfit.database.DatabaseQuerySingleton;
import com.poipoint.eatfit.database.DatabseUpdateService;
import com.poipoint.eatfit.database.IngredientCursorWrapper;
import com.poipoint.eatfit.database.ProductCursorWrapper;
import com.poipoint.eatfit.database.SearchObjectCursorWrapper;
import com.poipoint.eatfit.models.Category;
import com.poipoint.eatfit.models.Company;
import com.poipoint.eatfit.models.Ingredient;
import com.poipoint.eatfit.models.Product;
import com.poipoint.eatfit.models.SearchObject;
import com.squareup.picasso.Picasso;

public class CategoreyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private SearchObjectAdapter searchObjectAdapter;
    private ActionBarDrawerToggle toggle;
    private List<Category> categoryList = new ArrayList<Category>();
    private static final String TAG = "tagcategoryactivity";
    DatabaseQuerySingleton databaseQuerySingleton;
    public static final String PREF_DB_VERSION="db_version";
    public static final String PREF_SEARCH_READY="search_ready";
    public SearchView searchView;
    private List<SearchObject> searchObjectList=new ArrayList<SearchObject>();
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_category);
        databaseQuerySingleton = DatabaseQuerySingleton.get(getApplicationContext());
        if( PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(PREF_DB_VERSION, 0)==0)
       {
           PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(PREF_DB_VERSION,1).apply();
       }

        if( PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(PREF_SEARCH_READY, 0)==0)
        {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(PREF_SEARCH_READY,1).apply();
        }

        SearchObjectCursorWrapper searchObjectCursorWrapper=databaseQuerySingleton.querySearch(null, null);
        searchObjectCursorWrapper.moveToFirst();
        if(searchObjectCursorWrapper.isAfterLast())
        {
            new InsertInSearchTable().execute();
        }

        recyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCategoriesFromDatabase();
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);

        setupSupportActionBar();
        new CheckupdateTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.category_activity_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView =(SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Company,Ingredient or Product...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                              @Override
                                              public boolean onQueryTextSubmit(String query) {
                                                  return false;
                                              }

                                              @Override
                                              public boolean onQueryTextChange(String newText) {
                                                  if(searchObjectAdapter!=null)
                                                  {
                                                      if (TextUtils.isEmpty(newText)) {
                                                          searchObjectAdapter.getFilter().filter("");
                                                      } else {
                                                          searchObjectAdapter.getFilter().filter(newText);
                                                      }
                                                  }
                                                  return true;
                                              }
                                          }
        );

        ImageView searchButton = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        ImageView closeButton=(ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        final Drawable pinkSearchDrawable = getResources().getDrawable(R.drawable.ic_search_black_24dp);
        final Drawable pinkCloseDrawable=getResources().getDrawable(R.drawable.ic_close_black_24dp);
        pinkSearchDrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        pinkCloseDrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        searchButton.setImageDrawable(pinkSearchDrawable);
        closeButton.setImageDrawable(pinkCloseDrawable);
        TextView seachTextView=(TextView)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        seachTextView.setTextColor(getResources().getColor(R.color.white));


        OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                Log.d(TAG, "In on colapse");
                categoryAdapter.categories=categoryList;
                recyclerView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(PREF_SEARCH_READY, 0)!=2) {
                    Toast.makeText(getBaseContext(), "Search not ready yet.", Toast.LENGTH_LONG).show();
                    //MenuItemCompat.collapseActionView(searchItem);
                }
                else
                {
                    if(searchObjectList.size()==0)
                    {
                        SearchObjectCursorWrapper searchObjectCursorWrapper = databaseQuerySingleton.querySearch(null, null);
                        searchObjectCursorWrapper.moveToFirst();
                        while (!searchObjectCursorWrapper.isAfterLast())
                        {
                            searchObjectList.add(searchObjectCursorWrapper.getSearchObject());
                            searchObjectCursorWrapper.moveToNext();
                        }
                        Collections.sort(searchObjectList);
                        searchObjectAdapter=new SearchObjectAdapter(searchObjectList);
                    }
                    recyclerView.setAdapter(searchObjectAdapter);
                    searchObjectAdapter.notifyDataSetChanged();
                }
                return true;  // Return true to expand action view
            }
        };
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        return true;
    }


    //This method setups all actionbar customization
    private void setupSupportActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_title);

        String[] list = {"Home", "Products", "Ingredients", "About", "Exit"};
        ListView drawer = (ListView) findViewById(R.id.category_drawer_list_view);
        drawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
        drawerLayout = (DrawerLayout) findViewById(R.id.category_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_closed);
        drawerLayout.setDrawerListener(toggle);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_drawer);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        drawer.setOnItemClickListener(new DrawerItemClickListener());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    //This method loads the categories in to category model objects
    //Stores these objects into a List
    //Sorts the list according to order_id
    //takes out names in the sorted order to give to the categoryAdapter
    private void loadCategoriesFromDatabase() {
        Category categoryHold = null;

        CategoryCursorWrapper categoryCursorWrapper = databaseQuerySingleton.queryCategory(null, null);
        try {
            categoryCursorWrapper.moveToFirst();
            while (!categoryCursorWrapper.isAfterLast()) {
                categoryHold = categoryCursorWrapper.getCategory();
                categoryList.add(categoryHold);
                categoryCursorWrapper.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            categoryCursorWrapper.close();
        }
        Collections.sort(categoryList);
    }

    private void insertSearchTable() {
        ProductCursorWrapper productCursorWrapper=databaseQuerySingleton.queryProduct(null, null);
        productCursorWrapper.moveToFirst();
        while (!productCursorWrapper.isAfterLast())
        {
            Product tempProduct=productCursorWrapper.getProduct();
            databaseQuerySingleton.insertSearchObject(new SearchObject(tempProduct.getName(), 1, tempProduct.getId()));
            productCursorWrapper.moveToNext();
        }
        IngredientCursorWrapper ingredientCursorWrapper=databaseQuerySingleton.queryIngredient(null, null);
        ingredientCursorWrapper.moveToFirst();
        while (!ingredientCursorWrapper.isAfterLast())
        {
            Ingredient tempIngredient=ingredientCursorWrapper.getIngredient();
            if(!TextUtils.isEmpty(tempIngredient.getName1()))
            {
                databaseQuerySingleton.insertSearchObject(new SearchObject(tempIngredient.getName1(),2,tempIngredient.getId()));
            }
            if(!TextUtils.isEmpty(tempIngredient.getName2()))
            {
                databaseQuerySingleton.insertSearchObject(new SearchObject(tempIngredient.getName2(),2,tempIngredient.getId()));
            }
            if(!TextUtils.isEmpty(tempIngredient.getName3()))
            {
                databaseQuerySingleton.insertSearchObject(new SearchObject(tempIngredient.getName3(),2,tempIngredient.getId()));
            }
            if(!TextUtils.isEmpty(tempIngredient.getSymbol()))
            {
                databaseQuerySingleton.insertSearchObject(new SearchObject(tempIngredient.getSymbol(),2,tempIngredient.getId()));
            }
            ingredientCursorWrapper.moveToNext();
        }

        CompanyCursorWrapper companyCursorWrapper=databaseQuerySingleton.queryCompany(null, null);
        companyCursorWrapper.moveToFirst();
        while (!companyCursorWrapper.isAfterLast())
        {
            Company tempCompany=companyCursorWrapper.getCompany();
            databaseQuerySingleton.insertSearchObject(new SearchObject(tempCompany.getName(), 3, tempCompany.getId()));
            companyCursorWrapper.moveToNext();
        }
        Log.d(TAG,"done adding to search from list");
    }


    private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public int id;
        public ImageView imageView;
        public CategoryHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.category_list_item_text_view);
            imageView =(ImageView)itemView.findViewById(R.id.category_list_item_image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = SubcategoryActivity.makeIntent(CategoreyActivity.this, id);
            startActivity(i);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> implements Filterable {
        public List<Category> categories;
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Category> tempList=new ArrayList<Category>();
                //constraint is the result from text you want to filter against.
                //objects is your data set you will filter from
                if(constraint != null && categoryList!=null) {
                    int length=categoryList.size();
                    int i=0;
                    while(i<length){
                        Category item=categoryList.get(i);
                        //do whatever you wanna do here
                        //adding result set output array
                        if(item.getName().toLowerCase ().contains(constraint.toString().toLowerCase())) {
                            tempList.add(item);
                        }
                        i++;
                    }
                    //following two lines is very important
                    //as publish result can only take FilterResults objects
                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (results.count > 0) {
                    categories = (ArrayList<Category>) results.values;
                    notifyDataSetChanged();
                } else {
                    categories=new ArrayList<Category>();
                    notifyDataSetChanged();
                }
            }
        };
        @Override
        public Filter getFilter() {
            return myFilter;
        }
        public CategoryAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.category_list_item, viewGroup, false);
            return new CategoryHolder(v);
        }

        @Override
        public void onBindViewHolder(CategoryHolder categoryHolder, int i) {
            categoryHolder.textView.setText(categories.get(i).getName());
            categoryHolder.id = categories.get(i).getId();
            //Change base url here to load images from your desired location.
            Picasso.with(getBaseContext())
                    .load("http://i.imgur.com/"+categories.get(i).getIcon_url())
                    .resize(100,100)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .into(categoryHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return categories.size();

        }
    }


    private class SearchObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView preText;
        public TextView name;
        public int type;
        public int tableId;

        public SearchObjectHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.search_list_name_text_view);
            preText=(TextView)itemView.findViewById(R.id.search_list_comment_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(type==1)
            {
                //means it is a product, call the product info activity directly.
                Intent i=ProductInfoActivity.makeIntent(getBaseContext(), tableId);
                startActivity(i);
            }
           else
            {
                Intent i=SearchResultActivity.makeIntent(getBaseContext(),type,tableId);
                startActivity(i);
            }
        }
    }



    private class SearchObjectAdapter extends RecyclerView.Adapter<SearchObjectHolder> implements Filterable {
        public List<SearchObject> searchObjectAdapterList;
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<SearchObject> tempList=new ArrayList<SearchObject>();
                //constraint is the result from text you want to filter against.
                //objects is your data set you will filter from
                if(constraint != null && searchObjectList!=null) {
                    int length=searchObjectList.size();
                    int i=0;
                    while(i<length){
                        SearchObject item=searchObjectList.get(i);
                        //do whatever you wanna do here
                        if(item.getName().toLowerCase ().contains(constraint.toString().toLowerCase())) {
                            tempList.add(item);
                        }
                        i++;
                    }
                    //following two lines is very important
                    //as publish result can only take FilterResults objects
                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (results.count > 0) {
                    searchObjectAdapterList = (ArrayList<SearchObject>) results.values;
                    notifyDataSetChanged();
                } else {
                    searchObjectAdapterList=new ArrayList<SearchObject>();
                    notifyDataSetChanged();
                }
            }
        };
        @Override
        public Filter getFilter() {
            return myFilter;
        }
        public SearchObjectAdapter(List<SearchObject> searchObjectAdapterList) {
            this.searchObjectAdapterList = searchObjectAdapterList;
        }

        @Override
        public SearchObjectHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.search_list_item, viewGroup, false);
            return new SearchObjectHolder(v);
        }

        @Override
        public void onBindViewHolder(SearchObjectHolder searchObjectHolder, int i) {
            SearchObject temp=searchObjectAdapterList.get(i);
            searchObjectHolder.type=temp.getType();
            searchObjectHolder.tableId=temp.getTableId();
            searchObjectHolder.name.setText(searchObjectAdapterList.get(i).getName());
            if(temp.getType()==1)
            {
                searchObjectHolder.preText.setText("");
            }
            else if(temp.getType()==2)
            {
                searchObjectHolder.preText.setText("Products containing:");
            }
            else if(temp.getType()==3)
            {
                searchObjectHolder.preText.setText("Products from:");
            }
        }

        @Override
        public int getItemCount() {
            return searchObjectAdapterList.size();

        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button in case of below api 16
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CheckupdateTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            int latest_version=new JsonDownloaderAndParser().latestVersion();
            Log.d("tag","Latest version is"+latest_version);
            int current_version=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(PREF_DB_VERSION,0);
            Log.d("tag","current_version_is"+current_version);
            if(current_version!=0&&current_version<latest_version) {
                Intent i = DatabseUpdateService.makeIntent(getBaseContext(), current_version, latest_version);
                getBaseContext().startService(i);
            }
            return null;
        }
    }

    private class InsertInSearchTable extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG,"before insert in search tables");
            insertSearchTable();
            Log.d(TAG, "before setting to 2");
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(PREF_SEARCH_READY,2).apply();
            Log.d(TAG,"after setting to 2");
            return null;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
        private void selectItem(int position) {

            if (position==0)
            {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
            else if(position==1)
            {
                Intent i=SearchResultActivity.makeIntent(getBaseContext(),5);
                startActivity(i);
            }
            else if(position==2)
            {
                Intent i=new Intent(getBaseContext(),IngredientListActivity.class);
                startActivity(i);
            }

            else if(position==3)
            {
                Intent i=new Intent(getBaseContext(),AboutActivity.class);
                startActivity(i);
            }
            else if(position==4)
            {
                finish();
                System.exit(0);

            }
        }
        }


}