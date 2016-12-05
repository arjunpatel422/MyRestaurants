/*
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

public class FoodTypeAdapter extends ArrayAdapter<FoodType>
{
    private final Context m_context;
    private final ArrayList<FoodType> m_foodTypeArrayList;
    private int m_layoutId;

    public FoodTypeAdapter(Context context, ArrayList<FoodType> foodTypeArrayList, int layoutId)
    {
        super(context,layoutId, foodTypeArrayList);
        m_layoutId=layoutId;
        m_context= context;
        m_foodTypeArrayList=foodTypeArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        FoodType foodType=m_foodTypeArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) m_context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(m_layoutId, parent, false);
        if(m_layoutId==R.layout.restaurant_checkable_entry)
        {
            TextView restaurantId = (TextView) view.findViewById(R.id.restaurantId);
            TextView restaurantName = (TextView) view.findViewById(R.id.restaurantName);
            TextView foodTypeTextView=(TextView) view.findViewById(R.id.foodType);
            RatingBar restaurantRatingBar=(RatingBar)view.findViewById(R.id.restaurantRatingBar);
            CheckBox restaurantCheckBox=(CheckBox)view.findViewById(R.id.restaurantCheckBox);
            restaurantId.setText(restaurant.getRestaurantId());
            restaurantName.setText(restaurant.getRestaurantName());
            foodType.setText(restaurant.getFoodType());
            restaurantRatingBar.setRating(Float.parseFloat(restaurant.getRestaurantRating()));
            restaurantCheckBox.setChecked(false);
            restaurantRatingBar.setIsIndicator(true);
            foodType.setTextColor(m_context.getResources().getColor(R.color.aero_blue));
        }

        if(m_layoutId==R.layout.restaurant_entry)
        {
            TextView restaurantId = (TextView) view.findViewById(R.id.restaurantId);
            TextView restaurantName = (TextView) view.findViewById(R.id.restaurantName);
            TextView foodTypeTextView=(TextView) view.findViewById(R.id.foodType);
            RatingBar restaurantRatingBar=(RatingBar)view.findViewById(R.id.restaurantRatingBar);
            restaurantId.setText(restaurant.getRestaurantId());
            restaurantName.setText(restaurant.getRestaurantName());
            foodType.setText(restaurant.getFoodType());
            restaurantRatingBar.setRating(Float.parseFloat(restaurant.getRestaurantRating()));
            restaurantRatingBar.setIsIndicator(true);
        }
        return view;
    }
}
*/
