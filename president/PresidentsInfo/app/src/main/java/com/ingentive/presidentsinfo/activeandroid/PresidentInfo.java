package com.ingentive.presidentsinfo.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by PC on 07-06-2016.
 */
@Table(name = "PresidentInfo")
public class PresidentInfo extends Model {

    @Column(name = "president_id")
    public int presId;

    @Column(name = "president_name")
    public String presName;;

    @Column(name = "president_image_name")
    public String presImageUrl;
    @Column(name = "president_image_url")
    public String presImageName;
    @Column(name = "signature_image_name")
    public String presSignatureImageName;
    @Column(name = "signature_image_url")
    public String presSignatureImageUrl;

    @Column(name = "quotation")
    public String presQuotation;

    @Column(name = "background_image_name")
    public String backgroundImageName;
    @Column(name = "background_image_url")
    public String backgroundImageUrl;
    @Column(name = "president_fact")
    public String presFact;;
    @Column(name = "timestamp")
    public int timeStamp;;

    public int getPresId() {
        return presId;
    }

    public void setPresId(int presId) {
        this.presId = presId;
    }

    public String getPresName() {
        return presName;
    }

    public void setPresName(String presName) {
        this.presName = presName;
    }


    public String getPresQuotation() {
        return presQuotation;
    }

    public void setPresQuotation(String presQuotation) {
        this.presQuotation = presQuotation;
    }


    public String getPresFact() {
        return presFact;
    }

    public void setPresFact(String presFact) {
        this.presFact = presFact;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPresImageUrl() {
        return presImageUrl;
    }

    public void setPresImageUrl(String presImageUrl) {
        this.presImageUrl = presImageUrl;
    }

    public String getPresImageName() {
        return presImageName;
    }

    public void setPresImageName(String presImageName) {
        this.presImageName = presImageName;
    }

    public String getPresSignatureImageName() {
        return presSignatureImageName;
    }

    public void setPresSignatureImageName(String presSignatureImageName) {
        this.presSignatureImageName = presSignatureImageName;
    }

    public String getPresSignatureImageUrl() {
        return presSignatureImageUrl;
    }

    public void setPresSignatureImageUrl(String presSignatureImageUrl) {
        this.presSignatureImageUrl = presSignatureImageUrl;
    }

    public String getBackgroundImageName() {
        return backgroundImageName;
    }

    public void setBackgroundImageName(String backgroundImageName) {
        this.backgroundImageName = backgroundImageName;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public PresidentInfo() {
        super();
        this.presId = 0;
        this.presName = "";
        this.presImageName="";

        this.presSignatureImageName="";
        this.presImageUrl="";
        this.presSignatureImageUrl="";

        this.presQuotation="";
        this.backgroundImageName="";
        this.backgroundImageUrl="";
        this.timeStamp = 0;
    }
}
