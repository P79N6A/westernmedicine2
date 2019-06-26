package com.xywy.askforexpert.module.doctorcircle.topic;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.appcommon.view.dialog.BaseMultiChoiceDialog;

/**
 * A simple {@link Fragment} subclass.
 * 通用自定义布局多选对话框
 */
public class CusMultiChoiceDialog extends BaseMultiChoiceDialog {
    private static final String TAG = "CusMultiChoiceDialog";

    /**
     * @param dialogTitle   标题
     * @param mChoices      所有选项
     * @param mCheckedItems 已选项
     * @param isStrict      是否选择可选个数
     * @param strictNum     最多可选个数
     */
    public static CusMultiChoiceDialog newInstance(String dialogTitle, CharSequence[] mChoices,
                                                   boolean[] mCheckedItems, boolean isStrict, int strictNum) {
        CusMultiChoiceDialog fragment = new CusMultiChoiceDialog();
        SetArgs(fragment, dialogTitle, mChoices, mCheckedItems, isStrict, strictNum);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_multichoice_dialog, null);
        TextView dialogTitle = (TextView) contentView.findViewById(R.id.dialog_title);
        ListView dialogList = (ListView) contentView.findViewById(R.id.dialog_list);
        TextView dialogCancel = (TextView) contentView.findViewById(R.id.dialog_cancel);
        TextView dialogOk = (TextView) contentView.findViewById(R.id.dialog_ok);

        dialogTitle.setText(this.dialogTitle);

        final DialogListAdapter adapter = new DialogListAdapter(getActivity(), R.layout.multi_choice_list_item);
        dialogList.setAdapter(adapter);
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isStrict) {
                    if (mSelectedItems.contains(position)) {
                        mSelectedItems.remove(Integer.valueOf(position));
                        updateCheckedItems();
                        adapter.notifyDataSetChanged();
                    } else {
                        if (mSelectedItems.size() >= mStrictNum) {
                            Toast.makeText(getActivity(), "最多选中" + mStrictNum + "个分类", Toast.LENGTH_SHORT).show();
                        } else {
                            mSelectedItems.add(position);
                            updateCheckedItems();
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (mSelectedItems.contains(position)) {
                        mSelectedItems.remove(Integer.valueOf(position));
                        updateCheckedItems();
                        adapter.notifyDataSetChanged();
                    } else {
                        mSelectedItems.add(position);
                        updateCheckedItems();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLog.d(TAG, "ok mSelectedItems size = " + mSelectedItems.size());
                OkClicked();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(contentView);

        return builder.create();
    }

    class DialogListAdapter extends MyBaseAdapter {

        public DialogListAdapter(Context context, int itemLayoutId) {
            super(context, itemLayoutId);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false);
            }

            TextView text = CommonViewHolder.getView(convertView, R.id.dialog_list_text);
            ImageView selected = CommonViewHolder.getView(convertView, R.id.dialog_selected);

            if (mChoices != null && mChoices.length > 0 && position < mChoices.length) {
                text.setText(mChoices[position]);
            }
            if (mCheckedItems != null && mCheckedItems.length > 0 && position < mCheckedItems.length) {
                DLog.d(TAG, "mCheckedItems[position] = " + mCheckedItems[position]);
                if (mCheckedItems[position]) {
                    selected.setVisibility(View.VISIBLE);
                } else {
                    selected.setVisibility(View.GONE);
                }
            }

            return convertView;
        }
    }
}
