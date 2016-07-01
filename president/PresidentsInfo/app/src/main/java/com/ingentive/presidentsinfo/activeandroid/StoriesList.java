package com.ingentive.presidentsinfo.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by PC on 06-06-2016.
 */
@Table(name = "StoriesList")
public class StoriesList extends Model {

    @Column(name = "story_id")
    public int sId;

    @Column(name = "story_title")
    public String sTitle;

    public String getsTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public int getPresId() {
        return presId;
    }

    public void setPresId(int presId) {
        this.presId = presId;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Column(name = "president_id")

    public int presId;

    @Column(name = "time_stamp")
    public int timeStamp;
    public int getPresidentTimeStamp() {
        return presidentTimeStamp;
    }

    public void setPresidentTimeStamp(int presidentTimeStamp) {
        this.presidentTimeStamp = presidentTimeStamp;
    }
    @Column(name = "presidentTimestamp")
    public int presidentTimeStamp;

    public StoriesList() {
        super();
        this.sId = 0;
        this.sTitle = "";
        this.presId=0;
        this.timeStamp = 0;
        this.presidentTimeStamp=0;
    }
    public StoriesList(int sId, String sTitle,int presId, int time_stamp) {
        super();
        this.sId = sId;
        this.sTitle = sTitle;
        this.presId=presId;
        this.timeStamp = time_stamp;
    }
}
