package com.xywy.askforexpert.module.main.guangchang;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.HttpMultipartPost;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.UploadImgInfo;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：D_Platform 类名称：PromotionDetailActivity 类描述：晋级申请详情页 创建人：shihao
 * 创建时间：2015-6-1 下午1:56:35 修改备注：
 */
public class PromotionDetailActivity extends YMBaseActivity {

    private List<String> contentLists;

    private ListView lv;

    private DetailAdapter adapter;

    private RelativeLayout rlNull, rlContent;

    private TextView tvNull, tvContent;

    private String type = "";
    private String click = "";
    private String note = "";
    private String next = "";
    private String med_name = "";
    private String sq_status = "";

    private SelectPicPopupWindow menuWindow;

    private File origUri;
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 0x17;
    private static final int PHOTO_ZOOM = 0;
    private HttpMultipartPost post;
    public static List<String> imagePahtList_idcard = new ArrayList<String>();


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pro_detail;
    }

    @Override
    protected void beforeViewBind() {
        contentLists = getIntent().getStringArrayListExtra("content");
        type = getIntent().getStringExtra("type");
        click = getIntent().getStringExtra("click");
        note = getIntent().getStringExtra("note");
        next = getIntent().getStringExtra("next");
        med_name = getIntent().getStringExtra("med_name");
        sq_status = getIntent().getStringExtra("sq_status");
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("晋级详情");

        lv = (ListView) findViewById(R.id.lv_pro_detail);
        rlContent = (RelativeLayout) findViewById(R.id.rl_corrent_show);
        rlNull = (RelativeLayout) findViewById(R.id.rl_null_show);
        tvNull = (TextView) findViewById(R.id.tv_null_show);
        tvContent = (TextView) findViewById(R.id.tv_corrent_show);

        adapter = new DetailAdapter();

        View footView = LayoutInflater.from(PromotionDetailActivity.this)
                .inflate(R.layout.pro_detail_text, null);

        TextView tv = (TextView) footView.findViewById(R.id.tv_pro_item);

        if (!next.equals("")) {
            if (click.equals("1")) {
                rlContent.setVisibility(View.VISIBLE);
                rlNull.setVisibility(View.GONE);
                if (type.equals("save_smrenz")) {
                    tvContent.setText("上传证件");
                } else {
                    tvContent.setText("申请" + next);
                }
            } else {
                rlContent.setVisibility(View.GONE);
                rlNull.setVisibility(View.VISIBLE);

                if (Integer.parseInt(sq_status) == 1 || Integer.parseInt(sq_status) == 2 || Integer.parseInt(sq_status) == 3 || Integer.parseInt(sq_status) == 4 || Integer.parseInt(sq_status) == 5) {
                    tvNull.setText("我们将会在2个工作日内完成审核，请耐心等待~");
                } else {
                    tvNull.setText("您还不满足申请条件哦~再努力一下吧！");
                }
            }

        } else {
            rlContent.setVisibility(View.GONE);
            rlNull.setVisibility(View.VISIBLE);
            tvNull.setText("您现在已经是" + med_name + "了！");
        }

        tv.setTextColor(getResources().getColor(R.color.red));

        if (note.equals("1")) {
            tv.setText("注：每日有效回复总数≥10即可获得1个活跃数");

            lv.addFooterView(footView);
        }

        lv.setAdapter(adapter);

        tvContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (type.equals("save_smrenz")) {
                    menuWindow = new SelectPicPopupWindow(
                            PromotionDetailActivity.this, itemsOnClick);
                    // 显示窗口
                    menuWindow.showAtLocation(rlContent, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    promotionFun(CommonUrl.DP_COMMON + "command=" + type);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {
        Intent intent;

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:
                    // 照片命名
                    origUri = new File(PathUtil.getInstance().getImagePath(),
                            "osc_" + System.currentTimeMillis() + ".jpg");
                    origUri.getParentFile().mkdirs();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
                    startActivityForResult(intent, FLAG_CHOOSE_CAMERA);
                    break;
                case R.id.item_popupwindows_Photo:
                    // 照片命名
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                    startActivityForResult(intent, PHOTO_ZOOM);
                    break;
                case R.id.item_popupwindows_cancel:
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }

        }

    };

    /**
     * 上传证件
     *
     * @param url
     */
    private void uploadIdCard(String url) {
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("paperphoto", url);
        params.put("userid", uid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.PRO_UPLOAD_IDCARD, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        try {
                            JSONObject object = new JSONObject(t);
                            int code = object.getInt("code");
                            String msg = object.getString("msg");
                            if (code == 0) {
                                ToastUtils.shortToast("证件上传成功");
                                rlContent.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                                tvNull.setText("我们将会在2个工作日内完成审核，请耐心等待~");
                            } else {
                                ToastUtils.shortToast(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }

    /**
     * 获取从本地图库返回来的时候的URI解析出来的文件路径
     *
     * @return
     */
    private String getPhotoPathByLocalUri(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_ZOOM) {
                String imgPath = getPhotoPathByLocalUri(data);
                imagePahtList_idcard.clear();
                imagePahtList_idcard.add(imgPath);
                post = new HttpMultipartPost(PromotionDetailActivity.this, imagePahtList_idcard,
                        "http://api.club.xywy.com/doctorApp.interface.php",
                        uiHandler, 200);
                post.execute();
            }
            if (requestCode == FLAG_CHOOSE_CAMERA) {
                if (imagePahtList_idcard.size() == 2) {
                    ToastUtils.shortToast("最多可添加2张图片。");
                    return;
                }
                imagePahtList_idcard.clear();
                imagePahtList_idcard.add(origUri.getPath());

                post = new HttpMultipartPost(PromotionDetailActivity.this, imagePahtList_idcard,
                        "http://api.club.xywy.com/doctorApp.interface.php",
                        uiHandler, 200);
                post.execute();
            }


        }

    }

    private void promotionFun(String url) {
        // 点击晋级申请
        final ProgressDialog dialog = new ProgressDialog(
                PromotionDetailActivity.this, "全力加载中...");
        dialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();

        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(url, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String msg = jsonObject.getString("msg");
                    int code = jsonObject.getInt("code");

                    if (code == 0) {
                        rlContent.setVisibility(View.GONE);
                        rlNull.setVisibility(View.VISIBLE);
                        tvNull.setText("我们将会在2个工作日内完成审核，请耐心等待~");
                    }
                    dialog.closeProgersssDialog();
                    ToastUtils.shortToast(msg);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

    }

    public void onProDetailClick() {
        Intent intent = new Intent(PromotionDetailActivity.this, PromotionActivity.class);
        setResult(5000, intent);
        finish();

    }

    class DetailAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contentLists == null ? -1 : contentLists.size();
        }

        @Override
        public Object getItem(int position) {
            return contentLists == null ? null : contentLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(PromotionDetailActivity.this)
                        .inflate(R.layout.pro_detail_text, null);
                holder.tv_pro_item = (TextView) convertView
                        .findViewById(R.id.tv_pro_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_pro_item.setText(contentLists.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_pro_item;
    }


    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        String code;
        switch (msg.what) {

            case 200:
                UploadImgInfo upinfo2 = (UploadImgInfo) msg.obj;
                code = upinfo2.getCode();
                if (code != null && code.equals("0")) {
                    for (int i = 0; i < upinfo2.getData().size(); i++) {
                        uploadIdCard(upinfo2.getData().get(i)
                                .getUrl().toString());
                    }
                }
        }
    }
}
