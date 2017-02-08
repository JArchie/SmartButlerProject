package com.jarchie.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.entity.MyUser;
import com.jarchie.smartbutler.ui.LoginActivity;
import com.jarchie.smartbutler.ui.LogisticsActivity;
import com.jarchie.smartbutler.ui.PhoneRegionActivity;
import com.jarchie.smartbutler.utils.UtilTools;
import com.jarchie.smartbutler.view.CustomDialog;
import java.io.File;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.fragment
 * 文件名:   ButlerFragment
 * 创建者:   Jarchie
 * 创建时间: 17/1/15 下午2:12
 * 描述:     个人中心
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private Button exitBtn;
    private TextView editUser;
    private EditText userName, sex, age, desc;
    //修改按钮
    private Button okUpdateBtn;
    //圆形头像
    private CircleImageView circleImageView;
    private CustomDialog customDialog;
    //下方按钮
    private Button cameraBtn, picBtn, cancelBtn;
    //照片名称
    public static final String PHOTO_FILE_NAME = "fileImage.jpg";
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int ALBUM_REQUEST_CODE = 2;
    public static final int RESULT_REQUEST_CODE = 3;
    private File tempFile = null;
    //物流和归属地
    private TextView logisticsBtn, regionBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        initView(view);
        return view;
    }

    //初始化View
    private void initView(View view) {
        //物流和归属地
        logisticsBtn = (TextView) view.findViewById(R.id.tv_logistics);
        regionBtn = (TextView) view.findViewById(R.id.tv_region);
        logisticsBtn.setOnClickListener(this);
        regionBtn.setOnClickListener(this);
        exitBtn = (Button) view.findViewById(R.id.btn_exit);
        exitBtn.setOnClickListener(this);
        editUser = (TextView) view.findViewById(R.id.edit_user);
        editUser.setOnClickListener(this);
        userName = (EditText) view.findViewById(R.id.et_name);
        sex = (EditText) view.findViewById(R.id.et_sex);
        age = (EditText) view.findViewById(R.id.et_ages);
        desc = (EditText) view.findViewById(R.id.et_descs);
        okUpdateBtn = (Button) view.findViewById(R.id.btn_okupdate);
        okUpdateBtn.setOnClickListener(this);
        circleImageView = (CircleImageView) view.findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(this);
        //获取图片
        UtilTools.getImageFromShareUtil(getActivity(), circleImageView);
        //初始化Dialog
        customDialog = new CustomDialog(getActivity(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, R.layout.dialog_photo, R.style.Theme_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        customDialog.setCancelable(false);
        cameraBtn = (Button) customDialog.findViewById(R.id.btn_camera);
        picBtn = (Button) customDialog.findViewById(R.id.btn_picture);
        cancelBtn = (Button) customDialog.findViewById(R.id.btn_cancel);
        cameraBtn.setOnClickListener(this);
        picBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        //默认是不可点击的
        setEnabled(false);
        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo != null){
            userName.setText(userInfo.getUsername());
            sex.setText(userInfo.isSex() ? "男" : "女");
            age.setText(userInfo.getAge() + "");
            desc.setText(userInfo.getDesc());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //退出登录
            case R.id.btn_exit:
                exitLogin();
                break;
            //编辑资料
            case R.id.edit_user:
                setEnabled(true);
                okUpdateBtn.setVisibility(View.VISIBLE);
                break;
            //修改资料
            case R.id.btn_okupdate:
                updateUserInfo();
                break;
            //弹出头像选择的Dialog
            case R.id.profile_image:
                customDialog.show();
                break;
            //调用相机拍照
            case R.id.btn_camera:
                openCamera();
                break;
            //从相册选择
            case R.id.btn_picture:
                openAlbum();
                break;
            //取消按钮,dialog消失
            case R.id.btn_cancel:
                customDialog.dismiss();
                break;
            //物流查询
            case R.id.tv_logistics:
                startActivity(new Intent(getActivity(), LogisticsActivity.class));
                break;
            //归属地查询
            case R.id.tv_region:
                startActivity(new Intent(getActivity(), PhoneRegionActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                //相册数据
                case ALBUM_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setCircleImageView(data);
                        //设置完成图片之后,将之前图片删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //保存图片
    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存头像
        UtilTools.putImageToShareUtil(getActivity(), circleImageView);
    }

    //设置裁剪后的图片到ImageView上面
    private void setCircleImageView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            circleImageView.setImageBitmap(bitmap);
        }
    }

    //裁剪图片的方法
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", true);
        //设置裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置裁剪图片的质量
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //打开相册
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
        customDialog.dismiss();
    }

    //打开相机
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用,可用就进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        customDialog.dismiss();
    }

    //修改用户信息
    private void updateUserInfo() {
        String nameValue = userName.getText().toString().trim();
        String sexValue = sex.getText().toString().trim();
        String ageValue = age.getText().toString().trim();
        String descValue = desc.getText().toString().trim();
        if (!TextUtils.isEmpty(nameValue) & !TextUtils.isEmpty(sexValue) & !TextUtils.isEmpty(ageValue)) {
            //更新属性
            MyUser user = new MyUser();
            user.setUsername(nameValue);
            user.setSex(sexValue.equals("男") ? true : false);
            user.setAge(Integer.parseInt(ageValue));
            if (!TextUtils.isEmpty(descValue)) {
                user.setDesc(descValue);
            } else {
                user.setDesc("这个人很懒,什么都没有留下!");
            }
            BmobUser bmobUser = BmobUser.getCurrentUser();
            user.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        setEnabled(false);
                        okUpdateBtn.setVisibility(View.GONE);
                        UtilTools.toastShortMessage(getActivity(), "修改成功");
                    } else {
                        UtilTools.toastShortMessage(getActivity(), "修改失败");
                    }
                }
            });
        } else {
            UtilTools.toastShortMessage(getActivity(), "输入框不能为空");
        }
    }

    //设置焦点
    private void setEnabled(boolean is) {
        userName.setEnabled(is);
        sex.setEnabled(is);
        age.setEnabled(is);
        desc.setEnabled(is);
    }

    //退出登录逻辑处理
    private void exitLogin() {
        //清除缓存用户对象
        MyUser.logOut();
        //现在的currentUser是null了
        BmobUser currentUser = MyUser.getCurrentUser();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

}
