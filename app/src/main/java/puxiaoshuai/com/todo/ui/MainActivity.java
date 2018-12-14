package puxiaoshuai.com.todo.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.activity.ActivityBase;
import com.vondear.rxui.view.RxTitle;
import com.vondear.rxui.view.dialog.RxDialog;
import com.vondear.rxui.view.dialog.RxDialogLoading;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import puxiaoshuai.com.todo.R;
import puxiaoshuai.com.todo.adapter.TaskAdapter;
import puxiaoshuai.com.todo.base.Net_Api;
import puxiaoshuai.com.todo.model.TaskBean;
import puxiaoshuai.com.todo.util.Constants;

public class MainActivity extends ActivityBase {

    @BindView(R.id.rxtitle)
    RxTitle mRxtitle;
    @BindView(R.id.recycleview)
    RecyclerView mXRecyclerView;
    private String mToken;

    private TaskAdapter mTaskAdapter;
    private ArrayList<TaskBean.DataBean> mDataBeans=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mToken=RxSPTool.getString(mContext,Constants.Token);

        String message=RxSPTool.getString(mContext,Constants.Token);
        RxToast.success("欢迎:"+message+"回来");
        mRxtitle.setLeftIconVisibility(false);
        mRxtitle.setLeftText("退出");
        mRxtitle.setLeftTextVisibility(true);
        mRxtitle.setLeftTextSize(32);
        mRxtitle.setTitle("首页");
        mRxtitle.setLeftTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkGo.<String>post(Net_Api.Logout_url).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.v("TAG",response.body());
                        RxSPTool.remove(mContext, Constants.Token);
                        finish();
                    }
                });

            }
        });
        ArrayList<TaskBean.DataBean> task_list=load_data();
        initView(task_list);

    }
     private  void initView(ArrayList<TaskBean.DataBean> data){

         mXRecyclerView = findViewById(R.id.recycleview);
         mTaskAdapter = new TaskAdapter(R.layout.item_task, data);
         LinearLayoutManager manager = new LinearLayoutManager(this);
         manager.setOrientation(LinearLayoutManager.VERTICAL);
         mXRecyclerView.setLayoutManager(manager);
         mXRecyclerView.setAdapter(mTaskAdapter);
     }
    private ArrayList<TaskBean.DataBean> load_data() {

        OkGo.<String>post(Net_Api.Task_list).
                params("token",mToken).
                execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.v("TAG", response.body());
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.v("TAG", response.body());
                    }
                });

        return null;
    }

}
