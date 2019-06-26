package com.xywy.askforexpert.module.message.imgroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.im.group.ContactModel;
import com.xywy.askforexpert.model.im.group.GroupModel;
import com.xywy.askforexpert.model.im.hxuser.HxUserEntity;
import com.xywy.askforexpert.module.message.msgchat.ChatSendMessageHelper;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.retrofit.net.BaseData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Subscriber;

/**
 * Created by bobby on 16/6/17.
 */
public class ContactService {
    private static final String GroupIdKey = "groupid";
    private static final String GroupNameKey = "groupname";
    private static final String GroupIdKeyOfRealm = "groupId";
    private static final String ContactIdKeyOfRealm = "userId";
    private static ContactService instance;
    private RealmConfiguration realmConfig;
    private Realm realm;
    private Object groupObj = new Object();
    private Object contactObj = new Object();
    private String mUserId;
    private ConcurrentMap<String, GroupModel> mGroupList = new ConcurrentHashMap<>();//讨论组数据
    private ConcurrentMap<String, ContactModel> mAddressBookList = new ConcurrentHashMap<>();//通讯录数据

    private ContactService() {
    }

    public static ContactService getInstance() {
        if (instance == null) {
            instance = new ContactService();
        }
        return instance;
    }

    public void reset() {
        mGroupList.clear();
        mAddressBookList.clear();
    }

    public void init(String userId) {
        //        if(TextUtils.isEmpty(userId) || userId.equals(mUserId)) {
        //            return;
        //        }
        mUserId = userId;
        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(YMApplication.getAppContext()).deleteRealmIfMigrationNeeded().name(String.valueOf(userId)).build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        //初始化讨论组
        initGroupData();
        initAddressBookData();
    }


    private void initGroupData() {

        synchronized (groupObj) {
            //数据库读取
            if (realm != null) {
                List<GroupModel> groups = realm.where(GroupModel.class).findAll();
                LogUtils.d("groupList:" + groups.size());
                convertGroupHashMap(groups);
            }

            loadGroupDataFromNet();
        }
    }


    private void updateGroupData(final List<GroupModel> list) {

        synchronized (groupObj) {

            convertGroupHashMap(list);
            if (list == null || list.isEmpty()) {
                return;
            }
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //update整张表
                    realm.delete(GroupModel.class);
                    realm.copyToRealmOrUpdate(list);
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    //// TODO: 16/6/17
                }
            });
        }
    }

    public void loadGroupDataFromNet(final Subscriber<List<GroupModel>> subscriber) {
        RetrofitServiceProvider.getInstance().getGroupList(new CommonResponse<BaseData<List<GroupModel>>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<List<GroupModel>> listBaseData) {
                LogUtils.d("groupListSize:" + listBaseData.getData().size());
                updateGroupData(listBaseData.getData());
                subscriber.onNext(listBaseData.getData());
            }
        }, mUserId);
    }

    private void loadGroupDataFromNet() {
        RetrofitServiceProvider.getInstance().getGroupList(new CommonResponse<BaseData<List<GroupModel>>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<List<GroupModel>> listBaseData) {
                LogUtils.d("groupListSize:" + listBaseData.getData().size());
                updateGroupData(listBaseData.getData());
            }
        }, mUserId);
    }


    private void initAddressBookData() {
        synchronized (contactObj) {
            if (realm != null) {
                List<ContactModel> contacts = realm.where(ContactModel.class).findAll();
                convertContactHashMap(contacts);
            }

            loadContactDataFromNet();
        }
    }


    private void updateContactData(final List<ContactModel> list) {
        synchronized (groupObj) {

            convertContactHashMap(list);
            if (list == null || list.isEmpty()) {
                return;
            }
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //update整张表
                    realm.delete(ContactModel.class);
                    realm.copyToRealm(list);
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable throwable) {
                    //// TODO: 16/6/17
                }
            });
        }
    }


    public void loadContactDataFromNet() {
        RetrofitServiceProvider.getInstance().getAddressBookList(new CommonResponse<BaseData<List<HxUserEntity>>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<List<HxUserEntity>> listBaseData) {
                LogUtils.d("contactlist:" + GsonUtils.toJson(listBaseData));
                List<HxUserEntity> list = listBaseData.getData();
                if (null == list || list.isEmpty()) {
                    return;
                }
                List<ContactModel> userList = new ArrayList<ContactModel>();
                for (HxUserEntity hxUserEntity : list) {
                    ContactModel contactModel = hxUserEntity.toContactModel();
                    if (hxUserEntity.getType() == 1) {
                        //只添加联系人数据 其他类型不添加
                        userList.add(contactModel);
                    }
                }
                if (null == userList || userList.isEmpty()) {
                    return;
                }
                updateContactData(userList);
            }
        });
    }

    private void convertContactHashMap(List<ContactModel> list) {
        for (ContactModel contact : list) {
            mAddressBookList.put(String.valueOf(contact.getUserId()), contact);
        }
    }

    private void convertGroupHashMap(List<GroupModel> list) {
        for (GroupModel group : list) {
            mGroupList.put(group.getGroupId(), group);
        }
    }


    public List<GroupModel> getGroupList() {
        //数据库读取
        if (realm != null) {
            List<GroupModel> groups = realm.where(GroupModel.class).findAll();
            LogUtils.d("groupList:" + groups.size());
            return groups;
        }
        return null;
    }


    public GroupModel getGroupMemberFromLocal(String groupId) {
        GroupModel group = mGroupList.get(groupId);
        return group;
    }

    public void getGroupMember(String groupId, final Subscriber<GroupModel> subscriber) {
        GroupModel group = mGroupList.get(groupId);
        if (group != null && subscriber != null) {
            subscriber.onNext(group);
        } else if (group == null) {
            group = new GroupModel();
            group.setGroupId(groupId);
        }

        final GroupModel finalGroup = group;
        //get from net
        RetrofitServiceProvider.getInstance().getGroupMembers(new CommonResponse<BaseData<List<ContactModel>>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<List<ContactModel>> listBaseData) {
                if (listBaseData != null) {
                    finalGroup.setMemberList(listBaseData.getData());
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(finalGroup);
                    realm.commitTransaction();
                }

                if (subscriber != null) {
                    subscriber.onNext(finalGroup);
                    subscriber.onCompleted();
                }
            }
        }, groupId);
    }

    /**
     * 从数据库中读取 联系人数据
     *
     * @return
     */
    private List<ContactModel> getContactListFromDB() {
        //数据库读取
        if (realm != null) {
            List<ContactModel> contactModelList = realm.where(ContactModel.class).equalTo("isMyFriend", true).findAll();
            LogUtils.d("contactModelList:" + contactModelList.size());
            return contactModelList;
        }
        return null;
    }

    /**
     * 获取群成员
     *
     * @param subscriber
     */
    public void getContactList(final Subscriber<List<ContactModel>> subscriber) {
        //本地数据库读取数据 首次回调
        List<ContactModel> contactModelList = getContactListFromDB();
        if (null != contactModelList && null != subscriber) {
            //首次回调
            subscriber.onNext(contactModelList);
        }
        //网络获取最新数据
        RetrofitServiceProvider.getInstance().getAddressBookList(new CommonResponse<BaseData<List<HxUserEntity>>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<List<HxUserEntity>> listBaseData) {
                LogUtils.d("contactlist:" + GsonUtils.toJson(listBaseData));
                List<HxUserEntity> list = listBaseData.getData();
                if (null == list || list.isEmpty()) {
                    return;
                }
                List<ContactModel> userList = new ArrayList<ContactModel>();
                for (HxUserEntity hxUserEntity : list) {
                    if (hxUserEntity.getType() == 1) {
                        ContactModel contactModel = hxUserEntity.toContactModel();
                        //只添加联系人数据 其他类型不添加
                        userList.add(contactModel);
                    }
                }
                if (null == userList || userList.isEmpty()) {
                    return;
                }
                if (null != subscriber) {
                    //再次回调
                    subscriber.onNext(userList);
                }
                //更新本地数据
                updateContactData(userList);
            }
        });

    }


    /**
     * 获取群详情
     *
     * @param groupId
     * @param subscriber
     */
    public void getGroupDetail(final String groupId, final Subscriber<GroupModel> subscriber) {
        GroupModel group = mGroupList.get(groupId);
        if (group != null && subscriber != null) {
            subscriber.onNext(group);
        }

        RetrofitServiceProvider.getInstance().getGroupDetail(new CommonResponse<BaseData<GroupModel>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<GroupModel> groupModelBaseData) {
                if (groupModelBaseData != null && groupModelBaseData.getData() != null) {
                    GroupModel group = groupModelBaseData.getData();
                    if (null != group) {
                        group.updateMemberList();
                        //save to db
                        realm.beginTransaction();
                        mGroupList.put(group.getGroupId(), group);
                        realm.copyToRealmOrUpdate(group);
                        realm.commitTransaction();

                        if (subscriber != null && null != group) {
                            subscriber.onNext(group);
                            subscriber.onCompleted();
                        }
                    }

                }
            }
        }, groupId, mUserId);
    }


    /**
     * 添加人
     *
     * @param groupId
     * @param members
     * @param subscriber
     */
    public void inviteToGroup(final String groupId, final String[] members, final Subscriber<GroupModel> subscriber) {
        CommonResponse<BaseData<JsonObject>> resp = new CommonResponse<BaseData<JsonObject>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<JsonObject> groupModelBaseData) {
                GroupModel group = mGroupList.get(groupId);
                LogUtils.d("curGroupModel:" + GsonUtils.toJson(group));
                if (groupModelBaseData != null && groupModelBaseData.getData() != null) {
                    if (group != null) {
                        group.updateMemberList();
                        //save to db
                        realm.beginTransaction();
                        mGroupList.put(group.getGroupId(), group);
                        realm.copyToRealmOrUpdate(group);
                        realm.commitTransaction();
                    }
                }

                if (subscriber != null) {
                    subscriber.onNext(group);

//                    ChatSendMessageHelper.sendCMDMessage(groupId, getGroupList(members) + "已加入本讨论群", group.getContactName(), group.getHeadUrl());
                    ChatSendMessageHelper.saveMessage(groupId, getGroupList(members) + "已加入本讨论群", group.getContactName(), group.getHeadUrl());
                }
            }
        };

        RetrofitServiceProvider.getInstance().inviteToGroup(groupId, members, resp);
    }


    /**
     * 创建群组
     *
     * @param groupName
     * @param members
     * @param subscriber
     */
    public void createGroup(final String groupName, final String[] members, final Subscriber<GroupModel> subscriber) {
        CommonResponse<BaseData<GroupModel>> resp = new CommonResponse<BaseData<GroupModel>>(YMApplication.getAppContext()) {
            @Override

            public void onNext(BaseData<GroupModel> groupModelBaseData) {

                if (groupModelBaseData != null && groupModelBaseData.getData() != null) {
                    GroupModel groupModel = groupModelBaseData.getData();
                    if (null != groupModel) {
                        //save to db
                        realm.beginTransaction();
                        groupModel.updateMemberList();
                        mGroupList.put(groupModel.getGroupId(), groupModel);
                        realm.copyToRealmOrUpdate(groupModel);
                        realm.commitTransaction();

                        if (subscriber != null && null != groupModel) {
                            subscriber.onNext(groupModel);
                            subscriber.onCompleted();

                            Gson gson = new Gson();
                            String content = gson.toJson(userList(members));
//                            ChatSendMessageHelper.sendCMDMessage(groupModel.getGroupId(), content, groupName, groupModel.getHeadUrl(), true);
                            ChatSendMessageHelper.saveMessage(groupModel.getGroupId(), "您已邀请 " + getGroupList(members) + "加入群聊", groupModel.getContactName(), groupModel.getHeadUrl());
                        }

                    }

                }
            }
        };

        RetrofitServiceProvider.getInstance().createGroup(resp, members, mUserId, groupName);
    }

    /**
     * 更改群名称
     *
     * @param groupId
     * @param groupName
     * @param subscriber
     */
    public void changeGroupName(final String groupId, final String groupName, final Subscriber<GroupModel> subscriber) {
        CommonResponse<BaseData<JsonObject>> resp = new CommonResponse<BaseData<JsonObject>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<JsonObject> jsonObjectBaseData) {
                //save db
                realm.beginTransaction();
                GroupModel model = realm.where(GroupModel.class).equalTo(GroupIdKeyOfRealm, groupId).findFirst();
                if (model != null) {
                    model.setContactName(groupName);
                }
                realm.commitTransaction();
                //modify the cache
                GroupModel cachedGroupModel = mGroupList.get(groupId);
                if (cachedGroupModel != null) {
                    cachedGroupModel.setContactName(groupName);
                }

                if (subscriber != null) {
                    subscriber.onNext(model);
                    // 发送透传
//                    ChatSendMessageHelper.sendCMDMessage(model.getGroupId(), "管理员已将群名称修改为" + model.getContactName(), model.getContactName(), model.getHeadUrl());
                    ChatSendMessageHelper.saveMessage(model.getGroupId(), "管理员已将群名称修改为" + model.getContactName(), model.getContactName(), model.getHeadUrl());

                    subscriber.onCompleted();
                }
            }
        };

        RetrofitServiceProvider.getInstance().changeGroupName(resp, groupId, groupName, mUserId);
    }


    /**
     * 设置免打扰 1273
     *
     * @param groupId
     * @param flag       1：免打扰 0：提示
     * @param subscriber
     */
    public void setGroupNoDisturb(final String groupId, final int flag, final Subscriber<GroupModel> subscriber) {
        RetrofitServiceProvider.getInstance().setGroupNoDisturb(new CommonResponse<BaseData<JsonObject>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<JsonObject> jsonObjectBaseData) {

                //modify the cache
                GroupModel model = mGroupList.get(groupId);
                if (model != null) {
                    model.setIsDisturb(flag);
                }
                //save db
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(model);
                realm.commitTransaction();

                if (subscriber != null) {
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                }
            }
        }, groupId, mUserId, flag);
    }

    /**
     * 删除群
     *
     * @param groupId
     * @param masterId
     * @param subscriber
     */
    public void deleteGroup(final String groupId, final String masterId, final Subscriber<Integer> subscriber) {
        RetrofitServiceProvider.getInstance().deleteGroup(groupId, masterId, new CommonResponse<BaseData<JsonObject>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<JsonObject> jsonObjectBaseData) {
                final BaseData<JsonObject> baseData = jsonObjectBaseData;
                int code = baseData.getCode();
                String msg = baseData.getMsg();
                GroupModel group = mGroupList.get(groupId);
                if (10000 == code) {
                    LogUtils.d("删除群成功：" + code + " msg:" + msg);
                    if (group != null) {
                        //delete from cache
                        mGroupList.remove(group);
                        //delete from db
                        realm.beginTransaction();
                        GroupModel model = realm.where(GroupModel.class).equalTo(GroupIdKeyOfRealm, groupId).findFirst();
                        model.deleteFromRealm();
                        realm.commitTransaction();
                    }
                    if (subscriber != null) {
                        subscriber.onNext(code);
                        EMChatManager.getInstance().deleteConversation(groupId, true, true);
                    }
                } else {
                    LogUtils.e("删除群异常：" + code + " msg:" + msg);
                }
            }
        });
    }


    /**
     * 踢人 1331
     *
     * @param groupId
     * @param masterId
     * @param deleteUserId
     * @param subscriber
     */
    public void deleteGroupMember(final String groupId, final String masterId, final String deleteUserId, final Subscriber<Integer> subscriber) {
        RetrofitServiceProvider.getInstance().dealWithGroupMember(groupId, masterId, deleteUserId, new CommonResponse<BaseData<JsonObject>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<JsonObject> jsonObjectBaseData) {
                final BaseData<JsonObject> baseData = jsonObjectBaseData;
                int code = baseData.getCode();
                String msg = baseData.getMsg();
                GroupModel group = mGroupList.get(groupId);
                if (10000 == code) {
                    LogUtils.d("退出群成功：" + code + " msg:" + msg);
                    if (group != null) {
                        //delete from cache
                        mGroupList.remove(group);
                        //delete from db
                        realm.beginTransaction();
                        GroupModel model = realm.where(GroupModel.class).equalTo(GroupIdKeyOfRealm, groupId).findFirst();
                        model.deleteFromRealm();
                        realm.commitTransaction();
                    }

                    if (subscriber != null) {
                        subscriber.onNext(code);
                        //ChatSendMessageHelper.sendCMDMessage(groupId, getUserName(deleteUserId) + "已被管理员移出本讨论群", group.getContactName(), group.getHeadUrl());
                        ChatSendMessageHelper.saveMessage(groupId, getUserName(deleteUserId) + "已被管理员移出本讨论群", group.getContactName(), group.getHeadUrl());
                    }
                } else {
                    LogUtils.e("退出群异常：" + code + " msg:" + msg);
                }
            }
        });
    }

    /**
     * 群主退群 1274
     *
     * @param groupId
     * @param userId
     * @param newOwnerId
     * @param subscriber
     */
    public void existGroup(final String groupId, final String userId, final String newOwnerId, final Subscriber<Integer> subscriber) {
        RetrofitServiceProvider.getInstance().existGroup(groupId, userId, newOwnerId, new CommonResponse<BaseData<JsonObject>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<JsonObject> jsonObjectBaseData) {
                final BaseData<JsonObject> baseData = jsonObjectBaseData;
                int code = baseData.getCode();
                String msg = baseData.getMsg();
                GroupModel group = mGroupList.get(groupId);
                if (10000 == code) {
                    LogUtils.d("退出群成功：" + code + " msg:" + msg);
                    if (group != null) {
                        //delete from cache
                        mGroupList.remove(group);
                        //delete from db
                        realm.beginTransaction();
                        GroupModel model = realm.where(GroupModel.class).equalTo(GroupIdKeyOfRealm, groupId).findFirst();
                        model.deleteFromRealm();
                        realm.commitTransaction();
                    }

                    if (subscriber != null) {
                        subscriber.onNext(code);
                        try {
//                            ChatSendMessageHelper.sendCMDMessage(groupId, getUserName(newOwnerId) + "已成为本群的管理员", group.getContactName(), group.getHeadUrl());
                            EMChatManager.getInstance().deleteConversation(groupId, true, true);
                        } catch (Exception e) {


                            e.printStackTrace();
                        }

                    }
                } else {
                    LogUtils.e("退出群异常：" + code + " msg:" + msg);
                }
            }
        });
    }


    /**
     * 获取名字
     *
     * @param userid
     * @return
     */
    public String getUserName(String userid) {
        ContactModel model = realm.where(ContactModel.class).equalTo("userId", userid).findFirst();
        return model.getUserName();
    }

    public String getGroupList(String[] menber) {
        StringBuffer sb = new StringBuffer();
        String str = "";
        for (int i = 0; i < menber.length; i++) {
            Map<String, String> map = new HashMap<>();
            String id = menber[i];
            String name = getUserName(id);
            sb.append(name + "、");
        }
        if (sb.length() > 0) {
            str = sb.substring(0, sb.length() - 1);
        }
        return str;

    }

    /**
     * 获取群管理员名字
     *
     * @param
     * @return
     */
    public String getGroupOwner(String groupId) {
        GroupModel model = realm.where(GroupModel.class).equalTo("groupId", groupId).findFirst();
        return getUserName(model.getOwner());
    }

    public String getGroupName(String groupId) {
        //stone 判空
        if (realm != null) {
            GroupModel model = realm.where(GroupModel.class).equalTo("groupId", groupId).findFirst();
            return model != null ? model.getContactName() : "";
        }
        return "";
    }

    public String getGroupImg(String groupId) {
        GroupModel model = realm.where(GroupModel.class).equalTo("groupId", groupId).findFirst();
        return model != null ? model.getHeadUrl() : "";
    }

    public GroupModel getGroupInfo(String groupid) {
        GroupModel model = mGroupList.get(groupid);
        return model;
    }

    /**
     * 修改群信息
     *
     * @param finalGroup
     */
    public void updateGroupinfo(GroupModel finalGroup) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(finalGroup);
        realm.commitTransaction();
    }

    /**
     * 修改群名称
     *
     * @param groupName
     */
    public void updateGroupName(String groupId, String groupName) {
        realm.beginTransaction();
        GroupModel groupModel = getGroupInfo(groupId);
        if (null == groupModel) {
            groupModel = new GroupModel();
            groupModel.setGroupId(groupId);
        }
        groupModel.setContactName(groupName);
        realm.copyToRealmOrUpdate(groupModel);
        realm.commitTransaction();
    }

    /**
     * 修改群名称
     *
     * @param groupHeadUrl
     */
    public void updateGroupHeadUrl(String groupId, String groupHeadUrl) {
        realm.beginTransaction();
        GroupModel groupModel = getGroupInfo(groupId);
        if (null == groupModel) {
            groupModel = new GroupModel();
            groupModel.setGroupId(groupId);
        }
        groupModel.setHeadUrl(groupHeadUrl);
        realm.copyToRealmOrUpdate(groupModel);
        realm.commitTransaction();
    }


    /**
     * 初始化 或者更新群信息 入库
     *
     * @param groupName
     */
    public void initOrUpdateGroupInfo(String groupId, String groupName, String headUrl, String ownerId, String ownerName) {
        realm.beginTransaction();
        GroupModel groupModel = getGroupInfo(groupId);
        if (null == groupModel) {
            groupModel = new GroupModel();
            groupModel.setGroupId(groupId);
        }
        groupModel.setContactName(groupName);
        groupModel.setHeadUrl(headUrl);
        groupModel.setOwner(ownerId);
        groupModel.setOwner_name(ownerName);
        realm.copyToRealmOrUpdate(groupModel);
        realm.commitTransaction();
    }


    /**
     * 获取邀请人 list
     *
     * @param menber
     * @return
     */
    public List<HashMap<String, String>> userList(String[] menber) {

        List<HashMap<String, String>> uList = new ArrayList<>();
        for (int i = 0; i < menber.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            String id = menber[i];
            map.put("userid", id);
            map.put("username", getUserName(id));
            uList.add(map);
        }

        return uList;
    }

    /**
     * 更换群主
     *
     * @param groupId
     * @param toUserId
     * @param subscriber
     */
    public void transferGroupMaser(final String groupId, final String toUserId, final Subscriber<Integer> subscriber) {


        RetrofitServiceProvider.getInstance().transferGroupMaster(groupId, mUserId, toUserId, new CommonResponse<BaseData<JsonObject>>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseData<JsonObject> jsonObjectBaseData) {
                final BaseData<JsonObject> json = jsonObjectBaseData;
                int code = json.getCode();

                if (subscriber != null) {
                    subscriber.onNext(code);
                    subscriber.onCompleted();
                }
            }
        });

    }

    /**
     * TODO:待完善
     * @param groupId
     * @param masterId
     * @param filePath
     * @param subscriber
     */
    //    public void uploadGroupHeadImage(final String groupId, final String masterId,String filePath, final Subscriber<Integer> subscriber) {
    //
    //        RetrofitServiceProvider.getInstance().modifyGroupImage(filePath,groupId, masterId, new CommonResponse<BaseData<JsonObject>>(YMApplication.applicationContext) {
    //            @Override
    //            public void onNext(BaseData<JsonObject> jsonObjectBaseData) {
    //                final BaseData<JsonObject> json = jsonObjectBaseData;
    //                int code = json.getCode();
    //                if (subscriber != null) {
    //                    subscriber.onNext(code);
    //                    subscriber.onCompleted();
    //                }
    //            }
    //        });
    //
    //    }


}