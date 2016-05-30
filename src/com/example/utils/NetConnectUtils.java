package com.example.utils;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * 网络连接Utils
 * 
 * @param isNetConnected
 *            判断网络是否已经连接上
 * @param getNetType
 *            若已经连接上网络，获取当前网络类型（wifi，移动，联通，电信）
 * @param showNetDialog
 *            若网络未连接，可以弹出dialog，引导用户设置网络
 * @author Lucky
 * 
 */
public class NetConnectUtils {
	private static final String TAG = NetConnectUtils.class.getSimpleName()
			.trim();
	private static NetConnectUtils instance;
	private HashMap<String, String> hashMap = new HashMap<String, String>();
	// 中国移动cmwap
	public static String CMWAP = "cmwap";
	// 中国移动cmnet
	public static String CMNET = "cmnet";
	// 中国联通3GWAP设置 中国联通3G因特网设置 中国联通WAP设置 中国联通因特网设置
	// 3gwap 3gnet uniwap uninet
	// 3G wap 中国联通3gwap APN
	public static String GWAP_3 = "3gwap";
	// 3G net 中国联通3gnet APN
	public static String GNET_3 = "3gnet";
	// uni wap 中国联通uni wap APN
	public static String UNIWAP = "uniwap";
	// uni net 中国联通uni net APN
	public static String UNINET = "uninet";

	/**
	 * 电信APN列表
	 * 
	 * @author wudongdong
	 * 
	 */
	public static final String CTWAP = "ctwap";
	public static final String CTNET = "ctnet";

	/**
	 * 获取工具类的实例
	 * 
	 * @return
	 */
	public static NetConnectUtils getInstance() {
		if (instance == null) {
			instance = new NetConnectUtils();
		}
		return instance;
	}

	/**
	 * 判断网络连接
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetConnected(Context context) {
		boolean isAvailable = false;
		if (context != null) {
			NetworkInfo networkInfo = getNetworkInfo(context);
			if (networkInfo != null) {
				State mState = networkInfo.getState();
				String stateName = mState.name();
				if (stateName.equals(String.valueOf(State.CONNECTED))) {
					isAvailable = true;
				}
				// 网络类型，用来判断是手机网络还是wifi网络
				hashMap.put(
						"netTypeName",
						(networkInfo.getTypeName() != null) ? networkInfo
								.getTypeName() : null);
				/**
				 * 附加信息，当使用手机网络连接时，用来判断网络连接的具体类型 （移动【cmwap，cmnet】，联通【3gwap，3gnet
				 * ,uniwap,uninet】，电信【ctwap,ctent】）
				 */
				hashMap.put(
						"extraInfo",
						(networkInfo.getExtraInfo() != null) ? networkInfo
								.getExtraInfo() : null);
				// 网络连接状态，用来判断网络是否连接
				hashMap.put("stateName",
						(mState != null && stateName != null) ? stateName
								: null);
			} else {
				Log.i(TAG, "networkInfo------null");
			}
		}
		return isAvailable;

	}

	/**
	 * 获取网络的状态信息
	 * 
	 * @param context
	 * @return
	 */
	public NetworkInfo getNetworkInfo(Context context) {
		if (context != null) {
			// 获取网络连接管理者
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				// 获取网络的状态信息
				NetworkInfo mNetworkInfo = connectivityManager
						.getActiveNetworkInfo();
				return mNetworkInfo;
			} else {
				Log.i(TAG, "connectivityManager------null");
			}
		} else {
			Log.i(TAG, "context------null");
		}
		return null;
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context
	 * @return
	 */
	public String getNetType(Context context) {
		String netType = "";
		if (isNetConnected(context)) {
			if (hashMap.get("stateName")
					.equals(String.valueOf(State.CONNECTED))) {
				// 网络是否已经连接
				if (hashMap.get("netTypeName").toLowerCase().equals("wifi")) {
					// wifi网络
					netType = hashMap.get("netTypeName");
				} else {
					// 手机网络
					String extraInfo = hashMap.get("extraInfo").toLowerCase();
					netType = matchNetType(extraInfo);
				}
			} else {
				Toast.makeText(context, "请先连接网络", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "请先连接网络", Toast.LENGTH_SHORT).show();
		}
		return netType;
	}

	/**
	 * 匹配网络类型
	 * 
	 * @param extraInfo
	 * @return
	 */
	private String matchNetType(String extraInfo) {
		if (TextUtils.isEmpty(extraInfo)) {
			Log.i(TAG, "extraInfo-------null");
			return "";
		}
		if (extraInfo.startsWith(CMNET))
			return CMNET;
		else if (extraInfo.startsWith(CMWAP))
			return CMWAP;
		else if (extraInfo.startsWith(GNET_3))
			return GNET_3;
		else if (extraInfo.startsWith(GWAP_3))
			return GWAP_3;
		else if (extraInfo.startsWith(UNINET))
			return UNINET;
		else if (extraInfo.startsWith(UNIWAP))
			return UNIWAP;
		else if (extraInfo.startsWith(CTWAP))
			return CTWAP;
		else if (extraInfo.startsWith(CTNET))
			return CTNET;
		else if (extraInfo.startsWith("default"))
			return "default";
		else
			return "";
	}

	public void showNetDialog(final Context context) {
		Builder builder = new Builder(context);
		builder.setTitle("警告");
		builder.setMessage("\n\b\b网络连接异常，是否立即设置网络？\n");
		builder.setPositiveButton("设置", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(Settings.ACTION_SETTINGS));// 进入设置界面
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();

	}
}
