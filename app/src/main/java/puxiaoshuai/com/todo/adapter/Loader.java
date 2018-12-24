package puxiaoshuai.com.todo.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;
import com.yzs.imageshowpickerview.ImageLoader;

/**
 * Created by Administrator on 2018/12/24 0024.
 */
public class Loader extends ImageLoader {

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        if (path.startsWith("http"))
        {
            Picasso.with(context).load(path).into(imageView);
        }else {
            Picasso.with(context).load("file://"+path)
                    .into(imageView);
        }


    }

    @Override
    public void displayImage(Context context, @DrawableRes Integer resId, ImageView imageView) {
        imageView.setImageResource(resId);
    }

}