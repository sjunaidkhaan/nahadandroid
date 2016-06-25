package com.ingentive.presidentsinfo.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by PC on 07-06-2016.
 */
@Table(name = "StoryInfo")
public class StoryInfo extends Model {

    @Column(name = "story_id")
    public int storyId;

    @Column(name = "president_id")
    public int presidentId;

    @Column(name = "story_title")
    public String storyTitle;

    @Column(name = "description_heading")
    public String descriptionHeading;

    @Column(name = "short_description")
    public String shortDescription;

    @Column(name = "long_description")
    public String longDescription;

    @Column(name = "background_image")
    public String backgroundImage;

    @Column(name = "story_audio_name")
    public String storyAudioName;

    @Column(name = "story_audio_url")
    public String storyAudioUrl;
    @Column(name = "moral")
    public String storyMoral;

    @Column(name = "timestamp")
    public int timeStamp;

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public int getPresidentId() {
        return presidentId;
    }

    public void setPresidentId(int presidentId) {
        this.presidentId = presidentId;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getDescriptionHeading() {
        return descriptionHeading;
    }

    public void setDescriptionHeading(String descriptionHeading) {
        this.descriptionHeading = descriptionHeading;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getStoryAudioName() {
        return storyAudioName;
    }

    public void setStoryAudioName(String storyAudioName) {
        this.storyAudioName = storyAudioName;
    }

    public String getStoryAudioUrl() {
        return storyAudioUrl;
    }

    public void setStoryAudioUrl(String storyAudioUrl) {
        this.storyAudioUrl = storyAudioUrl;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStoryMoral() {
        return storyMoral;
    }

    public void setStoryMoral(String storyMoral) {
        this.storyMoral = storyMoral;
    }

    public StoryInfo(){
        super();
        this.storyId = 0;
        this.presidentId = 0;
        this.storyTitle = "";
        this.descriptionHeading = "";
        this.shortDescription = "";
        this.longDescription = "";
        this.backgroundImage = "";
        this.storyAudioName = "";
        this.storyAudioUrl = "";
        this.timeStamp = 0;
        this.storyMoral="";
    }
    public StoryInfo(int storyId, int presidentId, String storyTitle, String descriptionHeading,
                          String shortDescription, String longDescription, String backgroundImage,
                          String storyAudioName, String storyAudioUrl, int modifiedDate) {
        super();
        this.storyId = storyId;
        this.presidentId = presidentId;
        this.storyTitle = storyTitle;
        this.descriptionHeading = descriptionHeading;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.backgroundImage = backgroundImage;
        this.storyAudioName = storyAudioName;
        this.storyAudioUrl = storyAudioUrl;
        this.timeStamp = modifiedDate;
    }
}
