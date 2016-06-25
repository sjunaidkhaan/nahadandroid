package com.ingentive.presidentsinfo.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by PC on 06-06-2016.
 */
@Table(name = "PresidentsList")
public class PresidentsList extends Model{

    @Column(name = "president_id")
    public int presId;

    @Column(name = "president_name")
    public String presidentName;

    @Column(name = "time_stamp")
    public int timeStamp;

    public int getPresId() {
        return presId;
    }

    public void setPresId(int presId) {
        this.presId = presId;
    }

    public String getPresidentName() {
        return presidentName;
    }

    public void setPresidentName(String presidentName) {
        this.presidentName = presidentName;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public PresidentsList() {
        super();
        this.presId = 0;
        this.presidentName = "";
        this.timeStamp = 0;
    }

    public PresidentsList(int presId, String presidentName,int time_stamp) {
        super();
        this.presId = presId;
        this.presidentName = presidentName;
        this.timeStamp = time_stamp;
    }

}
