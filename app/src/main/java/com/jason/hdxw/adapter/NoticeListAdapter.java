package com.jason.hdxw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.bean.NoticeListBean;
import com.jason.hdxw.utils.DensityUtil;

import java.util.List;

/**
 * 公告列表适配器
 * created by wang on 2018/11/12
 */
public class NoticeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NoticeListBean.SelectBean> mList;
    private NoticeListClick mClick;
    private String type = "0";
    private LinearLayout.LayoutParams lp;

    public NoticeListAdapter(Context context, List<NoticeListBean.SelectBean> list) {
        mContext = context;
        mList = list;
    }

    public void setList(List<NoticeListBean.SelectBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setClick(NoticeListClick click) {
        mClick = click;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_noticelist, viewGroup, false);
        return new NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        //伪造已读和未读的标识
        if (viewHolder instanceof NoticeHolder) {
            if (position == mList.size() - 1) {
                ((NoticeHolder) viewHolder).line.setVisibility(View.GONE);
            } else {
                ((NoticeHolder) viewHolder).line.setVisibility(View.VISIBLE);
            }
            if (mList.get(position).getTitle() != null) {
                ((NoticeHolder) viewHolder).tv_title.setText(mList.get(position).getTitle());
            }
            if (mList.get(position).getStarttime() != null) {
                ((NoticeHolder) viewHolder).tv_time.setText(mList.get(position).getStarttime());
            }
            if (mList.get(position).getContent() != null) {
                ((NoticeHolder) viewHolder).tv_content.setText(mList.get(position).getContent());
            }
            if (type.equals("0")) {
                //已读
                ((NoticeHolder) viewHolder).iv_type.setVisibility(View.GONE);
                ((NoticeHolder) viewHolder).tv_title.setTextColor(mContext.getResources().getColor(R.color.black));
                lp = new LinearLayout.LayoutParams(((NoticeHolder) viewHolder).tv_title.getLayoutParams());
                lp.setMargins(DensityUtil.dip2px(mContext, 15), 0, 0, 0);
                ((NoticeHolder) viewHolder).tv_title.setLayoutParams(lp);
            } else {
                //未读a
                ((NoticeHolder) viewHolder).iv_type.setVisibility(View.VISIBLE);
                lp = new LinearLayout.LayoutParams(((NoticeHolder) viewHolder).iv_type.getLayoutParams());
                lp.setMargins(DensityUtil.dip2px(mContext, 8), 0, 0, 0);
                ((NoticeHolder) viewHolder).iv_type.setLayoutParams(lp);
                ((NoticeHolder) viewHolder).tv_title.setTextColor(mContext.getResources().getColor(R.color.black));
                lp = new LinearLayout.LayoutParams(((NoticeHolder) viewHolder).tv_title.getLayoutParams());
                lp.setMargins(DensityUtil.dip2px(mContext, 2), 0, 0, 0);
                ((NoticeHolder) viewHolder).tv_title.setLayoutParams(lp);
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClick != null) {
                        mClick.noticeClick(position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class NoticeHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_time, tv_content;
        private ImageView iv_type;
        private View line;

        public NoticeHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_noticelist_title_item);
            tv_time = itemView.findViewById(R.id.tv_noticelist_time_item);
            tv_content = itemView.findViewById(R.id.tv_noticelist_content_item);
            iv_type = itemView.findViewById(R.id.iv_noticelist_type_item);
            line = itemView.findViewById(R.id.view_noticelist_line_item);
        }
    }

    public interface NoticeListClick {
        void noticeClick(int position);
    }
}
