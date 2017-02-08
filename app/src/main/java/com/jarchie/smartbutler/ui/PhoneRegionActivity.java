package com.jarchie.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.utils.StaticClass;
import com.jarchie.smartbutler.utils.UtilTools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   PhoneRegionActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/22 下午3:11
 * 描述:     创建归属地查询的类,自定义键盘逻辑的实现
 */

public class PhoneRegionActivity extends BaseActivity implements View.OnClickListener {
    private EditText number;
    private ImageView imageView;
    private TextView result;
    private Button button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, buttonDel, buttonSel;
    private String str;
    //标记位
    private boolean flag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneregion);
        initView();
    }

    //初始化View
    private void initView() {
        number = (EditText) findViewById(R.id.et_number);
        imageView = (ImageView) findViewById(R.id.iv_company);
        result = (TextView) findViewById(R.id.tv_result);
        button0 = (Button) findViewById(R.id.btn_0);
        button1 = (Button) findViewById(R.id.btn_1);
        button2 = (Button) findViewById(R.id.btn_2);
        button3 = (Button) findViewById(R.id.btn_3);
        button4 = (Button) findViewById(R.id.btn_4);
        button5 = (Button) findViewById(R.id.btn_5);
        button6 = (Button) findViewById(R.id.btn_6);
        button7 = (Button) findViewById(R.id.btn_7);
        button8 = (Button) findViewById(R.id.btn_8);
        button9 = (Button) findViewById(R.id.btn_9);
        buttonDel = (Button) findViewById(R.id.btn_del);
        buttonSel = (Button) findViewById(R.id.btn_select);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonDel.setOnClickListener(this);
        buttonSel.setOnClickListener(this);
        //删除按钮的长按事件
        buttonDel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                number.setText("");
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        //获取输入框中的内容
        str = number.getText().toString();
        switch (view.getId()) {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if (flag) {
                    flag = false;
                    str = "";
                    number.setText("");
                }
                //每次结尾加1
                number.setText(str + ((Button) view).getText().toString());
                number.setSelection(str.length() + 1);
                break;
            case R.id.btn_del:
                if (!TextUtils.isEmpty(str) && str.length() > 0) {
                    //每次结尾减去1
                    number.setText(str.substring(0, str.length() - 1));
                    number.setSelection(str.length() - 1);
                }
                break;
            case R.id.btn_select:
                if (!TextUtils.isEmpty(str)) {
                    queryPhoneRegion();
                }
                break;
        }
    }

    //获取归属地,网络请求数据
    private void queryPhoneRegion() {
        String url = "http://apis.juhe.cn/mobile/get?phone=" + str + "&key=" + StaticClass.PHONE_REGION;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parserDataToWidget(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                UtilTools.toastShortMessage(PhoneRegionActivity.this, strMsg);
            }
        });
    }

    //解析Json数据,并显示到相关控件上面
    private void parserDataToWidget(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject resultString = jsonObject.getJSONObject("result");
            String province = resultString.getString("province");
            String city = resultString.getString("city");
            String areacode = resultString.getString("areacode");
            String zip = resultString.getString("zip");
            String company = resultString.getString("company");
            String card = resultString.getString("card");
            result.setText("归属地:" + province + city + "\n" +
                    "区号:" + areacode + "\n" + "邮编:" + zip + "\n" +
                    "运营商:" + company + "\n" + "类型:" + card);
            //图片显示
            switch (company) {
                case "移动":
                    imageView.setImageResource(R.drawable.china_moblie);
                    break;
                case "联通":
                    imageView.setImageResource(R.drawable.china_ubicom);
                    break;
                case "电信":
                    imageView.setImageResource(R.drawable.china_telecom);
                    break;
            }
            flag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
