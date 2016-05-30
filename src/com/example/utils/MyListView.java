package com.example.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class MyListView extends ListView implements OnScrollListener {

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (getAdapter() == null) {
			return;
		}
		if (getAdapter().getCount() == 0) {
			return;
		}
		int lastItemIndex = firstVisibleItem + visibleItemCount;
		if (lastItemIndex >= totalItemCount) {
			// 滑到最低项
//			addFooterView(footerView);// 用来提示用户正在加载下一页的数据
//			isLoading = true;
//			listener.loadData();
		}

	}
}
