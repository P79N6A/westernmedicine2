package com.xywy.askforexpert.model;

import java.io.Serializable;

/**
 * 频道实体类
 *
 * @author SHR
 * @2015-4-29上午10:40:07
 */
public class ChannelItem implements Serializable {
    /**
     * 序列号
     */
    private static final long serialVersionUID = -6465237897027410019L;
    /**
     * 栏目对应ID
     */
    private Integer id;
    /**
     * 栏目对应NAME
     */
    private String name;
    /**
     * 栏目在整体中的排序顺序 rank
     */
    private Integer orderId;
    /**
     * 图片指定ID
     */
    private int imgPath;
    /**
     * 更多指定图片ID
     */
    private int morePath;

    /**
     * 栏目是否选中
     */
    private Integer selected;

    /**
     * 描述
     */
    private String description;

    public ChannelItem() {
    }

    /**
     * @param id          唯一id
     * @param name        name
     * @param orderId     默认顺序
     * @param selected    选择顺序
     * @param imgPath     图片
     * @param morePath    更多图片
     * @param description 描述
     */
    public ChannelItem(Integer id, String name, Integer orderId,
                       Integer selected, int imgPath, int morePath, String description) {
        super();
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.imgPath = imgPath;
        this.selected = selected;
        this.morePath = morePath;
        this.description = description;
    }

    public ChannelItem(int id, String name, int orderId, int selected) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.selected = selected;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int paramInt) {
        this.id = Integer.valueOf(paramInt);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public int getOrderId() {
        return this.orderId.intValue();
    }

    public void setOrderId(int paramInt) {
        this.orderId = Integer.valueOf(paramInt);
    }

    public Integer getSelected() {
        return this.selected;
    }

    public void setSelected(Integer paramInteger) {
        this.selected = paramInteger;
    }

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public int getMorePath() {
        return morePath;
    }

    public void setMorePath(int morePath) {
        this.morePath = morePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ChannelItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orderId=" + orderId +
                ", imgPath=" + imgPath +
                ", morePath=" + morePath +
                ", selected=" + selected +
                ", description='" + description + '\'' +
                '}';
    }
}