package com.ingentive.nahad.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 27-04-2016.
 */
public class ContentsParentModel {

    String parentItemText;
    List<ContentsChildModel> productList = new ArrayList<ContentsChildModel>();;
    public ContentsParentModel(){

    }
    public String getParentItemText() {
        return parentItemText;
    }

    public void setParentItemText(String parentItemText) {
        this.parentItemText = parentItemText;
    }
    public List<ContentsChildModel> getChildsList() {
        return productList;
    }
    public void setChildsList(List<ContentsChildModel> productList) {
        this.productList = productList;
    }
}
