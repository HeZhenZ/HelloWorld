package com.example.weixincaozuo;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HTTPSTrustManager implements X509TrustManager {
	private static TrustManager[] trustManagers;
	private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

	/**
	 * �÷������ͻ��˵�֤�飬�������θ�֤�����׳��쳣���������ǲ���Ҫ�Կͻ��˽�����֤��
	 * �������ֻ��Ҫִ��Ĭ�ϵ����ι����������������JSSE�У�Ĭ�ϵ����ι�������ΪTrustManager��
	 */
	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	/**
	 * �÷�������������֤�飬�������θ�֤��ͬ���׳��쳣��ͨ���Լ�ʵ�ָ÷���������ʹ֮��������ָ�����κ�֤�顣��ʵ�ָ÷���ʱ��
	 * Ҳ���Լ򵥵Ĳ����κδ�����һ���յĺ����壬���ڲ����׳��쳣�����ͻ������κ�֤�顣
	 */
	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	public boolean isClientTrusted(X509Certificate[] chain) {
		return true;
	}

	public boolean isServerTrusted(X509Certificate[] chain) {
		return true;
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return _AcceptedIssuers;
	}

	public static void allowAllSSL() {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}

		});

		SSLContext context = null;
		if (trustManagers == null) {
			trustManagers = new TrustManager[] { new HTTPSTrustManager() };
		}

		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustManagers, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(context
				.getSocketFactory());
	}

}
