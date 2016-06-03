package com.ingentive.nahad.adapter;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingentive.nahad.R;
import com.ingentive.nahad.activity.BookInsideActivity;
import com.ingentive.nahad.activity.ContentsActivity;
import com.ingentive.nahad.common.DatabaseHandler;
import com.ingentive.nahad.model.ContentsParentModel;
import com.ingentive.nahad.model.TableOfContentsChildrenModel;
import com.ingentive.nahad.model.TableOfContentsFileModel;
import com.ingentive.nahad.model.TableOfContentsModel;
import com.ingentive.nahad.model.TableOfContentsSubChildrenModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 10-05-2016.
 */
public class TableOfContentsAdapter extends BaseExpandableListAdapter {

    private List<TableOfContentsModel> parentList;
    TableOfContentsModel tableOfContentsModel;
    TableOfContentsChildrenModel tableOfContentsChildrenModel;
    TableOfContentsFileModel tableOfContentsFileModel;
    public Context mContext;
    String bookName = "";
    String title = "";
    int fileId = 0;
    private static LayoutInflater inflater = null;
    int postion = -1;


    public TableOfContentsAdapter(Context context, List<TableOfContentsModel> parent) {
        this.parentList = parent;
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bookName = parentList.get(0).getTableOfContentsFileModel().getName();
        title = parentList.get(0).getTableOfContentsFileModel().getTitle();
        fileId = parentList.get(0).getTableOfContentsFileModel().getFileId();
    }

    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return parentList.get(i).getChildrenArray().size();
    }

    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return parentList.get(i).getName().toString();
    }

    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return parentList.get(i).getChildrenArray().get(i1);
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

        View vi = rowView;
        ViewHolderParent vhp = new ViewHolderParent();

        if (vi == null) {
            vi = inflater.inflate(R.layout.custom_row_contents_parent, viewGroup, false);

            vhp.tv_parent = (TextView) vi.findViewById(R.id.tv_parent);
            vhp.iv_parent = (ImageView) vi.findViewById(R.id.iv_parent);
            vhp.parent_layout = (RelativeLayout) vi.findViewById(R.id.parent_layout);
            int id = vi.generateViewId();
            vi.setId(id);
            vi.setTag(vhp);

        } else {
            vhp = (ViewHolderParent) vi.getTag();
        }
        parentList.get(groupPosition).setView(vhp);
        vhp.tv_parent.setText(parentList.get(groupPosition).getName().toString());
        if (parentList.get(groupPosition).getChildrenArray().size() > 0) {
            vhp.iv_parent.setVisibility(View.VISIBLE);
        }else {
            vhp.iv_parent.setVisibility(View.GONE);
        }
//        vhp.parent_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //String bookName = parentList.get(groupPosition).getTableOfContentsFileModel().getName();
//                int pageNo = parentList.get(groupPosition).getPageNo();
//
//                Intent i = new Intent(mContext, BookInsideActivity.class);
//                i.putExtra("book_name", bookName);
//                i.putExtra("book_title", title);
//                i.putExtra("page_no", pageNo);
//                i.putExtra("file_id",fileId);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(i);
//            }
//        });

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
            vhc.tv_child = (TextView) childView.findViewById(R.id.tv_child);
            vhc.iv_child = (ImageView) childView.findViewById(R.id.iv_child);

            vhc.child_layout = (RelativeLayout) childView.findViewById(R.id.child_layout);

            int id = childView.generateViewId();
            childView.setId(id);
            childView.setTag(vhc);

        } else {
            vhc = (ViewHolderChild) childView.getTag();
        }

        final ImageView iv_child;
        final TextView tv_child;
        tableOfContentsChildrenModel = new TableOfContentsChildrenModel();
        tableOfContentsChildrenModel = parentList.get(groupPosition).getChildrenArray().get(childPosition);
        String ch_name = tableOfContentsChildrenModel.getName();
        Log.d("", "");

        iv_child = vhc.iv_child;
        tv_child = vhc.tv_child;

        if (ch_name == null) {
            vhc.tv_child.setVisibility(View.GONE);
        } else {
            if(DatabaseHandler.getInstance(mContext).getSendEmail(fileId,parentList.get(groupPosition).getChildrenArray().get(childPosition).getPageNo())==true){
                iv_child.setBackgroundResource(R.drawable.checkbox_checked);
            }else {
                iv_child.setBackgroundResource(R.drawable.checkbox_unchecked);
            }
            vhc.tv_child.setText(parentList.get(groupPosition).getChildrenArray().get(childPosition).getName());

            vhc.tv_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //String bookName = parentList.get(groupPosition).getTableOfContentsFileModel().getName();
                    int pageNo = parentList.get(groupPosition).getChildrenArray().get(childPosition).getPageNo();

                    Intent i = new Intent(mContext, BookInsideActivity.class);
                    i.putExtra("book_name", bookName);
                    i.putExtra("book_title", title);
                    i.putExtra("page_no", pageNo);
                    i.putExtra("file_id", fileId);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });

            vhc.iv_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(DatabaseHandler.getInstance(mContext).getSendEmail(fileId,parentList.get(groupPosition).getChildrenArray().get(childPosition).getPageNo())==true){
                        iv_child.setBackgroundResource(R.drawable.checkbox_unchecked);
                        parentList.get(groupPosition).getChildrenArray().get(childPosition).setChecked(false);
                        int pageNo = parentList.get(groupPosition).getChildrenArray().get(childPosition).getPageNo();
                        DatabaseHandler.getInstance(mContext).removeSendEmail(fileId, pageNo);
                    }else {
                        int pageNo = parentList.get(groupPosition).getChildrenArray().get(childPosition).getPageNo();
                        iv_child.setBackgroundResource(R.drawable.checkbox_checked);
                        parentList.get(groupPosition).getChildrenArray().get(childPosition).setChecked(true);
                        DatabaseHandler.getInstance(mContext).addSendEmail(fileId,pageNo);
                    }

//                        //String bookName = parentList.get(groupPosition).getTableOfContentsFileModel().getName();
//                        int pageNo = chldrenList.get(groupPosition).getPageNo();
//
//                        Intent i = new Intent(mContext, BookInsideActivity.class);
//                        i.putExtra("book_name", bookName);
//                        i.putExtra("book_title", title);
//                        i.putExtra("page_no", pageNo);
//                        i.putExtra("file_id", fileId);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(i);
                }
            });


        }
//            if (tableOfContentsChildrenModel.getSubChildrendArray().size() > 0) {
//                vhc.iv_child.setVisibility(View.VISIBLE);
//            } else {
//                vhc.iv_child.setVisibility(View.GONE);
//            }

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

    public class ViewHolderParent {
        TextView tv_parent;
        ImageView iv_parent;
        RelativeLayout parent_layout;
    }
    public class ViewHolderChild {
        TextView tv_child;
        ImageView iv_child;
        RelativeLayout child_layout;
    }

//    public class CustExpListview extends ExpandableListView {
//
//        int intGroupPosition, intChildPosition, intGroupid;
//
//        public CustExpListview(Context context) {
//            super(context);
//        }
//
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(600, MeasureSpec.AT_MOST);
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
//    }

//    public class SecondLevelAdapter extends BaseExpandableListAdapter {
//
//        private List<TableOfContentsChildrenModel> chldrenList;
//        TableOfContentsChildrenModel tableOfContentsChildrenModel;
//        TableOfContentsSubChildrenModel tableOfContentsSubChildrenModel;
//        public Context mContext;
//        private LayoutInflater childrenInflater = null;
//
//
//        public SecondLevelAdapter(Context context, List<TableOfContentsChildrenModel> children) {
//            this.chldrenList = children;
//            this.mContext = context;
//            childrenInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//        @Override
//        //in this method you must set the text to see the parent/group on the list
//        public View getGroupView(final int groupPosition, boolean b, View rowView, final ViewGroup viewGroup) {
//
//            View childView = rowView;
//
//            ViewHolderChild vhc = new ViewHolderChild();
//
//            if (childView == null) {
//                childView = inflater.inflate(R.layout.custom_row_contents_child, null);
//                vhc.tv_child = (TextView) childView.findViewById(R.id.tv_child);
//                vhc.iv_child = (ImageView) childView.findViewById(R.id.iv_child);
//
//                vhc.child_layout = (RelativeLayout) childView.findViewById(R.id.child_layout);
//
//                int id = childView.generateViewId();
//                childView.setId(id);
//                childView.setTag(vhc);
//
//            } else {
//                vhc = (ViewHolderChild) childView.getTag();
//            }
//
//            final ImageView iv_child;
//            final TextView tv_child;
//            tableOfContentsChildrenModel = new TableOfContentsChildrenModel();
//            tableOfContentsChildrenModel = chldrenList.get(groupPosition);
//            String ch_name = tableOfContentsChildrenModel.getName();
//            Log.d("", "");
//
//            iv_child = vhc.iv_child;
//            tv_child = vhc.tv_child;
//
//            if (ch_name == null) {
//                vhc.tv_child.setVisibility(View.GONE);
//            } else {
//                if(DatabaseHandler.getInstance(mContext).getSendEmail(fileId,chldrenList.get(groupPosition).getPageNo())==true){
//                    iv_child.setBackgroundResource(R.drawable.checkbox_checked);
//                }else {
//                    iv_child.setBackgroundResource(R.drawable.checkbox_unchecked);
//                }
//                vhc.tv_child.setText(chldrenList.get(groupPosition).getName());
//
//                vhc.tv_child.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        //String bookName = parentList.get(groupPosition).getTableOfContentsFileModel().getName();
//                        int pageNo = chldrenList.get(groupPosition).getPageNo();
//
//                        Intent i = new Intent(mContext, BookInsideActivity.class);
//                        i.putExtra("book_name", bookName);
//                        i.putExtra("book_title", title);
//                        i.putExtra("page_no", pageNo);
//                        i.putExtra("file_id", fileId);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(i);
//                    }
//                });
//
//                vhc.iv_child.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if(DatabaseHandler.getInstance(mContext).getSendEmail(fileId,chldrenList.get(groupPosition).getPageNo())==true){
//                            iv_child.setBackgroundResource(R.drawable.checkbox_unchecked);
//                            chldrenList.get(groupPosition).setChecked(false);
//                            int pageNo = chldrenList.get(groupPosition).getPageNo();
//                            DatabaseHandler.getInstance(mContext).removeSendEmail(fileId, pageNo);
//                        }else {
//                            int pageNo = chldrenList.get(groupPosition).getPageNo();
//                            iv_child.setBackgroundResource(R.drawable.checkbox_checked);
//                            chldrenList.get(groupPosition).setChecked(true);
//                            DatabaseHandler.getInstance(mContext).addSendEmail(fileId,pageNo);
//                        }
//
////                        //String bookName = parentList.get(groupPosition).getTableOfContentsFileModel().getName();
////                        int pageNo = chldrenList.get(groupPosition).getPageNo();
////
////                        Intent i = new Intent(mContext, BookInsideActivity.class);
////                        i.putExtra("book_name", bookName);
////                        i.putExtra("book_title", title);
////                        i.putExtra("page_no", pageNo);
////                        i.putExtra("file_id", fileId);
////                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        mContext.startActivity(i);
//                    }
//                });
//
//
//            }
////            if (tableOfContentsChildrenModel.getSubChildrendArray().size() > 0) {
////                vhc.iv_child.setVisibility(View.VISIBLE);
////            } else {
////                vhc.iv_child.setVisibility(View.GONE);
////            }
//
//            return childView;
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//
//        @Override
//        public View getChildView(int groupPosition, int childPosition,
//                                 boolean isLastChild, View convertView, ViewGroup parent) {
//            View childView = convertView;
//
//            ViewHolderSubChild vhsc = new ViewHolderSubChild();
//
//            if (childView == null) {
//                childView = inflater.inflate(R.layout.custom_row_contents_sub_child, null);
//                vhsc.tv_sub_child = (TextView) childView.findViewById(R.id.tv_sub_child);
//
//                vhsc.sub_child_layout = (RelativeLayout) childView.findViewById(R.id.sub_child_layout);
//
//                int id = childView.generateViewId();
//                childView.setId(id);
//                childView.setTag(vhsc);
//
//            } else {
//                vhsc = (ViewHolderSubChild) childView.getTag();
//            }
//            tableOfContentsSubChildrenModel = tableOfContentsChildrenModel.getSubChildrendArray().get(childPosition);
//            //TableOfContentsChildrenModel tableOfContentsChildrenModel = lis.get(groupPosition).getChildrenArray().get(childPosition);
//            String sub_ch_name = tableOfContentsSubChildrenModel.getName();
//            Log.d("", "");
//            if (sub_ch_name == null) {
//                vhsc.tv_sub_child.setVisibility(View.GONE);
//            } else {
//                vhsc.tv_sub_child.setText(tableOfContentsSubChildrenModel.getName());
//
//                vhsc.tv_sub_child.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        int pageNo = tableOfContentsSubChildrenModel.getPageNo();
//
//                        Intent i = new Intent(mContext, BookInsideActivity.class);
//                        i.putExtra("book_name", bookName);
//                        i.putExtra("book_title", title);
//                        i.putExtra("page_no", pageNo);
//                        i.putExtra("file_id", fileId);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(i);
//                    }
//                });
//            }
//
//            return childView;
//        }
//
//        @Override
//        public int getChildrenCount(int groupPosition) {
//            return chldrenList.get(groupPosition).getSubChildrendArray().size();
//            //return 5;
//        }
//
//        @Override
//        public int getGroupCount() {
//
//            return chldrenList.size();
//            //return 1;
//        }
//
//        @Override
//        //gets the title of each parent/group
//        public Object getGroup(int i) {
//            return chldrenList.get(i).getName().toString();
//        }
//
//        @Override
//        //gets the name of each item
//        public Object getChild(int i, int i1) {
//            return chldrenList.get(i).getSubChildrendArray().get(i1);
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            // TODO Auto-generated method stub
//            return true;
//        }
//
//        public class ViewHolderChild {
//            TextView tv_child;
//            ImageView iv_child;
//            RelativeLayout child_layout;
//        }
//
//        public class ViewHolderSubChild {
//            TextView tv_sub_child;
//            RelativeLayout sub_child_layout;
//        }
//    }
}