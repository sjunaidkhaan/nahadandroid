package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.ConnectivityManager;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ingentive.nahad.R;
import com.ingentive.nahad.adapter.GlossaryAdapter;
import com.ingentive.nahad.adapter.GridViewAdapter;
import com.ingentive.nahad.common.DatabaseHandler;
import com.ingentive.nahad.model.AddFilesModel;
import com.ingentive.nahad.model.GlossaryModel;
import com.ingentive.nahad.model.GridViewModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendEmailActivity extends Activity {

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
    private TextView tvDone;
    private Button btnSend;
    private EditText etEmail, etSubject, etBody, etName;
    private String urlSendEmail = "http://pdfcms.azurewebsites.net/api/files/sendemail/?email=";
    private String email;
    private String token;
    private String urlToken = "&token=";
    private ProgressDialog pDialog;
    private boolean is3g;
    private boolean isWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        gridView = (GridView) findViewById(R.id.gridview);
        btnSend = (Button) findViewById(R.id.btn_send);
        tvDone = (TextView) findViewById(R.id.tv_done);
        etBody = (EditText) findViewById(R.id.et_body);
        etEmail = (EditText) findViewById(R.id.et_email);
        etName = (EditText) findViewById(R.id.et_name);
        etSubject = (EditText) findViewById(R.id.et_subject);

        etBody.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        etEmail.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        etName.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        etSubject.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendEmailActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // List<AddFilesModel>list=DatabaseHandler.getInstance(SendEmailActivity.this).getAllFiles();
                if (etEmail.getText().toString().trim().replace(" ", "").length() > 0
                        && etSubject.getText().toString().trim().replace(" ", "").length() > 0
                        && etName.getText().toString().trim().replace(" ", "").length() > 0) {

                    LoginActivity.prefs = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
                    email = LoginActivity.prefs.getString("email", null);
                    token = LoginActivity.prefs.getString("token", null);
                    if (email != null && token != null) {
                        //showpDialog();
                        if (wifiChecker() == true) {
                            //getGlossary(urlGlossary + email + urlToken + token);
                            sendEmail(urlSendEmail + email + urlToken + token);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(SendEmailActivity.this, "Input field empty!", Toast.LENGTH_LONG).show();
                }
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
            intList = DatabaseHandler.getInstance(SendEmailActivity.this).getBookMarkAll(fileId);

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
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private boolean wifiChecker() {
        // TODO Auto-generated method stub
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        Log.v("", is3g + " ConnectivityManager Test " + isWifi);
        if (!is3g && !isWifi) {
            return false;
        }
        return true;
    }
//  http://pdfcms.azurewebsites.net/api/files/sendemail/?email=admin@gmail.com&token=WRUPPWIDQXBAVQTNRIQDVSMM
    public void sendEmail(String url) {
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidepDialog();
                        try {
                            Toast.makeText(SendEmailActivity.this,""+response,Toast.LENGTH_LONG).show();
                            finish();
                            Log.d("", "" + response);

                        } catch (Exception e) {
                            hidepDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        String errorStr = error.getMessage();
                        Toast.makeText(SendEmailActivity.this, "errorStr: "+errorStr, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                AddFilesModel model = DatabaseHandler.getInstance(SendEmailActivity.this).getAllFileModel(fileId);
                params.put("FileId", model.getfId() + "");
                params.put("Version", model.getfVersion() + "");
                params.put("emailTo", etEmail.getText().toString().trim());
                for (int i = 0; i < intList.size(); i++) {
                    params.put("pages[" + i + "]", intList.get(i) + 1 + "");
                }
                params.put("subject", etSubject.getText().toString().trim());
                params.put("body", etBody.getText().toString().trim());
                params.put("name", etName.getText().toString().trim());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
