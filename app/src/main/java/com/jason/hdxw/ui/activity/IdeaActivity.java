package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.adapter.IdeaAdapter;
import com.jason.hdxw.base.ClearEditText;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.MsgReturnBean;
import com.jason.hdxw.utils.UserCache;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_IDEA_RETRUN;
import static com.jason.hdxw.api.API.MEMBER_MSG_RETURN;

/**
 * 意见反馈页面
 * created by wang on 2018/11/15
 */
public class IdeaActivity extends WhiteBarActivity implements View.OnClickListener {

    @BindView(R.id.iv_idea_back)
    ImageView mIvIdeaBack;
    @BindView(R.id.et_idea)
    ClearEditText mEtIdea;
    @BindView(R.id.tv_idea_sure)
    TextView mTvIdeaSure;
    @BindView(R.id.tv_idea_count)
    TextView mTvIdeaCount;
    @BindView(R.id.recycler_idea)
    RecyclerView mRecyclerIdea;
    @BindView(R.id.refreshlayout_idea)
    SmartRefreshLayout mRefreshlayoutIdea;

    //数据页数
    private int mPage = 0;
    private Gson mGson = new Gson();
    private MsgReturnBean mReturnBean;
    private List<MsgReturnBean.ListBean> mList = new ArrayList<>();
    private IdeaAdapter mAdapter = new IdeaAdapter(this, mList);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);
        ButterKnife.bind(this);
        //initData();
        initListener();
    }

    private void initData() {
        mRecyclerIdea.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerIdea.setAdapter(mAdapter);
        refreshData(mPage + "", null);
    }

    private void initListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvIdeaCount.setText(s.length() + "/200");
            }
        };
        mEtIdea.addTextChangedListener(textWatcher);
        mRefreshlayoutIdea.setEnableAutoLoadMore(false);
        mRefreshlayoutIdea.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
        mRefreshlayoutIdea.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
//        mRefreshlayoutIdea.autoRefresh();//自动刷新
        mRefreshlayoutIdea.setEnableRefresh(true);//是否启用下拉刷新功能
        mRefreshlayoutIdea.setEnableLoadMore(true);//是否启用上拉加载功能
        mRefreshlayoutIdea.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                mList.clear();
                mPage = 0;
                refreshData(mPage + "", refreshlayout);
            }
        });
        mRefreshlayoutIdea.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                refreshData(mPage + "", refreshlayout);
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refreshData(final String page, final RefreshLayout refreshlayout) {
        OkGo.<String>post(MEMBER_MSG_RETURN)
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
                        Logger.e("留言反馈接口返回数据：" + response.body());
                        try {
                            mReturnBean = mGson.fromJson(response.body(), MsgReturnBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mReturnBean.getStatus().equals("y")) {
                            if (mReturnBean.getList() != null && mReturnBean.getList().size() > 0) {
                                mList.addAll(mReturnBean.getList());
                                mAdapter.setList(mList);
                                mPage++;
                            } else {
                                if (page.equals("0")) {
//                                    if (mReturnBean.getMsg() != null) {
//                                        ToastUtils.show(mReturnBean.getMsg());
//                                    }
                                } else {
                                    ToastUtils.show(getString(R.string.hint_noMoreData));
                                    if (refreshlayout != null) {
                                        refreshlayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
                                    }
                                }
                            }
                        } else {
                            ToastUtils.show(mReturnBean.getMsg());
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


    @OnClick({R.id.iv_idea_back, R.id.tv_idea_sure})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_idea_back:
                finish();
                break;
            //确认
            case R.id.tv_idea_sure:
                submitIdea();
                break;
        }
    }

    /**
     * 提交意见
     */
    private void submitIdea() {
        if (mEtIdea.getText() == null || mEtIdea.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_submitidea));
            return;
        }
        OkGo.<String>post(BASICS_IDEA_RETRUN)
                .params("token", UserCache.getToken())
                .params("message", mEtIdea.getText().toString().trim())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("意见反馈接口返回数据：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                ToastUtils.show(jsonObject.getString("msg"));
                                finish();
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
}
