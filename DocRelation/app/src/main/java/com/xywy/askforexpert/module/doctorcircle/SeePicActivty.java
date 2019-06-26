package com.xywy.askforexpert.module.doctorcircle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.widget.view.HackyViewPager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 点击动态图片查看图片
 *
 * @author LG
 */
public class SeePicActivty extends AppCompatActivity {

    private List<String> imgs;
    ImageLoader instance;
    DisplayImageOptions options;
    private int curentItem = 1;
    private HackyViewPager viewpager;
    private TextView tv;
    private ImageView iv_delete;
    private MyAdapater myadapater;
    private ImageView iv_back;

    private Map<Integer, View> hsh = new LinkedHashMap<>();
    private PhotoView photoView;

    private SharedPreferences sp;
    private static final String QUEDETAILADAPTER = "QueDetailAdapter";
    private static final String IS_FROM = "isFrom";
    private String mIsFrom;
    private float mAngleOffset = 90f;
    private float mStartAngle = 0f;

    public  static void startActivity(){

    }

    public static void startActivity(Context context, ArrayList<String> pics, int position, @PublishType String type) {
        Intent intent = new Intent(context, SeePicActivty.class);
        intent.putExtra("type", type);
        intent.putExtra("imgs", pics);
        intent.putExtra("curentItem", position + "");
        context.startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().putString("mustUpdata", "").apply();
        setContentView(R.layout.activity_seepic);
        imgs = (ArrayList<String>) getIntent().getSerializableExtra("imgs");
        String delete = getIntent().getStringExtra("delete");
        String curentItem22 = getIntent().getStringExtra("curentItem");
        if(getIntent().hasExtra(IS_FROM)){
            mIsFrom = getIntent().getStringExtra(IS_FROM);
        }
        if (hsh != null && hsh.size() > 0) {
            hsh.clear();
        }

        if (!TextUtils.isEmpty(curentItem22)) {
            curentItem = Integer.parseInt(curentItem22);
        }

        viewpager = (HackyViewPager) findViewById(R.id.viewpager);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        tv = (TextView) findViewById(R.id.tv);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv.setText((curentItem + 1) + "/" + imgs.size());
        instance = ImageLoader.getInstance();
        if (TextUtils.isEmpty(delete)) {
            iv_delete.setVisibility(View.INVISIBLE);
        } else {
            iv_delete.setVisibility(View.VISIBLE);
        }
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.pic_default)
                .showImageOnFail(R.drawable.pic_default)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();

        for (int i = 0; i < imgs.size(); i++) {
            photoView = (PhotoView) View.inflate(SeePicActivty.this, R.layout.pic_item_fullscreen_layout, null).findViewById(R.id.image);
//            photoView = (PhotoView) view.findViewById(R.id.image);
            instance.displayImage(imgs.get(i), photoView, options, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {//加载图片成功后获取 宽高，设置相对应的显示类型
                    if (arg2.getWidth() < 600 && arg2.getHeight() > 600) {// 窄长的图片  放大
                        ((PhotoView) arg1).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else {//正常显示
                        ((PhotoView) arg1).setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }

            });

            // 换成此点击Listener可实现点击图片外返回
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float v, float v1) {
                    if(QUEDETAILADAPTER.equals(mIsFrom)){
                        ObjectAnimator animator = null;
                        if(null == animator){
                            animator = ObjectAnimator.ofFloat(view, "rotation", mStartAngle, mAngleOffset+mStartAngle);
                            animator.setDuration(1000);
                        }
                        mStartAngle += mAngleOffset;
                        if(mStartAngle==360f){
                            mStartAngle = 0f;
                        }
                        animator.start();
                    }else {
                        SeePicActivty.this.finish();
                    }
                }
            });

            final int finalI = i;
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    android.support.v7.app.AlertDialog.Builder builder = new
                            android.support.v7.app.AlertDialog.Builder(SeePicActivty.this);

                    builder.setTitle("保存图片到本地")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DLog.i("PAHT", "url" + imgs.get(finalI));
                                    saveBitmap(imgs.get(finalI));

                                }
                            })
                            .setNegativeButton("取消", null).create().show();

                    return true;
                }
            });

            hsh.put(i, photoView);
        }

        setData();


    }

    @SuppressWarnings("deprecation")
    private void setData() {
        viewpager.setOffscreenPageLimit(imgs.size());
        myadapater = new MyAdapater();
        viewpager.setAdapter(myadapater);

        viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageSelected(int arg0) {
                curentItem = viewpager.getCurrentItem();
                curentItem++;
                tv.setText(curentItem + "/" + imgs.size());
            }
        });
        iv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SeePicActivty.this.finish();
            }
        });
        viewpager.setCurrentItem(curentItem);

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    class MyAdapater extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return imgs == null ? -1 : imgs.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (container instanceof ViewPager) {
                if (object instanceof PhotoView) {
                    container.removeView((PhotoView) object);
                }
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            DLog.d("photo_pos", String.valueOf(position));
            PhotoView photoView = (PhotoView) hsh.get(position);
            if (container instanceof ViewPager) {
                container.addView(photoView);
            }

            return photoView;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACKSLASH) {
            if (imgs != null && imgs.size() > 0) {
                imgs.clear();
            }
            SeePicActivty.this.finish();
            return true;

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        StatisticalTools.onResume(this);
    }


    private void saveBitmap(String url) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            new DownLoadAsyncTask().execute(url);
        }

    }

    class DownLoadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String downState = "fail";
            HttpURLConnection urlConnection = null;
            String requestUrl = params[0];
            Bitmap bitmap;
            InputStream inputStream = null;
            BufferedOutputStream fileOutputStream = null;
            try {
                URL url = new URL(requestUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5 * 1000);
                urlConnection.setRequestMethod("GET");

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = urlConnection.getInputStream();

                    bitmap = BitmapFactory.decodeStream(inputStream);

                    String path = Environment.getExternalStorageDirectory().getPath() + "/DCIM";
                    DLog.i("PAHT", path);
                    File saveFile = new File(path + requestUrl.substring(requestUrl.lastIndexOf("/"), requestUrl.length()));

                    fileOutputStream = new BufferedOutputStream(new FileOutputStream(saveFile));

                    boolean state = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                    if (state) {
                        downState = "success";
                    }
                    fileOutputStream.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {

                        fileOutputStream.close();
                    }
                    if (inputStream != null) {

                        inputStream.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                urlConnection.disconnect();
            }

            return downState;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ("success".equals(s)) {
                Toast.makeText(YMApplication.getAppContext(), "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(YMApplication.getAppContext(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
