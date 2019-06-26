package com.xywy.askforexpert.module.my.pause;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xywy.askforexpert.R;


/**
 * 充值积分适配器
 *
 * @author apple
 */
public class MoneyAdapter extends BaseAdapter {
    String[] jifen = null;
    String[] money = null;
    Context context;

    public MoneyAdapter(Context context, String[] jifen, String[] money) {
        // TODO Auto-generated constructor stub
        this.money = money;
        this.jifen = jifen;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return jifen.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View converView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (converView == null) {
            holder = new ViewHolder();
            converView = View.inflate(context, R.layout.item_money, null);
            holder.tv_item_jifen = (TextView) converView.findViewById(R.id.tv_item_jifen);
            holder.tv_item_money = (TextView) converView.findViewById(R.id.tv_item_money);
            converView.setTag(holder);
        } else {
            holder = (ViewHolder) converView.getTag();
        }
        holder.tv_item_jifen.setText(jifen[arg0]);
        holder.tv_item_money.setText("¥" + money[arg0]);


        return converView;
    }

    class ViewHolder {
        TextView tv_item_jifen, tv_item_money;
    }

}
