package puxiaoshuai.com.todo.model;

import com.yzs.imageshowpickerview.ImageShowPickerBean;

/**
 * Created by Administrator on 2018/12/24 0024.
 */

public class ImageBean extends ImageShowPickerBean {
    private String url;
    private int resid;
    public ImageBean(String url)
    {
        this.url=url;
    }
    public ImageBean(int resid)
    {
        this.resid=resid;
    }
    @Override
    public String setImageShowPickerUrl() {
        return url;
    }

    @Override
    public int setImageShowPickerDelRes() {
        return resid;
    }
}
