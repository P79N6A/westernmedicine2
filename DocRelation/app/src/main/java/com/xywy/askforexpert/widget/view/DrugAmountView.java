package com.xywy.askforexpert.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.view.AmountView;
import com.xywy.util.LogUtils;

/**
 * Created by jason on 2018/7/10.
 */

public class DrugAmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private static final String TAG = "AmountView";
    private int amount = 1; //购买数量
    private int stock = Integer.MAX_VALUE;

    private TextView etAmount;
    private Button btnDecrease;
    private Button btnIncrease;
    private int oldAmount;

    public DrugAmountView(Context context) {
        this(context, null);
    }

    public DrugAmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_drug_amount, this);
        etAmount = (TextView) findViewById(R.id.etAmount);
        btnDecrease = (Button) findViewById(R.id.btnDecrease);
        btnIncrease = (Button) findViewById(R.id.btnIncrease);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
//        etAmount.addTextChangedListener(this);

        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnWidth, 80);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 0);
        int btnTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnTextSize, 0);
        obtainStyledAttributes.recycle();

        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        btnDecrease.setLayoutParams(btnParams);
        btnIncrease.setLayoutParams(btnParams);
        if (btnTextSize != 0) {
            btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        etAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            etAmount.setTextSize(tvTextSize);
        }
    }




    public void setCount(int count){
        etAmount.setText(count + "");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            oldAmount = amount;
            oldAmount--;
            if (amount > 1) {
                amount--;
                etAmount.setText(amount + "");
            }
        } else if (i == R.id.btnIncrease) {
            oldAmount = amount;
            if (stock<amount) {
                etAmount.setText(stock + "");
            }else{
                amount++;
                etAmount.setText(amount + "");
            }

        }

        etAmount.clearFocus();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        if (s.toString().isEmpty())
//            return;
//        amount = Integer.valueOf(s.toString());
//
//        if (mListener != null) {
//            LogUtils.i("text change----------"+etAmount.getText().toString());
//            checkInput(etAmount.getText().toString());
//            oldAmount = amount;
//            mListener.onAmountChange(this, amount);
//        }
    }

    private void checkInput(String s) {
        if(null != s && s.length()>1){
            String start = s.substring(0,1);
            if("0".equals(start)){
                etAmount.setText("0");
                amount = 0;
            }
        }
    }

    public void setStock(int stock) {
        this.stock = stock;
    }




    public String getText(){
        return etAmount.getText().toString();
    }

}
