package com.ingentive.nahad.adapter;

/**
 * Created by PC on 15-06-2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.artifex.mupdfdemo.MuPDFCore;
import com.ingentive.nahad.R;
import com.ingentive.nahad.activeandroid.BookMarkModel;
import com.ingentive.nahad.activeandroid.SendEmailModel;
import com.ingentive.nahad.activity.BookInsideActivity;

import com.ingentive.nahad.common.DatabaseHandler;
import com.ingentive.nahad.common.TouchImageView;

import java.io.File;
import java.io.IOException;

/**
 * Created by Junaid on 15-Jun-16.
 */
public class CustomPagerAdapter extends PagerAdapter {

    private static final String SAMPLE_FILE = "handbook_1.pdf";
    private MuPDFCore core;
    private String mFilePath;
    private int totalPages;

    private int leftPage = 0;
    private int rightPage = 1;
    private Context mContext;
    private int fileId = 0;
    String bookName;
    Bitmap bL;
    Bitmap bR;
    int currentPage = 0;
    SendEmailModel emailModel;
    BookMarkModel bookMarkModel;


    public CustomPagerAdapter(Context context, int totalPages, int fileId, String bookName, int currentPage) {
        mContext = context;
        this.totalPages = totalPages;
        this.fileId = fileId;
        this.bookName = bookName;
        this.currentPage = currentPage;
        System.gc();
        Log.e("Size", totalPages + "");
    }

    @Override
    public Object instantiateItem(final ViewGroup collection, int position) {

        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "NAHAD_PDF" + File.separator + bookName;
        //mFilePath = Environment.getExternalStorageState() + File.separator + "NAHAD_PDF" + File.separator + bookName;
        // mFilePath = "/sdcard/NAHAD_PDF/" + bookName;
        core = openFile(Uri.decode(mFilePath));

        if (core != null && core.countPages() == 0) {
            core = null;
        }
        if (core == null || core.countPages() == 0 || core.countPages() == -1) {
            Log.e("adapter", "Document Not Opening");
        }
        if (position == 0) {
            leftPage = position;
            rightPage = position + 1;
        } else {
            leftPage = position * 2;
            rightPage = (position * 2) + 1;
        }
        try {
            System.gc();
            PointF pointF = core.getPageSize(1);
            bL = core.drawPage(leftPage, (int) pointF.x, (int) pointF.y, 10, 10, (int) pointF.x, (int) pointF.y);
            bR = core.drawPage(rightPage, (int) pointF.x, (int) pointF.y, 10, 10, (int) pointF.x, (int) pointF.y);

        } catch (Exception e) {
        }
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.showpdf, collection, false);

        TouchImageView ivL = (TouchImageView) layout.findViewById(R.id.imcustoml);
        TouchImageView ivR = (TouchImageView) layout.findViewById(R.id.imcustomr);

        if (core.countPages() == rightPage) {
            ivR.setVisibility(View.INVISIBLE);
        } else {
            ivR.setVisibility(View.VISIBLE);
        }

        ivL.setImageBitmap(bL);
        ivR.setImageBitmap(bR);

        collection.addView(layout);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
//        super.destroyItem(collection, position, view);
        //System.gc();
//        bL.recycle();
//        bR.recycle();

    }

    @Override
    public int getCount() {
        if (totalPages % 2 == 1) {
            return (totalPages + 1) / 2;
        } else {
            return totalPages / 2;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "yo";
    }


    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        mFilePath = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(mContext, path);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e("Adapter", e.getMessage());
            return null;
        }
        return core;
    }
}
