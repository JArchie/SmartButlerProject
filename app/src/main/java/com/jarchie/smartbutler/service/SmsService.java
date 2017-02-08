package com.jarchie.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.utils.StaticClass;
import com.jarchie.smartbutler.view.DispatchLinrayLayout;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.service
 * 文件名:   SmsService
 * 创建者:   Jarchie
 * 创建时间: 17/1/30 下午1:43
 * 描述:     短信监听服务
 */

public class SmsService extends Service implements View.OnClickListener {
    private SmsReceiver smsReceiver;
    //发件人号码
    private String smsPhoneNum;
    //短信内容
    private String smsContent;
    //窗口管理器
    private WindowManager wm;
    //布局参数
    private WindowManager.LayoutParams layoutParams;
    //View
    private DispatchLinrayLayout mView;
    private TextView phone, content;
    private Button sendSms;
    private HomeWatchReceiver homeWatchReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    //初始化广播服务
    private void init() {
        //动态注册
        smsReceiver = new SmsReceiver();
        IntentFilter intent = new IntentFilter();
        //添加Action
        intent.addAction(StaticClass.SMS_ACTION);
        //设置权限
        intent.setPriority(Integer.MAX_VALUE);
        //注册短信广播
        registerReceiver(smsReceiver, intent);
        //注册HOME键广播
        homeWatchReceiver = new HomeWatchReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeWatchReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销
        unregisterReceiver(smsReceiver);
        unregisterReceiver(homeWatchReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sendsms:
                //跳转到发送短信的界面
                sendSmsMethod();
                //小时窗口
                if (mView.getParent() != null) {
                    wm.removeView(mView);
                }
                break;
        }
    }

    //回复短信
    private void sendSmsMethod() {
        Uri uri = Uri.parse("smsto:" + smsPhoneNum);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        //设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    //短信广播
    public class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StaticClass.SMS_ACTION.equals(action)) {
                //获取短信内容返回的是一个Object数组
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for (Object obj : objs) {
                    //把数组内容转化成短信对象
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                    //发件人
                    smsPhoneNum = sms.getOriginatingAddress();
                    //内容
                    smsContent = sms.getMessageBody();
                    //弹出窗口
                    showWindow();
                }
            }
        }
    }

    //窗口提示
    private void showWindow() {
        //获取系统服务
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //获取布局参数
        layoutParams = new WindowManager.LayoutParams();
        //定义宽高
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式
        layoutParams.format = PixelFormat.TRANSLUCENT;
        //定义类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //加载布局
        mView = (DispatchLinrayLayout) View.inflate(getApplicationContext(), R.layout.sms_item, null);
        //初始化View
        phone = (TextView) mView.findViewById(R.id.tv_phonenum);
        content = (TextView) mView.findViewById(R.id.tv_content);
        sendSms = (Button) mView.findViewById(R.id.btn_sendsms);
        sendSms.setOnClickListener(this);
        //添加数据
        phone.setText("发件人:" + smsPhoneNum);
        content.setText(smsContent);
        //添加View到窗口
        wm.addView(mView, layoutParams);
        mView.setDispatchKeyEventListener(mListener);
    }

    //自定义的事件分发
    private DispatchLinrayLayout.DispatchKeyEventListener mListener = new DispatchLinrayLayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否是按返回键
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                if (mView.getParent() != null){
                    wm.removeView(mView);
                }
                return true;
            }
            return false;
        }
    };

    //监听Home键的广播
    class HomeWatchReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                String reason = intent.getStringExtra(StaticClass.SYSTEM_DIALOGS_REASON_KEY);
                if (StaticClass.SYSTEM_DIALOGS_HOME_KEY.equals(reason)){
                    if (mView.getParent() != null){
                        wm.removeView(mView);
                    }
                }
            }
        }
    }

}
