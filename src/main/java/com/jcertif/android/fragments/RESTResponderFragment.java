/*
 * Copyright 2013-2014 JCertifLab.
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
package com.jcertif.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.service.RESTService;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * 
 * @author Patrick Bashizi
 * @author Komi Serge Innocent <komi.innocent@gmail.com>
 * 
 */
public abstract class RESTResponderFragment extends SherlockFragment implements RestFragment {

	private ResultReceiver mReceiver;
	protected boolean refreshing=false;
	protected PullToRefreshAttacher mPullToRefreshAttacher;
	
	public RESTResponderFragment() {

		mReceiver = new ResultReceiver(new Handler()) {

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				if (resultData != null
						&& resultData.containsKey(RESTService.REST_RESULT)) {
					onRESTResult(resultCode, resultData);
				} else {
					onRESTResult(resultCode, null);
				}
			}

		};
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public ResultReceiver getResultReceiver() {
		return mReceiver;
	}

	protected void setLoading(boolean state) {
		SherlockFragmentActivity act = getSherlockActivity();
		if (act != null) {
			act.setSupportProgressBarIndeterminateVisibility(state);
		}
	}

    @Override
    public JcertifApplication getApplicationContext() {
        Activity activity=getActivity();
        Context applicationContext = activity.getApplicationContext();
        return (JcertifApplication) applicationContext;
    }
}
