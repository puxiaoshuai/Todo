package puxiaoshuai.com.todo.base;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.vondear.rxtool.RxTool;

import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2018/12/13 0013.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        OkGo.getInstance().init(this).setOkHttpClient(builder.build());
        RxTool.init(this);

    }
}
