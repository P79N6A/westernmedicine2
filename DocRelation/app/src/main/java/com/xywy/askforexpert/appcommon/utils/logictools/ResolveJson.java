package com.xywy.askforexpert.appcommon.utils.logictools;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.util.HanziToPinyin;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.model.BookBaseInfo;
import com.xywy.askforexpert.model.ClinicStatInfo;
import com.xywy.askforexpert.model.CodexInfo;
import com.xywy.askforexpert.model.CodexSecondInfo;
import com.xywy.askforexpert.model.CommentFistInfo;
import com.xywy.askforexpert.model.ConsultingInfo;
import com.xywy.askforexpert.model.DiagnoseLogListInfo;
import com.xywy.askforexpert.model.HospitalInfo;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.model.MySmallActionInfo;
import com.xywy.askforexpert.model.NumStopTimeListInfo;
import com.xywy.askforexpert.model.PatientGroupInfo;
import com.xywy.askforexpert.model.PatientListInfo;
import com.xywy.askforexpert.model.PatientPerInfo;
import com.xywy.askforexpert.model.PersonInfo;
import com.xywy.askforexpert.model.RecrutiCenterInfo;
import com.xywy.askforexpert.model.SectionSoftInfo;
import com.xywy.askforexpert.model.TreatmentListInfo;
import com.xywy.askforexpert.model.UploadImgInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json 解析工具类
 *
 * @author 王鹏
 * @2015-4-30下午2:45:28
 */
public class ResolveJson {
    /**
     * 登陆请求返回解析
     *
     * @param string
     * @return
     */
    public static LoginInfo R_logininfo(String string) {
        LoginInfo logininfo = new LoginInfo();
        try {

            JSONObject jsonObject = new JSONObject(string);
            String code = getjsonString(jsonObject, "code");
            logininfo.setCode(code);
            logininfo.setMsg(getjsonString(jsonObject, "msg"));

            if (code != null && code.equals("0")) {
                if (jsonObject.has("data")) {
                    LoginInfo.UserData datainfo = logininfo.getData();
                    JSONObject data = jsonObject.getJSONObject("data");
                    datainfo.setPid(getjsonString(data, "pid"));
                    datainfo.setUsername(getjsonString(data, "username"));
                    datainfo.setRealname(getjsonString(data, "realname"));
                    datainfo.setToken(getjsonString(data, "token"));
                    datainfo.setCount(getjsonString(data, "count"));
                    datainfo.setIsjob(getjsonString(data, "isjob"));
                    datainfo.setSubject(getjsonString(data, "subject"));
                    datainfo.setIsdoctor(getjsonString(data, "isdoctor"));
                    datainfo.setApproveid(getjsonString(data, "approveid"));
                    datainfo.setHospital(getjsonString(data, "hospital"));
                    datainfo.setHosp_level(getjsonString(data, "hosp_level"));
                    datainfo.setJob(getjsonString(data, "job"));
                    datainfo.setSynopsis(getjsonString(data, "synopsis"));
                    datainfo.setPhoto(getjsonString(data, "photo"));
                    datainfo.setMedal(getjsonString(data, "medal"));
                    datainfo.setCored(getjsonString(data, "cored"));
                    datainfo.setHuanxin_username(getjsonString(data,
                            "huanxin_username"));
                    datainfo.setHuanxin_password(getjsonString(data,
                            "huanxin_password"));
                    datainfo.setUnreplySubject(getjsonString(data,
                            "unreplySubject"));
                    datainfo.setMobileSubject(getjsonString(data,
                            "mobileSubject"));
                    datainfo.setStat(getjsonString(data, "stat"));
                    datainfo.setH_num(getjsonString(data, "h_num"));
                    datainfo.setPhone(getjsonString(data, "phone"));
                    datainfo.setPoints(getjsonString(data, "points"));
                    datainfo.setSubjectName(getjsonString(data, "subjectName"));
                    datainfo.setSchool(getjsonString(data, "school"));
                    datainfo.setProfession(getjsonString(data, "profession"));


                    if (data.has("xiaozhan")) {
                        ClinicStatInfo xiaozhan = datainfo.getXiaozhan();
                        JSONObject xz_json = data.getJSONObject("xiaozhan");

                        xiaozhan.setDati(getjsonString(xz_json, "dati"));
                        xiaozhan.setYuyue(getjsonString(xz_json, "yuyue"));
                        xiaozhan.setPhone(getjsonString(xz_json, "phone"));
                        xiaozhan.setIszj(getjsonString(xz_json, "iszj"));
                        xiaozhan.setFamily(getjsonString(xz_json, "family"));
//						datainfo.setXiaozhan(xiaozhan);
                    }

//					logininfo.setData(datainfo);

                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return logininfo;
    }

    /**
     * 解析图片上传返回图片链接数据
     *
     * @param str
     */
    public static UploadImgInfo R_ImgUpload(String str) {

        UploadImgInfo up = new UploadImgInfo();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            String code = getjsonString(jsonObject, "code");
            up.setCode(code);
            up.setMsg(getjsonString(jsonObject, "msg"));
            if (jsonObject.has("data")) {
                List<UploadImgInfo> list = new ArrayList<UploadImgInfo>();
                JSONArray listjson = jsonObject.getJSONArray("data");
                for (int i = 0; i < listjson.length(); i++) {
                    JSONObject data = listjson.getJSONObject(i);
                    UploadImgInfo up1 = new UploadImgInfo();
                    up1.setCode(getjsonString(data, "code"));
                    up1.setMsg(getjsonString(data, "msg"));
                    up1.setUrl(getjsonString(data, "url"));
                    list.add(up1);
                }
                up.setData(list);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return up;

    }

    /**
     * 解析图片上传返回图片链接数据
     *
     * @param str
     */
    public static UploadImgInfo R_ImgUpload_Yixian(String str) {

        UploadImgInfo up = new UploadImgInfo();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            String code = getjsonString(jsonObject, "result");
            up.setCode(code);
            up.setMsg(getjsonString(jsonObject, "msg"));
            if (jsonObject.has("data")) {
                List<UploadImgInfo> list = new ArrayList<UploadImgInfo>();
                JSONArray listjson = jsonObject.getJSONArray("data");
                for (int i = 0; i < listjson.length(); i++) {

                    UploadImgInfo up1 = new UploadImgInfo();
                    up1.setUrl(listjson.getString(i));
                    list.add(up1);
                }
                up.setData(list);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return up;

    }

    /**
     * 解析资讯列表类
     *
     * @param str
     * @return
     */
    public static ConsultingInfo R_Consult(String str) {

        ConsultingInfo con = new ConsultingInfo();
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject(str);
            String code = getjsonString(jsonObject, "code");
            con.setCode(code);
            con.setMsg(getjsonString(jsonObject, "msg"));
            if (jsonObject.has("list")) {
                List<ConsultingInfo> con_list = new ArrayList<ConsultingInfo>();

                JSONArray json = jsonObject.getJSONArray("list");
                for (int i = 0; i < json.length(); i++) {
                    ConsultingInfo con_1 = new ConsultingInfo();
                    JSONObject data = json.getJSONObject(i);
                    con_1.setId(getjsonString(data, "id"));
                    con_1.setTitle(getjsonString(data, "title"));
                    con_1.setImage(getjsonString(data, "image"));
                    con_1.setCreatetime(getjsonString(data, "createtime"));
                    con_1.setPraiseNum(getjsonString(data, "praiseNum"));
                    con_1.setUrl(getjsonString(data, "url"));
                    con_1.setColle(getjsonString(data, "colle"));
                    con_1.setPraise(getjsonString(data, "praise"));
                    con_list.add(con_1);
                }
                con.setList(con_list);

            }
            return con;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 资讯 收藏 点赞
     *
     * @param str
     * @return
     */
    public static Map<String, String> R_Action(String str) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jsonObject;

            jsonObject = new JSONObject(str);
            // String code = getjsonString(jsonObject, "code");
            map.put("code", getjsonString(jsonObject, "code"));
            map.put("msg", getjsonString(jsonObject, "msg"));
            return (HashMap<String, String>) map;
            // con.setMsg(getjsonString(jsonObject, "msg"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 医生二维码
     *
     * @param str
     * @return
     */
    public static Map<String, String> R_Action_two(String str) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jsonObject;

            jsonObject = new JSONObject(str);
            // String code = getjsonString(jsonObject, "code");
            map.put("code", getjsonString(jsonObject, "code"));
            map.put("msg", getjsonString(jsonObject, "msg"));
            map.put("data", getjsonString(jsonObject, "data"));
            return  map;
            // con.setMsg(getjsonString(jsonObject, "msg"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析 资讯 评论列表
     *
     * @param str
     * @return
     */
    public static CommentFistInfo R_commentfistlist(String str) {
        CommentFistInfo coInfo = new CommentFistInfo();
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject(str);
            coInfo.setCode(getjsonString(jsonObject, "code"));
            coInfo.setMsg(getjsonString(jsonObject, "msg"));
            coInfo.setCommentNum(getjsonString(jsonObject, "commentNum"));
            if (jsonObject.has("list")) {
                JSONArray json = jsonObject.getJSONArray("list");
                List<CommentFistInfo> list = new ArrayList<CommentFistInfo>();
                for (int i = 0; i < json.length(); i++) {
                    CommentFistInfo comm_list = new CommentFistInfo();
                    JSONObject data = json.getJSONObject(i);
                    comm_list.setId(getjsonString(data, "id"));
                    comm_list.setUserid(getjsonString(data, "userid"));
                    comm_list.setToUserid(getjsonString(data, "toUserid"));
                    comm_list.setThemeid(getjsonString(data, "themeid"));
                    comm_list.setCreatetime(getjsonString(data, "createtime"));
                    comm_list.setPid(getjsonString(data, "pid"));
                    comm_list.setPraiseNum(getjsonString(data, "praiseNum"));
                    comm_list.setContent(getjsonString(data, "content"));
                    comm_list.setCommentNum(getjsonString(data, "commentNum"));
                    CommentFistInfo doc = new CommentFistInfo();
                    JSONObject data_2 = data.getJSONObject("doc");
                    doc.setName(getjsonString(data_2, "name"));
                    doc.setPhoto(getjsonString(data_2, "photo"));
                    doc.setId(getjsonString(data_2, "id"));
                    comm_list.setDoc(doc);
                    comm_list.setNomaldate(getjsonString(data, "nomaldate"));
                    List<CommentFistInfo> list_second = new ArrayList<CommentFistInfo>();

                    if (data.has("list")) {
                        JSONArray json_2 = data.getJSONArray("list");
                        for (int j = 0; j < json_2.length(); j++) {
                            CommentFistInfo comm_list_2 = new CommentFistInfo();
                            JSONObject data_3 = json_2.getJSONObject(j);
                            comm_list_2.setId(getjsonString(data_3, "id"));
                            comm_list_2.setUserid(getjsonString(data_3,
                                    "userid"));
                            comm_list_2.setToUserid(getjsonString(data_3,
                                    "toUserid"));
                            comm_list_2.setThemeid(getjsonString(data_3,
                                    "themeid"));
                            comm_list_2.setCreatetime(getjsonString(data_3,
                                    "createtime"));
                            comm_list_2.setPid(getjsonString(data_3, "pid"));
                            comm_list_2.setPraiseNum(getjsonString(data_3,
                                    "praiseNum"));
                            comm_list_2.setContent(getjsonString(data_3,
                                    "content"));
                            comm_list_2.setCommentNum(getjsonString(data_3,
                                    "commentNum"));
                            CommentFistInfo doc_2 = new CommentFistInfo();
                            JSONObject data_4 = data_3.getJSONObject("doc");
                            doc_2.setName(getjsonString(data_4, "name"));
                            doc_2.setPhoto(getjsonString(data_4, "photo"));
                            doc_2.setId(getjsonString(data_4, "id"));
                            comm_list_2.setDoc(doc_2);
                            CommentFistInfo todoc_2 = new CommentFistInfo();
                            JSONObject data_5 = data_3.getJSONObject("todoc");
                            todoc_2.setName(getjsonString(data_5, "name"));
                            todoc_2.setPhoto(getjsonString(data_5, "photo"));
                            todoc_2.setId(getjsonString(data_5, "id"));
                            comm_list_2.setTodoc(todoc_2);

                            comm_list_2.setNomaldate(getjsonString(data_3,
                                    "nomaldate"));
                            list_second.add(comm_list_2);
                        }
                        comm_list.setSecond_list(list_second);
                    }
                    list.add(comm_list);
                }
                coInfo.setList(list);
            }
            return coInfo;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析个人信息
     *
     * @param str
     * @return
     */
    public static PersonInfo R_personinfo(String str) {

        PersonInfo perinfo = new PersonInfo();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            String code = getjsonString(jsonObject, "code");
            perinfo.setCode(code);
            perinfo.setMsg(getjsonString(jsonObject, "msg"));
            if (jsonObject.has("data")) {
                JSONObject data = jsonObject.getJSONObject("data");
                PersonInfo data_info = new PersonInfo();
                data_info.setSchool(getjsonString(data, "school"));
                data_info.setProfession(getjsonString(data, "profession"));
                data_info.setTraining_hospital(getjsonString(data,
                        "training_hospital"));
                data_info.setRealname(getjsonString(data, "realname"));
                data_info.setSex(getjsonString(data, "sex"));
                data_info.setBirth_day(getjsonString(data, "birth_day"));
                data_info.setPhone(getjsonString(data, "phone"));
                data_info.setProfess_job(getjsonString(data, "profess_job"));
                data_info.setJob(getjsonString(data, "job"));
                data_info.setHospital(getjsonString(data, "hospital"));
                data_info.setSubject(getjsonString(data, "subject"));
                data_info.setSubject2(getjsonString(data, "subject2"));
                data_info.setSpecial(getjsonString(data, "special"));
                data_info.setSynopsis(getjsonString(data, "synopsis"));
                data_info.setPhoto(getjsonString(data, "photo"));
                data_info.setCity(getjsonString(data, "city"));
                data_info.setProvince(getjsonString(data, "province"));
                data_info.setProfess_job_id(getjsonString(data,
                        "profess_job_id"));
                data_info.setJob_id(getjsonString(data, "job_id"));
                data_info.setCityName(getjsonString(data, "cityName"));
                data_info.setProvinceName(getjsonString(data, "provinceName"));
                data_info.setSubject_id(getjsonString(data, "subject_id"));
                data_info.setSubject2_id(getjsonString(data, "subject2_id"));
                perinfo.setData(data_info);
            }
            return perinfo;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // logininfo.setCode(code);
        return null;
    }

    /**
     * 药典 列表
     *
     * @param str
     * @return
     */
    public static CodexInfo R_codex(String str) {
        CodexInfo codeInfo = new CodexInfo();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            String code = getjsonString(jsonObject, "code");
            codeInfo.setCode(code);
            codeInfo.setMsg(getjsonString(jsonObject, "msg"));
            codeInfo.setTotal(getjsonString(jsonObject, "total"));
            if (jsonObject.has("list")) {
                List<CodexInfo> list = new ArrayList<CodexInfo>();
                JSONArray json = jsonObject.getJSONArray("list");
                for (int i = 0; i < json.length(); i++) {
                    CodexInfo code_1 = new CodexInfo();
                    JSONObject data = json.getJSONObject(i);
                    code_1.setName(getjsonString(data, "name"));
                    code_1.setId(getjsonString(data, "id"));

                    if (data.has("list")) {
                        List<CodexInfo> list_second = new ArrayList<CodexInfo>();
                        JSONArray json_2 = data.getJSONArray("list");
                        for (int j = 0; j < json_2.length(); j++) {
                            CodexInfo code_2 = new CodexInfo();
                            JSONObject data_2 = json_2.getJSONObject(j);
                            code_2.setId(getjsonString(data_2, "id"));
                            code_2.setName(getjsonString(data_2, "name"));

                            list_second.add(code_2);
                        }
                        code_1.setList_second(list_second);

                    }
                    list.add(code_1);
                }
                codeInfo.setList(list);
            }
            return codeInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析 检查手册 药典 二级列表
     *
     * @param str
     * @return
     */
    public static CodexSecondInfo R_codexSecond(String str) {
        CodexSecondInfo codexinfo = new CodexSecondInfo();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            String code = getjsonString(jsonObject, "code");
            codexinfo.setCode(code);
            codexinfo.setMsg(getjsonString(jsonObject, "msg"));
            codexinfo.setTotal(getjsonString(jsonObject, "total"));
            if (jsonObject.has("list")) {
                List<CodexSecondInfo> list = new ArrayList<CodexSecondInfo>();
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    CodexSecondInfo cinfo = new CodexSecondInfo();
                    JSONObject data = jsonArray.getJSONObject(i);
                    cinfo.setTitle(getjsonString(data, "title"));
                    cinfo.setId(getjsonString(data, "id"));
                    cinfo.setUrl(getjsonString(data, "url"));
                    // cinfo.setMsg(getjsonString(data, "msg"));
                    // cinfo.setStatus(getjsonString(data, "status"));

                    cinfo.setAuthor(getjsonString(data, "author"));
                    cinfo.setCreatetime(getjsonString(data, "createtime"));
                    cinfo.setFilesize(getjsonString(data, "filesize"));
                    cinfo.setSource(getjsonString(data, "source"));
                    cinfo.setDownloadurl(getjsonString(data, "downloadurl"));
                    cinfo.setIscollection(getjsonString(data, "iscollection"));
                    cinfo.setMsg_iscollection(getjsonString(data,
                            "msg_iscollection"));

                    list.add(cinfo);
                }
                codexinfo.setList(list);
            }
            return codexinfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析 招聘列表
     *
     * @param str
     * @return
     */
    public static RecrutiCenterInfo R_Recruit_Center(String str) {
        RecrutiCenterInfo recinfo = new RecrutiCenterInfo();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            recinfo.setCode(getjsonString(jsonObject, "code"));
            recinfo.setMsg(getjsonString(jsonObject, "msg"));
            if (jsonObject.has("list")) {
                RecrutiCenterInfo recinfo2 = new RecrutiCenterInfo();

                JSONObject joson_list = jsonObject.getJSONObject("list");
                recinfo2.setCount(getjsonString(joson_list, "count"));
                if (joson_list.has("list")) {

                    List<RecrutiCenterInfo> list = new ArrayList<RecrutiCenterInfo>();
                    JSONArray json = joson_list.getJSONArray("list");
                    for (int i = 0; i < json.length(); i++) {
                        RecrutiCenterInfo rci = new RecrutiCenterInfo();
                        JSONObject data = json.getJSONObject(i);
                        rci.setId(getjsonString(data, "id"));
                        rci.setUrl(getjsonString(data, "url"));
                        rci.setDeliver(getjsonString(data, "deliver"));
                        rci.setColl(getjsonString(data, "coll"));
                        rci.setTitle(getjsonString(data, "title"));
                        rci.setUpdatetime(getjsonString(data, "updatetime"));
                        rci.setLogo(getjsonString(data, "logo"));
                        rci.setEntename(getjsonString(data, "entename"));
                        list.add(rci);
                    }
                    recinfo2.setList(list);
                }
                recinfo.setLists(recinfo2);

            }
            return recinfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析 招聘列表
     *
     * @param str
     * @return
     */
    public static RecrutiCenterInfo R_Mysume(String str) {
        RecrutiCenterInfo recinfo = new RecrutiCenterInfo();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            recinfo.setCode(getjsonString(jsonObject, "code"));
            recinfo.setMsg(getjsonString(jsonObject, "msg"));
            if (jsonObject.has("list")) {
                JSONObject json_objet = jsonObject.getJSONObject("list");

                RecrutiCenterInfo rec_list = new RecrutiCenterInfo();
                rec_list.setCount(getjsonString(json_objet, "count"));
                if (json_objet.has("list")) {
                    List<RecrutiCenterInfo> list = new ArrayList<RecrutiCenterInfo>();
                    JSONArray json = json_objet.getJSONArray("list");
                    for (int i = 0; i < json.length(); i++) {
                        RecrutiCenterInfo rci = new RecrutiCenterInfo();
                        JSONObject data = json.getJSONObject(i);

                        rci.setCreatetime(getjsonString(data, "createtime"));
                        rci.setIs_show(getjsonString(data, "is_show"));
                        rci.setUrl(getjsonString(data, "url"));
                        rci.setTitle(getjsonString(data, "title"));
                        rci.setUpdatetime(getjsonString(data, "updatetime"));
                        rci.setLogo(getjsonString(data, "logo"));
                        rci.setEntename(getjsonString(data, "entename"));
                        if (data.has("pid")) {
                            rci.setId(getjsonString(data, "pid"));
                        } else if (data.has("id")) {
                            rci.setId(getjsonString(data, "id"));
                        }

                        rci.setCollectiontime(getjsonString(data, "collectiontime"));
                        rci.setReturnedtime(getjsonString(data, "returnedtime"));
                        list.add(rci);
                    }
                    rec_list.setList(list);

                }
                recinfo.setList_first(rec_list);
            }
            return recinfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析我的简历
     *
     * @param str
     * @return
     */
    public static Map<String, String> R_MyResume(String str) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jsonObject;

            jsonObject = new JSONObject(str);
            // String code = getjsonString(jsonObject, "code");
            map.put("code", getjsonString(jsonObject, "code"));
            map.put("msg", getjsonString(jsonObject, "msg"));
            map.put("url", getjsonString(jsonObject, "url"));
            map.put("show", getjsonString(jsonObject, "show"));
            return map;
            // con.setMsg(getjsonString(jsonObject, "msg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析名片夹
     *
     * @param str
     * @return
     */
    public static AddressBook R_CardHold(String str) {
        AddressBook cardinfo = new AddressBook();
        try {
            JSONObject jsonobject = new JSONObject(str);
            cardinfo.setCode(getjsonString(jsonobject, "code"));
            cardinfo.setMsg(getjsonString(jsonobject, "msg"));
            ArrayList<AddressBook> childMediaList = new ArrayList<>();
            ArrayList<AddressBook> childServiceList = new ArrayList<>();
            if (jsonobject.has("data")) {
                JSONArray json = jsonobject.getJSONArray("data");
                List<AddressBook> data_info = new ArrayList<AddressBook>();
                for (int i = 0; i < json.length(); i++) {
                    JSONObject data = json.getJSONObject(i);
                    if (getjsonString(data, "type").equals("3")) {
                        AddressBook childMedia = new AddressBook();
                        childMedia.setType(3);
                        childMedia.setId(getjsonString(data, "id"));
                        childMedia.setPhoto(getjsonString(data, "photo"));
                        String realName = getjsonString(data, "realname").trim();
                        String chxusername = getjsonString(data, "hxusername");
                        if (TextUtils.isEmpty(realName)) {
                            childMedia.setRealname(chxusername);
                        } else {
                            childMedia.setRealname(realName);
                        }
                        childMedia.setHxusername(chxusername);
                        childMedia.setMobile(getjsonString(data, "mobile"));
                        childMedia.setHassend(getjsonString(data, "hassend"));

                        if (realName.equals("")) {
                            childMedia.setHeader("#");
                        } else {
                            childMedia.setHeader(formatRealname(realName));
                            char header = childMedia.getHeader().toLowerCase().charAt(0);
                            if (header < 'a' || header > 'z') {
                                childMedia.setHeader("#");
                            }
                        }

                        childMediaList.add(childMedia);
                        continue;
                    }

                    if (getjsonString(data, "type").equals("2")) {
                        AddressBook childService = new AddressBook();
                        childService.setType(2);
                        childService.setId(getjsonString(data, "id"));
                        childService.setPhoto(getjsonString(data, "photo"));
                        String realName = getjsonString(data, "realname").trim();
                        String chxusername = getjsonString(data, "hxusername");
                        if (TextUtils.isEmpty(realName)) {
                            childService.setRealname(chxusername);
                        } else {
                            childService.setRealname(realName);
                        }
                        childService.setHxusername(chxusername);
                        childService.setMobile(getjsonString(data, "mobile"));
                        childService.setHassend(getjsonString(data, "hassend"));
                        if (realName.equals("")) {
                            childService.setHeader("#");
                        } else {
                            childService.setHeader(formatRealname(realName));
                            char header = childService.getHeader().toLowerCase().charAt(0);
                            if (header < 'a' || header > 'z') {
                                childService.setHeader("#");
                            }
                        }
                        childServiceList.add(childService);
                        continue;
                    }

                    AddressBook card = new AddressBook();
                    card.setId(getjsonString(data, "id"));
                    card.setPhoto(getjsonString(data, "photo"));
                    String realName = getjsonString(data, "realname").trim();
                    String hxusername = getjsonString(data, "hxusername");
                    if (TextUtils.isEmpty(realName)) {
                        card.setRealname(hxusername);
                    } else {
                        card.setRealname(realName);
                    }

                    card.setHxusername(getjsonString(data, "hxusername"));
                    card.setMobile(getjsonString(data, "mobile"));
                    card.setHassend(getjsonString(data, "hassend"));
                    card.setType(getjsonInt(data, "type"));

                    if (realName.equals("")) {
                        card.setHeader("#");
                    } else {
                        //stone 去掉我的助理
                        if ("我的助理".equals(realName)) {
                            card.setHeader("");
                        } else if (hxusername.contains("sid_")) {        //病例研讨班id
                            card.setHeader("");
                            data_info.add(card);
                        } else {
                            card.setHeader(formatRealname(realName));
                            char header = card.getHeader().toLowerCase().charAt(0);
                            if (header < 'a' || header > 'z') {
                                card.setHeader("#");

                            }
                            data_info.add(card);
                        }
//                        data_info.add(card);
                    }

                }

                if (childMediaList.size() > 0) {
                    AddressBook meida = new AddressBook();
                    meida.setType(3);
                    meida.setHeader("");
                    meida.setRealname("媒体号");
                    meida.setHxusername("媒体号");
                    meida.setPhoto("媒体号");
                    meida.setHassend("0");
                    meida.setId("media");
                    meida.setData(childMediaList);
                    data_info.add(meida);
                }

                if (childServiceList.size() > 0) {
                    AddressBook service = new AddressBook();
                    service.setType(2);
                    service.setHeader("");
                    service.setRealname("服务号");
                    service.setHxusername("服务号");
                    service.setPhoto("服务号");
                    service.setHassend("0");
                    service.setId("service");
                    service.setData(childServiceList);
                    data_info.add(service);
                }

                if (childServiceList.size() > 0) {
                    AddressBook groupItem = new AddressBook();
                    groupItem.setType(5);
                    groupItem.setHeader("");
                    groupItem.setRealname("群组");
                    groupItem.setHxusername("群组");
                    groupItem.setPhoto("群组");
                    groupItem.setHassend("0");
                    groupItem.setId("group");
                    groupItem.setData(childServiceList);
                    data_info.add(groupItem);
                }

                cardinfo.setData(data_info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cardinfo;
    }

    /**
     * 解析 患者管理 名片夹列表
     *
     * @param str
     * @return
     */
    public static PatientListInfo R_Friend(String str) {
        PatientListInfo patientinfo = new PatientListInfo();
        try {
            JSONObject jsonObject = new JSONObject(str);
            patientinfo.setCode(getjsonString(jsonObject, "code"));
            patientinfo.setMsg(getjsonString(jsonObject, "msg"));

            if (jsonObject.has("data")) {
                PatientListInfo data = new PatientListInfo();
                JSONObject json_data = jsonObject.getJSONObject("data");
                data.setNewpatient(getjsonString(json_data, "newpatient"));
                data.setServered(getjsonString(json_data, "servered"));
                data.setListcount(getjsonString(json_data, "listcount"));
                if (json_data.has("list")) {
                    List<PatientListInfo> list = new ArrayList<PatientListInfo>();
                    JSONArray json = json_data.getJSONArray("list");
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject datas = json.getJSONObject(i);
                        String realName = getjsonString(datas, "realname")
                                .trim();
                        PatientListInfo list_info = new PatientListInfo();
                        list_info.setId(getjsonString(datas, "id"));
                        list_info.setPhoto(getjsonString(datas, "photo"));
                        if (TextUtils.isEmpty(realName)) {
                            list_info.setRealname(getjsonString(datas,
                                    "hxusername"));
                        } else {
                            list_info.setRealname(realName);
                        }

                        list_info.setHxusername(getjsonString(datas,
                                "hxusername"));
                        list_info.setMobile(getjsonString(datas, "mobile"));
                        list_info.setHassend(getjsonString(datas, "hassend"));

                        list_info.setHeader(formatRealname(realName));
                        char header = list_info.getHeader().toLowerCase()
                                .charAt(0);
                        if (header < 'a' || header > 'z') {
                            list_info.setHeader("#");
                        }

                        list.add(list_info);
                    }
                    data.setList(list);
                }
                patientinfo.setData(data);
            }
            return patientinfo;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
//		return null;
    }

    @NonNull
    private static String formatRealname(String realName) {
        if (TextUtils.isEmpty(realName)){
            Log.e(ResolveJson.class.getSimpleName(),"realName 为空");
            return "H";//default name 为会员
        }
        return HanziToPinyin.getInstance()
                .get(realName.substring(0, 1)).get(0).target
                .substring(0, 1).toUpperCase();
    }

    /**
     * 解析 患者个人信息
     *
     * @param str
     * @return
     */
    public static PatientPerInfo R_PatientPer(String str) {
        PatientPerInfo ppinfo = new PatientPerInfo();
        try {
            JSONObject json = new JSONObject(str);
            ppinfo.setCode(getjsonString(json, "code"));
            ppinfo.setMsg(getjsonString(json, "msg"));
            if (json.has("data")) {
                JSONObject data = json.getJSONObject("data");
                PatientPerInfo pp_data = new PatientPerInfo();
                pp_data.setRealname(getjsonString(data, "realname"));
                pp_data.setSex(getjsonString(data, "sex"));
                pp_data.setAge(getjsonString(data, "age"));
                pp_data.setGid(getjsonString(data, "gid"));
                pp_data.setGname(getjsonString(data, "gname"));
                pp_data.setPhoto(getjsonString(data, "photo"));
                ppinfo.setData(pp_data);
            }
            return ppinfo;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析患者管理分组列表
     *
     * @param str
     * @return
     */
    public static PatientGroupInfo R_PatientGroupList(String str) {
        PatientGroupInfo pginfo = new PatientGroupInfo();
        try {
            JSONObject json = new JSONObject(str);
            pginfo.setCode(getjsonString(json, "code"));
            pginfo.setMsg(getjsonString(json, "id"));
            if (json.has("data")) {
                List<PatientGroupInfo> data_list = new ArrayList<PatientGroupInfo>();

                JSONArray data_json = json.getJSONArray("data");
                for (int i = 0; i < data_json.length(); i++) {
                    PatientGroupInfo p_data = new PatientGroupInfo();
                    JSONObject data = data_json.getJSONObject(i);
                    p_data.setId(getjsonString(data, "id"));
                    p_data.setName(getjsonString(data, "name"));
                    data_list.add(p_data);
                }
                pginfo.setData(data_list);

            }
            return pginfo;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析诊断列表页面
     *
     * @param str
     * @return
     */
    public static DiagnoseLogListInfo R_dialoglist(String str) {
        DiagnoseLogListInfo dialistinfo = new DiagnoseLogListInfo();
        try {
            JSONObject json = new JSONObject(str);
            dialistinfo.setCode(getjsonString(json, "code"));
            dialistinfo.setMsg(getjsonString(json, "msg"));
            dialistinfo.setIscreate(getjsonString(json, "iscreate"));
            dialistinfo.setSickinfo(getjsonString(json, "sickinfo"));
            if (json.has("data")) {
                List<DiagnoseLogListInfo> data_list = new ArrayList<DiagnoseLogListInfo>();
                JSONArray json_data = json.getJSONArray("data");
                for (int i = 0; i < json_data.length(); i++) {
                    DiagnoseLogListInfo data_info = new DiagnoseLogListInfo();

                    JSONObject data = json_data.getJSONObject(i);
                    data_info.setId(getjsonString(data, "id"));
                    data_info.setDid(getjsonString(data, "did"));
                    data_info.setUpdatetime(getjsonString(data, "updatetime"));
                    data_info.setCotent(getjsonString(data, "content"));
                    data_info.setRealname(getjsonString(data, "realname"));
                    data_list.add(data_info);
                }
                dialistinfo.setData(data_list);
            }

            return dialistinfo;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析 诊断纪录 详情
     *
     * @param str
     * @return
     */
    public static TreatmentListInfo R_treatment(String str) {
        TreatmentListInfo treatment = new TreatmentListInfo();
        try {
            JSONObject json = new JSONObject(str);
            treatment.setCode(getjsonString(json, "code"));
            treatment.setMsg(getjsonString(json, "msg"));
            if (json.has("data")) {
                TreatmentListInfo data_treat = new TreatmentListInfo();
                JSONObject json_data = json.getJSONObject("data");
                data_treat.setId(getjsonString(json_data, "id"));
                data_treat.setDid(getjsonString(json_data, "did"));
                data_treat.setSlickname(getjsonString(json_data, "sickname"));
                data_treat.setSlickinfo(getjsonString(json_data, "sickinfo"));
                if (json_data.has("list")) {
                    List<TreatmentListInfo> list = new ArrayList<TreatmentListInfo>();
                    JSONArray json_array = json_data.getJSONArray("list");
                    for (int i = 0; i < json_array.length(); i++) {
                        JSONObject data = json_array.getJSONObject(i);
                        TreatmentListInfo list_treate = new TreatmentListInfo();
                        list_treate.setId(getjsonString(data, "id"));
                        list_treate.setContent(getjsonString(data, "content"));
                        list_treate.setAddtime(getjsonString(data, "addtime"));
                        JSONArray img_arr = data.getJSONArray("imgs");
                        List<String> img = new ArrayList<String>();
                        for (int j = 0; j < img_arr.length(); j++) {
                            img.add(img_arr.getString(j));
                            list_treate.setImgs(img);
                        }
                        list.add(list_treate);
                    }
                    data_treat.setList(list);

                }
                treatment.setData(data_treat);

            }
            return treatment;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

//    /**
//     * 解析我的小站
//     */
//
//    public static MySmallActionInfo R_mysmallaction(String str) {
//        MySmallActionInfo smallaction = new MySmallActionInfo();
//        try {
//            JSONObject json = new JSONObject(str);
//            smallaction.setCode(getjsonString(json, "code"));
//            smallaction.setMsg(getjsonString(json, "msg"));
//            if (json.has("data")) {
//                MySmallActionInfo data_info = new MySmallActionInfo();
//                JSONObject data = json.getJSONObject("data");
//                data_info.setSpeciality(getjsonString(data, "speciality"));
//                data_info.setSynopsis(getjsonString(data, "synopsis"));
//                if (data.has("clinic")) {
//                    JSONArray cl_arr = data.getJSONArray("clinic");
//                    List<MySmallActionInfo> clin_list = new ArrayList<MySmallActionInfo>();
//                    for (int i = 0; i < cl_arr.length(); i++) {
//                        JSONObject clin_data = cl_arr.getJSONObject(i);
//                        MySmallActionInfo clin_info = new MySmallActionInfo();
//                        clin_info.setTitle(getjsonString(clin_data, "title"));
//                        clin_info
//                                .setIsclicl(getjsonString(clin_data, "isclicl"));
//                        clin_info.setPrice(getjsonString(clin_data, "price"));
//                        clin_info.setNumber(getjsonString(clin_data, "number"));
//                        clin_list.add(clin_info);
//                    }
//                    data_info.setClinic(clin_list);
//                }
//                if (data.has("grade")) {
//                    JSONArray grad_arr = data.getJSONArray("grade");
//                    List<MySmallActionInfo> grad_list = new ArrayList<MySmallActionInfo>();
//                    for (int i = 0; i < grad_arr.length(); i++) {
//                        JSONObject grad_data = grad_arr.getJSONObject(i);
//                        MySmallActionInfo grad_info = new MySmallActionInfo();
//                        grad_info.setG_uid(getjsonString(grad_data, "g_uid"));
//                        grad_info.setG_cons(getjsonString(grad_data, "g_cons"));
//                        grad_info.setG_date(getjsonString(grad_data, "g_date"));
//                        grad_info.setG_stat(getjsonString(grad_data, "g_stat"));
//                        grad_info
//                                .setG_uname(getjsonString(grad_data, "g_uname"));
//                        grad_list.add(grad_info);
//                    }
//                    data_info.setGrade(grad_list);
//                }
//                smallaction.setData(data_info);
//
//            }
//            return smallaction;
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 解析医院
     *
     * @param str
     */
    public static HospitalInfo R_hospatil(String str) {

        HospitalInfo up = new HospitalInfo();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            String code = getjsonString(jsonObject, "code");
            up.setCode(code);
            up.setMsg(getjsonString(jsonObject, "msg"));
            if (jsonObject.has("data")) {
                List<String> list = new ArrayList<String>();
                JSONArray listjson = jsonObject.getJSONArray("data");
                for (int i = 0; i < listjson.length(); i++) {
                    list.add(listjson.getString(i));
                }
                up.setData(list);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return up;

    }

    /**
     * 返回 含有 name id 的list hashmap
     *
     * @param str
     * @return
     */
    public static List<HashMap<String, String>> R_List(String str) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONArray json = new JSONArray(str);
            for (int i = 0; i < json.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject data = json.getJSONObject(i);
                map.put("name", getjsonString(data, "name"));
                map.put("id", getjsonString(data, "id"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 返回 含有 name id 的list hashmap
     *
     * @param str
     * @return
     */
    public static List<List<HashMap<String, String>>> R_List_List(String str) {
        List<List<HashMap<String, String>>> list = new ArrayList<List<HashMap<String, String>>>();
        try {
            JSONArray json = new JSONArray(str);
            for (int i = 0; i < json.length(); i++) {
                List<HashMap<String, String>> list_inner = new ArrayList<>();
                JSONArray json_inner = json.getJSONArray(i);
                for(int j=0;j<json_inner.length();j++){
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject data = json_inner.getJSONObject(j);
                    map.put("name", getjsonString(data, "name"));
                    map.put("id", getjsonString(data, "id"));
                    list_inner.add(map);
                }
                list.add(list_inner);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 返回 含有 name id 的list hashmap
     *
     * @param str
     * @return
     */
    public static List<HashMap<String, String>> R_List_id(String str, String id) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {

            JSONObject json = new JSONObject(str);
            if (json.has(id)) {

                JSONArray json1 = json.getJSONArray(id);
                for (int j = 0; j < json1.length(); j++) {
                    // JSONObject data=json1.getJSONObject(j);
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject data = json1.getJSONObject(j);
                    map.put("name", getjsonString(data, "name"));
                    map.put("id", getjsonString(data, "id"));
                    list.add(map);
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回 含有 name id 的list hashmap
     *
     * @param str
     * @return
     */
    public static List<HashMap<String, String>> R_List_id2(String str, String id) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {

            JSONObject json = new JSONObject(str);
            if (json.has(id)) {
                JSONObject jsons = json.getJSONObject(id);
                // JSONObject datas = jsons.getJSONObject("list");

                JSONArray json1 = jsons.getJSONArray("list");
                for (int j = 0; j < json1.length(); j++) {
                    // JSONObject data=json1.getJSONObject(j);
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject data = json1.getJSONObject(j);
                    map.put("name", getjsonString(data, "name"));
                    map.put("id", getjsonString(data, "id"));
                    list.add(map);
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 扫面 获取结果请求
     *
     * @param str
     * @return
     */
    public static Map<String, String> R_Action_twos(String str) {
        Map<String, String> map = new HashMap<String, String>();
        JSONObject josnObject;
        try {
            josnObject = new JSONObject(str);
            map.put("code", getjsonString(josnObject, "code"));
            map.put("msg", getjsonString(josnObject, "msg"));
            map.put("isxywy", getjsonString(josnObject, "isxywy"));
            map.put("did", getjsonString(josnObject, "did"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return  map;
    }

    /**
     * 招聘职位收缩
     *
     * @param str
     * @param key
     * @return
     */
    public static List<HashMap<String, String>> R_r_serch(String str,
                                                          String[] key) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        JSONArray json;
        try {
            json = new JSONArray(str);
            for (int i = 0; i < json.length(); i++) {
                JSONObject data = json.getJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();
                for (int j = 0; j < key.length; j++) {
                    map.put(key[j], getjsonString(data, key[j]));
                }

                list.add(map);
            }
            return list;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 科室分类 指南
     *
     * @param str
     * @return
     */
    public static SectionSoftInfo R_section_soft(String str) {
        SectionSoftInfo section = new SectionSoftInfo();
        try {
            JSONObject json = new JSONObject(str);
            section.setCode(getjsonString(json, "code"));
            section.setMsg(getjsonString(json, "msg"));
            if (json.has("list")) {
                List<SectionSoftInfo> group_list = new ArrayList<SectionSoftInfo>();
                JSONArray grouparr = json.getJSONArray("list");
                for (int i = 0; i < grouparr.length(); i++) {
                    JSONObject group_obj = grouparr.getJSONObject(i);
                    SectionSoftInfo groups = new SectionSoftInfo();
                    groups.setName(getjsonString(group_obj, "name"));
                    groups.setId(getjsonString(group_obj, "id"));

                    if (group_obj.has("list")) {
                        List<SectionSoftInfo> child_list = new ArrayList<SectionSoftInfo>();
                        JSONArray childarr = group_obj.getJSONArray("list");
                        for (int j = 0; j < childarr.length(); j++) {
                            JSONObject child_obj = childarr.getJSONObject(j);
                            SectionSoftInfo childs = new SectionSoftInfo();
                            childs.setName(getjsonString(child_obj, "name"));
                            childs.setId(getjsonString(child_obj, "id"));
                            child_list.add(childs);
                        }
                        groups.setChild_list(child_list);
                    }
                    group_list.add(groups);

                }
                section.setGroup_list(group_list);

            }
            return section;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static BookBaseInfo R_Consult_Book(String str) {
        BookBaseInfo info = new BookBaseInfo();
        try {
            JSONObject json = new JSONObject(str);
            info.setCode(getjsonString(json, "code"));
            info.setMsg(getjsonString(json, "msg"));
            if (json.has("list")) {
                BookBaseInfo datainfo = new BookBaseInfo();
                JSONObject data = json.getJSONObject("list");
                datainfo.setIscollection(getjsonString(data, "colle"));
                datainfo.setIspraise(getjsonString(data, "praise"));
                datainfo.setCommNum(getjsonString(data, "commNum"));
                info.setList(datainfo);
            }

            return info;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析 停诊服务 列表
     *
     * @param str
     * @return
     */
    public static NumStopTimeListInfo R_num_stop_time_list(String str) {
        NumStopTimeListInfo num_list_info = new NumStopTimeListInfo();
        try {
            JSONObject josnObject = new JSONObject(str);
            num_list_info.setCode(getjsonString(josnObject, "code"));
            num_list_info.setMsg(getjsonString(josnObject, "msg"));
            if (josnObject.has("data")) {
                NumStopTimeListInfo data_info = new NumStopTimeListInfo();
                JSONObject data = josnObject.getJSONObject("data");
                data_info.setTotal(getjsonString(data, "total"));
                if (data.has("data")) {
                    List<NumStopTimeListInfo> list = new ArrayList<NumStopTimeListInfo>();
                    JSONArray jsonArray = data.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data_n = jsonArray.getJSONObject(i);
                        NumStopTimeListInfo data_info2 = new NumStopTimeListInfo();
                        data_info2.setStartdate(getjsonString(data_n,
                                "startdate"));
                        data_info2.setEnddate(getjsonString(data_n, "enddate"));
                        data_info2.setReason(getjsonString(data_n, "reason"));
                        data_info2.setAddtime(getjsonString(data_n, "addtime"));
                        data_info2.setState(getjsonString(data_n, "state"));
                        data_info2.setState_name(getjsonString(data_n,
                                "state_name"));
                        list.add(data_info2);
                    }
                    data_info.setData_list(list);
                }
                num_list_info.setData(data_info);
            }
            return num_list_info;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析 没有id 的json
     *
     * @param str
     * @return
     */
    public static List<String> R_list_noid(String str) {
        List<String> list = new ArrayList<String>();
        try {
            JSONArray json = new JSONArray(str);
            for (int i = 0; i < json.length(); i++) {
                list.add(json.getString(i));
            }
            return list;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param str
     */
    public static List<String> R_shoolname(String str, String i) {
        List<String> list = new ArrayList<String>();

        try {
            JSONObject json = new JSONObject(str);
            JSONArray json1 = json.getJSONArray(i);
            for (int j = 0; j < json1.length(); j++) {
                // JSONObject data=json1.getJSONObject(j);
                list.add(json1.getString(j));
            }
            return list;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public static String getjsonString(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.getString(key);
            }
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }
        return "";
    }

    public static int getjsonInt(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.getInt(key);
            }
        } catch (Exception e) {
            // TODO: handle exception
            return -1;
        }
        return -1;
    }

    public static Float getjsonFloat(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                return Float.parseFloat(jsonObject.getString(key));
            }
        } catch (Exception e) {
            // TODO: handle exception
            return -1.0f;
        }
        return -1.0f;
    }

    public static boolean getjsonBoolean(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.getBoolean(key);
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return false;
    }
}
