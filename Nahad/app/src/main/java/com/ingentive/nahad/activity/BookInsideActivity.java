package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.pdf.PdfRenderer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imagezoom.ImageAttacher;
import com.ingentive.nahad.R;
import com.ingentive.nahad.common.DatabaseHandler;
import com.ingentive.nahad.common.TouchImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BookInsideActivity extends Activity implements View.OnClickListener {

    /*
    https://guides.codepath.com/android/Gestures-and-Touch-Events#handling-multi-touch-events
     */

    private static int currentPage = 0;
    private TouchImageView ivPDF1, ivPDF2;
    private ImageView ivLeftEmail, ivLeftBookmark, ivRightEmil, ivRightBookmark, ivLogo;
    private ImageButton ibtnNext, ibtnPrevious;
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer, mPdfRenderer2;
    private PdfRenderer.Page leftPage, rightPage;
    private Bitmap bitmapLeft, bitmapRight;
    // private RelativeLayout right_layout;
    private String folder_main = "NAHAD_PDF/SEND_EMAIL";
    private boolean fileExist = false;
    private View headerLayout;
    private RelativeLayout headerEmailLayout,
            headerAZLayout, headerBookMarksLayout, headerMenuLayout;
    private ProgressDialog pDialog;
    private TextView tvHeaderBookTitle, tvPageNo;
    int fileId = 0;
    RelativeLayout pdf1Layout, pdf2Layout;
    private GestureDetector gestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 300;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;


    String bookName = "";
    String bookTitle = "";
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_inside);
        initialize();

        ivPDF1 = (TouchImageView) findViewById(R.id.iv_pdfView1);
        gestureDetector = new GestureDetector(new GestureListener());
        ivPDF1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                if (event.getActionIndex() == 0) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
                return false;
            }
        });

        ivPDF2 = (TouchImageView) findViewById(R.id.iv_pdfView2);
        gestureDetector = new GestureDetector(new GestureListener());
        ivPDF2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                if (event.getActionIndex() == 0) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
                return false;
            }
        });
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void initialize() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        currentPage = 0;
        if (leftPage != null) {
            leftPage.close();
        }
        if (rightPage != null) {
            rightPage.close();
        }
        ivLogo = (ImageView) findViewById(R.id.logo);
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookInsideActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvPageNo = (TextView) findViewById(R.id.tv_page_no_text);
        tvHeaderBookTitle = (TextView) findViewById(R.id.tv_header_book_title);
        ibtnPrevious = (ImageButton) findViewById(R.id.ibtn_previous);
        ibtnNext = (ImageButton) findViewById(R.id.ibtn_next);
        ivPDF1 = (TouchImageView) findViewById(R.id.iv_pdfView1);
        ivPDF2 = (TouchImageView) findViewById(R.id.iv_pdfView2);
        ivLeftEmail = (ImageView) findViewById(R.id.iv_left_email);
        ivLeftBookmark = (ImageView) findViewById(R.id.iv_left_bookmark);
        ivRightEmil = (ImageView) findViewById(R.id.iv_right_email);
        ivRightBookmark = (ImageView) findViewById(R.id.iv_right_bookmark);
        pdf1Layout = (RelativeLayout) findViewById(R.id.pdf1_layout);
        pdf2Layout = (RelativeLayout) findViewById(R.id.pdf2_layout);

        /*
        * access included header view
        */
        headerLayout = findViewById(R.id.header_layout_bookinside);
        headerEmailLayout = (RelativeLayout) headerLayout.findViewById(R.id.send_email_layout);
        headerAZLayout = (RelativeLayout) headerLayout.findViewById(R.id.a_z_layout);
        headerBookMarksLayout = (RelativeLayout) headerLayout.findViewById(R.id.bookmarks_layout);
        headerMenuLayout = (RelativeLayout) headerLayout.findViewById(R.id.menu_layout);

        headerMenuLayout.setOnClickListener(this);
        headerEmailLayout.setOnClickListener(this);
        headerAZLayout.setOnClickListener(this);
        headerBookMarksLayout.setOnClickListener(this);
        ivLeftEmail.setOnClickListener(this);
        ivLeftBookmark.setOnClickListener(this);
        ivRightEmil.setOnClickListener(this);
        ivRightBookmark.setOnClickListener(this);
        ibtnNext.setOnClickListener(this);
        ibtnPrevious.setOnClickListener(this);

        /*
        * open pdf file from sdcard
        */
        try {
            showpDialog();
            Bundle bundle = getIntent().getExtras();
            if (bundle.getString("book_name") != null && bundle.getString("book_title") != null) {
                bookName = bundle.getString("book_name");
                fileId = bundle.getInt("file_id");
                currentPage = bundle.getInt("page_no");
                bookTitle = bundle.getString("book_title");
                tvHeaderBookTitle.setText(bundle.getString("book_title"));
                openRenderer("/sdcard/NAHAD_PDF/" + bookName);
            }
        } catch (Exception e) {
            hidepDialog();
            Toast.makeText(getApplication(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void openRenderer(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            fileExist = true;
            try {
                mFileDescriptor = ParcelFileDescriptor.open(file,
                        ParcelFileDescriptor.MODE_READ_ONLY);
                mPdfRenderer = new PdfRenderer(mFileDescriptor);
                mPdfRenderer2 = new PdfRenderer(mFileDescriptor);
                showPage(currentPage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                hidepDialog();
            } catch (IOException e) {
                e.printStackTrace();
                hidepDialog();
            }
            hidepDialog();
        } else {
            fileExist = false;
            ibtnNext.setClickable(false);
            ibtnPrevious.setClickable(false);
            ivLeftEmail.setClickable(false);
            ivRightEmil.setClickable(false);
            ivLeftBookmark.setClickable(false);
            ivRightBookmark.setClickable(false);
            hidepDialog();
            Toast.makeText(getApplication(), "file not exist..", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ibtn_next:
                if (mPdfRenderer2.getPageCount() > currentPage + 2) {
                    ibtnPrevious.setClickable(true);
                    currentPage = currentPage + 2;
                    showPage(currentPage);
                }
                break;
            case R.id.ibtn_previous:
                if (currentPage > 0) {
                    if (currentPage == 1) {
                        currentPage = currentPage - 1;
                    } else {
                        currentPage = currentPage - 2;
                    }
                    ivPDF2.setVisibility(View.VISIBLE);
                    ibtnNext.setClickable(true);
                    ivRightEmil.setClickable(true);
                    ivRightBookmark.setClickable(true);
                    // currentPage = currentPage - 2;
                    showPage(currentPage);
                }
                break;
            case R.id.iv_left_email:
                if (DatabaseHandler.getInstance(BookInsideActivity.this).getSendEmail(fileId, currentPage) == true) {
                    ivLeftEmail.setBackgroundResource(R.drawable.layer_two);
                    DatabaseHandler.getInstance(BookInsideActivity.this).removeSendEmail(fileId, currentPage);
                } else {
                    ivLeftEmail.setBackgroundResource(R.drawable.layer_two_);
                    DatabaseHandler.getInstance(BookInsideActivity.this).addSendEmail(fileId, currentPage);
                }
                break;
            case R.id.iv_right_email:
                if (DatabaseHandler.getInstance(BookInsideActivity.this).getSendEmail(fileId, currentPage + 1) == true) {
                    ivRightEmil.setBackgroundResource(R.drawable.layer_two);
                    DatabaseHandler.getInstance(BookInsideActivity.this).removeSendEmail(fileId, currentPage + 1);

                } else {
                    ivRightEmil.setBackgroundResource(R.drawable.layer_two_);
                    DatabaseHandler.getInstance(BookInsideActivity.this).addSendEmail(fileId, currentPage + 1);
                }
                break;
            case R.id.iv_left_bookmark:
                if (DatabaseHandler.getInstance(BookInsideActivity.this).getBookMark(fileId, currentPage) == true) {
                    ivLeftBookmark.setBackgroundResource(R.drawable.bookmarkbl);
                    DatabaseHandler.getInstance(BookInsideActivity.this).removeBookMark(fileId, currentPage);
                } else {
                    ivLeftBookmark.setBackgroundResource(R.drawable.bookmarkb);
                    DatabaseHandler.getInstance(BookInsideActivity.this).addBookMark(fileId, currentPage);
                }
                break;
            case R.id.iv_right_bookmark:
                if (DatabaseHandler.getInstance(BookInsideActivity.this).getBookMark(fileId, currentPage + 1) == true) {
                    ivRightBookmark.setBackgroundResource(R.drawable.bookmarkbl);
                    DatabaseHandler.getInstance(BookInsideActivity.this).removeBookMark(fileId, currentPage + 1);
                } else {
                    ivRightBookmark.setBackgroundResource(R.drawable.bookmarkb);
                    DatabaseHandler.getInstance(BookInsideActivity.this).addBookMark(fileId, currentPage + 1);
                }
                break;
            case R.id.send_email_layout:
                bundle = getIntent().getExtras();
                if (bundle.getString("book_name") != null && bundle.getString("book_title") != null) {
                    bookName = bundle.getString("book_name");
                    fileId = bundle.getInt("file_id");
                    bookTitle = bundle.getString("book_title");

                    intent = new Intent(BookInsideActivity.this, SendEmailActivity.class);
                    intent.putExtra("book_name", bookName);
                    intent.putExtra("file_id", fileId);
                    intent.putExtra("book_title", bookTitle);
                    startActivity(intent);
                }
                break;
            case R.id.a_z_layout:
                intent = new Intent(BookInsideActivity.this, GlossaryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bookmarks_layout:
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
                }

                break;
            case R.id.menu_layout:
                intent = new Intent(BookInsideActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void showPage(int index) {
        if (mPdfRenderer == null || mPdfRenderer.getPageCount() <= index || index < 0) {
            ibtnPrevious.setClickable(false);
            return;
        }
        try {
            if (leftPage != null) {
                leftPage.close();
            }
            if (rightPage != null) {
                rightPage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        *  Open page with specified index
        */
        tvPageNo.setText((index + 2) + " / " + mPdfRenderer.getPageCount());
        leftPage = mPdfRenderer.openPage(index);
        bitmapLeft = Bitmap.createBitmap(leftPage.getWidth(),
                leftPage.getHeight(), Bitmap.Config.ARGB_8888);
        //Pdf page is rendered on Bitmap
        leftPage.render(bitmapLeft, null, null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        ivPDF1.setImageBitmap(bitmapLeft);

        if (DatabaseHandler.getInstance(BookInsideActivity.this).getSendEmail(fileId, index) == true) {
            ivLeftEmail.setBackgroundResource(R.drawable.layer_two_);
        } else {
            ivLeftEmail.setBackgroundResource(R.drawable.layer_two);
        }
        if (DatabaseHandler.getInstance(BookInsideActivity.this).getBookMark(fileId, index) == true) {
            ivLeftBookmark.setBackgroundResource(R.drawable.bookmarkb);
        } else {
            ivLeftBookmark.setBackgroundResource(R.drawable.bookmarkbl);
        }
//        if (currentPage == 0) {
//            ibtnPrevious.setClickable(false);
//        }
        int i = index + 1;
        if (mPdfRenderer2 != null && mPdfRenderer2.getPageCount() != i && i > 0) {
            rightPage = mPdfRenderer2.openPage(i);
            bitmapRight = Bitmap.createBitmap(rightPage.getWidth(),
                    rightPage.getHeight(), Bitmap.Config.ARGB_8888);
            rightPage.render(bitmapRight, null, null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            ivPDF2.setImageBitmap(bitmapRight);
            //usingSimpleImage(ivPDF2);

            if (DatabaseHandler.getInstance(BookInsideActivity.this).getSendEmail(fileId, index + 1) == true) {
                ivRightEmil.setBackgroundResource(R.drawable.layer_two_);
            } else {
                ivRightEmil.setBackgroundResource(R.drawable.layer_two);
            }
            if (DatabaseHandler.getInstance(BookInsideActivity.this).getBookMark(fileId, index + 1) == true) {
                ivRightBookmark.setBackgroundResource(R.drawable.bookmarkb);
            } else {
                ivRightBookmark.setBackgroundResource(R.drawable.bookmarkbl);
            }
//            if (DatabaseHandler.getInstance(BookInsideActivity.this).getBookMark(fileId, index + 1) == true) {
//                ivRightEmil.setBackgroundResource(R.drawable.layer_two_);
//            } else {
//                ivRightEmil.setBackgroundResource(R.drawable.layer_two);
//            }
        } else {
            //right_layout.setVisibility(View.INVISIBLE);
            ivPDF2.setVisibility(View.INVISIBLE);
            ibtnNext.setClickable(false);
            ivRightEmil.setClickable(false);
            ivRightBookmark.setClickable(false);
            //ibtnNext.setVisibility(View.INVISIBLE);
        }
    }

    private void sendEmail(Bitmap finalBitmap, int pageno) {

        String storageDir = Environment.getExternalStorageDirectory()
                + "/NAHAD_PDF/SEND_EMAIL";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        String fname = fileId + "_" + pageno + ".jpg";
        File file = new File(f, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        String[] TO = {""};
//        String[] CC = {""};
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Select PDF Page_" + fname);
//
//        String str = "NAHAD HOSE SAFETY INSTITUTE.org";
//        str = str.concat("");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, str);
//
//        emailIntent.setType("image/jpeg");
//        File bitmapFile = new File(Environment.getExternalStorageDirectory() +
//                "/NAHAD_PDF/SEND_EMAIL/" + fname);
//        Uri myUri = Uri.fromFile(bitmapFile);
//        emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//            //finish();
//            Log.i("Finished sending emal", "");
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(BookInsideActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                if (mPdfRenderer2.getPageCount() > currentPage + 2) {
                    ibtnPrevious.setClickable(true);
                    currentPage = currentPage + 2;
                    showPage(currentPage);

                }
                return true; // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(BookInsideActivity.this, "Left to right", Toast.LENGTH_SHORT).show();
                if (currentPage > 0) {
                    if (currentPage == 1) {
                        currentPage = currentPage - 1;
                    } else {
                        currentPage = currentPage - 1;
                    }
                    ivPDF2.setVisibility(View.VISIBLE);
                    ibtnNext.setClickable(true);
                    ivRightEmil.setClickable(true);
                    ivRightBookmark.setClickable(true);
                    //currentPage = currentPage - 2;
                    showPage(currentPage);
                }
                return true; // Left to right
            }

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return false;
        }
    }
}
