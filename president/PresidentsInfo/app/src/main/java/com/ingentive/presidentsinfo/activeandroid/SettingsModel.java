package com.ingentive.presidentsinfo.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by PC on 28-06-2016.
 */
@Table(name = "SettingsModel")
public class SettingsModel extends Model {

    @Column(name = "font_size")
    public int fontSize;

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getRandomize() {
        return randomize;
    }

    public void setRandomize(String randomize) {
        this.randomize = randomize;
    }

    @Column(name = "randomize")
    public String randomize;

    public SettingsModel(){
        super();
        this.fontSize=0;
        this.randomize="";
    }
}
