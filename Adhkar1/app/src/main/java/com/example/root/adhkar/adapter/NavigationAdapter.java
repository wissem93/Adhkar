package com.example.root.adhkar.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.adhkar.R;
import com.example.root.adhkar.classobject.ItemMenu;

import java.util.ArrayList;

/**
 * Created by root on 22/07/15.
 */
public class NavigationAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<ItemMenu> arrayitems;


    public NavigationAdapter(Activity activity, ArrayList<ItemMenu> arrayitems) {
        super();
        this.activity = activity;
        this.arrayitems = arrayitems;
    }
    @Override
    public Object getItem(int position)
    {
        return arrayitems.get(position);
    }
    @Override
    public int getCount() {
        return this.arrayitems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView item;
        ImageView icon;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater myInflater=activity.getLayoutInflater();
        if (convertView == null)
        {
            convertView = myInflater.inflate(R.layout.drawer_item_list, null);
            holder = new ViewHolder();
        ItemMenu itm=arrayitems.get(position);
holder.item=(TextView)convertView.findViewById(R.id.label);
            holder.item.setText(itm.getTitle());
            holder.icon=(ImageView) convertView.findViewById(R.id.imageicon);
            holder.icon.setImageResource(itm.getIcon());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;

    }


}
