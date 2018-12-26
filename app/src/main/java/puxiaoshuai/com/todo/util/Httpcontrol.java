package puxiaoshuai.com.todo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import puxiaoshuai.com.todo.ui.LoginActivity;

public class Httpcontrol {
    private Context mContext;

    public Httpcontrol(Context context) {
        mContext = context;
    }

    public String  is_ok(String response){
       try {
           JSONObject jsonObject = new JSONObject(response);
           String code = jsonObject.optString("code");
           String message = jsonObject.optString("message");
           if (code.equals("200")) {
               return response;
           }else if (code.equals("401")){
               RxToast.success(message);
               RxSPTool.remove(mContext, Constants.Token);
               RxActivityTool.skipActivityAndFinishAll(mContext,LoginActivity.class);


           }else {
               RxToast.success(message);
           }

       } catch (Exception e) {
           return "";

       }
       return "";
   }
}
