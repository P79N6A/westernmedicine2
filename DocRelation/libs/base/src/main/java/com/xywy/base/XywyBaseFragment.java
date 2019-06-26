package com.xywy.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xywy.util.AppUtils;
import com.xywy.util.L;
import com.xywy.util.T;

/**
 * Created by bailiangjin on 2016/10/24.
 */

public abstract class XywyBaseFragment extends Fragment  {


    protected View rootView;

    private LinearLayout ll_root;

    private FrameLayout baseContainer;

    private Toolbar toolbar;

    protected TitleBarBuilder titleBarBuilder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //L.d("Fragment:::-->>onCreateView");
        if(null==rootView){
            rootView = initRootView(inflater, container);
            beforeViewBind();
            bindView(rootView);
            hideCommonBaseTitle();
            initView();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        L.d("Fragment:::-->>onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData(savedInstanceState);
    }


    /**
     * 初始化父类UI
     */
    private View initRootView(LayoutInflater layoutInflater, ViewGroup container) {
        rootView = layoutInflater.inflate(R.layout.base_fragment_base, container, false);
        ll_root = (LinearLayout) rootView.findViewById(R.id.ll_root);
        baseContainer = (FrameLayout) rootView.findViewById(R.id.baseContainer);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        View ChildView = layoutInflater.inflate(getLayoutResId(), null);
        baseContainer.addView(ChildView);
        titleBarBuilder = new TitleBarBuilder(getActivity(), toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.setPadding(0, AppUtils.getStatusBarHeight(getActivity()), 0, 0);
        }
        return rootView;
    }

    /**
     * 隐藏公共title
     */
    public void hideCommonBaseTitle() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }


    /**
     * 显示公共的title
     */
    public void showCommonBaseTitle() {

        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 设置隐藏状态栏高度
     */
    public void hideStatusBarHight() {
        ll_root.setFitsSystemWindows(false);
    }

    /**
     * 设置添加状态栏高度
     */
    public void showStatusBarHight() {
        ll_root.setFitsSystemWindows(true);
    }


    @Override
    public void onAttach(Activity activity) {
        //L.d("Fragment:::-->>onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //L.d("Fragment:::-->>onCreate");
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        //L.d("Fragment:::-->>onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        //L.d("Fragment:::-->>onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        //L.d("Fragment:::-->>onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        //L.d("Fragment:::-->>onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        //L.d("Fragment:::-->>onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        //L.d("Fragment:::-->>onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        //L.d("Fragment:::-->>onDetach");
        super.onDetach();
    }


    /**
     * shortToast toast by string
     *
     * @param string
     */
    protected void shortToast(String string) {
        T.showShort(getActivity(),string);
    }

    /**
     * shortToast toast by res id
     *
     * @param resId
     */
    protected void shortToast(int resId) {
         T.showShort(getActivity(),resId);
    }

    /**
     * long toast
     *
     * @param string
     */
    protected void longToast(String string) {
        T.showLong(getContext(),string);
    }

    /**
     * long toast
     *
     * @param resId
     */
    protected void longToast(int resId) {
        T.showLong(getContext(),resId);
    }


    /**
     * 设置layout ResId
     *
     * @return ResId
     */
    protected abstract int getLayoutResId();


    protected abstract void beforeViewBind();

    /**
     * BaseFragment 添加view绑定使用 具体Fragment不需要Override该方法
     *
     * @param view
     */
    protected abstract void bindView(View view);

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void unBindView();

    protected abstract void initListener();

}
