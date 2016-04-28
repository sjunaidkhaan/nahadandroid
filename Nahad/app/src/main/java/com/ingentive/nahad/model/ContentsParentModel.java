package com.ingentive.nahad.model;

import com.ingentive.nahad.adapter.ContentsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 27-04-2016.
 */
public class ContentsParentModel {

    String parentItemText;
    //List<ContentsChildModel> productList = new ArrayList<ContentsChildModel>();
    private List<ContentsChildModel> mShopChildrenList;

    ContentsAdapter.ViewHolderParent view;
    public ContentsParentModel(){

    }
    public ContentsAdapter.ViewHolderParent getView() {
        return view;
    }

    public void setView(ContentsAdapter.ViewHolderParent view) {
        this.view = view;
    }
    public String getParentItemText() {
        return parentItemText;
    }

    public void setParentItemText(String parentItemText) {
        this.parentItemText = parentItemText;
    }
//    public List<ContentsChildModel> getChildsList() {
//        return mShopChildrenList;
//    }
//    public void setChildsList(List<ContentsChildModel> productList) {
//        this.mShopChildrenList = productList;
//    }

    public List<ContentsChildModel> getArrayChildren() {
        return mShopChildrenList;
    }

    public void setArrayChildren(List<ContentsChildModel> arrayChildren) {
        mShopChildrenList = arrayChildren;
    }
}
