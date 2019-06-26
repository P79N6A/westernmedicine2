package com.xywy.retrofit.rxbus;

import com.xywy.util.L;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;



/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/9/2 10:04
 */
public class RxBus {

    private final Subject<Event, Event> mBus;
    //粘性事件集合
    private final Map<String, Event> mStickyEventMap;

    private static volatile RxBus mDefaultInstance = new RxBus();

    private RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.<Event>create());
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static  RxBus getDefault() {
        return mDefaultInstance;
    }
    /**
     * 发送事件
     */
    public void post(Event event) {
        mBus.onNext(event);
    }

    /**
     * 注册事件回调
     */
    public void registerEvent(String eventType, final EventSubscriber subscriber, Object tag) {
        Subscription subscription=toObservable(eventType).subscribe(new Subscriber<Event>(subscriber) {
            @Override
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                L.e("rxbus 事件失败" + e.getMessage());
                L.ex(e);
                subscriber.onError(e);
            }

            @Override
            public void onNext(Event event) {
                subscriber.onNext(event);
            }
        });
        addSubscriptions(tag,subscription);
    }
    /**
     * 解除注册
     */
    private void unregisterEvent(Subscription subscription) {
        if (subscription!=null&&!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
    /**
     * 解除注册
     */
    private void unregisterEventList(Set<Subscription> subscriptions) {
        for (Subscription sub : subscriptions){
            unregisterEvent(sub);
        }
    }

    /**
     * 根据传递的 eventName 返回被观察者并过滤事件
     */
    public Observable<Event> toObservable(final String eventName) {
        return mBus.filter(new Func1<Event, Boolean>() {
            @Override
            public Boolean call(Event event) {
                return eventName.equals(event.getName());
            }
        });
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return mBus.hasObservers();
    }



    /**
     * Stciky 相关
     */

    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(Event event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getName(), event);
        }
        post(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public  Observable<Event> toObservableSticky(final String eventName) {
        //此处为组合调用，必须加上同步。
        synchronized (mStickyEventMap) {
            Observable<Event> observable = toObservable(eventName);
            final Event event = mStickyEventMap.get(eventName);
            //若此处发生线程切换，event可能被其他线程改变了。
            if (event != null) {
                return observable.mergeWith(Observable.create(new Observable.OnSubscribe<Event>() {
                    @Override
                    public void call(Subscriber<? super Event> subscriber) {
                        subscriber.onNext(event);
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public Event getStickyEvent(final String eventName) {
        synchronized (mStickyEventMap){
            return mStickyEventMap.get(eventName);
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public Event removeStickyEvent(final String eventName) {
        synchronized (mStickyEventMap) {
            return mStickyEventMap.remove(eventName);
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }


    //已订阅集合，需要进行解注册防止内存泄漏
    private final  ConcurrentHashMap<Object,Set<Subscription>>  subscriptions=new ConcurrentHashMap<>();
    private  void addSubscriptions(Object tag,Subscription subscription) {
        if (tag==null)
            return;
        if(subscriptions.get(tag)==null){
            subscriptions.put(tag,new HashSet<Subscription>());
        }
        subscriptions.get(tag).add(subscription);
    }
    public  void removeSubscriptions(Object tag) {
        if (tag==null)
            return;
        if(subscriptions.get(tag)!=null){
            RxBus.getDefault().unregisterEventList(subscriptions.get(tag));
            subscriptions.remove(tag);//stone 新添加
        }
    }
}

