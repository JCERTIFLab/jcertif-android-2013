/*
 * Copyright 2013 JCertifLab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
