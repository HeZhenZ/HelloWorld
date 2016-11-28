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

	// ΢�Żص�����
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
		
		StringBuffer msg = new StringBuffer(); // ��֯һ������ʾ����Ϣ����
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
	
	// ���͵�΢���������Ӧ���
	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// ���ͳɹ�
			SendAuth.Resp sendResp = (SendAuth.Resp) resp;
			if (sendResp != null) {
				String code = sendResp.code;
				getAccess_token(code);
			}
			Toast.makeText(getApplicationContext(), "���ͳɹ�", 1).show();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// ����ȡ��
			Toast.makeText(getApplicationContext(), " ����ȡ��", 1).show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// ���ͱ��ܾ�
			Toast.makeText(getApplicationContext(), " ���ͱ��ܾ�", 1).show();
			break;
		default:
			// ���ͷ���
			Toast.makeText(getApplicationContext(), "���ͷ���", 1).show();
			break;
		}
		finish();

	}

	/**
	 * ��ȡopenid accessTokenֵ���ں��ڲ���
	 * 
	 * @param code
	 *            ������
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
							.initSSLWithHttpClinet(path);// ����https���Ӳ��õ�json���
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
			JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);// ����https���Ӳ��õ�json���
			if (null != jsonObject) {
				String nickname = jsonObject.getString("nickname");
				int sex = Integer.parseInt(jsonObject.get("sex").toString());
				String headimgurl = jsonObject.getString("headimgurl");

				Log.e(TAG, "getUserMesg �õ����û�Wx������Ϣ.. nickname:" + nickname);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;

	}

}
