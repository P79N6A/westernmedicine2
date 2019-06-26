/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xywy.askforexpert.module.message.msgchat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.ImageUtils;
import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.old.BaseActivity;
import com.xywy.askforexpert.appcommon.utils.imageutils.ImageCache;
import com.xywy.askforexpert.appcommon.utils.others.DLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * 下载显示大图
 *
 */
public class ShowBigImage extends BaseActivity {

    private static final String TAG = "ShowBigImage";
    private ProgressDialog pd;
    private PhotoView image;
    private int default_res = R.drawable.default_image;
    private String localFilePath;
    private Bitmap bitmap;
    private boolean isDownloaded;
    private ProgressBar loadLocalPb;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_big_image);
        super.onCreate(savedInstanceState);

        image = (PhotoView) findViewById(R.id.image);
        loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
        default_res = getIntent().getIntExtra("default_image", R.drawable.default_avatar);
        Uri uri = getIntent().getParcelableExtra("uri");
        String remotepath = getIntent().getExtras().getString("remotepath");
        String secret = getIntent().getExtras().getString("secret");
        System.err.println("show big image uri:" + uri + " remotepath:" + remotepath);

        //本地存在，直接显示本地的图片
        if (uri != null && new File(uri.getPath()).exists()) {
            System.err.println("showbigimage file exists. directly show it");
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // int screenWidth = metrics.widthPixels;
            // int screenHeight =metrics.heightPixels;
            bitmap = ImageCache.getInstance().get(uri.getPath());
            if (bitmap == null) {
                LoadLocalBigImgTask task = new LoadLocalBigImgTask(this, uri.getPath(), image, loadLocalPb, ImageUtils.SCALE_IMAGE_WIDTH,
                        ImageUtils.SCALE_IMAGE_HEIGHT);
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    task.execute();
                }
            } else {
                image.setImageBitmap(bitmap);
            }
        } else if (remotepath != null) { //去服务器下载图片
            System.err.println("download remote image");
            Map<String, String> maps = new HashMap<String, String>();
            if (!TextUtils.isEmpty(secret)) {
                maps.put("share-secret", secret);
            }
            downloadImage(remotepath, maps);
        } else {
            image.setImageResource(default_res);
        }

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DLog.i(TAG, "点击了");
                finish();
            }
        });

        image.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                finish();
            }
        });
    }

    /**
     * 通过远程URL，确定下本地下载后的localurl
     * @param remoteUrl
     * @return
     */
    public String getLocalFilePath(String remoteUrl) {
        String localPath;
        if (remoteUrl.contains("/")) {
            localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"
                    + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
        } else {
            localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteUrl;
        }
        return localPath;
    }

	//// TODO: 16/8/17 replace by shijiazi

	/**
	 * download image
	 *
	 * @param remoteFilePath
	 */
	@SuppressLint("NewApi")
	private void downloadImage(final String remoteFilePath, final Map<String, String> headers) {
		String str1 = getResources().getString(R.string.Download_the_pictures);
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage(str1);
		pd.show();
		localFilePath = getLocalFilePath(remoteFilePath);
		File temp = new File(localFilePath);
		final String tempPath = temp.getParent() + "/temp_" + temp.getName();
		final EMCallBack callback = new EMCallBack() {
			public void onSuccess() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						new File(tempPath).renameTo(new File(localFilePath));

						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(metrics);
						int screenWidth = metrics.widthPixels;
						int screenHeight = metrics.heightPixels;

						bitmap = ImageUtils.decodeScaleImage(localFilePath, screenWidth, screenHeight);
						if (bitmap == null) {
							image.setImageResource(default_res);
						} else {
							image.setImageBitmap(bitmap);
							ImageCache.getInstance().put(localFilePath, bitmap);
							isDownloaded = true;
						}
						if (ShowBigImage.this.isFinishing() || ShowBigImage.this.isDestroyed()) {
							return;
						}
						if (pd != null) {
							pd.dismiss();
						}
					}
				});
			}

			public void onError(int error, String msg) {
				EMLog.e(TAG, "offline file transfer error:" + msg);
				File file = new File(tempPath);
				if (file.exists()&&file.isFile()) {
					file.delete();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (ShowBigImage.this.isFinishing() || ShowBigImage.this.isDestroyed()) {
							return;
						}
						image.setImageResource(default_res);
						pd.dismiss();
					}
				});
			}

			public void onProgress(final int progress, String status) {
				EMLog.d(TAG, "Progress: " + progress);
				final String str2 = getResources().getString(R.string.Download_the_pictures_new);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (ShowBigImage.this.isFinishing() || ShowBigImage.this.isDestroyed()) {
							return;
						}
						pd.setMessage(str2 + progress + "%");
					}
				});
			}
		};

		EMClient.getInstance().chatManager().downloadFile(remoteFilePath, tempPath, headers, callback);

	}

	@Override
	public void onBackPressed() {
		if (isDownloaded)
			setResult(RESULT_OK);
		finish();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
