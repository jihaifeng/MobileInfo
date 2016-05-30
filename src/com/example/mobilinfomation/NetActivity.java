package com.example.mobilinfomation;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.example.utils.NetConnectUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_net)
public class NetActivity extends Activity {
	private ArrayList<String> netInfoList;
	@ViewInject(R.id.net)
	private TextView net;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(NetActivity.this);
		netInfoList = new ArrayList<String>();
		isMobileConnected(NetActivity.this);
		isWifiConnected(NetActivity.this);
		// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
		// if(mobile==State.CONNECTED||mobile==State.CONNECTING)
		// return;
		// if(wifi==State.CONNECTED||wifi==State.CONNECTING)
		// return;

		net.setText(netInfoList.size() + "\n" + getListInfo());
//		NetConnectUtils.getInstance().showNetDialog(NetActivity.this);
	}

	private String getListInfo() {
		if (NetConnectUtils.getInstance().isNetConnected(NetActivity.this)) {
			NetworkInfo networkInfo = NetConnectUtils.getInstance()
					.getNetworkInfo(NetActivity.this);
			if (networkInfo != null) {
				netInfoList.add("\n" + "详细状态 : "
						+ networkInfo.getDetailedState() + "\n");
				netInfoList.add("附加信息 : " + networkInfo.getExtraInfo() + "\n");
				netInfoList.add("连接失败的原因 : " + networkInfo.getReason() + "\n");
				// 返回值 -1：没有网络 1：WIFI网络2：wap网络3：net网络
				netInfoList.add("网络类型 : " + networkInfo.getType() + "\n");
				netInfoList.add("网络类型名称 : " + networkInfo.getTypeName() + "\n");
				netInfoList.add("网络是否可用 : " + networkInfo.isAvailable() + "\n");
				netInfoList.add("是否已经连接 : " + networkInfo.isConnected() + "\n");
				netInfoList.add("是否已经连接或正在连接 : "
						+ networkInfo.isConnectedOrConnecting() + "\n");
				netInfoList.add("是否连接失败 : " + networkInfo.isFailover() + "\n");
				netInfoList.add("是否漫游 : " + networkInfo.isRoaming() + "\n");
				// netInfoList.add("设备支持的网络 : " + infos.toString() + "\n");
			}
		}
		return netInfoList.toString();

	}


	/**
	 * 判断wifi是否可用
	 * 
	 * @param context
	 * @return
	 */
	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				netInfoList.add("wifi状态 : " + mWiFiNetworkInfo.isConnected()
						+ "  " + mWiFiNetworkInfo.getState() + "\n");
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断mobile是否可用
	 * 
	 * @param context
	 * @return
	 */
	public boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				netInfoList.add("mobile状态 : "
						+ mMobileNetworkInfo.isConnected() + "  "
						+ mMobileNetworkInfo.getState() + "\n");
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
