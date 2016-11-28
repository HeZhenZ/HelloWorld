package com.wxapi;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.widget.Toast;

import com.example.weixincaozuo.JsonUtils;
import com.example.weixincaozuo.MainActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private final String TAG = this.getClass().getSimpleName();
	public static final String APP_ID = "wx2a5cddf88237d25b";
	public static final String APP_SECRET = "1eMXTh94Jvfz2z5NwtTKAPmenYL8CSd8";
	private IWXAPI mApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
		mApi.handleIntent(this.getIntent(), this);
	}

	// 微信回调方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();		
			Toast.makeText(getApplicationContext(), " goToGetMsg();", 1).show();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			Toast.makeText(getApplicationContext(), " goToShowMsg((ShowMessageFromWX.Req) req);", 1).show();
			break;
		default:
			break;
		}

	}
	private void goToGetMsg() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtras(getIntent());
		startActivity(intent);
		finish();
	}
	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;		
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
		
		StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);
		
		Intent intent = new Intent(this, MainActivity.class);
		// intent.putExtra(Constants.MainActivity.STitle, wxMsg.title);
		// intent.putExtra(Constants.MainActivity.SMessage, msg.toString());
		// intent.putExtra(Constants.MainActivity.BAThumbData, wxMsg.thumbData);
		startActivity(intent);
		finish();
	}
	
	// 发送到微信请求的响应结果
	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// 发送成功
			SendAuth.Resp sendResp = (SendAuth.Resp) resp;
			if (sendResp != null) {
				String code = sendResp.code;
				getAccess_token(code);
			}
			Toast.makeText(getApplicationContext(), "发送成功", 1).show();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// 发送取消
			Toast.makeText(getApplicationContext(), " 发送取消", 1).show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// 发送被拒绝
			Toast.makeText(getApplicationContext(), " 发送被拒绝", 1).show();
			break;
		default:
			// 发送返回
			Toast.makeText(getApplicationContext(), "发送返回", 1).show();
			break;
		}
		finish();

	}

	/**
	 * 获取openid accessToken值用于后期操作
	 * 
	 * @param code
	 *            请求码
	 */
	private void getAccess_token(final String code) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
						+ APP_ID
						+ "&secret="
						+ APP_SECRET
						+ "&code="
						+ code
						+ "&grant_type=authorization_code";
				try {
					JSONObject jsonObject = JsonUtils
							.initSSLWithHttpClinet(path);// 请求https连接并得到json结果
					if (null != jsonObject) {
						String openid = jsonObject.getString("openid")
								.toString().trim();
						String access_token = jsonObject
								.getString("access_token").toString().trim();
						getUserMesg(access_token, openid);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	private void getUserMesg(final String access_token, final String openid) {
		String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ access_token + "&openid=" + openid;
		try {
			JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);// 请求https连接并得到json结果
			if (null != jsonObject) {
				String nickname = jsonObject.getString("nickname");
				int sex = Integer.parseInt(jsonObject.get("sex").toString());
				String headimgurl = jsonObject.getString("headimgurl");

				Log.e(TAG, "getUserMesg 拿到了用户Wx基本信息.. nickname:" + nickname);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;

	}

}
