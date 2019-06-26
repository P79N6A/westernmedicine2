package com.xywy.askforexpert.model.wenxian;

import java.io.Serializable;

/**
 * 文献详情bean
 *
 * @author apple
 */
public class WXditaileBean implements Serializable {

    public String Conference;
    private int code;
    private String msg;
    private String Volum;
    private String Issue;
    private String Page;
    private String Abstract;
    private String HasOriginalDoc;
    private String Organization;
    private String ArticleID;
    private String Title;
    private String Creator;
    private String Source;
    private String KeyWords;
    private String Year;
    private String DBID;
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVolum() {
        return Volum;
    }

    public void setVolum(String volum) {
        Volum = volum;
    }

    public String getIssue() {
        return Issue;
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public String getPage() {
        return Page;
    }

    public void setPage(String page) {
        Page = page;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String abstract1) {
        Abstract = abstract1;
    }

    public String getHasOriginalDoc() {
        return HasOriginalDoc;
    }

    public void setHasOriginalDoc(String hasOriginalDoc) {
        HasOriginalDoc = hasOriginalDoc;
    }

    public String getOrganization() {
        return Organization;
    }

    public void setOrganization(String organization) {
        Organization = organization;
    }

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
        return "WXditaileBean [code=" + code + ", msg=" + msg + ", Volum="
                + Volum + ", Issue=" + Issue + ", Page=" + Page + ", Abstract="
                + Abstract + ", HasOriginalDoc=" + HasOriginalDoc
                + ", Organization=" + Organization + ", ArticleID=" + ArticleID
                + ", Title=" + Title + ", Creator=" + Creator + ", Source="
                + Source + ", KeyWords=" + KeyWords + ", Year=" + Year
                + ", DBID=" + DBID + "]";
    }


}
