package puxiaoshuai.com.todo.ui;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.activity.ActivityBase;
import com.vondear.rxui.view.RxTitle;
import com.vondear.rxui.view.dialog.RxDialogLoading;
import com.yzs.imageshowpickerview.ImageShowPickerBean;
import com.yzs.imageshowpickerview.ImageShowPickerListener;
import com.yzs.imageshowpickerview.ImageShowPickerView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import puxiaoshuai.com.todo.R;
import puxiaoshuai.com.todo.adapter.Loader;
import puxiaoshuai.com.todo.base.Net_Api;
import puxiaoshuai.com.todo.event.FreshTaskList;
import puxiaoshuai.com.todo.model.ImageBean;
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
    @BindView(R.id.image_picker)
    ImageShowPickerView mImagePicker;
    private TaskBean.DataBean.ListBean mTask;
    private boolean isAdd = false;
    private String mToken = "";
    List<ImageBean> mImageBeans=new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 233;
    private List<String> mFile_key=new ArrayList<>();

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
        isAdd = getIntent().getBooleanExtra("add", false);
        addMessage(mTask);
        init_getToken();
        get_image();
    }
    private void get_image() {
        mImagePicker.setImageLoaderInterface(new Loader());
        mImagePicker.setNewData(mImageBeans);
        mImagePicker.setShowAnim(true);
        mImagePicker.setPickerListener(new ImageShowPickerListener() {
            @Override
            public void addOnClickListener(int remainNum) {
                Matisse.from(mContext)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(remainNum + 1)
                        .gridExpectedSize(300)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }

            @Override
            public void picOnClickListener(List<ImageShowPickerBean> list, int position, int i1) {
               //图片点击事件

            }

            @Override
            public void delOnClickListener(int position, int i1) {
                mFile_key.remove(position);


            }
        });
        mImagePicker.show();

    }

    private void init_getToken() {
        OkGo.<String>post(Net_Api.Token).execute(new StringCallback() {
            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onSuccess(Response<String> response) {
                Httpcontrol httpcontrol = new Httpcontrol(mContext);
                if (!RxDataTool.isNullString(httpcontrol.is_ok(response.body()))) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        mToken = jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void addMessage(final TaskBean.DataBean.ListBean task_data) {
        if (isAdd) {
            title.setRightText("添加");
            title.setRightTextOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
                    rxDialogLoading.setLoadingText("提交中");
                    rxDialogLoading.show();
                    String title = edTitle.getText().toString();
                    String content = edContent.getText().toString();
                    if (RxDataTool.isNullString(title) || RxDataTool.isNullString(content)) {
                        RxToast.warning("标题或者内容不能为空");
                        return;
                    }
                    PostRequest<String> params = OkGo.<String>post(Net_Api.Task_add)
                            .params("title", title)
                            .params("content", content);

                    for (int i = 0; i < mFile_key.size(); i++) {
                        params.params("files",mFile_key.get(i));

                    }
                    params.params("token", RxSPTool.getString(mContext, Constants.Token))
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                    rxDialogLoading.cancel();
                                }

                                @Override
                                public void onSuccess(Response<String> response) {
                                    rxDialogLoading.cancel();
                                    Httpcontrol httpcontrol = new Httpcontrol(mContext);
                                    if (!RxDataTool.isNullString(httpcontrol.is_ok(response.body()))) {
                                        RxToast.success("上传成功");
                                        EventBus.getDefault().post(new FreshTaskList(true));
                                        finish();
                                    }
                                }
                            });
                }
            });
        } else {
            edTitle.setText(task_data.getTitle());
            edContent.setText(task_data.getContent());
            title.setRightTextOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
                    rxDialogLoading.setLoadingText("修改中");
                    rxDialogLoading.show();
                    String title = edTitle.getText().toString();
                    String content = edContent.getText().toString();
                    OkGo.<String>post(Net_Api.Task_edit).params("id", task_data.getId())
                            .params("title", title)
                            .params("content", content)
                            .params("token", RxSPTool.getString(mContext, Constants.Token))
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                    rxDialogLoading.cancel();
                                }

                                @Override
                                public void onSuccess(Response<String> response) {
                                    rxDialogLoading.cancel();
                                    Httpcontrol httpcontrol = new Httpcontrol(mContext);
                                    if (!RxDataTool.isNullString(httpcontrol.is_ok(response.body()))) {
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

    private void to_up_qiuniu(String url){
        UploadManager uploadManager=new UploadManager();
        uploadManager.put(url, null, mToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                         try{
                             key=res.optString("key");
                             mFile_key.add(key);

                         }catch (Exception e){

                         }

                        } else {
                            Log.v("T",res.toString());
                        }

                    }
                }, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_CHOOSE&&resultCode==RESULT_OK)
        {
            List<Uri> uriList=Matisse.obtainResult(data);
            List<ImageBean> list=new ArrayList<>();
            for (int i = 0; i < uriList.size(); i++) {
                list.add(new ImageBean(getRealFilePath(mContext,uriList.get(i))));
                to_up_qiuniu(getRealFilePath(mContext,uriList.get(i)));
            }
            mImagePicker.addData(list);
        }
    }
    public String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
