package com.example.finalexam.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.finalexam.R;

import java.util.List;

/**
 * Created by mshehab on 5/6/18.
 */

public class GiftsAdapter extends ArrayAdapter<Gift>{

    public GiftsAdapter(Context context, int resource, List<Gift> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gift gift = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){ //if no view to re-use then inflate a new one
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gift_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            viewHolder.textViewPrice = (TextView) convertView.findViewById(R.id.textViewPriceBudget);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //set the data from the gift object
        viewHolder.textViewName.setText(gift.name);
        viewHolder.textViewPrice.setText(String.valueOf(gift.price));
        return convertView;
    }

    //View Holder to cache the views
    private static class ViewHolder{
        TextView textViewName;
        TextView textViewPrice;
    }
}
