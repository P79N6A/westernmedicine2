package com.xywy.askforexpert.module.message.imgroup.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.KeyBoardUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PNDialogListener;
import com.xywy.askforexpert.model.im.group.ContactModel;
import com.xywy.askforexpert.model.im.group.GroupModel;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.imgroup.ContactService;
import com.xywy.askforexpert.module.message.imgroup.adapter.GroupMemberGridAdapter;
import com.xywy.askforexpert.module.message.imgroup.constants.UserPageShowType;
import com.xywy.askforexpert.module.message.imgroup.utils.GroupBroadCastUtils;
import com.xywy.askforexpert.module.message.imgroup.utils.GroupUtils;
import com.xywy.askforexpert.module.message.msgchat.ChatSendMessageHelper;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 群组信息Activity
 * Created by bailiangjin on 16/6/30.
 */
public class GroupInfoActivity extends YMBaseActivity {


    public static final String GROUP_HEAD_URL_INTENT_KEY = "GROUP_HEAD_URL_INTENT_KEY";
    public static final String NEW_GROUP_MASTER_ID_INTENT_KEY = "NEW_GROUP_MASTER_ID_INTENT_KEY";
    /**
     * gridView Add 标签的名字 用以区别 普通成员item
     */
    public static final String ADD_LABEL_NAME = "ADD_LABEL_NAME";
    private static final String GROUP_ID_INTENT_KEY = "GROUP_ID_INTENT_KEY";
    /**
     * 选择新群主 请求码
     */
    private static final int SELECT_NEW_GROUP_MASTER_REQUEST_CODE = 100;

    /**
     * 打卡相机拍照
     */
    private static final int TAKE_PHOTO_REQUEST_CODE = 201;
    /**
     * 打卡相册
     */
    private static final int ALBUM_REQUEST_CODE = 202;

    /**
     * 修改头像完成
     */
    private static final int MODIFY_FINISH_REQUEST_CODE = 203;
    @Bind(R.id.rl_root)
    View rl_root;
    @Bind(R.id.et_group_name)
    EditText et_group_name;
    @Bind(R.id.iv_group_avatar)
    ImageView iv_group_avatar;
    @Bind(R.id.tv_group_member_count)
    TextView tv_group_member_count;
    @Bind(R.id.cb_group_disturb)
    CheckBox cb_group_disturb;
    @Bind(R.id.gv_group_member)
    GridView gv_group_member;
    private SharedPreferences mPreferences;
    private SelectPicPopupWindow menuWindow;

    private GroupMemberGridAdapter groupMemberGridAdapter;

    private String groupId;
    private GroupModel curGroupModel;
    private List<ContactModel> memberList = new ArrayList<>();

    /**
     * 图片地址
     */
    private File origUri;
    // 头像修改 窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            origUri = new File(PathUtil.getInstance().getImagePath(), "osc_"
                    + System.currentTimeMillis() + ".jpg");
            if (origUri.getParentFile() != null) {
                origUri.getParentFile().mkdirs();
            }

            switch (v.getId()) {
                case R.id.item_popupwindows_camera:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(GroupInfoActivity.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            CommonUtils.permissionRequestDialog(GroupInfoActivity.this, "无法打开相机，请授予照相机(Camera)权限", 123);
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                    break;
                case R.id.item_popupwindows_Photo:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(GroupInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            CommonUtils.permissionRequestDialog(GroupInfoActivity.this, "无法打开相册，请授予内存(Storage)权限", 123);
                        } else {
                            toSelectPhoto();
                        }
                    } else {
                        toSelectPhoto();
                    }
                    break;
                case R.id.item_popupwindows_cancel:
                    menuWindow.dismiss();

                    break;
                default:
                    break;
            }

        }

        private void openCamera() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
            startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
        }

        private void toSelectPhoto() {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, ALBUM_REQUEST_CODE);
        }
    };

    public static void start(Activity activity, String groupId) {
        LogUtils.d("groupId:" + groupId);
        if (TextUtils.isEmpty(groupId)) {
            ToastUtils.shortToast("群id参数为空");
            return;
        }
        Intent intent = new Intent(activity, GroupInfoActivity.class);
        intent.putExtra(GROUP_ID_INTENT_KEY, groupId);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_groupinfo;
    }

    @Override
    protected void beforeViewBind() {
        mPreferences = getSharedPreferences("msg_manager", MODE_PRIVATE);
        groupId = getIntent().getStringExtra(GROUP_ID_INTENT_KEY);
    }

    @Override
    protected void initView() {
        gv_group_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onGroupMemberClicked(position);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //回到群信息页刷新数据
        initGroupInfo();
    }

    @Override
    protected void initData() {
        //获取群组信息
        initGroupInfo();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_NEW_GROUP_MASTER_REQUEST_CODE:
                    //转移群主身份
                    final String newGroupMasterId = data.getStringExtra(NEW_GROUP_MASTER_ID_INTENT_KEY);
                    if (!TextUtils.isEmpty(newGroupMasterId)) {
                        LogUtils.d("更换群主todo：" + newGroupMasterId);

                        String groupId = curGroupModel.getGroupId();
                        String curUserId = curGroupModel.getOwner();
                        callServerToExistGroup(groupId, curUserId, newGroupMasterId, true);
                    }
                    break;
                case ALBUM_REQUEST_CODE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photoBitmap != null) {
                            Bitmap newBitMap = GroupUtils.zoomBitmap(photoBitmap);
                            photoBitmap.recycle();
                            String photoPath = GroupUtils.saveGroupHeadPhotoBitmap(this, newBitMap);
                            startClip(photoPath);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case TAKE_PHOTO_REQUEST_CODE:
                    String photoPath = origUri.getPath();
                    LogUtils.d("groupImage:path:" + photoPath);
                    startClip(photoPath);
                    break;

                case MODIFY_FINISH_REQUEST_CODE:
                    shortToast("群头像修改成功");
                    GroupBroadCastUtils.sendModifyGroupHeadBroadcast(GroupInfoActivity.this);
                    String groupHeadUrl = data.getStringExtra(GROUP_HEAD_URL_INTENT_KEY);
                    LogUtils.d("groupHeadUrl:" + groupHeadUrl);
                    ImageLoadUtils.INSTANCE.loadRoundedImageView(iv_group_avatar, groupHeadUrl);
                    ChatSendMessageHelper.saveMessage(groupId, "管理员已修改群头像", curGroupModel.getContactName(), groupHeadUrl);
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 全局事件分发 实现 触摸非输入框控件 隐藏键盘
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            //判断之前输入框 是否获取了焦点
            if (et_group_name.isFocused()) {
                return checkToModifyGroupName();

            } else {
                //未获取焦点 不拦截事件
                return super.dispatchTouchEvent(motionEvent);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @OnClick({R.id.btn_exist, R.id.iv_group_avatar, R.id.rl_group_member_top, R.id.cb_group_disturb})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exist:
                StatisticalTools.eventCount(this, "WithdrawFromThiGroup");
                //退出群组逻辑
                //shortToast("退出群组");
                existGroup();
                break;
            case R.id.iv_group_avatar:
                StatisticalTools.eventCount(this, "GroupHead");
                //群主修改头像
                //shortToast("修改群头像");
                // 设置layout在PopupWindow中显示的位置
                menuWindow = new SelectPicPopupWindow(GroupInfoActivity.this, itemsOnClick);
                backgroundAlpha(0.5f);
                // 显示窗口
                menuWindow.showAtLocation(rl_root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                break;
            case R.id.rl_group_member_top:
                //打开群成员列表
                ImUserListActivity.start(GroupInfoActivity.this, groupId, UserPageShowType.SHOW);
                break;
            case R.id.cb_group_disturb:
                StatisticalTools.eventCount(this, "MessageWithoutInterruption");
                //更改免打扰状态
                ContactService.getInstance().setGroupNoDisturb(groupId, curGroupModel.getIsDisturb() == 1 ? 0 : 1, new CommonResponse<GroupModel>(YMApplication.getAppContext()) {
                    @Override
                    public void onNext(GroupModel groupModel) {
                        curGroupModel = groupModel;
                        initOrUpdateGroupData(curGroupModel);
                    }
                });

                if (curGroupModel.getIsDisturb() == 0) {

                    mPreferences.edit().putBoolean(YMUserService.getCurUserId() + groupId, true).commit();
                } else {
                    mPreferences.edit().putBoolean(YMUserService.getCurUserId() + groupId, false).commit();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 校验 符合条件 修改群组名称
     *
     * @return
     */
    private boolean checkToModifyGroupName() {
        //已获取焦点 拦截事件
        final String newGroupName = et_group_name.getText().toString();
        if (TextUtils.isEmpty(newGroupName.trim())) {
            shortToast("给您的讨论群起一个响亮的名字吧！");
            et_group_name.setText(curGroupModel.getContactName());
            return true;
        }
        if (newGroupName.length() > 15) {
            shortToast("群名称不能多于15个字符");
            return true;
        }
        //输入框去除焦点
        et_group_name.clearFocus();
        //关闭键盘
        KeyBoardUtils.closeKeyboard(getCurrentFocus());
        boolean isGroupNameChanged = null != curGroupModel && !newGroupName.equals(curGroupModel.getContactName());
        if (isGroupNameChanged) {
            modifyGroupName(newGroupName);
        } else {
            //群名称无变化
            LogUtils.e("群名称无变化");
        }
        return true;
    }

    /**
     * 修改群名称
     *
     * @param newGroupName
     */
    private void modifyGroupName(final String newGroupName) {
        LogUtils.d("更改群名称");
        //更改成功后 更新groupName为新群名称
        ContactService.getInstance().changeGroupName(groupId, newGroupName, new CommonResponse<GroupModel>(YMApplication.getAppContext()) {

            @Override
            public void onNext(GroupModel groupModel) {
                LogUtils.d("更改群名称 成功");
                shortToast("更改成功");
                curGroupModel = groupModel;
                initOrUpdateGroupData(curGroupModel);

            }
        });
    }

    /**
     * 初始化 群信息
     */
    private void initGroupInfo() {
        ContactService.getInstance().getGroupDetail(groupId, new CommonResponse<GroupModel>(YMApplication.getAppContext()) {

            @Override
            public void onNext(final GroupModel groupModel) {
                curGroupModel = groupModel;
                initOrUpdateGroupData(curGroupModel);

                //LogUtils.d("curGroupModel:" + GsonUtils.INSTANCE.toJson(groupModel));
                LogUtils.d("curGroupModel:owner:" + groupModel.getOwner());
                updateMemberList();


            }
        });
    }

    /**
     * 群成员 gridview item 点击事件
     *
     * @param position
     */
    private void onGroupMemberClicked(int position) {
        if (null == memberList || memberList.isEmpty() || position > memberList.size() - 1) {
            LogUtils.e("列表为空 或数组越界");
            return;
        }

        ContactModel contactModel = memberList.get(position);
        if (null == contactModel) {
            return;
        }

        if (position == memberList.size() - 1) {
            StatisticalTools.eventCount(this, "GroupMore");
            StatisticalTools.eventCount(this, "NewGroupMembers");
            if (curGroupModel.getMemberList().size() >= curGroupModel.getMaxusers()) {

                new XywyPNDialog.Builder().setContent("对不起，群人数已经超过上限！").create(GroupInfoActivity.this, new PNDialogListener() {
                    @Override
                    public void onPositive() {

                    }

                    @Override
                    public void onNegative() {

                    }
                });

                return;
            }
            //add 按钮
            LogUtils.d("添加群成员");
            ImUserListActivity.start(GroupInfoActivity.this, groupId, UserPageShowType.ADD_MEMBER);
        } else {
            //跳转到群成员信息页
            LogUtils.d("跳转到个人信息页：userId：" + contactModel.getUserId() + " userName:" + contactModel.getUserName());
            PersonDetailActivity.start(GroupInfoActivity.this, contactModel.getUserId(), "3");
        }
    }

    /**
     * 更新 群成员GridView
     */
    private void updateMemberList() {
        if (curGroupModel == null) {
            return;
        }
        //下载群头像
        List<ContactModel> memberListTemp = curGroupModel.getMemberList();
        if (null == memberListTemp || memberListTemp.isEmpty()) {
            tv_group_member_count.setText("0");
            return;
        } else {
            tv_group_member_count.setText("" + memberListTemp.size());
            List<ContactModel> sortedContactedList = sortContactList(memberListTemp);

            Collections.reverse(sortedContactedList);

            memberList.clear();
            // 限制数量 最多两行
            int number = sortedContactedList.size() > 9 ? 9 : sortedContactedList.size();
            for (int i = 0; i < number; i++) {
                ContactModel model = sortedContactedList.get(i);
                String owner = curGroupModel.getOwner();
                if (!TextUtils.isEmpty(owner) && owner.equals(model.getUserId())) {
                    model.setMaster(true);
                }
                memberList.add(model);
            }

            //添加加号图标
            ContactModel addLabel = new ContactModel();
            addLabel.setUserId(ADD_LABEL_NAME);
            addLabel.setUserName(ADD_LABEL_NAME);
            LogUtils.d("memberList:size:" + memberList.size());
            memberList.add(addLabel);
            groupMemberGridAdapter = new GroupMemberGridAdapter(GroupInfoActivity.this, memberList);
            gv_group_member.setAdapter(groupMemberGridAdapter);
        }


    }

    @NonNull
    private List<ContactModel> sortContactList(List<ContactModel> memberListTemp) {
        List<ContactModel> tempList = new ArrayList<>();
        ContactModel master = null;
        for (ContactModel contactModel : memberListTemp) {
            String owner = curGroupModel.getOwner();
            if (!TextUtils.isEmpty(owner) && owner.equals(contactModel.getUserId())) {
                contactModel.setMaster(true);
                master = contactModel;
            } else {
                tempList.add(contactModel);
            }
        }
        if (null != master) {
            tempList.add(master);
        }
        return tempList;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 退出群组
     */
    private void existGroup() {
        final String curUserId = YMUserService.getCurUserId();
        if (null == curGroupModel) {
            LogUtils.e("群信息异常");
            return;
        }
        if (curUserId.equals(curGroupModel.getOwner())) {
            groupMasterExistGroup(curUserId);

        } else {
            normalUserExistGroup(curUserId);


        }

    }

    private void normalUserExistGroup(final String curUserId) {
        //当前用户不是群主
        new XywyPNDialog.Builder().setContent("您是否确定退出本讨论群？")
                .create(GroupInfoActivity.this, new PNDialogListener() {
                    @Override
                    public void onPositive() {
                        callServerToExistGroup(groupId, curUserId, null, false);
                    }

                    @Override
                    public void onNegative() {

                    }
                });
    }

    private void groupMasterExistGroup(final String curUserId) {
        //当前用户为群主
        if (curGroupModel.getMemberList().size() > 2) {

            new XywyPNDialog.Builder().setContent("您是否确定退出本讨论群，因您是管理员需要将管理权限转接给另一位群成员")
                    .create(GroupInfoActivity.this, new PNDialogListener() {
                        @Override
                        public void onPositive() {
                            // 群成员大于两人
                            ImUserListActivity.startForResult(GroupInfoActivity.this, groupId, UserPageShowType.SELECT_NEW_MASTER, SELECT_NEW_GROUP_MASTER_REQUEST_CODE);

                        }

                        @Override
                        public void onNegative() {

                        }
                    });


            return;

        } else if (curGroupModel.getMemberList().size() <= 2) {

            new XywyPNDialog.Builder().setContent("您是否确定退出本讨论群？")
                    .create(GroupInfoActivity.this, new PNDialogListener() {
                        @Override
                        public void onPositive() {
                            deleteGroup(groupId, curUserId);
                        }

                        @Override
                        public void onNegative() {

                        }
                    });
        }
    }

    private void callServerToExistGroup(final String groupId, String curUserId, final String newGroupMasterId, final boolean isChangeMaster) {
        //调用转移群主接口
        ContactService.getInstance().existGroup(groupId, curUserId, newGroupMasterId, new CommonResponse<Integer>(YMApplication.getAppContext()) {
            @Override
            public void onNext(Integer resultCode) {
                if (10000 == resultCode) {
                    GroupBroadCastUtils.sendExistGroupBroadcast(GroupInfoActivity.this);
                    if (isChangeMaster) {
                        LogUtils.d("更换群主成功：groupId:" + groupId + " newGroupMasterId:" + newGroupMasterId);
                        shortToast("更换群主成功");
                    } else {
                        LogUtils.d("退出群组成功：" + groupId);
                        shortToast("退出群组成功");
                    }
                    //跳转到群组列表页
                    // GroupListActivity.start(GroupInfoActivity.this, GroupListShowType.SHOW);
                    finish();
                }
            }
        });
    }

    private void deleteGroup(final String groupId, String curUserId) {
        //调用转移群主接口
        ContactService.getInstance().deleteGroup(groupId, curUserId, new CommonResponse<Integer>(YMApplication.getAppContext()) {
            @Override
            public void onNext(Integer resultCode) {
                if (10000 == resultCode) {
                    GroupBroadCastUtils.sendExistGroupBroadcast(GroupInfoActivity.this);

                    LogUtils.d("退出群组成功：" + groupId);
                    shortToast("退出群组成功");
                    finish();
                }
            }
        });
    }

    /**
     * 更新群名称
     */
    private void initOrUpdateGroupData(GroupModel curGroupModel) {
        if (null != curGroupModel) {
            LogUtils.d("getIsDisturb:" + curGroupModel.getIsDisturb());
            cb_group_disturb.setChecked(curGroupModel.getIsDisturb() == 1);
            //设置是否可以修改群名称
            boolean isCurUserGroupMaster = YMUserService.getCurUserId().equals(curGroupModel.getOwner());
            //设置是否可编辑群名称
            et_group_name.setEnabled(isCurUserGroupMaster);
            et_group_name.setFocusableInTouchMode(isCurUserGroupMaster);
            et_group_name.setFocusable(isCurUserGroupMaster);
            //设置是否可修改群头像
            iv_group_avatar.setEnabled(isCurUserGroupMaster);
            ImageLoadUtils.INSTANCE.loadRoundedImageView(iv_group_avatar, curGroupModel.getHeadUrl(), R.drawable.icon_group_default_head, 2);
            LogUtils.d("iv_group_avatar:" + curGroupModel.getHeadUrl());
            String groupName = TextUtils.isEmpty(curGroupModel.getContactName()) ? "" : curGroupModel.getContactName();
            titleBarBuilder.setTitleText(groupName);
            et_group_name.setText(groupName);
        } else {
            LogUtils.e("curGroupModel is null");
        }

    }

    /**
     * 剪切图片并上传
     *
     * @param photoPath
     */
    private void startClip(String photoPath) {
        LogUtils.d("groupImage:path:" + photoPath);
        Intent intent = new Intent(this, ClipGroupPictureAndUploadActivity.class);
        intent.putExtra("path", photoPath);
        intent.putExtra("groupid", curGroupModel.getGroupId());
        intent.putExtra("userid", curGroupModel.getOwner());
        startActivityForResult(intent, MODIFY_FINISH_REQUEST_CODE);
    }


}
