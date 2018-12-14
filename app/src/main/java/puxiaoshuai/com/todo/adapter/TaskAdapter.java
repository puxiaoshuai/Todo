package puxiaoshuai.com.todo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import puxiaoshuai.com.todo.R;
import puxiaoshuai.com.todo.model.TaskBean;

/**
 * Created by Administrator on 2018/12/13 0013.
 */

public class TaskAdapter extends BaseQuickAdapter<TaskBean.DataBean,BaseViewHolder> {

    public TaskAdapter(int layoutResId, @Nullable List<TaskBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskBean.DataBean item) {
        helper.setText(R.id.task_title,item.getTitle());
        helper.setText(R.id.task_content,item.getContent());
        helper.setText(R.id.task_time,item.getCreate_time());
    }
}
