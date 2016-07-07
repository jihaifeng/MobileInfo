package com.example.mobilinfomation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import com.example.bean.MobileInfoBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_mobile_info)
public class MobileInfoActivity extends Activity {
	private ArrayList<String> infoList;
	private MobileInfoBean mobileInfoBean;

	@ViewInject(R.id.tv_info)
	private TextView tv_mobileInfo;
	@ViewInject(R.id.tv_bat)
	private TextView tv_bat;
	@ViewInject(R.id.tv_cpu)
	private TextView tv_cpu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(MobileInfoActivity.this);
		mobileInfoBean = new MobileInfoBean();
		infoList = new ArrayList<String>();

		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				updateUI();
				handler.postDelayed(this, 1000);

			}
		};
		handler.removeCallbacks(runnable);
		handler.postDelayed(runnable, 1000);
	}

	/**
	 * 更新UI
	 */
	protected void updateUI() {
		getMobileInfo();
		getCupInfo();
		getAvailSave();
		getTotalSave();
		getMaxCpuFreq();
		getMinCpuFreq();
		getCurCpuFreq();
		getBatteryLevel();
		setListInfo();
		tv_mobileInfo.setText(infoList.toString());
	}

	/**
	 * 设置信息
	 */
	private void setListInfo() {
		if (infoList != null) {
			infoList.clear();
		}
		infoList.add("\n" + "imei号 : " + mobileInfoBean.imei + "\n");
		infoList.add("imsi号 : " + mobileInfoBean.imsi + "\n");
		infoList.add("手机品牌 : " + mobileInfoBean.mtyb + "\n");
		infoList.add("运营商 ： " + mobileInfoBean.serviceName + "\n");
		infoList.add("手机号 ： " + mobileInfoBean.number + "\n");
		infoList.add("手机型号 : " + mobileInfoBean.mtype + "\n");
		infoList.add("运行中的进程数量 ： " + mobileInfoBean.runningProcess + "\n");

		infoList.add("MAC地址 ：" + mobileInfoBean.macAddr + "\n");
		infoList.add("cpu型号 ： " + mobileInfoBean.cpuType + "\n");
		infoList.add("cpu频率  最大 ： " + mobileInfoBean.cpuMaxHz + "KHZ  " + "\n");
		infoList.add("cpu频率  最小 ： " + mobileInfoBean.cpuMinHz + "KHZ  " + "\n");
		infoList.add("可用内存 : " + mobileInfoBean.availSave + "\n");
		infoList.add("总内存 : " + mobileInfoBean.totalSave + "\n");
		infoList.add("sdk版本 ： " + mobileInfoBean.sdk + "\n");
		infoList.add("系统版本号 ： " + mobileInfoBean.release + "\n");
		infoList.add("deviceId： " + mobileInfoBean.deviceId + "\n");
	}

	/**
	 * 获取手机信息
	 */
	private void getMobileInfo() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		mobileInfoBean.macAddr = wifiInfo.getMacAddress();// 手机MAC地址，只有wifi下才能获取到
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mobileInfoBean.imei = telephonyManager.getDeviceId();// 手机imei号
		mobileInfoBean.imsi = telephonyManager.getSubscriberId();// 手机imsi号
		mobileInfoBean.number = (TextUtils.isEmpty(telephonyManager.getLine1Number()) ? null : telephonyManager.getLine1Number());// 手机号
		mobileInfoBean.serviceName = telephonyManager.getSimOperatorName(); // 运营商
		mobileInfoBean.mtyb = Build.BRAND;// 手机品牌
		mobileInfoBean.mtype = Build.MODEL;// 手机型号
		mobileInfoBean.sdk = Build.VERSION.SDK_INT;// sdk版本号
		mobileInfoBean.release = Build.VERSION.RELEASE;// 系统版本号
		mobileInfoBean.deviceId = telephonyManager.getDeviceId();

	}

	// 获取CPU最大频率（单位KHZ）
	// "/system/bin/cat" 命令行
	// "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
	private void getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		mobileInfoBean.cpuMaxHz = result.trim();
	}

	// 获取CPU最小频率（单位KHZ）
	private void getMinCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		mobileInfoBean.cpuMinHz = result.trim();
	}

	/**
	 * 实时获取CPU当前频率（单位KHZ）
	 */
	private void getCurCpuFreq() {
		String result = "N/A";
		try {
			FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			result = text.trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mobileInfoBean.cpuCurHz = result.trim();
		tv_cpu.setText("cup频率 ： " + result);
	}

	/**
	 * 获取cup信息
	 */
	private void getCupInfo() {
		String str1 = "proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" }; // 0-cpu型号 //1-cpu频率
		String[] str;
		String total_memory = "0";
		try {
			FileReader fileReader = new FileReader(str1);
			BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
			str2 = bufferedReader.readLine();
			str = str2.split("\\s+");
			for (int i = 2; i < str.length; i++) {
				cpuInfo[0] = cpuInfo[0] + str[i] + " ";
			}
			str2 = bufferedReader.readLine();
			str = str2.split("\\s+");
			cpuInfo[1] += str[2];
			total_memory = Formatter.formatFileSize(MobileInfoActivity.this, Integer.valueOf(str[1]).intValue() * 1024);// 获得系统总内存，单位是KB，乘以1024转换为Byte
			bufferedReader.close();
		} catch (Exception e) {
		}
		mobileInfoBean.cpuType = cpuInfo[0];
		// mobileInfoBean.cpuMaxHz = "cpu频率 ： " + cpuInfo[1];
	}

	/**
	 * 获取android当前可用内存大小
	 */
	private void getAvailSave() {
		// 获取android当前可用内存大小
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		mobileInfoBean.runningProcess = activityManager.getRunningAppProcesses().size();
		Log.i("processaaa", activityManager.getRunningAppProcesses().toString());
		mobileInfoBean.availSave = Formatter.formatFileSize(MobileInfoActivity.this, memoryInfo.availMem);
	}

	/**
	 * 获取总内存大小
	 */
	private void getTotalSave() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i("str2", num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		mobileInfoBean.totalSave = Formatter.formatFileSize(MobileInfoActivity.this, initial_memory);// Byte转换为KB或者MB，内存大小规格化

	}

	/**
	 * 获取手机剩余电量
	 */
	private void getBatteryLevel() {
		BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				int batNow = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);// 获得当前电量
				int batTotal = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);// 获得总电量
				int level = -1;
				if (batNow >= 0 && batTotal > 0) {
					level = (batNow * 100) / batTotal;
				}
				mobileInfoBean.batteryLevel = level + " %";
				tv_bat.setText("手机电量 ： " + mobileInfoBean.batteryLevel);
			}
		};
		IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryReceiver, batteryFilter);
	}
}
