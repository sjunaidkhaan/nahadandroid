package com.ingentive.nahad.adapter;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ingentive.nahad.R;
import com.ingentive.nahad.model.AddFilesModel;
import com.ingentive.nahad.model.MenuModel;

import java.util.List;

/**
 * Created by PC on 06-05-2016.
 */
public class MainMenuAdapter extends BaseAdapter {

    private ProgressDialog pDialog;
    private List<AddFilesModel> itemList;
    private int res;
    private Context mContext;
    private static LayoutInflater inflater = null;

    public MainMenuAdapter(Context context, List<AddFilesModel> txtData, int rowId) {
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
            vi = inflater.inflate(R.layout.custom_row_menu, parent, false);
            vh.tvItem = (TextView) vi.findViewById(R.id.tv_item);
            int id = vi.generateViewId();
            vi.setId(id);
            vi.setTag(vh);
        } else {
            vh = (ViewHolder) vi.getTag();
        }
        final TextView tvItem;
        vh.tvItem.setText(itemList.get(postion).getfTitle());
        tvItem = vh.tvItem;
        return vi;
    }
    public class ViewHolder {
        TextView tvItem;
    }
}
