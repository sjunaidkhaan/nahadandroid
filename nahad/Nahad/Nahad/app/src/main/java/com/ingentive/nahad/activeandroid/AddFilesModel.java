package com.ingentive.nahad.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by PC on 16-06-2016.
 */
@Table(name = "AddFilesModel")
public class AddFilesModel extends Model {

    @Column(name = "file_name")
    public String fileName;

    @Column(name = "category_id")
    public int categoryId;


    @Column(name = "pages_limit")
    public int pagesLimit;


    @Column(name = "file_version")
    public int fileVersion;

    @Column(name = "toc_version")
    public int tocVersion;

    @Column(name = "file_title")
    public String fileTitle;

    @Column(name = "file_id")
    public int fileId;
    ;

    @Column(name = "file_path")
    public String filePath;

    @Column(name = "category_identifier")
    public int categoryIdentifier;

    @Column(name = "category_name")
    public String categoryName;

    public String getFileName() {
        return fileName;
    }

    @Column(name = "update_date")
    public String updateDate;

    public int getCategoryIdentifier() {
        return categoryIdentifier;
    }

    public void setCategoryIdentifier(int categoryIdentifier) {
        this.categoryIdentifier = categoryIdentifier;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPagesLimit() {
        return pagesLimit;
    }

    public void setPagesLimit(int pagesLimit) {
        this.pagesLimit = pagesLimit;
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

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public AddFilesModel() {
        super();
        this.fileName = "";
        this.categoryId = 0;
        this.pagesLimit = 0;
        this.fileVersion = 0;
        this.tocVersion = 0;
        this.fileTitle = "";
        this.fileId = 0;
        this.filePath = "";
        this.updateDate = "";
        this.categoryIdentifier = 0;
        this.categoryName = "";
    }
}
