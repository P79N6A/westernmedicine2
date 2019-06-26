package com.xywy.askforexpert.appcommon.net.retrofitWrapper;

import java.util.List;

/**
 * Created by aidonglei on 2015/12/18.
 */
public class BookingEntity {


    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String id;
        private String name;
        private String title;
        private String teach;
        private String goodat;
        private String photo;
        private String info;
        private String expertId;
        private String answernum;
        private String is_ask;
        private String is_plus;
        private String hospital;
        private String depart;
        private String plusNum;
        private ScheduleEntity schedule;
        private String plus_require;
        private String address;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTeach() {
            return teach;
        }

        public void setTeach(String teach) {
            this.teach = teach;
        }

        public String getGoodat() {
            return goodat;
        }

        public void setGoodat(String goodat) {
            this.goodat = goodat;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getExpertId() {
            return expertId;
        }

        public void setExpertId(String expertId) {
            this.expertId = expertId;
        }

        public String getAnswernum() {
            return answernum;
        }

        public void setAnswernum(String answernum) {
            this.answernum = answernum;
        }

        public String getIs_ask() {
            return is_ask;
        }

        public void setIs_ask(String is_ask) {
            this.is_ask = is_ask;
        }

        public String getIs_plus() {
            return is_plus;
        }

        public void setIs_plus(String is_plus) {
            this.is_plus = is_plus;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getDepart() {
            return depart;
        }

        public void setDepart(String depart) {
            this.depart = depart;
        }

        public String getPlusNum() {
            return plusNum;
        }

        public void setPlusNum(String plusNum) {
            this.plusNum = plusNum;
        }

        public ScheduleEntity getSchedule() {
            return schedule;
        }

        public void setSchedule(ScheduleEntity schedule) {
            this.schedule = schedule;
        }

        public String getPlus_require() {
            return plus_require;
        }

        public void setPlus_require(String plus_require) {
            this.plus_require = plus_require;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public static class ScheduleEntity {


            private NumEntity num;


            private List<RdtimeEntity> rdtime;

            public NumEntity getNum() {
                return num;
            }

            public void setNum(NumEntity num) {
                this.num = num;
            }

            public List<RdtimeEntity> getRdtime() {
                return rdtime;
            }

            public void setRdtime(List<RdtimeEntity> rdtime) {
                this.rdtime = rdtime;
            }

            public static class NumEntity {
                private String total;
                private String res_num;

                public String getTotal() {
                    return total;
                }

                public void setTotal(String total) {
                    this.total = total;
                }

                public String getRes_num() {
                    return res_num;
                }

                public void setRes_num(String res_num) {
                    this.res_num = res_num;
                }
            }

            public static class RdtimeEntity {
                private String title;
                private String id;
                private String date;
                private String week;
                private String halfday;
                private String type;
                private String state;
                private String money;
                private String address;
                private String msg;
                private String already_num;
                private String amount;
                private String surplus;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getHalfday() {
                    return halfday;
                }

                public void setHalfday(String halfday) {
                    this.halfday = halfday;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getState() {
                    return state;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public String getMoney() {
                    return money;
                }

                public void setMoney(String money) {
                    this.money = money;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getMsg() {
                    return msg;
                }

                public void setMsg(String msg) {
                    this.msg = msg;
                }

                public String getAlready_num() {
                    return already_num;
                }

                public void setAlready_num(String already_num) {
                    this.already_num = already_num;
                }

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }

                public String getSurplus() {
                    return surplus;
                }

                public void setSurplus(String surplus) {
                    this.surplus = surplus;
                }
            }
        }
    }
}
