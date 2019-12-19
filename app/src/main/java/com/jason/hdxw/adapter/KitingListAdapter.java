package com.jason.hdxw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.bean.KitingDetailsBean;

import java.util.List;

/**
 * 提现明细适配器
 * created by wang on 2018/11/14
 */
public class KitingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<KitingDetailsBean.UserWithdrawBean> mList;

    public KitingListAdapter(Context context, List<KitingDetailsBean.UserWithdrawBean> list) {
        mContext = context;
        mList = list;
    }

    public void setList(List<KitingDetailsBean.UserWithdrawBean> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_kitinglist, viewGroup, false);
        return new KitingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof KitingHolder) {
            if (mList.get(i).getMsg() != null) {
                ((KitingHolder) viewHolder).tv_name.setText(mList.get(i).getMsg());
            }
            if (mList.get(i).getDateline() != null) {
                ((KitingHolder) viewHolder).tv_time.setText(mList.get(i).getDateline());
            }
//            if (mList.get(i).getMoney() != null) {
//                ((KitingHolder) viewHolder).tv_money.setText("￥ " + mList.get(i).getMoney());
//            }
            if (mList.get(i).getType_msg() != null) {
                ((KitingHolder) viewHolder).tv_type.setText(mList.get(i).getType_msg());
                if (mList.get(i).getWithdraw_status() != null) {
                    if (mList.get(i).getWithdraw_status().equals("1")) {
                        ((KitingHolder) viewHolder).tv_type.setTextColor(mContext.getResources().getColor(R.color.kiting_green));
                    } else {
                        ((KitingHolder) viewHolder).tv_type.setTextColor(mContext.getResources().getColor(R.color.kiting_red));
                    }
                }
            }
//            if (mList.get(i).getMsg() != null) {
//                ((KitingHolder) viewHolder).tv_name.setText(mList.get(i).getMsg());
//            }
//            if (mList.get(i).getDateline() != null) {
//                ((KitingHolder) viewHolder).tv_time.setText(mList.get(i).getDateline());
//            }
//            if (mList.get(i).getJiajian() != null && mList.get(i).getMoney() != null) {
//                ((KitingHolder) viewHolder).tv_money.setText("￥ " + mList.get(i).getMoney());
//
////                if (mList.get(i).getJiajian().equals("1")) {
////                    ((KitingHolder) viewHolder).tv_money.setText("￥ + " + mList.get(i).getMoney());
////                } else {
////                    ((KitingHolder) viewHolder).tv_money.setText("￥ - " + mList.get(i).getMoney());
////                }
//            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class KitingHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_money, tv_time, tv_type;

        public KitingHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_kitinglist_name_item);
//            tv_money = itemView.findViewById(R.id.tv_kitinglist_money_item);
            tv_time = itemView.findViewById(R.id.tv_kitinglist_time_item);
            tv_type = itemView.findViewById(R.id.tv_kitinglist_type_item);
        }
    }
}
