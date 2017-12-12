package com.example.nc.androidphotos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nc.androidphotos.Model.*;
import com.example.nc.androidphotos.R;

import java.util.ArrayList;

/**
 * Created by Neil on 12/11/17.
 */

public class GridViewAdapter extends ArrayAdapter<Image> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Image> data = new ArrayList<Image>();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Image item = data.get(position);
        holder.imageTitle.setText(item.getTitle());
        if(item.getImageUri()!=null)holder.image.setImageURI(Uri.parse(item.getImageUri()));

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}
