package com.blackstarsky.myrestaurants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public
class RestaurantAdapter extends ArrayAdapter<Restaurant>
{
    private final Context m_context;
    private final ArrayList<Restaurant> m_restaurantArrayList;
    private int m_layoutId;

    public
    RestaurantAdapter(Context context, ArrayList<Restaurant> restaurantArrayList, int layoutId)
    {

        super(context, layoutId, restaurantArrayList);
        m_layoutId = layoutId;
        m_context = context;
        m_restaurantArrayList = restaurantArrayList;
    }

    @Override
    public
    View getView(int position, View convertView, ViewGroup parent)
    {
        Restaurant restaurant = m_restaurantArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(m_layoutId, parent, false);
        if (m_layoutId == R.layout.restaurant_checkable_entry)
        {
            TextView restaurantId = (TextView) view.findViewById(R.id.restaurantId);
            TextView restaurantName = (TextView) view.findViewById(R.id.restaurantName);
            TextView foodType = (TextView) view.findViewById(R.id.foodType);
            RatingBar restaurantRatingBar = (RatingBar) view.findViewById(R.id.restaurantRatingBar);
            CheckBox restaurantCheckBox = (CheckBox) view.findViewById(R.id.restaurantCheckBox);
            restaurantId.setText(restaurant.getRestaurantId());
            restaurantName.setText(restaurant.getRestaurantName());
            foodType.setText(restaurant.getFoodType());
            restaurantRatingBar.setRating(Float.parseFloat(restaurant.getRestaurantRating()));
            restaurantCheckBox.setChecked(false);
            restaurantRatingBar.setIsIndicator(true);
            foodType.setTextColor(m_context.getResources().getColor(R.color.aero_blue));
        }

        if (m_layoutId == R.layout.restaurant_entry)
        {
            TextView restaurantId = (TextView) view.findViewById(R.id.restaurantId);
            TextView restaurantName = (TextView) view.findViewById(R.id.restaurantName);
            TextView foodType = (TextView) view.findViewById(R.id.foodType);
            RatingBar restaurantRatingBar = (RatingBar) view.findViewById(R.id.restaurantRatingBar);
            restaurantId.setText(restaurant.getRestaurantId());
            restaurantName.setText(restaurant.getRestaurantName());
            foodType.setText(restaurant.getFoodType());
            restaurantRatingBar.setRating(Float.parseFloat(restaurant.getRestaurantRating()));
            restaurantRatingBar.setIsIndicator(true);
        }
        return view;
    }
}


