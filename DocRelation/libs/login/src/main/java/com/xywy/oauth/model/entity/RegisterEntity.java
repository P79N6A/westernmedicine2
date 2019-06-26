package com.xywy.oauth.model.entity;

/**
 * Created by aidonglei on 2015/12/10.
 */
public class RegisterEntity {
    private int code;
    private String msg;
    private DataEntity data;

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

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private int result;
        private String username;
        private String passwd;
        private String newpasswd;
        private int type;
        private String action;
        private int points;
        private String photo;
        private String userphone;
        private String usersource;
        private String msg;
        private String nickname;
        private String id;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }

        public String getNewpasswd() {
            return newpasswd;
        }

        public void setNewpasswd(String newpasswd) {
            this.newpasswd = newpasswd;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUserphone() {
            return userphone;
        }

        public void setUserphone(String userphone) {
            this.userphone = userphone;
        }

        public String getUsersource() {
            return usersource;
        }

        public void setUsersource(String usersource) {
            this.usersource = usersource;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
