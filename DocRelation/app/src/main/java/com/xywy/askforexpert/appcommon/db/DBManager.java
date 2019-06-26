package com.xywy.askforexpert.appcommon.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.ChannelItem;
import com.xywy.askforexpert.model.topics.MoreTopicItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理类
 *
 * @author SHR
 * @2015-5-5上午9:58:26
 */
public final class DBManager {
    public static final String ID = "id";
    public static final String TOPIC_Id = "topicId";
    public static final String TOPIC_NAME = "topicName";
    public static final String HEADER = "header";
    public static DBManager dbManager;
    static List<ChannelItem> defaultOtherChannels;
    private static List<ChannelItem> defaultUserChannels;
    private static int[] service_pic = {R.drawable.service_img_1,
            R.drawable.service_img_2, R.drawable.service_img_3,
            R.drawable.service_img_4, R.drawable.service_img_5,
            R.drawable.service_img_6, R.drawable.service_img_7};
    private static int[] more_pic = {R.drawable.service_more_1,
            R.drawable.service_more_2, R.drawable.service_more_3,
            R.drawable.service_more_4, R.drawable.service_more_5,
            R.drawable.service_more_6, R.drawable.service_more_7};

    static {
        defaultUserChannels = new ArrayList<ChannelItem>();
        defaultOtherChannels = new ArrayList<ChannelItem>();

        defaultUserChannels.add(new ChannelItem(0, "文献", 0, 1, service_pic[0], more_pic[0], "专业、精准的医学文献查阅平台"));
        defaultUserChannels.add(new ChannelItem(1, "招聘中心", 1, 1, service_pic[1], more_pic[1], "专注提供医疗行业好工作"));
        defaultUserChannels.add(new ChannelItem(2, "医疗资讯", 2, 1, service_pic[2], more_pic[2], "热点资讯前沿时政,一站式精选"));
        defaultUserChannels.add(new ChannelItem(3, "临床指南", 3, 1, service_pic[5], more_pic[5], "诊疗规范先知道 临床应用效果好"));
        defaultUserChannels.add(new ChannelItem(4, "药典", 4, 1, service_pic[3], more_pic[3], "药典在手 用药不愁"));
        defaultUserChannels.add(new ChannelItem(5, "检查手册", 5, 1, service_pic[4], more_pic[4], "参数解读不用慌，检查手册帮你忙"));
        defaultUserChannels.add(new ChannelItem(6, "医学视频", 6, 1, service_pic[6], more_pic[6], "最新、最全的医学视频在线观看"));
    }

    private DBOpenHelper openHelper;
    private boolean userExist;
    private String userId = "";

    private DBManager() {
        if (openHelper == null) {
            openHelper = new DBOpenHelper(YMApplication.getAppContext());
        }


    }

    /**
     * 初始化频道管理类
     *
     * @throws SQLException
     */
    public static synchronized DBManager getManager() throws SQLException {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public List<ChannelItem> getUserChannelData() {
        // 先从数据库查看，没有获取本地
        List<ChannelItem> channelList = getSelectListShow();
        if (channelList.size() <= 0) {
            initDefaultChannel();
            return defaultUserChannels;
        } else {
            return channelList;
        }

    }

    /**
     * 初始化数据库内的频道数据
     */
    private void initDefaultChannel() {
        saveUserChannelData(defaultUserChannels);
    }

    /**
     * 保存用户数据到数据库
     *
     * @return
     */
    public void saveUserChannelData(List<ChannelItem> userList) {

        for (int i = 0; i < userList.size(); i++) {
            ChannelItem item = userList.get(i);
            addItem(item);
        }

    }

    public boolean addItem(ChannelItem item) {
        boolean flag = false;
        long id;
        SQLiteDatabase db = openHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("id", item.getId());
            values.put("name", item.getName());
            values.put("orderId", item.getOrderId());
            values.put("selected", item.getSelected());
            values.put("imgPath", item.getImgPath());
            values.put("morePath", item.getMorePath());
            values.put("description", item.getDescription());
            id = db.insert(DBOpenHelper.TABLE_SERVICE, null, values);
            flag = (id != -1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return flag;
    }

    /**
     * 更新排序之后的位置
     *
     * @param id
     * @return
     */
    public boolean updateItemOrder(int id, int selected) {
        boolean result = false;
        SQLiteDatabase db = openHelper.getWritableDatabase();
        try {
            db.execSQL("update " + DBOpenHelper.TABLE_SERVICE
                            + " set selected=? where id=?",
                    new Object[]{selected, id});
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    /**
     * 获取选中的服务 selected = 1
     *
     * @return
     */
    public synchronized List<ChannelItem> getSelectListShow() {
        List<ChannelItem> list = new ArrayList<ChannelItem>();
        try {
            SQLiteDatabase db = openHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(
                    "select id,name,orderId,selected,imgPath from "
                            + DBOpenHelper.TABLE_SERVICE
                            + " where selected=1 order by orderId asc", null);
            while (cursor.moveToNext()) {
                ChannelItem item = new ChannelItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("id")));
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                item.setImgPath(cursor.getInt(cursor.getColumnIndex("imgPath")));
                item.setOrderId(cursor.getInt(cursor.getColumnIndex("orderId")));
                item.setSelected(cursor.getInt(cursor.getColumnIndex("selected")));
                list.add(item);
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * 获取选中的服务 selected = 1
     *
     * @return
     */
    public synchronized List<ChannelItem> getSelectMoreListShow() {
        List<ChannelItem> list = new ArrayList<ChannelItem>();
        try {
            SQLiteDatabase db = openHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(
                    "select id,name,orderId,selected,morePath,description from "
                            + DBOpenHelper.TABLE_SERVICE
                            + " where selected=1 order by orderId asc", null);
            while (cursor.moveToNext()) {
                ChannelItem item = new ChannelItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("id")));
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                item.setMorePath(cursor.getInt(cursor.getColumnIndex("morePath")));
                item.setOrderId(cursor.getInt(cursor.getColumnIndex("orderId")));
                item.setSelected(cursor.getInt(cursor
                        .getColumnIndex("selected")));
                item.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                list.add(item);
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public synchronized void clearTableData(String tableName) {
        try {
            SQLiteDatabase db = openHelper.getWritableDatabase();
            if (tabIsExist(db, tableName)) {
                String sql = "DELETE FROM " + tableName;
                db.execSQL(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取topic历史记录
     *
     * @return
     */
    public synchronized List<MoreTopicItem.ListEntity> getTopicData(String tableName) {
        List<MoreTopicItem.ListEntity> list = new ArrayList<>();
        try {
            SQLiteDatabase db = openHelper.getReadableDatabase();
            if (!tabIsExist(db, tableName)) {
                DLog.i("db", "创建话题历史记录表" + tableName);
                db.execSQL("create table if not exists " + tableName + "(" + ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TOPIC_Id + " INTEGER , "
                        + HEADER + " varchar(500) , " + TOPIC_NAME
                        + " varchar(500))");
            }
            Cursor cursor = db.rawQuery(
                    "select * from "
                            + tableName
                            + " order by id asc", null);
            while (cursor.moveToNext()) {
                MoreTopicItem.ListEntity item = new MoreTopicItem.ListEntity();
                item.setId(cursor.getInt(cursor.getColumnIndex("id")));
                item.setTheme(cursor.getString(cursor.getColumnIndex("topicName")));
                item.setId(cursor.getInt(cursor.getColumnIndex("topicId")));
                item.setHeader(cursor.getString(cursor.getColumnIndex("header")));
                list.add(item);
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * 添加topic 历史记录
     *
     * @param item
     * @return
     */
    public boolean addTopicData(MoreTopicItem.ListEntity item, String tableName) {
        // TODO Auto-generated method stub
        boolean flag = false;
        long id;
        SQLiteDatabase db = openHelper.getWritableDatabase();
        try {
            if (!tabIsExist(db, tableName)) {
                DLog.i("db", "创建话题历史记录表" + tableName);
                db.execSQL("create table if not exists " + tableName + "(" + ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TOPIC_Id + " INTEGER , "
                        + HEADER + " varchar(500) , " + TOPIC_NAME
                        + " varchar(500))");
            }
            ContentValues values = new ContentValues();
            values.put("topicName", item.getTheme());
            values.put("topicId", item.getId());
            values.put("header", item.getHeader());
            id = db.insert(tableName, null, values);
            flag = (id != -1);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return flag;
    }

    private void createTopicHisTab() {

        //
        DLog.i("db", "创建最近使用表rec_" + userId);
//        db.execSQL("create table if not exists " + "rec_" + userId + "(" + ID
//                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +TOPIC_Id+ " INTEGER , "
//                + HEADER + " varchar(500) , "+ TOPIC_NAME
//                + " varchar(500))");
    }

    /**
     * 判断某张表是否存在
     *
     * @param tabName 表名
     * @return
     */
    public boolean tabIsExist(SQLiteDatabase db, String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='"
                    + tabName + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            cursor = null;
        }
        return result;
    }
}