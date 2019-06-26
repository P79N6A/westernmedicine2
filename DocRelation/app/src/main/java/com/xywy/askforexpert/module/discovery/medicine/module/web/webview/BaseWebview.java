package com.xywy.askforexpert.module.discovery.medicine.module.web.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.util.AppUtils;
import com.xywy.util.ContextUtil;
import com.xywy.util.MatcherUtils;

import java.io.File;
import java.lang.ref.WeakReference;


/**
 * 封装的webview控件
 */
public class BaseWebview extends FrameLayout {

    public static final int FILECHOOSER_REQUESTCODE = 20012;

    private WebSettings webSettings;
    private XYWebview webview;
    private LayoutInflater inflater;
    private WebChromeClient mWebChromeClient;
    private WebViewClient mWebViewClient;

    //用于上传图片
    private String mCameraFilePath = null;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mFilePathCallbackArray;

    private Activity mActivity;

    //用于跳转和状态监听
    private WeakReference<BaseWebviewListerner> listerner;

    //得到webviewTitle
    private String webTitle ;

    public String getWebTitle(){
        return webTitle;
    }


    public BaseWebview(Context context) {
        super(context);
        init(null, 0);
    }

    public BaseWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BaseWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void init() {
        /**
         * 安全漏洞
         * http://nickycc.lofter.com/post/23e2a6_29f4fdf
         */
        webview.removeJavascriptInterface("accessibility");
        webview.removeJavascriptInterface("accessibilityTraversal");
        webview.removeJavascriptInterface("searchBoxJavaBridge_");
    }

    public void setXYWebviewScrollCallback(XYWebview.XYWebviewScrollCallback callback) {
        webview.setXYWebviewScrollCallback(callback);
    }


    public void setWebViewClient(WebViewClient client) {
        mWebViewClient = client;
        if(webview != null) {
            webview.setWebViewClient(client);
        }

        String ua = webview.getSettings().getUserAgentString();
        webview.getSettings().setUserAgentString(ua + "MobileHospital/android/" + AppUtils.getVersionName(getContext()));

    }

    public void setWebChromeClient(WebChromeClient client) {
        mWebChromeClient = client;
        if(webview != null) {
            webview.setWebChromeClient(client);
        }
    }

    private void init(AttributeSet attrs, int defStyle) {
        initWebview();
    }

    public void setListerner(BaseWebviewListerner listerner) {
        if(listerner!=null) {
            this.listerner = new WeakReference<>(listerner);
        }
    }

    //清空webview的方法
    public void clear() {
        if(webview!=null) {
            webview.setWebChromeClient(null);
            webview.setWebViewClient(null);
            webview.destroy();
            webview = null;
        }
    }

    public void loadUrl(String url) {
        if(webview!=null) {
            webview.loadUrl(url);
        }
    }

    public boolean canGoBack() {
        boolean can = false;
        if(webview!=null) {
            can = webview.canGoBack();
        }
        return can;
    }

    public void goBack() {
        if(webview!=null) {
            webview.goBack();
        }
    }

    public void onActivityResult(int resultCode, Intent data, Activity act) {
        Uri[] results = null;
        Uri result = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                if (mCameraFilePath != null) {
                    //if there is not data here, then we may have taken a photo/video
                    File cameraFile = new File(mCameraFilePath);
                    if (cameraFile.exists()) {
                        result = Uri.fromFile(cameraFile);
                        results = new Uri[]{result};
                    }
                }
            } else {
                Cursor cursor = null;
                Uri selectedImage = null;
                try {
                    if (act != null && !act.isFinishing()) {
                        selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        cursor = act.getContentResolver()
                                .query(selectedImage, filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            cursor.close();
                            result = Uri.fromFile(new File(picturePath));
                            results = new Uri[]{result};
                        }else{
                            results = new Uri[]{selectedImage};
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    results = new Uri[]{selectedImage};
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            if (mFilePathCallbackArray != null) {
                mFilePathCallbackArray.onReceiveValue(results);
                mFilePathCallbackArray = null;
            } else if (mUploadMessage != null) {
                if( result != null){
                    mUploadMessage.onReceiveValue(result);

                }else if(results != null && results.length>0){
                    mUploadMessage.onReceiveValue(results[0]);
                }

                mUploadMessage = null;
            }
        } else {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
            if (mFilePathCallbackArray != null) {
                mFilePathCallbackArray.onReceiveValue(null);
                mFilePathCallbackArray = null;
            }
        }
    }

    private void initWebview() {
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_base_webview, this, true);
        setFocusable(false);

        webview = (XYWebview) findViewById(R.id.webview);

        webSettings = webview.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(false);
        webview.setDownloadListener(new WebViewDownLoadListener(getContext()));
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        //启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);

        webSettings.setDomStorageEnabled(true);
        //Needed for file upload feature
        if(mWebChromeClient != null) {
            webview.setWebChromeClient(mWebChromeClient);
        } else {
            webview.setWebChromeClient(new BaseChromeClient());
        }

        if(mWebViewClient != null) {
            webview.setWebViewClient(mWebViewClient);
        } else {
            webview.setWebViewClient(new BaseWebViewClient());
        }
        webview.requestFocus(View.FOCUS_DOWN);
        webview.requestFocusFromTouch();
    }

    private void showAttachmentDialog(ValueCallback filePathCallback) {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
        }
        mUploadMessage = filePathCallback;
        Intent takePictureIntent = createCameraIntent();
        showChooserDialog(takePictureIntent);
    }

    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File externalDataDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        System.out.println("externalDataDir:" + externalDataDir);
        File cameraDataDir = new File(externalDataDir.getAbsolutePath()
                + File.separator + "Camera");
        cameraDataDir.mkdirs();
        mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator
                + System.currentTimeMillis() + ".jpg";
        System.out.println("mcamerafilepath:" + mCameraFilePath);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(mCameraFilePath)));

        return cameraIntent;
    }

     //For lollypop
    private boolean showChooserDialog(Intent intent) {
        Activity act = ContextUtil.currentActivity();
        if(act==null || act.isFinishing()) {
            return false;
        }
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent[] intentArray;
        if (intent != null) {
            intentArray = new Intent[]{intent};
        } else {
            intentArray = new Intent[0];
        }
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        act.startActivityForResult(chooserIntent, FILECHOOSER_REQUESTCODE);
        return true;
    }


    public class WebViewDownLoadListener implements DownloadListener {
        private WeakReference<Context> context;

        public WebViewDownLoadListener(Context context) {
            this.context = new WeakReference<>(context);
        }

        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.get().startActivity(intent);
        }

    }


    public class BaseWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                getContext().startActivity(intent);
                return true;
            }
            if (!MatcherUtils.isUrl(url)) {
                return true;
            } else {

                if(listerner!=null && listerner.get()!=null) {
                    return listerner.get().onUrlChanged(url);
                }
            }
            return false;
        }

        private boolean loadFailed = false;
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(!loadFailed && listerner!=null && listerner.get()!=null) {
                listerner.get().onStatusChanged(WebviewStatus.WebviewLoadSucessed, url);
            }
            webview.requestFocus(View.FOCUS_DOWN);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loadFailed = false;
            if (!url.equals("about:blank")) {
                if(listerner!=null && listerner.get()!=null) {
                    listerner.get().onStatusChanged(WebviewStatus.WebviewDonloading, url);
                }
            }
            webview.requestFocus(View.FOCUS_DOWN);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            loadFailed = true;
            Toast.makeText(getContext(), "无网络", Toast.LENGTH_SHORT);
            if(listerner!=null && listerner.get()!=null) {
                listerner.get().onStatusChanged(WebviewStatus.WebviewLoadFailed, failingUrl);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

    }

    public class BaseChromeClient extends WebChromeClient {

        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        // file upload callback (Android 2.2 (API level 8) -- Android 2.3 (API level 10)) (hidden method)
        public void openFileChooser(ValueCallback<Uri> filePathCallback) {
            showAttachmentDialog(filePathCallback);
        }

        // file upload callback (Android 3.0 (API level 11) -- Android 4.0 (API level 15)) (hidden method)
        public void openFileChooser(ValueCallback filePathCallback, String acceptType) {
            showAttachmentDialog(filePathCallback);
        }

        // file upload callback (Android 4.1 (API level 16) -- Android 4.3 (API level 18)) (hidden method)
        public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType, String capture) {
            showAttachmentDialog(filePathCallback);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            webTitle = title;
        }

         //file upload callback (Android 5.0 (API level 21) -- current) (public method)
         //for Lollipop, all in one
        @Override
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallbackArray != null) {
                mFilePathCallbackArray.onReceiveValue(null);
            }
            mFilePathCallbackArray = filePathCallback;
            // Set up the take picture intent
            Intent takePictureIntent = createCameraIntent();
            return showChooserDialog(takePictureIntent);
        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {


            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }



    public void setActivity(Activity activity) {
        mActivity = activity;
    }
}
