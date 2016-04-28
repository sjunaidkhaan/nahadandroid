package com.ingentive.nahad.activity;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ingentive.nahad.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PDFDownloadActivity extends AppCompatActivity {

    TextView tv_loading;
    String dest_file_path = "test.pdf";
    int downloadedSize = 0, totalsize;
    String download_file_url = "https://maven.apache.org/archives/maven-1.x/maven.pdf";
    float per = 0;
    String folder_main = "NAHAD_PDF";
    private Button btnFinish,btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_pdfdownload);
        tv_loading = (TextView)findViewById(R.id.tv_loading);
        //setContentView(tv_loading);
        tv_loading.setGravity(Gravity.CENTER);
        tv_loading.setTypeface(null, Typeface.BOLD);
        btnFinish = (Button)findViewById(R.id.btn_finish);
        btnDownload = (Button)findViewById(R.id.btn_download);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiChecker() == true) {
                    downloadAndOpenPDF();
                } else {
                    Toast.makeText(getApplication(), "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                    //finish();
                }
            }
        });

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        //downloadAndOpenPDF();
    }
    void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {
                downloadFile(download_file_url);
                //finish();
//                Uri path = Uri.fromFile(downloadFile(download_file_url));
//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(path, "application/pdf");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                } catch (ActivityNotFoundException e) {
//                    tv_loading
//                            .setError("PDF Reader application is not installed in your device");
//                }
            }
        }).start();

    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            //File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            //file = new File(SDCardRoot, dest_file_path);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => "+c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String formattedDate = df.format(c.getTime());



            file = new File("/sdcard/"+folder_main+"/", formattedDate+".pdf");

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();
            setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;
            downloadedSize=0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            setText("Download Complete.");

        } catch (final MalformedURLException e) {
            setTextError("Some error occured. Press back and try again.",
                    Color.RED);
        } catch (final IOException e) {
            setTextError("Some error occured. Press back and try again.",
                    Color.RED);
        } catch (final Exception e) {
            setTextError(
                    "Failed to download PDF. Please check your internet connection.",
                    Color.RED);

        }
        return file;
    }

    void setTextError(final String message, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setTextColor(color);
                tv_loading.setText(message);
            }
        });

    }

    void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
            }
        });

    }
    public boolean wifiChecker() {
        // TODO Auto-generated method stub
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        Log.v("", is3g + " ConnectivityManager Test " + isWifi);
        if (!is3g && !isWifi) {
//            Toast.makeText(getApplicationContext(), "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}