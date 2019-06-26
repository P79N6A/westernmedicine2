package com.xywy.askforexpert.module.docotorcirclenew.fragment.tab;


import android.support.v4.app.Fragment;

import com.xywy.askforexpert.appcommon.base.fragment.CommonListFragment;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.BaseUltimateRecycleAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.CircleVisitHistoryAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.model.DoctorCircleModelFactory;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;

import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Anonymous;
import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Realname;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCircleVIsitHistoryTabFragment extends DocCircleCommonTabFragment {
    protected void initSubFragments() {
        addFragment(Realname);
        addFragment(Anonymous);
    }

    private void addFragment(@PublishType String type)  {
        CommonListFragment visitHistoryFragment =  new CommonListFragment();
        IRecycleViewModel realModel = DoctorCircleModelFactory.newVisitHistoryModel(visitHistoryFragment, this.getActivity(), type);
        visitHistoryFragment.setRecycleViewModel(realModel);
        BaseUltimateRecycleAdapter adapter = new CircleVisitHistoryAdapter(getContext());
        visitHistoryFragment.setAdapter(adapter);
        subFragmets.add(visitHistoryFragment);
    }

}
