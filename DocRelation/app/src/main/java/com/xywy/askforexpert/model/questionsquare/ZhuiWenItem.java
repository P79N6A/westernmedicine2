package com.xywy.askforexpert.model.questionsquare;

import java.util.List;

/**
 * Created by bailiangjin on 2017/4/28.
 */

public class ZhuiWenItem {
    private int reply_uid;
    private String content;
    private String addtime;
    private List<String> picutres;

    public int getReply_uid() {
        return reply_uid;
    }

    public void setReply_uid(int reply_uid) {
        this.reply_uid = reply_uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public List<String> getPicutres() {
        return picutres;
    }

    public void setPicutres(List<String> picutres) {
        this.picutres = picutres;
    }

    //    if (arrayzhui != null && arrayzhui.length() != 0) {
//        for (int i = 0; i < arrayzhui.length(); i++) {
//            JSONObject jsonChild = arrayzhui.getJSONObject(i);
//            int uid = jsonChild.getInt("reply_uid");
//            if (uid == doctor.getuId()) {
//                String tempContent = jsonChild
//                        .getString("content");
//                String tempTime = jsonChild
//                        .getString("addtime");
//                JSONArray picArray1 = jsonChild
//                        .getJSONArray("picutres");
//                if ("".equals(tempContent)) {
//                    if (picArray1 != null && picArray1.length() != 0) {
//                        for (int j = 0; j < picArray1.length(); j++) {
//                            ListData dataPic = new ListData();
//                            dataPic.setType(2);
//                            dataPic.setDetail("");
//                            dataPic.setuId(uid);
//                            dataPic.setCreateTime(tempTime);
//                            dataPic.setPicture(picArray1
//                                    .getString(i));
//                            queDetails.add(dataPic);
//                        }
//                    }
//                } else {
//                    ListData dataZhui = new ListData();
//                    dataZhui.setType(2);
//                    dataZhui.setuId(uid);
//                    dataZhui.setDetail(tempContent);
//                    dataZhui.setCreateTime(tempTime);
//                    queDetails.add(dataZhui);
//                }
//
//            } else {
//                String tempUserContent = jsonChild
//                        .getString("content");
//                String tempCreateTime = jsonChild
//                        .getString("addtime");
//                JSONArray picArray2 = jsonChild
//                        .getJSONArray("picutres");
//                if (!"".equals(tempUserContent)) {
//
//
//                    ListData userZhui = new ListData();
//                    userZhui.setType(3);
//                    userZhui.setuId(uid);
//                    userZhui.setDetail(jsonChild
//                            .getString("content"));
//                    userZhui.setCreateTime(jsonChild
//                            .getString("addtime"));
//                    queDetails.add(userZhui);
//                }
//
//                if (picArray2 != null && picArray2.length() != 0) {
//                    for (int k = 0; k < picArray2.length(); k++) {
//                        ListData dataPic = new ListData();
//                        dataPic.setType(3);
//                        dataPic.setuId(uid);
//                        dataPic.setDetail("");
//                        dataPic.setCreateTime(tempCreateTime);
//                        dataPic.setPicture(picArray2
//                                .getString(k));
//                        queDetails.add(dataPic);
//                    }
//                }
//            }
//
//        }
//    }
}
