package com.xywy.askforexpert.module.message.friend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.widget.view.SlidingMenu;

/**
 * 名片夹 Deprecated stone
 *
 * @author 王鹏
 * @2015-5-14下午2:01:24
 */
@Deprecated
public class CardHolderMainActivity2 extends Fragment {

    private static final String TAG = "CardHolderMainActivity2";
    private SlidingMenu mSlidingMenu;
    private FragmentTransaction mTransaction;
    private Menu_CardHoder_Right_Fragment rightFragment;
    private CardHolderFragment centerFragment;
    private ImageButton btn2;
    private boolean hidden;
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.card_holder_main);
//		initView();
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_holder_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void refresh() {
        DLog.i(TAG, "是否刷新了..");
        centerFragment.refresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    public void initView() {

        mSlidingMenu = (SlidingMenu) getView().findViewById(R.id.slidingMenu);
        mSlidingMenu.setCanSliding(false);
        mSlidingMenu.setRightView(getActivity().getLayoutInflater().inflate(
                R.layout.right_frame, null));
        mSlidingMenu.setCenterView(getActivity().getLayoutInflater().inflate(
                R.layout.center_frame, null));
        mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        rightFragment = new Menu_CardHoder_Right_Fragment();
        centerFragment = new CardHolderFragment();
        btn2 = (ImageButton) getView().findViewById(R.id.btn2);
        mTransaction.replace(R.id.center_frame, centerFragment);
        mTransaction.replace(R.id.right_frame, rightFragment);
        mTransaction.commit();
        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mSlidingMenu.showRightView();

            }
        });
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("CardHolderMainActivity2");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("CardHolderMainActivity2");
    }

//	public void onClickBack(View view)
//	{
//		switch (view.getId())
//		{
//		case R.id.btn1:
//
////			finish();
//
//			break;
//		case R.id.btn2:
//			mSlidingMenu.showRightView();
//			break;
//		default:
//			break;
//		}
//	}

}
