package com.ingentive.nahad.adapter;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingentive.nahad.R;
import com.ingentive.nahad.model.MenuModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2/4/2016.
 */
public class MenuAdapter extends BaseAdapter {

    private ProgressDialog pDialog;
    private List<MenuModel> itemList;
    private int res;
    private Context mContext;
    private static LayoutInflater inflater = null;

    public MenuAdapter(Context context, List<MenuModel> txtData, int rowId) {
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
        vh.tvItem.setText(itemList.get(postion).getItem());
        tvItem = vh.tvItem;



        return vi;
    }

    public class ViewHolder {
        TextView tvItem;
    }
}
