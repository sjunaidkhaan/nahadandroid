package com.ingentive.nahad.adapter;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingentive.nahad.R;
import com.ingentive.nahad.activeandroid.AddFilesModel;
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
            vh.custom_row_menu_layout = (LinearLayout) vi.findViewById(R.id.custom_row_menu_layout);
            vh.iv_radio = (ImageView) vi.findViewById(R.id.iv_radio);
            int id = vi.generateViewId();
            vi.setId(id);
            vi.setTag(vh);
        } else {
            vh = (ViewHolder) vi.getTag();
        }
        final TextView tvItem;
        final ImageView iv_radio;
        final LinearLayout custom_row_menu_layout;
        vh.tvItem.setText(itemList.get(postion).getFileTitle());
        tvItem = vh.tvItem;
        iv_radio = vh.iv_radio;
        custom_row_menu_layout = vh.custom_row_menu_layout;
        iv_radio.setBackgroundResource(R.drawable.bullseye);

        vh.custom_row_menu_layout.setOnTouchListener(
                new TextView.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                custom_row_menu_layout.setBackgroundResource(R.drawable.rectangle_fourteen);
                                iv_radio.setBackgroundResource(R.drawable.radio);
                                tvItem.setTextColor(Color.WHITE);
                                break;
                            case MotionEvent.ACTION_UP:
                                custom_row_menu_layout.setBackgroundResource(R.drawable.rectangle_four_copy);
                                iv_radio.setBackgroundResource(R.drawable.bullseye);
                                break;
                        }
                        return false;
                    }
                }
        );
        return vi;
    }

    public class ViewHolder {
        TextView tvItem;
        LinearLayout custom_row_menu_layout;
        ImageView iv_radio;
    }
}
