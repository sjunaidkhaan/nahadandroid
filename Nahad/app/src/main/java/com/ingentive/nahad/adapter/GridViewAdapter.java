package com.ingentive.nahad.adapter;

/**
 * Created by PC on 03-06-2016.
 */
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.ingentive.nahad.R;
import com.ingentive.nahad.model.GridViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 03-06-2016.
 */
public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<GridViewModel> data = new ArrayList<GridViewModel>();

    public GridViewAdapter(Context context, int layoutResourceId, List<GridViewModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        //final ImageItem item = data.get(position);
        holder.image.setImageBitmap(data.get(position).getImage());
        return row;
    }

    public class ViewHolder {
        ImageView image;
    }
}
