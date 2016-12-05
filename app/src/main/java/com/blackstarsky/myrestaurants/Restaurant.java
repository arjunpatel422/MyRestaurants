package com.blackstarsky.myrestaurants;

/**
 * Created by Arjun on 6/13/15.
 */
public
class Restaurant
{
    private String[] m_restaurantFields;

    public
    Restaurant()
    {
        m_restaurantFields = new String[10];
    }

    public
    String getRestaurantId()
    {
        return m_restaurantFields[0];
    }

    public
    String getRestaurantName()
    {
        return m_restaurantFields[1];
    }

    public
    String getFoodType()
    {
        return m_restaurantFields[2];
    }

    public
    String getRestaurantCity()
    {
        return m_restaurantFields[3];
    }

    public
    String getRestaurantAddress()
    {
        return m_restaurantFields[4];
    }

    public
    String getRestaurantParking()
    {
        return m_restaurantFields[5];
    }

    public
    String getRestaurantPhoneNumber()
    {
        return m_restaurantFields[6];
    }

    public
    String getDateVisited()
    {
        return m_restaurantFields[7];
    }

    public
    String getRestaurantRating()
    {
        return m_restaurantFields[8];
    }

    public
    String getAdditionalComments()
    {
        return m_restaurantFields[9];
    }

    public
    String getField(int fieldNumber)
    {
        return m_restaurantFields[fieldNumber];
    }

    public
    void setRestaurantId(String restaurantId)
    {
        m_restaurantFields[0] = restaurantId;
    }

    public
    void setRestaurantName(String restaurantName)
    {
        m_restaurantFields[1] = restaurantName;
    }

    public
    void setFoodType(String foodType)
    {
        m_restaurantFields[2] = foodType;
    }

    public
    void setRestaurantCity(String restaurantCity)
    {
        m_restaurantFields[3] = restaurantCity;
    }

    public
    void setRestaurantAddress(String restaurantAddress)
    {
        m_restaurantFields[4] = restaurantAddress;
    }

    public
    void setRestaurantParking(String restaurantParking)
    {
        m_restaurantFields[5] = restaurantParking;
    }

    public
    void setRestaurantPhoneNumber(String restaurantPhoneNumber)
    {
        m_restaurantFields[6] = restaurantPhoneNumber;
    }

    public
    void setDateVisited(String dateVisited)
    {
        m_restaurantFields[7] = dateVisited;
    }

    public
    void setRestaurantRating(String restaurantRating)
    {
        m_restaurantFields[8] = restaurantRating;
    }

    public
    void setAdditionalComments(String additionalComments)
    {
        m_restaurantFields[9] = additionalComments;
    }

    public
    void setField(int fieldNumber, String field)
    {
        m_restaurantFields[fieldNumber] = field;
    }

    public
    boolean isParkingAvailable(){return m_restaurantFields[5].equals("Available");}
}
