package com.jarchie.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.adapter.ChatListAdapter;
import com.jarchie.smartbutler.entity.ChatListBean;
import com.jarchie.smartbutler.utils.LogUtil;
import com.jarchie.smartbutler.utils.ShareUtil;
import com.jarchie.smartbutler.utils.StaticClass;
import com.jarchie.smartbutler.utils.UtilTools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.fragment
 * 文件名:   ButlerFragment
 * 创建者:   Jarchie
 * 创建时间: 17/1/15 下午2:12
 * 描述:     服务管家
 */

public class ButlerFragment extends Fragment implements View.OnClickListener {
    private EditText text;
    private Button send;
    private ListView mListview;
    private List<ChatListBean> mList = new ArrayList<>();
    private ChatListAdapter mAdapter;
    //TTS
    private SpeechSynthesizer mTts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler, null);
        initView(view);
        return view;
    }

    //初始化View
    private void initView(View view) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端

        text = (EditText) view.findViewById(R.id.et_text);
        send = (Button) view.findViewById(R.id.btn_send);
        send.setOnClickListener(this);
        mListview = (ListView) view.findViewById(R.id.mChatListView);
        //设置适配器
        mAdapter = new ChatListAdapter(getActivity(), mList);
        mListview.setAdapter(mAdapter);
        addLeftItem("主人,您好,我是Robot!");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //点击发送按钮发送消息
            case R.id.btn_send:
                sendMessage();
                break;
        }
    }

    //采用TTS引擎,进行语音合成服务
    private void startSpeak(String content) {
        //开始合成
        mTts.startSpeaking(content, mSynListener);

    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

    //解析聚合机器人接口返回的数据,并将其添加到左边显示出来
    private void parserJsonData(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject resultJson = jsonObject.getJSONObject("result");
            String textValue = resultJson.getString("text");
            addLeftItem(textValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发送消息,获取网络数据
    private void sendMessage() {
        String value = text.getText().toString();
        if (!TextUtils.isEmpty(value)) {
            if (text.length() > 30) {
                UtilTools.toastShortMessage(getActivity(), "输入长度超出限制");
            } else {
                text.setText("");
                addRightItem(value);
                String url = "http://op.juhe.cn/robot/index?info="
                        + value + "&key=" + StaticClass.ROBOT_KEY;
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        LogUtil.i("json:" + t);
                        parserJsonData(t);
                    }
                });
            }
        } else {
            UtilTools.toastShortMessage(getActivity(), "输入框不能为空");
        }
    }

    //添加左边文本
    private void addLeftItem(String text) {
        boolean isSpeak = ShareUtil.getBoolean(getActivity(), StaticClass.VOICE_FLAG, false);
        if (isSpeak) {
            //开始说话
            startSpeak(text);
        }
        ChatListBean bean = new ChatListBean();
        bean.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        bean.setText(text);
        mList.add(bean);
        //通知adapter刷新
        mAdapter.notifyDataSetChanged();
        //滚动到底部
        mListview.setSelection(mListview.getBottom());
    }

    //添加右边文本
    private void addRightItem(String text) {
        ChatListBean bean = new ChatListBean();
        bean.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        bean.setText(text);
        mList.add(bean);
        //通知adapter刷新
        mAdapter.notifyDataSetChanged();
        //滚动到底部
        mListview.setSelection(mListview.getBottom());
    }

}
