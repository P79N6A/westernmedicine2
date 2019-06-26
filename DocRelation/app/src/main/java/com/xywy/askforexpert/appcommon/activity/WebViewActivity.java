package com.xywy.askforexpert.appcommon.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.interfaces.listener.MyWebViewDownLoadListener;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PNDialogListener;
import com.xywy.askforexpert.module.main.service.que.model.MedicineBean;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


/**
 * 作者：bailiangjin  bailiangjin@gmail.com
 * 创建时间：15/12/13 01:00
 */
public class WebViewActivity extends YMBaseActivity {

    private WebView webView;
    private static final String WEB_URL_KEY = "activity_url";
    private static final String TITLE_KEY = "title";
    private static final String COOKIE_KEY = "cookie";


    private boolean isBackToSourceDialogShow = false;


    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;

    /**
     * request_code
     */
    public final static int FILECHOOSER_REQUESTCODE = 10011;

    private String title;
    private String webUrl;
    private String cookie;


    public static void start(Activity activity, String title, String webUrl) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(WEB_URL_KEY, webUrl);
        intent.putExtra(TITLE_KEY, title);
        activity.startActivity(intent);

    }

    public static void start(Activity activity, String title, String webUrl,String cookie) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(WEB_URL_KEY, webUrl);
        intent.putExtra(COOKIE_KEY, cookie);
        activity.startActivity(intent);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void beforeViewBind() {
        title = getIntent().getStringExtra(TITLE_KEY);
        try {
            webUrl = new String(getIntent().getStringExtra(WEB_URL_KEY).getBytes(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cookie = getIntent().getStringExtra(COOKIE_KEY);
    }

    @Override
    protected void initView() {
        View jsdhTitle = findViewById(R.id.jsdh_title);
        TextView title_name = (TextView) findViewById(R.id.title_name);
        RelativeLayout Lback = (RelativeLayout) findViewById(R.id.Lback);
        ImageView close_iv = (ImageView) findViewById(R.id.close_iv);
        if (TextUtils.isEmpty(cookie)){
            jsdhTitle.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
        }else{
            jsdhTitle.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            title_name.setText(TextUtils.isEmpty(title) ? "网页" : title);
            Lback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWebViewBack();
                }
            });
            close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        titleBarBuilder.setTitleText(TextUtils.isEmpty(title) ? "网页" : title);

        if (TextUtils.isEmpty(webUrl)) {
            shortToast("无效链接 请返回");
            return;
        }
//        titleBarBuilder.addItem("关闭", new ItemClickListener() {
//            @Override
//            public void onClick() {
//                if(isBackToSourceDialogShow){
//                    showBackToSourceDialog();
//                }else {
//                    finish();
//                }
//
//            }
//        }).build();

        titleBarBuilder.setBackIconClickEvent(new ItemClickListener() {
            @Override
            public void onClick() {
                onWebViewBack();
            }
        });


        webView = (WebView) findViewById(R.id.webView);


        WebSettings webSettings = webView.getSettings();

        webSettings.setDomStorageEnabled(true);
        // 设置支持javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        //启动缓存
//        webSettings.setAppCacheEnabled(false);
        // 设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //使用自定义的WebViewClient

        // Use WideViewport and Zoom out if there is no viewport defined
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // Enable pinch to zoom without the zoom buttons
        webSettings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            webSettings.setDisplayZoomControls(false);
        }
        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setWebViewClient(new WebViewClient() {

            //覆盖shouldOverrideUrlLoading 方法

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrlLoading(view,request.getUrl().toString());
//                if (request.getUrl().toString().startsWith("yimaiapp://goback")) {
//                    WebViewActivity.this.finish();
//                    return true;
//                }
//
//
//                String[] paramArray = parseMedicienUrl(request.getUrl().toString());
//
//                if (null != paramArray) {
//                    if (paramArray.length < 5) {
//                        shortToast("选择药品跳转Url不完整");
//                        LogUtils.e("选择药品跳转Url不完整:" + request.getUrl().toString());
//                    } else {
//                        String medicinName = "";
//                        if (!TextUtils.isEmpty(paramArray[4])) {
//                            try {
//                                //解决药品名中文乱码问题
//                                medicinName = URLDecoder.decode(paramArray[4], "UTF-8");
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//
//                        // 发送 已选择药品 event bus 事件
//                        YmRxBus.notifySelectMedicineSuccess(new MedicineBean(paramArray[0], medicinName, paramArray[3], paramArray[1], paramArray[2]));
//                        finish();
//                    }
//                    return true;
//                }
//                addCookie(request.getUrl().toString());
//                if (request.isRedirect()) {
//                    webView.loadUrl(request.getUrl().toString());
//                    return true;
//                }
//                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("yimaiapp://goback")) {
                    WebViewActivity.this.finish();
                    return true;
                }


                String[] paramArray = parseMedicienUrl(url);

                if (null != paramArray) {
                    if (paramArray.length < 5) {
                        shortToast("选择药品跳转Url不完整");
                        LogUtils.e("选择药品跳转Url不完整:" + url);
                    } else {
                        String medicinName = "";
                        if (!TextUtils.isEmpty(paramArray[4])) {
                            try {
                                //解决药品名中文乱码问题
                                medicinName = URLDecoder.decode(paramArray[4], "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }


                        // 发送 已选择药品 event bus 事件
                        YmRxBus.notifySelectMedicineSuccess(new MedicineBean(paramArray[0], medicinName, paramArray[3], paramArray[1], paramArray[2]));
                        finish();
                    }
                    return true;
                }
                addCookie(url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //打印加载完成时的Cookie
                CookieManager cookieManager = CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);
                LogUtils.d("Cookies = " + CookieStr);
            }

        });

        webView.setDownloadListener(new MyWebViewDownLoadListener());

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                take();
                return true;
            }


            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                take();
            }
        });
        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(ua + "/XYWY_YIMAI/android/" + AppUtils.getVersionName(this));
        addCookie(webUrl);

        webView.loadUrl(webUrl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onResume();
        }
    }

    private void addCookie(String url) {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            cookieManager.setAcceptThirdPartyCookies(webView,true);
        } else {
            cookieManager.setAcceptCookie(true);
        }

        if (!TextUtils.isEmpty(cookie)){
            try {
                String cookieData = new String(cookie.getBytes(),"utf-8");
                String keyData = new String("doc_msg=".getBytes(),"utf-8");
                cookieManager.setCookie(".d.jisudianhua.xywy.com", "c_uid=" + YMUserService.getCurUserId());
                cookieManager.setCookie(".d.jisudianhua.xywy.com", "cookie_user=" + YMUserService.getCurUserId());
                cookieManager.setCookie(".d.jisudianhua.xywy.com","doc_msg="+cookieData+";path=/;domain=.xywy.com");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        cookieManager.setCookie(webUrl, "Yimai-Request=" + AppUtils.getAPPInfo());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }else{
            CookieSyncManager.getInstance().sync();
        }
        LogUtils.d("Yimai-Request=" + AppUtils.getAPPInfo());
        LogUtils.d("contentUrl = " + webUrl);
        LogUtils.e("Yimai-Request-cookie:" + cookieManager.getCookie(url));
        LogUtils.e("mCookies" + cookieManager.getCookie(url));
    }

    /**
     * 解析选择药品Url 获取参数 productInfo:id  | 3gURL | pcURL | IMAGE | NAME
     *
     * @param url
     * @return
     */
    private static String[] parseMedicienUrl(String url) {
        if (null != url && url.startsWith("productinfo")) {
            url = url.replace("productinfo://", "");
            String[] paramArray = url.split("\\|");

            for (String item : paramArray) {
                System.out.println(item);
            }
            return paramArray;
        }

        return null;
    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return onWebViewBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * WebView 返回事件
     *
     * @return
     */
    private boolean onWebViewBack() {
        if (webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            LogUtils.e("返回上一页");
            //shortToast("返回上一页");
            return true;
        } else {
            if (isBackToSourceDialogShow) {
                showBackToSourceDialog();
            } else {
                finish();
            }
            return true;
        }
    }

    private void showBackToSourceDialog() {

        new XywyPNDialog.Builder().setContent("确定关闭当前页面？").create(WebViewActivity.this, new PNDialogListener() {
            @Override
            public void onPositive() {
                LogUtils.e("返回源页面");
                //shortToast("返回源页面");
                //关闭当前Activity
                WebViewActivity.this.finish();
            }

            @Override
            public void onNegative() {

            }
        }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {

                if (result != null) {
                    String path = getPath(getApplicationContext(),
                            result);
                    Uri uri = Uri.fromFile(new File(path));
                    mUploadMessage
                            .onReceiveValue(uri);
                } else {
                    mUploadMessage.onReceiveValue(imageUri);
                }
                mUploadMessage = null;


            }
        }
    }


    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.BASE)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;

        if (resultCode == Activity.RESULT_OK) {

            if (data == null) {

                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }


    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        WebViewActivity.this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}
