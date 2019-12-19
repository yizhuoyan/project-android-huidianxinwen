package com.jason.hdxw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.jason.hdxw.R;
import com.jason.hdxw.adapter.NoticeListAdapter;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.NoticeListBean;
import com.jason.hdxw.utils.UserCache;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_NOTICE_LIST;

/**
 * 公告页面
 * created by wang on 2018/11/12
 */
public class NoticeActivity extends WhiteBarActivity implements NoticeListAdapter.NoticeListClick, View.OnClickListener {

    @BindView(R.id.recycler_notice)
    RecyclerView mRecyclerNotice;
    @BindView(R.id.refreshlayout_notice)
    SmartRefreshLayout mRefreshlayoutNotice;
    @BindView(R.id.iv_notice_back)
    ImageView mIvNoticeBack;
    @BindView(R.id.relative_notice_nodata)
    RelativeLayout mRelativeNoticeNodata;
    private NoticeListAdapter mAdapter = new NoticeListAdapter(this, new ArrayList<NoticeListBean.SelectBean>());
    private NoticeListBean mNoticeListBean;
    private Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initListener() {
        mAdapter.setClick(this);
        //是否启用列表惯性滑动到底部时自动加载更多
        mRefreshlayoutNotice.setEnableAutoLoadMore(false);
        mRefreshlayoutNotice.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
        mRefreshlayoutNotice.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
//        mRefreshlayoutNotice.autoRefresh();//自动刷新
        mRefreshlayoutNotice.setEnableRefresh(true);//是否启用下拉刷新功能
        mRefreshlayoutNotice.setEnableLoadMore(false);//是否启用上拉加载功能
        mRefreshlayoutNotice.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(1000);//传入false表示刷新失败
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                getNoticeList(refreshlayout);

            }
        });
    }

    /**
     * 获取公告列表
     */
    private void getNoticeList(final RefreshLayout refreshlayout) {
        OkGo.<String>post(BASICS_NOTICE_LIST)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        refreshlayout.finishRefresh();
                        Logger.e("公告列表接口返回数据：" + response.body());
                        try {
                            mNoticeListBean = mGson.fromJson(response.body(), NoticeListBean.class);
                            if (mNoticeListBean.getStatus().equals("y")) {
                                if (mNoticeListBean.getSelect() != null && mNoticeListBean.getSelect().size() > 0) {
                                    mRelativeNoticeNodata.setVisibility(View.GONE);
                                    mAdapter.setList(mNoticeListBean.getSelect());
                                } else {
                                    mRelativeNoticeNodata.setVisibility(View.VISIBLE);
                                }
                            } else {
                                mRelativeNoticeNodata.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        refreshlayout.finishRefresh();
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
    }

    private void initData() {
        mRecyclerNotice.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerNotice.setAdapter(mAdapter);
        mNoticeListBean = (NoticeListBean) getIntent().getSerializableExtra("noticeList");
        if (mNoticeListBean != null) {
            if (mNoticeListBean.getStatus().equals("y")) {
                if (mNoticeListBean.getSelect() != null && mNoticeListBean.getSelect().size() > 0) {
                    mRelativeNoticeNodata.setVisibility(View.GONE);
                    mAdapter.setList(mNoticeListBean.getSelect());
                } else {
                    mRelativeNoticeNodata.setVisibility(View.VISIBLE);
                }
            } else {
                mRelativeNoticeNodata.setVisibility(View.VISIBLE);
            }
        } else {
            mRelativeNoticeNodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noticeClick(int position) {
        Logger.e("查看公告详情的id：" + mNoticeListBean.getSelect().get(position).getId());
        Intent intent = new Intent(this, NoticeDetailsActivity.class);
        intent.putExtra("noticeId", mNoticeListBean.getSelect().get(position).getId());
        startActivity(intent);
    }

    @OnClick(R.id.iv_notice_back)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_notice_back:
                finish();
                break;
        }
    }
}
