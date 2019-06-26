package com.xywy.askforexpert.module.message.imgroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.im.group.GroupBean;
import com.xywy.askforexpert.model.im.group.GroupModel;
import com.xywy.askforexpert.module.message.imgroup.adapter.ImGroupPinyinAdapter;
import com.xywy.askforexpert.module.message.imgroup.constants.GroupListShowType;
import com.xywy.askforexpert.module.message.imgroup.view.pinyin.SearchFilter;
import com.xywy.askforexpert.module.message.imgroup.view.pinyin.callback.ItemClickCallback;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.widget.module.im.imgroup.PinyinIndexer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 群列表 Activity
 * Created by bailiangjin on 16/6/30.
 */
public class GroupListActivity extends YMBaseActivity {

    public static final String GROUP_ID_INTENT_KEY = "GROUP_ID_INTENT_KEY";
    public static final String GROUP_NAME_INTENT_KEY = "GROUP_NAME_INTENT_KEY";
    public static final String GROUP_HEAD_URL_INTENT_KEY = "GROUP_HEAD_URL_INTENT_KEY";
    private static final String SHOW_TYPE_KEY = "SHOW_TYPE_KEY";
    @Bind(R.id.et_search)
    EditText et_search;

    @Bind(R.id.pinyin_bar)
    PinyinIndexer pinyin_bar;


    @Bind(R.id.elv_groups)
    ExpandableListView elv_groups;

    private ImGroupPinyinAdapter imGroupPinyinAdapter;

    private List<GroupBean> groupBeanList = new ArrayList<>();

    private GroupListShowType showType;

    public static void start(Context context, GroupListShowType showType) {
        Intent intent = new Intent(context, GroupListActivity.class);
        intent.putExtra(SHOW_TYPE_KEY, showType.toString());
        context.startActivity(intent);
    }

    public static void startForResult(Activity context, int requestCode) {
        Intent intent = new Intent(context, GroupListActivity.class);
        intent.putExtra(SHOW_TYPE_KEY, GroupListShowType.SELECT_TO_SHARE.toString());
        context.startActivityForResult(intent, requestCode);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_group_list;
    }

    @Override
    protected void beforeViewBind() {
        String showTypeStr = getIntent().getStringExtra(SHOW_TYPE_KEY);
        if (GroupListShowType.SHOW.toString().equals(showTypeStr)) {
            showType = GroupListShowType.SHOW;
        } else if (GroupListShowType.SELECT_TO_SHARE.toString().equals(showTypeStr)) {
            showType = GroupListShowType.SELECT_TO_SHARE;
        } else {
            showType = GroupListShowType.SHOW;
        }
    }

    @Override
    protected void initView() {
        pinyin_bar.setVisibility(View.VISIBLE);
        pinyin_bar.setOnTouchLetterChangedListener(new PinyinIndexer.OnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String letter) {
                if (imGroupPinyinAdapter == null || null == imGroupPinyinAdapter.getKeyMapList()) {
                    LogUtils.e("群组数据为空");
                    return;
                }
                for (int i = 0, j = imGroupPinyinAdapter.getKeyMapList().getTypes().size(); i < j; i++) {
                    String str = imGroupPinyinAdapter.getKeyMapList().getTypes().get(i);
                    if (letter.toUpperCase().equals(str.toUpperCase())) {
                        /**跳转到选中的字母表*/
                        elv_groups.setSelectedGroup(i);
                    }
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ContactService.getInstance().loadGroupDataFromNet(new CommonResponse<List<GroupModel>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(List<GroupModel> groupModels) {
                initOrUpdateData(groupModels);
            }
        });
    }

    @Override
    protected void initData() {
        titleBarBuilder.setTitleText("群组");
        List<GroupModel> groupModelList = ContactService.getInstance().getGroupList();

        initOrUpdateData(groupModelList);

    }

    private void initOrUpdateData(List<GroupModel> groupModelList) {
        if (null == groupModelList) {
            shortToast("群列表数据为空");
            return;
        }
        groupBeanList.clear();
        for (GroupModel groupBean : groupModelList) {
            groupBeanList.add(groupBean.toGroupBean());
        }

        elv_groups.setOnGroupClickListener(null);

        if (null == groupBeanList || groupBeanList.isEmpty()) {
            LogUtils.e("群组列表为空");
            shortToast("群组列表为空");
        } else {
            initOrUpdateAdapter(groupBeanList);
        }

        final GroupFilter groupFilter = new GroupFilter(groupBeanList);

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

    /**
     * 初始化 或更新 Adapter
     *
     * @param groupBeanList
     */
    private void initOrUpdateAdapter(List<GroupBean> groupBeanList) {
        if (null == imGroupPinyinAdapter) {
            ItemClickCallback itemClickCallback = new ItemClickCallback<GroupBean>() {
                @Override
                public void onItemClick(GroupBean groupBean) {
                    if (GroupListShowType.SHOW == showType) {
                        ChatMainActivity.start(GroupListActivity.this, groupBean.getGroupId(), groupBean.getContactName(), groupBean.getHeadUrl(), true);
                        return;
                    } else if (GroupListShowType.SELECT_TO_SHARE == showType) {
                        Intent intent = new Intent();
                        intent.putExtra(GROUP_ID_INTENT_KEY, groupBean.getGroupId());
                        intent.putExtra(GROUP_NAME_INTENT_KEY, groupBean.getContactName());
                        intent.putExtra(GROUP_HEAD_URL_INTENT_KEY, groupBean.getHeadUrl());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }

                @Override
                public void onItemDelete(GroupBean groupBean) {

                }
            };
            imGroupPinyinAdapter = new ImGroupPinyinAdapter(elv_groups, groupBeanList, itemClickCallback);
        } else {
            imGroupPinyinAdapter.setListData(groupBeanList);
            imGroupPinyinAdapter.notifyDataSetChanged();
        }
        //展开群组视图
        imGroupPinyinAdapter.expandGroup();
    }


    class GroupFilter extends SearchFilter<GroupBean> {

        public GroupFilter(List<GroupBean> myList) {
            super(myList);
        }

        @Override
        protected String getSearchKeyFiledValue(final GroupBean item) {
            LogUtils.d("search_log:item");
            if (null == item) {
                return "";
            }
            return TextUtils.isEmpty(item.getContactName()) ? "" : item.getContactName();

        }

        @Override
        protected void updateData(final List<GroupBean> list) {
            LogUtils.d("search_log:updateData" + list);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initOrUpdateAdapter(list);
                }
            });

        }
    }


}
