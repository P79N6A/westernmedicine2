package com.xywy.askforexpert.model.main;

import com.xywy.askforexpert.module.main.service.que.model.QuestionNote;

import java.io.Serializable;
import java.util.List;


public class HistoryListPageDataBean implements Serializable {
    public int more;
    public String count;
    public List<QuestionNote> data;
}
