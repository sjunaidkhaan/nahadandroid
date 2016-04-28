package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ingentive.nahad.R;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

public class BookInsideActivity extends Activity implements OnPageChangeListener {

    public static final String SAMPLE_FILE = "sample.pdf";

    public static final String ABOUT_FILE = "about.pdf";
    private PDFView pdfView1, pdfView2;
    public String pdfName = "about.pdf";
    Integer pageNumber = 1;
    int totalPages = 1;
    File file;
    private ImageButton ibtnLeft, ibtnRight, ibtnBookMark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_inside);
        initialize();

        file = new File("/sdcard/NAHAD_PDF/test.pdf");

        if (file.exists()) {
            new BackgroundAsyncTask_nea(1).execute();
        } else {
            Toast.makeText(getApplicationContext(), "test.pdf not found", Toast.LENGTH_LONG).show();
        }
    }


    public void initialize() {
        pdfView1 = (PDFView) findViewById(R.id.pdfview1);
        pdfView2 = (PDFView) findViewById(R.id.pdfview2);

        ibtnLeft = (ImageButton) findViewById(R.id.left_arrow);
        ibtnRight = (ImageButton) findViewById(R.id.right_arrow);
        // display(pdfName,false);


        ibtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageNumber > 1) {
                    pageNumber = pageNumber - 1;
                    //pageChange(pageNumber);
                    new BackgroundAsyncTask_nea(pageNumber).execute();
                }
            }
        });
        ibtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageNumber < totalPages) {
                    pageNumber = pageNumber + 1;
                    //pageChange(pageNumber);
                    new BackgroundAsyncTask_nea(pageNumber).execute();
                }
            }
        });
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        totalPages = pageCount;
        pageNumber = page;
    }

    public void pageChange(int pageNumber) {
        pdfView1.fromAsset(pdfName)
                .defaultPage(pageNumber)
                        //.onPageChange(this)
                .load();
        pdfView2.fromAsset(pdfName)
                .defaultPage(pageNumber)
                        //.onPageChange(this)
                .load();
    }

public class BackgroundAsyncTask_nea extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;
    int pagneo = 1;

    public BackgroundAsyncTask_nea(int page) {
        this.pagneo = page;
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        dialog = ProgressDialog.show(BookInsideActivity.this, "", "Loading. Please wait...", true);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // TODO Auto-generated method stub

        new Thread(new Runnable() {
            public void run() {
                pdfView1.fromFile(file)
                        .defaultPage(pagneo)
                        .onPageChange(BookInsideActivity.this)
                        .load();
                pdfView2.fromFile(file)
                        .defaultPage(pagneo)
                        .onPageChange(BookInsideActivity.this)
                        .load();
            }
        }).start();

        return null;
    }
}
}
