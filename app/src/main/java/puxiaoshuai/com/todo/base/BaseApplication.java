package puxiaoshuai.com.todo.base;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
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
//        请求flask接口，seesion保存在cookie中，所以需要设置cookie，不然拿不到session中的值
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        OkGo.getInstance().init(this).setOkHttpClient(builder.build());
        RxTool.init(this);

    }
}
