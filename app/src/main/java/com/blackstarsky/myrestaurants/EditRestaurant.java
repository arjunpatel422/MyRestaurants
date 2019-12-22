package com.blackstarsky.myrestaurants;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;

public
class EditRestaurant extends BaseActivity
{

    public
    boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.edit_restaurant_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchIcon).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return (super.onCreateOptionsMenu(menu));
    }

    public
    boolean onOptionsItemSelected(MenuItem item)
    {
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

    Button saveRestaurantButton;
    EditText restaurantName;
    Spinner foodTypeSpinner;
    EditText restaurantCity;
    EditText restaurantAddress;
    Spinner parkingSpinner;
    EditText restaurantPhoneNumber;
    EditText dateVisited;
    RatingBar restaurantRatingBar;
    EditText additionalComments;
    private Restaurant restaurant;
    DatabaseHandler databaseHandler;

    public
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_restaurant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar);
        databaseHandler = new DatabaseHandler(this);
        saveRestaurantButton = findViewById(R.id.saveRestaurantButton);
        restaurantName = findViewById(R.id.restaurantName);
        foodTypeSpinner = findViewById(R.id.foodTypeSpinner);
        restaurantCity = findViewById(R.id.restaurantCity);
        restaurantAddress = findViewById(R.id.restaurantAddress);
        parkingSpinner = findViewById(R.id.parkingSpinner);
        restaurantPhoneNumber = findViewById(R.id.restaurantPhoneNumber);
        dateVisited = findViewById(R.id.dateVisited);
        restaurantRatingBar = findViewById(R.id.restaurantRatingBar);
        additionalComments = findViewById(R.id.additionalComments);
        Intent theIntent = getIntent();
        restaurant = databaseHandler.getRestaurantInfo(theIntent.getStringExtra("restaurantId"));
        restaurantName.setText(restaurant.getRestaurantName());
        restaurantCity.setText(restaurant.getRestaurantCity());
        restaurantAddress.setText(restaurant.getRestaurantAddress());
        restaurantPhoneNumber.setText(restaurant.getRestaurantPhoneNumber());
        dateVisited.setText(restaurant.getDateVisited());
        additionalComments.setText(restaurant.getAdditionalComments());
        restaurantRatingBar.setRating(Float.parseFloat(restaurant.getRestaurantRating()));
        addListeners();
        initializeFoodTypeSpinner();
        initializeParkingSpinner();
    }

    public
    void updateRestaurant()
    {
        restaurantName = findViewById(R.id.restaurantName);
        foodTypeSpinner = findViewById(R.id.foodTypeSpinner);
        restaurantCity = findViewById(R.id.restaurantCity);
        restaurantAddress = findViewById(R.id.restaurantAddress);
        parkingSpinner = findViewById(R.id.parkingSpinner);
        restaurantPhoneNumber = findViewById(R.id.restaurantPhoneNumber);
        dateVisited = findViewById(R.id.dateVisited);
        restaurantRatingBar = findViewById(R.id.restaurantRatingBar);
        additionalComments = findViewById(R.id.additionalComments);
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
        databaseHandler.updateRestaurant(restaurant);
        viewRestaurant(restaurant.getRestaurantId());
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
        restaurantRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener()
        {
            public
            void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {

                restaurant.setRestaurantRating(Float.toString(rating));
            }
        });

        View.OnClickListener listener = (new View.OnClickListener()
        {
            @Override
            public
            void onClick(View view)
            {
                switch (view.getId())
                {
                    case R.id.saveRestaurantButton:
                        updateRestaurant();
                        break;
                }
            }
        });
        saveRestaurantButton.setOnClickListener(listener);
    }


    public
    void initializeFoodTypeSpinner()
    {

        ArrayAdapter<String> myAdap = (ArrayAdapter<String>) foodTypeSpinner.getAdapter(); //cast to an ArrayAdapter

        int foodTypeSpinnerPosition = myAdap.getPosition(restaurant.getFoodType());

        //set the default according to value
        foodTypeSpinner.setSelection(foodTypeSpinnerPosition, false);
    }

    public
    void initializeParkingSpinner()
    {

        ArrayAdapter<String> myAdap = (ArrayAdapter<String>) parkingSpinner.getAdapter(); //cast to an ArrayAdapter

        int parkingSpinnerPosition = myAdap.getPosition(restaurant.getRestaurantParking());

        //set the default according to value
        parkingSpinner.setSelection(parkingSpinnerPosition, false);
    }

    public
    boolean isValidName()
    {
        String name = restaurant.getRestaurantName();
        for (int position = 0; position < name.length(); position++)
        {
            char testedCharacter = name.charAt(position);
            if (testedCharacter != ' ')
            {
                return true;
            }
        }
        return false;
    }

}
