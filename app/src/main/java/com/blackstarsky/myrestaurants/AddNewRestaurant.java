package com.blackstarsky.myrestaurants;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

/**
 * Created by Arjun on 6/14/15.
 */
public
class AddNewRestaurant extends BaseActivity
{

    @Override
    public
    boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_restaurant_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchIcon).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public
    boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle item selection
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.action_settings:
                changeDisplaySettings();
                break;
            /*case R.id.searchIcon:
                search();
                break;*/
            case R.id.homeIcon:
                callMainActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    EditText restaurantName;
    Spinner foodTypeSpinner;
    EditText restaurantCity;
    EditText restaurantAddress;
    Spinner parkingSpinner;
    EditText restaurantPhoneNumber;
    EditText dateVisited;
    RatingBar restaurantRatingBar;
    EditText additionalComments;
    Button addNewRestaurantButton;
    private Restaurant restaurant;
    DatabaseHandler databaseHandler;

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar);
        databaseHandler = new DatabaseHandler(this);
        restaurant = new Restaurant();
        // Initialize all Objects
        addNewRestaurantButton = findViewById(R.id.addNewRestaurantButton);
        restaurant.setRestaurantRating("0.0");
        restaurantName = findViewById(R.id.restaurantName);
        foodTypeSpinner = findViewById(R.id.foodTypeSpinner);
        restaurantCity = findViewById(R.id.restaurantCity);
        restaurantAddress = findViewById(R.id.restaurantAddress);
        parkingSpinner = findViewById(R.id.parkingSpinner);
        restaurantPhoneNumber = findViewById(R.id.restaurantPhoneNumber);
        dateVisited = findViewById(R.id.dateVisited);
        restaurantRatingBar = findViewById(R.id.restaurantRatingBar);
        additionalComments = findViewById(R.id.additionalComments);
        addListeners();
    }

    public
    void addRestaurant()
    {

        restaurant.setRestaurantName(restaurantName.getText().toString());
        if (!isValidName())
        {
            return;
        }
        restaurant.setFoodType(foodTypeSpinner.getSelectedItem().toString());
        restaurant.setRestaurantCity(restaurantCity.getText().toString());
        restaurant.setRestaurantAddress(restaurantAddress.getText().toString());
        restaurant.setRestaurantParking(parkingSpinner.getSelectedItem().toString());
        restaurant.setRestaurantPhoneNumber(restaurantPhoneNumber.getText().toString());
        restaurant.setDateVisited(dateVisited.getText().toString());
        restaurant.setAdditionalComments(additionalComments.getText().toString());
        databaseHandler.insertRestaurant(restaurant);
        callMainActivity();
    }

    public
    void addListeners()
    {
        foodTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {

            @Override
            public
            void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
            }

            @Override
            public
            void onNothingSelected(AdapterView<?> arg0)
            {
            }
        });
        parkingSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {

            @Override
            public
            void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
            }

            @Override
            public
            void onNothingSelected(AdapterView<?> arg0)
            {
            }
        });
        restaurantRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            public
            void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {

                restaurant.setRestaurantRating(Float.toString(rating));
            }
        });

        OnClickListener listener = (new View.OnClickListener()
        {
            @Override
            public
            void onClick(View view)
            {
                switch (view.getId())
                {
                    case R.id.addNewRestaurantButton:
                        addRestaurant();
                        break;
                }
            }
        });
        addNewRestaurantButton.setOnClickListener(listener);
    }

    public
    boolean isValidName()
    {
        String name = restaurant.getRestaurantName();
        for (int position = 0; position < name.length(); position++)
        {
            char testedCharacter = name.charAt(position);
            if (testedCharacter == ' ') {
                continue;
            }
            return true;
        }
        return false;
    }
}
