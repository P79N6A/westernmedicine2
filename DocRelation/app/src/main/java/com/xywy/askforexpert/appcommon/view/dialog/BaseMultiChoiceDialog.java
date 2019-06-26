package com.xywy.askforexpert.appcommon.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.xywy.askforexpert.module.doctorcircle.topic.UpdateSelectedItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/7/21 17:33
 */
public class BaseMultiChoiceDialog extends AppCompatDialogFragment {
    protected static final String DIALOG_TITLE_ARGS_KEY = "dialogTitle";
    protected static final String MULTI_CHOICES_ARGS_KEY = "multiChoices";
    protected static final String MULTI_CHECKED_ITEMS = "checkedItems";
    protected static final String IS_STRICT_ARGS_KEY = "isStrict";
    protected static final String STRICT_NUM_ARGS_KEY = "strictNum";

    /**
     * 对话框title
     */
    protected String dialogTitle;

    /**
     * 选择项
     */
    protected CharSequence[] mChoices;

    /**
     * 已选择的item
     */
    protected boolean[] mCheckedItems;

    /**
     * 是否限制选择的数量
     */
    protected boolean isStrict;

    /**
     * 最多可选择的数量
     */
    protected int mStrictNum = Integer.MAX_VALUE;

    /**
     * 保存已选择的项
     */
    protected List<Integer> mSelectedItems = new ArrayList<>();

    protected UpdateSelectedItems onUpdateSelectedItems;

    public BaseMultiChoiceDialog() {
        // Required empty public constructor
    }

    /**
     * @param dialogTile        对话框标题
     * @param multiChoicesArray 可供选择的项目
     * @param checkedItems      已选择的item
     * @param isStrict          是否需要限制选择的个数
     * @param strictNum         最多可选择的个数
     */
    protected static void SetArgs(BaseMultiChoiceDialog fragment, String dialogTile, CharSequence[] multiChoicesArray,
                                  boolean[] checkedItems, boolean isStrict, int strictNum) {
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE_ARGS_KEY, dialogTile);
        args.putCharSequenceArray(MULTI_CHOICES_ARGS_KEY, multiChoicesArray);
        args.putBooleanArray(MULTI_CHECKED_ITEMS, checkedItems);
        args.putBoolean(IS_STRICT_ARGS_KEY, isStrict);
        args.putInt(STRICT_NUM_ARGS_KEY, strictNum);
        fragment.setArguments(args);
    }

    /**
     * 设置已选项目更新监听
     */
    public void setOnUpdateSelectedItems(UpdateSelectedItems onUpdateSelectedItems) {
        this.onUpdateSelectedItems = onUpdateSelectedItems;
    }

    public List<Integer> getSelectedItems() {
        return mSelectedItems;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        this.dialogTitle = arguments.getString(DIALOG_TITLE_ARGS_KEY);
        this.mChoices = arguments.getCharSequenceArray(MULTI_CHOICES_ARGS_KEY);
        this.mCheckedItems = arguments.getBooleanArray(MULTI_CHECKED_ITEMS);
        this.isStrict = arguments.getBoolean(IS_STRICT_ARGS_KEY);
        this.mStrictNum = arguments.getInt(STRICT_NUM_ARGS_KEY);

        if (this.dialogTitle == null || this.dialogTitle.equals("")) {
            this.dialogTitle = "MAKE YOUR CHOICE...";
        }
        if (this.mChoices == null) {
            this.mChoices = new CharSequence[]{};
        }
        if (this.mStrictNum <= 0) {
            this.mStrictNum = this.mChoices.length;
        }

        if (this.mCheckedItems != null && this.mCheckedItems.length > 0) {
            for (int i = 0; i < this.mCheckedItems.length; i++) {
                if (this.mCheckedItems[i]) {
                    this.mSelectedItems.add(i);
                }
            }
        }
    }

    protected synchronized void updateCheckedItems() {
        if (this.mCheckedItems != null && this.mCheckedItems.length > 0) {
            for (int i = 0; i < this.mCheckedItems.length; i++) {
                this.mCheckedItems[i] = false;
            }
        }

        if (this.mSelectedItems != null && !this.mSelectedItems.isEmpty()) {
            for (int i = 0; i < this.mSelectedItems.size(); i++) {
                this.mCheckedItems[this.mSelectedItems.get(i)] = true;
            }
        }
    }

    protected void OkClicked() {
        if (onUpdateSelectedItems != null) {
            onUpdateSelectedItems.updateSelectedItems();
        }
        if (mSelectedItems.isEmpty()) {
            Toast.makeText(getActivity(), "至少选择一个分类", Toast.LENGTH_SHORT).show();
        }
        getDialog().dismiss();
    }

    protected abstract class MyBaseAdapter extends BaseAdapter {
        protected Context mContext;
        protected int mItemLayoutId;

        public MyBaseAdapter(Context context, int itemLayoutId) {
            this.mContext = context;
            this.mItemLayoutId = itemLayoutId;
        }

        @Override
        public int getCount() {
            return mChoices == null ? 0 : mChoices.length;
        }

        @Override
        public Object getItem(int position) {
            return mChoices == null ? null : mChoices[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public abstract View getView(int position, View convertView, ViewGroup parent);
    }
}
