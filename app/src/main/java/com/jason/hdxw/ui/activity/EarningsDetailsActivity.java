package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.jason.hdxw.adapter.EarningsDetailsAdapter;
import com.jason.hdxw.base.TransparencyBarActivity;
import com.jason.hdxw.bean.EarningsDetailsBean;
import com.jason.hdxw.utils.UserCache;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.MEMBER_EARNINGS_DETAILS;

/**
 * 收益明细页面
 * created by wang on 2018/11/14
 */
public class EarningsDetailsActivity extends TransparencyBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_earningsdetails_back)
    ImageView mIvEarningsdetailsBack;
    @BindView(R.id.relative_earnings_title)
    View mRelativeEarningsTitle;
    @BindView(R.id.tab_earningsdetails)
    TabLayout mTabEarningsdetails;
    @BindView(R.id.recycler_earningsdetails)
    RecyclerView mRecyclerEarningsdetails;
    @BindView(R.id.refreshlayout_earningsdetails)
    SmartRefreshLayout mRefreshlayoutEarningsdetails;
    @BindView(R.id.relative_earningsdetails_norecord)
    RelativeLayout mRelativeEarningsdetailsNorecord;
    @BindView(R.id.tv_earningsdetails_amount)
    TextView mTvEarningsdetailsAmount;
    @BindView(R.id.tv_earningsdetails_today)
    TextView mTvEarningsdetailsToday;

    private List<EarningsDetailsBean.UserWithdrawBean> mList = new ArrayList();
    private EarningsDetailsAdapter mAdapter = new EarningsDetailsAdapter(this, mList);
    //数据页数
    private int mPage = 0;
    //当前数据分类 默认浏览收益
    private String mType = "2";
    private Gson mGson = new Gson();
    private EarningsDetailsBean mEarningsDetailsBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earningsdetails);
        ButterKnife.bind(this);
        inivView();
        initData();
        initListener();
    }

    private void initData() {
        mRecyclerEarningsdetails.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerEarningsdetails.setAdapter(mAdapter);
        refreshData(mPage + "", mType, null);
    }

    /**
     * 刷新数据
     */
    private void refreshData(final String page, String type, final RefreshLayout refreshlayout) {
        Logger.e("收益明细刷新数据： page= " + page + "  type= " + type);
        OkGo.<String>post(MEMBER_EARNINGS_DETAILS)
                .params("token", UserCache.getToken())
                .params("type", type)
                .params("page", page)
                .params("pagesize", "15")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (refreshlayout != null) {
                            refreshlayout.finishLoadMore();
                            refreshlayout.finishRefresh();
                        }
                        Logger.e("收益明细接口返回数据：" + response.body());
                        try {
                            mEarningsDetailsBean = mGson.fromJson(response.body(), EarningsDetailsBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mEarningsDetailsBean.getStatus().equals("y")) {
                            mTvEarningsdetailsAmount.setText(mEarningsDetailsBean.getZong_money() + "");
                            mTvEarningsdetailsToday.setText(getString(R.string.index_today) + mEarningsDetailsBean.getJin_money());
                            if (mEarningsDetailsBean.getUser_withdraw() != null && mEarningsDetailsBean.getUser_withdraw().size() > 0) {
                                mRelativeEarningsdetailsNorecord.setVisibility(View.GONE);
                                mList.addAll(mEarningsDetailsBean.getUser_withdraw());
                                mAdapter.setList(mList);
                                mPage++;
                            } else {
                                if (page.equals("0")) {
                                    mRelativeEarningsdetailsNorecord.setVisibility(View.VISIBLE);
                                } else {
                                    ToastUtils.show(getString(R.string.hint_noMoreData));
                                    if (refreshlayout != null) {
                                        refreshlayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
                                    }
                                }
                            }
                        } else {
                            mRelativeEarningsdetailsNorecord.setVisibility(View.VISIBLE);
                            ToastUtils.show(mEarningsDetailsBean.getMsg());
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

    private void initListener() {
        mTabEarningsdetails.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mList.clear();
                    mPage = 0;
                    mType = "2";
                    refreshData(mPage + "", mType, null);
                } else if (tab.getPosition() == 1) {
                    mList.clear();
                    mPage = 0;
                    mType = "4";
                    refreshData(mPage + "", mType, null);
                } else if (tab.getPosition() == 2) {
                    mList.clear();
                    mPage = 0;
                    mType = "3";
                    refreshData(mPage + "", mType, null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mRefreshlayoutEarningsdetails.setEnableAutoLoadMore(false);
        mRefreshlayoutEarningsdetails.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
        mRefreshlayoutEarningsdetails.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
//        mRefreshlayoutEarningsdetails.autoRefresh();//自动刷新
        mRefreshlayoutEarningsdetails.setEnableRefresh(true);//是否启用下拉刷新功能
        mRefreshlayoutEarningsdetails.setEnableLoadMore(true);//是否启用上拉加载功能
        mRefreshlayoutEarningsdetails.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                mList.clear();
                mPage = 0;
                refreshData(mPage + "", mType, refreshlayout);
            }
        });
        mRefreshlayoutEarningsdetails.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                refreshData(mPage + "", mType, refreshlayout);
            }
        });
    }

    private void inivView() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mRelativeEarningsTitle.getLayoutParams();
        //System.out.println(lp.getClass());
        lp.setMargins(0, result, 0, 0);
        mRelativeEarningsTitle.setLayoutParams(lp);

//        mTabEarningsdetails.addTab(mTabEarningsdetails.newTab().setText("全部"));
        mTabEarningsdetails.addTab(mTabEarningsdetails.newTab().setText("浏览收益"));
        mTabEarningsdetails.addTab(mTabEarningsdetails.newTab().setText("广告分润"));
//        mTabEarningsdetails.addTab(mTabEarningsdetails.newTab().setText("推广分红"));
    }

    @OnClick(R.id.iv_earningsdetails_back)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_earningsdetails_back:
                finish();
                break;
        }
    }

}
