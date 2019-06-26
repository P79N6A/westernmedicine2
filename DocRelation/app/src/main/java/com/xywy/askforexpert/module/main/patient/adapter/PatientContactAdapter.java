package com.xywy.askforexpert.module.main.patient.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hyphenate.util.EMLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.PatientListInfo;
import com.xywy.askforexpert.module.message.utils.Trans2PinYin;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的好友Adapter实现
 */
public class PatientContactAdapter extends ArrayAdapter<PatientListInfo> implements SectionIndexer {
    private static final String TAG = "ContactAdapter";
    List<String> list;
    List<PatientListInfo> userList;
    List<PatientListInfo> copyUserList;
    FinalBitmap fb;
    ImageLoader instance;
    DisplayImageOptions options;
    private LayoutInflater layoutInflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private int res;
    private MyFilter myFilter;
    private boolean notiyfyByFilter;

    public PatientContactAdapter(Context context, int resource, List<PatientListInfo> objects) {
        super(context, resource, objects);
        this.res = resource;
        this.userList = objects;
        copyUserList = new ArrayList<PatientListInfo>();
        copyUserList.addAll(objects);
        layoutInflater = LayoutInflater.from(context);
        fb = FinalBitmap.create(getContext(), false);
        options = new Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showStubImage(R.drawable.icon_photo_def)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def).cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        instance = ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.row_patient, null);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.nameTextview = (TextView) convertView.findViewById(R.id.name);
            holder.tvHeader = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PatientListInfo user = getItem(position);
//		if(user == null)
//			Log.d("ContactAdapter", position + "");
        //设置nick，demo里不涉及到完整user，用username代替nick显示
        String username = user.getRealname();
        String header = user.getHeader();
//		if (position == 0 || header != null && !header.equals(getItem(position - 1).getHeader())) {
//			if ("".equals(header)) {
//			    holder.tvHeader.setVisibility(View.GONE);
//			} else {
//			    holder.tvHeader.setVisibility(View.VISIBLE);
//			    holder.tvHeader.setText(header);
//			}
//		} else {
//		    holder.tvHeader.setVisibility(View.GONE);
//		}

//		//显示申请与通知item
//		if(username.equals(EaseConstant.NEW_FRIENDS_USERNAME)){
//		    holder.nameTextview.setText(user.getNick());
//		    holder.avatar.setImageResource(R.drawable.new_friends_icon);
//			if(user.getUnreadMsgCount() > 0){
//			    holder.unreadMsgView.setVisibility(View.VISIBLE);
//			    holder.unreadMsgView.setText(user.getUnreadMsgCount()+"");
//			}else{
//			    holder.unreadMsgView.setVisibility(View.INVISIBLE);
//			}
//		}else if(username.equals(EaseConstant.GROUP_USERNAME)){
//			//群聊item
//		    holder.nameTextview.setText(user.getNick());
//		    holder.avatar.setImageResource(R.drawable.groups_icon);
//		}else{
        holder.nameTextview.setText(username);
        //设置用户头像
//			UserUtils.setUserAvatar(getContext(), username, holder.avatar);
//			if(holder.unreadMsgView != null)
//			    holder.unreadMsgView.setVisibility(View.INVISIBLE);
//		    holder.avatar.setTag(user.getPhoto());
//		    if(holder.avatar.getTag()!=null&&holder.avatar.getTag().equals(user.getPhoto()))
//		    {
//		    	 fb.display(holder.avatar , user.getPhoto());
//					fb.configLoadfailImage(R.drawable.icon_photo_def);
//		    }

        instance.displayImage(user.getPhoto(), holder.avatar, options);
//		}

        return convertView;
    }

    @Override
    public PatientListInfo getItem(int position) {
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
            //		System.err.println("contactadapter getsection getHeader:" + letter + " name:" + getItem(i).getRealname());
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyUserList.clear();
            copyUserList.addAll(userList);
        }
    }

    private static class ViewHolder {
        ImageView avatar;
        TextView unreadMsgView;
        TextView nameTextview;
        TextView tvHeader;
    }

    private class MyFilter extends Filter {
        List<PatientListInfo> mOriginalList = null;

        public MyFilter(List<PatientListInfo> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalList == null) {
                mOriginalList = new ArrayList<PatientListInfo>();
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
                final ArrayList<PatientListInfo> newValues = new ArrayList<PatientListInfo>();
                for (int i = 0; i < count; i++) {
                    final PatientListInfo user = mOriginalList.get(i);
//					String username ;
                    String prefixString;
//					if(m.matches())
//					{
//						username = user.getHeader();
//						prefixString = prefix.toString().toUpperCase();
//						break;
//					}
//					else
//					{
//						username = user.getRealname();
//						prefixString = prefix.toString();
//					}
                    String username = Trans2PinYin.trans2PinYin(
                            user.getRealname()).toUpperCase();

                    prefixString = Trans2PinYin.trans2PinYin(prefix.toString())
                            .toUpperCase();
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
                                && user.getRealname().startsWith(
                                prefix.toString())) {
                            newValues.add(user);
                        } else {
                            final String[] words = username.split(" ");
                            final String[] words1 = user.getRealname()
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
            EMLog.d(TAG, "contacts filter results size: " + results.count);
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                                                   FilterResults results) {
            userList.clear();
            userList.addAll((List<PatientListInfo>) results.values);
            EMLog.d(TAG, "publish contacts filter results size: " + results.count);
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
