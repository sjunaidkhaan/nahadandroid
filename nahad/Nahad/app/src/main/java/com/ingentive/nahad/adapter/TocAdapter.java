package com.ingentive.nahad.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.ingentive.nahad.R;
import com.ingentive.nahad.activeandroid.EmailSelectedTemp;
import com.ingentive.nahad.activeandroid.TocChildrenModel;
import com.ingentive.nahad.activeandroid.TocParentModel;
import com.ingentive.nahad.activeandroid.TocSubChildrenModel;
import com.ingentive.nahad.activity.BookInsideActivity;
import com.ingentive.nahad.activity.ContentsActivity;
import com.ingentive.nahad.activity.MainMenuActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 17-06-2016.
 */
public class TocAdapter extends BaseExpandableListAdapter {

    private List<TocParentModel> parentList;
    public Context mContext;
    private static LayoutInflater inflater = null;
    String bookName = "";
    String bookTitle = "";
    int fileId = 0;


    public TocAdapter(Context context, List<TocParentModel> parent) {
        this.parentList = parent;
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bookName = parentList.get(0).getFileName();
        this.bookTitle = parentList.get(0).fileTitle;
        this.fileId = parentList.get(0).fileId;
    }

    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        //return parentList.get(i).getTocChildrenAray().size();
        return 1;
    }

    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return parentList.get(i).getName().toString();
    }

    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return parentList.get(i).getTocChildrenAray().get(i1);
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
        if (parentList.get(groupPosition).getTocChildrenAray().size() > 0) {
            vhp.iv_parent.setVisibility(View.VISIBLE);
        } else {
            vhp.iv_parent.setVisibility(View.GONE);
        }
        vhp.tv_parent.setText(parentList.get(groupPosition).getName().toString());

        vhp.tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parentList.get(groupPosition).getTocChildrenAray().size() > 0) {

                } else {
                    Intent i = new Intent(mContext, BookInsideActivity.class);
                    i.putExtra("book_name", bookName);
                    i.putExtra("book_title", bookTitle);
                    i.putExtra("file_id", fileId);
                    i.putExtra("page_no", parentList.get(groupPosition).getPageNo());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            }
        });

        return vi;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, final View view, final ViewGroup viewGroup) {
        CustExpListview SecondLevelexplv = new CustExpListview(mContext);

        //if (groupPosition == childPosition) {
        List<TocChildrenModel> list = new ArrayList<TocChildrenModel>();
        list = parentList.get(groupPosition).getTocChildrenAray();
        SecondLevelexplv.setAdapter(new SecondLevelAdapter(mContext, list));
        SecondLevelexplv.setGroupIndicator(null);
        //}

        return SecondLevelexplv;
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
        RelativeLayout parent_layout;
        ImageView iv_parent;
    }

    public class CustExpListview extends ExpandableListView {

        int intGroupPosition, intChildPosition, intGroupid;

        public CustExpListview(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(600, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public class SecondLevelAdapter extends BaseExpandableListAdapter {

        private List<TocChildrenModel> chldrenList;
        TocChildrenModel tocChildrenModel;
        TocSubChildrenModel tocSubChildrenModel;
        public Context mContext;
        private LayoutInflater childrenInflater = null;
        //        List<EmailSelectedTemp> temEmailList=new ArrayList<EmailSelectedTemp>();


        public SecondLevelAdapter(Context context, List<TocChildrenModel> children) {
            this.chldrenList = children;
            this.mContext = context;
            childrenInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //new Delete().from(EmailSelectedTemp.class).where("file_id=?",this.chldrenList.get(0).fileId).execute();
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        //in this method you must set the text to see the parent/group on the list
        public View getGroupView(final int groupPosition, boolean b, View rowView, final ViewGroup viewGroup) {

            View childView = rowView;

            ViewHolderChild vhc = new ViewHolderChild();

            if (childView == null) {
                childView = inflater.inflate(R.layout.custom_row_contents_child, null);
                vhc.tv_child = (TextView) childView.findViewById(R.id.tv_child);
                vhc.iv_child = (ImageView) childView.findViewById(R.id.iv_child);
                vhc.iv_child_chek = (ImageView) childView.findViewById(R.id.iv_child_chek);

                vhc.child_layout = (RelativeLayout) childView.findViewById(R.id.child_layout);

                int id = childView.generateViewId();
                childView.setId(id);
                childView.setTag(vhc);

            } else {
                vhc = (ViewHolderChild) childView.getTag();
            }

            tocChildrenModel = new TocChildrenModel();
            tocChildrenModel = chldrenList.get(groupPosition);
            String ch_name = tocChildrenModel.getName();
            final ImageView iv_child_chek;
            final TextView tv_child;


            if (tocChildrenModel == null) {
                vhc.tv_child.setVisibility(View.INVISIBLE);
            } else {
                vhc.tv_child.setText(tocChildrenModel.getName());
            }
            if (tocChildrenModel.getTocSubChildrenArray().size() > 0) {
                vhc.iv_child.setVisibility(View.VISIBLE);
                vhc.iv_child_chek.setVisibility(View.INVISIBLE);
            } else {
                vhc.iv_child.setVisibility(View.INVISIBLE);
                vhc.iv_child_chek.setVisibility(View.VISIBLE);
                if (new Select().from(EmailSelectedTemp.class).where("file_id=? AND page_no=? AND topic_id=?", chldrenList.get(groupPosition).getFileId(), chldrenList.get(groupPosition).getPageNo(), chldrenList.get(groupPosition).getTopicId()).executeSingle() != null) {
                    vhc.iv_child_chek.setBackgroundResource(R.drawable.checkbox_checked);
                } else {
                    vhc.iv_child_chek.setBackgroundResource(R.drawable.checkbox_unchecked);
                }
            }
            iv_child_chek = vhc.iv_child_chek;
            tv_child = vhc.tv_child;
            vhc.iv_child_chek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pageNO = chldrenList.get(groupPosition).getPageNo();
                    if (new Select().from(EmailSelectedTemp.class).where("file_id=? AND page_no=? AND topic_id=?", chldrenList.get(groupPosition).getFileId(), chldrenList.get(groupPosition).getPageNo(), chldrenList.get(groupPosition).getTopicId()).executeSingle() != null) {
                        iv_child_chek.setBackgroundResource(R.drawable.checkbox_unchecked);
                        new Delete().from(EmailSelectedTemp.class).where("file_id=? AND page_no=? AND topic_id=?", chldrenList.get(groupPosition).getFileId(), chldrenList.get(groupPosition).getPageNo(), chldrenList.get(groupPosition).getTopicId()).execute();
                    } else if (new Select().all().from(EmailSelectedTemp.class).where("file_id=?", chldrenList.get(groupPosition).getFileId()).execute().size() < 5) {
                        iv_child_chek.setBackgroundResource(R.drawable.checkbox_checked);
                        EmailSelectedTemp temp = new EmailSelectedTemp();
                        temp.fileId = chldrenList.get(groupPosition).getFileId();
                        temp.pageNo = chldrenList.get(groupPosition).getPageNo();
                        temp.topicId = chldrenList.get(groupPosition).getTopicId();
                        temp.save();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContentsActivity.contentsActivity);
                        alertDialog.setMessage("You can select maximum 5 pages!");

                        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                }
            });

//            vhc.child_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (chldrenList.get(groupPosition).getTocSubChildrenArray().size() > 0) {
//
//                    } else {
//                        int pageNo = chldrenList.get(groupPosition).getPageNo();
//                        Intent i = new Intent(mContext, BookInsideActivity.class);
//                        i.putExtra("book_name", bookName);
//                        i.putExtra("book_title", bookTitle);
//                        i.putExtra("file_id", fileId);
//                        i.putExtra("page_no", chldrenList.get(groupPosition).getPageNo());
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(i);
//                    }
//                }
//            });
            vhc.tv_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chldrenList.get(groupPosition).getTocSubChildrenArray().size() > 0) {

                    } else {
                        int pageNo = chldrenList.get(groupPosition).getPageNo();
                        Intent i = new Intent(mContext, BookInsideActivity.class);
                        i.putExtra("book_name", bookName);
                        i.putExtra("book_title", bookTitle);
                        i.putExtra("file_id", fileId);
                        i.putExtra("page_no", chldrenList.get(groupPosition).getPageNo());
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);
                    }

                    //((Activity)mContext).finish();
                }
            });

            return childView;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
            //return 0;
        }

        @Override
        public View getChildView(final int groupPositionSubChild, final int childPositionSubChild,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            View childView = convertView;

            ViewHolderSubChild vhsc = new ViewHolderSubChild();
            if (childView == null) {
                childView = inflater.inflate(R.layout.custom_row_contents_sub_child, null);
                vhsc.tv_sub_child = (TextView) childView.findViewById(R.id.tv_sub_child);
                vhsc.iv_sub_child_check = (ImageView) childView.findViewById(R.id.iv_sub_child_chek);
                vhsc.sub_child_layout = (RelativeLayout) childView.findViewById(R.id.sub_child_layout);
                int id = childView.generateViewId();
                childView.setId(id);
                childView.setTag(vhsc);

            } else {
                vhsc = (ViewHolderSubChild) childView.getTag();
            }
            final ImageView iv_sub_child_check;
            final TextView tv_sub_child;

            if (tocChildrenModel.getTocSubChildrenArray().size() > 0) {
                tocSubChildrenModel = new TocSubChildrenModel();
                tocSubChildrenModel = tocChildrenModel.getTocSubChildrenArray().get(childPositionSubChild);
                String sub_ch_name = tocSubChildrenModel.getName();
                Log.d("", "");
                if (sub_ch_name == null) {
                    vhsc.tv_sub_child.setVisibility(View.GONE);
                } else {
                    vhsc.tv_sub_child.setText(tocSubChildrenModel.getName());
                    vhsc.tv_sub_child.setTextColor(Color.parseColor("#555555"));
                    vhsc.iv_sub_child_check.setVisibility(View.VISIBLE);
                    if (new Select().from(EmailSelectedTemp.class).where("file_id=? AND page_no=? AND topic_id=?",
                            tocSubChildrenModel.getFileId(),
                            tocSubChildrenModel.getPageNo(),
                            tocSubChildrenModel.getTopicId()).executeSingle() != null) {
                        vhsc.iv_sub_child_check.setBackgroundResource(R.drawable.checkbox_checked);
                    } else {
                        vhsc.iv_sub_child_check.setBackgroundResource(R.drawable.checkbox_unchecked);
                    }
                }
            }
            iv_sub_child_check = vhsc.iv_sub_child_check;
            tv_sub_child = vhsc.tv_sub_child;

            vhsc.iv_sub_child_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tocSubChildrenModel =new TocSubChildrenModel();
                    tocSubChildrenModel = chldrenList.get(groupPositionSubChild).getTocSubChildrenArray().get(childPositionSubChild);
                    if (new Select().from(EmailSelectedTemp.class).where("file_id=? AND page_no=? AND topic_id=?",
                            tocSubChildrenModel.getFileId(),
                            tocSubChildrenModel.getPageNo(),
                            tocSubChildrenModel.getTopicId()).executeSingle() != null) {
                        iv_sub_child_check.setBackgroundResource(R.drawable.checkbox_unchecked);
                        new Delete().from(EmailSelectedTemp.class).where("file_id=? AND page_no=? AND topic_id=?",
                                tocSubChildrenModel.getFileId(),
                                tocSubChildrenModel.getPageNo(),
                                tocSubChildrenModel.topicId).execute();
                    } else if (new Select().all().from(EmailSelectedTemp.class).where("file_id=?", tocSubChildrenModel.getFileId()).execute().size() < 5) {
                        iv_sub_child_check.setBackgroundResource(R.drawable.checkbox_checked);
                        EmailSelectedTemp temp = new EmailSelectedTemp();
                        temp.fileId = tocSubChildrenModel.getFileId();
                        temp.pageNo = tocSubChildrenModel.getPageNo();
                        temp.topicId=tocSubChildrenModel.getTopicId();
                        temp.save();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContentsActivity.contentsActivity);
                        alertDialog.setMessage("You can select maximum 5 pages!");
                        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }

//                        EmailSelectedTemp model = new Select().from(EmailSelectedTemp.class).where("file_id=? AND page_no=? AND topic_id=?",
//                                tocSubChildrenModel.getFileId(),
//                                tocSubChildrenModel.getPageNo(),
//                                tocChildrenModel.getTopicId()).executeSingle();
//                        if (model != null) {
//                            iv_sub_child_check.setBackgroundResource(R.drawable.checkbox_unchecked);
//                            new Delete().from(EmailSelectedTemp.class).where("file_id=? AND page_no=? AND topic_id=?",
//                                    tocSubChildrenModel.getFileId(),
//                                    tocSubChildrenModel.getPageNo(),
//                                    tocChildrenModel.getTopicId()).execute();
//                        } else if (new Select().all().from(EmailSelectedTemp.class).where("file_id=?", tocSubChildrenModel.getFileId()).execute().size() < 5) {
//                            EmailSelectedTemp temp = new EmailSelectedTemp();
//                            temp.fileId = tocSubChildrenModel.getFileId();
//                            temp.pageNo = tocSubChildrenModel.getPageNo();
//                            temp.topicId = tocChildrenModel.getTopicId();
//                            long res = temp.save();
//                            if (res > 0) {
//                                iv_sub_child_check.setBackgroundResource(R.drawable.checkbox_checked);
//                            }
//
//
//                        } else {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContentsActivity.contentsActivity);
//                            alertDialog.setMessage("You can select maximum 5 pages!");
//                            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            });
//                            alertDialog.show();
//                        }
                }
            });
            vhsc.sub_child_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pageNo = tocSubChildrenModel.getPageNo();
                    Intent i = new Intent(mContext, BookInsideActivity.class);
                    i.putExtra("book_name", bookName);
                    i.putExtra("book_title", bookTitle);
                    i.putExtra("file_id", fileId);
                    i.putExtra("page_no", tocSubChildrenModel.getPageNo());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                    //((Activity)mContext).finish();
                }
            });
            vhsc.tv_sub_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pageNo = tocSubChildrenModel.getPageNo();
                    Intent i = new Intent(mContext, BookInsideActivity.class);
                    i.putExtra("book_name", bookName);
                    i.putExtra("book_title", bookTitle);
                    i.putExtra("file_id", fileId);
                    i.putExtra("page_no", tocSubChildrenModel.getPageNo());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                    //((Activity)mContext).finish();
                }
            });

            return childView;
        }


        @Override
        public int getChildrenCount(int groupPosition) {
            return chldrenList.get(groupPosition).getTocSubChildrenArray().size();
//            if (chldrenList.get(groupPosition).getTocSubChildrenArray().size() == 0) {
//                return 0;
//            }
//            return 1;
        }

        @Override
        public int getGroupCount() {

            return chldrenList.size();
            //return 1;
        }

        @Override
        //gets the title of each parent/group
        public Object getGroup(int i) {
            return chldrenList.get(i).getName().toString();
        }

        @Override
        //gets the name of each item
        public Object getChild(int i, int i1) {
            return chldrenList.get(i).getTocSubChildrenArray().get(i1);

        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }




        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

        public class ViewHolderChild {
            TextView tv_child;
            ImageView iv_child, iv_child_chek;
            RelativeLayout child_layout;
        }

        public class ViewHolderSubChild {
            TextView tv_sub_child;
            ImageView iv_sub_child_check;
            RelativeLayout sub_child_layout;
        }
    }
}