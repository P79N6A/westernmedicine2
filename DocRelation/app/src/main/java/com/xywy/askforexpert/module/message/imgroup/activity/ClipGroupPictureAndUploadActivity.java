package com.xywy.askforexpert.module.message.imgroup.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.im.group.UploadGroupImageResponse;
import com.xywy.askforexpert.module.message.imgroup.utils.CommonHttpMultipartPost;
import com.xywy.askforexpert.widget.module.my.ClipView;
import com.xywy.askforexpert.widget.module.my.ClipView.OnDrawListenerComplete;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片裁剪
 * <p>
 * TODO：待重构 待处理
 *
 * @author 王鹏
 * @2015-1-12上午10:55:13
 */
public class ClipGroupPictureAndUploadActivity extends Activity implements OnTouchListener {
    /**
     * 动作标志：无
     */
    private static final int NONE = 0;
    /**
     * 动作标志：拖动
     */
    private static final int DRAG = 1;
    /**
     * 动作标志：缩放
     */
    private static final int ZOOM = 2;
    public int screenWidth = 0;
    public int screenHeight = 0;
    /**
     * 裁剪图片地址
     */
    public String filename = Environment.getExternalStorageDirectory()
            .getPath() + "/clip.png";
    String groupid = "";
    String userid = "";
    private ImageView srcPic;
    /**
     * 保存按钮
     */
    private TextView saveLayout;
    private TextView tv_title;

    private ClipView clipview;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    /**
     * 初始化动作标志
     */
    private int mode = NONE;
    /**
     * 记录起始坐标
     */
    private PointF start = new PointF();
    /**
     * 记录缩放时两指中间点坐标
     */
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private Bitmap bitmap;
    private String mPath;
    private CommonHttpMultipartPost commonHttpMultipartPost;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            UploadGroupImageResponse uploadGroupImageResponse = (UploadGroupImageResponse) msg.obj;
            int code = uploadGroupImageResponse.getCode();
            if (code == 10000) {
                Intent intent = new Intent();
                intent.putExtra(GroupInfoActivity.GROUP_HEAD_URL_INTENT_KEY, uploadGroupImageResponse.getData());
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                finish();
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupid = getIntent().getStringExtra("groupid");
        userid = getIntent().getStringExtra("userid");
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.clip_image);

        // byte[] bis = getIntent().getByteArrayExtra("bitmap");
        // bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        getWindowWH();
        mPath = getIntent().getStringExtra("path");

        if (TextUtils.isEmpty(mPath)) {
            ToastUtils.shortToast("选择的图片无效，请重新选择其他图片");
            finish();
            return;
        }
        File file = new File(mPath);
        if (!file.exists()) {
            ToastUtils.shortToast("选择的图片无效，请重新选择其他图片");
            finish();
            return;
        }
        LogUtils.d("裁剪图片path" + mPath);
        bitmap = createBitmap(mPath, screenWidth, screenHeight);
        if (null == bitmap) {
            ToastUtils.shortToast("无效的图片 请重新选择");
            finish();
            return;
        }
        srcPic = (ImageView) this.findViewById(R.id.src_pic);

        srcPic.setOnTouchListener(this);
        ViewTreeObserver observer = srcPic.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                srcPic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                initClipView(srcPic.getTop(), bitmap);
            }
        });

        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("移动和缩放");
        saveLayout = (TextView) this.findViewById(R.id.btn22);
        saveLayout.setText("完成");
        saveLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 获取屏幕的高和宽
     */
    private void getWindowWH() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    public Bitmap createBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth;
            int destHeight;
            // 缩放的比例
            double ratio;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 初始化截图区域，并将源图按裁剪框比例缩放
     *
     * @param top
     * @param bitmap
     */
    private void initClipView(int top, final Bitmap bitmap) {
//        srcPic.setImageBitmap(bitmap);
        clipview = new ClipView(ClipGroupPictureAndUploadActivity.this);
        clipview.setCustomTopBarHeight(top);

        clipview.addOnDrawCompleteListener(new OnDrawListenerComplete() {

            public void onDrawCompelete() {
                clipview.removeOnDrawCompleteListener();
                int clipHeight = clipview.getClipHeight();
                int clipWidth = clipview.getClipWidth() + 50;
                int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
                int midY = clipview.getClipTopMargin() + (clipHeight / 2);

                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();

                DLog.i("300", "imageWidth=" + imageWidth + "imageHeight=" + imageHeight
                        + "clipHeight=" + clipHeight + "clipWidth=" + clipWidth
                        + "midX=" + midX + "midY=" + midY);
                // 按裁剪框求缩放比例
                float scale = (clipWidth * 1.0f) / imageWidth;
                if (imageWidth > imageHeight) {
                    scale = (clipHeight * 1.0f) / imageHeight;
                }
                // 起始中心点
                float imageMidX = imageWidth / 2;
                float imageMidY = clipview.getCustomTopBarHeight()
                        + imageHeight * scale / 2;
                srcPic.setScaleType(ScaleType.MATRIX);
                DLog.i("300", "imageMidX=" + imageMidX + "imageMidY=" + imageMidY
                        + "scale=" + scale);
                // 缩放
                matrix.postScale(scale, scale);
                // 平移
                matrix.postTranslate(0, midY - imageMidY);

                srcPic.setImageMatrix(matrix);
                srcPic.setImageBitmap(bitmap);
            }
        });

        this.addContentView(clipview, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                // 设置开始点位置
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;

            default:
                break;
        }
        view.setImageMatrix(matrix);
        return true;
    }

    /**
     * 多点触控时，计算最先放下的两指距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void onClick_back(View v) {

        switch (v.getId()) {
            case R.id.btn22:
                getBitmap();
                if (NetworkUtil.isNetWorkConnected()) {
                    List<String> filePathList = new ArrayList<String>();
                    filePathList.add(filename);
                    Map paramMap = new HashMap();
                    paramMap.put("a", "huanxingroup");
                    paramMap.put("m", "modifyimg");
                    paramMap.put("groupid", groupid);
                    paramMap.put("userid", userid);
                    paramMap.put("bind", userid);
                    //signkey 传未经MD5处理的Key CommonHttpMultipartPost 内部会对该key进行MD5处理
                    paramMap.put("sign", userid);
                    commonHttpMultipartPost = new CommonHttpMultipartPost(ClipGroupPictureAndUploadActivity.this, filePathList,
                            CommonUrl.ModleUrl + "index.interface.php",
                            handler, 200);
                    commonHttpMultipartPost.execute(paramMap);
                } else {
                    ToastUtils.shortToast("网络连失败,图片不能上传,请联网重试");
                }
//                Intent intent = new Intent();
//                intent.putExtra("path", filename);
//                setResult(Activity.RESULT_OK, intent);
//                finish();
                break;
            case R.id.btn1:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 获取裁剪框内截图
     *
     * @return
     */
    private void getBitmap() {
        View view = null;
        WeakReference<Bitmap> bmp = null;
        try {
            // 获取截屏
            view = this.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();

            // 获取状态栏高度
            Rect frame = new Rect();
            this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(),
                    clipview.getClipLeftMargin(),
                    clipview.getClipTopMargin() + statusBarHeight,
                    clipview.getClipWidth(),
                    clipview.getClipHeight());
            bmp = new WeakReference<>(finalBitmap);
            savaBitmap(bmp.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (view != null) {
                view.destroyDrawingCache();
            }
            if (bmp != null && bmp.get() != null) {
                bmp.get().recycle();
            }
        }

    }

    /**
     * 保存bitmap对象到本地
     *
     * @param bitmap
     */
    public void savaBitmap(Bitmap bitmap) {
        File f = new File(filename);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
        try {
            if (fOut != null) {
                fOut.flush();
                fOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(ClipGroupPictureAndUploadActivity.this);

    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(ClipGroupPictureAndUploadActivity.this);
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }

}
