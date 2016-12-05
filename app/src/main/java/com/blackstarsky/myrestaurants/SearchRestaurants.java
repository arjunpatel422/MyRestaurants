package com.blackstarsky.myrestaurants;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public
class SearchRestaurants extends BaseActivity
{
    private static final String[] restaurantFields = {"restaurantId", "restaurantName", "foodType",
                                                      "restaurantCity", "restaurantAddress",
                                                      "restaurantParking", "restaurantPhoneNumber",
                                                      "dateVisited", "restaurantRating",
                                                      "additionalComments"};

    private ArrayList<Restaurant> searchResults;
    private ListView searchRestaurantsListView;
    private String query;
    private DatabaseHandler databaseHandler;
    private String m_selection;
    private String[] m_selectionArgs;
    private String m_orderBy;

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_restaurants);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar);
        databaseHandler = new DatabaseHandler(this);
        computeDisplayArgs();
        databaseHandler.setDisplay(m_selection,m_selectionArgs,m_orderBy);
        searchRestaurantsListView = (ListView) findViewById(R.id.searchRestaurantsListView);
        handleIntent(getIntent());
    }

    @Override
    public
    boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_restaurants_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchIcon).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public
    boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                changeDisplaySettings();
                break;
            case R.id.addNewRestaurantIcon:
                addNewRestaurant();
                break;
            case R.id.homeIcon:
                callMainActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected
    void onNewIntent(Intent intent)
    {
        handleIntent(intent);
    }

    private
    void handleIntent(Intent intent)
    {

        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            if (!isSearchable())
            {
                return;
            }
            if (searchResults != null)
            {
                searchResults.clear();
            }
            searchResults = databaseHandler.getAllSearchResults(query);
            if (searchResults.size() != 0)
            {

                searchRestaurantsListView.setOnItemClickListener(new OnItemClickListener()
                {

                    public
                    void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {

                        TextView restaurantId = (TextView) view.findViewById(R.id.restaurantId);
                        viewRestaurant(restaurantId.getText().toString());
                    }
                });

                ListAdapter adapter = new RestaurantAdapter(this, searchResults,
                                                            R.layout.restaurant_entry);
                searchRestaurantsListView.setAdapter(adapter);
            }

        }
    }

    public
    boolean isSearchable()
    {
        for (int position = 0; position < query.length(); position++)
        {
            Character testedCharacter = query.charAt(position);
            testedCharacter = Character.toLowerCase(testedCharacter);
            switch (testedCharacter)
            {
                case ' ':
                    continue;
                default:
                    return true;
            }
        }
        return false;
    }

    public
    void addListeners()
    {

    }

    private void computeDisplayArgs()
    {
        DisplaySettings displaySettings=databaseHandler.getDisplaySettings("1");
        int selectedFoodTypesCount=databaseHandler.getSelectedFoodTypesCount();
        int allFoodTypesCount=databaseHandler.getAllFoodTypesCount();
        int offset=(displaySettings.displayRestaurantsWithParkingOnly()?restaurantFields.length-1:restaurantFields.length-2);
        StringBuilder selection=new StringBuilder();
        int position;
        for(position=1;position<restaurantFields.length;position++)
        {
            if(position==5) //removes query from parking
                continue;
            selection.append(restaurantFields[position]); //puts in the field name
            selection.append(" = ? OR ");//puts in the equal sign
        }
        position=selection.length();
        selection.delete(position - 3, position);
        selection.append(" AND ");
        if(selectedFoodTypesCount==0)
        {
            selection.append("foodType=?");
            offset=restaurantFields.length-1;
            m_selectionArgs=new String[offset];
            m_selectionArgs[offset-1]="None";
        }

        else if (selectedFoodTypesCount==allFoodTypesCount)
        {
            m_selectionArgs=new String[offset];
            if(displaySettings.displayRestaurantsWithParkingOnly())
            {
                selection.append("restaurantParking=?");
                m_selectionArgs[offset-1]="Available";
            }

            else
            {
                offset=selection.length();
                selection.delete(offset-4,offset);
            }
        }

        else
        {

            String equalityComparison;
            if(selectedFoodTypesCount<allFoodTypesCount/2)
            {
                equalityComparison="foodType = ? AND ";
                m_selectionArgs=databaseHandler.getFoodTypes(new String[]{"T"},offset);
            }

            else
            {
                equalityComparison="foodType != ? AND ";
                m_selectionArgs=databaseHandler.getFoodTypes(new String[]{"F"}, offset);
            }

            if(offset>(restaurantFields.length-2))
            {
                selection.append("restaurantParking = ? AND ");
                m_selectionArgs[offset-1]="Available";
            }

            while(offset<m_selectionArgs.length)
            {
                selection.append(equalityComparison);
                offset++;
            }

            offset=selection.length();
            selection.delete(offset-4, offset);
        }
        m_selection=(selection.length()>0?selection.toString():null);
        m_orderBy=displaySettings.getDisplayOption()+" "+displaySettings.getDisplayOrder();
    }
}
