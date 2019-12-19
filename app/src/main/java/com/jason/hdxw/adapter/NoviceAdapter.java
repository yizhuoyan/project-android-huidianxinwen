package com.jason.hdxw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.bean.NoviceCourseBean;

import java.util.List;

/**
 * 新手教程适配器
 * created by wang on 2018/11/16
 */
public class NoviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NoviceCourseBean.SelectBean> mList;
    private NoviceClick mClick;

    public NoviceAdapter(Context context, List<NoviceCourseBean.SelectBean> list) {
        mContext = context;
        mList = list;
    }

    public void setList(List<NoviceCourseBean.SelectBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setClick(NoviceClick click) {
        mClick = click;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_novice, viewGroup, false);
        return new NoviceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof NoviceHolder) {
            ((NoviceHolder) viewHolder).tv_name.setText(mList.get(i).getTitle());
            ((NoviceHolder) viewHolder).tv_content.setText(mList.get(i).getContent());
            //展开
            if (mList.get(i).isSelect()) {
                ((NoviceHolder) viewHolder).iv_arrows.setImageDrawable(mContext.getResources().getDrawable(R.drawable.i_backtop));
                ((NoviceHolder) viewHolder).lin_content.setVisibility(View.VISIBLE);
            } else {
                //收起
                ((NoviceHolder) viewHolder).iv_arrows.setImageDrawable(mContext.getResources().getDrawable(R.drawable.i_backbottom));
                ((NoviceHolder) viewHolder).lin_content.setVisibility(View.GONE);
            }
            ((NoviceHolder) viewHolder).relative_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClick != null) {
                        mClick.noviceClick(i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface NoviceClick {
        void noviceClick(int i);
    }

    class NoviceHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_content;
        private ImageView iv_arrows;
        private LinearLayout lin_content;
        private RelativeLayout relative_click;

        public NoviceHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_novice_name_item);
            tv_content = itemView.findViewById(R.id.tv_novice_content_item);
            iv_arrows = itemView.findViewById(R.id.iv_novice_arrows_item);
            lin_content = itemView.findViewById(R.id.linear_novice_content_item);
            relative_click = itemView.findViewById(R.id.relative_novice_click);
        }
    }
}
