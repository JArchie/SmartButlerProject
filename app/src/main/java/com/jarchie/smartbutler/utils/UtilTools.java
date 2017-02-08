package com.jarchie.smartbutler.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.utils
 * 文件名:   UtilTools
 * 创建者:   Jarchie
 * 创建时间: 17/1/14 下午3:00
 * 描述:     封装统一的工具方法类
 */

public class UtilTools {
    //短时吐司
    public static void toastShortMessage(Context mContext, String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    //长时吐司
    public static void toastLongMessage(Context mContext, String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    //设置字体
    public static void setFont(Context mContext, TextView textView) {
        Typeface fontType = Typeface.createFromAsset(mContext.getAssets(), "fonts/FONT.TTF");
        textView.setTypeface(fontType);
    }

    //保存头像到ShareUtil里面
    public static void putImageToShareUtil(Context mContext, ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //第一步:将Bitmap压缩成字节数组输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream);
        //第二步:利用Base64将我们的字节数组输出流转换成String
        byte[] byteArray = byStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三部:将String保存到ShareUtils里面
        ShareUtil.putString(mContext, StaticClass.IMAGE_TITLE, imageString);
    }

    //从ShareUtil里面读出图片设置到ImageView上面
    public static void getImageFromShareUtil(Context mContext, ImageView imageView) {
        String imageString = ShareUtil.getString(mContext, StaticClass.IMAGE_TITLE, "");
        if (!imageString.equals("")) {
            byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
            ByteArrayInputStream biStream = new ByteArrayInputStream(byteArray);
            Bitmap bitmap = BitmapFactory.decodeStream(biStream);
            imageView.setImageBitmap(bitmap);
        }
    }

    //获取版本号
    public static String getVersion(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "无法获取";
        }
    }

}
