package com.xywy.askforexpert.model.answer.show;

/**
 * 已答试题列表条目
 * Created by bailiangjin on 16/4/20.
 */
public class AnsweredListItem {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_WRONG = 2;

    private String id;
    private String name;
    private String update_time;
    private boolean isChecked;
    /**
     * 类型 1:试题 2:错题
     */
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getMapKey() {
        return id + type;
    }

    public String getTypeStr() {
        if (1 == type) {
            return "record";
        } else {
            return "wrong";
        }

    }


    public PaperItem toPaperItem() {
        PaperItem paperItem = new PaperItem(name);
        paperItem.setPaper_id(id);
        paperItem.setVersion(update_time);
        return paperItem;
    }
}
