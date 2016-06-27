package com.ingentive.nahad.model;

import com.ingentive.nahad.adapter.ContentsAdapter;
import com.ingentive.nahad.adapter.TableOfContentsAdapter;

import java.util.List;

/**
 * Created by PC on 09-05-2016.
 */
public class TableOfContentsModel {

    int topicId;
    String name;
    int pageNo;
    int parentId;
    int fileId;


    public TableOfContentsFileModel getTableOfContentsFileModel() {
        return tableOfContentsFileModel;
    }

    public void setTableOfContentsFileModel(TableOfContentsFileModel tableOfContentsFileModel) {
        this.tableOfContentsFileModel = tableOfContentsFileModel;
    }

    TableOfContentsFileModel tableOfContentsFileModel;
    List<TableOfContentsChildrenModel> childrenArray;

    public List<TableOfContentsChildrenModel> getChildrenArray() {
        return childrenArray;
    }

    public void setChildrenArray(List<TableOfContentsChildrenModel> childrenArray) {
        this.childrenArray = childrenArray;
    }
//    public void setView(TableOfContentsAdapter.ViewHolderParent view) {
//        this.view = view;
//    }
//
//    TableOfContentsAdapter.ViewHolderParent view;
//    public TableOfContentsModel(){
//
//    }
//    public TableOfContentsAdapter.ViewHolderParent getView() {
//        return view;
//    }


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
}
