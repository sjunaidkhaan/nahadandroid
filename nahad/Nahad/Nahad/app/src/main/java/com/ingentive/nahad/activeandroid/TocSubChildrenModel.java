package com.ingentive.nahad.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;

/**
 * Created by PC on 17-06-2016.
 */
@Table(name = "TocSubChildrenModel")
public class TocSubChildrenModel extends Model {
    @Column(name = "topic_id")
    public int topicId;
    @Column(name = "name")
    public String name;
    @Column(name = "page_no")
    public int pageNo;
    @Column(name = "parent_id")
    public int parentId;

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

    public String getFile() {
        return file;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Column(name = "file_id")
    public int fileId;
    @Column(name = "file")
    public String file;
    public boolean isCheck=false;
    public TocSubChildrenModel() {
        super();
        this.topicId=0;
        this.name = "";
        this.pageNo = 0;
        this.parentId = 0;
        this.fileId = 0;
        this.file="";
        this.isCheck=false;
    }
}
