package com.jarchie.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.jarchie.smartbutler.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   QRCodeActivity
 * 创建者:   Jarchie
 * 创建时间: 17/2/3 下午7:09
 * 描述:     生成我的二维码
 */

public class QRCodeActivity extends BaseActivity {
    private ImageView imageview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initView();
    }

    //初始化View
    private void initView() {
        imageview = (ImageView) findViewById(R.id.iv_qrcode);
        //获取屏幕的宽
        int width = getResources().getDisplayMetrics().widthPixels;
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("智能管家",width/2,width/2,
                BitmapFactory.decodeResource(getResources(),R.drawable.logo));
        imageview.setImageBitmap(qrCodeBitmap);
    }

}
