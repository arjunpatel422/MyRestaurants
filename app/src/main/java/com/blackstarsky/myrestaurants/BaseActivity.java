package com.blackstarsky.myrestaurants;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Method;

public abstract
class BaseActivity extends AppCompatActivity
{

    private Toolbar m_toolbar;
    private int m_menuId;

    @Override
    public
    boolean onCreateOptionsMenu(Menu menu)
    {
        if (menu != null && menu.getClass().getSimpleName().equals("MenuBuilder"))
        {
            try
            {
                Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            }
            catch (Exception e)
            {
                Log.e(getClass().getSimpleName(),
                      "onMenuOpened...unable to set icons for overflow menu", e);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    public
    void callMainActivity()
    {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

    public
    void addNewRestaurant()
    {
        Intent addNewRestaurantIntent = new Intent(getApplicationContext(), AddNewRestaurant.class);
        startActivity(addNewRestaurantIntent);
    }

    public
    void changeDisplaySettings()
    {
        Intent changeDisplaySettingsIntent = new Intent(getApplicationContext(),
                                                        RestaurantDisplaySettings.class);
        startActivity(changeDisplaySettingsIntent);
    }

    /*public
    void search()
    {
        Intent searchIntent = new Intent(getApplicationContext(), SearchRestaurants.class);
        startActivity(searchIntent);
    }*/

    public
    void importRestaurants()
    {
        Intent importRestaurantsIntent = new Intent(getApplicationContext(),
                                                    ImportRestaurants.class);
        startActivity(importRestaurantsIntent);
    }

    public
    void editRestaurant(String restaurantId)
    {
        Intent editIntent = new Intent(getApplicationContext(), EditRestaurant.class);
        editIntent.putExtra("restaurantId", restaurantId);
        startActivity(editIntent);
    }

    public
    void viewRestaurant(String restaurantId)
    {
        Intent viewIntent = new Intent(getApplicationContext(), ViewRestaurant.class);
        viewIntent.putExtra("restaurantId", restaurantId);
        startActivity(viewIntent);
    }

    abstract public
    void addListeners(); //conveniently forces people to add a listener for performance

    public void setMenu(int menuId)
    {
        this.m_menuId=menuId;
    }

    public void setToolbar(Toolbar toolbar)
    {
        this.m_toolbar=toolbar;
        if (m_toolbar != null)
        {
            setSupportActionBar(m_toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
    }

}
