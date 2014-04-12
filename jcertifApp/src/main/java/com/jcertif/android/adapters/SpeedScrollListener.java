package com.jcertif.android.adapters;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class SpeedScrollListener implements OnScrollListener {

	private int previousFirstVisibleItem = 0;
	private long previousEventTime = 0, currTime, timeToScrollOneElement;
	private double speed = 0;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (previousFirstVisibleItem != firstVisibleItem) {
			currTime = System.currentTimeMillis();
			timeToScrollOneElement = currTime - previousEventTime;
			speed = ((double) 1 / timeToScrollOneElement) * 1000;

			previousFirstVisibleItem = firstVisibleItem;
			previousEventTime = currTime;

		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	public double getSpeed() {
		return speed;
	}

}
