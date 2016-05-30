package com.example.mobilinfomation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {
	@ViewInject(R.id.btn_getInfo)
	private Button getInfo;
	@ViewInject(R.id.btn_getApp)
	private Button getApp;

	@ViewInject(R.id.btn_getNet)
	private Button getNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(MainActivity.this);
	}

	@OnClick({ R.id.btn_getInfo, R.id.btn_getApp ,R.id.btn_getNet})
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_getInfo:
			// 手机信息
			MainActivity.this.startActivity(new Intent(MainActivity.this,
					MobileInfoActivity.class));
			break;
		case R.id.btn_getApp:
			// 已安装APP
			MainActivity.this.startActivity(new Intent(MainActivity.this,
					MyAppActivity.class));
			break;
		case R.id.btn_getNet:
			// 已安装APP
			MainActivity.this.startActivity(new Intent(MainActivity.this,
					NetActivity.class));
			break;
		default:
			break;
		}

	}
}
