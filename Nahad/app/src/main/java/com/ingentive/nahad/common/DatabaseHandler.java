package com.ingentive.nahad.common;

/**
 * Created by PC on 06-05-2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ingentive.nahad.model.AddFilesModel;
import com.ingentive.nahad.model.FilesCategory;
import com.ingentive.nahad.model.GlossaryModel;
import com.ingentive.nahad.model.TableOfContentsChildrenModel;
import com.ingentive.nahad.model.TableOfContentsFileModel;
import com.ingentive.nahad.model.TableOfContentsModel;
import com.ingentive.nahad.model.TableOfContentsSubChildrenModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler mInstance;

    public static synchronized DatabaseHandler getInstance(Context context) {
        if (mInstance == null || mInstance.equals("")) {
            mInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return mInstance;
    }

    public DatabaseHandler(Context context) {
        super(context, Const.DATABASE_NAME, null, Const.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Const.CREATE_TABLE_ALL_FILES);
        db.execSQL(Const.CREATE_TABLE_CATEGORY);
        db.execSQL(Const.CREATE_TABLE_GLOSSARY);
        db.execSQL(Const.CREATE_TABLE_TABLE_OF_CONTENTS);
        db.execSQL(Const.CREATE_TABLE_FILE);
        db.execSQL(Const.CREATE_TABLE_CHILDREN);
        db.execSQL(Const.CREATE_TABLE_SUB_CHILDREN);
        db.execSQL(Const.CREATE_TABLE_BOOK_MARK);
        db.execSQL(Const.CREATE_TABLE_SEND_EMAIL);

        //db.execSQL(Const.CREATE_TABLE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_ALL_FILES);
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_GLOSSARY);
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_TABLE_OF_CONTENTS);
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_FILE);
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_CHILDREN);
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_SUB_CHILDREN);
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_BOOK_MARK);
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_SEND_EMAIL);

    }

    public void addBookMark(int fileId, int pageNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.FILE_ID, fileId);
        values.put(Const.PAGE_NO, pageNo);
        db.insert(Const.TABLE_BOOK_MARK, null, values);
        db.close();
    }

    public void addSendEmail(int fileId, int pageNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.FILE_ID, fileId);
        values.put(Const.PAGE_NO, pageNo);
        db.insert(Const.TABLE_SEND_EMAIL, null, values);
        db.close();
    }

    public boolean getBookMark(int fileId, int pageNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + Const.TABLE_BOOK_MARK + " WHERE " + Const.FILE_ID + "=" + fileId + " AND " + Const.PAGE_NO + "=" + pageNo;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public List<Integer> getBookMarkAll(int fileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Integer> intList = new ArrayList<Integer>();
        String selectQuery = "SELECT * FROM " + Const.TABLE_BOOK_MARK + " WHERE " + Const.FILE_ID + "=" + fileId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                int pageNo = cursor.getInt(1);

                intList.add(pageNo);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return intList;
    }

    public boolean getSendEmail(int fileId, int pageNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + Const.TABLE_SEND_EMAIL + " WHERE " + Const.FILE_ID + "=" + fileId + " AND " + Const.PAGE_NO + "=" + pageNo;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public void removeBookMark(int fileId, int pageNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table_name = Const.TABLE_BOOK_MARK;
        String where = Const.FILE_ID + "=" + fileId + " AND " + Const.PAGE_NO + "=" + pageNo;
        db.delete(table_name, where, null);
    }

    public void removeSendEmail(int fileId, int pageNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table_name = Const.TABLE_SEND_EMAIL;
        String where = Const.FILE_ID + "=" + fileId + " AND " + Const.PAGE_NO + "=" + pageNo;
        db.delete(table_name, where, null);
    }

    public String addFiles(AddFilesModel addFile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.FILE_ID, addFile.getfId());
        values.put(Const.TITLE, addFile.getfTitle());
        values.put(Const.NAME, addFile.getfName());
        values.put(Const.PATH, addFile.getfPath());
        values.put(Const.VERSION, addFile.getfVersion());
        values.put(Const.CATEGORY_ID, addFile.getfCategoryId());
        long a = db.insert(Const.TABLE_ALL_FILES, null, values);
        if (a > 0) {
            FilesCategory category = new FilesCategory();
            category = addFile.getFilesCategory();
            category.setCategoryId(category.getCategoryId());
            category.setName(category.getName());
            category.setCategoryId(category.getIdentifier());
            addCategory(category);
        }

        db.close();
        return "Success";
    }

    public String addFiles(List<AddFilesModel> filesList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        AddFilesModel addFile;
        for (int i = 0; i < filesList.size(); i++) {
            values = new ContentValues();
            addFile = new AddFilesModel();

            addFile = filesList.get(i);
            values.put(Const.FILE_ID, addFile.getfId());
            values.put(Const.TITLE, addFile.getfTitle());
            values.put(Const.NAME, addFile.getfName());
            values.put(Const.PATH, addFile.getfPath());
            values.put(Const.VERSION, addFile.getfVersion());
            values.put(Const.CATEGORY_ID, addFile.getfCategoryId());
            long a = db.insert(Const.TABLE_ALL_FILES, null, values);
            if (a > 0) {
                FilesCategory category = new FilesCategory();
                category = addFile.getFilesCategory();
//                category.setCategoryId(category.getCategoryId());
//                category.setName(category.getName());
//                category.setCategoryId(category.getIdentifier());
                addCategory(category);
            }
        }

        db.close();
        return "Success";
    }

    public void addCategory(FilesCategory category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.CATEGORY_ID, category.getCategoryId());
        values.put(Const.NAME, category.getName());
        values.put(Const.IDENTIFIER, category.getIdentifier());
        long a = db.insert(Const.TABLE_CATEGORY, null, values);
        db.close();
    }

    public long addTableOfContents(TableOfContentsModel tableOfContentsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.TOPIC_ID, tableOfContentsModel.getTopicId());
        values.put(Const.NAME, tableOfContentsModel.getName());
        values.put(Const.PAGE_NO, tableOfContentsModel.getPageNo());
        values.put(Const.PARENT_ID, tableOfContentsModel.getParentId());
        values.put(Const.FILE_ID, tableOfContentsModel.getFileId());
        long a = db.insert(Const.TABLE_TABLE_OF_CONTENTS, null, values);
        db.close();
        return a;
    }

    public int getTableOfContents(int topicId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        String selectQuery = "SELECT * FROM " + Const.TABLE_TABLE_OF_CONTENTS + " WHERE " + Const.TOPIC_ID + "=" + topicId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
            db.close();
            return id;
        }
        cursor.close();
        db.close();
        return id;
    }

    public int getTableOfContents(int topicId, int fileId, int pageNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        String selectQuery = "SELECT * FROM " + Const.TABLE_TABLE_OF_CONTENTS + " WHERE " + Const.TOPIC_ID + "=" + topicId +
                " AND " + Const.FILE_ID + "=" + fileId + " AND " + Const.PAGE_NO + "=" + pageNo;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
            db.close();
            return id;
        }
        cursor.close();
        db.close();
        return id;
    }

    public TableOfContentsModel getTableOfContentsModel(int fileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        TableOfContentsModel tableOfContentsModel = new TableOfContentsModel();
        String selectQuery = "SELECT * FROM " + Const.TABLE_TABLE_OF_CONTENTS + " WHERE " + Const.FILE_ID + "=" + fileId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            tableOfContentsModel = new TableOfContentsModel();
            tableOfContentsModel.setTopicId(Integer.parseInt(cursor.getString(0)));
            tableOfContentsModel.setName(cursor.getString(1));
            tableOfContentsModel.setPageNo(Integer.parseInt(cursor.getString(2)));
            tableOfContentsModel.setParentId(Integer.parseInt(cursor.getString(3)));
            tableOfContentsModel.setFileId(Integer.parseInt(cursor.getString(4)));
        }
        cursor.close();
        db.close();
        return tableOfContentsModel;
    }

    public List<TableOfContentsModel> getTableOfContents() {
        List<TableOfContentsModel> tableOfContentsModelList = new ArrayList<TableOfContentsModel>();
//        String selectQuery = "SELECT * FROM " + Const.TABLE_TABLE_OF_CONTENTS;
        String selectQuery = "SELECT * FROM " + Const.TABLE_TABLE_OF_CONTENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        TableOfContentsModel tableOfContentsModel;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tableOfContentsModel = new TableOfContentsModel();
                tableOfContentsModel.setTopicId(Integer.parseInt(cursor.getString(0)));
                tableOfContentsModel.setName(cursor.getString(1));
                tableOfContentsModel.setPageNo(Integer.parseInt(cursor.getString(2)));
                tableOfContentsModel.setParentId(Integer.parseInt(cursor.getString(3)));
                tableOfContentsModel.setFileId(Integer.parseInt(cursor.getString(4)));

                tableOfContentsModelList.add(tableOfContentsModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tableOfContentsModelList;
    }

    public List<TableOfContentsModel> getTableOfContentsModels(int filId) {
        List<TableOfContentsModel> tableOfContentsModelList = new ArrayList<TableOfContentsModel>();
//        String selectQuery = "SELECT * FROM " + Const.TABLE_TABLE_OF_CONTENTS;
        String selectQuery = "SELECT * FROM " + Const.TABLE_TABLE_OF_CONTENTS + " WHERE " + Const.FILE_ID + "=" + filId;
        SQLiteDatabase db = this.getReadableDatabase();
        TableOfContentsModel tableOfContentsModel;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tableOfContentsModel = new TableOfContentsModel();
                tableOfContentsModel.setTopicId(Integer.parseInt(cursor.getString(0)));
                tableOfContentsModel.setName(cursor.getString(1));
                tableOfContentsModel.setPageNo(Integer.parseInt(cursor.getString(2)));
                tableOfContentsModel.setParentId(Integer.parseInt(cursor.getString(3)));
                tableOfContentsModel.setFileId(Integer.parseInt(cursor.getString(4)));

                tableOfContentsModelList.add(tableOfContentsModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tableOfContentsModelList;
    }

    public List<TableOfContentsFileModel> getTableOfContentsFileModels(int fileId) {
        List<TableOfContentsFileModel> tableOfContentsFileModelList = new ArrayList<TableOfContentsFileModel>();
//        String selectQuery = "SELECT * FROM " + Const.TABLE_FILE;
        String selectQuery = "SELECT * FROM " + Const.TABLE_FILE + " WHERE " + Const.FILE_ID + "=" + fileId;
        SQLiteDatabase db = this.getReadableDatabase();
        TableOfContentsFileModel tableOfContentsFileModel;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tableOfContentsFileModel = new TableOfContentsFileModel();
                tableOfContentsFileModel.setFileId(Integer.parseInt(cursor.getString(0)));
                tableOfContentsFileModel.setTitle(cursor.getString(1));
                tableOfContentsFileModel.setName(cursor.getString(2));
                tableOfContentsFileModel.setPath(cursor.getString(3));
                tableOfContentsFileModel.setVersion(Integer.parseInt(cursor.getString(4)));
                tableOfContentsFileModel.setCategoryId(Integer.parseInt(cursor.getString(5)));
                tableOfContentsFileModel.setCategory(cursor.getString(6));
                tableOfContentsFileModel.setCategories(cursor.getString(7));
                tableOfContentsFileModel.setFile(cursor.getString(8));

                tableOfContentsFileModelList.add(tableOfContentsFileModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tableOfContentsFileModelList;
    }

    public TableOfContentsFileModel getTableOfContentsFileModel(int fileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        TableOfContentsFileModel tableOfContentsFileModel = new TableOfContentsFileModel();
        String selectQuery = "SELECT * FROM " + Const.TABLE_FILE + " WHERE " + Const.FILE_ID + "=" + fileId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            tableOfContentsFileModel = new TableOfContentsFileModel();
            tableOfContentsFileModel.setFileId(Integer.parseInt(cursor.getString(0)));
            tableOfContentsFileModel.setTitle(cursor.getString(1));
            tableOfContentsFileModel.setName(cursor.getString(2));
            tableOfContentsFileModel.setPath(cursor.getString(3));
            tableOfContentsFileModel.setVersion(Integer.parseInt(cursor.getString(4)));
            tableOfContentsFileModel.setCategoryId(Integer.parseInt(cursor.getString(5)));
            tableOfContentsFileModel.setCategory(cursor.getString(6));
            tableOfContentsFileModel.setCategories(cursor.getString(7));
            tableOfContentsFileModel.setFile(cursor.getString(8));


        }
        cursor.close();
        db.close();
        return tableOfContentsFileModel;
    }

    public List<TableOfContentsChildrenModel> getTableOfContentsChildren() {
        List<TableOfContentsChildrenModel> tableOfContentsChildrenModelList = new ArrayList<TableOfContentsChildrenModel>();
        String selectQuery = "SELECT * FROM " + Const.TABLE_CHILDREN;
        SQLiteDatabase db = this.getReadableDatabase();
        TableOfContentsChildrenModel tableOfContentsChildrenModel;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tableOfContentsChildrenModel = new TableOfContentsChildrenModel();
                tableOfContentsChildrenModel.setChildrenId(Integer.parseInt(cursor.getString(0)));
                tableOfContentsChildrenModel.setTopicId(Integer.parseInt(cursor.getString(1)));
                tableOfContentsChildrenModel.setName(cursor.getString(2));
                tableOfContentsChildrenModel.setPageNo(Integer.parseInt(cursor.getString(3)));
                tableOfContentsChildrenModel.setParentId(Integer.parseInt(cursor.getString(4)));
                tableOfContentsChildrenModel.setFileId(Integer.parseInt(cursor.getString(5)));
                tableOfContentsChildrenModel.setFile(cursor.getString(6));

                tableOfContentsChildrenModelList.add(tableOfContentsChildrenModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tableOfContentsChildrenModelList;
    }

    public List<TableOfContentsChildrenModel> getTableOfContentsChildrenModels(int parentId, int fileId) {
        List<TableOfContentsChildrenModel> tableOfContentsChildrenModelList = new ArrayList<TableOfContentsChildrenModel>();
        //String selectQuery = "SELECT * FROM " + Const.TABLE_CHILDREN;
        String selectQuery = "SELECT * FROM " + Const.TABLE_CHILDREN + " WHERE " + Const.FILE_ID + "=" + fileId
                + " AND " + Const.PARENT_ID + "=" + parentId;
        SQLiteDatabase db = this.getReadableDatabase();
        TableOfContentsChildrenModel tableOfContentsChildrenModel;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tableOfContentsChildrenModel = new TableOfContentsChildrenModel();
                tableOfContentsChildrenModel.setChildrenId(Integer.parseInt(cursor.getString(0)));
                tableOfContentsChildrenModel.setTopicId(Integer.parseInt(cursor.getString(1)));
                tableOfContentsChildrenModel.setName(cursor.getString(2));
                tableOfContentsChildrenModel.setPageNo(Integer.parseInt(cursor.getString(3)));
                tableOfContentsChildrenModel.setParentId(Integer.parseInt(cursor.getString(4)));
                tableOfContentsChildrenModel.setFileId(Integer.parseInt(cursor.getString(5)));
                tableOfContentsChildrenModel.setFile(cursor.getString(6));

                tableOfContentsChildrenModelList.add(tableOfContentsChildrenModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tableOfContentsChildrenModelList;
    }

    public TableOfContentsChildrenModel getTableOfContentsChildrenModel(int parentId, int fileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        TableOfContentsChildrenModel tableOfContentsChildrenModel = new TableOfContentsChildrenModel();
        String selectQuery = "SELECT * FROM " + Const.TABLE_CHILDREN + " WHERE " + Const.FILE_ID + "=" + fileId +
                " AND " + Const.PARENT_ID + "=" + parentId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            tableOfContentsChildrenModel.setChildrenId(Integer.parseInt(cursor.getString(0)));
            tableOfContentsChildrenModel.setTopicId(Integer.parseInt(cursor.getString(1)));
            tableOfContentsChildrenModel.setName(cursor.getString(2));
            tableOfContentsChildrenModel.setPageNo(Integer.parseInt(cursor.getString(3)));
            tableOfContentsChildrenModel.setParentId(Integer.parseInt(cursor.getString(4)));
            tableOfContentsChildrenModel.setFileId(Integer.parseInt(cursor.getString(5)));
            tableOfContentsChildrenModel.setFile(cursor.getString(6));
        }
        cursor.close();
        db.close();
        return tableOfContentsChildrenModel;
    }

    public TableOfContentsSubChildrenModel getTableOfContentsSubChildrenModel(int parentId, int fileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        TableOfContentsSubChildrenModel tableOfContentsSubChildrenModel = new TableOfContentsSubChildrenModel();
        String selectQuery = "SELECT * FROM " + Const.TABLE_SUB_CHILDREN + " WHERE " + Const.FILE_ID + "=" + fileId +
                " AND " + Const.PARENT_ID + "=" + parentId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            tableOfContentsSubChildrenModel = new TableOfContentsSubChildrenModel();
            tableOfContentsSubChildrenModel.setTopicId(Integer.parseInt(cursor.getString(0)));
            tableOfContentsSubChildrenModel.setName(cursor.getString(1));
            tableOfContentsSubChildrenModel.setPageNo(Integer.parseInt(cursor.getString(2)));
            tableOfContentsSubChildrenModel.setParentId(Integer.parseInt(cursor.getString(3)));
            tableOfContentsSubChildrenModel.setFileId(Integer.parseInt(cursor.getString(4)));
            tableOfContentsSubChildrenModel.setFile(cursor.getString(5));
        }
        cursor.close();
        db.close();
        return tableOfContentsSubChildrenModel;
    }


    public List<TableOfContentsSubChildrenModel> getTableOfContentsSubChildrenModels(int parentId, int fileId) {
        List<TableOfContentsSubChildrenModel> tableOfContentsSubChildrenModelList = new ArrayList<TableOfContentsSubChildrenModel>();
//        String selectQuery = "SELECT * FROM " + Const.TABLE_SUB_CHILDREN;
        String selectQuery = "SELECT * FROM " + Const.TABLE_SUB_CHILDREN + " WHERE " + Const.FILE_ID + "=" + fileId
                + " AND " + Const.PARENT_ID + "=" + parentId;
        SQLiteDatabase db = this.getReadableDatabase();
        TableOfContentsSubChildrenModel tableOfContentsSubChildrenModel;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d("", "" + cursor);
                tableOfContentsSubChildrenModel = new TableOfContentsSubChildrenModel();
                tableOfContentsSubChildrenModel.setTopicId(Integer.parseInt(cursor.getString(0)));
                tableOfContentsSubChildrenModel.setName(cursor.getString(1));
                tableOfContentsSubChildrenModel.setPageNo(Integer.parseInt(cursor.getString(2)));
                tableOfContentsSubChildrenModel.setParentId(Integer.parseInt(cursor.getString(3)));
                tableOfContentsSubChildrenModel.setFileId(Integer.parseInt(cursor.getString(4)));
                tableOfContentsSubChildrenModel.setFile(cursor.getString(5));

                tableOfContentsSubChildrenModelList.add(tableOfContentsSubChildrenModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tableOfContentsSubChildrenModelList;
    }


//    public int getTableOfContentsFile(int fileId, int file_version, int file_categoryId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int id = 0;
//        String selectQuery = "SELECT * FROM " + Const.TABLE_FILE + " WHERE " + Const.FILE_ID + "=" + fileId +
//                " AND " + Const.VERSION + "=" + file_version + " AND " + Const.CATEGORY_ID + "=" + file_categoryId;
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        if (cursor != null && cursor.getCount() != 0) {
//            id = Integer.parseInt(cursor.getString(0));
//            return id;
//        }
//        cursor.close();
//        db.close();
//        return id;
//    }
    public int getTableOfContentsFile(int fileId, int file_version) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        String selectQuery = "SELECT * FROM " + Const.TABLE_FILE + " WHERE " + Const.FILE_ID + "=" + fileId +
                " AND " + Const.VERSION + "=" + file_version ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            id = Integer.parseInt(cursor.getString(0));
            return id;
        }
        cursor.close();
        db.close();
        return id;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Const.TABLE_TABLE_OF_CONTENTS);
        db.execSQL("delete from " + Const.TABLE_FILE);
        db.execSQL("delete from " + Const.TABLE_CHILDREN);
        db.execSQL("delete from " + Const.TABLE_SUB_CHILDREN);
    }

    public int getTableOfContentsChildren(int topicId, int parentId, int fileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        String selectQuery = "SELECT * FROM " + Const.TABLE_CHILDREN + " WHERE " + Const.TOPIC_ID + "=" + topicId + " AND " +
                Const.FILE_ID + "=" + fileId + " AND " + Const.PARENT_ID + "=" + parentId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
            db.close();
            return id;
        }
        cursor.close();
        db.close();
        return id;
    }

    public long addTableOfContentsFile(TableOfContentsFileModel tableOfContentsFileModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.FILE_ID, tableOfContentsFileModel.getFileId());
        values.put(Const.TITLE, tableOfContentsFileModel.getTitle());
        values.put(Const.NAME, tableOfContentsFileModel.getName());
        values.put(Const.PATH, tableOfContentsFileModel.getPath());
        values.put(Const.VERSION, tableOfContentsFileModel.getVersion());
        values.put(Const.CATEGORY_ID, tableOfContentsFileModel.getCategoryId());
        values.put(Const.CATEGORY, tableOfContentsFileModel.getCategory());
        values.put(Const.CATEGORIES, tableOfContentsFileModel.getCategories());
        values.put(Const.FILE, tableOfContentsFileModel.getFile());
        long a = db.insert(Const.TABLE_FILE, null, values);

        db.close();
        return a;
    }

    public long addTableOfContentsChildren(TableOfContentsChildrenModel tableOfContentsChildrenModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.TOPIC_ID, tableOfContentsChildrenModel.getTopicId());
        values.put(Const.NAME, tableOfContentsChildrenModel.getName());
        values.put(Const.PAGE_NO, tableOfContentsChildrenModel.getPageNo());
        values.put(Const.PARENT_ID, tableOfContentsChildrenModel.getParentId());
        values.put(Const.FILE_ID, tableOfContentsChildrenModel.getFileId());
        values.put(Const.FILE, tableOfContentsChildrenModel.getFile());
        long a = db.insert(Const.TABLE_CHILDREN, null, values);

        db.close();
        return a;
    }

    public long addTableOfContentsSubChildren(TableOfContentsSubChildrenModel tableOfContentsSubChildrenModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.TOPIC_ID, tableOfContentsSubChildrenModel.getTopicId());
        values.put(Const.NAME, tableOfContentsSubChildrenModel.getName());
        values.put(Const.PAGE_NO, tableOfContentsSubChildrenModel.getPageNo());
        values.put(Const.PARENT_ID, tableOfContentsSubChildrenModel.getParentId());
        values.put(Const.FILE_ID, tableOfContentsSubChildrenModel.getFileId());
        values.put(Const.FILE, tableOfContentsSubChildrenModel.getFile());
        long a = db.insert(Const.TABLE_SUB_CHILDREN, null, values);
        db.close();
        return a;
    }

    public void addGlossary(GlossaryModel glossary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.GLOSSARY_ID, glossary.getGlossaryId());
        values.put(Const.ALPHABET, glossary.getAlphabet());
        values.put(Const.WORD, glossary.getWord());
        values.put(Const.DEFINITION, glossary.getDefinition());
        values.put(Const.FILE, glossary.getFile());
        long a = db.insert(Const.TABLE_GLOSSARY, null, values);
        db.close();
    }

    public List<GlossaryModel> getGlossary() {
        List<GlossaryModel> glossaryList = new ArrayList<GlossaryModel>();
        String selectQuery = "SELECT * FROM " + Const.TABLE_GLOSSARY;
        SQLiteDatabase db = this.getReadableDatabase();
        GlossaryModel glossaryModel;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                glossaryModel = new GlossaryModel();
                glossaryModel.setGlossaryId(Integer.parseInt(cursor.getString(0)));
                glossaryModel.setAlphabet(cursor.getString(1));
                glossaryModel.setWord(cursor.getString(2));
                glossaryModel.setDefinition(cursor.getString(3));
                glossaryModel.setFile(cursor.getString(4));

                glossaryList.add(glossaryModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return glossaryList;
    }

    public int getGlossary(int glossaryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        String selectQuery = "SELECT * FROM " + Const.TABLE_GLOSSARY + " WHERE " + Const.GLOSSARY_ID + "=" + glossaryId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
            db.close();
            return id;
        }
        cursor.close();
        db.close();
        return id;
    }

    public int getMaxId() {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        String selectQuery = "SELECT * FROM " + Const.TABLE_ALL_FILES + " ORDER BY " + Const.FILE_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
            db.close();
            return id;
        }
        cursor.close();
        db.close();
        return id;
    }

    public AddFilesModel getAllFileModel(int fileId) {
        List<AddFilesModel> filesList = new ArrayList<AddFilesModel>();
        String selectQuery = "SELECT * FROM " + Const.TABLE_ALL_FILES + " WHERE " + Const.FILE_ID + "=" + fileId;
        SQLiteDatabase db = this.getReadableDatabase();
        AddFilesModel filesModel = new AddFilesModel();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            filesModel = new AddFilesModel();
            filesModel.setId(Integer.parseInt(cursor.getString(0)));
            filesModel.setfId(Integer.parseInt(cursor.getString(1)));
            filesModel.setfTitle(cursor.getString(2));
            filesModel.setfName(cursor.getString(3));
            filesModel.setfPath(cursor.getString(4));
            filesModel.setfVersion(Integer.parseInt(cursor.getString(5)));
            filesModel.setfCategoryId(Integer.parseInt(cursor.getString(6)));

        }
        cursor.close();
        db.close();
        return filesModel;
    }

    public List<AddFilesModel> getAllFiles() {
        List<AddFilesModel> filesList = new ArrayList<AddFilesModel>();
        String selectQuery = "SELECT * FROM " + Const.TABLE_ALL_FILES;
        SQLiteDatabase db = this.getReadableDatabase();
        AddFilesModel filesModel;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                filesModel = new AddFilesModel();
                filesModel.setId(Integer.parseInt(cursor.getString(0)));
                filesModel.setfId(Integer.parseInt(cursor.getString(1)));
                filesModel.setfTitle(cursor.getString(2));
                filesModel.setfName(cursor.getString(3));
                filesModel.setfPath(cursor.getString(4));
                filesModel.setfVersion(Integer.parseInt(cursor.getString(5)));
                filesModel.setfCategoryId(Integer.parseInt(cursor.getString(6)));

                filesList.add(filesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return filesList;
    }

    public List<AddFilesModel> getParticularCategory(int category) {
        List<AddFilesModel> filesList = new ArrayList<AddFilesModel>();
        String selectQuery = "SELECT * FROM " + Const.TABLE_ALL_FILES + " WHERE " + Const.CATEGORY_ID + "=" + category;
        SQLiteDatabase db = this.getReadableDatabase();
        AddFilesModel filesModel;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                filesModel = new AddFilesModel();
                filesModel.setId(Integer.parseInt(cursor.getString(0)));
                filesModel.setfId(Integer.parseInt(cursor.getString(1)));
                filesModel.setfTitle(cursor.getString(2));
                filesModel.setfName(cursor.getString(3));
                filesModel.setfPath(cursor.getString(4));
                filesModel.setfVersion(Integer.parseInt(cursor.getString(5)));
                filesModel.setfCategoryId(Integer.parseInt(cursor.getString(6)));

                filesList.add(filesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return filesList;
    }

    public int getFile(int fileId, int version, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        String selectQuery = "SELECT * FROM " + Const.TABLE_ALL_FILES + " WHERE " + Const.FILE_ID + "=" + fileId + " AND " +
                Const.VERSION + "=" + version + " AND " + Const.CATEGORY_ID + "=" + categoryId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
            db.close();
            return id;
        }
        cursor.close();
        db.close();
        return id;
    }

    public int getFileById(int fileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        String selectQuery = "SELECT * FROM " + Const.TABLE_ALL_FILES + " WHERE " + Const.FILE_ID + "=" + fileId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
            db.close();
            return id;
        }
        cursor.close();
        db.close();
        return id;
    }

    public int deletFile(int fileId,int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table_name = Const.TABLE_ALL_FILES;
        String where = Const.FILE_ID + "=" + fileId ;

        deletCategory(categoryId);
        int res= db.delete(table_name, where, null);
        db.close();
        return res;
    }
    public void deletCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table_name = Const.TABLE_CATEGORY;
        String where = Const.CATEGORY_ID + "=" + categoryId ;
        db.delete(table_name, where, null);
        db.close();
    }
    public void deletTableContentsFile(int fileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table_name = Const.CREATE_TABLE_FILE;
        String where = Const.FILE_ID + "=" + fileId ;
        deletTableContentsChildren(fileId);
        db.delete(table_name, where, null);
        db.close();
    }
    public void deletTableContentsChildren(int fileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table_name = Const.CREATE_TABLE_CHILDREN;
        String where = Const.FILE_ID + "=" + fileId ;
        db.delete(table_name, where, null);
        db.close();
    }




//    public int getFile(int fileId, int version, int categoryId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int id = 0;
//        String selectQuery = "SELECT * FROM " + Const.TABLE_ALL_FILES + " WHERE " + Const.FILE_ID + "=" + fileId + " AND "+
//                Const.VERSION + "=" + version + " AND " +Const.CATEGORY_ID + "="+categoryId;
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        if (cursor != null && cursor.getCount() != 0) {
//            id = Integer.parseInt(cursor.getString(0));
//            return id;
//        }
//        return id;
//    }
}

