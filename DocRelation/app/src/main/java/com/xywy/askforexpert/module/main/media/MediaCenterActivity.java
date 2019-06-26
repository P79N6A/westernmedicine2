package com.xywy.askforexpert.module.main.media;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.util.HanziToPinyin;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.interfaces.TextWatcherImpl;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitServices;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.model.media.MediaListCacheData;
import com.xywy.askforexpert.module.message.adapter.ContactAdapter;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;
import com.xywy.askforexpert.widget.Sidebar2;
import com.xywy.base.view.CircleProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shihao on 16/3/7.
 * <p>
 * modified by Jack Fang
 */
public class MediaCenterActivity extends Activity {
    private static final String TAG = "MediaCenterActivity";

    private static final String PARAMS_BIND = "patientlist";
    private static final String PARAMS_A = "areamedical";
    private static final String PARAMS_M = "patientlist";

    @Bind(R.id.media_center_btn)
    ImageButton mediaCenterBtn;
    @Bind(R.id.media_center_title)
    TextView mediaCenterTitle;
    @Bind(R.id.media_center_barlayout)
    RelativeLayout mediaCenterBarlayout;
    @Bind(R.id.media_center_list)
    ListView mediaCenterList;
    @Bind(R.id.search_bar_view)
    EditText searchBarView;
    @Bind(R.id.sidebar)
    Sidebar2 sidebar;
    @Bind(R.id.floating_header)
    TextView floatingHeader;
    @Bind(R.id.no_healthy_user)
    TextView noHealthyUser;
    @Bind(R.id.healthy_user_loading_progress)
    CircleProgressBar healthyUserLoadingProgress;

    private Map<String, String> sMap = new HashMap<>();
    private Map<String, Object> sMediaMap = new HashMap<>();

    private List<AddressBook> dataList = new ArrayList<>();

    private ContactAdapter mediaListAdapter;

    private int type = -1;
    private Call<AddressBook> call;

    private SharedPreferences mediaListCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_media_center);
        ButterKnife.bind(this);

        mediaListCache = getSharedPreferences("mediaListCache", MODE_PRIVATE);

        sMap.put("bind", PARAMS_BIND);
        sMap.put("a", PARAMS_A);
        sMap.put("m", PARAMS_M);
        sMap.put("timestamp", String.valueOf(System.currentTimeMillis()));

        sMediaMap.put("a", "chat");
        sMediaMap.put("m", "getRelation");
        sMediaMap.put("username", YMApplication.getLoginInfo().getData().getHuanxin_username());
        sMediaMap.put("bind", YMApplication.getLoginInfo().getData().getHuanxin_username());
        sMediaMap.put("timestamp", System.currentTimeMillis());
        sMediaMap.put("sign", CommonUtils.computeSign(sMediaMap.get("timestamp")
                + YMApplication.getLoginInfo().getData().getHuanxin_username()));

        initData();
        switch (type) {
            case 2:
                mediaCenterTitle.setText("服务号");
                break;
            case 3:
            case 5:
                mediaCenterTitle.setText("媒体号");
                break;
            case 4:
                mediaCenterTitle.setText("签约居民");
                break;
        }
        sidebar.setListView(mediaCenterList);
        mediaListAdapter = new ContactAdapter(this, 1, dataList);
        if (type == 4) {
            mediaListAdapter.setType(type);
        }
        mediaCenterList.setAdapter(mediaListAdapter);
//        mediaCenterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        mediaCenterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                //跳转到详情页
                switch (type) {
                    case 2:
                        intent.setClass(MediaCenterActivity.this, DiscussSettingsActivity.class);
                        intent.putExtra("uuid", dataList.get(position).getHxusername().replaceAll("sid_", ""));
                        intent.putExtra("isDoctor", 3 + "");
                        break;

                    case 3:
                    case 5:
                        intent.setClass(MediaCenterActivity.this, MediaDetailActivity.class);
                        intent.putExtra("mediaId", dataList.get(position).getId());
                        break;

                    // 签约居民
                    case 4:
//                        intent.setClass(MediaCenterActivity.this, HealthyUserInfoDetailActivity.class);
//                        intent.putExtra(HealthyUserInfoDetailActivity.PATIENT_ID_INTENT_KEY,
//                                dataList.get(position).getId());
                        StatisticalTools.eventCount(MediaCenterActivity.this, "ResidentList");
                        intent.setClass(MediaCenterActivity.this, ChatMainActivity.class);
                        AddressBook data = dataList.get(position);
                        DLog.d(TAG, "healthy user id = " + data.getId() + ", hxid = " + data.getHxid());
                        intent.putExtra("userId", data.getHxid());
                        intent.putExtra("username", data.getXm());
                        intent.putExtra("toHeadImge", data.getTxdz());
                        intent.putExtra(ChatMainActivity.IS_HEALTHY_USER_KEY, true);
                        intent.putExtra(ChatMainActivity.DIAL_ID, data.getId());
                        break;
                }
                startActivity(intent);
//                if (type != 4 || type != 5)
//                    finish();
            }
        });
        searchBarView.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (mediaListAdapter != null) {
                    mediaListAdapter.setData(dataList);
                    mediaListAdapter.getFilter().filter(s);
                }
            }
        });

        searchBarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 4) {
                    StatisticalTools.eventCount(MediaCenterActivity.this, "SignResidentSearch");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((type == 5 || type == 3) && mediaListCache.getBoolean("changed", false)) {
            String listCache = mediaListCache.getString("media_list_cache", "");
            if (!listCache.equals("")) {
                MediaListCacheData mediaListCacheData = new Gson().fromJson(listCache, MediaListCacheData.class);
                List<AddressBook> mediacachelist = mediaListCacheData.getMediacachelist();
                dataList.clear();
                dataList.addAll(mediacachelist);
                sortDataList();
                mediaListCache.edit().putBoolean("changed", false).apply();
            }
        }
    }

    private void initData() {
        type = getIntent().getIntExtra("type", -1);
        ArrayList<AddressBook> list = (ArrayList<AddressBook>) getIntent().getSerializableExtra("mediaList");
        if (!dataList.isEmpty()) {
            dataList.clear();
        }
        if (list != null) {
            dataList.addAll(list);
        }

        if (type == 4) { // 签约居民 请求数据
            showHealthyLoading(true);
            requestData();
        } else if (type == 5 || type == 3) {
            showHealthyLoading(true);
            requestMediaListData();
        } else {
            sortDataList();
        }
    }

    private void sortDataList() {
        if (dataList != null && !dataList.isEmpty()) {
            Collections.sort(dataList, new Comparator<AddressBook>() {
                @Override
                public int compare(AddressBook lhs, AddressBook rhs) {
                    try {
                        String realname = getName(lhs);
                        String realname1 = getName(rhs);
                        if (realname != null && realname1 != null) {
                            if (!"".equals(realname)
                                    && !"".equals(realname1)) {
                                char med1 = HanziToPinyin.getInstance()
                                        .get(realname.substring(0, 1)).get(0).target
                                        .substring(0, 1).toUpperCase().charAt(0);
                                char med2 = HanziToPinyin.getInstance()
                                        .get(realname1.substring(0, 1)).get(0).target
                                        .substring(0, 1).toUpperCase().charAt(0);
                                if (med1 > med2) {
                                    return 1;
                                }
                                if (med1 < med2) {
                                    return -1;
                                }
                                if (med1 == med2) {
                                    return 0;
                                }
                            } else {
                                return realname.compareTo(realname1);
                            }
                        } else {
                            return 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }

                    return 0;
                }
            });
        }

        if (type == 4 || type == 5 || type == 3) {
            setHeaders();
            mediaListAdapter.notifyDataSetChanged();
        }
    }

    private void setHeaders() {
        if (dataList != null && !dataList.isEmpty()) {
            for (int i = 0; i < dataList.size(); i++) {
                AddressBook data = dataList.get(i);
                String xm = getName(data);
                if (xm.equals("")) {
                    data.setHeader("#");
                } else {
                    data.setHeader(HanziToPinyin.getInstance().get(xm.substring(0, 1)).get(0).target
                            .substring(0, 1).toUpperCase());
                    char header = data.getHeader().toLowerCase().charAt(0);
                    if (header < 'a' || header > 'z') {
                        data.setHeader("#");
                    }
                }
            }
        }
    }

    private String getName(AddressBook lhs) {
        if (type == 4) {
            return lhs.getXm();
        }

        return lhs.getRealname();
    }

    private void requestData() {
        sMap.put("sign", CommonUtils.computeSign(sMap.get("timestamp") + PARAMS_BIND));

        RetrofitServices.HealthyUserListService service =
                RetrofitUtil.createService(RetrofitServices.HealthyUserListService.class);
        call = service.getData(sMap);
        call.enqueue(new Callback<AddressBook>() {
            @Override
            public void onResponse(Call<AddressBook> call, Response<AddressBook> response) {
                if (BuildConfig.DEBUG) {
                    RetrofitUtil.checkRetrofitRequest(call.request());
                }

                showHealthyLoading(false);
                if (response.isSuccessful()) {
                    AddressBook data = response.body();
                    List<AddressBook> datalist = data.getData();
                    if (datalist != null && !datalist.isEmpty()) {
                        dataList.clear();
                        dataList.addAll(datalist);
                        sortDataList();
                    } else {
                        sidebar.setVisibility(View.GONE);
                        showHealthyNoDataPage(true);
                    }
                } else {
                    Toast.makeText(MediaCenterActivity.this, "刷新数据失败", Toast.LENGTH_SHORT).show();
                    sidebar.setVisibility(View.GONE);
                    showHealthyNoDataPage(true);
                }
            }

            @Override
            public void onFailure(Call<AddressBook> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    RetrofitUtil.checkRetrofitRequest(call.request());
                }
                DLog.d(TAG, "onFailure " + t.getMessage());
                Toast.makeText(MediaCenterActivity.this, "刷新数据失败", Toast.LENGTH_SHORT).show();
                showHealthyNoDataPage(true);
                showHealthyLoading(false);
                sidebar.setVisibility(View.GONE);
            }
        });
    }

    private void requestMediaListData() {
        RetrofitServices.FriendListService service =
                RetrofitUtil.createService(RetrofitServices.FriendListService.class);
        Call<AddressBook> mediaListCall = service.getData(sMediaMap);
        mediaListCall.enqueue(new Callback<AddressBook>() {
            @Override
            public void onResponse(Call<AddressBook> call, Response<AddressBook> response) {
                showHealthyLoading(false);
                if (response.isSuccessful()) {
                    AddressBook body = response.body();
                    if (body != null) {
                        List<AddressBook> data = body.getData();
                        if (data != null) {
                            if (!dataList.isEmpty()) {
                                dataList.clear();
                            }
                            for (AddressBook book : data) {
                                if (book.getType() == 3) {
                                    dataList.add(book);
                                }
                            }
                            String s = new Gson().toJson(dataList);
                            String jsonString = "{\"medialistcache\":" + s + "}";
                            DLog.d(TAG, "media dataList json s = " + jsonString);
                            mediaListCache.edit().clear().apply();
                            mediaListCache.edit().putString("media_list_cache", jsonString).apply();
                            mediaListCache.edit().putBoolean("changed", false).apply();
                            sortDataList();
                        } else {
                            Toast.makeText(MediaCenterActivity.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                            sidebar.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(MediaCenterActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        sidebar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MediaCenterActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    sidebar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AddressBook> call, Throwable t) {
                showHealthyLoading(false);
                Toast.makeText(MediaCenterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                sidebar.setVisibility(View.GONE);
            }
        });
    }

    private void showHealthyNoDataPage(boolean b) {
        int v = b ? View.VISIBLE : View.GONE;
        noHealthyUser.setVisibility(v);
    }

    private void showHealthyLoading(boolean b) {
        int v = b ? View.VISIBLE : View.GONE;
        healthyUserLoadingProgress.setVisibility(v);
    }

    @Override
    protected void onDestroy() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        super.onDestroy();

    }

    @OnClick(R.id.media_center_btn)
    public void onClick() {
        YMApplication.isrefresh = true;
        finish();
    }
}
