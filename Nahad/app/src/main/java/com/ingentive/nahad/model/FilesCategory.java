package com.ingentive.nahad.model;

/**
 * Created by PC on 06-05-2016.
 */
public class FilesCategory {

    int categoryId;
    int identifier;
    String name;
   public FilesCategory(){

    }
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
