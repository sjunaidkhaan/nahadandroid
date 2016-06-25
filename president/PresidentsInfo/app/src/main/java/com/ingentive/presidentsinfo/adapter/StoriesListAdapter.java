package com.ingentive.presidentsinfo.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ingentive.presidentsinfo.R;
import com.ingentive.presidentsinfo.activeandroid.PresidentsList;
import com.ingentive.presidentsinfo.activeandroid.StoriesList;

import java.util.List;

/**
 * Created by PC on 06-06-2016.
 */
public class StoriesListAdapter extends BaseAdapter {


    private List<StoriesList> storiesLists;
    private int res;
    private Context mContext;
    private static LayoutInflater inflater = null;

    public StoriesListAdapter(Context context, List<StoriesList> txtData, int rowId) {
        this.mContext = context;
        this.res = rowId;
        this.storiesLists = txtData;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.storiesLists.size();
    }

    @Override
    public Object getItem(int i) {
        return storiesLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(final int postion, View rowView, ViewGroup parent) {
        View vi = rowView;
        ViewHolder vh = new ViewHolder();
        if (vi == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.custom_row_text, parent, false);
            vh.tvTitle = (TextView) vi.findViewById(R.id.tv_text);
            int id = vi.generateViewId();
            vi.setId(id);
            vi.setTag(vh);
        } else {
            vh = (ViewHolder) vi.getTag();
        }
        final TextView tvTitle;
        vh.tvTitle.setText(storiesLists.get(postion).getsTitle());
        tvTitle = vh.tvTitle;
        return vi;
    }

    public class ViewHolder {
        TextView tvTitle;
    }
}
