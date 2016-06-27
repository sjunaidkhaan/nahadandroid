package com.ingentive.nahad.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.ingentive.nahad.R;
import com.ingentive.nahad.activeandroid.BookMarkModel;
import com.ingentive.nahad.activeandroid.SendEmailModel;
import com.ingentive.nahad.activity.BookInsideActivity;
import com.ingentive.nahad.model.GridViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 23-06-2016.
 */
public class BookMarkGridViewAdapter extends BaseAdapter {
    private Context mContext;

    private int layoutResourceId;
    private List<GridViewModel> data = new ArrayList<GridViewModel>();


    public BookMarkGridViewAdapter(Context context, int layoutResourceId, List<GridViewModel> data) {
        // super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //View grid;
        View vi =convertView;
        ViewHolder vh = new ViewHolder();

        if (vi == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.grid_single, null);
            vh.button = (Button)vi.findViewById(R.id.btn_remove);
            vh.imageView = (ImageView)vi.findViewById(R.id.grid_image);
            int id = vi.generateViewId();
            vi.setId(id);
            vi.setTag(vh);
        } else {
            vh = (ViewHolder) vi.getTag();
        }
        final ImageView imageView;
        final Button button;
        imageView = vh.imageView;
        button = vh.button;

        vh. imageView.setImageBitmap(data.get(position).getImage());
        vh.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Delete().from(BookMarkModel.class).where("page_no=?", data.get(position).getPageNo()).execute();
                data.remove(position);
                notifyDataSetChanged();
                if (data.size() == 0) {
//                    Intent intent =new Intent(mContext, BookInsideActivity.class);
//                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                }
            }
        });
        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(mContext, BookInsideActivity.class);
                intent.putExtra("book_name", data.get(position).getBookName());
                intent.putExtra("book_title", data.get(position).getBook_title());
                intent.putExtra("file_id", data.get(position).getFile_id());
                intent.putExtra("page_no", data.get(position).getPageNo());
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
            }
        });

        return vi;
    }
    public class ViewHolder {
        ImageView imageView;
        Button button;
    }
}