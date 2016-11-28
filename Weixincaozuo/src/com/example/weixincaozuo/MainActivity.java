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
     * ΢����Ȩ��¼����
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317851&token=&lang=zh_CN
     * 1.sendReq(req). �û���Ȩ�����õ�code
     * 2.��code.����Wx�ӿ��õ� openid & accessToken
     * 3.ͨ��openid & accessToken �����������õ������û���Ϣ
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
            Toast.makeText(this, "�û�δ��װ΢��", Toast.LENGTH_SHORT).show();

    }
}
