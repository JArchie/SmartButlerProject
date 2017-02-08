package com.jarchie.smartbutler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.view
 * 文件名:   DispatchLinrayLayout
 * 创建者:   Jarchie
 * 创建时间: 17/1/30 下午3:58
 * 描述:     处理事件分发
 */

public class DispatchLinrayLayout extends LinearLayout{
    private DispatchKeyEventListener dispatchKeyEventListener;

    public DispatchLinrayLayout(Context context) {
        super(context);
    }

    public DispatchLinrayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchLinrayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DispatchKeyEventListener getDispatchKeyEventListener() {
        return dispatchKeyEventListener;
    }

    public void setDispatchKeyEventListener(DispatchKeyEventListener dispatchKeyEventListener) {
        this.dispatchKeyEventListener = dispatchKeyEventListener;
    }

    //接口回调
    public static interface DispatchKeyEventListener{
        boolean dispatchKeyEvent(KeyEvent event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //如果不为空 则调用 就去获取事件
        if (dispatchKeyEventListener != null){
            return dispatchKeyEventListener.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

}
