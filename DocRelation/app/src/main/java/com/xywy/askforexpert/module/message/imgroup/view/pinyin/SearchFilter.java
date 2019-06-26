package com.xywy.askforexpert.module.message.imgroup.view.pinyin;

import android.widget.Filter;

import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.module.message.utils.Trans2PinYin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bailiangjin on 16/7/12.
 */
public abstract class SearchFilter<T> extends Filter {
    List<T> orgList = new ArrayList<>();
    List<T> resultList = new ArrayList<>();

    public SearchFilter(List<T> myList) {
        this.orgList.clear();
        if (null != myList && !myList.isEmpty()) {
            this.orgList.addAll(myList);
        }
    }

    @Override
    protected synchronized FilterResults performFiltering(CharSequence prefix) {
        FilterResults filterResults = new FilterResults();
        //清空搜索结果
        resultList.clear();
        if (prefix == null || prefix.length() == 0) {
            filterResults.values = orgList;
            filterResults.count = orgList.size();
        } else {
            Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher matcher = pattern.matcher(prefix);

            final int orgListSize = orgList.size();
            for (int i = 0; i < orgListSize; i++) {
                final T data = orgList.get(i);
                String orgName = getSearchKeyFiledValue(data);
                String pinyinName = Trans2PinYin.trans2PinYin(orgName).toUpperCase();
                String prefixString = Trans2PinYin.trans2PinYin(prefix.toString())
                        .toUpperCase();
                if (!matcher.matches()) {
                    if (pinyinName.startsWith(prefixString)) {
                        resultList.add(data);
                    } else {
                        final String[] words = pinyinName.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with
                        // space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                resultList.add(data);
                                break;
                            }
                        }
                    }
                } else {
                    if (pinyinName.startsWith(prefixString)
                            && orgName.startsWith(
                            prefix.toString())) {
                        resultList.add(data);
                    } else {
                        final String[] words = pinyinName.split(" ");
                        final String[] words1 = orgName.split("");
                        final int wordCount = words.length;
                        // Start at index 0, in case valueText starts with
                        // space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)
                                    && words1[k].startsWith(prefix
                                    .toString())) {
                                resultList.add(data);
                                break;
                            }
                        }
                    }

                }

            }
            filterResults.values = resultList;
            filterResults.count = resultList.size();
        }
        LogUtils.d("contacts filter results size: " + filterResults.count);
        return filterResults;
    }

    @Override
    protected synchronized void publishResults(CharSequence constraint, FilterResults results) {
        if (results.values != null) {
            //处理数据
            updateData((List<T>) results.values);
        }
    }


    /**
     * 获取搜索关键词 字段
     *
     * @param item
     * @return
     */
    protected abstract String getSearchKeyFiledValue(T item);

    /**
     * 更新数据回调
     *
     * @param list
     */
    protected abstract void updateData(List<T> list);
}
