package com.ingentive.nahad.model;

/**
 * Created by PC on 09-05-2016.
 */
public class TableOfContentsSubChildrenModel {

    int subChildId;
    int topicId;
    String name;
    int pageNo;
    int parentId;
    int fileId;
    String file;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    boolean checked;

    public TableOfContentsSubChildrenModel(){
        checked=false;
    }

    public int getSubChildId() {
        return subChildId;
    }

    public void setSubChildId(int subChildId) {
        this.subChildId = subChildId;
    }

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

    public void setFile(String file) {
        this.file = file;
    }
}
