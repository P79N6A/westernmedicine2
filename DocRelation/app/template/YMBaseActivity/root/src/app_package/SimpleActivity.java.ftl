package ${packageName};


public class ${activityClass} extends YMBaseActivity{
@Override
protected int getLayoutResId() {

<#if generateLayout>
    return R.layout.${layoutName};
<#else>
    return 0;
</#if>

}

@Override
protected void initIntentData() {

}

@Override
protected void initView() {

}

@Override
protected void initData() {

}
}