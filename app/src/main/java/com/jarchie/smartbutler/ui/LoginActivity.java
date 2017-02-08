package com.jarchie.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.jarchie.smartbutler.MainActivity;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.entity.MyUser;
import com.jarchie.smartbutler.utils.ShareUtil;
import com.jarchie.smartbutler.utils.StaticClass;
import com.jarchie.smartbutler.utils.UtilTools;
import com.jarchie.smartbutler.view.CustomDialog;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   LoginActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/17 上午8:56
 * 描述:     登录页
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //注册
    private Button registerBtn;
    //登录
    private EditText name, pwd;
    private Button loginBtn;
    private CheckBox saveCb;
    //忘记密码
    private TextView forgetBtn;
    //自定义Dialog
    private CustomDialog customDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    //初始化View
    private void initView() {
        registerBtn = (Button) findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(this);
        name = (EditText) findViewById(R.id.et_name);
        pwd = (EditText) findViewById(R.id.et_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
        saveCb = (CheckBox) findViewById(R.id.save_password);
        forgetBtn = (TextView) findViewById(R.id.tv_forget);
        forgetBtn.setOnClickListener(this);
        customDialog = new CustomDialog(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,R.layout.dialog_loading,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        customDialog.setCancelable(false);
        //初始化记住密码状态
        initSaveState();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //跳转到注册页面
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            //登录逻辑处理
            case R.id.btn_login:
                loginProcessMethod();
                break;
            //跳转到忘记密码
            case R.id.tv_forget:
                startActivity(new Intent(this,ForgetPwdActivity.class));
                break;
        }
    }

    //初始化设置选中的状态
    private void initSaveState() {
        boolean isCheck = ShareUtil.getBoolean(LoginActivity.this, StaticClass.IS_SAVE, false);
        saveCb.setChecked(isCheck);
        if (isCheck) {
            //设置密码
            name.setText(ShareUtil.getString(LoginActivity.this, StaticClass.USERNAME, ""));
            pwd.setText(ShareUtil.getString(LoginActivity.this, StaticClass.PASSWORD, ""));
        }
    }

    //记住密码,假设输入完成但是不点击直接关闭页面,所以放在onDestroy()里面处理不合适
    private void savePwd() {
        ShareUtil.putBoolean(LoginActivity.this, StaticClass.IS_SAVE, saveCb.isChecked());
        //是否记住密码
        if (saveCb.isChecked()) {
            //记住用户名和密码
            ShareUtil.putString(LoginActivity.this, StaticClass.USERNAME, name.getText().toString().trim());
            ShareUtil.putString(LoginActivity.this, StaticClass.PASSWORD, pwd.getText().toString().trim());
        } else {
            ShareUtil.delShare(LoginActivity.this, StaticClass.USERNAME);
            ShareUtil.delShare(LoginActivity.this, StaticClass.PASSWORD);
        }
    }

    //登录的处理逻辑
    private void loginProcessMethod() {
        String nameValue = name.getText().toString().trim();
        String pwdValue = pwd.getText().toString().trim();
        //判断是否为空
        if (!TextUtils.isEmpty(nameValue) & !TextUtils.isEmpty(pwdValue)) {
            customDialog.show();
            //登录
            final MyUser user = new MyUser();
            user.setUsername(nameValue);
            user.setPassword(pwdValue);
            user.login(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    customDialog.dismiss();
                    //判断结果
                    if (e == null) {
                        //判断邮箱是否验证
                        if (user.getEmailVerified()) {
                            //记住密码
                            savePwd();
                            //跳转
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            UtilTools.toastShortMessage(LoginActivity.this, "请前往邮箱验证");
                        }
                    } else {
                        UtilTools.toastShortMessage(LoginActivity.this, "登录失败:" + e.toString());
                    }
                }
            });
        } else {
            UtilTools.toastShortMessage(this, "输入框不能为空");
        }
    }

}
