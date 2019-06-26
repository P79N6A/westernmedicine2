package com.xywy.askforexpert.module.main.patient.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.consultentity.QuestionMsgListRspEntity;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Created by jason on 2018/11/9.
 */

public class AddGroupActivity extends YMBaseActivity {
    @Bind(R.id.group_name)
    EditText group_name;
    private String doctorId = YMUserService.getCurUserId();
    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("添加分组");
        titleBarBuilder.addItem("保存", new ItemClickListener() {
            @Override
            public void onClick() {
                if (!TextUtils.isEmpty(group_name.getText())
                        &&group_name.getText().length()>=1
                        &&group_name.getText().length()<=10) {
                    ServiceProvider.addGroup(doctorId, group_name.getText().toString(), new Subscriber<QuestionMsgListRspEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.longToast("保存分组失败");
                        }

                        @Override
                        public void onNext(QuestionMsgListRspEntity questionMsgListRspEntity) {
                            if (questionMsgListRspEntity.getCode()==10000){
                                ToastUtils.longToast("保存分组成功");
                                setResult(RESULT_OK);
                                finish();
                            }else{
                                ToastUtils.longToast(questionMsgListRspEntity.getMsg());
                            }
                        }
                    });
                }else{
                    ToastUtils.longToast("请输入分组名称1-10字");
                }
            }
        }).build();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.add_group_layout;
    }
}
