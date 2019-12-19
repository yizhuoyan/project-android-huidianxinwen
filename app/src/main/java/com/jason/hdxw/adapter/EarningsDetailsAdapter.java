package com.jason.hdxw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.bean.EarningsDetailsBean;

import java.util.List;

/**
 * 收益明细适配器
 * created by wang on 2018/11/14
 */
public class EarningsDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<EarningsDetailsBean.UserWithdrawBean> mList;

    public EarningsDetailsAdapter(Context context, List<EarningsDetailsBean.UserWithdrawBean> list) {
        mContext = context;
        mList = list;
    }

    public void setList(List<EarningsDetailsBean.UserWithdrawBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_earnings, viewGroup, false);
        return new EarningsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof EarningsHolder) {
            if (mList.get(i).getMsg() != null) {
                ((EarningsHolder) viewHolder).tv_name.setText(mList.get(i).getMsg());
            }
            if (mList.get(i).getDateline() != null) {
                ((EarningsHolder) viewHolder).tv_time.setText(mList.get(i).getDateline());
            }
            if (mList.get(i).getStatus() != null && mList.get(i).getMoney() != null) {
                ((EarningsHolder) viewHolder).tv_money.setText("￥ " + mList.get(i).getMoney());
//                if (mList.get(i).getStatus().equals("1")) {
//                    ((EarningsHolder) viewHolder).tv_money.setText("￥ + " + mList.get(i).getMoney());
//                } else {
//                    ((EarningsHolder) viewHolder).tv_money.setText("￥ - " + mList.get(i).getMoney());
//                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class EarningsHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_money, tv_time;

        public EarningsHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_earnings_name_item);
            tv_money = itemView.findViewById(R.id.tv_earnings_money_item);
            tv_time = itemView.findViewById(R.id.tv_earnings_time_item);
        }
    }
}