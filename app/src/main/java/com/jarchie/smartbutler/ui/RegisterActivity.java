package com.jarchie.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.entity.MyUser;
import com.jarchie.smartbutler.utils.UtilTools;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   RegisterActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/17 上午10:10
 * 描述:     注册页面
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText user, age, desc, pass, password, email;
    private RadioGroup mRadioGroup;
    private Button registerBtn;
    //性别
    private boolean isMan = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    //初始化控件
    private void initView() {
        user = (EditText) findViewById(R.id.et_user);
        age = (EditText) findViewById(R.id.et_age);
        desc = (EditText) findViewById(R.id.et_desc);
        pass = (EditText) findViewById(R.id.et_pass);
        password = (EditText) findViewById(R.id.et_password);
        email = (EditText) findViewById(R.id.et_email);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        registerBtn = (Button) findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                registerProcessMethod();
                break;
        }
    }

    //注册处理逻辑
    private void registerProcessMethod(){
        //获取输入框的值
        String userValue = user.getText().toString().trim();
        String ageValue = age.getText().toString().trim();
        String descValue = desc.getText().toString().trim();
        String passValue = pass.getText().toString().trim();
        String pwdValue = password.getText().toString().trim();
        String emailValue = email.getText().toString().trim();
        if (!TextUtils.isEmpty(userValue) & !TextUtils.isEmpty(ageValue) &
                !TextUtils.isEmpty(passValue) & !TextUtils.isEmpty(pwdValue) &
                !TextUtils.isEmpty(emailValue)) {
            //判断两次输入的密码是否一致
            if (passValue.equals(pwdValue)) {
                //先把性别判断一下
                mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        if (checkedId == R.id.rb_boy) {
                            isMan = true;
                        } else if (checkedId == R.id.rb_girl) {
                            isMan = false;
                        }
                    }
                });
                //判断简介是否为空
                if (TextUtils.isEmpty(descValue)) {
                    descValue = "这个人很懒,什么都没有留下";
                }
                //注册
                MyUser user = new MyUser();
                user.setUsername(userValue);
                user.setPassword(passValue);
                user.setEmail(emailValue);
                user.setAge(Integer.parseInt(ageValue));
                user.setSex(isMan);
                user.setDesc(descValue);
                user.signUp(new SaveListener<MyUser>() {

                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null) {
                            UtilTools.toastShortMessage(RegisterActivity.this, "注册成功");
                            finish();
                        } else {
                            UtilTools.toastShortMessage(RegisterActivity.this, "注册失败:" + e.toString());
                        }
                    }
                });
            } else {
                UtilTools.toastShortMessage(this, "两次输入的密码不一致");
            }
        } else {
            UtilTools.toastShortMessage(this, "输入框不能为空");
        }
    }
}
