package com.xywy.askforexpert.module.message.imgroup.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PNDialogListener;
import com.xywy.askforexpert.model.im.group.ContactBean;
import com.xywy.askforexpert.model.im.group.ContactModel;
import com.xywy.askforexpert.model.im.group.GroupModel;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.imgroup.ContactService;
import com.xywy.askforexpert.module.message.imgroup.adapter.ImUserGridAdapter;
import com.xywy.askforexpert.module.message.imgroup.adapter.ImUserPinyinAdapter;
import com.xywy.askforexpert.module.message.imgroup.adapter.PinyinBaseAdapter;
import com.xywy.askforexpert.module.message.imgroup.constants.UserPageShowType;
import com.xywy.askforexpert.module.message.imgroup.utils.GroupUtils;
import com.xywy.askforexpert.module.message.imgroup.view.pinyin.SearchFilter;
import com.xywy.askforexpert.module.message.imgroup.view.pinyin.callback.ItemClickCallback;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.widget.module.im.imgroup.PinyinIndexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 群成员列表 Activity
 * Created by bailiangjin on 16/6/30.
 */
public class ImUserListActivity extends YMBaseActivity {
    private static final String GROUP_ID_INTENT_KEY = "GROUP_ID_INTENT_KEY";
    private static final String SHOW_TYPE_INTENT_KEY = "SHOW_TYPE_INTENT_KEY";

    @Bind(R.id.et_search)
    EditText et_search;

    @Bind(R.id.pinyin_bar)
    PinyinIndexer pinyin_bar;

    @Bind(R.id.lv_e_list_view)
    ExpandableListView lv_e_list_view;

    @Bind(R.id.gv_selected_members)
    GridView gv_selected_members;

    @Bind(R.id.btn_confirm)
    Button btn_confirm;

    @Bind(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    private ImUserPinyinAdapter imUserPinyinAdapter;
    private ImUserGridAdapter imUserGridAdapter;

    private String groupId;
    private GroupModel curGroupModel;

    private List<ContactBean> lvContactBeanList = new ArrayList<>();
    private List<ContactBean> selectedList = new ArrayList<>();

    /**
     * 展示类型 本页面有以下四种 状态
     * SHOW：展示群成员
     * SELECT_NEW_MASTER：选择新群主
     * ADD_MEMBER：添加用户
     * CREATE_GROUP：创建群组
     */
    private UserPageShowType type;

    private Map<String, ContactModel> groupMemberMap = new HashMap<>();
    private boolean isMaster;

    public static void start(Context context, String groupId, UserPageShowType type) {
        Intent intent = new Intent(context, ImUserListActivity.class);
        intent.putExtra(GROUP_ID_INTENT_KEY, groupId);
        intent.putExtra(SHOW_TYPE_INTENT_KEY, type.toString());
        context.startActivity(intent);
    }

    public static void startForResult(Activity context, String groupId, UserPageShowType type, int requestCode) {
        Intent intent = new Intent(context, ImUserListActivity.class);
        intent.putExtra(GROUP_ID_INTENT_KEY, groupId);
        intent.putExtra(SHOW_TYPE_INTENT_KEY, type.toString());
        context.startActivityForResult(intent, requestCode);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_im_user_list;
    }


    @Override
    protected void beforeViewBind() {
        groupId = getIntent().getStringExtra(GROUP_ID_INTENT_KEY);
        String typeStr = getIntent().getStringExtra(SHOW_TYPE_INTENT_KEY);
        if (UserPageShowType.ADD_MEMBER.toString().equals(typeStr)) {
            type = UserPageShowType.ADD_MEMBER;
        } else if (UserPageShowType.CREATE_GROUP.toString().equals(typeStr)) {
            type = UserPageShowType.CREATE_GROUP;
        } else if (UserPageShowType.SHOW.toString().equals(typeStr)) {
            type = UserPageShowType.SHOW;
        } else if (UserPageShowType.SELECT_NEW_MASTER.toString().equals(typeStr)) {
            type = UserPageShowType.SELECT_NEW_MASTER;
        }
    }


    @Override
    protected void initView() {
        //覆盖拼音条点击收缩事件
        lv_e_list_view.setOnGroupClickListener(null);

        //显示类型为创建群组时显示 pinyin bar 其他情况隐藏
        if (type == UserPageShowType.CREATE_GROUP) {
            pinyin_bar.setVisibility(View.VISIBLE);
            pinyin_bar.setOnTouchLetterChangedListener(new PinyinIndexer.OnTouchLetterChangedListener() {

                @Override
                public void onTouchLetterChanged(String letter) {
                    for (int i = 0, j = imUserPinyinAdapter.getKeyMapList().getTypes().size(); i < j; i++) {
                        String str = imUserPinyinAdapter.getKeyMapList().getTypes().get(i);
                        if (letter.toUpperCase().equals(str.toUpperCase())) {
                            /**跳转到选中的字母表*/
                            lv_e_list_view.setSelectedGroup(i);
                        }
                    }
                }
            });
        } else {
            pinyin_bar.setVisibility(View.GONE);
        }
        //底部 滚动view 点击事件
        if (type == UserPageShowType.CREATE_GROUP || type == UserPageShowType.ADD_MEMBER) {
            gv_selected_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ContactBean contactBean = selectedList.get(position);
                    contactBean.setChecked(!contactBean.isChecked());
                    imUserPinyinAdapter.setListData(lvContactBeanList);
                    imUserPinyinAdapter.notifyDataSetChanged();
                    updateSelectedList();
                }
            });
        }

    }


    @Override
    protected void initData() {
        initOrUpdateTitle(GroupUtils.getGroupName(curGroupModel));
        switch (type) {
            case ADD_MEMBER:
                rl_bottom.setVisibility(View.VISIBLE);
                curGroupModel = ContactService.getInstance().getGroupMemberFromLocal(groupId);
                if (null == curGroupModel || null == curGroupModel.getMemberList() || curGroupModel.getMemberList().isEmpty()) {
                    LogUtils.e("group info error");
                    return;
                } else {
                    //将群成员信息 存放到map中
                    for (ContactModel groupContactModel : curGroupModel.getMemberList()) {
                        groupMemberMap.put(groupContactModel.getUserId(), groupContactModel);
                    }
                    LogUtils.d("groupMemberMap:keySize" + groupMemberMap.keySet().size());
                }
                ContactService.getInstance().getContactList(new CommonResponse<List<ContactModel>>(YMApplication.getAppContext()) {
                    @Override
                    public void onNext(List<ContactModel> contactModels) {
                        List<ContactModel> contactModelList = contactModels;
                        Set<String> userIdSet = groupMemberMap.keySet();
                        for (ContactModel contactModel : contactModelList) {
                            if (userIdSet.contains(contactModel.getUserId())) {
                                contactModel.setGroupMember(true);
                                LogUtils.d("groupMemberMap:addMemberLabel:" + contactModel.getUserId());
                                continue;

                            }
                        }
                        initOrUpdateAdapterData(contactModelList);
                    }
                });
                break;

            case CREATE_GROUP:
                //底部 滚动View处理
                rl_bottom.setVisibility(View.VISIBLE);
                ContactService.getInstance().getContactList(new CommonResponse<List<ContactModel>>(YMApplication.getAppContext()) {
                    @Override
                    public void onNext(List<ContactModel> contactModels) {
                        LogUtils.d("contactModels:size:" + contactModels.size());
                        List<ContactModel> contactModelList = contactModels;
                        initOrUpdateAdapterData(contactModelList);
                    }
                });

                break;
            case SHOW:
                //左滑删除 logic
                rl_bottom.setVisibility(View.GONE);
                showGroupMember();
                break;
            case SELECT_NEW_MASTER:
                rl_bottom.setVisibility(View.GONE);
                showGroupMember();
                break;
            default:
                break;
        }
    }

    /**
     * 更新选中列表
     */
    private void updateSelectedList() {
        if (type == UserPageShowType.SHOW) {
            LogUtils.d("展示状态 不显示 底部View");
            return;
        }
        selectedList.clear();
        for (ContactBean contactBean : lvContactBeanList) {
            if (contactBean.isChecked()) {
                selectedList.add(contactBean);
            }
        }
        btn_confirm.setText("确定(" + selectedList.size() + ")");
        initOrUpdateSelectedAdapter(selectedList);
    }

    /**
     * 显示群成员
     */
    private void showGroupMember() {
        //获取群组信息
        ContactService.getInstance().getGroupDetail(groupId, new CommonResponse<GroupModel>(YMApplication.getAppContext()) {
            @Override
            public void onNext(final GroupModel groupModel) {
                curGroupModel = groupModel;
                LogUtils.d("curGroupModel:" + GsonUtils.toJson(groupModel));
                LogUtils.d("curGroupModel:owner:" + groupModel.getOwner());
                updateData();
            }
        });
    }


    private void updateData() {
        if (curGroupModel == null) {
            return;
        }
        String groupName = GroupUtils.getGroupName(curGroupModel);
        initOrUpdateTitle(groupName + "(" + curGroupModel.getMemberList().size() + ")");
        //下载群头像
        List<ContactModel> memberListStart = curGroupModel.getMemberList();
        //更新列表数据
        initOrUpdateAdapterData(memberListStart);
    }


    /**
     * 初始化 或更新 Adapter 数据
     *
     * @param dataList
     */
    private void initOrUpdateAdapterData(List<ContactModel> dataList) {
        if (null == dataList || dataList.isEmpty()) {
            return;
        } else {
            lvContactBeanList.clear();
            if (null != curGroupModel) {
                //展示群成员状态
                for (ContactModel model : dataList) {
                    String owner = curGroupModel.getOwner();
                    if (!TextUtils.isEmpty(owner) && owner.equals(model.getUserId())) {
                        model.setMaster(true);
                        lvContactBeanList.add(model.toContactBean());
                        continue;
                    }
                    lvContactBeanList.add(model.toContactBean());
                }
            } else {
                //展示通讯录好友
                lvContactBeanList.addAll(toContactBeanList(dataList));
            }
            initOrUpdateListAdapter(lvContactBeanList);

            final ImUserFilter groupFilter = new ImUserFilter(lvContactBeanList);


            et_search.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    groupFilter.filter(charSequence);
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void afterTextChanged(Editable s) {

                }
            });

        }
    }

    private void initOrUpdateListAdapter(final List<ContactBean> lvContactBeanList) {
        Collections.reverse(lvContactBeanList);
        LogUtils.d("memberListNew:" + lvContactBeanList.size());

        ItemClickCallback itemClickCallback = new ItemClickCallback<ContactBean>() {
            @Override
            public void onItemClick(final ContactBean contactBean) {
                switch (type) {
                    case ADD_MEMBER:
                        if (contactBean.isGroupMember()) {
                            //已经是组成员 不添加
                            LogUtils.d("已经是组成员 不添加:" + contactBean.getUserId() + ":" + contactBean.getUserName());
                            shortToast("已经是群成员");
                            return;
                        }
                        contactBean.setChecked(!contactBean.isChecked());
                        LogUtils.d("群组添加或取消人:" + contactBean.getUserId() + ":" + contactBean.getUserName() + " 选中状态：" + contactBean.isChecked());
                        imUserPinyinAdapter.setListData(lvContactBeanList);
                        imUserPinyinAdapter.notifyDataSetChanged();
                        updateSelectedList();
                        break;
                    case CREATE_GROUP:
                        contactBean.setChecked(!contactBean.isChecked());
                        LogUtils.d("创建群添加或取消人:" + contactBean.getUserId() + ":" + contactBean.getUserName() + " 选中状态：" + contactBean.isChecked());
                        imUserPinyinAdapter.setListData(lvContactBeanList);
                        imUserPinyinAdapter.notifyDataSetChanged();
                        updateSelectedList();
                        break;
                    case SHOW:
                        //lv_group_members.setCanScroll(true);
                        LogUtils.d("跳转到群成员 个人信息页:" + contactBean.getUserId() + ":" + contactBean.getUserName());
                        PersonDetailActivity.start(ImUserListActivity.this, contactBean.getUserId(), "3");
                        break;
                    case SELECT_NEW_MASTER:
                        if (contactBean.isMaster()) {
                            shortToast("您不能选择自己");
                            return;
                        } else {
                            showChangeGroupMasterNoticeDialog(contactBean);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onItemDelete(final ContactBean contactBean) {
                StatisticalTools.eventCount(ImUserListActivity.this, "RemoveTheGroupButton");
                new XywyPNDialog.Builder().setContent("您是否确定将此成员移除本群？")
                        .create(ImUserListActivity.this, new PNDialogListener() {
                            @Override
                            public void onPositive() {
                                //当前用户不是群主
                                kickMember(contactBean);
                            }

                            @Override
                            public void onNegative() {

                            }
                        });

            }
        };

        //展示状态 群主 可滑动删除用户
        boolean isCanScroll = (type == UserPageShowType.SHOW && YMUserService.getCurUserId().equals(curGroupModel.getOwner()));
        imUserPinyinAdapter = new ImUserPinyinAdapter(lv_e_list_view, lvContactBeanList, type, isCanScroll, itemClickCallback);
        imUserPinyinAdapter.expandGroup();
        updateSelectedList();
    }

    private void showChangeGroupMasterNoticeDialog(final ContactBean contactBean) {
        new XywyPNDialog.Builder().setContent(" 你是否将管理员权限授予 " + GroupUtils.getUserName(contactBean) + "?")
                .create(ImUserListActivity.this, new PNDialogListener() {
                    @Override
                    public void onPositive() {
                        //选定了新群主 结束当前Activity
                        LogUtils.d("选择了：" + contactBean.getUserId() + " " + contactBean.getUserName());
                        Intent intent = new Intent();
                        intent.putExtra(GroupInfoActivity.NEW_GROUP_MASTER_ID_INTENT_KEY, contactBean.getUserId());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onNegative() {

                    }
                });

    }

    /**
     * 踢出群成员
     *
     * @param contactBean
     */
    private void kickMember(ContactBean contactBean) {
        ContactService.getInstance().deleteGroupMember(groupId, curGroupModel.getOwner(), contactBean.getUserId(), new CommonResponse<Integer>(YMApplication.getAppContext()) {
            @Override
            public void onNext(Integer resultCode) {
                if (10000 == resultCode) {
                    //修改成功
                    finish();
                    ImUserListActivity.start(ImUserListActivity.this, groupId, UserPageShowType.SHOW);
                } else {
                    shortToast("移除群成员失败：错误码：" + resultCode);
                }
            }
        });
    }

    private List<ContactBean> toContactBeanList(List<ContactModel> contactModelList) {
        List<ContactBean> contactBeanList = new ArrayList<>();
        for (ContactModel model : contactModelList) {
            contactBeanList.add(model.toContactBean());
        }
        return contactBeanList;
    }

    private void initOrUpdateSelectedAdapter(List<ContactBean> selectedList) {
        int size = selectedList.size();
        int itemDpWidth = 44;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridViewWidth = (int) (size * (itemDpWidth) * density);
        int itemWidth = (int) (itemDpWidth * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridViewWidth, DensityUtils.dp2px( 44));
        gv_selected_members.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_selected_members.setColumnWidth(itemWidth); // 设置列表项宽
        gv_selected_members.setHorizontalSpacing(2); // 设置列表项水平间距
        gv_selected_members.setStretchMode(GridView.NO_STRETCH);
        gv_selected_members.setNumColumns(size); // 设置列数量=列表集合数

        if (null == imUserGridAdapter) {
            imUserGridAdapter = new ImUserGridAdapter(ImUserListActivity.this, selectedList);
            gv_selected_members.setAdapter(imUserGridAdapter);
        } else {
            imUserGridAdapter.setData(selectedList);
            imUserGridAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 更新群名称
     *
     * @param titleText
     */
    private void initOrUpdateTitle(String titleText) {
        switch (type) {
            case ADD_MEMBER:
                titleText = "添加好友";
                break;
            case CREATE_GROUP:
                titleText = "创建群";
                break;
            case SHOW:
                //titleText = "群成员";
                break;

            case SELECT_NEW_MASTER:
                titleText = "选择新群主";
                break;
            default:
                break;
        }
        titleBarBuilder.setTitleText(titleText);
    }


    @SuppressWarnings("PMD")
    @OnClick({R.id.btn_confirm, R.id.et_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
                StatisticalTools.eventCount(this, "SearchFriends");
                break;
            case R.id.btn_confirm:
                StatisticalTools.eventCount(this, "Sure");
                String[] members = getMemberIdStr();
                if (members == null) {
                    return;
                }
                switch (type) {
                    case CREATE_GROUP:
                        shortToast("创建群");
                        createGroup(members);

                        break;
                    case ADD_MEMBER:
                        addMember(members);
                        break;
                }
                break;

            default:
                break;
        }
    }

    @Nullable
    private String[] getMemberIdStr() {
        //创建群组 或添加成员
        if (selectedList.size() == 0) {
            shortToast("请先选择好友");
            return null;
        }
        String[] members = new String[selectedList.size()];
        for (int i = 0; i < selectedList.size(); i++) {
            members[i] = selectedList.get(i).getUserId();
        }
        return members;
    }

    private void addMember(String[] members) {
        int maxUser = curGroupModel.getMaxusers();
        int curUserSize = curGroupModel.getMemberList().size();
        if (maxUser < curUserSize + members.length) {

            new XywyPNDialog.Builder().setContent("群人数已经超过上限，请重新选择好友！").create(ImUserListActivity.this, new PNDialogListener() {
                @Override
                public void onPositive() {

                }

                @Override
                public void onNegative() {

                }
            });

            return;
        }
        ContactService.getInstance().inviteToGroup(groupId, members, new CommonResponse<GroupModel>(YMApplication.getAppContext()) {
            @Override
            public void onNext(GroupModel groupModel) {
                shortToast("添加成功");
                finish();
            }
        });
    }

    private void createGroup(String[] members) {
        String toCreateGroupName;
        String curUserName = YMUserService.getCurUserName();
        if (TextUtils.isEmpty(curUserName)) {
            toCreateGroupName = "用户" + YMUserService.getCurUserId();
        } else {
            toCreateGroupName = curUserName;
        }
        ContactService.getInstance().createGroup(toCreateGroupName, members, new CommonResponse<GroupModel>(YMApplication.getAppContext()) {
            @Override
            public void onNext(GroupModel groupModel) {
                if (null == groupModel) {
                    LogUtils.e("创建群返回数据异常");
                    return;
                }
                shortToast("创建成功");
                finish();
                //跳转到群列表页
                ChatMainActivity.start(ImUserListActivity.this, groupModel.getGroupId(), groupModel.getContactName(), groupModel.getHeadUrl(), true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PinyinBaseAdapter.clearMovedView();
    }


    /**
     * 群成员 搜索 过滤器
     */
    class ImUserFilter extends SearchFilter<ContactBean> {

        public ImUserFilter(List<ContactBean> myList) {
            super(myList);
        }

        @Override
        protected String getSearchKeyFiledValue(final ContactBean item) {
            LogUtils.d("search_log:item");
            if (null == item) {
                return "";
            }
            return TextUtils.isEmpty(item.getUserName()) ? "" : item.getUserName();

        }

        @Override
        protected void updateData(final List<ContactBean> list) {
            LogUtils.d("search_log:updateData" + list);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initOrUpdateListAdapter(list);
                }
            });

        }
    }
}
