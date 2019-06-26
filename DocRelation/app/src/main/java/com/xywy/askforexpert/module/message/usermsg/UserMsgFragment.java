package com.xywy.askforexpert.module.message.usermsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ACache;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.module.message.MessageInfoFragment;
import com.xywy.askforexpert.module.message.friend.AddCardHoldVerifyActiviy;
import com.xywy.askforexpert.module.message.friend.AddNewCardHolderActivity;
import com.xywy.askforexpert.module.message.friend.InviteNewFriendMainActivity;
import com.xywy.askforexpert.module.message.friend.Menu_CardHoder_Right_Fragment;
import com.xywy.askforexpert.module.message.friend.MyIdCardActivity;
import com.xywy.askforexpert.widget.ActionItem;
import com.xywy.askforexpert.widget.TitlePopup;
import com.xywy.askforexpert.widget.view.SlidingMenu;
import com.xywy.easeWrapper.domain.EaseUser;
import com.zxing.activity.CaptureActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserMsgFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserMsgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserMsgFragment extends Fragment implements TitlePopup.OnItemOnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "UserMsgFragment";

    private static final String ADD_FRIEND = "添加好友";
    private static final String INVITE_FRIEND = "邀请好友";
    private static final String MY_CARD = "我的名片";
    private static final String ERWEIMA = "扫一扫";
    Map<String, String> map;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SlidingMenu mSlidingMenu;
    private FragmentTransaction mTransaction;
    private Menu_CardHoder_Right_Fragment rightFragment;
    private MessageInfoFragment messageInfoFragment;
    private ImageButton message_right;
    private AddressBook pListInfo;
    private ImageView msg_card_iv;
    private View view;
    private ACache mCache;
    private OnFragmentInteractionListener mListener;
    private String uid = "";
    private TitlePopup menuPopup;

    public UserMsgFragment() {
        // Required empty public constructor
        rightFragment = new Menu_CardHoder_Right_Fragment();
        messageInfoFragment = new MessageInfoFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserMsgFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserMsgFragment newInstance(String param1, String param2) {
        UserMsgFragment fragment = new UserMsgFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_msg, container, false);

        initView(view);

        if (!YMUserService.isGuest()) {
            if (YMApplication.getLoginInfo() != null) {
                uid = YMApplication.getLoginInfo().getData().getPid();
                getData();
            }

        }

        return view;
    }

    public void setContentList() {
        Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();

        for (int i = 0; i < pListInfo.getData().size(); i++) {
            EaseUser user = new EaseUser(pListInfo.getData().get(i).getHxusername());
            // setUserHearder(username, user);
            userlist.put(pListInfo.getData().get(i).getHxusername(), user);
        }
        YMApplication.getInstance().setContactList(userlist);
    }

    public void getData() {

        mCache = ACache.get(getActivity());


        String did = YMApplication.getLoginInfo().getData().getPid();
//		AddressBook	pListInfo1 = (AddressBook)mCache.getAsObject("tag");
        String bind = YMApplication.getLoginInfo().getData()
                .getHuanxin_username();
        Long st = System.currentTimeMillis();

        String sign = CommonUtils.computeSign(st + bind);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "getRelation");
        params.put("username", bind);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "名片夹返回数据" + t.toString());
                        pListInfo = ResolveJson.R_CardHold(t.toString());
                        mCache.put("card" + uid, pListInfo);
                        super.onSuccess(t);

                        setContentList();
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        AddressBook cachePlistInfo = (AddressBook) mCache.getAsObject("card" + uid);
                        if (cachePlistInfo != null) {
                            pListInfo = cachePlistInfo;

                            setContentList();
                        }

                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }

    private void initView(View view) {
        message_right = (ImageButton) view.findViewById(R.id.message_right);
        msg_card_iv = (ImageView) view.findViewById(R.id.msg_card_iv);

        mSlidingMenu = (SlidingMenu) view.findViewById(R.id.msg_slidingMenu);
        mSlidingMenu.setCanSliding(false);
        mSlidingMenu.setRightView(getActivity().getLayoutInflater().inflate(
                R.layout.right_frame, null));
        mSlidingMenu.setCenterView(getActivity().getLayoutInflater().inflate(
                R.layout.center_frame, null));
        mTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        mTransaction.replace(R.id.center_frame, messageInfoFragment);
        mTransaction.replace(R.id.right_frame, rightFragment);
        mTransaction.commit();

        menuPopup = new TitlePopup(getActivity(), LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        menuPopup.setItemOnClickListener(this);

        menuPopup.cleanAction();
        menuPopup.addAction(new ActionItem(getActivity(), ADD_FRIEND));
        menuPopup.addAction(new ActionItem(getActivity(), INVITE_FRIEND));
        menuPopup.addAction(new ActionItem(getActivity(), MY_CARD));
        menuPopup.addAction(new ActionItem(getActivity(), ERWEIMA));

        message_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSlidingMenu.showRightView();
                menuPopup.show(v);
            }
        });

        msg_card_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(YMApplication.getAppContext(), MsgFriendCardActivity.class));
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(ActionItem item, int position) {
        String mTitle = (String) item.mTitle;
        Intent intent;
        switch (mTitle) {
            // 添加好友
            case ADD_FRIEND:
                StatisticalTools.eventCount(getActivity(), "addfriends");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    intent = new Intent(getActivity(),
                            AddNewCardHolderActivity.class);
                    startActivity(intent);
                }
                break;

            // 邀请好友
            case INVITE_FRIEND:
                StatisticalTools.eventCount(getActivity(), "Invite");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    intent = new Intent(getActivity(),
                            InviteNewFriendMainActivity.class);
                    startActivity(intent);
                }
                break;

            // 我的名片
            case MY_CARD:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    intent = new Intent(getActivity(),
                            MyIdCardActivity.class);
                    startActivity(intent);
                }
                break;

            // 扫一扫
            case ERWEIMA:
                StatisticalTools.eventCount(getActivity(), "RichScan");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                }
                break;

            default:
                break;
        }
    }

    public MessageInfoFragment getMessageInfoFragment() {
        return messageInfoFragment;
    }

    public void refreshMsg() {
        DLog.i("shrmsg", "消息刷新进入UserMsgrefreshMsg");
        if (messageInfoFragment != null) {
            DLog.i("shrmsg", "消息刷新进入messageInfoFragment");
            messageInfoFragment.refresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        DLog.i(TAG, "消息也暂定状态");
        mSlidingMenu.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            sendData(scanResult);
            // T.shortToast(scanResult );
        }
    }

    public void sendData(final String str) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + str;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "qrcodeScan");
        params.put("url", str);
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "扫面返回的结果" + t.toString());
                        Gson gson = new Gson();
                        map = ResolveJson.R_Action_twos(t.toString());
                        if (map.get("code").equals("0")) {
                            if (map.get("isxywy").equals("1")) {
                                Intent intenAdd = new Intent(getActivity(),
                                        AddCardHoldVerifyActiviy.class);
                                intenAdd.putExtra("toAddUsername",
                                        "did_" + map.get("did"));
                                startActivity(intenAdd);
                            } else {
                                if (str.contains("http")) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = Uri.parse(str);
                                    intent.setData(content_url);
                                    startActivity(intent);
                                } else {
                                    Dialog(getActivity(), str);
                                }

                            }
                        } else {
                            ToastUtils.shortToast( map.get("msg"));
                            if (str.contains("http")) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(str);
                                intent.setData(content_url);
                                startActivity(intent);
                            } else {
                                Dialog(getActivity(), str);
                            }
                        }
                        super.onSuccess(t);
                    }
                });

    }

    public void Dialog(Context context, String str) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.myclic_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView content = (TextView) layout.findViewById(R.id.tv_content);
        content.setText(str);
        RelativeLayout re_ok = (RelativeLayout) layout.findViewById(R.id.rl_ok);
        re_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
