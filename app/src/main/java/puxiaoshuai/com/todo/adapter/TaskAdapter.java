package puxiaoshuai.com.todo.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.view.dialog.RxDialogSureCancel;

import java.util.ArrayList;
import java.util.List;

import puxiaoshuai.com.todo.R;
import puxiaoshuai.com.todo.base.Net_Api;
import puxiaoshuai.com.todo.model.ImageBean;
import puxiaoshuai.com.todo.model.TaskBean;
import puxiaoshuai.com.todo.ui.TaskDetailsActivity;
import puxiaoshuai.com.todo.util.Constants;
import puxiaoshuai.com.todo.util.Httpcontrol;

/**
 * Created by Administrator on 2018/12/13 0013.
 */

public class TaskAdapter extends BaseQuickAdapter<TaskBean.DataBean.ListBean,BaseViewHolder> {
    List<TaskBean.DataBean.ListBean> items=new ArrayList<>();
    public TaskAdapter(int layoutResId, @Nullable List<TaskBean.DataBean.ListBean> data) {
        super(layoutResId, data);
        items=data;
    }

    @Override
    protected void convert(BaseViewHolder helper, final TaskBean.DataBean.ListBean item) {
        helper.setText(R.id.task_title,item.getTitle());
        helper.setText(R.id.task_content,item.getContent());
        helper.setText(R.id.task_time,item.getCreate_time());
        helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final RxDialogSureCancel rxDialogEditSureCancel = new RxDialogSureCancel(mContext);
                rxDialogEditSureCancel.getTitleView().setBackgroundResource(R.drawable.todo);
                rxDialogEditSureCancel.setContent("确定删除当前记录？");
                rxDialogEditSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OkGo.<String>post(Net_Api.Task_del).params("id",item.getId())
                                .params("token",RxSPTool.getString(mContext,Constants.Token))
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        Httpcontrol httpcontrol=new Httpcontrol(mContext);
                                        String data=httpcontrol.is_ok(response.body());
                                        if (!RxDataTool.isNullString(data))
                                        {
                                            RxToast.success("删除成功");
                                            items.remove(item);
                                            notifyDataSetChanged();
                                        }
                                    }
                                });
                        rxDialogEditSureCancel.cancel();
                    }
                });
                rxDialogEditSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditSureCancel.cancel();
                    }
                });
                rxDialogEditSureCancel.show();
                return true;
            }
        });

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("task",item);
                bundle.putBoolean("add",false);
                RxActivityTool.skipActivity(mContext,TaskDetailsActivity.class,bundle);
            }
        });
        ArrayList<ImageInfo> imageInfos = new ArrayList<>();
        for (int i = 0; i < item.getImages().size(); i++) {
            ImageInfo imageInfo=new ImageInfo();
            imageInfo.setThumbnailUrl(Net_Api.BASE_IMG+item.getImages().get(i));
            imageInfo.setBigImageUrl(Net_Api.BASE_IMG+item.getImages().get(i));
            imageInfos.add(imageInfo);
        }
        NineGridView mgrid=helper.getView(R.id.ninegridview);
        mgrid.setAdapter(new NineGridViewClickAdapter(mContext,imageInfos));

    }
}
