package com.ingentive.nahad.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.ingentive.nahad.R;
import com.ingentive.nahad.adapter.GridViewAdapter;
import com.ingentive.nahad.common.DatabaseHandler;
import com.ingentive.nahad.model.GridViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookMarkActivity extends Activity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Bitmap bitmap;
    private GridViewModel imageItem;
    private List<GridViewModel> mList = new ArrayList<GridViewModel>();

    private PdfRenderer.Page mCurrentPage = null;
    private PdfRenderer mPdfRenderer = null;
    private String bookName = "";
    private int fileId = 0;
    private String bookTitle = "";
    private File file = null;
    private ParcelFileDescriptor mFileDescriptor = null;
    private List<Integer> intList;
    private List<GridViewModel> imageItemList;
    private ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        gridView = (GridView) findViewById(R.id.gridview);
        ivLogo = (ImageView) findViewById(R.id.logo);
        ivLogo.setOnClickListener(new View.OnClickListener() {
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
            file = new File("/sdcard/NAHAD_PDF/" + bookName);

            intList = new ArrayList<Integer>();
            imageItemList = new ArrayList<GridViewModel>();
            intList = DatabaseHandler.getInstance(BookMarkActivity.this).getBookMarkAll(fileId);

            try {
                mFileDescriptor = ParcelFileDescriptor.open(file,
                        ParcelFileDescriptor.MODE_READ_ONLY);
                mPdfRenderer = new PdfRenderer(mFileDescriptor);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < intList.size(); i++) {
                if (mPdfRenderer == null || mPdfRenderer.getPageCount() <= intList.get(i) || intList.get(i) < 0) {
                    return;
                }
                try {
                    if (mCurrentPage != null) {
                        mCurrentPage.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mCurrentPage = mPdfRenderer.openPage(intList.get(i));
                bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(),
                        mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);

                mCurrentPage.render(bitmap, null, null,
                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                imageItem = new GridViewModel();
                imageItem.setImage(bitmap);
                imageItem.setPageNo(intList.get(i));
                mList.add(imageItem);
                //mThumbIds.add(bitmap);
            }

            gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mList);
            gridView.setAdapter(gridAdapter);
            try {
                mFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent i = new Intent(getApplicationContext(), BookInsideActivity.class);
                    GridViewModel model = new GridViewModel();
                    model = mList.get(position);
                    int pageno = model.getPageNo();
                    i.putExtra("book_name", bookName);
                    i.putExtra("book_title", bookTitle);
                    i.putExtra("file_id", fileId);
                    i.putExtra("page_no", mList.get(position).getPageNo());
                    startActivity(i);
                    finish();
                }
            });
        }
    }
}
