package com.xywy.askforexpert.module.main.service.que.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.RefreshBaseAdapter;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.main.service.que.model.QuestionNote;
import com.xywy.askforexpert.widget.expandabletextview.ExpandableTextView;

import java.math.BigDecimal;
import java.util.List;

import static com.xywy.askforexpert.appcommon.utils.YMOtherUtils.handleStar;

public class QueItemAdapter extends RefreshBaseAdapter {
    /**
     * 要删除的position
     */
    public int remove_position = -1;
    private Context context;
    private List<QuestionNote> listDatas;
    private QueItemAdapter.OnItemClickListener listener;

    private boolean useFooter;

    private boolean mIsFromHistory;//历史回复

    private static final String Q_TYPE_1 = "1";//指定付费
    private static final String Q_TYPE_2 = "2";//悬赏
    private static final String Q_TYPE_3 = "3";//绩效

    private static final String Q_TYPE_1_VALUE = "指定";//指定付费
    private static final String Q_TYPE_2_VALUE = "悬赏";//悬赏
    private static final String Q_TYPE_3_VALUE = "绩效";//绩效
    private static final String TYPE_ZHUIWEN = "zhuiwen";//帖子类型为追问
    private static final String ZHUIWEN = "追问";//追问
    private SpannableString mSsp;
    private StringBuilder stringBuilder;

    private final SparseBooleanArray mCollapsedStatus;
    private QuestionNote questionNote;

    public QueItemAdapter(Context context) {
        this.context = context;
        mCollapsedStatus = new SparseBooleanArray();
    }


    public void setHistory(boolean is) {
        this.mIsFromHistory = is;
    }

    public void setList(List<QuestionNote> listDatas) {
        this.listDatas = listDatas;
    }

    public void bindItemData(List<QuestionNote> listDatas) {
        this.listDatas = listDatas;
        notifyDataSetChanged();
    }

    @Override
    public boolean useFooter() {
        return useFooter;
    }

    @Override
    public ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_new, null);
        QueItemAdapter.QueViewHolder holder = new QueItemAdapter.QueViewHolder(view);
        return holder;
    }

    @Override
    public void onBindContentItemView(ViewHolder holder, final int position) {
        QueViewHolder queViewHolder = (QueViewHolder) holder;
        questionNote = listDatas.get(position);
        if(null != questionNote){
            //stone 添加评价 有评分就有评语
            if (questionNote.grade != null
                    && !TextUtils.isEmpty(listDatas.get(position).grade.getStar())) {
                queViewHolder.layout_comment.setVisibility(View.VISIBLE);

                queViewHolder.expand_text_view.setText(listDatas.get(position).grade.getCon(), mCollapsedStatus, position);

                handleStar(queViewHolder.iv_star, Double.parseDouble(new BigDecimal(listDatas.get(position).grade.getStar()).multiply(new BigDecimal("2")).setScale(2, BigDecimal.ROUND_HALF_UP) + ""));

            } else {
                queViewHolder.layout_comment.setVisibility(View.GONE);
            }

            DLog.d("my reply", "age = " + listDatas.get(position).age);
            queViewHolder.tvPatientName.setText(listDatas.get(position).nickname);
            if (TextUtils.isEmpty(questionNote.sex)) {
                queViewHolder.tvPatientSex.setVisibility(View.GONE);
            } else {
                queViewHolder.tvPatientSex.setText(questionNote.sex);
            }

            if (TextUtils.isEmpty(questionNote.age)) {
                queViewHolder.tvPatientAge.setVisibility(View.GONE);
            } else {
                if ("0".equals(listDatas.get(position).age)) {
                    queViewHolder.tvPatientAge.setVisibility(View.GONE);
                } else {
                    queViewHolder.tvPatientAge.setVisibility(View.VISIBLE);
                    queViewHolder.tvPatientAge.setText(questionNote.age);
                }
            }
            //历史回复用createtime 否则用time stone
            if (mIsFromHistory) {
                queViewHolder.tvTime.setText(TextUtils.isEmpty(questionNote.createtime) ? questionNote.time : questionNote.createtime);
            } else {
                queViewHolder.tvTime.setText(questionNote.time);
            }

            //stone
            stringBuilder = new StringBuilder("");

            //历史回复用givepoint 否则用money stone
            if (Q_TYPE_1.equals(questionNote.q_type)) {
                stringBuilder.append("[" + Q_TYPE_1_VALUE + " " + (mIsFromHistory ? questionNote.givepoint : questionNote.money) + (Q_TYPE_3.equals(questionNote.q_type) ? "] " : "元] "));
            } else if (Q_TYPE_2.equals(listDatas.get(position).q_type)) {
                stringBuilder.append("[" + Q_TYPE_2_VALUE + " " + (mIsFromHistory ? questionNote.givepoint : questionNote.money) + (Q_TYPE_3.equals(questionNote.q_type) ? "] " : "元] "));
            } else if (Q_TYPE_3.equals(questionNote.q_type)) {
                stringBuilder.append("[" + Q_TYPE_3_VALUE + "] ");
            }

            //stone 添加追问
            if (TYPE_ZHUIWEN.equals(questionNote.type)) {
                stringBuilder.append(ZHUIWEN + " ");
            }
            stringBuilder.append(questionNote.con);

            mSsp = new SpannableString(stringBuilder);


            if (TYPE_ZHUIWEN.equals(questionNote.type)) {
                int end = stringBuilder.indexOf(ZHUIWEN) + 2;
                mSsp.setSpan(new ForegroundColorSpan(Color.parseColor("#f4aa29")), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (!TextUtils.isEmpty(stringBuilder) && stringBuilder.toString().startsWith("[")) {
                int end = stringBuilder.indexOf("]") + 1;
                if (end > 0) {
                    mSsp.setSpan(new ForegroundColorSpan(Color.parseColor("#f4aa29")), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            queViewHolder.tvPatientDesc.setText(mSsp);
            if(!TextUtils.isEmpty(questionNote.level) && Constants.EIGHT_STR.equals(questionNote.level)){
                queViewHolder.tv_notice_dot.setVisibility(View.VISIBLE);
            }else {
                queViewHolder.tv_notice_dot.setVisibility(View.INVISIBLE);
            }


//        queViewHolder.tvSubject.setText(listDatas.get(position).subject);

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        if (null != listener) {
                            listener.onItemClick(position, listDatas.get(position));
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "请重新刷新列表", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "请重新刷新列表", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isAwardShow(String from) {
        return "award".equals(from) || "pay_ques".equals(from);
    }

    @Override
    public int getContentItemCount() {
        return listDatas == null ? 0 : listDatas.size();
    }

    @Override
    public int getContentItemType(int position) {
        return 0;
    }

    /**
     * 设置监听方法 * * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setUseFooter(boolean useFooter) {
        this.useFooter = useFooter;
    }

    /**
     * 内部接口回调方法
     */
    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }

    private class QueViewHolder extends ViewHolder {

        private TextView tvPatientName, tvPatientSex, tvPatientAge, tvTime, tvPatientDesc,tv_notice_dot;
//                tvSubject, tvAwardMoney;

        //新增的评价 stone
        private View layout_comment;
        private ImageView iv_star;
        private ExpandableTextView expand_text_view;


        public QueViewHolder(View itemView) {
            super(itemView);
            tvPatientName = (TextView) itemView.findViewById(R.id.tv_patient_name);
            tvPatientSex = (TextView) itemView.findViewById(R.id.tv_patient_sex);
            tvPatientAge = (TextView) itemView.findViewById(R.id.tv_patient_age);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvPatientDesc = (TextView) itemView.findViewById(R.id.tv_patient_desc);
            tv_notice_dot = (TextView) itemView.findViewById(R.id.tv_notice_dot);
//            tvSubject = (TextView) itemView.findViewById(R.id.tv_subject);
//            tvAwardMoney = (TextView) itemView.findViewById(R.id.tv_award_money);
            //新增的评价 stone
            layout_comment = itemView.findViewById(R.id.layout_comment);
            iv_star = (ImageView) itemView.findViewById(R.id.iv_star);
            expand_text_view = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);

        }
    }

}
