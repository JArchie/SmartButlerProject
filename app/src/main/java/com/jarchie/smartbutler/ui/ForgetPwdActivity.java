package com.jarchie.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.entity.MyUser;
import com.jarchie.smartbutler.utils.UtilTools;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   ForgetPwdActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/17 下午4:40
 * 描述:     忘记/重置密码
 */

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener{
    private Button forgetBtn,changepwdBtn;
    private EditText oldpwd,newpwd,againpwd,email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    //初始化View
    private void initView() {
        forgetBtn = (Button) findViewById(R.id.btn_forgetpwd);
        changepwdBtn = (Button) findViewById(R.id.btn_changepwd);
        forgetBtn.setOnClickListener(this);
        changepwdBtn.setOnClickListener(this);
        email = (EditText) findViewById(R.id.et_email);
        oldpwd = (EditText) findViewById(R.id.et_old);
        newpwd = (EditText) findViewById(R.id.et_new);
        againpwd = (EditText) findViewById(R.id.et_again);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_forgetpwd:
                findPassword();
                break;
            case R.id.btn_changepwd:
                changePassword();
                break;
        }
    }

    //修改密码
    private void changePassword(){
        String oldValue = oldpwd.getText().toString().trim();
        String newValue = newpwd.getText().toString().trim();
        String againValue = againpwd.getText().toString().trim();
        if (!TextUtils.isEmpty(oldValue) & !TextUtils.isEmpty(newValue) & !TextUtils.isEmpty(againValue)){
            //判断两次密码是否一致
            if (newValue.equals(againValue)){
                MyUser.updateCurrentUserPassword(oldValue, newValue, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            UtilTools.toastShortMessage(ForgetPwdActivity.this,"重置密码成功");
                            finish();
                        }else {
                            UtilTools.toastShortMessage(ForgetPwdActivity.this,"重置密码失败");
                        }
                    }
                });
            }else {
                UtilTools.toastShortMessage(ForgetPwdActivity.this,"两次输入的密码不一致");
            }
        }else {
            UtilTools.toastShortMessage(this,"输入框不能为空");
        }
    }

    //邮箱找回密码
    private void findPassword(){
        //获取输入的邮箱
        final String emailValue = email.getText().toString().trim();
        //判断是否为空
        if (!TextUtils.isEmpty(emailValue)){
            //发送邮件重置密码
            MyUser.resetPasswordByEmail(emailValue, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null){
                        UtilTools.toastShortMessage(ForgetPwdActivity.this,
                                "邮件已经发送至:"+emailValue);
                        finish();
                    }else {
                        UtilTools.toastShortMessage(ForgetPwdActivity.this,"邮件发送失败");
                    }
                }
            });
        }else {
            UtilTools.toastShortMessage(this,"输入框不能为空");
        }
    }

}
