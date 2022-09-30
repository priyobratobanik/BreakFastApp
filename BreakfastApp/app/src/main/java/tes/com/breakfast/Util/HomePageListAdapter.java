package tes.com.breakfast.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


import java.util.List;

import tes.com.breakfast.DTO.Items;
import tes.com.breakfast.R;

/

public class HomePageListAdapter extends ArrayAdapter<Items> {
    private List<Items> items;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView itemNameView;
        TextView itemPriceView;
        ImageView imageView;
    }

    public HomePageListAdapter(List<Items> items, Context context) {
        super(context, R.layout.home_activity_list_view_item, items);
        this.items = items;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Items dataModel = getItem(position);

        final HomePageListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        viewHolder = new HomePageListAdapter.ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.home_activity_list_view_item, parent, false);
        viewHolder.itemNameView = (TextView) convertView.findViewById(R.id.foodItemName);
        viewHolder.itemPriceView = (TextView) convertView.findViewById(R.id.foodItemPrice);
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.foodItemImage);

        result = convertView;

        convertView.setTag(viewHolder);

        lastPosition = position;
        if (dataModel != null) {
            viewHolder.itemNameView.setText(dataModel.getItemName());
            viewHolder.itemPriceView.setText(dataModel.getItemPrice());
            Picasso.get().load(dataModel.getItemImage()).resize(150, 150).centerCrop().into(viewHolder.imageView);
        }
        return convertView;
    }
}
