package com.blackstarsky.myrestaurants;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public
class ImportRestaurants extends BaseActivity
{

    public
    boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.import_restaurants_menu, menu);
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

    DatabaseHandler databaseHandler;
    Button selectAllFilesButton;
    Button deselectAllFilesButton;
    Button importSelectedFilesAsRestaurantsButton;
    private ArrayList<File> allTextFiles;
    private ArrayList<HashMap<String, String>> allFileNames;
    private ListView importRestaurantsListView;

    protected
    void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        databaseHandler = new DatabaseHandler(this);
        setContentView(R.layout.import_restaurants);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar);
        importRestaurantsListView = (ListView) findViewById(R.id.importRestaurantsListView);
        selectAllFilesButton = (Button) findViewById(R.id.selectAllFilesButton);
        deselectAllFilesButton = (Button) findViewById(R.id.deselectAllFilesButton);
        importSelectedFilesAsRestaurantsButton = (Button) findViewById(
                R.id.importSelectedFilesAsRestaurantsButton);
        if (!externalStateReadable())
        {
            allTextFiles = new ArrayList<>();
        }
        else
        {
            addListeners();
        }
        updateListView();
    }

    public
    boolean externalStateReadable()
    {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        if (storageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            return true;
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
    void getAllTextFiles()
    {
        String fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileLocation += "/Android/data/com.blackstarysky.myrestaurants/Imported Restaurants";
        File importedRestaurantDirectory = new File(fileLocation);
        importedRestaurantDirectory.mkdirs();
        allTextFiles = getAllFilesFromDirectory(importedRestaurantDirectory);
        if (allTextFiles.size() < 1)
        {
            return;
        }
        allFileNames = new ArrayList<HashMap<String, String>>();
        for (File file : allTextFiles)
        {
            HashMap<String, String> fileName = new HashMap<String, String>();
            fileName.put("fileName", file.getName());
            allFileNames.add(fileName);
        }
    }

    public
    ArrayList<File> getAllFilesFromDirectory(File directory)
    {
        ArrayList<File> allTextFiles = new ArrayList<>();
        ArrayList<File> directories=new ArrayList<>();
        directories.add(directory);
        for(int position=0;position<directories.size();position++)
        {
            File[] foundFiles=directories.get(position).listFiles();
            for (File currentFile : foundFiles)
            {
                if (currentFile.isDirectory())
                {
                    directories.add(currentFile);
                }
                else if (currentFile.getName().endsWith(".txt"))
                {
                    allTextFiles.add(currentFile);
                }
            }
        }
        return allTextFiles;
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
                    case R.id.selectAllFilesButton:
                        allFilesSelector(true);
                        break;
                    case R.id.deselectAllFilesButton:
                        allFilesSelector(false);
                        break;
                    case R.id.importSelectedFilesAsRestaurantsButton:
                        importSelectedFilesAsRestaurants();
                        break;
                }

            }
        });
        selectAllFilesButton.setOnClickListener(listener);
        deselectAllFilesButton.setOnClickListener(listener);
        importSelectedFilesAsRestaurantsButton.setOnClickListener(listener);
    }

    public
    void allFilesSelector(Boolean selected)
    {
        if (allTextFiles.size() < 1)
        {
            return;
        }
        for (int position = 0; position < importRestaurantsListView.getChildCount(); position++)
        {
            CheckBox fileCheckBox = (CheckBox) importRestaurantsListView.getChildAt(
                    position).findViewById(R.id.fileCheckBox);
            fileCheckBox.setChecked(selected);
        }
    }

    public
    void importSelectedFilesAsRestaurants()
    {
        if (allTextFiles.size() < 1)
        {
            return;
        }
        for (int position = 0; position < importRestaurantsListView.getChildCount(); position++)
        {
            CheckBox fileCheckBox = (CheckBox) importRestaurantsListView.getChildAt(
                    position).findViewById(R.id.fileCheckBox);
            if (fileCheckBox.isChecked())
            {
                importSelectedFileAsRestaurant(allTextFiles.get(position));
            }
        }
        callMainActivity();
    }

    public
    void importSelectedFileAsRestaurant(File importedRestaurantFile)
    {
        String[] restaurantFields = {"restaurantName", "foodType", "restaurantCity",
                                     "restaurantAddress", "restaurantParking",
                                     "restaurantPhoneNumber", "dateVisited", "restaurantRating",
                                     "additionalComments"};
        try
        {
            BufferedReader lineReader = new BufferedReader(new FileReader(importedRestaurantFile));
            int lineNumber = 1;
            int position = 1;
            Restaurant restaurant = new Restaurant();
            for (String line = lineReader.readLine(); line != null; line = lineReader.readLine())
            {
                switch (lineNumber)
                {
                    case 3:
                    case 5:
                    case 7:
                    case 9:
                    case 11:
                    case 13:
                    case 15:
                    case 17:
                    case 19:
                        restaurant.setField(position, line);
                        position++;
                        break;
                    default:
                        break;
                }
                lineNumber++;
            }
            lineReader.close();
            for (position = 0; position < restaurantFields.length; position++)
            {
                if (restaurant.getField(position + 1).equalsIgnoreCase("Unknown"))
                {
                    restaurant.setField(position + 1, "");
                }
            }
            databaseHandler.insertRestaurant(restaurant);

        }
        catch (IOException e)
        {
            e.printStackTrace();
            callMainActivity();
        }
    }

    public
    void updateListView()
    {
        if (!externalStateReadable())
        {
            return;
        }
        getAllTextFiles();
        if (allTextFiles.size() > 0)
        {

            importRestaurantsListView = (ListView) (findViewById(R.id.importRestaurantsListView));
            // A list adapter is used bridge between a ListView and
            // the ListViews data

            // The SimpleAdapter connects the data in an ArrayList
            // to the XML file

            // First we pass in a Context to provide information needed
            // about the application
            // The ArrayList of data is next followed by the xml resource
            // Then we have the names of the data in String format and
            // their specific resource ids
            ListAdapter adapter = new SimpleAdapter(ImportRestaurants.this, allFileNames,
                                                    R.layout.file_entry,
                                                    new String[]{"fileName", "fileCheckBox"},
                                                    new int[]{R.id.fileName, R.id.fileCheckBox});
            importRestaurantsListView.setAdapter(adapter);
        }
    }

}
