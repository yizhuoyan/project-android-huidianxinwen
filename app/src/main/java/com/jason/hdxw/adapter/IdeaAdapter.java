package com.jason.hdxw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.bean.MsgReturnBean;

import java.util.List;

/**
 * 意见反馈适配器
 * created by wang on 2018/12/3
 */
public class IdeaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MsgReturnBean.ListBean> mList;

    public IdeaAdapter(Context context, List<MsgReturnBean.ListBean> list) {
        mContext = context;
        mList = list;
    }

    public void setList(List<MsgReturnBean.ListBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_idea, viewGroup, false);
        return new IdeaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof IdeaHolder) {
            if (i == mList.size() - 1) {
                ((IdeaHolder) viewHolder).mView.setVisibility(View.GONE);
            } else {
                ((IdeaHolder) viewHolder).mView.setVisibility(View.VISIBLE);
            }
            if (mList.get(i).getIs_reply().equals("0")) {
                ((IdeaHolder) viewHolder).lin_content.setVisibility(View.GONE);
            } else {
                ((IdeaHolder) viewHolder).lin_content.setVisibility(View.VISIBLE);
            }
            if (mList.get(i).getContent() != null) {
                ((IdeaHolder) viewHolder).tv_title.setText(mList.get(i).getContent());
            }
            if (mList.get(i).getReply() != null) {
                ((IdeaHolder) viewHolder).tv_content.setText(mList.get(i).getReply());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class IdeaHolder extends RecyclerView.ViewHolder {
        private LinearLayout lin_content;
        private TextView tv_title, tv_content;
        private View mView;

        public IdeaHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_idea_title_item);
            tv_content = itemView.findViewById(R.id.tv_idea_content_item);
            lin_content = itemView.findViewById(R.id.linear_idea_content_item);
            mView = itemView.findViewById(R.id.view_idea_item);
        }
    }
}
