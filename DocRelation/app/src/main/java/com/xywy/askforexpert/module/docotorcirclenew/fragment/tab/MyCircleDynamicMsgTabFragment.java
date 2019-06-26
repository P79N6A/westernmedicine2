package com.xywy.askforexpert.module.docotorcirclenew.fragment.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xywy.askforexpert.appcommon.base.fragment.CommonListFragment;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.AdapterFactory;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.BaseUltimateRecycleAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.model.DoctorCircleModelFactory;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;

import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Anonymous;
import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Realname;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCircleDynamicMsgTabFragment extends DocCircleCommonTabFragment {
    String visitedUserId;
    public  static MyCircleDynamicMsgTabFragment newInstance(String visitedUserId){
        MyCircleDynamicMsgTabFragment fragment = new MyCircleDynamicMsgTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("visitedUserId", visitedUserId);
        fragment.setArguments(bundle);
        return fragment;
    }
    protected void initSubFragments() {
        visitedUserId=getArguments().getString("visitedUserId");
        addFragment(Realname);
        addFragment(Anonymous);
    }

    private void addFragment(@PublishType String type)  {
        CommonListFragment commonListFragment =new CommonListFragment();
        IRecycleViewModel recycleViewModel = DoctorCircleModelFactory.newInstance(commonListFragment, this.getActivity(), type, visitedUserId);
        commonListFragment.setRecycleViewModel(recycleViewModel);
        BaseUltimateRecycleAdapter adapter = AdapterFactory.newDynanicMsgAdapter(getContext(), type);
        commonListFragment.setAdapter(adapter);
        subFragmets.add(commonListFragment);
    }

}
