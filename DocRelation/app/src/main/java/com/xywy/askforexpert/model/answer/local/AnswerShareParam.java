package com.xywy.askforexpert.model.answer.local;

import com.xywy.askforexpert.model.answer.show.PaperItem;
import com.xywy.askforexpert.model.answer.show.TestSet;

/**
 * Created by bailiangjin on 16/7/19.
 */
public class AnswerShareParam {
    /**
     * 类型 1：主页 2：试题列表页 3：试题详情页
     */
    private int type;

    /**
     * id 试卷分类id or 试卷id
     */
    private String id;

    /**
     * 试卷名 or 试卷名
     */
    private String name;

    /**
     * 试卷版本
     */
    private String version;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public PaperItem toPaperItem() {
        PaperItem paperItem = new PaperItem(name);
        paperItem.setPaper_id(id);
        paperItem.setVersion(version);
        return paperItem;
    }

    public TestSet toTestSet() {
        TestSet testSet = new TestSet();
        testSet.setId(id);
        testSet.setName(name);
        return testSet;
    }
}
