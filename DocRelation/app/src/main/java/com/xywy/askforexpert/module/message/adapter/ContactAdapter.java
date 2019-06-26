package com.xywy.askforexpert.module.message.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hyphenate.util.EMLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.module.message.utils.Trans2PinYin;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的好友Adapter实现
 */
public class ContactAdapter extends ArrayAdapter<AddressBook>
        implements SectionIndexer {
    private static final String TAG = "ContactAdapter";
    public List<AddressBook> userList;
    List<String> list;
    List<AddressBook> copyUserList = new ArrayList<AddressBook>();
    private LayoutInflater layoutInflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private int res;
    private MyFilter myFilter;
    private boolean notiyfyByFilter;
    private FinalBitmap fb;
    private ImageLoader instance;
    private DisplayImageOptions options;
    private int type = 0;

    public ContactAdapter(Context context, int resource,
                          List<AddressBook> objects) {
        super(context, resource, objects);
        this.res = resource;
        this.userList = objects;
        copyUserList.addAll(objects);
        layoutInflater = LayoutInflater.from(context);
        fb = FinalBitmap.create(getContext(), false);
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        instance = ImageLoader.getInstance();
    }

    public void setData(List<AddressBook> objects) {
        this.userList = objects;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.row_contact, parent, false);
            holder.tvHeader = (TextView) convertView.findViewById(R.id.header);
            holder.defaultLayout = (RelativeLayout) convertView.findViewById(R.id.default_layout);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.nameTextview = (TextView) convertView.findViewById(R.id.name);

            holder.healthyLayout = (RelativeLayout) convertView.findViewById(R.id.healthy_user_list_item_layout);
            holder.healthyAvatar = (ImageView) convertView.findViewById(R.id.healthy_user_list_item_avatar);
            holder.healthyName = (TextView) convertView.findViewById(R.id.healthy_user_list_item_name);
            holder.healthyGeder = (TextView) convertView.findViewById(R.id.healthy_user_list_item_gender);
            holder.healthyAge = (TextView) convertView.findViewById(R.id.healthy_user_list_item_age);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (type == 4) {
            holder.defaultLayout.setVisibility(View.GONE);
            holder.healthyLayout.setVisibility(View.VISIBLE);
        } else {
            holder.defaultLayout.setVisibility(View.VISIBLE);
            holder.healthyLayout.setVisibility(View.GONE);
        }

        AddressBook user = getItem(position);
        LogUtils.d("AddressBook:User:" + GsonUtils.toJson(user));
        // if(user == null)
        // Log.d("ContactAdapter", position + "");
        // 设置nick，demo里不涉及到完整user，用username代替nick显示
        String username = getRealname(user);
        String header = user.getHeader();
        if (position == 0 || header != null
                && !header.equals(getItem(position - 1).getHeader())) {
            if ("".equals(header)) {
                holder.tvHeader.setVisibility(View.GONE);
            } else {
                DLog.d(TAG, "header = " + header);
                holder.tvHeader.setVisibility(View.VISIBLE);
                holder.tvHeader.setText(header);
            }
        } else {
            holder.tvHeader.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(username)) {
            if (type != 4) {
                holder.nameTextview.setText("用户" + user.getHxusername());
            }
        } else {
            if (type == 4) {
                holder.healthyName.setText(user.getXm());
            } else {
                holder.nameTextview.setText(username);
            }
        }

        if (type == 4) {
            instance.displayImage(user.getTxdz(), holder.healthyAvatar, options);
            holder.healthyGeder.setText(user.getXb());
            String nl = user.getNl();
            if (nl == null || nl.equals("")) {
                holder.healthyAge.setVisibility(View.GONE);
            } else {
                holder.healthyAge.setVisibility(View.VISIBLE);
                holder.healthyAge.setText(nl + "岁");
            }
        } else {
            switch (user.getPhoto()) {
                case "媒体号":
                    holder.avatar.setImageResource(R.drawable.media_msg_icon);
                    break;

                case "服务号":
                    holder.avatar.setImageResource(R.drawable.service_num_icon);
                    break;
                case "群组":
                    holder.avatar.setImageResource(R.drawable.icon_group);
                    break;

                default:
                    instance.displayImage(user.getPhoto(), holder.avatar, options);
                    break;
            }
        }
        return convertView;
    }

    @Override
    public AddressBook getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public int getPositionForSection(int section) {
        return positionOfSection.get(section);
    }

    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<String>();
        list.add("点我");
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 1; i < count; i++) {

            String letter = getItem(i).getHeader();
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter(userList);
        }
        return myFilter;
    }

    private String getRealname(AddressBook user) {
        if (type == 4) {
            return user.getXm();
        }

        return user.getRealname();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyUserList.clear();
            copyUserList.addAll(userList);
        }
    }

    private static class ViewHolder {
        TextView tvHeader;

        RelativeLayout defaultLayout;
        ImageView avatar;
        TextView unreadMsgView;
        TextView nameTextview;

        RelativeLayout healthyLayout;
        ImageView healthyAvatar;
        TextView healthyName;
        TextView healthyGeder;
        TextView healthyAge;
    }

    private class MyFilter extends Filter {
        List<AddressBook> mOriginalList = null;

        public MyFilter(List<AddressBook> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(
                CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalList == null) {
                mOriginalList = new ArrayList<>();
            }
            EMLog.d(TAG, "contacts original size: " + mOriginalList.size());
            EMLog.d(TAG, "contacts copy size: " + copyUserList.size());

            if (prefix == null || prefix.length() == 0) {
                results.values = copyUserList;
                results.count = copyUserList.size();
            } else {
                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher m = p.matcher(prefix);

                final int count = mOriginalList.size();
                final ArrayList<AddressBook> newValues = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    final AddressBook user = mOriginalList.get(i);

                    String prefixString;
                    // if(m.matches())
                    // {
                    String username = Trans2PinYin.trans2PinYin(getRealname(user)).toUpperCase();

                    prefixString = Trans2PinYin.trans2PinYin(prefix.toString())
                            .toUpperCase();
                    // }
                    // else
                    // {
                    // username = user.getRealname();
                    // prefixString = prefix.toString();
                    // }
                    //
                    if (!m.matches()) {
                        if (username.startsWith(prefixString)) {
                            newValues.add(user);
                        } else {
                            final String[] words = username.split(" ");
                            final int wordCount = words.length;

                            // Start at index 0, in case valueText starts with
                            // space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)) {
                                    newValues.add(user);
                                    break;
                                }
                            }
                        }
                    } else {
                        if (username.startsWith(prefixString)
                                && getRealname(user).startsWith(
                                prefix.toString())) {
                            newValues.add(user);
                        } else {
                            final String[] words = username.split(" ");
                            final String[] words1 = getRealname(user)
                                    .split("");
                            final int wordCount = words.length;

                            // Start at index 0, in case valueText starts with
                            // space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)
                                        & words1[k].startsWith(prefix
                                        .toString())) {
                                    newValues.add(user);
                                    break;
                                }
                            }
                        }

                    }

                }

                results.values = newValues;
                results.count = newValues.size();
            }
            DLog.i(TAG, "contacts filter results size: " + results.count);
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                                                   FilterResults results) {
            userList.clear();
            if (results.values != null) {
                userList.addAll((List<AddressBook>) results.values);
            }
            DLog.i(TAG, "publish contacts filter results size: "
                    + results.count);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
                notiyfyByFilter = false;
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}
