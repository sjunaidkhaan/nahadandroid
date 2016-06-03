package com.ingentive.nahad.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ingentive.nahad.model.AddFilesModel;
import com.ingentive.nahad.model.FilesCategory;
import com.ingentive.nahad.model.GlossaryModel;
import com.ingentive.nahad.model.TableOfContentsChildrenModel;
import com.ingentive.nahad.model.TableOfContentsFileModel;
import com.ingentive.nahad.model.TableOfContentsModel;
import com.ingentive.nahad.model.TableOfContentsSubChildrenModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 10-05-2016.
 */
public class JSONRequests {
    ProgressDialog pDialog;
    Context mContext;
    List<AddFilesModel> filesList;
    String contentsResponse;

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    DefaultHttpClient httpClient;
    HttpEntity httpEntity = null;
    HttpResponse httpResponse = null;
    List<TableOfContentsModel> tableOfContentsModelList;
    TableOfContentsModel tableOfContentsModel;
    TableOfContentsFileModel tableOfContentsFileModel;
    TableOfContentsChildrenModel tableOfContentsChildrenModel;
    TableOfContentsSubChildrenModel tableOfContentsSubChildrenModel;
    List<TableOfContentsSubChildrenModel> tableOfContentsSubChildrenModelList;
    List<TableOfContentsChildrenModel> tableOfContentsChildrenModelList;

    public JSONRequests(Context context) {
        this.mContext = context;
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        filesList = new ArrayList<AddFilesModel>();
        httpClient = new DefaultHttpClient();
        tableOfContentsModelList = new ArrayList<TableOfContentsModel>();
        tableOfContentsSubChildrenModelList = new ArrayList<TableOfContentsSubChildrenModel>();
        tableOfContentsChildrenModelList = new ArrayList<TableOfContentsChildrenModel>();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();

    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    class serviceCall extends AsyncTask<String, Void, String> {

        int method;
        private String URL;

        public serviceCall(String url, int method) {
            this.URL = url;
            this.method = method;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                //showpDialog();
                //URL mURL= new URL(URL);
                if (method == POST) {
                    HttpPost httpPost = new HttpPost(URL);
                    httpResponse = httpClient.execute(httpPost);

                } else if (method == GET) {
                    HttpGet httpGet = new HttpGet(URL);

                    httpResponse = httpClient.execute(httpGet);

                }
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                //hidepDialog();
                return response;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //hidepDialog();
        }
    }

    public List<AddFilesModel> getAllFiles(String url, int method) {
        showpDialog();

        try {
            //String contentsResponse = makeServiceCall(url, method);
            contentsResponse = new serviceCall(url, method).execute().get();
            JSONArray responseArray = new JSONArray(contentsResponse);
            for (int i = 0; i < responseArray.length(); i++) {
                AddFilesModel addFiles = new AddFilesModel();
                JSONObject jsonObject = (JSONObject) responseArray.get(i);
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
                filesList.add(addFiles);
                // downloadAndOpenPDF(path);
            }
            hidepDialog();
            //downloadAndOpenPDF(filesList);
        } catch (JSONException e) {
            hidepDialog();

            e.printStackTrace();
        } catch (Exception e) {
            hidepDialog();
        }
        return filesList;
    }

    public List<AddFilesModel> getAllFiles(String url) {
        showpDialog();
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
                                filesList.add(addFiles);
                                // downloadAndOpenPDF(path);
                            }
                            hidepDialog();
                            //downloadAndOpenPDF(filesList);
                        } catch (JSONException e) {
                            hidepDialog();
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(mContext,
//                        "Error: " + error
//                                + ">>" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq1);
        return filesList;
    }

    public void getGlossary(String url) {
        showpDialog();
        final JsonArrayRequest jsonObjReq1 = new
                JsonArrayRequest(url, // DICDUXJHSNGHAJRSZNNJVQQP  admin@gmail.com
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());
                        try {
                            GlossaryModel glossaryModel;
                            for (int i = 0; i < response.length(); i++) {
                                AddFilesModel addFiles = new AddFilesModel();
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                int glossaryId = jsonObject.getInt("GlossaryId");
                                String alphabet = jsonObject.getString("Alphabet");
                                String word = jsonObject.getString("Word");
                                String definition = jsonObject.getString("Definition");
                                String file = jsonObject.getString("File");

                                glossaryModel = new GlossaryModel();
                                glossaryModel.setGlossaryId(glossaryId);
                                glossaryModel.setAlphabet(alphabet);
                                glossaryModel.setWord(word);
                                glossaryModel.setDefinition(definition);
                                glossaryModel.setFile(file);

                                int id = DatabaseHandler.getInstance(mContext).getGlossary(glossaryId);
                                if (id == 0) {
                                    DatabaseHandler.getInstance(mContext).addGlossary(glossaryModel);
                                }
                            }
                            hidepDialog();
                            //downloadAndOpenPDF(filesList);

                        } catch (JSONException e) {
                            hidepDialog();
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq1);
    }


    public List<TableOfContentsModel> tableOfContents(String url) {
        showpDialog();

        try {
            contentsResponse = new serviceCall(url, 1).execute().get();
            JSONArray responseArray = new JSONArray(contentsResponse);
            TableOfContentsModel tableOfContentsModel;
            for (int i = 0; i < responseArray.length(); i++) {
                tableOfContentsModel = new TableOfContentsModel();
                JSONObject jsonObject = (JSONObject) responseArray.get(i);
                int topicId = jsonObject.getInt("TopicId");
                String name = jsonObject.getString("Name");
                int pageNo = jsonObject.getInt("PageNo");
                String parentId = jsonObject.getString("ParentId");
                int fileId = jsonObject.getInt("FileId");

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
                tableOfContentsModel.setTableOfContentsFileModel(tableOfContentsFileModel);


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
                        tableOfContentsSubChildrenModelList.add(tableOfContentsSubChildrenModel);
                    }
                }
                tableOfContentsChildrenModel.setSubChildrendArray(tableOfContentsSubChildrenModelList);
                tableOfContentsChildrenModelList.add(tableOfContentsChildrenModel);
                tableOfContentsModel.setChildrenArray(tableOfContentsChildrenModelList);
                tableOfContentsModelList.add(tableOfContentsModel);
            }
            hidepDialog();
            //downloadAndOpenPDF(filesList);

        } catch (JSONException e) {
            hidepDialog();
            e.printStackTrace();
        }catch (Exception e){

        }
        hidepDialog();
        return tableOfContentsModelList;
    }

//    public List<TableOfContentsModel> tableOfContents(String url) {
//        showpDialog();
//        final JsonArrayRequest jsonObjReq1 = new
//                JsonArrayRequest(url, // DICDUXJHSNGHAJRSZNNJVQQP  admin@gmail.com
//                new com.android.volley.Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("TAG", response.toString());
//                        try {
//                            TableOfContentsModel tableOfContentsModel;
//                            for (int i = 0; i < response.length(); i++) {
//
//                                tableOfContentsModel = new TableOfContentsModel();
//                                JSONObject jsonObject = (JSONObject) response.get(i);
//                                int topicId = jsonObject.getInt("TopicId");
//                                String name = jsonObject.getString("Name");
//                                int pageNo = jsonObject.getInt("PageNo");
//                                String parentId = jsonObject.getString("ParentId");
//                                int fileId = jsonObject.getInt("FileId");
//
//                                tableOfContentsModel.setTopicId(topicId);
//                                tableOfContentsModel.setName(name);
//                                tableOfContentsModel.setPageNo(pageNo);
//                                tableOfContentsModel.setFileId(fileId);
//                                if (parentId.equals("null") || parentId == null) {
//                                    tableOfContentsModel.setParentId(0);
//                                } else {
//                                    tableOfContentsModel.setParentId(Integer.parseInt(parentId.toString()));
//                                }
//
//
//                                JSONObject fileObject = jsonObject.getJSONObject("File");
//                                int file_fileId = fileObject.getInt("FileId");
//                                String file_title = fileObject.getString("Title");
//
//                                String file_name = fileObject.getString("Name");
//                                String file_path = fileObject.getString("Path");
//                                int file_version = fileObject.getInt("Version");
//                                int file_categoryId = fileObject.getInt("CategoryId");
//                                String file_category = fileObject.getString("Category");
//                                String file_categories = fileObject.getString("Categories");
//                                String file_file = fileObject.getString("File");
//
//                                tableOfContentsFileModel = new TableOfContentsFileModel();
//                                tableOfContentsFileModel.setFileId(file_fileId);
//                                tableOfContentsFileModel.setTitle(file_title);
//                                tableOfContentsFileModel.setName(file_name);
//                                tableOfContentsFileModel.setPath(file_path);
//                                tableOfContentsFileModel.setVersion(file_version);
//                                tableOfContentsFileModel.setCategoryId(file_categoryId);
//                                tableOfContentsFileModel.setCategory(file_category);
//                                tableOfContentsFileModel.setCategories(file_categories);
//                                tableOfContentsFileModel.setFile(file_file);
//                                tableOfContentsModel.setTableOfContentsFileModel(tableOfContentsFileModel);
//
//
//                                JSONObject childObject;
//                                JSONArray childrenArray = jsonObject.getJSONArray("Children");
//                                for (int j = 0; j < childrenArray.length(); j++) {
//                                    tableOfContentsChildrenModel = new TableOfContentsChildrenModel();
//                                    childObject = childrenArray.getJSONObject(j);
//                                    int child_topicId = childObject.getInt("TopicId");
//                                    String child_name = childObject.getString("Name");
//                                    int child_pageNo = childObject.getInt("PageNo");
//                                    int child_parentId = childObject.getInt("ParentId");
//                                    int child_fileId = childObject.getInt("FileId");
//                                    String child_file = childObject.getString("File");
//
//                                    tableOfContentsChildrenModel.setTopicId(child_topicId);
//                                    tableOfContentsChildrenModel.setName(child_name);
//                                    tableOfContentsChildrenModel.setPageNo(child_pageNo);
//                                    tableOfContentsChildrenModel.setParentId(child_parentId);
//                                    tableOfContentsChildrenModel.setFileId(child_fileId);
//                                    tableOfContentsChildrenModel.setFile(child_file);
//
//                                    JSONArray subChildrenArray = childObject.getJSONArray("Children");
//                                    JSONObject subChildObject;
//
//                                    for (int k = 0; k < subChildrenArray.length(); k++) {
//                                        subChildObject = subChildrenArray.getJSONObject(j);
//
//                                        tableOfContentsSubChildrenModel = new TableOfContentsSubChildrenModel();
//                                        //JSONObject subChildObject = file1Object.getJSONObject("Children");
//                                        int sub_child_topicId = subChildObject.getInt("TopicId");
//                                        String sub_child_name = subChildObject.getString("Name");
//                                        int sub_child_pageNo = subChildObject.getInt("PageNo");
//                                        int sub_child_parentId = subChildObject.getInt("ParentId");
//                                        int sub_child_fileId = subChildObject.getInt("FileId");
//                                        String sub_child_file = childObject.getString("File");
//
//                                        tableOfContentsSubChildrenModel.setTopicId(sub_child_topicId);
//                                        tableOfContentsSubChildrenModel.setName(sub_child_name);
//                                        tableOfContentsSubChildrenModel.setPageNo(sub_child_pageNo);
//                                        tableOfContentsSubChildrenModel.setParentId(sub_child_parentId);
//                                        tableOfContentsSubChildrenModel.setFileId(sub_child_fileId);
//                                        tableOfContentsSubChildrenModel.setFile(sub_child_file);
//                                        tableOfContentsSubChildrenModelList.add(tableOfContentsSubChildrenModel);
//                                        //DatabaseHandler.getInstance(mContext).addTableOfContentsSubChildren(tableOfContentsSubChildrenModel);
//                                    }
//                                }
//                                tableOfContentsChildrenModel.setSubChildrendArray(tableOfContentsSubChildrenModelList);
//                                tableOfContentsChildrenModelList.add(tableOfContentsChildrenModel);
//                                tableOfContentsModel.setChildrenArray(tableOfContentsChildrenModelList);
//                                tableOfContentsModelList.add(tableOfContentsModel);
//                                //}
//                                //}
//                                //}
//
//                                //}
//
//
////                                int id = DatabaseHandler.getInstance(mContext).getTableOfContents(topicId);
////                                if (id == 0) {
////                                    long result = DatabaseHandler.getInstance(mContext).addTableOfContents(tableOfContentsModel);
////                                    if (result > 0) {
////                                        TableOfContentsFileModel tableOfContentsFileModel = new TableOfContentsFileModel();
////                                        JSONObject fileObject = jsonObject.getJSONObject("File");
////                                        int file_fileId = fileObject.getInt("FileId");
////                                        String file_title = fileObject.getString("Title");
////
////                                        String file_name = fileObject.getString("Name");
////                                        String file_path = fileObject.getString("Path");
////                                        int file_version = fileObject.getInt("Version");
////                                        int file_categoryId = fileObject.getInt("CategoryId");
////                                        String file_category = fileObject.getString("Category");
////                                        String file_categories = fileObject.getString("Categories");
////                                        String file_file = fileObject.getString("File");
////
////                                        int fileid = DatabaseHandler.getInstance(mContext).getTableOfContentsFile(file_fileId);
////                                        if (fileid == 0) {
////                                            tableOfContentsFileModel.setFileId(file_fileId);
////                                            tableOfContentsFileModel.setTitle(file_title);
////                                            tableOfContentsFileModel.setName(file_name);
////                                            tableOfContentsFileModel.setPath(file_path);
////                                            tableOfContentsFileModel.setVersion(file_version);
////                                            tableOfContentsFileModel.setCategoryId(file_categoryId);
////                                            tableOfContentsFileModel.setCategory(file_category);
////                                            tableOfContentsFileModel.setCategories(file_categories);
////                                            tableOfContentsFileModel.setFile(file_file);
////
////                                            DatabaseHandler.getInstance(mContext).addTableOfContentsFile(tableOfContentsFileModel);
////                                        }
//
//
////                                        TableOfContentsChildrenModel tableOfContentsChildrenModel;
////                                        JSONObject childObject;
////                                        JSONArray childrenArray = jsonObject.getJSONArray("Children");
////                                        for (int j = 0; j < childrenArray.length(); j++) {
////                                            tableOfContentsChildrenModel = new TableOfContentsChildrenModel();
////                                            childObject = childrenArray.getJSONObject(j);
////                                            int child_topicId = childObject.getInt("TopicId");
////                                            String child_name = childObject.getString("Name");
////                                            int child_pageNo = childObject.getInt("PageNo");
////                                            int child_parentId = childObject.getInt("ParentId");
////                                            int child_fileId = childObject.getInt("FileId");
////                                            String child_file = childObject.getString("File");
////
////                                            int child_id = DatabaseHandler.getInstance(mContext).getTableOfContentsChildren(child_topicId, child_parentId, child_fileId);
////                                            if (child_id == 0) {
////                                                tableOfContentsChildrenModel.setTopicId(child_topicId);
////                                                tableOfContentsChildrenModel.setName(child_name);
////                                                tableOfContentsChildrenModel.setPageNo(child_pageNo);
////                                                tableOfContentsChildrenModel.setParentId(child_parentId);
////                                                tableOfContentsChildrenModel.setFileId(child_fileId);
////                                                tableOfContentsChildrenModel.setFile(child_file);
////
////                                                long childrenId = DatabaseHandler.getInstance(mContext).addTableOfContentsChildren(tableOfContentsChildrenModel);
////                                                if (childrenId > 0) {
////                                                    JSONArray subChildrenArray = childObject.getJSONArray("Children");
////                                                    JSONObject subChildObject;
////                                                    TableOfContentsSubChildrenModel tableOfContentsSubChildrenModel;
////                                                    for (int k = 0; k < subChildrenArray.length(); k++) {
////                                                        subChildObject = subChildrenArray.getJSONObject(j);
////
////                                                        tableOfContentsSubChildrenModel = new TableOfContentsSubChildrenModel();
////                                                        //JSONObject subChildObject = file1Object.getJSONObject("Children");
////                                                        int sub_child_topicId = subChildObject.getInt("TopicId");
////                                                        String sub_child_name = subChildObject.getString("Name");
////                                                        int sub_child_pageNo = subChildObject.getInt("PageNo");
////                                                        int sub_child_parentId = subChildObject.getInt("ParentId");
////                                                        int sub_child_fileId = subChildObject.getInt("FileId");
////                                                        String sub_child_file = childObject.getString("File");
////
////                                                        tableOfContentsSubChildrenModel.setTopicId(sub_child_topicId);
////                                                        tableOfContentsSubChildrenModel.setName(sub_child_name);
////                                                        tableOfContentsSubChildrenModel.setPageNo(sub_child_pageNo);
////                                                        tableOfContentsSubChildrenModel.setParentId(sub_child_parentId);
////                                                        tableOfContentsSubChildrenModel.setFileId(sub_child_fileId);
////                                                        tableOfContentsSubChildrenModel.setFile(sub_child_file);
////                                                        DatabaseHandler.getInstance(mContext).addTableOfContentsSubChildren(tableOfContentsSubChildrenModel);
////                                                    }
////                                                }
////                                            //}
////                                        //}
////                                    }
////
////                                }
//                            }
//                            hidepDialog();
//                            //downloadAndOpenPDF(filesList);
//
//                        } catch (JSONException e) {
//                            hidepDialog();
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(mContext, "Error: " + error
//                        + ">>" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
//                hidepDialog();
//            }
//        });
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq1);
//        return tableOfContentsModelList;
//    }
}
