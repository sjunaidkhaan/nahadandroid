package com.ingentive.nahad.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.ingentive.nahad.adapter.TableOfContentsAdapter;
import com.ingentive.nahad.adapter.TocAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 17-06-2016.
 */
@Table(name = "TocParentModel")
public class TocParentModel extends Model {
    public TocAdapter.ViewHolderParent getView() {
        return view;
    }

    @Column(name = "topic_id")
    public int topicId;
    @Column(name = "name")
    public String name;
    @Column(name = "page_no")
    public int pageNo;
    @Column(name = "parent_id")
    public int parentId;
    @Column(name = "file_id")
    public int fileId;
    @Column(name = "file_title")
    public String fileTitle;
    @Column(name = "file_name")
    public String fileName;
    @Column(name = "file_path")
    public String filePath;
    @Column(name = "file_version")
    public int fileVersion;
    @Column(name = "toc_version")
    public int tocVersion;
    @Column(name = "category_id")
    public int categoryId;
    @Column(name = "category")
    public String category;
    @Column(name = "pages_limit")
    public int pagesLimit;
    @Column(name = "categories")
    public String categories;
    @Column(name = "file")
    public String file;

    public List<TocChildrenModel> tocChildrenAray;


    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(int fileVersion) {
        this.fileVersion = fileVersion;
    }

    public int getTocVersion() {
        return tocVersion;
    }

    public void setTocVersion(int tocVersion) {
        this.tocVersion = tocVersion;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPagesLimit() {
        return pagesLimit;
    }

    public void setPagesLimit(int pagesLimit) {
        this.pagesLimit = pagesLimit;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<TocChildrenModel> getTocChildrenAray() {
        return tocChildrenAray;
    }

    public void setTocChildrenAray(List<TocChildrenModel> tocChildrenAray) {
        this.tocChildrenAray = tocChildrenAray;
    }

    public TocParentModel() {
        super();
        this.name = "";
        this.pageNo = 0;
        this.parentId = 0;
        this.fileId = 0;
        this.fileTitle = "";
        this.fileName = "";
        this.filePath = "";
        this.fileVersion = 0;

        this.tocVersion = 0;
        this.categoryId = 0;

        this.category = "";
        this.pagesLimit = 0;
        this.categories = "";
        this.file = "";
        this.tocChildrenAray=new ArrayList<TocChildrenModel>();
    }
    TocAdapter.ViewHolderParent view;
    public void setView(TocAdapter.ViewHolderParent view) {
        this.view = view;
    }
}
