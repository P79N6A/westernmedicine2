package com.xywy.askforexpert.model.answer.show;

import java.io.Serializable;

/**
 * Created by bailiangjin on 16/4/18.
 */
public class AnswerItem implements Serializable {

    public static final int INDEX_TYPE_ABC = 1;
    public static final int INDEX_TYPE_NUMBER = 2;
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 选项index类型 1:字母 2:数字
     */
    private int indexType;
    /**
     * 选项内容
     */
    private String content;
    /**
     * 是否被勾选
     */
    private boolean checked;
    /**
     * 是否正确选项
     */
    private boolean right;

    /**
     * 是否为多选
     */
    private boolean isMulti;

    /**
     * 是否完成
     */
    private boolean isComplete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIndexType() {
        return indexType;
    }

    public void setIndexType(int indexType) {
        this.indexType = indexType;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public void setMulti(boolean multi) {
        isMulti = multi;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
