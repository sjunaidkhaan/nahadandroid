package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ingentive.nahad.R;
import com.ingentive.nahad.adapter.GlossaryAdapter;
import com.ingentive.nahad.adapter.MainMenuAdapter;
import com.ingentive.nahad.common.AppController;
import com.ingentive.nahad.common.DatabaseHandler;
import com.ingentive.nahad.common.JSONRequests;
import com.ingentive.nahad.model.AddFilesModel;
import com.ingentive.nahad.model.FilesCategory;
import com.ingentive.nahad.model.GlossaryModel;
import com.ingentive.nahad.model.TableOfContentsChildrenModel;
import com.ingentive.nahad.model.TableOfContentsFileModel;
import com.ingentive.nahad.model.TableOfContentsModel;
import com.ingentive.nahad.model.TableOfContentsSubChildrenModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends Activity implements View.OnClickListener {

    //private String urlGetAllFiles = "http://pdfcms.azurewebsites.net/api/files?email=&token=";
    private String urlAllFiles1 = "http://pdfcms.azurewebsites.net/api/files?email=";
    private String urlToken = "&token=";
    private String urlInstituteHandbook = "http://pdfcms.azurewebsites.net/api/files/get/1?email=";
    private String urlFabricationGuides = "http://pdfcms.azurewebsites.net/api/files/get/2?email=";
    private String urlWhitePapers = "http://pdfcms.azurewebsites.net/api/files/get/3?email=";
    private String urlResources = "http://pdfcms.azurewebsites.net/api/files/get/4?email=";
    private String urlGlossary = "http://pdfcms.azurewebsites.net/api/glossaries/?email=";
    private String urlVisitWebsite = "http://pdfcms.azurewebsites.net/api/visitwebsite?email=";
    private String urlTableOfContents = "http://pdfcms.azurewebsites.net/api/tocs/get/";
    private ProgressDialog pDialog;
    private boolean is3g;
    private boolean isWifi;
    private String email;
    private String token;
    private ProgressDialog mProgressDialog;
    private ProgressDialog pdialog;
    public static final int progress_bar_type = 0;
    private String folder_main = "NAHAD_PDF";
    private RelativeLayout tv_institute_handbook_layout, fabb_layout, resources_layout,
            white_papers_layout, glossary_lauout, visit_website_layout, right_layout;
    private ListView listView;
    private MainMenuAdapter mAdapter;
    private TextView tvSelectedLayoutName;
    private List<AddFilesModel> particularList;
    private List<GlossaryModel> glossaryModelList;
    private AddFilesModel filesModel;
    private JSONRequests jsonRequestsData;
    private List<AddFilesModel> filesList;

    List<TableOfContentsModel> parentModelsList = new ArrayList<TableOfContentsModel>();
    TableOfContentsModel tableOfContentsModel;
    TableOfContentsFileModel tableOfContentsFileModel;
    TableOfContentsChildrenModel tableOfContentsChildrenModel;
    TableOfContentsSubChildrenModel tableOfContentsSubChildrenModel;
    Intent book_intent, content_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        initialize();
        particularList = DatabaseHandler.getInstance(MainMenuActivity.this).getAllFiles();
        Log.d("", "");
    }

    public void initialize() {
        tv_institute_handbook_layout = (RelativeLayout) findViewById(R.id.tv_institute_handbook_layout);
        fabb_layout = (RelativeLayout) findViewById(R.id.fabb_layout);
        resources_layout = (RelativeLayout) findViewById(R.id.resources_layout);
        white_papers_layout = (RelativeLayout) findViewById(R.id.white_papers_layout);
        glossary_lauout = (RelativeLayout) findViewById(R.id.glossary_lauout);
        visit_website_layout = (RelativeLayout) findViewById(R.id.visit_website_layout);
        right_layout = (RelativeLayout) findViewById(R.id.right);
        listView = (ListView) findViewById(R.id.listview);
        tvSelectedLayoutName = (TextView) findViewById(R.id.tv_selected);

        tv_institute_handbook_layout.setOnClickListener(this);
        fabb_layout.setOnClickListener(this);
        resources_layout.setOnClickListener(this);
        white_papers_layout.setOnClickListener(this);
        glossary_lauout.setOnClickListener(this);
        visit_website_layout.setOnClickListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        LoginActivity.prefs = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        email = LoginActivity.prefs.getString("email", null);
        token = LoginActivity.prefs.getString("token", null);
        if (email != null && token != null) {
            if (wifiChecker() == true) {
                jsonRequestsData = new JSONRequests(MainMenuActivity.this);
                getAllFiles(urlAllFiles1 + email + urlToken + token);
            } else {
                //Toast.makeText(getApplicationContext(), "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_institute_handbook_layout:
                tvSelectedLayoutName.setVisibility(View.VISIBLE);
                tvSelectedLayoutName.setText("Institute Handbook");
                getParticularData(1);
                break;
            case R.id.fabb_layout:
                tvSelectedLayoutName.setVisibility(View.VISIBLE);
                tvSelectedLayoutName.setText("Fabrication Guides");
                getParticularData(2);
                break;
            case R.id.resources_layout:
                tvSelectedLayoutName.setVisibility(View.VISIBLE);
                tvSelectedLayoutName.setText("Resourcess");
                getParticularData(4);
                break;
            case R.id.white_papers_layout:
                tvSelectedLayoutName.setVisibility(View.VISIBLE);
                tvSelectedLayoutName.setText("White Papers");
                getParticularData(3);
                break;
            case R.id.glossary_lauout:
                Intent i = new Intent(MainMenuActivity.this, GlossaryActivity.class);
                startActivity(i);
                break;
            case R.id.visit_website_layout:
                if (wifiChecker() == true) {
                    WebView mWebview = new WebView(this);
                    mWebview.loadUrl("http://www.nahad.org/aws/NAHAD/pt/sp/home_page");
                    setContentView(mWebview);
                } else {
                    Toast.makeText(getApplicationContext(), "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void showGlossary() {
        tvSelectedLayoutName.setVisibility(View.VISIBLE);
        tvSelectedLayoutName.setText("Glossary");
        glossaryModelList = DatabaseHandler.getInstance(MainMenuActivity.this).getGlossary();
        GlossaryAdapter mAdapter = new GlossaryAdapter(MainMenuActivity.this, glossaryModelList, R.layout.custom_row_menu, "a");
        right_layout.setBackgroundColor(0xFFFFFFFF);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainMenuActivity.this, GlossaryActivity.class);
                //i.putExtra("itemname", glossaryModelList.get(position).getWord());
                startActivity(i);
            }
        });
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

    public void getParticularData(int category) {
        particularList = DatabaseHandler.getInstance(MainMenuActivity.this).getParticularCategory(category);
        mAdapter = new MainMenuAdapter(MainMenuActivity.this, particularList, R.layout.custom_row_menu);
        right_layout.setBackgroundColor(0xFFFFFFFF);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                book_intent = new Intent(MainMenuActivity.this, BookInsideActivity.class);
                book_intent.putExtra("book_name", particularList.get(position).getfName());
                book_intent.putExtra("file_id", particularList.get(position).getfId());
                book_intent.putExtra("book_title", particularList.get(position).getfTitle());
                book_intent.putExtra("page_no", 0);

                content_intent = new Intent(MainMenuActivity.this, ContentsActivity.class);
                content_intent.putExtra("book_name", particularList.get(position).getfName());
                content_intent.putExtra("file_id", particularList.get(position).getfId());
                content_intent.putExtra("book_title", particularList.get(position).getfTitle());

                if (email != null && token != null) {
                    if (wifiChecker() == true) {
                        String url = urlTableOfContents + particularList.get(position).getfId() + "?email=" + email + urlToken + token;
                        getTableOfContents(urlTableOfContents + particularList.get(position).getfId() + "?email=" + email + urlToken + token);
                    } else {
                        List<TableOfContentsModel> mList = new ArrayList<TableOfContentsModel>();
                        mList = DatabaseHandler.getInstance(MainMenuActivity.this).getTableOfContentsModels(particularList.get(position).getfId());
                        if (mList.size() > 0) {
                            startActivity(content_intent);
                        } else {
                            startActivity(book_intent);
                        }
                    }
                }

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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pdialog = new ProgressDialog(this);
                pdialog.setIndeterminate(false);
                pdialog.setMax(100);
                pdialog.setMessage("Downloading file. Please wait...");
                pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pdialog.setCancelable(false);
                pdialog.show();
                return pdialog;
            default:
                return null;
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        List<AddFilesModel> files;

        DownloadFileFromURL(List<AddFilesModel> files) {
            this.files = files;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            AddFilesModel getFile;
            for (int j = 0; j < files.size(); j++) {
                filesModel = new AddFilesModel();
                filesModel = files.get(j);
                int fileId = DatabaseHandler.getInstance(MainMenuActivity.this).getFile(filesModel.getfId(), filesModel.getfVersion(), filesModel.getfCategoryId());
                if (fileId == 0) {
                    //DatabaseHandler.getInstance(MainMenuActivity.this).addFiles(filesModel);
                    try {
                        URL url = new URL(filesModel.getfPath().replace(" ", "%20"));
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setDoOutput(true);
                        // connect
                        urlConnection.connect();
                        int lenghtOfFile = urlConnection.getContentLength();
                        File file = new File("/sdcard/" + folder_main + "/", filesModel.getfName());
                        FileOutputStream fileOutput = new FileOutputStream(file);
                        // Stream used for reading the data from the internet
                        InputStream inputStream = urlConnection.getInputStream();
                        byte data[] = new byte[1024];
                        long total = 0;
                        while ((count = inputStream.read(data)) != -1) {
                            total += count;
                            publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                            fileOutput.write(data, 0, count);
                        }
                        fileOutput.flush();
                        fileOutput.close();
                        inputStream.close();
                        DatabaseHandler.getInstance(MainMenuActivity.this).addFiles(filesModel);

                    } catch (final MalformedURLException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplication(), filesModel.getfTitle() + " not exist!", Toast.LENGTH_LONG).show();

                            }
                        });
                    } catch (final IOException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplication(), " not exist!", Toast.LENGTH_LONG).show();

                            }
                        });
                    } catch (final Exception e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplication(), "Failed to download PDF. Please check your internet connection.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pdialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);
        }
    }

    public void getAllFiles(String url) {
        showpDialog();
        filesList = new ArrayList<AddFilesModel>();
        final JsonArrayRequest jsonObjReq1 = new
                JsonArrayRequest(url, // DICDUXJHSNGHAJRSZNNJVQQP  admin@gmail.com
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                AddFilesModel addFiles = new AddFilesModel();
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                int field = jsonObject.getInt("FileId");
                                String title = jsonObject.getString("Title");
                                String name = jsonObject.getString("Name");
                                String path = jsonObject.getString("Path");
                                int version = jsonObject.getInt("Version");
                                int categoryId = jsonObject.getInt("CategoryId");

                                JSONObject category = jsonObject.getJSONObject("Category");
                                int CategoryId = category.getInt("CategoryId");
                                String Name = category.getString("Name");
                                int Identifier = category.getInt("Identifier");

                                addFiles.setfId(field);
                                addFiles.setfTitle(title);
                                addFiles.setfName(name);
                                addFiles.setfPath(path);
                                addFiles.setfVersion(version);
                                addFiles.setfCategoryId(categoryId);

                                FilesCategory filesCategory = new FilesCategory();
                                filesCategory.setCategoryId(CategoryId);
                                filesCategory.setName(Name);
                                filesCategory.setIdentifier(Identifier);

                                addFiles.setFilesCategory(filesCategory);

                                int fileId = DatabaseHandler.getInstance(MainMenuActivity.this).getFileById(field);
                                if (fileId > 0) {
                                    AddFilesModel model = DatabaseHandler.getInstance(MainMenuActivity.this).getAllFileModel(field);
                                    if (model.getfVersion() < version) {
                                        if (DatabaseHandler.getInstance(MainMenuActivity.this).deletFile(field, categoryId) > 0) {
                                            filesList.add(addFiles);
                                        }
                                    }
                                } else {
                                    filesList.add(addFiles);
                                }
                            }
                            hidepDialog();
                            DownloadFileFromURL aa = new DownloadFileFromURL(filesList);
                            aa.execute();
                        } catch (JSONException e) {
                            hidepDialog();
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hidepDialog();
//
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(MainMenuActivity.this, "Network TimeoutError ",Toast.LENGTH_LONG).show();
//                } else if (error instanceof AuthFailureError) {
//                    //TODO
//                } else if (error instanceof ServerError) {
//                    //TODO
//                } else if (error instanceof NetworkError) {
//                    //TODO
//                } else {
//                    if (error instanceof ParseError) {
//                        //TODO
//                    }
//                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq1);
    }

    public void getTableOfContents(String url) {
        showpDialog();
        final JsonArrayRequest jsonObjReq1 = new
                JsonArrayRequest(url, // DICDUXJHSNGHAJRSZNNJVQQP  admin@gmail.com
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());
                        int finleid_ = 0;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                tableOfContentsModel = new TableOfContentsModel();
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                int topicId = jsonObject.getInt("TopicId");
                                String name = jsonObject.getString("Name");
                                int pageNo = jsonObject.getInt("PageNo");
                                String parentId = jsonObject.getString("ParentId");
                                int fileId = jsonObject.getInt("FileId");
                                finleid_ = jsonObject.getInt("FileId");
                                tableOfContentsModel.setTopicId(topicId);
                                tableOfContentsModel.setName(name);
                                tableOfContentsModel.setPageNo(pageNo);
                                tableOfContentsModel.setFileId(fileId);
                                if (parentId.equals("null") || parentId == null) {
                                    tableOfContentsModel.setParentId(0);
                                } else {
                                    tableOfContentsModel.setParentId(Integer.parseInt(parentId.toString()));
                                }
                                JSONObject fileObject = jsonObject.getJSONObject("File");
                                int file_fileId = fileObject.getInt("FileId");
                                String file_title = fileObject.getString("Title");

                                String file_name = fileObject.getString("Name");
                                String file_path = fileObject.getString("Path");
                                int file_version = fileObject.getInt("Version");
                                int file_categoryId = fileObject.getInt("CategoryId");
                                String file_category = fileObject.getString("Category");
                                String file_categories = fileObject.getString("Categories");
                                String file_file = fileObject.getString("File");

                                TableOfContentsFileModel model = DatabaseHandler.getInstance(MainMenuActivity.this).getTableOfContentsFileModel(fileId);
                                if (model.getVersion() < file_version) {
                                    DatabaseHandler.getInstance(MainMenuActivity.this).deletTableContentsFile(file_fileId);
                                    tableOfContentsFileModel = new TableOfContentsFileModel();
                                    tableOfContentsFileModel.setFileId(file_fileId);
                                    tableOfContentsFileModel.setTitle(file_title);
                                    tableOfContentsFileModel.setName(file_name);
                                    tableOfContentsFileModel.setPath(file_path);
                                    tableOfContentsFileModel.setVersion(file_version);
                                    tableOfContentsFileModel.setCategoryId(file_categoryId);
                                    tableOfContentsFileModel.setCategory(file_category);
                                    tableOfContentsFileModel.setCategories(file_categories);
                                    tableOfContentsFileModel.setFile(file_file);
                                    DatabaseHandler.getInstance(MainMenuActivity.this).addTableOfContentsFile(tableOfContentsFileModel);
                                }
                                if (DatabaseHandler.getInstance(MainMenuActivity.this).getTableOfContentsFile(file_fileId, file_version) == 0) {
                                    DatabaseHandler.getInstance(MainMenuActivity.this).addTableOfContentsFile(tableOfContentsFileModel);
                                    JSONObject childObject;
                                    JSONArray childrenArray = jsonObject.getJSONArray("Children");
                                    for (int j = 0; j < childrenArray.length(); j++) {
                                        tableOfContentsChildrenModel = new TableOfContentsChildrenModel();
                                        childObject = childrenArray.getJSONObject(j);
                                        int child_topicId = childObject.getInt("TopicId");
                                        String child_name = childObject.getString("Name");
                                        int child_pageNo = childObject.getInt("PageNo");
                                        int child_parentId = childObject.getInt("ParentId");
                                        int child_fileId = childObject.getInt("FileId");
                                        String child_file = childObject.getString("File");

                                        tableOfContentsChildrenModel.setTopicId(child_topicId);
                                        tableOfContentsChildrenModel.setName(child_name);
                                        tableOfContentsChildrenModel.setPageNo(child_pageNo);
                                        tableOfContentsChildrenModel.setParentId(child_parentId);
                                        tableOfContentsChildrenModel.setFileId(child_fileId);
                                        tableOfContentsChildrenModel.setFile(child_file);

                                        DatabaseHandler.getInstance(MainMenuActivity.this).addTableOfContentsChildren(tableOfContentsChildrenModel);

                                        JSONArray subChildrenArray = childObject.getJSONArray("Children");
                                        JSONObject subChildObject;

                                        for (int k = 0; k < subChildrenArray.length(); k++) {
                                            subChildObject = subChildrenArray.getJSONObject(j);

                                            tableOfContentsSubChildrenModel = new TableOfContentsSubChildrenModel();
                                            //JSONObject subChildObject = file1Object.getJSONObject("Children");
                                            int sub_child_topicId = subChildObject.getInt("TopicId");
                                            String sub_child_name = subChildObject.getString("Name");
                                            int sub_child_pageNo = subChildObject.getInt("PageNo");
                                            int sub_child_parentId = subChildObject.getInt("ParentId");
                                            int sub_child_fileId = subChildObject.getInt("FileId");
                                            String sub_child_file = childObject.getString("File");

                                            tableOfContentsSubChildrenModel.setTopicId(sub_child_topicId);
                                            tableOfContentsSubChildrenModel.setName(sub_child_name);
                                            tableOfContentsSubChildrenModel.setPageNo(sub_child_pageNo);
                                            tableOfContentsSubChildrenModel.setParentId(sub_child_parentId);
                                            tableOfContentsSubChildrenModel.setFileId(sub_child_fileId);
                                            tableOfContentsSubChildrenModel.setFile(sub_child_file);
                                            //tableOfContentsSubChildrenModelList.add(tableOfContentsSubChildrenModel);
                                            DatabaseHandler.getInstance(MainMenuActivity.this).addTableOfContentsSubChildren(tableOfContentsSubChildrenModel);
                                            //}
                                        }
                                    }
                                }
                            }
                            List<TableOfContentsModel> mList = new ArrayList<TableOfContentsModel>();
                            mList = DatabaseHandler.getInstance(MainMenuActivity.this).getTableOfContentsModels(finleid_);
                            if (mList.size() > 0) {
                                startActivity(content_intent);
                            } else {
                                startActivity(book_intent);
                            }
                            hidepDialog();

                        } catch (JSONException e) {
                            hidepDialog();
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq1);
    }
}
