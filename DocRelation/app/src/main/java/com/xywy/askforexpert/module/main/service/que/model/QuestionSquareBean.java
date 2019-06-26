package com.xywy.askforexpert.module.main.service.que.model;

import java.util.List;

/**
 * Created by xugan on 2017/12/25.
 * 问题广场实例
 */

public class QuestionSquareBean{
//    {
//        "total": 9,
//            "list": [
//        {
//            "qid": "133394129",
//                "title": "1.精神因素导致的月经推迟。如果突然出现",
//                "nickname": "xywy",
//                "age": "34岁",
//                "sex": "女",
//                "time": "10:05",
//                "subject": "月经不调",
//                "q_type": "1",
//                "money": "7",
//                "type": "ques",
//                "con": "肚纸痛，怎么办啊  啊啊啊     啊啊啊"
//        },
//        {
//            "qid": "133392870",
//                "title": "脚这样了，鞋子不透气，去看",
//                "nickname": "xywy",
//                "age": "",
//                "sex": "男",
//                "time": "21:56",
//                "subject": "皮肤综合",
//                "q_type": "3",
//                "money": "0",
//                "type": "zhuiwen",
//                "con": "肚纸痛，怎么办啊  啊啊啊     啊啊啊"
//        }
//        ]
//    }
    /**
     * 处理中的帖子,还未读的帖子总条数
     */
    int total;
    public List<QuestionNote> list;
}

