package com.xywy.livevideo.player;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.base.XywyBaseActivity;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.common_interface.CommonLiveResponse;
import com.xywy.livevideo.common_interface.IDataResponse;
import com.xywy.livevideo.common_interface.LiveRequest;
import com.xywy.livevideo.entity.BaseData;
import com.xywy.livevideo.entity.CreateLiveRoomRespEntity;
import com.xywy.livevideo.entity.ImageBean;
import com.xywy.livevideo.publisher.LiveHostActivity;
import com.xywy.livevideolib.R;
import com.xywy.util.ImageLoaderUtils;
import com.xywy.util.XYImageLoader;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangzheng on 2017/2/24.
 */
public class VideoBroadcastActivity extends XywyBaseActivity implements View.OnClickListener {

    private ImageView ivClose;

    private Spinner sp_type;

    //开始直播
    private Button btnStart;

    //直播名称
    private EditText etVideoName;

    private ImageView ivCover;

    private TextView tvUpload;

    Map<String, String> categoryData;
    private Uri imageUri;

    private static final int REQUESTCODE_CUTTING = 1;

    /**
     * 调用开播
     *
     * @param context
     */
    public static void startVideoBroadcastActivity(Context context) {
        context.startActivity(new Intent(context, VideoBroadcastActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity_video_broadcast);
        hideCommonBaseTitle();
        ImageLoaderUtils.getInstance().init(this);
        initView();
        initData();
    }

    private void initView() {
        sp_type = (Spinner) findViewById(R.id.sp_type);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
        ivCover = (ImageView) findViewById(R.id.iv_cover_img);
        ivCover.setOnClickListener(this);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        etVideoName = (EditText) findViewById(R.id.et_name);
        tvUpload = (TextView) findViewById(R.id.tv_upload_pic);
        if (LiveManager.getInstance().getConfig().anchorUrl != null && !"".equals(LiveManager.getInstance().getConfig().anchorUrl)) {
            tvUpload.setVisibility(View.GONE);
            XYImageLoader.getInstance().displayImage(LiveManager.getInstance().getConfig().anchorUrl, ivCover);
        }
    }

    /*
     [1=>'名医直播',2=>'医学美容',3=>'肿瘤',4=>'健身瑜伽',5=>'心理科',6=>'妇儿'] 直播房间分类
     */
    private void initData() {
        categoryData = new HashMap<>();
        categoryData.put("名医直播", "1");
        categoryData.put("医学美容", "2");
        categoryData.put("肿瘤", "3");
        categoryData.put("健身瑜伽", "4");
        categoryData.put("心理科", "5");
        categoryData.put("妇儿", "6");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.live_category_spinner_item, getResources().getStringArray(R.array.live_video_show_type));
        sp_type.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start) {
            //创建直播
            createLiveRoom();
        } else if (v.getId() == R.id.iv_cover_img) {
            //选择封面
            Intent intent = new Intent(this, PhotoSelectorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("limit", 1);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.iv_close) {
            //关闭
            finish();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data != null) {
                    List<String> paths = (List<String>) data.getExtras().getSerializable("photos");
                    // TODO: 2017/3/1  上传照片
                    if (paths != null && paths.get(0) != null) {
                        File file = new File(paths.get(0));
                        if (file.exists()) {
                            tvUpload.setText("正在上传图片...");
                            LiveRequest.uploadPic(file, new CommonLiveResponse<BaseData<ImageBean>>() {
                                @Override
                                public void onReceived(BaseData<ImageBean> result) {
                                    LiveManager.getInstance().getConfig().anchorUrl = result.data.getUrl();
                                    XYImageLoader.getInstance().displayImage(LiveManager.getInstance().getConfig().anchorUrl, ivCover);
                                    tvUpload.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 图片裁剪
     */
    public void startPhotoZoom() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 720);
        intent.putExtra("outputY", 540);

        //  如下方式裁剪大尺寸图片
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    private void createLiveRoom() {
        String cateId = null;
        if (sp_type.getSelectedView() != null) {
            TextView tvContent = (TextView) sp_type.getSelectedView().findViewById(R.id.tv_text_content);
            String cateType = tvContent.getText().toString();
            cateId = categoryData.get(cateType);
        }
        String name = etVideoName.getText().toString();
        if (checkParams(LiveManager.getInstance().getConfig().anchorUrl, cateId, name)) {
            LiveRequest.createLiveRoom(LiveManager.getInstance().getConfig().userId, LiveManager.getInstance().getConfig().anchorUrl, cateId, name, LiveManager.getInstance().getConfig().userType, new IDataResponse<CreateLiveRoomRespEntity>() {
                @Override
                public void onReceived(CreateLiveRoomRespEntity createLiveRoomRespEntity) {
                    if (createLiveRoomRespEntity.getCode() == 10000) {
                        LiveHostActivity.start(VideoBroadcastActivity.this, createLiveRoomRespEntity.getData().getRtmp(), createLiveRoomRespEntity.getData().getId() + "", createLiveRoomRespEntity.getData().getChatroomsid());
                        finish();
                    } else {
                        Toast.makeText(VideoBroadcastActivity.this, "创建直播失败," + createLiveRoomRespEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(VideoBroadcastActivity.this, "创建直播失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean checkParams(String url, String cateId, String name) {
        if (url == null || "".equals(url)) {
            Toast.makeText(this, "请上传封面照片", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name == null || name.equals("")) {
            Toast.makeText(this, "请输入直播标题", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cateId == null || cateId.equals("")) {
            Toast.makeText(this, "请选择直播类别", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
