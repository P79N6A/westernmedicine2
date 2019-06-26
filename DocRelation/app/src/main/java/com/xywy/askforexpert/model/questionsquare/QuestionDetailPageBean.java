package com.xywy.askforexpert.model.questionsquare;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bailiangjin on 2017/4/28.
 */

public class QuestionDetailPageBean {


    /**
     * code : 0
     * msg : 成功
     * data : {"picture":[],"id":128186467,"title":"吃馒头喝粥硬鄂软鄂地方刺痛疙瘩疱...","sex":"男","givepoint":"0","age":"25","createtime":"2017-04-28 15:36:09","intime":1493364969,"ques_from":"mobile","status":"0","photo":"","detail_1":"吃馒头喝粥硬鄂软鄂地方刺痛疙瘩疱疹，用刀割开里面有胶水一样的东西，影响进食，饮食清淡和吃药7个月没什么用 ，这个症状有很多年了，吃东西就起，过一会消下去","detail_2":"","detail_3":"","subject":"314","subject_pid":"306","subjectName":"皮肤综合","subject_pidName":"皮肤科","analyse":"","suggest":"","tag":"","allowSkip":1,"isEdit":1,"isQuick":0,"box_num":1,"isTou":1}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * picture : []
         * id : 128186467
         * title : 吃馒头喝粥硬鄂软鄂地方刺痛疙瘩疱...
         * sex : 男
         * givepoint : 0
         * age : 25
         * createtime : 2017-04-28 15:36:09
         * intime : 1493364969
         * ques_from : mobile
         * status : 0
         * photo :
         * detail_1 : 吃馒头喝粥硬鄂软鄂地方刺痛疙瘩疱疹，用刀割开里面有胶水一样的东西，影响进食，饮食清淡和吃药7个月没什么用 ，这个症状有很多年了，吃东西就起，过一会消下去
         * detail_2 :
         * detail_3 :
         * subject : 314
         * subject_pid : 306
         * subjectName : 皮肤综合
         * subject_pidName : 皮肤科
         * analyse :
         * suggest :
         * tag :
         * allowSkip : 1
         * isEdit : 1
         * isQuick : 0
         * box_num : 1
         * isTou : 1
         */

        private int id;
        private String title;
        private String sex;
        private String givepoint;
        private String age;
        private String createtime;
        private String rep_createtime;
        private String subjectName;
        private int intime;
        private String ques_from;
        private String status;
        private String photo;
        @SerializedName("detail_1")
        private String detail;
        @SerializedName("detail_2")
        private String state;
        @SerializedName("detail_3")
        private String help;
        private String subject;
        private String subject_pid;
        @SerializedName("subject_pidName")
        private String oneDpart;
        private String analyse;
        private String suggest;
        private String tag;
        private String rid;
        private int rep_uid;
        private String rep_content;
        private String doc_photo;
        private String blood_id;
        private int allowSkip;
        private int isEdit;
        private int isQuick;
        private int box_num;
        private int isTou;
        private int hasZw;
        private List<String> picture;
        private List<ZhuiWenItem> zhuiwen;

        public String getBlood_id() {
            return blood_id;
        }

        public void setBlood_id(String blood_id) {
            this.blood_id = blood_id;
        }

        public List<ZhuiWenItem> getZhuiwen() {
            return zhuiwen;
        }

        public void setZhuiwen(List<ZhuiWenItem> zhuiwen) {
            this.zhuiwen = zhuiwen;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getGivepoint() {
            return givepoint;
        }

        public void setGivepoint(String givepoint) {
            this.givepoint = givepoint;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getRep_createtime() {
            return rep_createtime;
        }

        public void setRep_createtime(String rep_createtime) {
            this.rep_createtime = rep_createtime;
        }

        public int getIntime() {
            return intime;
        }

        public void setIntime(int intime) {
            this.intime = intime;
        }

        public String getQues_from() {
            return ques_from;
        }

        public void setQues_from(String ques_from) {
            this.ques_from = ques_from;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getHelp() {
            return help;
        }

        public void setHelp(String help) {
            this.help = help;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getSubject_pid() {
            return subject_pid;
        }

        public void setSubject_pid(String subject_pid) {
            this.subject_pid = subject_pid;
        }


        public String getOneDpart() {
            return oneDpart;
        }

        public void setOneDpart(String oneDpart) {
            this.oneDpart = oneDpart;
        }

        public String getAnalyse() {
            return analyse;
        }

        public void setAnalyse(String analyse) {
            this.analyse = analyse;
        }

        public String getSuggest() {
            return suggest;
        }

        public void setSuggest(String suggest) {
            this.suggest = suggest;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getAllowSkip() {
            return allowSkip;
        }

        public void setAllowSkip(int allowSkip) {
            this.allowSkip = allowSkip;
        }

        public int getIsEdit() {
            return isEdit;
        }

        public void setIsEdit(int isEdit) {
            this.isEdit = isEdit;
        }

        public int getIsQuick() {
            return isQuick;
        }

        public void setIsQuick(int isQuick) {
            this.isQuick = isQuick;
        }

        public int getBox_num() {
            return box_num;
        }

        public void setBox_num(int box_num) {
            this.box_num = box_num;
        }

        public int getIsTou() {
            return isTou;
        }

        public void setIsTou(int isTou) {
            this.isTou = isTou;
        }

        public List<String> getPicture() {
            return picture;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public int getRep_uid() {
            return rep_uid;
        }

        public void setRep_uid(int rep_uid) {
            this.rep_uid = rep_uid;
        }

        public String getRep_content() {
            return rep_content;
        }

        public void setRep_content(String rep_content) {
            this.rep_content = rep_content;
        }

        public String getDoc_photo() {
            return doc_photo;
        }

        public void setDoc_photo(String doc_photo) {
            this.doc_photo = doc_photo;
        }
        public int getHasZw() {
            return hasZw;
        }

        public void setHasZw(int hasZw) {
            this.hasZw = hasZw;
        }
    }

//    try {
//        JSONObject jsonObject = new JSONObject(json);
//        int code = jsonObject.getInt("code");
//        String msg = jsonObject.getString("msg");
//        if (code == 0) {
//            ListData data = new ListData();
//            JSONObject jsonElement = jsonObject.optJSONObject("data");
//            JSONArray array = jsonElement.optJSONArray("picture");
//            String dataTime = jsonElement.optString("createtime");
//            data.setCreateTime(dataTime);
//            data.setType(0);
//            data.setSubject_pidName(jsonElement
//                    .getString("subject_pidName"));
//            oneDpart = jsonElement
//                    .getString("subject_pidName");
//            twoDpart = jsonElement.getString("subjectName");
//            data.setSubjectName(jsonElement.getString("subjectName"));
//            data.setTitle(jsonElement.optString("title"));
//            data.setSex(jsonElement.getString("sex"));
//            data.setAge(jsonElement.getString("age"));
//            data.setDetail(jsonElement.getString("detail_1"));
//            data.setState(jsonElement.getString("detail_2"));
//            data.setHelp(jsonElement.getString("detail_3"));
//            data.setPhoto(jsonElement.getString("photo"));
//            data.setAwardMoney(jsonElement.optString("givepoint"));
//            data.setQues_from(jsonElement.optString("ques_from"));
//            queDetails.add(data);
//            if (array != null && array.length() != 0) {
//                for (int i = 0; i < array.length(); i++) {
//                    ListData dataPic = new ListData();
//                    dataPic.setType(3);
//                    dataPic.setDetail("");
//                    dataPic.setCreateTime(dataTime);
//                    dataPic.setPicture(array.getString(i));
//                    queDetails.add(dataPic);
//                }
//            }
//
//            isEdit = jsonElement.optInt("isEdit");
//            isQuick = jsonElement.getInt("isQuick");
//            box_num = jsonElement.getInt("box_num");
//            isTou = jsonElement.getInt("isTou");
//            allowSkip = jsonElement.getInt("allowSkip");
//            if (tag.equals("myreply") || tag.equals("zhuiwen") || tag.equals("expert_zw")) {
//                ListData doctor = new ListData();
//                doctor.setType(1);
//                doctor.setCreateTime(jsonElement
//                        .getString("rep_createtime"));
//                doctor.setuId(jsonElement.getInt("rep_uid"));
//                rid = jsonElement.getString("rid");
//                doctor.setrId(jsonElement.getString("rid"));
//                doctor.setDetail(Html.fromHtml(
//                        jsonElement.getString("rep_content")).toString());
//                doctor.setPhoto(jsonElement.getString("doc_photo"));
//                analyse = jsonElement.getString("analyse");
//                suggest = jsonElement.getString("suggest");
//                queDetails.add(doctor);
//                JSONArray arrayzhui = jsonElement.getJSONArray("zhuiwen");
//                if (arrayzhui != null && arrayzhui.length() != 0) {
//                    for (int i = 0; i < arrayzhui.length(); i++) {
//                        JSONObject jsonChild = arrayzhui.getJSONObject(i);
//                        int uid = jsonChild.getInt("reply_uid");
//                        if (uid == doctor.getuId()) {
//                            String tempContent = jsonChild
//                                    .getString("content");
//                            String tempTime = jsonChild
//                                    .getString("addtime");
//                            JSONArray picArray1 = jsonChild
//                                    .getJSONArray("picutres");
//                            if ("".equals(tempContent)) {
//                                if (picArray1 != null && picArray1.length() != 0) {
//                                    for (int j = 0; j < picArray1.length(); j++) {
//                                        ListData dataPic = new ListData();
//                                        dataPic.setType(2);
//                                        dataPic.setDetail("");
//                                        dataPic.setuId(uid);
//                                        dataPic.setCreateTime(tempTime);
//                                        dataPic.setPicture(picArray1
//                                                .getString(i));
//                                        queDetails.add(dataPic);
//                                    }
//                                }
//                            } else {
//                                ListData dataZhui = new ListData();
//                                dataZhui.setType(2);
//                                dataZhui.setuId(uid);
//                                dataZhui.setDetail(tempContent);
//                                dataZhui.setCreateTime(tempTime);
//                                queDetails.add(dataZhui);
//                            }
//
//                        } else {
//                            String tempUserContent = jsonChild
//                                    .getString("content");
//                            String tempCreateTime = jsonChild
//                                    .getString("addtime");
//                            JSONArray picArray2 = jsonChild
//                                    .getJSONArray("picutres");
//                            if (!"".equals(tempUserContent)) {
//
//
//                                ListData userZhui = new ListData();
//                                userZhui.setType(3);
//                                userZhui.setuId(uid);
//                                userZhui.setDetail(jsonChild
//                                        .getString("content"));
//                                userZhui.setCreateTime(jsonChild
//                                        .getString("addtime"));
//                                queDetails.add(userZhui);
//                            }
//
//                            if (picArray2 != null && picArray2.length() != 0) {
//                                for (int k = 0; k < picArray2.length(); k++) {
//                                    ListData dataPic = new ListData();
//                                    dataPic.setType(3);
//                                    dataPic.setuId(uid);
//                                    dataPic.setDetail("");
//                                    dataPic.setCreateTime(tempCreateTime);
//                                    dataPic.setPicture(picArray2
//                                            .getString(k));
//                                    queDetails.add(dataPic);
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
}
