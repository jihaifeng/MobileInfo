package com.example.mobilinfomation;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.adapter.AppListAdapter;
import com.example.bean.AppInfoBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_myapp)
public class MyAppActivity extends Activity {
	private ArrayList<AppInfoBean> systemAppInfoList;
	private ArrayList<AppInfoBean> otherAppInfoList;
	private List<PackageInfo> packageInfosList;
	private AppListAdapter appListAdapter;
	private boolean isSystem = false;// true 系统，false 非系统
	private AppInfoBean appInfo;
	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount; // 当前窗口可见项总数
	private Thread thread;

	@ViewInject(R.id.appList)
	private ListView appList;
	@ViewInject(R.id.tog)
	private ToggleButton tog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(MyAppActivity.this);

		getAppList();
		tog.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// 系统
					isSystem = true;
					setAdapter();
				} else {
					// 非系统
					isSystem = false;
					setAdapter();
				}

			}
		});
		appList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (isSystem) {
					appInfo = systemAppInfoList.get(position);
				} else {
					appInfo = otherAppInfoList.get(position);
				}
				String msg = "包名 : " + appInfo.packageName + "\n版本名称:"
						+ appInfo.versionName + "\n版本号：" + appInfo.versionCode
						+ "\n安装时间：" + appInfo.lastInstal + "\napp大小 ："
						+ appInfo.appSize + "\n安装路径：" + appInfo.instalPath;
				Builder builder = new AlertDialog.Builder(MyAppActivity.this);
				builder.setTitle(appInfo.appName);
				builder.setIcon(appInfo.appIcon);
				builder.setMessage(msg);
				builder.setNegativeButton("关闭", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				builder.setPositiveButton("打开", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							startActivity(appInfo.appIntent);
						} catch (Exception e) {
							System.out.println(e.toString());
							Toast.makeText(MyAppActivity.this, "该应用不支持打开",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});

	}

	private void setAdapter() {
		if (isSystem) {
			// 系统
			appListAdapter = new AppListAdapter(MyAppActivity.this,
					systemAppInfoList);
		} else {
			// 非系统
			appListAdapter = new AppListAdapter(MyAppActivity.this,
					otherAppInfoList);
		}
		appList.setAdapter(appListAdapter);
	}

	private Runnable action = new Runnable() {

		@Override
		public void run() {
			if (appListAdapter != null) {
				appListAdapter.notifyDataSetChanged();
			}
		}
	};
	Runnable getApp = new Runnable() {

		@Override
		public void run() {
			packageInfosList = getPackageManager().getInstalledPackages(0);
			// 遍历所有已安装应用
			for (PackageInfo packageInfo : packageInfosList) {
				if (isFinishing()) {
					return;
				}
				AppInfoBean appInfoBean;
				// 判断是否是系统应用
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// 不是系统应用
					appInfoBean = setListData(packageInfo);
					otherAppInfoList.add(appInfoBean);
					appList.post(action);
				} else {
					// 是系统应用
					appInfoBean = setListData(packageInfo);
					systemAppInfoList.add(appInfoBean);
					appList.post(action);

				}
			}
		}
	};

	private void getAppList() {
		otherAppInfoList = new ArrayList<AppInfoBean>();
		systemAppInfoList = new ArrayList<AppInfoBean>();
		setAdapter();
		thread = new Thread(getApp);
		thread.start();
	}

	private AppInfoBean setListData(PackageInfo packageInfo) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		appInfo = new AppInfoBean();
		appInfo.appName = packageInfo.applicationInfo.loadLabel(
				getPackageManager()).toString();
		appInfo.packageName = packageInfo.packageName;
		appInfo.versionName = packageInfo.versionName;
		appInfo.versionCode = packageInfo.versionCode;
		appInfo.appIcon = packageInfo.applicationInfo
				.loadIcon(getPackageManager());
		appInfo.lastInstal = format
				.format(new Date(packageInfo.lastUpdateTime));
		appInfo.instalPath = packageInfo.applicationInfo.sourceDir;
		appInfo.appSize = Formatter.formatFileSize(MyAppActivity.this, Long
				.valueOf((int) new File(
						packageInfo.applicationInfo.publicSourceDir).length()));
		appInfo.appIntent = getPackageManager().getLaunchIntentForPackage(
				packageInfo.packageName);
		return appInfo;
	}

	@Override
	protected void onResume() {
		// otherAppInfoList.clear();
		// systemAppInfoList.clear();
		// appListAdapter.notifyDataSetChanged();

		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// otherAppInfoList.clear();
		// systemAppInfoList.clear();
		// appListAdapter.notifyDataSetChanged();
		Toast.makeText(MyAppActivity.this, "onPause", 3000).show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// otherAppInfoList.clear();
		// systemAppInfoList.clear();
		// appListAdapter.notifyDataSetChanged();
		Toast.makeText(MyAppActivity.this, "onDestroy", 3000).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			MyAppActivity.this.finish();
		}
		return true;
	}
}
