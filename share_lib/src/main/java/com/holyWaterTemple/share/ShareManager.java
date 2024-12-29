package com.holywatertemple.share;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by zhangyiipeng on 2018/7/4.
 */

public class ShareManager {

    public static final String TAG = "ShareManager";

    private static ShareManager instance;

    public static ShareManager getInstance() {
        if (instance == null) {
            synchronized (ShareManager.class) {
                if (instance == null) {
                    instance = new ShareManager();
                }
            }
        }
        return instance;
    }

    private ShareManager() {
    }

    private Context mContext;

    public void init(Context context) {
        mContext = context.getApplicationContext();
        registerWeChat(mContext);
    }

    public static final String APP_ID = "wx8bc54b0d7a7f427b";    //这个APP_ID就是注册APP的时候生成的

    public static final String APP_SECRET = "62a18fa3b126a306f1a3a04f05e0dff2";

    public IWXAPI mWXApi;      //这个对象是专门用来向微信发送数据的一个重要接口,使用强引用持有,所有的信息发送都是基于这个对象的

    public void registerWeChat(Context context) {   //向微信注册app
        mWXApi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mWXApi.registerApp(APP_ID);
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    /**
     * scene 0-分享给朋友 1-分享至朋友圈
     */
    public void shareFileToFriend(String filePath, String fileName) {
        shareFile(SendMessageToWX.Req.WXSceneSession, filePath, fileName);
    }

    /**
     * 文件无法分享到朋友圈
     *
     * @param filePath
     * @param fileName
     */
    public void shareFileToFriendCircle(String filePath, String fileName) {
        shareFile(SendMessageToWX.Req.WXSceneTimeline, filePath, fileName);
    }

    /**
     * 分享到收藏
     * @param filePath
     * @param fileName
     */
    public void shareFileToFavorite(String filePath, String fileName) {
        shareFile(SendMessageToWX.Req.WXSceneFavorite, filePath, fileName);
    }

    /**
     *  // 发送到聊天界面 —— WXSceneSession
     *  // 发送到朋友圈 —— WXSceneTimeline
     *  // 添加到微信收藏 —— WXSceneFavorite
     req.scene = SendMessageToWX.Req.WXSceneSession;
     * @param scene
     * @param filePath
     * @param fileName
     */
    private void shareFile(int scene, String filePath, String fileName) {
        if (!mWXApi.isWXAppInstalled()) {
            Toast.makeText(mContext, "微信未安装", Toast.LENGTH_SHORT).show();
            return;
        }
        WXFileObject fileObject = new WXFileObject();
        fileObject.setContentLengthLimit(1024 * 1024 * 10);
        fileObject.setFilePath(filePath + fileName);//设置文件本地地址
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = fileObject;
        msg.title = fileName;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("file");
        req.message = msg;
        req.scene = scene;
        mWXApi.sendReq(req);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void shareText() {
        if (!mWXApi.isWXAppInstalled()) {
            Toast.makeText(mContext, "微信未安装", Toast.LENGTH_SHORT).show();
            return;
        }
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = "微信文本分享测试";
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;   // 发送文本类型的消息时，title字段不起作用
        msg.title = "Will be ignored";
        msg.description = "微信文本分享测试";   // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;   // 分享或收藏的目标场景，通过修改scene场景值实现。
        // 发送到聊天界面 —— WXSceneSession
        // 发送到朋友圈 —— WXSceneTimeline
        // 添加到微信收藏 —— WXSceneFavorite
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        // 调用api接口发送数据到微信
        final boolean b = mWXApi.sendReq(req);
        Log.d(TAG, "mWXApi.sendReq(req):" + b);
    }


}
