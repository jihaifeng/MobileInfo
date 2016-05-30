package com.example.bean;

import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.graphics.drawable.Drawable;

public class AppInfoBean {
	public String appName;// 应用名称
	public String packageName;// 包名
	public String versionName;// 版本名称
	public int versionCode;// 版本号
	public Drawable appIcon;// app 图标
	public Intent appIntent;// list item 点击动作
	public String lastInstal;// 最后一次安装时间
	public String instalPath;// 安装路径
	public String appSize;// 安装路径
}
