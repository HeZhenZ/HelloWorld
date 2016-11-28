package com.example.weixincaozuo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wxapi.WXEntryActivity;

public class MainActivity extends Activity {
	  private IWXAPI mApi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

    /**
     * 微信授权登录流程
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317851&token=&lang=zh_CN
     * 1.sendReq(req). 用户授权可以拿到code
     * 2.用code.调用Wx接口拿到 openid & accessToken
     * 3.通过openid & accessToken 俩参数可以拿到最终用户信息
     *
     * @param view
     */
    public void onButton(View view) {
        mApi = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, true);
        mApi.registerApp(WXEntryActivity.APP_ID);

        if (mApi != null && mApi.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test_neng";
            mApi.sendReq(req);
        } else
            Toast.makeText(this, "用户未安装微信", Toast.LENGTH_SHORT).show();

    }
}
