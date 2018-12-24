package puxiaoshuai.com.todo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.melnykov.fab.FloatingActionButton;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxFragmentTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.activity.ActivityBase;
import com.vondear.rxui.view.RxTitle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import puxiaoshuai.com.todo.R;
import puxiaoshuai.com.todo.adapter.TaskAdapter;
import puxiaoshuai.com.todo.base.Net_Api;
import puxiaoshuai.com.todo.event.FreshTaskList;
import puxiaoshuai.com.todo.model.TaskBean;
import puxiaoshuai.com.todo.util.Constants;
import puxiaoshuai.com.todo.util.Httpcontrol;

public class MainActivity extends ActivityBase implements View.OnClickListener {

    @BindView(R.id.rxtitle)
    RxTitle mRxtitle;
    @BindView(R.id.recycleview)
    RecyclerView mXRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.swipe_fresh)
    SwipeRefreshLayout swipeFresh;
    private String mToken;
    private long clickTime = 0; // 第一次点击的时间
    private TaskAdapter mTaskAdapter;
    private int page = 1;
    private ArrayList<TaskBean.DataBean.ListBean> mDataBeans = new ArrayList<>();
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("TAG", "mainactivity");

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(mContext);
        mToken = RxSPTool.getString(mContext, Constants.Token);
        String username = RxSPTool.getString(mContext, Constants.UserName);
        RxToast.success("欢迎:" + username + "回来");
        mRxtitle.setLeftTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxSPTool.remove(mContext, Constants.Token);
                RxActivityTool.skipActivityAndFinish(mContext, LoginActivity.class);
            }
        });
        initEmptyView();
        initView();
        initRefreshLout();

        //第一次加载
        swipeFresh.setRefreshing(true);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                refresh();
            }
        }, 1000);
        HiPermission.create(mContext)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });


    }

    private void initView() {

        mXRecyclerView = findViewById(R.id.recycleview);
        mTaskAdapter = new TaskAdapter(R.layout.item_task, mDataBeans);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(manager);
        mXRecyclerView.setAdapter(mTaskAdapter);
        fab.attachToRecyclerView(mXRecyclerView);
        fab.setOnClickListener(this);
        mTaskAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mTaskAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page = page + 1;
                load_LoadMoreData(page);
            }
        }, mXRecyclerView);

    }

    private void initRefreshLout() {
        swipeFresh.setColorSchemeResources( R.color.colorPrimaryDark,R.color.colorAccent);
        swipeFresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        //防止下拉刷新的时候还能上拉加载
        mTaskAdapter.setEnableLoadMore(false);
        //下啦刷新
        page = 1;
        mDataBeans.clear();
        mTaskAdapter.notifyDataSetChanged();
        load_refreshdata(page);

    }


    private void load_refreshdata(int page) {
        OkGo.<String>post(Net_Api.Task_list).
                params("token", mToken).
                params("page", page).
                execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.v("TAG", response.body());
                        mTaskAdapter.setEnableLoadMore(true);
                        swipeFresh.setRefreshing(false);

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        mTaskAdapter.setEnableLoadMore(true);
                        swipeFresh.setRefreshing(false);
                        Httpcontrol httpcontrol = new Httpcontrol(mContext);
                        String message = httpcontrol.is_ok(response.body());
                        if (!RxDataTool.isNullString(message)) {
                            Gson gson = new Gson();
                            TaskBean taskBean = gson.fromJson(response.body(), TaskBean.class);
                            if (taskBean.getData().getTotal_num() == 0) {
                                mTaskAdapter.setEmptyView(mEmptyView);
                                return;
                            }
                            mDataBeans.addAll(taskBean.getData().getList());
                            mTaskAdapter.notifyDataSetChanged();
                            if (mDataBeans.size() < taskBean.getData().getTotal_num()) {
                                //第一页如果不够一页就不显示没有更多数据布局
                                mTaskAdapter.loadMoreComplete();
                            } else {
                                mTaskAdapter.loadMoreEnd();
                            }


                        }
                    }
                });
    }

    private void load_LoadMoreData(int page) {
        OkGo.<String>post(Net_Api.Task_list).
                params("token", mToken).
                params("page", page).
                execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.v("TAG", response.body());
                        mTaskAdapter.loadMoreFail();

                    }

                    @Override
                    public void onSuccess(Response<String> response) {

                        Httpcontrol httpcontrol = new Httpcontrol(mContext);
                        String message = httpcontrol.is_ok(response.body());
                        if (!RxDataTool.isNullString(message)) {

                            Gson gson = new Gson();
                            TaskBean taskBean = gson.fromJson(response.body(), TaskBean.class);
                            mDataBeans.addAll(taskBean.getData().getList());
                            if (mDataBeans.size() < taskBean.getData().getTotal_num()) {
                                mTaskAdapter.loadMoreComplete();
                            } else {
                                mTaskAdapter.loadMoreEnd();
                            }

                        }
                    }
                });
    }

    private void initEmptyView() {
        mEmptyView = getLayoutInflater().inflate(R.layout.empty, (ViewGroup) mXRecyclerView.getParent(), false);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(FreshTaskList freshTaskList) {
        if (freshTaskList.isFresh()) {
            mDataBeans.clear();
            page = 1;
            load_refreshdata(page);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(mContext))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Bundle bundle = new Bundle();
                bundle.putBoolean("add", true);
                RxActivityTool.skipActivity(mContext, TaskDetailsActivity.class, bundle);
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            RxToast.showToast("再按一次后退键退出程序");
            clickTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
