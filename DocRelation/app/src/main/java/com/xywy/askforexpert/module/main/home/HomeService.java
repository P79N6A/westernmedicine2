package com.xywy.askforexpert.module.main.home;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.main.NewsListPageBean;
import com.xywy.askforexpert.model.main.SubscribeTitleListBean;

import java.util.List;

import rx.Subscriber;

/**
 * 首页Tab请求网络服务类
 * Created by bailiangjin on 2017/1/10.
 */
public class HomeService {

    /**
     * 获取订阅标题列表
     *
     * @param subscriber
     */
    public static void getSubscribeTitleListInfo(final Subscriber<SubscribeTitleListBean> subscriber) {

        // YMApplication.getLoginInfo().getData().getIsjob()  isjob 1 兼职 2 专家  0未认证
        // 医学生  isdoctor   12|| 13|| 14
        String identityType;
        if (!YMUserService.isGuest()) {
            identityType = YMApplication.getLoginInfo().getData().getIsjob();
            LogUtils.d("user type = " + identityType);
            String isDoctor = YMApplication.getLoginInfo().getData().getIsdoctor();
            if (null == identityType || "".equals(identityType)) {
                identityType = "0";
            } else {
                switch (identityType) {
                    // 为认证：
                    case "0":
                        identityType = "0";
                        if (isDoctor.equals("12") || isDoctor.equals("13") || isDoctor.equals("14")) {
                            identityType = "2";
                        }
                        break;

                    // 兼职医生
                    case "1":
                        identityType = "3";
                        break;

                    // 专家
                    case "2":
                        identityType = "1";
                        break;

                    // 医学生
                    default:
                        if (isDoctor.equals("12") || isDoctor.equals("13") || isDoctor.equals("14")) {
                            identityType = "2";
                        }
                        break;
                }
            }

        } else {
            identityType = "0";
        }

        final String userId = YMUserService.getCurUserId();

        RetrofitServiceProvider.getInstance().getSubscribeTitleList(userId, identityType, new CommonResponse<SubscribeTitleListBean>() {


            @Override
            public void onNext(SubscribeTitleListBean subscribeTitleListBean) {
                subscribeTitleListBean = null == subscribeTitleListBean ? HomePageCacheUtils.getSubscribeTitleListBean(userId) : subscribeTitleListBean;

                if (null != subscribeTitleListBean) {
                    subscriber.onNext(subscribeTitleListBean);

                    List<SubscribeTitleListBean.SubscribeBean> subscribeBeanList= subscribeTitleListBean.getSubscribe();
                    if(null!=subscribeBeanList&&subscribeBeanList.size()>0){
                        HomePageCacheUtils.cacheNewsTabTitleListData(userId, subscribeTitleListBean);
                    }
                } else {
                    subscriber.onError(new RuntimeException("返回数据为空"));
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                subscriber.onError(e);
            }
        });
    }

    public static void getMewsListPageBeanList(final String id, final int pageNumber, int pageSize, final Subscriber<NewsListPageBean> subscriber) {

        RetrofitServiceProvider.getInstance().getNewsListPageBeanList(YMUserService.getCurUserId(), id, pageNumber, pageSize, new CommonResponse<NewsListPageBean>() {
            @Override
            public void onNext(NewsListPageBean newsListPageBean) {

                newsListPageBean = null == newsListPageBean && 1 == pageNumber ? HomePageCacheUtils.getNewsListBean(id) : newsListPageBean;
                if (null != newsListPageBean) {
                    subscriber.onNext(newsListPageBean);
                    if(newsListPageBean.getList().size()>0){
                        HomePageCacheUtils.cacheNewsListData(id, newsListPageBean);
                    }
                } else {
                    subscriber.onError(new RuntimeException("返回数据为空"));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                    subscriber.onError(e);
            }
        });

    }
}
