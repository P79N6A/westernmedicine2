/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xywy.askforexpert.module.message.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.google.gson.Gson;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.ShareCardInfo;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.message.imgroup.ContactService;
import com.xywy.askforexpert.module.message.utils.Trans2PinYin;
import com.xywy.easeWrapper.EaseConstant;
import com.xywy.easeWrapper.utils.SmileUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 显示所有聊天记录adpater
 */
public class ChatAllHistoryAdapter extends ArrayAdapter<EMConversation> {

    private static final String TAG = "ChatAllHistoryAdapter";
    ImageLoader instance;
    DisplayImageOptions options;
    DisplayImageOptions options2;
    private LayoutInflater inflater;
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;
    private ConversationFilter conversationFilter;
    private boolean notiyfyByFilter;
    private SharedPreferences sharedPreferences;

    public ChatAllHistoryAdapter(Context context, int textViewResourceId, List<EMConversation> objects) {
        super(context, textViewResourceId, objects);
        this.conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.addAll(objects);
        inflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).showStubImage(R.drawable.icon_photo_def).showImageForEmptyUri(R.drawable.icon_photo_def).showImageOnFail(R.drawable.icon_photo_def).cacheInMemory(true).cacheOnDisc(true).build();

        options2 = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).showStubImage(R.drawable.group_icon).showImageForEmptyUri(R.drawable.group_icon).showImageOnFail(R.drawable.group_icon).cacheInMemory(true).cacheOnDisc(true).build();
        instance = ImageLoader.getInstance();

        sharedPreferences = context.getSharedPreferences("save_user", Context.MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_item_layout = (RelativeLayout) convertView.findViewById(R.id.list_item_layout);
            holder.tv_red = (ImageView) convertView.findViewById(R.id.tv_red);
            convertView.setTag(holder);
        }
        // if (position % 2 == 0) {
        // holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem);
        // } else {
        // holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem_grey);
        // }

        // 获取与此用户/群组的会话
        EMConversation conversation = getItem(position);
        // 获取用户username或者群组groupid
        String username = conversation.getUserName();

        //// TODO: 16/8/16 shijiazi maybe unused code --start
//        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
//        EMContact contact = null;
//        boolean isGroup = false;
//        for (EMGroup group : groups) {
//            if (group.getGroupId().equals(username)) {
//                isGroup = true;
//                contact = group;
//                break;
//            }
//        }
        //// TODO: 16/8/16 shijiazi maybe unused code --end


        // if (isGroup)
        // {
        // // 群聊消息，显示群聊头像
        // holder.avatar.setImageResource(R.drawable.group_icon);
        // holder.name.setText(contact.getNick() != null ? contact.getNick()
        // : username);
        // } else
        // {
        // UserUtils.setUserAvatar(getContext(), username, holder.avatar);
        // if (username.equals(EaseConstant.GROUP_USERNAME))
        // {
        // holder.name.setText("群聊");
        //
        // } else if (username.equals(EaseConstant.NEW_FRIENDS_USERNAME))
        // {
        // holder.name.setText("申请与通知");
        // }
        // holder.name.setText(username);
        // }

        DLog.i(TAG, "username==" + username);

        if (username.contains("消息小助手")) {
            holder.avatar.setImageResource(R.drawable.message_icon);
            holder.name.setText("消息小助手");
        } else if (username.contains("病例夹")) {
            holder.avatar.setImageResource(R.drawable.dossier);
        } else if (username.contains("订阅媒体号")) {
            holder.avatar.setImageResource(R.drawable.media_msglist_icon);
        }  else if (username.contains("网站公告")) {
            holder.avatar.setImageResource(R.drawable.wangzhangonggao_2x);
            holder.name.setText("网站公告");
        } else {
            if (conversation.getLastMessage().direct() == EMMessage.Direct.SEND || conversation.isGroup()) {
                // conversation.getLastMessage().getStringAttribute("toRealName");
                //				fb.display(holder.avatar, conversation.getLastMessage()
                //						.getStringAttribute("toAvatar", ""));
                //				fb.configLoadfailImage(R.drawable.icon_photo_def);
                if (conversation.isGroup()) {
                    instance.displayImage(conversation.getLastMessage().getStringAttribute("toAvatar", ""), holder.avatar, options2);
                } else {
                    instance.displayImage(conversation.getLastMessage().getStringAttribute("toAvatar", ""), holder.avatar, options);
                }

                String toRealName = conversation.getLastMessage().getStringAttribute("toRealName", "");
                if (TextUtils.isEmpty(toRealName)) {
                    username = username.replaceAll("did_", "");
                    username = username.replaceAll("uid_", "");
                    username = username.replaceAll(Constants.QXYL_USER_HXID_MARK, "");
                    holder.name.setText("用户" + username);
                } else {
                    String groupName = ContactService.getInstance().getGroupName(conversation.getUserName());
                    DLog.d(TAG, "SEND chat history name = " + toRealName + ", group name = " + groupName);
                    DLog.d(TAG, "SEND username = " + conversation.getUserName() + ", is group = " + conversation.isGroup());
                    if (conversation.isGroup() && groupName != null && !TextUtils.isEmpty(groupName)) {
                        holder.name.setText(groupName);
                    } else {
                        holder.name.setText(toRealName);
                    }
                }

            } else {
                //				fb.display(holder.avatar, conversation.getLastMessage()
                //						.getStringAttribute("fromAvatar", ""));
                //				fb.configLoadfailImage(R.drawable.icon_photo_def);

                DLog.i(TAG, "接受消息" + conversation.getLastMessage().getStringAttribute("fromAvatar", "") +
                        "===" + conversation.getLastMessage().getStringAttribute("fromRealName", "") + "=====" + conversation.getLastMessage().getStringAttribute("newMsgType", "") + "=====" + conversation.getLastMessage().getStringAttribute("msgBody", ""));
                DLog.i(TAG, "生日祝福" + conversation.getLastMessage().getStringAttribute("em_apns_ext", "默认空"));
                if (conversation.isGroup()) {
                    instance.displayImage(conversation.getLastMessage().getStringAttribute("toAvatar", ""), holder.avatar, options2);
                } else {
                    instance.displayImage(conversation.getLastMessage().getStringAttribute("fromAvatar", ""), holder.avatar, options);
                }


                if (TextUtils.isEmpty(conversation.getLastMessage().getStringAttribute("fromRealName", ""))) {
                    username = username.replaceAll("did_", "");
                    username = username.replaceAll("uid_", "");
                    username = username.replaceAll(Constants.QXYL_USER_HXID_MARK, "");
                    DLog.d(TAG, "username== after " + username);
                    holder.name.setText("用户" + username);
                } else {
                    DLog.d(TAG, "chat history name = " + conversation.getLastMessage().getStringAttribute("fromRealName", ""));
                    //stone 去掉:
                    if (username.equals(MyConstant.HX_SYSTEM_ID)) {
                        holder.name.setText(conversation.getLastMessage().getStringAttribute("fromRealName", "").replaceAll(":", ""));
                    } else {
                        holder.name.setText(conversation.getLastMessage().getStringAttribute("fromRealName", ""));
                    }
                }

                //				holder.name.setText(conversation.getLastMessage()
                //						.getStringAttribute("fromRealName", ""));
            }
            // holder.avatar.setImageResource(R.drawable.default_avatar);
        }

        if (username.contains("消息小助手")) {
            // String msgnum = YMApplication.msgLists.get(0).getMsgNum();
            String msgnum = YMApplication.msgtotal;
            DLog.i(TAG, "消息小助手" + msgnum);
            if (TextUtils.isEmpty(msgnum) || Integer.valueOf(msgnum) == 0) {
                holder.tv_red.setVisibility(View.GONE);
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            } else {
                //				if(Integer.valueOf(msgnum)>99)
                //				{
                //					holder.unreadLabel.setText(99+"+");
                //				}
                //				else
                //				{
                //					holder.unreadLabel.setText(msgnum);
                //				}
                holder.tv_red.setVisibility(View.VISIBLE);
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            }

        } else if (username.contains("新朋友")) {
            String addFriendNum = YMApplication.addFriendNum;
            DLog.i(TAG, "新朋友" + addFriendNum);
            if (TextUtils.isEmpty(addFriendNum) || Integer.valueOf(addFriendNum) == 0) {
                holder.tv_red.setVisibility(View.GONE);
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            } else {
                holder.unreadLabel.setVisibility(View.VISIBLE);
                holder.unreadLabel.setText(addFriendNum);
            }
        } else if (username.contains("订阅媒体号")) {
            holder.name.setText("订阅媒体号");

            if (YMApplication.getInstance().getMediaList().getData() != null && YMApplication.getInstance().getMediaList().getData().size() > 0) {
                if (YMApplication.getInstance().getMediaList().getData().get(0).getId().equals(sharedPreferences.getString("mediaId", ""))) {
                    holder.tv_red.setVisibility(View.INVISIBLE);
                    holder.unreadLabel.setVisibility(View.INVISIBLE);
                } else {
                    holder.tv_red.setVisibility(View.VISIBLE);
                    holder.unreadLabel.setVisibility(View.INVISIBLE);
                }
            }

        }else if(username.contains("网站公告")){
            if (conversation.getAllMessages().size() != 0) {
                EMMessage lastMessage = conversation.getLastMessage();
                try {
                    int hs_read = lastMessage.getIntAttribute(Constants.HS_READ);
                    if (0==hs_read) {
                        holder.tv_red.setVisibility(View.VISIBLE);
                        holder.unreadLabel.setVisibility(View.INVISIBLE);
                    } else {
                        holder.tv_red.setVisibility(View.GONE);
                        holder.unreadLabel.setVisibility(View.INVISIBLE);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (conversation.getUnreadMsgCount() > 0) {
                if (conversation.getUnreadMsgCount() > 99) {
                    holder.unreadLabel.setText(99 + "+");
                } else {
                    // 显示与此用户的消息未读数
                    holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
                }
                holder.tv_red.setVisibility(View.GONE);
                holder.unreadLabel.setVisibility(View.VISIBLE);
            } else {
                holder.tv_red.setVisibility(View.GONE);
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            }
        }
        if (conversation.getAllMessages().size() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = conversation.getLastMessage();
            String msg_type = lastMessage.getStringAttribute("newMsgType", "");
            String extraJson = conversation.getLastMessage().getStringAttribute("msgBody", "");
            String friendName = "";
            DLog.d(TAG, "msgBody = " + extraJson);
            try {
                Gson gson = new Gson();
                ShareCardInfo shareCardInfo = gson.fromJson(extraJson, ShareCardInfo.class);
                if (shareCardInfo != null) {
                    friendName = shareCardInfo.getFriendName();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            DLog.i(TAG, "分享数据。。。" + lastMessage.getStringAttribute("newMsgType", "") + "..." + lastMessage.getStringAttribute("toAvatar", ""));
            if (!TextUtils.isEmpty(msg_type) & msg_type.equals("share")) {
                if (conversation.getLastMessage().direct() == EMMessage.Direct.SEND) {
                    holder.message.setText("您分享了一个链接");
                } else {
                    holder.message.setText("分享了一个链接给您");
                }
            } else if (!TextUtils.isEmpty(msg_type) & msg_type.equals("shareNameCard")) {
                if (conversation.getLastMessage().direct() == EMMessage.Direct.SEND) {
                    holder.message.setText("您推荐了" + friendName);
                } else {
                    holder.message.setText("向您推荐了" + friendName);
                }
            } else if (!TextUtils.isEmpty(msg_type) & msg_type.equals("seminar")) {
                if (conversation.getLastMessage().direct() == EMMessage.Direct.SEND) {
                    holder.message.setText("您收到了病例研讨班的通知");
                } else {
                    holder.message.setText("分享了一个病例研讨班给您");
                }
            } else if (!TextUtils.isEmpty(msg_type) && msg_type.equals("zixun")) { // 资讯推送
                if (conversation.getLastMessage().direct() == EMMessage.Direct.SEND) {
                    holder.message.setText("您收到了一条资讯通知");
                } else {
                    holder.message.setText("分享了一条资讯给您");
                }
            } else {
                if (getMessageDigest(lastMessage, (this.getContext())) != null) {
                    holder.message.setText(SmileUtils.getSmiledText(getContext(), getMessageDigest(lastMessage, (this.getContext()))), BufferType.SPANNABLE);
                } else {
                    holder.message.setText("");
                }
            }

            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    private String getMessageDigest(EMMessage message, Context context) {
        String digest;
        switch (message.getType()) {
            //            case LOCATION: // 位置消息
            //                if (message.direct() == Direct.RECEIVE) {
            //                    // 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
            //                    // digest = EasyUtils.getAppResourceString(context,
            //                    // "location_recv");
            //                    digest = getStrng(context, R.string.location_recv);
            //                    digest = String.format(digest, message.getFrom());
            //                    return digest;
            //                } else {
            //                    // digest = EasyUtils.getAppResourceString(context,
            //                    // "location_prefix");
            //                    digest = getStrng(context, R.string.location_prefix);
            //                }
            //                break;
            case IMAGE: // 图片消息
                EMImageMessageBody imageBody = (EMImageMessageBody) message.getBody();
                digest = getStrng(context, R.string.picture);
                //                        + imageBody.getFileName();
                break;
            case VOICE:// 语音消息
                digest = "[语音]";
                break;
            //            case VIDEO: // 视频消息
            //                digest = getStrng(context, R.string.video);
            //                break;
            case TXT: // 文本消息
                if (!message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                } else {
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
                }
                break;
            //            case FILE: // 普通文件消息
            //                digest = getStrng(context, R.string.file);
            //                break;
            default:
                //                System.err.println("error, unknow type");
                return "消息类型不支持，请更新至最新版本";
        }

        return digest;
    }

    private static class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        ImageView avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_item_layout;

        ImageView tv_red;
    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }

    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                String prefixString;
                final int count = mOriginalValues.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(prefix);
                    final EMConversation value = mOriginalValues.get(i);
                    // String username = value.getUserName();
                    String username;
                    String copyUsername;
                    if (value.getLastMessage().direct() == EMMessage.Direct.SEND) {
                        // conversation.getLastMessage().getStringAttribute("toRealName");
                        // fb.display(holder.avatar,
                        // conversation.getLastMessage().getStringAttribute("toAvatar",
                        // ""));
                        username = value.getLastMessage().getStringAttribute("toRealName", "");
                        copyUsername = username;
                    } else {
                        // fb.display(holder.avatar,
                        // conversation.getLastMessage().getStringAttribute("fromAvatar",
                        // ""));
                        username = value.getLastMessage().getStringAttribute("fromRealName", "");
                        copyUsername = username;
                    }

                    // EMGroup group = EMGroupManager.getInstance().getGroup(
                    // username);
                    // if (group != null)
                    // {
                    // username = group.getGroupName();
                    // }

                    // First match against the whole ,non-splitted value
                    if (TextUtils.isEmpty(username)) {
                        username = value.getUserName();
                        copyUsername = username;
                    }

                    username = Trans2PinYin.trans2PinYin(username).toUpperCase();

                    prefixString = Trans2PinYin.trans2PinYin(prefix.toString()).toUpperCase();

                    if (!m.matches()) {
                        if (username.startsWith(prefixString)) {
                            newValues.add(value);
                        } else {
                            final String[] words = username.split(" ");
                            final int wordCount = words.length;

                            // start at index 0, in case valueText starts with
                            // space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                    } else {
                        if (username.startsWith(prefixString) && copyUsername.startsWith(prefix.toString())) {
                            newValues.add(value);
                        } else {
                            final String[] words = username.split(" ");
                            final String[] wordscopy = copyUsername.split(" ");
                            final int wordCount = words.length;

                            // start at index 0, in case valueText starts with
                            // space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString) && wordscopy[k].startsWith(prefix.toString())) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            conversationList.clear();
            conversationList.addAll((List<EMConversation>) results.values);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }

    }


}
