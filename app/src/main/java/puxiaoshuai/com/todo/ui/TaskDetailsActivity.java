package puxiaoshuai.com.todo.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.activity.ActivityBase;
import com.vondear.rxui.view.RxTitle;
import com.vondear.rxui.view.dialog.RxDialogLoading;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import puxiaoshuai.com.todo.R;
import puxiaoshuai.com.todo.base.Net_Api;
import puxiaoshuai.com.todo.event.FreshTaskList;
import puxiaoshuai.com.todo.model.TaskBean;
import puxiaoshuai.com.todo.util.Constants;
import puxiaoshuai.com.todo.util.Httpcontrol;

public class TaskDetailsActivity extends ActivityBase {
    @BindView(R.id.title)
    RxTitle title;
    @BindView(R.id.ed_title)
    EditText edTitle;
    @BindView(R.id.ed_content)
    EditText edContent;
    private TaskBean.DataBean.ListBean mTask;
    private boolean isAdd=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        ButterKnife.bind(this);
        title.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTask = (TaskBean.DataBean.ListBean) getIntent().getSerializableExtra("task");
        //false是编辑，true是提交
        isAdd=getIntent().getBooleanExtra("add",false);
        if (isAdd){
            title.setRightText("添加");
            title.setRightTextOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
                    rxDialogLoading.setLoadingText("提交中");
                    rxDialogLoading.show();
                    String title=edTitle.getText().toString();
                    String content=edContent.getText().toString();
                    if (RxDataTool.isNullString(title)||RxDataTool.isNullString(content))
                    {
                        RxToast.warning("标题或者内容不能为空");
                        return;
                    }
                    OkGo.<String>post(Net_Api.Task_add)
                            .params("title",title)
                            .params("content",content)
                            .params("token",RxSPTool.getString(mContext,Constants.Token))
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                    rxDialogLoading.cancel();
                                }

                                @Override
                                public void onSuccess(Response<String> response) {
                                    rxDialogLoading.cancel();
                                    Httpcontrol httpcontrol=new Httpcontrol(mContext);
                                    if(!RxDataTool.isNullString(httpcontrol.is_ok(response.body())))
                                    {
                                        RxToast.success("上传成功");
                                        EventBus.getDefault().post(new FreshTaskList(true));
                                        finish();
                                    }
                                }
                            });
                }
            });
        }else {
            edTitle.setText(mTask.getTitle());
            edContent.setText(mTask.getContent());
            title.setRightTextOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
                    rxDialogLoading.setLoadingText("修改中");
                    rxDialogLoading.show();
                    String title=edTitle.getText().toString();
                    String content=edContent.getText().toString();
                    OkGo.<String>post(Net_Api.Task_edit).params("id",mTask.getId())
                            .params("title",title)
                            .params("content",content)
                            .params("token",RxSPTool.getString(mContext,Constants.Token))
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                    rxDialogLoading.cancel();
                                }

                                @Override
                                public void onSuccess(Response<String> response) {
                                    rxDialogLoading.cancel();
                                    Httpcontrol httpcontrol=new Httpcontrol(mContext);
                                    if(!RxDataTool.isNullString(httpcontrol.is_ok(response.body())))
                                    {
                                        RxToast.success("修改成功");
                                        EventBus.getDefault().post(new FreshTaskList(true));
                                        finish();
                                    }
                                }
                            });
                }
            });
        }

    }
}
