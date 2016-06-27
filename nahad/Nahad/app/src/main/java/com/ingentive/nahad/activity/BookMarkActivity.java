package com.ingentive.nahad.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.artifex.mupdfdemo.MuPDFCore;
import com.ingentive.nahad.R;
import com.ingentive.nahad.activeandroid.AddFilesModel;
import com.ingentive.nahad.activeandroid.BookMarkModel;
import com.ingentive.nahad.adapter.BookMarkGridViewAdapter;
import com.ingentive.nahad.model.GridViewModel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookMarkActivity extends Activity {

    private GridView gridView;
    private BookMarkGridViewAdapter gridAdapter;
    private Bitmap bitmap;
    private GridViewModel imageItem;
    private List<GridViewModel> mList = new ArrayList<GridViewModel>();

    private String bookName = "";
    private int fileId = 0;
    private String bookTitle = "";
    private List<BookMarkModel> intList;
    private List<GridViewModel> imageItemList;
    private MuPDFCore core;
    private String mFilePath;
    private TextView tvDone;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gridView = (GridView) findViewById(R.id.gridview);
        tvDone = (TextView) findViewById(R.id.tv_done);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookMarkActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle.getInt("file_id") != 0 && bundle.getString("book_name") != null) {
            bookName = bundle.getString("book_name");
            fileId = bundle.getInt("file_id");
            bookTitle = bundle.getString("book_title");

            intList = new ArrayList<BookMarkModel>();
            imageItemList = new ArrayList<GridViewModel>();
            intList = new Select().all().from(BookMarkModel.class).where("file_id=?",fileId).orderBy("page_no ASC").execute();

            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "NAHAD_PDF" + File.separator + bookName;
            core = openFile(Uri.decode(mFilePath));
            if (core != null && core.countPages() == 0) {
                core = null;
            }
            if (core == null || core.countPages() == 0 || core.countPages() == -1) {
                Log.e("", "Document Not Opening");
            }
            if (core != null) {
                for (int i = 0; i < intList.size(); i++) {
                    PointF pointF = core.getPageSize(1);
                    bitmap = core.drawPage(intList.get(i).getPageNo(), (int) 200, (int) 200, 0, 0, (int) 200, (int)200);
                    imageItem = new GridViewModel();
                    imageItem.setImage(bitmap);
                    imageItem.setPageNo(intList.get(i).getPageNo());
                    imageItem.setFile_id(intList.get(i).getFileId());
                    imageItem.setBookName(bookName);
                    imageItem.setBook_title(bookTitle);
                    mList.add(imageItem);
                    //mThumbIds.add(bitmap);
                }
            }

            gridAdapter = new BookMarkGridViewAdapter(this, R.layout.grid_item_layout, mList);
            gridView.setAdapter(gridAdapter);
        }
    }
    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        mFilePath = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(BookMarkActivity.this, path);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e("Adapter", e.getMessage());
            return null;
        }
        return core;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        AddFilesModel addFilesModel = new AddFilesModel();
        addFilesModel = new Select().from(AddFilesModel.class).orderBy("update_date DESC").executeSingle();
        if(addFilesModel!=null){
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
                if (hours-24> 0) {
                    Intent i = new Intent(BookMarkActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
