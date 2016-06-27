package com.ingentive.nahad.common;

/**
 * Created by PC on 06-05-2016.
 */
public class Const {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "nahad_db";
    // Tables List
    public static final String TABLE_ALL_FILES = "all_files";
    public static final String TABLE_CATEGORY = "category_tbl";
    public static final String TABLE_GLOSSARY = "glossary_tbl";
    public static final String TABLE_TABLE_OF_CONTENTS = "table_contents_tbl";
    public static final String TABLE_FILE = "file_tbl";
    public static final String TABLE_CHILDREN = "children_tbl";
    public static final String TABLE_SUB_CHILDREN = "sub_children_tbl";
    public static final String TABLE_BOOK_MARK= "book_mark_tbl";
    public static final String TABLE_SEND_EMAIL= "send_email_tbl";


    //ID Lis
    public static final String ID_PRIMARY_KEY = "id";
    public static final String FILE_ID = "file_id";
    public static final String CATEGORY_ID = "category_id";
    public static final String GLOSSARY_ID = "glossary_id";
    public static final String TOPIC_ID = "topic_id";
    public static final String PARENT_ID = "parent_id";
    public static final String CHILDREN_ID = "child_id";
    public static final String SUB_CHILDREN_ID = "sub_child_id";

    // Name LIST

    public static final String TITLE = "title";
    public static final String NAME = "name";
    public static final String PATH = "path";
    public static final String VERSION = "version";
    public static final String IDENTIFIER = "identifier";
    public static final String ALPHABET = "alphabet";
    public static final String WORD = "word";
    public static final String DEFINITION = "definition";
    public static final String FILE = "file";
    public static final String PAGE_NO = "page_no";

    public static final String CATEGORY = "category";
    public static final String CATEGORIES = "categories";



    public static String CREATE_TABLE_BOOK_MARK= "CREATE TABLE " + TABLE_BOOK_MARK + "("
            + FILE_ID + " INTEGER,"
            + PAGE_NO + " INTEGER" + ")";
    public static String CREATE_TABLE_SEND_EMAIL= "CREATE TABLE " + TABLE_SEND_EMAIL + "("
            + FILE_ID + " INTEGER,"
            + PAGE_NO + " INTEGER" + ")";
    public static String CREATE_TABLE_ALL_FILES = "CREATE TABLE " + TABLE_ALL_FILES + "("
            + ID_PRIMARY_KEY + " INTEGER PRIMARY KEY,"
            + FILE_ID + " INTEGER NOT NULL UNIQUE,"
            + TITLE + " TEXT,"
            + NAME + " TEXT,"
            + PATH + " TEXT,"
            + VERSION + " INTEGER,"
            + CATEGORY_ID + " INTEGER" + ")";

    public static String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "("
            + CATEGORY_ID + " INTEGER,"
            + NAME + " TEXT,"
            + IDENTIFIER + " INTEGER" + ")";

    public static String CREATE_TABLE_GLOSSARY = "CREATE TABLE " + TABLE_GLOSSARY + "("
            + GLOSSARY_ID + " INTEGER PRIMARY KEY,"
            + ALPHABET + " TEXT,"
            + WORD + " TEXT,"
            + DEFINITION + " TEXT,"
            + FILE + " TEXT" + ")";

    public static String CREATE_TABLE_TABLE_OF_CONTENTS = "CREATE TABLE " + TABLE_TABLE_OF_CONTENTS + "("
            + TOPIC_ID + " INTEGER PRIMARY KEY,"
            + NAME + " TEXT,"
            + PAGE_NO + " INTEGER,"
            + PARENT_ID + " INTEGER,"
            + FILE_ID + " INTEGER" + ")";

    public static String CREATE_TABLE_FILE = "CREATE TABLE " + TABLE_FILE + "("
            + FILE_ID + " INTEGER PRIMARY KEY,"
            + TITLE + " TEXT,"
            + NAME + " TEXT,"
            + PATH + " TEXT,"
            + VERSION + " INTEGER,"
            + CATEGORY_ID + " INTEGER,"
            + CATEGORY + " TEXT,"
            + CATEGORIES + " TEXT,"
            + FILE + " TEXT" + ")";


    public static String CREATE_TABLE_CHILDREN = "CREATE TABLE " + TABLE_CHILDREN + "("
            + CHILDREN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TOPIC_ID + " INTEGER,"
            + NAME + " TEXT,"
            + PAGE_NO + " INTEGER,"
            + PARENT_ID + " INTEGER,"
            + FILE_ID + " INTEGER,"
            + FILE + " TEXT" + ")";
    public static String CREATE_TABLE_SUB_CHILDREN = "CREATE TABLE " + TABLE_SUB_CHILDREN + "("
            + SUB_CHILDREN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TOPIC_ID + " INTEGER,"
            + NAME + " TEXT,"
            + PAGE_NO + " INTEGER,"
            + PARENT_ID + " INTEGER,"
            + FILE_ID + " INTEGER,"
            + FILE + " TEXT" + ")";
}
