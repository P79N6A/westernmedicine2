package com.xywy.askforexpert.module.message.health;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.widget.Sidebar2;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 签约居民
 *
 * @author Jack Fang
 */
@Deprecated
public class HealthyUserListActivity extends AppCompatActivity {
    private static final String TAG = "HealthyUserListActivity";
    private static final String PARAMS_BIND = "patientlist";
    private static final String PARAMS_A = "areamedical";
    private static final String PARAMS_M = "patientlist";
    @Bind(R.id.healthy_user_list_toolbar)
    Toolbar healthyUserListToolbar;
    @Bind(R.id.healthy_user_list_search)
    EditText healthyUserListSearch;
    @Bind(R.id.healthy_user_list)
    ListView healthyUserList;
    @Bind(R.id.sidebar)
    Sidebar2 sidebar;
    @Bind(R.id.floating_header)
    TextView floatingHeader;
    @Bind(R.id.no_healthy_user)
    TextView noHealthyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_user_list);

        ButterKnife.bind(this);
        CommonUtils.initSystemBar(this);
        CommonUtils.setToolbar(this, healthyUserListToolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
