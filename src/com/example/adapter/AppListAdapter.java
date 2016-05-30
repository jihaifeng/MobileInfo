package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.AppInfoBean;
import com.example.mobilinfomation.R;
import com.lidroid.xutils.BitmapUtils;

public class AppListAdapter extends BaseAdapter {
	private List<AppInfoBean> list;
	private Context context;

	public AppListAdapter(Context context, List<AppInfoBean> list) {
		this.context = context;
		this.list = list;
		notifyDataSetChanged();
	}

	static class ViewHolder {
		ImageView appIcon;
		TextView appName;
		TextView appVisionName;
		TextView appVisionCode;
		TextView packageName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.app_list_item, null);
			holder.appIcon = (ImageView) convertView.findViewById(R.id.appIcon);
			holder.appName = (TextView) convertView.findViewById(R.id.appName);
			holder.appVisionCode = (TextView) convertView.findViewById(R.id.appVisionCode);
			holder.appVisionName = (TextView) convertView.findViewById(R.id.appVisionName);
			holder.packageName = (TextView) convertView.findViewById(R.id.packageName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.appIcon.setBackgroundDrawable(list.get(position).appIcon);
		holder.appName.setText(list.get(position).appName);
//		holder.appVisionCode.setText(" " + list.get(position).versionCode);
		holder.appVisionCode.setVisibility(View.GONE);
		holder.appVisionName.setText(list.get(position).versionName);
		holder.packageName.setText(list.get(position).packageName);
		return convertView;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
