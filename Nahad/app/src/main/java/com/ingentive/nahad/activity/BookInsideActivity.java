package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.ingentive.nahad.R;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.util.logging.Handler;

public class BookInsideActivity extends Activity implements OnPageChangeListener {

    private PDFView pdfView1, pdfView2;
    String pdfName = "sample.pdf";
    Integer pageNumber = 1;
    int totalPages = 1;
    // private EditText etPageNo;
    private ImageButton ibtnLeft, ibtnRight, ibtnBookMark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_inside);

        pdfView1 = (PDFView) findViewById(R.id.pdfview1);
        pdfView2 = (PDFView) findViewById(R.id.pdfview2);

        ibtnLeft = (ImageButton) findViewById(R.id.left_arrow);
        ibtnRight = (ImageButton) findViewById(R.id.right_arrow);
        display(pdfName, false);
    }

    private void display(String file, boolean jumpToFirstPage) {
        try {
            if (jumpToFirstPage) pageNumber = 1;
            pdfView1.fromAsset(file)
                    .defaultPage(pageNumber)
                    .onPageChange(this)
                    .load();
            pdfView2.fromAsset(file)
                    .defaultPage(pageNumber)
                    .onPageChange(this)
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        try {
            totalPages = pageCount;
            pageNumber = page;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
