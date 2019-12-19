package com.jason.hdxw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.jason.hdxw.R;
import com.jason.hdxw.bean.MyTeamBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的团队适配器
 * created by wang on 2018/11/14
 */
public class MyTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MyTeamBean.UserWithdrawBean> mList;

    public MyTeamAdapter(Context context, List<MyTeamBean.UserWithdrawBean> list) {
        mContext = context;
        mList = list;
    }

    public void setList(List<MyTeamBean.UserWithdrawBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mytean, viewGroup, false);
        return new TeamHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof TeamHolder) {
            if (mList.get(i).getUsername() != null) {
                ((TeamHolder) viewHolder).tv_name.setText(mList.get(i).getUsername());
            }
            if (mList.get(i).getReg_time() != null) {
                ((TeamHolder) viewHolder).tv_time.setText(mList.get(i).getReg_time());
            }
            if (mList.get(i).getIco() != null) {
                Picasso.get()
                        .load(mList.get(i).getIco())
                        .error(R.drawable.ico_head)
                        .fit()
                        .centerCrop()
                        .into(((TeamHolder) viewHolder).iv_head);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TeamHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_head;
        private TextView tv_name, tv_time;

        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_myteam_name_item);
            tv_time = itemView.findViewById(R.id.tv_myteam_time_item);
            iv_head = itemView.findViewById(R.id.iv_myteam_head_item);

        }
    }
}
