package com.xywy.askforexpert.module.main.service.linchuang.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.DownFileItemInfo;
import com.xywy.askforexpert.module.main.service.downFile.AgentConstant;
import com.xywy.askforexpert.module.main.service.downFile.BaseDownFragment;
import com.xywy.askforexpert.module.main.service.downFile.ContentValue;
import com.xywy.askforexpert.module.main.service.downFile.DownloadService;
import com.xywy.askforexpert.widget.view.ProgressWebView;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DbUpdateListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * webview
 *
 * @author 王鹏
 * @2015-5-11上午8:52:07
 */

public class GuideWebViewFragment extends BaseDownFragment implements
        ContentValue {
    private static final String TAG = "GuideWebViewFragment";
    // com.xywy.askforexpert.widget.view.ProgressWebView

    public ProgressWebView webview;
    private String url;
    private String title;
    private String filesize;
    private String fileurl;
    private List<DownFileItemInfo> items = new ArrayList<DownFileItemInfo>();
    private DownFileItemInfo item;

    private FinalDb fb;
    private Class<DownFileItemInfo> clazz;
    private TextView tv_title_guide, tv_filesize, btn_down;

    private List<DownFileItemInfo> file;
    private Handler handler = new Handler();

    private boolean isGo;
    String userid;
    private SQLiteDatabase db;

    private Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    isGo = true;
                    handler.removeCallbacks(runnable);
                    break;
                case 200:
                    if (btn_down != null) {
                        btn_down.setText(msg.obj.toString());
                    }

                    break;
                case 300:
                    isGo = true;
                    handler.removeCallbacks(runnable);
                    if (btn_down != null) {
                        btn_down.setText("打开");
                    }
                    btn_down.setOnClickListener(new MyOnclick(btn_down, 2, msg.obj
                            .toString()));

                    break;
                default:
                    break;
            }

        }
    };

    private Runnable runnable = new Runnable() {

        public void run() {
            file = getMyApp().getDownloadItems();
            Message msg = handler2.obtainMessage();
            if (item == null) {
                for (int i = 0; i < file.size(); i++) {
                    if (file.get(i).getMovieName().trim().equals(title)) {
                        item = file.get(i);
                        break;
                    }

                }
                if (!isGo) {
                    handler.postDelayed(runnable, 500);
                }
            } else {
//            	if(item != null)
//            {
                DLog.i(TAG, "下载状态。。" + item.getDownloadState());
                if (item.getDownloadState() == DOWNLOAD_STATE_DOWNLOADING) {
                    msg.what = 200;
                    msg.obj = item.getPercentage();
                    DLog.i(TAG, "数据下载中。。。" + msg.obj);
                    handler2.sendMessage(msg);
                    if (!isGo) {
                        handler.postDelayed(runnable, 500);
                    }
                } else if (item.getDownloadState() == DOWNLOAD_STATE_SUCCESS) {
                    msg.what = 300;
                    msg.obj = item.getFilePath().toString().trim();
                    DLog.i(TAG, "数据成功。。。" + msg.obj);
                    handler2.sendMessage(msg);
                } else {
                    if (btn_down != null) {
                        btn_down.setText("下载失败");
                    }
                }
            }
            DLog.i(TAG, "下载查询。。。");
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle budle = getArguments();
        url = budle.getString("url");
        title = budle.getString("title").trim().replace("/", "_");
        filesize = budle.getString("filesize");
        fileurl = budle.getString("fileurl");
        clazz = DownFileItemInfo.class;
        fb = FinalDb.create(getActivity().getBaseContext(), "coupon.db", true, 2, new DbUpdateListener() {

            @Override
            public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                DLog.i(TAG, "数据库版本" + arg1 + "新的" + arg2);
                arg0.execSQL("ALTER TABLE downloadtask  ADD commed default '0'");

            }
        });

//		List<DownFileItemInfo> newItems = fb.findAll(clazz);
//		for (int i = 0; i < newItems.size(); i++)
//		{
//			update_2(newItems.get(i));
//		}

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        super.initView();
        // View view = inflater.inflate(R.layout.guide_webview, container,
        // false);
        webview = (ProgressWebView) getActivity().findViewById(R.id.webview);
        tv_title_guide = (TextView) getActivity().findViewById(
                R.id.tv_title_guide);
        tv_filesize = (TextView) getActivity().findViewById(R.id.tv_filesize);
        btn_down = (TextView) getActivity().findViewById(R.id.btn_down);
        // tv_title_guide=(TextView)view.findViewById(R.id.tv_title_guide);
        tv_title_guide.setText(title);
        tv_filesize.setText(filesize);
//		String titles = title.replace("/", "_");

        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getLoginInfo().getData().getPid();
        }
        try {

//            items = fb.findAllByWhere(clazz, "movieName='" + title + "' and userid='" + userid + "' and commed='" + "0" + "'");
            items = fb.findAllByWhere(clazz, "movieName='" + title + "' and commed='" + "0" + "'");
            if (items.size()>0) {
                String fileName = items.get(0).getMovieName();
                String filePath = items.get(0).getFilePath();
                if (!getFile(fileName, filePath)) {
                    items.clear();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (items.size() > 0) {
            DLog.i(TAG, "下载状态" + items.get(0).getDownloadState());
            switch (items.get(0).getDownloadState()) {
                case DOWNLOAD_STATE_DOWNLOADING:
                    btn_down.setText(items.get(0).getPercentage());
                    break;
                case DOWNLOAD_STATE_SUSPEND:
                    btn_down.setText("暂停");
                    break;
                case DOWNLOAD_STATE_WATTING:
                    btn_down.setText("等待");
                    break;
                case DOWNLOAD_STATE_FAIL:
                    btn_down.setText("下载失败");
                    break;
                case DOWNLOAD_STATE_SUCCESS:
                    btn_down.setText("打开");
                    btn_down.setOnClickListener(new MyOnclick(btn_down, 2, items
                            .get(0).getFilePath().toString().trim()));
                    break;
                case DOWNLOAD_STATE_DELETE:
                    btn_down.setText("gg");
                    break;
                case DOWNLOAD_STATE_NONE:
                    btn_down.setText("下载");
                    break;
                case DOWNLOAD_STATE_START:
                    btn_down.setText("下载");
                    break;
                case DOWNLOAD_STATE_EXCLOUDDOWNLOAD:
                    btn_down.setText("等待中");
                    break;

                default:

                    btn_down.setText("下载");
                    btn_down.setOnClickListener(new MyOnclick(btn_down, 1));
                    break;
            }

        } else {

            btn_down.setText("下载");

            btn_down.setOnClickListener(new MyOnclick(btn_down, 1));

        }

        init();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        initView();
        if (getMyApp().getDownloadItems() != null
                && getMyApp().getDownloadItems().size() != 0) {
            // 如果Applaction里面有数据
        } else {
            // 从数据库中加载
            Intent i = getServerIntent();
            i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_LOADITEM);
            getActivity().startService(i);
        }

        handler.postDelayed(runnable, 1500);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guide_webview, container, false);
    }

    class MyOnclick implements OnClickListener {

        // int position;
        TextView tv_text;
        int type;
        String url;

        public MyOnclick(TextView tv_text, int type, String url) {
            // this.position = position;
            this.tv_text = tv_text;
            this.type = type;
            this.url = url;
        }

        public MyOnclick(TextView tv_text, int type) {
            // this.position = position;
            this.tv_text = tv_text;
            this.type = type;
        }

        @Override
        public void onClick(View arg0) {
            StatisticalTools.eventCount(getActivity(), "download");
            if (YMUserService.isGuest()) {
                DialogUtil.LoginDialog_back(new YMOtherUtils(getActivity()).context);
            } else {
                DLog.i(TAG, "下载地址" + fileurl);

                if (type == 1) {
                    //stone
//                    items = fb.findAllByWhere(clazz, "movieName='" + title
//                            + "'  and userid='" + userid + "'  and commed='" + "0" + "'");
                    items = fb.findAllByWhere(clazz, "movieName='" + title
                            + "'  and commed='" + "0" + "'");
                    if (items != null & items.size() > 0) {
                        ToastUtils.shortToast("别戳了！再戳就破啦...");
                    } else {

                        handler.postDelayed(runnable, 100);
                        // tv_text.setText("正在下载");
                        Intent i = new Intent(getActivity(),
                                DownloadService.class);
                        DownFileItemInfo d = new DownFileItemInfo();
                        d.setDownloadUrl(fileurl);
                        d.setFileSize(filesize);
//						String titles = title.replace("/", "_");
                        d.setCommed("0");
                        d.setMovieName(title);
                        d.setUserid(userid);
                        d.setDownloadState(DOWNLOAD_STATE_WATTING);
                        i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
                        // i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
                        i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
                        getActivity().startService(i);
                    }

                } else if (type == 2) {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(getActivity(),
                                MuPDFActivity.class);
                        intent.setAction(Intent.ACTION_VIEW);
                        // intent.putExtra("path",
                        // items.get(arg2).getFilePath().toString().trim());
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (Exception e) {
                        // TODO: handle exception

                        DLog.i(TAG, "PDF打开失败原因 " + e);
                        ToastUtils.shortToast("pdf打开失败");
                    }

                }
            }

        }

    }

    private void init() {
        webview.getSettings().setJavaScriptEnabled(true);
        WebViewUtils2.safeEnhance(webview);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        // webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        // webview.setWebChromeClient(new ChromewebViewClient());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, "Yimai-Request=" + AppUtils.getAPPInfo());
        webview.loadUrl(url);
        webview.setWebViewClient(new HelloWebViewClient());
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isGo = true;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            webview.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            // TODO Auto-generated method stub
            super.onReceivedSslError(view, handler, error);
        }
    }

//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
//	{
//		if (newVersion == 2)
//		{
//			db.execSQL("ALTER downloadtask info ADD commed default '0'");
//			List<DownFileItemInfo> newItems = fb.findAll(clazz);
//			for (int i = 0; i < newItems.size(); i++)
//			{
//				update_2(newItems.get(i));
//			}
//
//		}
//
//		DLog.i(TAG,"数据库版本" + oldVersion + "新的" + newVersion);
//
//	}

    public void update_2(DownFileItemInfo sdmi) {
        String sql = "UPDATE " + AgentConstant.DOWNLOADTASK_TABLE + " SET "
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

    public boolean getFile(String title, String url) {
        try {
            File f = new File(url);
            if (!f.exists()) {
                DLog.i(TAG, "文件地址没有");
                fb.deleteByWhere(clazz, "movieName='" + title + "'");
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            DLog.i(TAG, "数据操作错误日志" + e);
            return false;
        }
        return true;
    }

}
