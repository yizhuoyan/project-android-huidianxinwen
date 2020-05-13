package com.jason.hdxw.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.RollMessage;
import com.jason.hdxw.base.UILazyFragment;
import com.jason.hdxw.bean.HomeEarningsBean;
import com.jason.hdxw.bean.NoticeListBean;
import com.jason.hdxw.ui.activity.EarningsDetailsActivity;
import com.jason.hdxw.ui.activity.IdeaActivity;
import com.jason.hdxw.ui.activity.InviteActivity;
import com.jason.hdxw.ui.activity.JournalismActivity;
import com.jason.hdxw.ui.activity.KitingActivity;
import com.jason.hdxw.ui.activity.KitingDetailsActivity;
import com.jason.hdxw.ui.activity.MyTeamActivity;
import com.jason.hdxw.ui.activity.NoticeActivity;
import com.jason.hdxw.ui.activity.NoviceActivity;
import com.jason.hdxw.utils.DensityUtil;
import com.jason.hdxw.utils.Strings;
import com.jason.hdxw.utils.UserCache;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jason.hdxw.api.API.BASICS_HOME_EARNINGS;
import static com.jason.hdxw.api.API.BASICS_NOTICE_LIST;
import static com.jason.hdxw.api.API.MEMBER_EXAMINE_MONEY;

/**
 * 首页
 * created by wang on 2018/11/29
 */
public class IndexFragment extends UILazyFragment implements View.OnClickListener {

    @BindView(R.id.btn_main_eye)
    ImageButton mBtnEye;

    @BindView(R.id.iv_index_noticeicon)
    ImageView mIvIndexNoticeicon;
    @BindView(R.id.rollmsg_index)
    RollMessage mRollmsgIndex;
    @BindView(R.id.relative_notice)
    RelativeLayout mRelativeNotice;
    @BindView(R.id.tv_index_balance)
    TextView mTvIndexBalance;
    @BindView(R.id.btn_index_kiting)
    TextView mTvIndexKiting;

    @BindView(R.id.tv_main_total_income)
    TextView mTvMainTotalIncome;
    @BindView(R.id.tv_main_today_income)
    TextView mTvMainTodayIncome;

    @BindView(R.id.linear_index_incomedetails)
    LinearLayout mLinearIndexIncomedetails;
    @BindView(R.id.linear_index_kiting)
    LinearLayout mLinearIndexKiting;
    @BindView(R.id.linear_index_novice)
    LinearLayout mLinearIndexNovice;
    @BindView(R.id.linear_index_myteam)
    LinearLayout mLinearIndexMyteam;
    @BindView(R.id.nestedsv_index)
    NestedScrollView mNestedsvIndex;
    @BindView(R.id.refreshlayout_index)
    SmartRefreshLayout mRefreshlayoutIndex;
    @BindView(R.id.tv_index_teamNum)
    TextView mTvIndexTeamNum;
    @BindView(R.id.linear_index_invite)
    LinearLayout mLinearIndexInvite;
    @BindView(R.id.linear_index_kiting_record)
    LinearLayout mLinearIndexKitingRecord;
    @BindView(R.id.linear_index_shop_enter)
    LinearLayout mLinearIndexShopEnter;
    @BindView(R.id.linear_index_shop)
    LinearLayout mLinearIndexShop;
    @BindView(R.id.linear_index_feedback)
    LinearLayout mLinearIndexFeedback;
    Unbinder unbinder;

    public Gson mGson = new Gson();
    public HomeEarningsBean mEarningsBean;
    private NoticeListBean mNoticeListBean;
    private List<View> mRollMsgviews = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        int result = 0;
        int resourceId = getSupportActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getSupportActivity().getResources().getDimensionPixelSize(resourceId);
        }

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mRelativeNotice.getLayoutParams();
        lp.setMargins(DensityUtil.dip2px(getSupportActivity(), 15), result + DensityUtil.dip2px(getSupportActivity(), 10), DensityUtil.dip2px(getSupportActivity(), 15), 0);
        mRelativeNotice.setLayoutParams(lp);
    }

    @Override
    protected void initData() {
        Logger.e("本地Token：" + UserCache.getToken());
        getEarnings(null);
        getNoticeList();
        initListener();
    }

    private void initListener() {
        mNestedsvIndex.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mRefreshlayoutIndex != null) {
                    mRefreshlayoutIndex.setEnabled(mNestedsvIndex.getScrollY() == 0);
                }
            }
        });
        mRefreshlayoutIndex.setEnableAutoLoadMore(false);
        mRefreshlayoutIndex.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
        mRefreshlayoutIndex.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
//        mRefreshlayoutIndex.autoRefresh();//自动刷新
        mRefreshlayoutIndex.setEnableRefresh(true);//是否启用下拉刷新功能
        mRefreshlayoutIndex.setEnableLoadMore(false);//是否启用上拉加载功能
        mRefreshlayoutIndex.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                getEarnings(refreshlayout);
            }
        });
    }

    /**
     * 获取收益信息
     */
    public void getEarnings(final RefreshLayout refreshlayout) {
        OkGo.<String>post(BASICS_HOME_EARNINGS)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (refreshlayout != null) {
                            refreshlayout.finishRefresh();
                        }
                        Logger.e("首页收益接口返回数据：" + response.body());
                        try {
                            mEarningsBean = mGson.fromJson(response.body(), HomeEarningsBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mEarningsBean.getStatus().equals("y")) {
                            if (mEarningsBean.getMoney().getJin_day() != null && mEarningsBean.getMoney().getJin_day().length() > 0) {
                                mTvMainTodayIncome.setText(mEarningsBean.getMoney().getJin_day());
                            }
                            if (mEarningsBean.getMoney().getZong_money() != null && mEarningsBean.getMoney().getZong_money().length() > 0) {
                                mTvMainTotalIncome.setText(mEarningsBean.getMoney().getZong_money());
                            }
                            if (mEarningsBean.getMoney().getUser_money() != null && mEarningsBean.getMoney().getUser_money().length() > 0) {
                                mTvIndexBalance.setText(mEarningsBean.getMoney().getUser_money());
                                //余额存到本地
                                UserCache.setBalance(mEarningsBean.getMoney().getUser_money());
                            }
                            if (mEarningsBean.getMoney().getZong() != null && mEarningsBean.getMoney().getZong().length() > 0) {
                                mTvIndexTeamNum.setText(mEarningsBean.getMoney().getZong());
                            }
                            if (mEarningsBean.getWalletUrl() != null && mEarningsBean.getWalletUrl().length() > 0) {
                                UserCache.setWalletURL(mEarningsBean.getWalletUrl());
                            }
                        } else {
                            ToastUtils.show(mEarningsBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                        if (refreshlayout != null) {
                            refreshlayout.finishRefresh();
                        }
                    }
                });
    }

    /**
     * 获取公告列表
     */
    public void getNoticeList() {
        OkGo.<String>post(BASICS_NOTICE_LIST)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("公告列表接口返回数据：" + response.body());
                        try {
                            mNoticeListBean = mGson.fromJson(response.body(), NoticeListBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mNoticeListBean.getStatus().equals("y")) {
                            if (mNoticeListBean.getSelect() != null && mNoticeListBean.getSelect().size() > 0) {
                                mRollMsgviews.clear();
                                for (int i = 0; i < mNoticeListBean.getSelect().size(); i++) {
                                    LinearLayout rollView = (LinearLayout) LayoutInflater.from(getSupportActivity()).inflate(R.layout.item_index_rollmsg, null);
                                    TextView tv = rollView.findViewById(R.id.tv_index_rollmsg_item);
                                    tv.setText(mNoticeListBean.getSelect().get(i).getTitle());
                                    mRollMsgviews.add(rollView);
                                }
                                //显示滚动物流
                                mRollmsgIndex.setViews(mRollMsgviews);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
    }

    /**
     * 检查是否可以赚钱
     */
    private void examineMoney() {
        OkGo.<String>post(MEMBER_EXAMINE_MONEY)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("是否可以赚钱返回数据：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                if (jsonObject.getString("checkinfo").equals("1")) {
                                    Intent intent1 = new Intent(getSupportActivity(), JournalismActivity.class);
                                    startActivityForResult(intent1, 88);
                                } else {
                                    ToastUtils.show(jsonObject.getString("msg"));
                                }
                            } else {
                                ToastUtils.show(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
    }

    @OnClick({R.id.btn_main_eye,R.id.relative_notice, R.id.btn_index_kiting, R.id.linear_index_incomedetails, R.id.linear_index_myteam,
            R.id.linear_index_novice, R.id.linear_index_shop_enter, R.id.btn_main_do_task, R.id.linear_index_shop,
            R.id.linear_index_feedback, R.id.linear_index_kiting_record, R.id.linear_index_invite, R.id.linear_index_kiting})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //公告
            case R.id.relative_notice:
                Intent intent = new Intent(getSupportActivity(), NoticeActivity.class);
                intent.putExtra("noticeList", mNoticeListBean);
                startActivity(intent);
                break;
            //显示/隐藏余额收益
            case R.id.btn_main_eye:
                toggleBalanceVisible();
                break;
            //提现
            case R.id.btn_index_kiting:
                startActivity(KitingActivity.class);
                break;
            //我要提现
            case R.id.linear_index_kiting:
                startActivity(KitingActivity.class);
                break;
            //执行任务
            case R.id.btn_main_do_task:
                examineMoney();
                break;
            //收益明细
            case R.id.linear_index_incomedetails:
                startActivity(EarningsDetailsActivity.class);
                break;
            //我的团队
            case R.id.linear_index_myteam:
                startActivity(MyTeamActivity.class);
                break;
            //新手教程
            case R.id.linear_index_novice:
                startActivity(NoviceActivity.class);
                break;
            //留言
            case R.id.linear_index_feedback:
                startActivity(IdeaActivity.class);
                break;
            //提现记录
            case R.id.linear_index_kiting_record:
                startActivity(KitingDetailsActivity.class);
                break;
            //我要代言
            case R.id.linear_index_invite:
                startActivity(InviteActivity.class);
                break;
            //商家入驻
            case R.id.linear_index_shop_enter:
                String shopInURL= Strings.trim(UserCache.getIndexShopInURL());
                if(shopInURL==null) {
                    ToastUtils.show("开发中");
                }else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shopInURL)));
                }
                break;
            //商家
            case R.id.linear_index_shop:
                String shopURL=Strings.trim(UserCache.getIndexShopURL());
                if(shopURL==null) {
                    ToastUtils.show("开发中");
                }else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shopURL)));
                }
                break;
        }
    }

    public void toggleBalanceVisible(){
        if(mTvIndexBalance.getHint()==null){
            mTvMainTotalIncome.setHint(mTvMainTotalIncome.getText());
            mTvIndexBalance.setHint(mTvIndexBalance.getText());
            mTvMainTodayIncome.setHint(mTvMainTodayIncome.getText());
            mTvIndexTeamNum.setHint(mTvIndexTeamNum.getText());

        }
        if(mBtnEye.isActivated()){
            mBtnEye.setBackgroundResource(R.drawable.ic_eye_open);
            mTvIndexBalance.setText(mTvIndexBalance.getHint());
            mTvMainTotalIncome.setText(mTvMainTotalIncome.getHint());
            mTvMainTodayIncome.setText(mTvMainTodayIncome.getHint());
            mTvIndexTeamNum.setText(mTvIndexTeamNum.getHint());
            mBtnEye.setActivated(false);
        }else{
            mTvIndexBalance.setText("******");
            mTvMainTotalIncome.setText("******");
            mTvMainTodayIncome.setText("******");
            mTvIndexTeamNum.setText("***");
            mBtnEye.setActivated(true);
            mBtnEye.setBackgroundResource(R.drawable.ic_eye_close);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 88) {
            getEarnings(null);
            getNoticeList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 是否启用沉浸式状态栏
     *
     * @return
     */
    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    /**
     * 是否开启状态栏暗色字体
     *
     * @return
     */
    @Override
    public boolean statusBarDarkFont() {
        return false;
    }
}
