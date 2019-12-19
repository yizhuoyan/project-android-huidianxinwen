package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.jason.hdxw.adapter.MyTeamAdapter;
import com.jason.hdxw.base.TransparencyBarActivity;
import com.jason.hdxw.bean.MyTeamBean;
import com.jason.hdxw.utils.UserCache;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.MEMBER_MY_TEAM;

/**
 * 我的团队页面
 * created by wang on 2018/11/14
 */
public class MyTeamActivity extends TransparencyBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_myteam_back)
    ImageView mIvMyteamBack;
    @BindView(R.id.relative_myteam_title)
    RelativeLayout mRelativeMyteamTitle;
    @BindView(R.id.linear_myteam_bg)
    LinearLayout mLinearMyteamBg;
    @BindView(R.id.tv_myteam_one)
    TextView mTvMyteamOne;
    @BindView(R.id.tv_myteam_two)
    TextView mTvMyteamTwo;
    @BindView(R.id.tv_myteam_three)
    TextView mTvMyteamThree;
    @BindView(R.id.linear_myteam_count)
    LinearLayout mLinearMyteamCount;
    @BindView(R.id.tab_myteam)
    TabLayout mTabMyteam;
    @BindView(R.id.tv_myteam_invite)
    TextView mTvMyteamInvite;
    @BindView(R.id.recycler_myteam)
    RecyclerView mRecyclerMyteam;
    @BindView(R.id.refreshlayout_myteam)
    SmartRefreshLayout mRefreshlayoutMyteam;
    @BindView(R.id.relative_myteam_nodata)
    RelativeLayout mRelativeMyteamNodata;
    @BindView(R.id.tv_myteam_count)
    TextView mTvMyteamCount;

    private List<MyTeamBean.UserWithdrawBean> mList = new ArrayList();
    private MyTeamAdapter mAdapter = new MyTeamAdapter(this, mList);
    private MyTeamBean mMyTeamBean;
    //数据页数
    private int mPage = 0;
    //当前数据分类 默认一级分类
    private String mType = "1";
    private Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myteam);
        ButterKnife.bind(this);
        inivView();
        initData();
        initListener();
    }

    private void initListener() {
        mRefreshlayoutMyteam.setEnableAutoLoadMore(false);
        mRefreshlayoutMyteam.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
        mRefreshlayoutMyteam.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
//        mRefreshlayoutMyteam.autoRefresh();//自动刷新
        mRefreshlayoutMyteam.setEnableRefresh(true);//是否启用下拉刷新功能
        mRefreshlayoutMyteam.setEnableLoadMore(true);//是否启用上拉加载功能
        mRefreshlayoutMyteam.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                mList.clear();
                mPage = 0;
                refreshData(mPage + "", mType, refreshlayout);
            }
        });
        mRefreshlayoutMyteam.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                refreshData(mPage + "", mType, refreshlayout);
            }
        });
        mTabMyteam.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mList.clear();
                    mPage = 0;
                    mType = "1";
                    refreshData(mPage + "", mType, null);
                } else if (tab.getPosition() == 1) {
                    mList.clear();
                    mPage = 0;
                    mType = "2";
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
    }

    private void initData() {
        mRecyclerMyteam.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerMyteam.setAdapter(mAdapter);
        refreshData(mPage + "", mType, null);
    }

    /**
     * 刷新数据
     *
     * @param page
     * @param type
     * @param refreshlayout
     */
    private void refreshData(final String page, String type, final RefreshLayout refreshlayout) {
        Logger.e("我的团队刷新数据： page= " + page + "  type= " + type);
        OkGo.<String>post(MEMBER_MY_TEAM)
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
                        Logger.e("我的团队接口返回数据：" + response.body());
                        try {
                            mMyTeamBean = mGson.fromJson(response.body(), MyTeamBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mMyTeamBean.getStatus().equals("y")) {
                            if (mMyTeamBean.getCount().getZong() != null) {
                                mTvMyteamCount.setText(mMyTeamBean.getCount().getZong());
                            }
                            if (mMyTeamBean.getCount().getYi_count() != null) {
                                mTvMyteamOne.setText(mMyTeamBean.getCount().getYi_count());
                            }
                            if (mMyTeamBean.getCount().getEr_count() != null) {
                                mTvMyteamTwo.setText(mMyTeamBean.getCount().getEr_count());
                            }
                            if (mMyTeamBean.getCount().getSan_count() != null) {
                                mTvMyteamThree.setText(mMyTeamBean.getCount().getSan_count());
                            }
                            if (mMyTeamBean.getUser_withdraw() != null && mMyTeamBean.getUser_withdraw().size() > 0) {
                                mRelativeMyteamNodata.setVisibility(View.GONE);
                                mList.addAll(mMyTeamBean.getUser_withdraw());
                                mAdapter.setList(mList);
                                mPage++;
                            } else {
                                if (page.equals("0")) {
                                    mRelativeMyteamNodata.setVisibility(View.VISIBLE);
                                } else {
                                    ToastUtils.show(getString(R.string.hint_noMoreData));
                                    if (refreshlayout != null) {
                                        refreshlayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
                                    }
                                }
                            }
                        } else {
                            mRelativeMyteamNodata.setVisibility(View.VISIBLE);
                            ToastUtils.show(mMyTeamBean.getMsg());
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

    private void inivView() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mRelativeMyteamTitle.getLayoutParams());
        lp.setMargins(0, result, 0, 0);
        mRelativeMyteamTitle.setLayoutParams(lp);
        mTabMyteam.addTab(mTabMyteam.newTab().setText("一级会员"));
        mTabMyteam.addTab(mTabMyteam.newTab().setText("二级会员"));
        mTabMyteam.addTab(mTabMyteam.newTab().setText("三级会员"));
    }

    @OnClick({R.id.iv_myteam_back, R.id.tv_myteam_invite})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_myteam_back:
                finish();
                break;
            //邀请好友
            case R.id.tv_myteam_invite:
                startActivity(InviteActivity.class);
                break;
        }
    }
}
