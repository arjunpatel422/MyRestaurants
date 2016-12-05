package com.blackstarsky.myrestaurants;

public
class DisplaySettings
{
    private String[] m_displaySettingsFields;

    public
    DisplaySettings()
    {
        m_displaySettingsFields = new String[4];
        m_displaySettingsFields[0] = "1";
    }

    public
    String getDisplaySettingsId()
    {
        return m_displaySettingsFields[0];
    }

    public
    String getDisplayRestaurantsWithParkingOnly()
    {
        return m_displaySettingsFields[1];
    }

    public
    String getDisplayOption()
    {
        return m_displaySettingsFields[2];
    }

    public
    String getDisplayOrder()
    {
        return m_displaySettingsFields[3];
    }

    public
    String getField(int fieldNumber)
    {
        return m_displaySettingsFields[fieldNumber];
    }

    public
    void setDisplayId(String displaySettingsId)
    {
        m_displaySettingsFields[0] = displaySettingsId;
    }

    public
    void setDisplayRestaurantsWithParkingOnly(String displayRestaurantsWithParkingOnly)
    {
        m_displaySettingsFields[1] = displayRestaurantsWithParkingOnly;
    }

    public
    void setDisplayOption(String displayOption)
    {
        m_displaySettingsFields[2] = displayOption;
    }

    public
    void setDisplayOrder(String displayOrder)
    {
        m_displaySettingsFields[3] = displayOrder;
    }


    public
    void setField(int fieldNumber, String field)
    {
        m_displaySettingsFields[fieldNumber] = field;
    }

    public
    boolean displayRestaurantsWithParkingOnly(){return m_displaySettingsFields[1].equals("T");}
}

