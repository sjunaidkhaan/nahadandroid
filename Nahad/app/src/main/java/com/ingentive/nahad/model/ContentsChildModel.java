package com.ingentive.nahad.model;

/**
 * Created by PC on 27-04-2016.
 */
public class ContentsChildModel {

    String childItemText ="";

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    String sequence = "";

    public ContentsChildModel() {

    }
    public String getChildItemText() {
        return childItemText;
    }

    public void setChildItemText(String childTtemText) {
        this.childItemText = childTtemText;
    }

}
