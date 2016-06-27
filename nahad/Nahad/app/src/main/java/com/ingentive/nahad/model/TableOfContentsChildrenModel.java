package com.ingentive.nahad.model;

import com.ingentive.nahad.adapter.ContentsAdapter;

import java.util.List;

/**
 * Created by PC on 09-05-2016.
 */
public class TableOfContentsChildrenModel {

    public int getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(int childrenId) {
        this.childrenId = childrenId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    boolean checked=false;
    int childrenId;
    int topicId;
    String name;
    int pageNo;
    int parentId;
    int fileId;
    String file;
    List<TableOfContentsSubChildrenModel> subChildrendArray;
    ContentsAdapter.ViewHolderParent view;

    public ContentsAdapter.ViewHolderParent getView() {
        return view;
    }

    public void setView(ContentsAdapter.ViewHolderParent view) {
        this.view = view;
    }

    public TableOfContentsChildrenModel(){

    }

    public List<TableOfContentsSubChildrenModel> getSubChildrendArray() {
        return subChildrendArray;
    }

    public void setSubChildrendArray(List<TableOfContentsSubChildrenModel> subChildrendArray) {
        this.subChildrendArray = subChildrendArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
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
