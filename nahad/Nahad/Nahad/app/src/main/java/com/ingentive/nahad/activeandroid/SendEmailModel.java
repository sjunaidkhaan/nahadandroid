package com.ingentive.nahad.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by PC on 18-06-2016.
 */
@Table(name = "SendEmailModel")
public class SendEmailModel extends Model{
    @Column(name = "file_id")
    public int fileId;
    @Column(name = "page_no")
    public int pageNo;
    @Column(name = "topic_id")
    public int topicId;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public SendEmailModel(){
        super();
        this.fileId=0;

        this.pageNo=0;
        this.topicId=0;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
