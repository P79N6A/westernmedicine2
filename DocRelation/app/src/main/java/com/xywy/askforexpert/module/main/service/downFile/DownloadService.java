package com.xywy.askforexpert.module.main.service.downFile;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.DownFileItemInfo;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DbUpdateListener;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 驻留后台的服务
 *
 * @author 王鹏
 * @2015-6-5下午4:27:06
 */
public class DownloadService extends BaseDownService {
    protected static final String TAG = "DownloadService";
    private String toPath;
    private YMApplication m;
    private FinalDb fb;
    private Class<DownFileItemInfo> clazz;
    private SQLiteDatabase db;
    private List<DownFileItemInfo> items = new ArrayList<DownFileItemInfo>(); // 当前所有的任务
    private List<DownFileItemInfo> newitems = new ArrayList<DownFileItemInfo>(); // 当前所有的任务

    private Map<Long, DownFileItemInfo> currentDownloadItems = new HashMap<Long, DownFileItemInfo>();
    private String ppid;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            // // 将下载池更新
            // for (int i = 0; i < items.size(); i++)
            // {
            // String filecommd = items.get(i).getCommed();
            // int downstate = items.get(i).getDownloadState();
            // String uid = items.get(i).getUserid();
            // if (downstate == 6
            // | !uid.equals(ppid))
            // {
            // items.remove(i);
            // }
            // }
            m.setDownloadItems(items);
            m.setNewDownloadItems(items);
            handler.postDelayed(this, 100);
        }

    };

    @Override
    public void onDestroy() {
        // 服务结束的时候结束定时器
        handler.removeCallbacks(runnable);
        DLog.i(TAG, "服务停止");
        db.close();
        super.onDestroy();
    }

    /**
     */
    @Override
    public void onCreate() {
        super.onCreate();
        m = (YMApplication) getApplication();
        // fb = FinalDb.create(getBaseContext(), "coupon.db", true, 2, this);
        fb = FinalDb.create(getBaseContext(), "coupon.db", true, 2,
                new DbUpdateListener() {

                    @Override
                    public void onUpgrade(SQLiteDatabase arg0, int arg1,
                                          int arg2) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "数据库版本" + arg1 + "新的" + arg2);
                        arg0.execSQL("ALTER TABLE downloadtask ADD commed default '0'");
                        arg0.close();
                    }
                });

        clazz = DownFileItemInfo.class;
        String dbpath = getBaseContext().getDatabasePath("coupon.db")
                .getAbsolutePath();
        db = SQLiteDatabase.openDatabase(dbpath, null, 0);
        DLog.i(TAG, "服务开启成功");

    }

    /**
     * @param intent
     * @param startId
     */
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (YMApplication.getLoginInfo() == null
                || YMApplication.getLoginInfo().getData() == null
                || TextUtils.isEmpty(YMApplication.getLoginInfo().getData()
                .getPid())) {
            return;
        }
        ppid = YMApplication.getLoginInfo().getData().getPid();
        DLog.i(TAG, "服务开始");
        // DLog.i(TAG,"服务"+ppid);
        if (intent != null) {
            int code = intent.getIntExtra(SERVICE_TYPE_NAME, ERROR_CODE);
            // toPath = intent.getStringExtra(CACHE_DIR);
            // if (TextUtils.isEmpty(toPath))
            // {
            // 如果存储路径未设置
            // toPath = "/mnt/sdcard/cloud_coupon/";
            if (TextUtils.isEmpty(ppid)) {
                toPath = Environment.getExternalStorageDirectory()
                        + "/XYWY/XYWY_PDFDOWN/";
            } else {
                toPath = Environment.getExternalStorageDirectory()
                        + "/XYWY/XYWY_PDFDOWN/" + ppid + "/";
            }

            // }
            DLog.i(TAG, "文件地址" + toPath);
            File cacheDir = new File(toPath, "");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                toPath = getBaseContext().getCacheDir().getAbsolutePath();
            }
            switch (code) {
                case START_DOWNLOAD_MOVIE:
                    // 开启下载
                    DownFileItemInfo startdmi = (DownFileItemInfo) intent
                            .getSerializableExtra(DOWNLOAD_TAG_BY_INTENT);
                    // startdmi.setFilePath(toPath);
                    items.add(startdmi);// 讲接到的任务存放到下载池中
                    startDownloadMovie(toPath, startdmi);
                    // 开启一个定时器,每隔1秒钟刷新一次下载进度
                    startTimerUpdateProgress();
                    break;
                case DOWNLOAD_STATE_SUSPEND:
                    // 暂停一个下载任务
                    DownFileItemInfo stopdmi = m.getStopOrStartDownloadfileItem();
                    if (stopdmi != null) {
                        stopDownload(stopdmi, false);
                    }
                    break;
                case DOWNLOAD_STATE_START:
                    // 开始一个正在暂停的下载任务
                    DownFileItemInfo startDmiByPausing = m
                            .getStopOrStartDownloadfileItem();
                    if (startDmiByPausing != null) {
                        startPausingDownload(startDmiByPausing, toPath);
                    }
                    break;
                case DOWNLOAD_STATE_DELETE:
                    DownFileItemInfo delDownload = m
                            .getStopOrStartDownloadfileItem();
                    deleteDownload(delDownload, toPath, false);
                    break;
                case DOWNLOAD_STATE_CLEAR:
                    clearAllDownload();
                    break;
                case START_DOWNLOAD_LOADITEM:
                    // 数据库中装载下载任务
                    if (items == null || items.size() == 0) {
                        // 尝试从数据库中得到items
                        // items = fb.findAll(clazz);
                        items = fb.findAllByWhere(clazz, "downloadState <>'" + 6
                                + "' and userid= '" + ppid + "'");
                        if (items == null) {
                            return;
                        }
                        if (items.size() != 0) {
                            for (int i = 0; i < items.size(); i++) {
                                // 保证不管应用以什么方式终止 重新启动的时候 所有状态都为 暂停
                                if (items.get(i).getDownloadState() != DOWNLOAD_STATE_SUCCESS) {
                                    // 如果当前不是下载完成的状态
                                    items.get(i).setDownloadState(
                                            DOWNLOAD_STATE_SUSPEND);
                                }
                            }
                        }
                        m.setDownloadItems(items);
                        m.setNewDownloadItems(items);
                        // 如果是退出之后
                    }
                    break;
                case START_DOWNLOAD_ALLSUSPEND:
                    // 设置所有下载状态为暂停
                    setAllDownloadTaskSuspend();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: setAllDownloadTaskSuspend
     * @Description:
     */
    private void setAllDownloadTaskSuspend() {
        for (int i = 0; i < items.size(); i++) {
            DownFileItemInfo sdmi = items.get(i);
            sdmi.setDownloadState(DOWNLOAD_STATE_SUSPEND);
            String sql = "UPDATE " + DOWNLOADTASK_TABLE + " SET "
                    + "movieId='" + sdmi.getMovieId() + "',movieName='"
                    + sdmi.getMovieName() + "',fileSize='" + sdmi.getFileSize()
                    + "',currentProgress=" + sdmi.getCurrentProgress()
                    + ",percentage='" + sdmi.getPercentage() + "',filePath='"
                    + sdmi.getFilePath() + "',downloadUrl='"
                    + sdmi.getDownloadUrl() + "',uuid=" + sdmi.getUuid()
                    + ",progressCount=" + sdmi.getProgressCount() + ",commed="
                    + sdmi.getCommed() + ",downloadState="
                    + DOWNLOAD_STATE_WATTING + "' WHERE movieName='"
                    + sdmi.getMovieName() + "'and userid='" + sdmi.getUserid()
                    + "'";

            DLog.i(TAG, sql);
            db.execSQL(sql);
        }
    }

    /**
     * @param dmi 接收一个 下载任务
     * @return void
     * @throws
     * @Title: stopDownload
     * @Description: 暂停指定的下载任务
     */
    private void stopDownload(DownFileItemInfo dmi, boolean isDel) {
        DownFileItemInfo sdmi = currentDownloadItems.get(dmi.getUuid());
        if (sdmi != null) {
            // Toast.makeText(getApplicationContext(),
            // "暂停：" + sdmi.getMovieName(), Toast.LENGTH_SHORT).show();
            ToastUtils.shortToast(
                    "暂停：" + sdmi.getMovieName());
            sdmi.setDownloadState(DOWNLOAD_STATE_SUSPEND);
            // 将下载任务暂停
            if (sdmi.getDownloadFile() != null) {
                sdmi.getDownloadFile().stopDownload();
            }
            // 设置下载任务状态
            currentDownloadItems.remove(sdmi.getUuid());// 将任务从下载队列中移除
            if (!isDel) {
                // 如果不是删除,讲数据库中的状态进行变更
                String sql = "UPDATE " + DOWNLOADTASK_TABLE
                        + " SET " + "movieId='" + sdmi.getMovieId()
                        + "',movieName='" + sdmi.getMovieName()
                        + "',fileSize='" + sdmi.getFileSize()
                        + "',currentProgress=" + sdmi.getCurrentProgress()
                        + ",percentage='" + sdmi.getPercentage()
                        + "',filePath='" + sdmi.getFilePath() + "',commed='"
                        + sdmi.getCommed() + "',downloadUrl='"
                        + sdmi.getDownloadUrl() + "',uuid=" + sdmi.getUuid()
                        + ",progressCount=" + sdmi.getProgressCount()
                        + ",downloadState=" + sdmi.getDownloadState()
                        + " WHERE movieName='" + sdmi.getMovieName()
                        + "'and userid='" + sdmi.getUserid() + "'";
                DLog.i(TAG, sql);
                db.execSQL(sql);
                startDownloadMovie(toPath, null);
            }
            DLog.i(TAG, sdmi.getMovieName() + "：任务暂停："
                    + sdmi.getPercentage() + "状态：" + sdmi.getDownloadState());
            // 继续查找新的任务
        } else {
            DLog.i(TAG, "暂停失败,原因是未知的下载状态");
        }
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: deleteDownload
     * @Description: 删除一个下载任务
     * @author
     */
    private void deleteDownload(DownFileItemInfo dmi, String toPath,
                                boolean allDel) {
        // 停止这个任务
        // stopDownload(dmi , true);
        String delPath = dmi.getFilePath();
        if (TextUtils.isEmpty(delPath)) {
            delPath = toPath + dmi.getMovieName();
        }
        File delFile = new File(delPath);
        if (delFile.delete()) {
            // 删除成功
            // 移除条目
            items.remove(dmi);
            currentDownloadItems.remove(dmi.getUuid());
            // 如果是删除一个任务,此标志作废
            if (!allDel) {
                String sql = "DELETE from " + DOWNLOADTASK_TABLE
                        + " WHERE movieName='" + dmi.getMovieName()
                        + "'and userid='" + dmi.getUserid() + "';";
                db.execSQL(sql);
            }
        } else {
            // 删除失败
            DLog.i(TAG, dmi.getMovieName() + "：删除失败");
            items.remove(dmi);
            String sql = "DELETE from " + DOWNLOADTASK_TABLE
                    + " WHERE movieName='" + dmi.getMovieName()
                    + "'and userid='" + dmi.getUserid() + "';";
            db.execSQL(sql);
        }

    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: clearAllDownload
     * @Description: 清除所有下载任务
     * @author
     */
    private void clearAllDownload() {
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getMovieName();
            String userid = items.get(i).getUserid();
            String sql = "DELETE from " + DOWNLOADTASK_TABLE
                    + " WHERE movieName='" + name + "'and userid='" + userid
                    + "';";
            DLog.i(TAG, sql);
            db.execSQL(sql);
            DLog.i(TAG, "删除：" + name);
            if (items.get(i).getDownloadFile() != null) {
                items.get(i).getDownloadFile().stopDownload();
            }
            if (!TextUtils.isEmpty(items.get(i).getFilePath())) {
                new File(items.get(i).getFilePath()).delete();
            }
        }
        items.clear();
        currentDownloadItems.clear();
        m.getDownloadItems().clear();
    }

    /**
     * @param dmi 接收一个 下载任务
     * @return void
     * @throws
     * @Title: startPausingDownload
     * @Description: 开始一个 当前是暂停状态的任务
     * @author
     */
    private void startPausingDownload(DownFileItemInfo dmi, String toPath) {

        for (int i = 0; i < items.size(); i++) {
            if (dmi.getUuid().equals( items.get(i).getUuid())) {
                items.get(i).setDownloadState(DOWNLOAD_STATE_WATTING);
                // Toast.makeText(getApplicationContext(),
                // dmi.getMovieName() + ":开始下载", Toast.LENGTH_SHORT)
                // .show();
                ToastUtils.shortToast(dmi.getMovieName()
                        + ":开始下载");
                DLog.i(TAG, dmi.getUuid() + "任务开始："
                        + dmi.getCurrentProgress());
                db.update(DOWNLOADTASK_TABLE,
                        setDbValues(items.get(i)), "uuid=?",
                        new String[]{items.get(i).getUuid() + ""});
                // 更改下载状态
                startDownloadMovie(toPath, items.get(i));
            }
        }

    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: startTimerUpdateProgress
     * @Description: 更新下载进度
     * @author
     */
    private void startTimerUpdateProgress() {
        handler.postDelayed(runnable, 100);
    }

    /**
     * @return void
     * @throws
     * @Title: startDownloadMovie
     * @author
     */

    public void startDownloadMovie(final String toPath, DownFileItemInfo dmid) {

        int size = items.size();
        for (int g = 0; g < size; g++) {

            // 如果状态为等待状态
            // 如果当前正在下载的任务 不够三个
            if (items.get(g).getDownloadState() == DOWNLOAD_STATE_WATTING
                    || items.get(g).getDownloadState() == DOWNLOAD_STATE_EXCLOUDDOWNLOAD) {
                if (currentDownloadItems.size() < 3) {
                    // 取得下载任务
                    final DownFileItemInfo dmi = items.get(g);
                    // 构建存储文件路径

                    // File cacheDir = new File(filePath,toPath);
                    // if (!cacheDir.exists())
                    // {
                    // cacheDir.mkdirs();
                    // }
                    DLog.i(TAG, "名字" + dmi.getMovieName());
                    // File f = new File(toPath, dmi.getMovieName() + ".pdf");
                    // File f = new File(toPath, dmi.getMovieName());
                    String newpaht = "";
                    if ("0".equals(dmi.getCommed())) {
                        DLog.i(TAG, "guild");
                        newpaht = toPath + "guid/";
                    } else if ("1".equals(dmi.getCommed())) {
                        DLog.i(TAG, "guild222");

                        newpaht = toPath + "wenxian/";
                    }
                    File f = getFilePath(newpaht, dmi.getMovieName());

                    dmi.setFilePath(f.getAbsolutePath());
                    dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
                    if (dmi.getUuid() == null) {
                        Long id = UUID.randomUUID().getLeastSignificantBits();
                        dmi.setUuid(id);
                    }
                    if (dmi.getUuid() != null) {
                        currentDownloadItems.put(dmi.getUuid(), dmi);// 讲下载任务存入当前的队列中
                    }
                    DownloadFile d = download(dmi.getDownloadUrl(),
                            f.getAbsolutePath(), new AjaxCallBack() {

                                /**
                                 * (非 Javadoc) Title: onStart Description:
                                 *
                                 * @see AjaxCallBack#onStart()
                                 */
                                @Override
                                public void onStart() {
                                    // 如果这个任务在等待,开启这个下载任务
                                    // 讲下载状态设置为 正在下载
                                    // 开启任务
                                    dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
                                    DLog.i(TAG, dmi.getMovieName()
                                            + "--开始下载");
                                    // 为下载任务分配ID号
                                    // 将当前下载的对象以及状态写入到数据库中
                                    // 如果这个对象在数据库中已经存在了
                                    List<DownFileItemInfo> ls = fb.findAllByWhere(clazz, "uuid="
                                            + dmi.getUuid() + "");
                                    if (ls != null && ls.size() != 0) {
                                        for (int i = 0; i < ls.size(); i++) {
                                            if (!dmi.getUuid().equals(ls.get(i).getUuid())
                                                    && !ls.get(i).getMovieName().equals(dmi.getMovieName())) {

                                                // 将这个对象插入到数据库中
                                                db.insert(DOWNLOADTASK_TABLE, null, setDbValues(dmi));
                                            }
                                        }
                                    } else {
                                        db.insert(DOWNLOADTASK_TABLE, null, setDbValues(dmi));
                                    }

                                }

                                // @Override
                                // public boolean isProgress() {
                                // // TODO Auto-generated method stub
                                // return super.isProgress();
                                // }
                                //
                                // @Override
                                // public AjaxCallBack<File> progress(
                                // boolean progress, int rate) {
                                // // TODO Auto-generated method stub
                                // return super.progress(progress, rate);
                                // }

                                /**
                                 * (非 Javadoc) Title: onLoading Description:
                                 *
                                 * @param count
                                 * @param current
                                 * @see AjaxCallBack#onLoading(long, long)
                                 */
                                @Override
                                public void onLoading(long count, long current) {
                                    DLog.i(TAG, "进度" + count + "/"
                                            + current);
                                    DLog.i(TAG, "进度" + count + "/" + current);
                                    // 设置当前的进度
                                    dmi.setProgressCount(count);
                                    dmi.setCurrentProgress(current);
                                    if (count != 0) {
                                        int p = (int) ((current * 100) / count);
                                        dmi.setPercentage(p + "%");
                                    } else {
                                        dmi.setPercentage("0%");
                                    }
                                    dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
                                }

                                /**
                                 * (非 Javadoc) Title: onSuccess Description:
                                 *
                                 * @param t
                                 * @see AjaxCallBack#onSuccess(String)
                                 */
                                @Override
                                public void onSuccess(File t) {
                                    dmi.setDownloadState(DOWNLOAD_STATE_SUCCESS);
                                    DLog.i(TAG, "下载成功："
                                            + t.getAbsolutePath());
                                    currentDownloadItems.remove(dmi.getUuid());// 成功的时候从当前下载队列中去除
                                    // 将下载的状态插入到数据库中
                                    DownFileItemInfo sdmi = dmi;

                                    String sql = "UPDATE "
                                            + DOWNLOADTASK_TABLE
                                            + " SET " + "movieId='"
                                            + sdmi.getMovieId()
                                            + "',movieName='"
                                            + sdmi.getMovieName()
                                            + "',fileSize='"
                                            + sdmi.getFileSize()
                                            + "',currentProgress="
                                            + sdmi.getCurrentProgress()
                                            + ",percentage='"
                                            + sdmi.getPercentage()
                                            + "',filePath='"
                                            + sdmi.getFilePath() + "',commed='"
                                            + sdmi.getCommed()
                                            + "',downloadUrl='"
                                            + sdmi.getDownloadUrl() + "',uuid="
                                            + sdmi.getUuid()
                                            + ",progressCount="
                                            + sdmi.getProgressCount()
                                            + ",downloadState="
                                            + sdmi.getDownloadState()
                                            + " WHERE movieName='"
                                            + sdmi.getMovieName() + "'";
                                    try {
                                        db.execSQL(sql);
                                    }catch (Exception e){
                                        DLog.i(TAG,e.toString());
                                    }

                                    // for(int i=0;i<items)
                                    m.setDownloadItems(items);
                                    items.remove(dmi);
                                    m.setNewDownloadItems(items);
                                    startDownloadMovie(toPath, null);// 继续寻找可以下载的任务

                                }

                                /**
                                 * (非 Javadoc) Title: onFailure Description:
                                 *
                                 * @param t
                                 * @param strMsg
                                 */
                                @Override
                                public void onFailure(Throwable t, int errorNo,
                                                      String strMsg) {
                                    dmi.setDownloadState(DOWNLOAD_STATE_FAIL);
                                    DLog.i(TAG, "下载失败:" + strMsg + ":"
                                            + dmi.getDownloadUrl() + "：\n原因："
                                            + t.getMessage() + "异常信息:"
                                            + t.getLocalizedMessage());
                                    // Toast.makeText(getApplicationContext(),
                                    // dmi.getMovieName() + "：下载失败",
                                    // Toast.LENGTH_SHORT).show();
                                    ToastUtils.shortToast(
                                            dmi.getMovieName() + "：下载失败");
                                    t.printStackTrace();
                                    currentDownloadItems.remove(dmi.getUuid());
                                    SystemClock.sleep(1000);
                                    startDownloadMovie(toPath, null);
                                    // 更新数据库状态
                                    // fb.update(dmi, dmi.getUuid()+"");
                                    DownFileItemInfo sdmi = dmi;
                                    String sql = "UPDATE "
                                            + DOWNLOADTASK_TABLE
                                            + " SET " + "movieId='"
                                            + sdmi.getMovieId()
                                            + "',movieName='"
                                            + sdmi.getMovieName()
                                            + "',fileSize='"
                                            + sdmi.getFileSize()
                                            + "',currentProgress="
                                            + sdmi.getCurrentProgress()
                                            + ",percentage='"
                                            + sdmi.getPercentage()
                                            + "',filePath='"
                                            + sdmi.getFilePath() + "',commed='"
                                            + sdmi.getCommed()
                                            + "',downloadUrl='"
                                            + sdmi.getDownloadUrl() + "',uuid="
                                            + sdmi.getUuid()
                                            + ",progressCount="
                                            + sdmi.getProgressCount()
                                            + ",downloadState="
                                            + sdmi.getDownloadState()
                                            + " WHERE movieName='"
                                            + sdmi.getMovieName() + "'";
                                    db.execSQL(sql);
                                    // Toast.makeText(
                                    // getApplicationContext(),
                                    // sdmi.getMovieName()
                                    // + "下载失败,可能是存储空间不足或者网络环境太差",
                                    // Toast.LENGTH_SHORT).show();
                                    ToastUtils.shortToast(
                                            sdmi.getMovieName()
                                                    + "下载失败,可能是存储空间不足或者网络环境太差或者下载地址有问题");
                                    super.onFailure(t, errorNo, strMsg);
                                }
                            });
                    dmi.setDownloadFile(d);

                } else {
                    // 将下载任务插入到数据库中
                    if (dmid != null) {
                        dmid.setDownloadState(DOWNLOAD_STATE_EXCLOUDDOWNLOAD);
                        db.insert(DOWNLOADTASK_TABLE, null,
                                setDbValues(dmid));
                    }
                }
            }
        }

    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: setDbValues
     * @Description:
     * @author
     */
    private ContentValues setDbValues(DownFileItemInfo dmi) {
        ContentValues values = new ContentValues();
        values.put("id", dmi.getId());
        values.put("editState", dmi.isEditState());
        values.put("movieName", dmi.getMovieName());
        values.put("fileSize", dmi.getFileSize());
        values.put("currentProgress", dmi.getCurrentProgress());
        values.put("isSelected", dmi.isSelected());
        values.put("percentage", dmi.getPercentage());
        values.put("filePath", dmi.getFilePath());
        values.put("downloadUrl", dmi.getDownloadUrl());
        values.put("uuid", dmi.getUuid());
        values.put("progressCount", dmi.getProgressCount());
        values.put("downloadState", dmi.getDownloadState());
        values.put("movieId", dmi.getMovieId());
        values.put("userid", dmi.getUserid());
        values.put("commed", dmi.getCommed());
        return values;

    }

    public static File getFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath, "");
            if (!file.exists()) {
                file.mkdir();
            }

        } catch (Exception e) {

        }
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: download
     * @Description: 开启一个普通的下载任务
     * @author
     */
    private DownloadFile download(String url, String toPath,
                                  AjaxCallBack downCallBack) {
        return new DownloadFile().startDownloadFileByUrl(url, toPath,
                downCallBack);
    }

    // @Override
    // public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    // {
    // if (newVersion == 2)
    // {
    // db.execSQL("ALTER downloadtask info ADD commed default '0'");
    // List<DownFileItemInfo> newItems = fb.findAll(clazz);
    // for (int i = 0; i < newItems.size(); i++)
    // {
    // update_2(newItems.get(i));
    // }
    //
    // }
    //
    // DLog.i(TAG,"数据库版本" + oldVersion + "新的" + newVersion);
    //
    // }

    public void update_2(DownFileItemInfo sdmi) {
        String sql = "UPDATE " + DOWNLOADTASK_TABLE + " SET "
                + "movieId='" + sdmi.getMovieId() + "',movieName='"
                + sdmi.getMovieName() + "',fileSize='" + sdmi.getFileSize()
                + "',currentProgress=" + sdmi.getCurrentProgress()
                + ",percentage='" + sdmi.getPercentage() + "',filePath='"
                + sdmi.getFilePath() + "',commed='" + sdmi.getCommed()
                + "',downloadUrl='" + sdmi.getDownloadUrl() + "',uuid="
                + sdmi.getUuid() + ",progressCount=" + sdmi.getProgressCount()
                + ",downloadState=" + sdmi.getDownloadState();
        DLog.i(TAG, "更新升级" + sql);
        db.execSQL(sql);

    }
}
