package com.blackstarsky.myrestaurants;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;


public
class ViewRestaurant extends BaseActivity
{
    TextView viewRestaurantName;
    TextView viewFoodType;
    TextView viewRestaurantCity;
    TextView viewRestaurantAddress;
    TextView viewRestaurantParking;
    TextView viewRestaurantPhoneNumber;
    TextView viewDateVisited;
    RatingBar viewRestaurantRating;
    TextView viewAdditionalComments;
    Button exportRestaurantButton;
    private Restaurant restaurant;
    private Exporter exporter;
    private DatabaseHandler databaseHandler;

    public
    boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.view_restaurant_menu, menu);
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
            case R.id.editRestaurantIcon:
                editRestaurant(restaurant.getRestaurantId());
                break;
            case R.id.deleteRestaurantIcon:
                deleteRestaurant();
                break;
            case R.id.homeIcon:
                callMainActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected
    void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_restaurant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar);
        databaseHandler = new DatabaseHandler(this);
        viewRestaurantName = findViewById(R.id.viewRestaurantName);
        viewFoodType = findViewById(R.id.viewFoodType);
        viewRestaurantCity = findViewById(R.id.viewRestaurantCity);
        viewRestaurantAddress = findViewById(R.id.viewRestaurantAddress);
        viewRestaurantParking = findViewById(R.id.viewRestaurantParking);
        viewRestaurantPhoneNumber = findViewById(R.id.viewRestaurantPhoneNumber);
        viewDateVisited = findViewById(R.id.viewDateVisited);
        viewRestaurantRating = findViewById(R.id.viewRestaurantRating);
        viewAdditionalComments = findViewById(R.id.viewAdditionalComments);
        exportRestaurantButton = findViewById(R.id.exportRestaurantButton);
        Intent theIntent = getIntent();
        restaurant = databaseHandler.getRestaurantInfo(theIntent.getStringExtra("restaurantId"));
        viewRestaurantName.setText(restaurant.getRestaurantName());
        viewFoodType.setText(restaurant.getFoodType());
        viewRestaurantCity.setText(restaurant.getRestaurantCity());
        viewRestaurantAddress.setText(restaurant.getRestaurantAddress());
        viewRestaurantParking.setText(restaurant.getRestaurantParking());
        viewRestaurantPhoneNumber.setText(restaurant.getRestaurantPhoneNumber());
        viewDateVisited.setText(restaurant.getDateVisited());
        viewRestaurantRating.setRating(Float.parseFloat(restaurant.getRestaurantRating()));
        viewAdditionalComments.setText(restaurant.getAdditionalComments());
        viewRestaurantRating.setIsIndicator(true);
        addListeners();
    }

    public
    void deleteRestaurant()
    {
        databaseHandler.deleteRestaurant(restaurant.getRestaurantId());
        callMainActivity();
    }

    public
    boolean externalStateWritable()
    {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        if (storageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            return false;
        }
        if (storageState.equals(Environment.MEDIA_CHECKING))
        {
            return false;
        }
        if (storageState.equals(Environment.MEDIA_BAD_REMOVAL))
        {
            return false;
        }
        if (storageState.equals(Environment.MEDIA_NOFS))
        {
            return false;
        }
        if (storageState.equals(Environment.MEDIA_REMOVED))
        {
            return false;
        }
        if (storageState.equals(Environment.MEDIA_SHARED))
        {
            return false;
        }
        if (storageState.equals(Environment.MEDIA_UNMOUNTABLE))
        {
            return false;
        }
        if (storageState.equals(Environment.MEDIA_UNMOUNTED))
        {
            return false;
        }
        return false;
    }

    private
    void exportRestaurant()
    {
        if (externalStateWritable())
        {
            if (exporter == null)
            {
                exporter = new Exporter();
            }
            exporter.exportRestaurant(restaurant);
        }
    }

    public
    void addListeners()
    {
        OnClickListener listener = (new View.OnClickListener()
        {
            @Override
            public
            void onClick(View view)
            {
                switch (view.getId())
                {
                    case R.id.exportRestaurantButton:
                        exportRestaurant();
                        break;
                }
            }
        });
        exportRestaurantButton.setOnClickListener(listener);
    }
}
