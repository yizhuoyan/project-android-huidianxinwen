package com.jason.hdxw.ui.activity;

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
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.jason.hdxw.R;
import com.jason.hdxw.adapter.KitingListAdapter;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.KitingDetailsBean;
import com.jason.hdxw.utils.UserCache;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.MEMBER_KITING_DETAILS;

/**
 * 提现明细页面
 * created by wang on 2018/11/14
 */
public class KitingDetailsActivity extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_kitingdetails_back)
    ImageView mIvKitingdetailsBack;
    @BindView(R.id.recycler_kitingdetails)
    RecyclerView mRecyclerKitingdetails;
    @BindView(R.id.refreshlayout_kitingdetails)
    SmartRefreshLayout mRefreshlayoutKitingdetails;
    @BindView(R.id.relative_kitingdetails_norecord)
    RelativeLayout mRelativeKitingdetailsNorecord;

    private List<KitingDetailsBean.UserWithdrawBean> mList = new ArrayList();
    private KitingListAdapter mAdapter = new KitingListAdapter(this, mList);
    //数据页数
    private int mPage = 0;
    private KitingDetailsBean mKitingDetailsBean;
    private Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitingdetails);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initListener() {
        mRefreshlayoutKitingdetails.setEnableAutoLoadMore(false);
        mRefreshlayoutKitingdetails.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
        mRefreshlayoutKitingdetails.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
//        mRefreshlayoutKitingdetails.autoRefresh();//自动刷新
        mRefreshlayoutKitingdetails.setEnableRefresh(true);//是否启用下拉刷新功能
        mRefreshlayoutKitingdetails.setEnableLoadMore(true);//是否启用上拉加载功能
        mRefreshlayoutKitingdetails.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                mList.clear();
                mPage = 0;
                refreshData(mPage + "", refreshlayout);
            }
        });
        mRefreshlayoutKitingdetails.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                refreshData(mPage + "", refreshlayout);
            }
        });
    }

    private void initData() {
        mRecyclerKitingdetails.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerKitingdetails.setAdapter(mAdapter);
        refreshData(mPage + "", null);
    }

    /**
     * 刷新数据
     */
    private void refreshData(final String page, final RefreshLayout refreshlayout) {
        OkGo.<String>post(MEMBER_KITING_DETAILS)
                .params("token", UserCache.getToken())
                .params("page", page)
                .params("pagesize", "15")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (refreshlayout != null) {
                            refreshlayout.finishLoadMore();
                            refreshlayout.finishRefresh();
                        }
                        Logger.e("提现明细接口返回数据：" + response.body());
                        try {
                            mKitingDetailsBean = mGson.fromJson(response.body(), KitingDetailsBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mKitingDetailsBean.getStatus().equals("y")) {
                            if (mKitingDetailsBean.getUser_withdraw() != null && mKitingDetailsBean.getUser_withdraw().size() > 0) {
                                mRelativeKitingdetailsNorecord.setVisibility(View.GONE);
                                mList.addAll(mKitingDetailsBean.getUser_withdraw());
                                mAdapter.setList(mList);
                                mPage++;
                            } else {
                                if (page.equals("0")) {
                                    mRelativeKitingdetailsNorecord.setVisibility(View.VISIBLE);
                                } else {
                                    ToastUtils.show(getString(R.string.hint_noMoreData));
                                    if (refreshlayout != null) {
                                        refreshlayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
                                    }
                                }
                            }
                        } else {
                            mRelativeKitingdetailsNorecord.setVisibility(View.VISIBLE);
                            ToastUtils.show(mKitingDetailsBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                        if (refreshlayout != null) {
                            refreshlayout.finishLoadMore();
                            refreshlayout.finishRefresh();
                        }
                    }
                });
    }

    @OnClick(R.id.iv_kitingdetails_back)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_kitingdetails_back:
                finish();
                break;
        }
    }
}
