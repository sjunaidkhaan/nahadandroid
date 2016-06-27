package com.ingentive.nahad.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingentive.nahad.R;
import com.ingentive.nahad.model.TableOfContentsSubChildrenModel;

import java.util.List;

/**
 * Created by PC on 06-05-2016.
 */
public class ShowDialogAdapter extends BaseAdapter {

    private List<TableOfContentsSubChildrenModel> itemList;
    private int res;
    private Context mContext;
    private static LayoutInflater inflater = null;

    public ShowDialogAdapter(Context context, List<TableOfContentsSubChildrenModel> txtData, int rowId) {
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
            vi = inflater.inflate(R.layout.custom_row_contents_sub_child, parent, false);
            vh.tv_sub_child = (TextView) vi.findViewById(R.id.tv_sub_child);
            int id = vi.generateViewId();
            vi.setId(id);
            vi.setTag(vh);
        } else {
            vh = (ViewHolder) vi.getTag();
        }
        final TextView tvItem;
        final ImageView iv_sub_child;

        tvItem = vh.tv_sub_child;
        iv_sub_child =vh.iv_sub_child;

        vh.tv_sub_child.setText(itemList.get(postion).getName());

        vh.iv_sub_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemList.get(postion).isChecked()==false){
                    itemList.get(postion).setChecked(true);
                    iv_sub_child.setBackgroundResource(R.drawable.checkbox_checked);
                }else {
                    iv_sub_child.setBackgroundResource(R.drawable.checkbox_unchecked);
                    itemList.get(postion).setChecked(false);
                }
            }
        });

        return vi;
    }

    public class ViewHolder {
        TextView tv_sub_child;
        ImageView iv_sub_child;
    }
}
