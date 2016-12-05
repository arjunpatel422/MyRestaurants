package com.blackstarsky.myrestaurants;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public
class RestaurantDisplaySettings extends BaseActivity
{

    public
    boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.display_settings_menu, menu);
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
            case R.id.homeIcon:
                callMainActivity();
                break;
            /*case R.id.searchIcon:
                search();
                break;*/
            case R.id.addNewRestaurantIcon:
                addNewRestaurant();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    CheckBox displayRestaurantsWithParkingOnlyCheckBox;
    Spinner displayOptionsSpinner;
    Spinner displayOrderSpinner;
    Button selectAllFoodTypesButton;
    Button deselectAllFoodTypesButton;
    Button saveDisplaySettingsButton;
    private LinearLayout linearLayout;
    private DisplaySettings displaySettings;
    private FoodType[] displayFoodTypes;

    DatabaseHandler databaseHandler;

    @Override
    public
    void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.restaurant_display_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar);
        databaseHandler = new DatabaseHandler(this);
        // Initialize all Objects
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        displayRestaurantsWithParkingOnlyCheckBox = (CheckBox) findViewById(
                R.id.displayRestaurantsWithParkingOnlyCheckBox);
        displayOptionsSpinner = (Spinner) findViewById(R.id.displayOptionsSpinner);
        displayOrderSpinner = (Spinner) findViewById(R.id.displayOrderSpinner);
        selectAllFoodTypesButton = (Button) findViewById(R.id.selectAllFoodTypesButton);
        deselectAllFoodTypesButton = (Button) findViewById(R.id.deselectAllFoodTypesButton);
        saveDisplaySettingsButton = (Button) findViewById(R.id.saveDisplaySettingsButton);
        displaySettings = databaseHandler.getDisplaySettings("1");
        changeToUsableFormat();
        addListeners();
        initializeSpinners();
    }

    public
    void saveDisplaySettings()
    {
        displaySettings.setDisplayOption(displayOptionsSpinner.getSelectedItem().toString());
        displaySettings.setDisplayOrder(displayOrderSpinner.getSelectedItem().toString());
        changeTodatabaseHandlerFormat();
        databaseHandler.updateDisplaySettings(displaySettings);
        databaseHandler.updateDisplayFoodTypes(displayFoodTypes);
        this.callMainActivity();
    }

    public
    void initializeSpinners()
    {
        ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) displayOptionsSpinner.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = spinnerAdapter.getPosition(displaySettings.getDisplayOption());
        displayOptionsSpinner.setSelection(spinnerPosition, false);

        spinnerAdapter = (ArrayAdapter<String>) displayOrderSpinner.getAdapter(); //cast to an ArrayAdapter

        spinnerPosition = spinnerAdapter.getPosition(displaySettings.getDisplayOrder());
        displayOrderSpinner.setSelection(spinnerPosition, false);
    }

    public
    void addListeners()
    {
        OnClickListener listener = new OnClickListener()
        {

            @Override
            public
            void onClick(View view)
            {
                switch (view.getId())
                {
                    case R.id.selectAllFoodTypesButton:
                        allFoodTypesSelector(true);
                        break;
                    case R.id.deselectAllFoodTypesButton:
                        allFoodTypesSelector(false);
                        break;
                    case R.id.saveDisplaySettingsButton:
                        saveDisplaySettings();
                        break;
                }

            }

        };
        selectAllFoodTypesButton.setOnClickListener(listener);
        deselectAllFoodTypesButton.setOnClickListener(listener);
        saveDisplaySettingsButton.setOnClickListener(listener);
    }

    public
    void allFoodTypesSelector(Boolean selected)
    {
        for (int position = 0; position < linearLayout.getChildCount(); position++)
        {
            CheckBox displayFoodTypeCheckBox = (CheckBox) linearLayout.getChildAt(
                    position).findViewById(R.id.displayFoodTypeCheckBox);
            displayFoodTypeCheckBox.setChecked(selected);
        }
    }

    public
    void changeToUsableFormat()
    {
        allFoodTypesSelector(true);
        displayRestaurantsWithParkingOnlyCheckBox.setChecked(displaySettings.displayRestaurantsWithParkingOnly() );
        String field = displaySettings.getDisplayOption();
        displaySettings.setDisplayOption(field.equals("restaurantName") ? "Alphabetically" : "By Rating");
        field = displaySettings.getDisplayOrder();
        displaySettings.setDisplayOrder(field.equals("ASC") ? "Ascending" : "Descending");
        displayFoodTypes = databaseHandler.getDisplayedFoodTypes();
        LayoutInflater inflater = getLayoutInflater();
        for (int position = 0; position < displayFoodTypes.length; position++)
        {
            View view = inflater.inflate(R.layout.food_type_entry, null);
            TextView displayFoodType = (TextView) view.findViewById(R.id.displayFoodType);
            displayFoodType.setText(displayFoodTypes[position].getFoodType());
            CheckBox displayFoodTypeCheckBox = (CheckBox) view.findViewById(
                    R.id.displayFoodTypeCheckBox);
            if (displayFoodTypes[position].isChecked())
            {
                displayFoodTypeCheckBox.setChecked(true);
            }
            linearLayout.addView(view, position);
        }
        //linearLayout.setVisibility(View.GONE);
    }

    public
    void changeTodatabaseHandlerFormat()
    {
        displaySettings.setDisplayRestaurantsWithParkingOnly(
                displayRestaurantsWithParkingOnlyCheckBox.isChecked() ? "T" : "F");
        String field = displaySettings.getDisplayOption();
        displaySettings.setDisplayOption(field.equals("Alphabetically") ? "restaurantName" : "restaurantRating");
        field = displaySettings.getDisplayOrder();
        displaySettings.setDisplayOrder(field.equals("Ascending") ? "ASC" : "DESC");
        int position;
        for (position = 0; position < linearLayout.getChildCount(); position++)
        {
            CheckBox displayFoodTypeCheckBox = (CheckBox) linearLayout.getChildAt(
                    position).findViewById(R.id.displayFoodTypeCheckBox);
            displayFoodTypes[position].setDisplay(displayFoodTypeCheckBox.isChecked() ? "T" : "F");
        }
    }
}