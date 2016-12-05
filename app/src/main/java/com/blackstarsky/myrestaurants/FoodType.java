package com.blackstarsky.myrestaurants;

public
class FoodType
{
    String[] m_foodTypeFields;

    public
    FoodType()
    {
        m_foodTypeFields=new String[3];
    }

    public
    FoodType(String foodTypeId, String foodType, String display)
    {
        m_foodTypeFields=new String[3];
        m_foodTypeFields[0]=foodTypeId;
        m_foodTypeFields[1]=foodType;
        m_foodTypeFields[2]=display;
    }

    public
    void setFoodTypeId(String foodTypeId)
    {
        m_foodTypeFields[0]=foodTypeId;
    }

    public
    void setFoodType(String foodType)
    {
        m_foodTypeFields[1] = foodType;
    }

    public
    void setDisplay(String display)
    {
        m_foodTypeFields[2]=display;
    }

    public void setField(int fieldNumber,String field)
    {
        m_foodTypeFields[fieldNumber]=field;
    }

    public
    String getFoodTypeId()
    {
      return m_foodTypeFields[0];
    }

    public
    String getFoodType()
    {
        return m_foodTypeFields[1];
    }

    public
    String getDisplay(){return m_foodTypeFields[2];}

    public
    String getField(int fieldNumber)
    {
        return m_foodTypeFields[fieldNumber];
    }

    public
    boolean isChecked()
    {
        return m_foodTypeFields[2].equals("T");
    };
}
