package com.holywatertemple.wxapi;


import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.holywatertemple.share.R;
import com.holywatertemple.share.ShareManager;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.holywatertemple.share.ShareManager.APP_ID;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by zhangyiipeng on 2018/7/4.
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx);

       ShareManager.getInstance().getWXApi().handleIntent(getIntent(), this);
    }


    @Override
    public void onResp(BaseResp resp) { //在这个方法中处理微信传回的数据
        //形参resp 有下面两个个属性比较重要
        //1.resp.errCode
        //2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
        switch (resp.errCode) { //根据需要的情况进行处理
            case BaseResp.ErrCode.ERR_OK:
                //正确返回
                Log.d(TAG,"正确返回");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                Log.d(TAG,"用户取消");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
                Log.d(TAG,"认证被否决");
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                //发送失败
                Log.d(TAG,"发送失败");
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                //不支持错误
                Log.d(TAG,"不支持错误");
                break;
            case BaseResp.ErrCode.ERR_COMM:
                //一般错误
                Log.d(TAG,"一般错误");
                break;
            default:
                //其他不可名状的情况
                Log.d(TAG,"其他不可名状的情况");
                break;
        }
    }

    @Override
    public void onReq(BaseReq req) {
        //......这里是用来处理接收的请求,暂不做讨论
    }

}
