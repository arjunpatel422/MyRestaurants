package com.blackstarsky.myrestaurants;

/**
 * Created by Arjun on 6/14/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Locale;

public
class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String dataTable1 = "CREATE TABLE if not exists restaurants ( restaurantId INTEGER PRIMARY KEY, restaurantName TEXT,foodType TEXT, restaurantCity TEXT, restaurantAddress TEXT, restaurantParking TEXT, restaurantPhoneNumber TEXT, dateVisited TEXT, restaurantRating TEXT, additionalComments TEXT)";

    private static final String dataTable2 = "CREATE TABLE if not exists displaySettings(displaySettingsId INTEGER PRIMARY Key, displayRestaurantsWithParkingOnly Text, displayOption Text, displayOrder Text)";

    private static final String dataTable3 = "CREATE TABLE if not exists searchTable ( restaurantId INTEGER PRIMARY KEY, restaurantName TEXT, foodType TEXT, restaurantCity TEXT, restaurantAddress TEXT, restaurantParking TEXT, " + "restaurantPhoneNumber TEXT, dateVisited TEXT, restaurantRating TEXT, additionalComments TEXT)";

    private static final String dataTable4=  "CREATE TABLE if not exists displayedFoodTypes (foodTypeId INTEGER PRIMARY KEY, foodType TEXT, displayFoodType CHAR(1))";

    private static final String[] displayFields = {"displaySettingsId",
                                                   "displayRestaurantsWithParkingOnly",
                                                   "displayOption",
                                                   "displayOrder"};

    private static final String[] restaurantFields = {"restaurantId", "restaurantName", "foodType",
                                                      "restaurantCity", "restaurantAddress",
                                                      "restaurantParking", "restaurantPhoneNumber",
                                                      "dateVisited", "restaurantRating",
                                                      "additionalComments"};

    private static final String[] displayedFoodTypeFields={"foodTypeId","foodType","displayFoodType"};

    private String[] foodTypes;
    private Cursor m_cursor;
    private String m_selection;
    private String[] m_selectionArgs;
    private String m_orderBy;

    public
    DatabaseHandler(Context applicationContext)
    {
        super(applicationContext, "restaurantbook.db", null, 1);
        if(applicationContext!=null && foodTypes==null)
            foodTypes=applicationContext.getResources().getStringArray(R.array.food_type);
    }

    @Override
    public
    void onCreate(SQLiteDatabase database)
    {
        database.execSQL(dataTable1);
        database.execSQL(dataTable2);
        database.execSQL(dataTable3);
        database.execSQL(dataTable4);
        initializeDisplaySettings(database);
    }

    @Override
    public
    void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        //String query = "DROP TABLE IF EXISTS restaurants; Drop Table if Exists displaySettings ;Drop Table if Exists searchTable";
        onCreate(database);
        database.close();
    }

    public
    void insertRestaurant(Restaurant restaurant)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        ContentValues searchValues = new ContentValues();
        for (int position = 1; position < restaurantFields.length; position++)
        {
            String original = restaurant.getField(position);
            values.put(restaurantFields[position], original);
            String modified = original.trim().toLowerCase(Locale.US);
            searchValues.put(restaurantFields[position], modified);
        }
        database.insert("restaurants", null, values);
        database.insert("searchTable", null, searchValues);
        database.close();
    }

    public
    int updateRestaurant(Restaurant restaurant)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        ContentValues searchValues = new ContentValues();
        for (int position = 1; position < restaurantFields.length; position++)
        {
            String original = restaurant.getField(position);
            values.put(restaurantFields[position], original);
            String modified= original.trim().toLowerCase(Locale.US);
            modified = modified.toLowerCase(Locale.US);
            searchValues.put(restaurantFields[position], modified);
        }
        database.update("restaurants", values, restaurantFields[0] + " = ?",
                        new String[]{restaurant.getRestaurantId()});
        return database.update("searchTable", searchValues, restaurantFields[0] + " = ?",
                               new String[]{restaurant.getRestaurantId()});
    }

    public
    void deleteRestaurant(String id)
    {

        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM restaurants WHERE restaurantId='" + id+"'";
        database.execSQL(deleteQuery);
        deleteQuery="DELETE FROM searchTable WHERE restaurantId='" + id + "'";
        database.execSQL(deleteQuery);
        database.close();
    }

    public
    ArrayList<Restaurant> getAllRestaurants()
    {
        ArrayList<Restaurant> restaurantArrayList = new ArrayList<Restaurant>();
        SQLiteDatabase database = this.getWritableDatabase();
        m_cursor=database.query("restaurants", null, m_selection, m_selectionArgs, null, null, m_orderBy);
        Restaurant restaurant;
        for (Boolean b = m_cursor.moveToFirst(); b; b = m_cursor.moveToNext())
        {
            restaurant = new Restaurant();
            for (int position = 0; position < restaurantFields.length; position++)
            {
                restaurant.setField(position, m_cursor.getString(position));
            }
            restaurantArrayList.add(restaurant);
        }
        database.close();
        m_cursor.close();
        return restaurantArrayList;

    }

    public
    Restaurant getRestaurantInfo(String id)
    {
        Restaurant restaurant = new Restaurant();

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM restaurants WHERE restaurantId='" + id + "'";

        m_cursor = database.rawQuery(selectQuery, null);

        for (Boolean successfulMove = m_cursor.moveToFirst(); successfulMove; successfulMove = m_cursor.moveToNext())
        {
            for (int position = 0; position < restaurantFields.length; position++)
            {
                restaurant.setField(position, m_cursor.getString(position));
            }
        }
        database.close();
        m_cursor.close();
        return restaurant;
    }

    public
    void initializeDisplaySettings(SQLiteDatabase database)
    {
        String selectQuery = "SELECT * FROM displaySettings";
        m_cursor = database.rawQuery(selectQuery, null);
        ContentValues values=new ContentValues();
        if (!m_cursor.moveToFirst())
        {
            values.put(displayFields[1], "F");
            values.put(displayFields[2], "restaurantName");
            values.put(displayFields[3], "ASC");
            database.insert("displaySettings", null, values);
        }
        selectQuery="SELECT * FROM displayedFoodTypes";
        m_cursor = database.rawQuery(selectQuery, null);
        values.clear();
        if(!m_cursor.moveToFirst())
        {
            for(String foodType: foodTypes)
            {
                values.put(displayedFoodTypeFields[1],foodType);
                values.put(displayedFoodTypeFields[2],"T");
                database.insert("displayedFoodTypes",null,values);
                values.clear();
            }
        }
        //do not close the database or it becomes permanently locked causing an error
        m_cursor.close();
    }

    public
    int updateDisplaySettings(DisplaySettings displaySettings)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for (int position = 0; position < displayFields.length; position++)
        {
            values.put(displayFields[position], displaySettings.getField(position));
        }
        return database.update("displaySettings", values,
                               displayFields[0] + " = " + displaySettings.getDisplaySettingsId(),
                               null);
    }

    public int updateDisplayFoodTypes(FoodType[] displayedFoodTypes)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(FoodType foodType: displayedFoodTypes)
        {
            for(int position=0;position<displayedFoodTypeFields.length;position++)
            {
                values.put(displayedFoodTypeFields[position],foodType.getField(position));
            }
            database.update("displayedFoodTypes",values,displayedFoodTypeFields[0]+" = " + foodType.getFoodTypeId(),null);
        }
        return 0;
    }

    public
    DisplaySettings getDisplaySettings(String id)
    {
        DisplaySettings displaySettings = new DisplaySettings();
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM displaySettings WHERE displaySettingsId='" + id + "'";
        m_cursor = database.rawQuery(selectQuery, null);
        for (Boolean b = m_cursor.moveToFirst(); b; b = m_cursor.moveToNext())
        {
            for (int position = 0; position < displayFields.length; position++)
            {
                displaySettings.setField(position, m_cursor.getString(position));
            }
        }
        database.close();
        m_cursor.close();
        return displaySettings;

    }

    public
    FoodType[] getDisplayedFoodTypes()
    {
        SQLiteDatabase database=this.getReadableDatabase();
        String query="SELECT * FROM displayedFoodTypes order by foodType";
        m_cursor = database.rawQuery(query, null);
        FoodType[] displayedFoodTypes=new FoodType[m_cursor.getCount()];
        int foodTypeId=0;
        FoodType foodType;
        for (Boolean b = m_cursor.moveToFirst(); b; b = m_cursor.moveToNext())
        {
            foodType=new FoodType();
            for (int position =0;position < displayedFoodTypeFields.length; position++)
            {
                foodType.setField(position, m_cursor.getString(position));
            }
            displayedFoodTypes[foodTypeId++]=foodType;
        }
        database.close();
        m_cursor.close();
        return displayedFoodTypes;
    }

    public
    ArrayList<Restaurant> getAllSearchResults(String searchTerm)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        searchTerm = searchTerm.trim().toLowerCase(Locale.US);
        for(int position=0;position<restaurantFields.length-2;position++)
        {
            m_selectionArgs[position]=searchTerm;
        }
        ArrayList<Restaurant> searchResults=new ArrayList<Restaurant>();
        m_cursor=database.query("searchTable", null, m_selection, m_selectionArgs, null, null, m_orderBy);
        Restaurant restaurant;
        for (Boolean b = m_cursor.moveToFirst(); b; b = m_cursor.moveToNext())
        {
            restaurant = new Restaurant();
            for (int position = 0; position < restaurantFields.length; position++)
            {
                restaurant.setField(position, m_cursor.getString(position));
            }
            searchResults.add(restaurant);
        }
        database.close();
        m_cursor.close();
        return searchResults;
    }

    public int getSelectedFoodTypesCount()
    {
        SQLiteDatabase database = this.getReadableDatabase();
        m_cursor = database.rawQuery("Select displayFoodType from displayedFoodTypes where displayFoodType=?", new String[]{"T"});
        int result=m_cursor.getCount();
        database.close();
        m_cursor.close();
        return result;
    }

    public int getAllFoodTypesCount()
    {
        return foodTypes.length;
    }

    public String[] getFoodTypes(String[] selected,int offset)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        m_cursor=database.rawQuery("Select foodType,displayFoodType from displayedFoodTypes where displayFoodType=?",selected);
        String[] selectedFoodTypes=new String[offset+m_cursor.getCount()];
        for (Boolean b = m_cursor.moveToFirst(); b; b = m_cursor.moveToNext())
        {
            selectedFoodTypes[offset++]=m_cursor.getString(0);
        }
        database.close();
        m_cursor.close();
        return selectedFoodTypes;
    }

    public void setDisplay(String selection,String[] selectionArgs,String orderBy)
    {
        m_selection=selection;
        m_selectionArgs=selectionArgs;
        m_orderBy=orderBy;
    }

}
