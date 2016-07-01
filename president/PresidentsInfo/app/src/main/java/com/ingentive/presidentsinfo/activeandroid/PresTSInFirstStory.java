package com.ingentive.presidentsinfo.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by PC on 01-07-2016.
 */
@Table(name = "PresTSInFirstStory")
public class PresTSInFirstStory extends Model{

    @Column(name = "president_id")
    public int presId;

    @Column(name = "story_id")
    public int sId;

    @Column(name = "presidentTimeStamp")
    public int presidentTimeStamp;

    public int getPresId() {
        return presId;
    }

    public void setPresId(int presId) {
        this.presId = presId;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public int getPresidentTimeStamp() {
        return presidentTimeStamp;
    }

    public void setPresidentTimeStamp(int presidentTimeStamp) {
        this.presidentTimeStamp = presidentTimeStamp;
    }

    public PresTSInFirstStory(){
        super();
        this.presidentTimeStamp=0;
        this.presId=0;
        this.sId=0;
    }
}
