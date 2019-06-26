package com.xywy.askforexpert.model;

import android.graphics.Bitmap;

import com.xywy.askforexpert.module.main.service.downFile.DownloadFile;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * 文件下载模型
 *
 * @author 王鹏
 * @2015-6-5下午4:30:33
 */
@Table(name = "downloadtask")
public class DownFileItemInfo implements Serializable {

    private String id;// 文件的ID
    private Long progressCount = (long) 0; // 总大小
    private Long currentProgress = (long) 0;// 当前进度
    private Integer downloadState = 0; // 下载状态
    private boolean editState;// 是否是编辑状态
    private Bitmap fileHeadImage;
    private String fileHeadImagePath;// 文件图片的路径
    private String fileSize;// 文件大小
    private String movieName;// 文件名称
    private String downloadUrl; // 下载地址
    private DownloadFile downloadFile; // 下载控制器
    private String percentage = "0%"; // 下载百分比的字符串
    private Long uuid; // 下载任务的标识
    private String filePath; // 存储路径
    private boolean isSelected; // 选中状态
    private String movieId;
    private String userid;//userid
    private String commed = "0";//0指南1文献


    public String getCommed() {
        return commed;
    }

    public void setCommed(String commed) {
        this.commed = commed;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    // private boolean existDwonloadQueue;//是否身在下载队列中
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProgressCount() {
        return progressCount;
    }

    public void setProgressCount(Long progressCount) {
        this.progressCount = progressCount;
    }

    public Long getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Long currentProgress) {
        this.currentProgress = currentProgress;
    }

    public Integer getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(Integer downloadState) {
        this.downloadState = downloadState;
    }

    public boolean isEditState() {
        return editState;
    }

    public void setEditState(boolean editState) {
        this.editState = editState;
    }

    public Bitmap getFileHeadImage() {
        return fileHeadImage;
    }

    public void setFileHeadImage(Bitmap fileHeadImage) {
        this.fileHeadImage = fileHeadImage;
    }

    public String getFileHeadImagePath() {
        return fileHeadImagePath;
    }

    public void setFileHeadImagePath(String fileHeadImagePath) {
        this.fileHeadImagePath = fileHeadImagePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public DownloadFile getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(DownloadFile downloadFile) {
        this.downloadFile = downloadFile;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }


}
