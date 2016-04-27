package com.ingentive.nahad.adapter;

/**
 * Created by PC on 27-04-2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingentive.nahad.R;
import com.ingentive.nahad.activity.BookInsideActivity;
import com.ingentive.nahad.activity.ContentsActivity;
import com.ingentive.nahad.model.ContentsChildModel;
import com.ingentive.nahad.model.ContentsParentModel;

public class ContentsAdapter extends BaseExpandableListAdapter {

    private List<ContentsParentModel> parentList;
    public Context mContext;
    private static LayoutInflater inflater = null;
    String[] titles = {"A", "B", "C"};

    public ContentsAdapter(Context context, List<ContentsParentModel> parent) {
        this.parentList = parent;
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return parentList.get(i).getChildsList().size();
    }

    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return parentList.get(i).getParentItemText().toString();
    }

    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return parentList.get(i).getChildsList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(final int groupPosition, boolean b, View rowView, final ViewGroup viewGroup) {


        final TextView tvShopSecNamePar;
        final ImageView ivShopSecIcon;
        final ImageView ivArrow;


        View vi = rowView;
        ViewHolderParent vhp = new ViewHolderParent();

        if (vi == null) {
            vi = inflater.inflate(R.layout.custom_row_contents_parent, viewGroup, false);

            vhp.tv_contents = (TextView) vi.findViewById(R.id.tv_parent);
            int id = vi.generateViewId();
            vi.setId(id);
            vi.setTag(vhp);

        }else{
            vhp = (ViewHolderParent)vi.getTag();
        }
        //parentList.get(groupPosition).setView(vhp);
        vhp.tv_contents.setText(parentList.get(groupPosition).getParentItemText().toString());

        return vi;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, final View view, final ViewGroup viewGroup) {

        View childView = view;

        ViewHolderChild vhc = new ViewHolderChild();

        if (childView == null) {
            childView = inflater.inflate(R.layout.custom_row_contents_child, null);
            vhc.tv_sequence = (TextView) childView.findViewById(R.id.tv_sequence);
            vhc.tv_child = (TextView) childView.findViewById(R.id.tv_child);

            vhc.child_layout = (RelativeLayout) childView.findViewById(R.id.child_layout);

            int id = childView.generateViewId();
            childView.setId(id);
            childView.setTag(vhc);

        }else{
            vhc = (ViewHolderChild)childView.getTag();
        }

        vhc.tv_sequence.setText(parentList.get(groupPosition).getChildsList().get(childPosition).getSequence().toString());
        vhc.tv_child.setText(parentList.get(groupPosition).getChildsList().get(childPosition).getChildItemText().toString());

        vhc.child_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialod( groupPosition, childPosition);
            }
        });
        return childView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        /* used to make the notifyDataSetChanged() method work */
        super.registerDataSetObserver(observer);
    }


    public class ViewHolderParent
    {
        TextView tv_contents;


    }
    public class ViewHolderChild
    {
        TextView tv_child,tv_sequence;
        RelativeLayout child_layout;
    }

    public void showDialod(int groupPosition,int childPosition){
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialogbox);
       // dialog.setTitle(null);

        TextView txt = (TextView) dialog.findViewById(R.id.tv);
        TextView tv = (TextView) dialog.findViewById(R.id.text_dialog);
        tv.setText(parentList.get(groupPosition).getChildsList().get(childPosition).getChildItemText().toString());

        ListView myList = (ListView) dialog.findViewById(R.id.mlist);
        txt.setText("Select a sub-section to view, or mark a selection to email.");

        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};

        final ArrayAdapter<String> myAdapter=new
                ArrayAdapter<String>(
                mContext, android.R.layout.simple_list_item_1, planets);
        myList.setAdapter(myAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext,""+myAdapter.getItem(position),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, BookInsideActivity.class);
                mContext.startActivity(intent);
                //((Activity)mContext).finish();
            }
        });

        ImageView image = (ImageView)dialog.findViewById(R.id.iv);
        image.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.ic_dialog_info));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}