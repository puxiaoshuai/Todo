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

        if (helper.getAdapterPosition()==1){
            ImageInfo imageInfo1=new ImageInfo();
            imageInfo1.setBigImageUrl("http://d.hiphotos.baidu.com/image/pic/item/91ef76c6a7efce1b5ef04082a251f3deb58f659b.jpg");
            imageInfo1.setThumbnailUrl("http://d.hiphotos.baidu.com/image/pic/item/91ef76c6a7efce1b5ef04082a251f3deb58f659b.jpg");
            imageInfos.add(imageInfo1);
        }else  if (helper.getAdapterPosition()==2){
            ImageInfo imageInfo1=new ImageInfo();
            imageInfo1.setBigImageUrl("http://g.hiphotos.baidu.com/image/pic/item/5bafa40f4bfbfbed5572eb3875f0f736afc31f4a.jpg");
            imageInfo1.setThumbnailUrl("http://g.hiphotos.baidu.com/image/pic/item/5bafa40f4bfbfbed5572eb3875f0f736afc31f4a.jpg");

            ImageInfo imageInfo3=new ImageInfo();
            imageInfo3.setBigImageUrl("http://h.hiphotos.baidu.com/image/pic/item/f2deb48f8c5494eec7036a5f20f5e0fe99257e56.jpg");
            imageInfo3.setThumbnailUrl("http://h.hiphotos.baidu.com/image/pic/item/f2deb48f8c5494eec7036a5f20f5e0fe99257e56.jpg");
            imageInfos.add(imageInfo1);

            imageInfos.add(imageInfo3);
        }
        else {
            ImageInfo imageInfo1=new ImageInfo();
            imageInfo1.setBigImageUrl("http://b.hiphotos.baidu.com/image/pic/item/11385343fbf2b2119202e609c78065380cd78e4c.jpg");
            imageInfo1.setThumbnailUrl("http://b.hiphotos.baidu.com/image/pic/item/11385343fbf2b2119202e609c78065380cd78e4c.jpg");
            ImageInfo imageInfo2=new ImageInfo();
            imageInfo2.setBigImageUrl("http://g.hiphotos.baidu.com/image/pic/item/d52a2834349b033bd5d4739f18ce36d3d539bd13.jpg");
            imageInfo2.setThumbnailUrl("http://g.hiphotos.baidu.com/image/pic/item/d52a2834349b033bd5d4739f18ce36d3d539bd13.jpg");
            ImageInfo imageInfo3=new ImageInfo();
            imageInfo3.setBigImageUrl("http://h.hiphotos.baidu.com/image/pic/item/b3fb43166d224f4af75c135404f790529922d1b4.jpg");
            imageInfo3.setThumbnailUrl("http://h.hiphotos.baidu.com/image/pic/item/b3fb43166d224f4af75c135404f790529922d1b4.jpg");
            ImageInfo imageInfo4=new ImageInfo();
            imageInfo4.setBigImageUrl("http://b.hiphotos.baidu.com/image/pic/item/4bed2e738bd4b31cce8a1df38ad6277f9f2ff8c0.jpg");
            imageInfo4.setThumbnailUrl("http://b.hiphotos.baidu.com/image/pic/item/4bed2e738bd4b31cce8a1df38ad6277f9f2ff8c0.jpg");
            imageInfos.add(imageInfo1);
            imageInfos.add(imageInfo2);
            imageInfos.add(imageInfo3);
            imageInfos.add(imageInfo4);
        }
        NineGridView mgrid=helper.getView(R.id.ninegridview);
        mgrid.setAdapter(new NineGridViewClickAdapter(mContext,imageInfos));

    }
}
