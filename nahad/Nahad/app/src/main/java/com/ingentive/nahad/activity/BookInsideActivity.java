package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.artifex.mupdfdemo.MuPDFCore;
import com.ingentive.nahad.R;
import com.ingentive.nahad.activeandroid.AddFilesModel;
import com.ingentive.nahad.activeandroid.BookMarkModel;
import com.ingentive.nahad.activeandroid.SendEmailModel;
import com.ingentive.nahad.activeandroid.TocParentModel;
import com.ingentive.nahad.adapter.CustomPagerAdapter;
import com.ingentive.nahad.common.TouchImageView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookInsideActivity extends Activity implements View.OnClickListener {

    /*
    https://guides.codepath.com/android/Gestures-and-Touch-Events#handling-multi-touch-events
     */

    private static int currentPage = -1;
    private View headerLayout;
    private RelativeLayout headerEmailLayout,
            headerAZLayout, headerBookMarksLayout, headerMenuLayout, headerToceLayout;
    private ProgressDialog pDialog;
    private TextView tvHeaderBookTitle, tvPageNo;
    private int fileId = 0;
    private ViewPager viewPager;
    private ImageView ivEmailLeft, ivBookmarkLeft, ivEmailRight, ivBookmarkRight,
            ivLogo, ivToc, ivAZ, ivBookMark, ivEmail, ivMenu, ivExpand;
    private MuPDFCore core;
    private Context mContext;
    private String mFilePath;
    private String bookName = "";
    private String bookTitle = "";
    private Bundle bundle;
    private ImageButton ibtn_previous, ibtn_next;
    int position = 0, leftPage = 0, rightPage = 1;
    SendEmailModel emailModel;
    BookMarkModel bookMarkModel;
    Intent intent;
    private static final String SAMPLE_FILE = "handbook_1.pdf";
    private String folder_main = "NAHAD_PDF/BOOK_MARKS";
    private boolean expand = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_inside);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initialize();
    }

    public void initialize() {
        ivExpand = (ImageView) findViewById(R.id.iv_expand_button);
        ivEmailLeft = (ImageView) findViewById(R.id.iv_email_left);
        ivBookmarkLeft = (ImageView) findViewById(R.id.iv_bookmark_left);
        ivEmailRight = (ImageView) findViewById(R.id.iv_email_right);
        ivBookmarkRight = (ImageView) findViewById(R.id.iv_bookmark_right);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tvPageNo = (TextView) findViewById(R.id.tv_page_no_text);
        tvHeaderBookTitle = (TextView) findViewById(R.id.tv_header_book_title);
        ibtn_previous = (ImageButton) findViewById(R.id.ibtn_previous);
        ibtn_next = (ImageButton) findViewById(R.id.ibtn_next);

        ivLogo = (ImageView) findViewById(R.id.logo);
        ivToc = (ImageView) findViewById(R.id.iv_toc);
        ivAZ = (ImageView) findViewById(R.id.iv_a_z);
        ivBookMark = (ImageView) findViewById(R.id.layer_one);
        ivEmail = (ImageView) findViewById(R.id.at_the_rate);
        ivMenu = (ImageView) findViewById(R.id.iv_menu_icon);

        headerLayout = findViewById(R.id.header_layout_bookinside);
        headerEmailLayout = (RelativeLayout) headerLayout.findViewById(R.id.send_email_layout);
        headerAZLayout = (RelativeLayout) headerLayout.findViewById(R.id.a_z_layout);
        headerBookMarksLayout = (RelativeLayout) headerLayout.findViewById(R.id.bookmarks_layout);
        headerMenuLayout = (RelativeLayout) headerLayout.findViewById(R.id.menu_layout);
        headerToceLayout = (RelativeLayout) headerLayout.findViewById(R.id.toc_layout);
        ibtn_previous.setOnClickListener(this);
        ibtn_next.setOnClickListener(this);
        headerMenuLayout.setOnClickListener(this);
        headerEmailLayout.setOnClickListener(this);
        headerAZLayout.setOnClickListener(this);
        headerBookMarksLayout.setOnClickListener(this);
        ivLogo.setOnClickListener(this);
        headerToceLayout.setOnClickListener(this);

        ivEmailLeft.setOnClickListener(this);
        ivBookmarkLeft.setOnClickListener(this);
        ivEmailRight.setOnClickListener(this);
        ivBookmarkRight.setOnClickListener(this);


        ivExpand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        if(expand==false){
                            headerLayout.setVisibility(View.GONE);
                            expand=true;
                        }else {
                            headerLayout.setVisibility(View.VISIBLE);
                            expand=false;
                        }
                        break;
                    }
                }

                return true;
            }
        });

        ivMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        intent = new Intent(BookInsideActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finish();

                        break;
                    }
                }

                return true;
            }
        });
        ivEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();

                        if (new Select().all().from(SendEmailModel.class).where("file_id=?", fileId).execute().size() > 0) {
                            bundle = getIntent().getExtras();
                            if (bundle.getString("book_name") != null && bundle.getString("book_title") != null) {
                                bookName = bundle.getString("book_name");
                                fileId = bundle.getInt("file_id");
                                bookTitle = bundle.getString("book_title");

                                intent = new Intent(BookInsideActivity.this, SendEmailActivity.class);
                                intent.putExtra("book_name", bookName);
                                intent.putExtra("file_id", fileId);
                                intent.putExtra("book_title", bookTitle);
                                intent.putExtra("from", "book_inside");
                                startActivity(intent);
                                //finish();
                            }
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookInsideActivity.this);
                            alertDialog.setMessage("You must select at least 1 page!");
                            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();
                        }

                        break;
                    }
                }

                return true;
            }
        });
        ivBookMark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();

                        if (new Select().all().from(BookMarkModel.class).where("file_id=?", fileId).execute().size() > 0) {
                            bundle = getIntent().getExtras();
                            if (bundle.getString("book_name") != null && bundle.getString("book_title") != null) {
                                bookName = bundle.getString("book_name");
                                fileId = bundle.getInt("file_id");
                                bookTitle = bundle.getString("book_title");

                                intent = new Intent(BookInsideActivity.this, BookMarkActivity.class);
                                intent.putExtra("book_name", bookName);
                                intent.putExtra("file_id", fileId);
                                intent.putExtra("book_title", bookTitle);
                                startActivity(intent);
                                //finish();
                            }
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookInsideActivity.this);
                            alertDialog.setMessage("You must select at least 1 page!");
                            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        }
                        break;
                    }
                }

                return true;
            }
        });
        ivAZ.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        intent = new Intent(BookInsideActivity.this, GlossaryActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                return true;
            }
        });
        ivToc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();

                        intent = new Intent(BookInsideActivity.this, ContentsActivity.class);
                        intent.putExtra("book_name", bookName);
                        intent.putExtra("file_id", fileId);
                        intent.putExtra("book_title", bookTitle);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                return true;
            }
        });
        ivLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        intent = new Intent(BookInsideActivity.this, MainMenuActivity.class);
                        intent.putExtra("intent", "");
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                return true;
            }
        });

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.getString("book_name") != null && bundle.getString("book_title") != null) {
                bookName = bundle.getString("book_name");
                fileId = bundle.getInt("file_id");
                currentPage = bundle.getInt("page_no");
                bookTitle = bundle.getString("book_title");
                tvHeaderBookTitle.setText(bundle.getString("book_title"));
                if (new Select().from(TocParentModel.class).where("file_id=?", fileId).execute().size() < 2) {
                    headerToceLayout.setVisibility(View.GONE);
                } else {
                    headerToceLayout.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplication(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "NAHAD_PDF" + File.separator + bookName;

        core = openFile(mFilePath);

        if (core != null && core.countPages() == 0) {
            core = null;
        }
        if (core == null || core.countPages() == 0 || core.countPages() == -1) {
            Log.e("", "Document Not Opening");
        }
        if (core != null) {
            viewPager();
        } else {
            Toast.makeText(BookInsideActivity.this, bookTitle + " not exist!", Toast.LENGTH_LONG).show();
            ivBookmarkLeft.setVisibility(View.INVISIBLE);
            ivBookmarkRight.setVisibility(View.INVISIBLE);
            ivEmailLeft.setVisibility(View.INVISIBLE);
            ivEmailRight.setVisibility(View.INVISIBLE);
        }
    }

    private void viewPager() {
        if (core != null) {
            if (currentPage >= 0) {
                viewPager.setAdapter(new CustomPagerAdapter(this, core.countPages(), fileId, bookName, currentPage));
                tvPageNo.setText("page " + 2 + " of " + core.countPages());
                int pos = 0;
                if (currentPage < 2) {
                    pos = 0;
                    tvPageNo.setText("page " + 2 + " of " + core.countPages());
                } else if (currentPage % 2 == 0) {
                    pos = (currentPage) / 2;
                    tvPageNo.setText("page " + (pos + 1) * 2 + " of " + core.countPages());
                } else if (currentPage % 2 == 1) {
                    pos = (currentPage) / 2;
                    tvPageNo.setText("page " + (pos + 1) * 2 + " of " + core.countPages());
                }
//                else if (currentPage % 2 == 0) {
//                    pos = (currentPage) / 2;
//                    tvPageNo.setText(pos * 2 + " of " + core.countPages());
//                }
                viewPager.setCurrentItem(pos);
                emailBookMarkLayouts();
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        //Toast.makeText(getApplication(),""+position,Toast.LENGTH_LONG).show();
                        int page = 0;
                        if (position == 0) {
                            page = position + 2;
                            tvPageNo.setText("page " + page + " of " + core.countPages());
                        } else {
                            page = (position + 1) * 2;
                            if (page > core.countPages()) {
                                page = page - 1;
                            }
                            tvPageNo.setText("page " + page + " of " + core.countPages());
                        }
                        emailBookMarkLayouts();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }
        }
    }

    private void emailBookMarkLayouts() {
        getPageNo();
        emailModel = new Select().from(SendEmailModel.class).where("file_id=? AND  page_no=?", fileId, leftPage).executeSingle();
        if (emailModel == null) {
            ivEmailLeft.setBackgroundResource(R.drawable.layer_two);
        } else {
            ivEmailLeft.setBackgroundResource(R.drawable.layer_two_);
        }
        bookMarkModel = new BookMarkModel();
        bookMarkModel = new Select().from(BookMarkModel.class).where("file_id=? AND  page_no=?", fileId, leftPage).executeSingle();
        if (bookMarkModel == null) {
            ivBookmarkLeft.setBackgroundResource(R.drawable.bookmarkgray);
        } else {
            ivBookmarkLeft.setBackgroundResource(R.drawable.bookmarkg);
        }
        if (core.countPages() != rightPage && rightPage > 0) {
            emailModel = new SendEmailModel();
            emailModel = new Select().from(SendEmailModel.class).where("file_id=? AND  page_no=?", fileId, rightPage).executeSingle();
            if (emailModel == null) {
                ivEmailRight.setBackgroundResource(R.drawable.layer_two);
            } else {
                ivEmailRight.setBackgroundResource(R.drawable.layer_two_);
            }
            bookMarkModel = new BookMarkModel();
            bookMarkModel = new Select().from(BookMarkModel.class).where("file_id=? AND  page_no=?", fileId, rightPage).executeSingle();
            if (bookMarkModel == null) {
                ivBookmarkRight.setBackgroundResource(R.drawable.bookmarkgray);
            } else {
                ivBookmarkRight.setBackgroundResource(R.drawable.bookmarkg);
            }
        }
        int count = core.countPages();
        if (leftPage == 0) {
            ibtn_previous.setVisibility(View.INVISIBLE);
        } else {
            ibtn_previous.setVisibility(View.VISIBLE);
        }
        if (core.countPages() == rightPage + 1) {
            ibtn_next.setVisibility(View.INVISIBLE);
        } else if (core.countPages() == rightPage) {
            //right_layout.setVisibility(View.INVISIBLE);
            ivEmailRight.setVisibility(View.INVISIBLE);
            ivBookmarkRight.setVisibility(View.INVISIBLE);
            ibtn_next.setVisibility(View.INVISIBLE);
            tvPageNo.setText("page " + core.countPages() + " of " + core.countPages());
        } else {
            ivEmailRight.setVisibility(View.VISIBLE);
            ivBookmarkRight.setVisibility(View.VISIBLE);
            ibtn_next.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ibtn_previous:
                position = viewPager.getCurrentItem();
                if (position - 1 >= 0) {
                    viewPager.setCurrentItem(position - 1);
                    viewPager.getAdapter().notifyDataSetChanged();
                }
                break;
            case R.id.ibtn_next:
                position = viewPager.getCurrentItem();
                if (core != null && core.countPages() % 2 == 1) {
                    if (position + 1 < core.countPages() + 1 / 2) {
                        viewPager.setCurrentItem(position + 1);
                        viewPager.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    if (position + 1 < core.countPages() / 2) {
                        viewPager.setCurrentItem(position + 1);
                        viewPager.getAdapter().notifyDataSetChanged();
                    }
                }
                break;
            case R.id.iv_email_left:
                getPageNo();
                if (new Select().from(SendEmailModel.class).where("file_id=? AND  page_no=?", fileId, leftPage).executeSingle() == null) {
                    ivEmailLeft.setBackgroundResource(R.drawable.layer_two_);
                    emailModel = new SendEmailModel();
                    emailModel.pageNo = leftPage;
                    emailModel.fileId = fileId;
                    emailModel.save();
                } else {
                    ivEmailLeft.setBackgroundResource(R.drawable.layer_two);
                    new Delete().from(SendEmailModel.class).where("file_id=? AND  page_no=?", fileId, leftPage).execute();
                }
                break;

            case R.id.iv_bookmark_left:
                getPageNo();

                if (new Select().from(BookMarkModel.class).where("file_id=? AND  page_no=?", fileId, leftPage).executeSingle() == null) {
                    ivBookmarkLeft.setBackgroundResource(R.drawable.bookmarkg);
                    bookMarkModel = new BookMarkModel();
                    bookMarkModel.fileId = fileId;
                    bookMarkModel.pageNo = leftPage;
                    bookMarkModel.save();
                } else {
                    ivBookmarkLeft.setBackgroundResource(R.drawable.bookmarkgray);
                    new Delete().from(BookMarkModel.class).where("file_id=? AND  page_no=?", fileId, leftPage).execute();
                }
                break;
            case R.id.iv_email_right:
                getPageNo();
                if (new Select().from(SendEmailModel.class).where("file_id=? AND  page_no=?", fileId, rightPage).executeSingle() == null) {
                    ivEmailRight.setBackgroundResource(R.drawable.layer_two_);
                    emailModel = new SendEmailModel();
                    emailModel.pageNo = rightPage;
                    emailModel.fileId = fileId;
                    emailModel.save();
                } else {
                    new Delete().from(SendEmailModel.class).where("file_id=? AND  page_no=?", fileId, rightPage).execute();
                    ivEmailRight.setBackgroundResource(R.drawable.layer_two);
                }
                break;
            case R.id.iv_bookmark_right:
                getPageNo();
                if (new Select().from(BookMarkModel.class).where("file_id=? AND  page_no=?", fileId, rightPage).executeSingle() == null) {
                    ivBookmarkRight.setBackgroundResource(R.drawable.bookmarkg);
                    bookMarkModel = new BookMarkModel();
                    bookMarkModel.fileId = fileId;
                    bookMarkModel.pageNo = rightPage;
                    bookMarkModel.save();
                } else {
                    ivBookmarkRight.setBackgroundResource(R.drawable.bookmarkgray);
                    new Delete().from(BookMarkModel.class).where("file_id=? AND  page_no=?", fileId, rightPage).execute();
                }
                break;
        }
    }

    private void getPageNo() {
        position = viewPager.getCurrentItem();
        if (position == 0) {
            leftPage = position;
            rightPage = position + 1;
        } else {
            leftPage = position * 2;
            rightPage = (position * 2) + 1;
        }
    }

    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        mFilePath = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(BookInsideActivity.this, path);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e("", e.getMessage());
            return null;
        }
        return core;
    }

    @Override
    protected void onStart() {
        super.onStart();
        emailBookMarkLayouts();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        AddFilesModel addFilesModel = new AddFilesModel();
        addFilesModel = new Select().from(AddFilesModel.class).orderBy("update_date DESC").executeSingle();
        if (addFilesModel != null) {
            String toyBornTime = addFilesModel.getUpdateDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date oldDate = dateFormat.parse(toyBornTime);
                System.out.println(oldDate);
                Date currentDate = new Date();
                long diff = currentDate.getTime() - oldDate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                //long days = hours / 24;
                if (hours - 24 > 0) {
                    Intent i = new Intent(BookInsideActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.gc();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
