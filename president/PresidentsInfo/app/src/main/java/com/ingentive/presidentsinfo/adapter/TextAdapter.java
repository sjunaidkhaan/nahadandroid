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
import com.ingentive.presidentsinfo.model.TextModel;

import java.util.List;

/**
 * Created by PC on 24-05-2016.
 */
public class TextAdapter extends BaseAdapter {


    private List<TextModel> itemList;
    private int res;
    private Context mContext;
    private static LayoutInflater inflater = null;

    public TextAdapter(Context context, List<TextModel> txtData, int rowId) {
        this.mContext = context;
        this.res = rowId;
        this.itemList = txtData;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
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
            vh.tvItem = (TextView) vi.findViewById(R.id.tv_text);
            int id = vi.generateViewId();
            vi.setId(id);
            vi.setTag(vh);
        } else {
            vh = (ViewHolder) vi.getTag();
        }
        final TextView tvItem;
        vh.tvItem.setText(itemList.get(postion).getText());
        tvItem = vh.tvItem;
        return vi;
    }

    public class ViewHolder {
        TextView tvItem;
    }
}
