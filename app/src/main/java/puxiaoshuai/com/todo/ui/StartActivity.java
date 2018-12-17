package puxiaoshuai.com.todo.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxSPTool;

import puxiaoshuai.com.todo.R;
import puxiaoshuai.com.todo.util.Constants;

public class StartActivity extends AppCompatActivity {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                finish();
                return true;
            }else if(msg.what==2){
                RxActivityTool.skipActivityAndFinish(StartActivity.this,MainActivity.class);
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        //设置无标题
        setContentView(R.layout.activity_start);
        //设置全屏
        String token= RxSPTool.getString(this, Constants.Token);
        if (!RxDataTool.isNullString(token))
        {
            handler.sendEmptyMessageDelayed(2, 2500);
        }else {
            handler.sendEmptyMessageDelayed(1, 2500);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}