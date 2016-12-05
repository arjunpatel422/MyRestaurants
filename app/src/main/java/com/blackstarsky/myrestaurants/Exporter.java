package com.blackstarsky.myrestaurants;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public
class Exporter
{
    public
    Exporter()
    {

    }

    public
    void exportRestaurant(Restaurant restaurantInformation)
    {
        String[] restaurantFields = {"Restaurant Name", "Food Type", "Restaurant City",
                                     "Restaurant Address", "Restaurant Parking",
                                     "Restaurant Phone Number", "Date Visited", "Restaurant Rating",
                                     "Additional Comments"};
        String fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileLocation += "/Android/data/com.blackstarsky.myrestaurants/Exported Restaurants";
        File exportedRestaurantDirectory = new File(fileLocation);
        exportedRestaurantDirectory.mkdirs();
        fileLocation = "/" + restaurantInformation.getFoodType();
        File foodTypeDirectory = new File(exportedRestaurantDirectory, fileLocation);
        foodTypeDirectory.mkdir();
        fileLocation = "/" + restaurantInformation.getRestaurantName() + ".txt";
        File exportedRestaurant = new File(foodTypeDirectory, fileLocation);

        FileOutputStream fileWriter = null;
        try
        {
            fileWriter = new FileOutputStream(exportedRestaurant, false);
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (fileWriter == null)
        {
            return;
        }
        String writtenRestaurant = "Restaurant Information";
        OutputStreamWriter writer = new OutputStreamWriter(fileWriter);
        appendToFile(writer, writtenRestaurant);
        for (int position = 0; position < restaurantFields.length; position++)
        {
            writtenRestaurant = restaurantFields[position];
            appendToFile(writer, writtenRestaurant);
            if (writableField(restaurantInformation.getField(position + 1)))
            {
                writtenRestaurant = restaurantInformation.getField(position + 1);
            }
            else
            {
                writtenRestaurant = "Unknown";
            }
            appendToFile(writer, writtenRestaurant);
        }
        try
        {
            writer.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try
        {
            fileWriter.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public
    boolean writableField(String field)
    {
        for (int position = 0; position < field.length(); position++)
        {
            Character testedCharacter = field.charAt(position);
            switch (testedCharacter)
            {
                case ' ':
                    break;
                default:
                    return true;
            }
        }
        return false;
    }

    public
    void appendToFile(OutputStreamWriter writer, String text)
    {
        try
        {
            writer.write(text);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try
        {
            writer.flush();
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try
        {
            writer.write("\r\n");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try
        {
            writer.flush();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
