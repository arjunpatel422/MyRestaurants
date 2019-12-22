package com.blackstarsky.myrestaurants;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

public
class MainActivity extends BaseActivity
{

    @Override
    public
    boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
        switch (item.getItemId())
        {
            case R.id.action_settings:
                changeDisplaySettings();
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

    Button selectAllRestaurantsButton;
    Button deselectAllRestaurantsButton;
    Button deleteSelectedRestaurantsButton;
    Button exportSelectedRestaurantsButton;
    Button importRestaurantsButton;
    TextView restaurantId;
    DatabaseHandler databaseHandler;
    private ArrayList<Restaurant> restaurantList;
    private ListView mainRestaurantsListView;
    private Exporter exporter;
    private String m_selection;
    private String[] m_selectionArgs;
    private String m_orderBy;

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater myInflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        TableLayout layout = (TableLayout) myInflater.inflate(R.layout.main_activity, null);
        //layout.setBackgroundColor(getResources().getColor(R.color.egg_shell));
        setContentView(layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar);
        mainRestaurantsListView = findViewById(R.id.mainRestaurantsListView);
        databaseHandler = new DatabaseHandler(getApplicationContext());
        computeDisplayArgs();
        databaseHandler.setDisplay(m_selection, m_selectionArgs, m_orderBy);
        restaurantList = databaseHandler.getAllRestaurants();
        selectAllRestaurantsButton = findViewById(R.id.selectAllRestaurantsButton);
        deselectAllRestaurantsButton = findViewById(R.id.deselectAllRestaurantsButton);
        deleteSelectedRestaurantsButton = findViewById(
                R.id.deleteSelectedRestaurantsButton);
        exportSelectedRestaurantsButton = findViewById(
                R.id.exportSelectedRestaurantsButton);
        importRestaurantsButton = findViewById(R.id.importRestaurantsButton);
        addListeners();
        updateListView();
    }

    public
    void addListeners()
    {
        OnClickListener listener = (new OnClickListener()
        {

            @Override
            public
            void onClick(View view)
            {
                switch (view.getId())
                {
                    case R.id.selectAllRestaurantsButton:
                        allRestaurantsSelector(true);
                        break;
                    case R.id.deselectAllRestaurantsButton:
                        allRestaurantsSelector(false);
                        break;
                    case R.id.deleteSelectedRestaurantsButton:
                        deleteSelectedRestaurants();
                        break;
                    case R.id.exportSelectedRestaurantsButton:
                        exportSelectedRestaurants();
                        break;
                    case R.id.importRestaurantsButton:
                        importRestaurants();
                        break;
                }

            }
        });
        selectAllRestaurantsButton.setOnClickListener(listener);
        deselectAllRestaurantsButton.setOnClickListener(listener);
        deleteSelectedRestaurantsButton.setOnClickListener(listener);
        exportSelectedRestaurantsButton.setOnClickListener(listener);
        importRestaurantsButton.setOnClickListener(listener);
    }

    public
    void allRestaurantsSelector(Boolean selected)
    {
        if (restaurantList.size() < 1)
        {
            return;
        }
        for (int position = 0; position < mainRestaurantsListView.getChildCount(); position++)
        {
            CheckBox restaurantCheckBox = (CheckBox) mainRestaurantsListView.getChildAt(
                    position).findViewById(R.id.restaurantCheckBox);
            restaurantCheckBox.setChecked(selected);
        }
    }

    public
    void deleteSelectedRestaurants()
    {
        if (restaurantList.size() < 1)
        {
            return;
        }
        for (int position = 0; position < mainRestaurantsListView.getChildCount(); position++)
        {
            CheckBox restaurantCheckBox = mainRestaurantsListView.getChildAt(
                    position).findViewById(R.id.restaurantCheckBox);
            if (restaurantCheckBox.isChecked())
            {
                databaseHandler.deleteRestaurant(restaurantList.get(position).getRestaurantId());
            }
        }
        updateListView();
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

    public
    void exportSelectedRestaurants()
    {
        if (restaurantList.size() < 1)
        {
            return;
        }
        if (!externalStateWritable())
        {
            return;
        }
        for (int position = 0; position < mainRestaurantsListView.getChildCount(); position++)
        {
            CheckBox restaurantCheckBox = mainRestaurantsListView.getChildAt(
                    position).findViewById(R.id.restaurantCheckBox);
            if (restaurantCheckBox.isChecked())
            {
                if (exporter == null)
                {
                    exporter = new Exporter();
                }
                exporter.exportRestaurant((restaurantList.get(position)));
            }
        }
    }


    public
    void updateListView()
    {
        restaurantList = databaseHandler.getAllRestaurants();
        if (restaurantList.size() != 0)
        {
            mainRestaurantsListView = (findViewById(R.id.mainRestaurantsListView));
            mainRestaurantsListView.setOnItemClickListener(new OnItemClickListener()
            {

                public
                void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {

                    restaurantId = view.findViewById(R.id.restaurantId);

                    viewRestaurant(restaurantId.getText().toString());
                }
            });

            ListAdapter adapter = new RestaurantAdapter(this, restaurantList,
                                                        R.layout.restaurant_checkable_entry);
            mainRestaurantsListView.setAdapter(adapter);
        }
        else
        {
            mainRestaurantsListView.setAdapter(null);
        }
    }

    private
    void computeDisplayArgs()
    {
        DisplaySettings displaySettings = databaseHandler.getDisplaySettings("1");
        int selectedFoodTypesCount = databaseHandler.getSelectedFoodTypesCount();
        int allFoodTypesCount = databaseHandler.getAllFoodTypesCount();
        StringBuilder selection = new StringBuilder();
        if (selectedFoodTypesCount == 0)
        {
            selection.append("foodType='NONE'");
        }
        else if (selectedFoodTypesCount == allFoodTypesCount)
        {
            if (displaySettings.displayRestaurantsWithParkingOnly())
            {
                selection.append("restaurantParking=?");
                m_selectionArgs = new String[]{"Available"};
            }
        }

        else
        {
            int offset = (displaySettings.displayRestaurantsWithParkingOnly() ? 1 : 0);
            String equalityComparison;
            if (selectedFoodTypesCount < allFoodTypesCount / 2)
            {
                equalityComparison = "foodType = ? AND ";
                m_selectionArgs = databaseHandler.getFoodTypes(new String[]{"T"}, offset);
            }

            else
            {
                equalityComparison = "foodType != ? AND ";
                m_selectionArgs = databaseHandler.getFoodTypes(new String[]{"F"}, offset);
            }

            if (offset > 0)
            {
                selection.append("restaurantParking = ? AND ");
                m_selectionArgs[0] = "Available";
            }
            while (offset < m_selectionArgs.length)
            {
                selection.append(equalityComparison);
                offset++;
            }
            offset = selection.length();
            selection.delete(offset - 4, offset);
        }
        m_selection = (selection.length() > 0 ? selection.toString() : null);
        m_orderBy = displaySettings.getDisplayOption() + " " + displaySettings.getDisplayOrder();
    }
}
