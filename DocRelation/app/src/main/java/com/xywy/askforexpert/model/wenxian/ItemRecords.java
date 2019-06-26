package com.xywy.askforexpert.model.wenxian;

import java.io.Serializable;

/**
 * 文献搜索item
 *
 * @author apple
 */
public class ItemRecords implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String ArticleID;
    private String Title;
    private String Creator;
    private String Source;
    private String KeyWords;
    private String Year;
    private String DBID;

    public String getArticleID() {
        return ArticleID;
    }

    public void setArticleID(String articleID) {
        ArticleID = articleID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getKeyWords() {
        return KeyWords;
    }

    public void setKeyWords(String keyWords) {
        KeyWords = keyWords;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getDBID() {
        return DBID;
    }

    public void setDBID(String dBID) {
        DBID = dBID;
    }

    @Override
    public String toString() {
        return "ItemRecords [ArticleID=" + ArticleID + ", Title=" + Title
                + ", Creator=" + Creator + ", Source=" + Source + ", KeyWords="
                + KeyWords + ", Year=" + Year + ", DBID=" + DBID + "]";
    }

}
