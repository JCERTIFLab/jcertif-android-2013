package com.jcertif.android.service;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.dao.CategorieProvider;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.fragments.SessionListFragment;
import com.jcertif.android.fragments.SpeakeListFragment;
import com.jcertif.android.model.Category;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.widget.Toast;

public class DataUpdaterService extends Service {

	private NotificationManager mManager;
	private ResultReceiver mReceiver;

	private static int threadCount = 3;
	private static int currentThreadNo = 0;

	@Override
	public void onCreate() {

		super.onCreate();

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
	public int onStartCommand(Intent intent, int flags, int startId) {

		cleanDataBase();

		loadData(SpeakeListFragment.SPEAKER_LIST_URI);

		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	void loadData(String URI) {

		Intent intent = new Intent(this, RESTService.class);
		intent.setData(Uri.parse(URI));
		Bundle params = new Bundle();
		params.putString(RESTService.KEY_JSON_PLAYLOAD, null);
		intent.putExtra(RESTService.EXTRA_PARAMS, params);
		intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER, getResultReceiver());
		startService(intent);
	}

	private ResultReceiver getResultReceiver() {
		return mReceiver;
	}

	private void onRESTResult(int resultCode, Bundle resultData) {
		String result = null;
		if (resultData != null) {
			result = resultData.getString(RESTService.REST_RESULT);
		} else {
			return;
		}
		if (resultCode == 200 && result != null) {
			result = resultData.getString(RESTService.REST_RESULT);
			String resultType = resultData.getString(RESTService.KEY_URI_SENT);

			if (resultType.equals(SessionListFragment.SESSIONS_LIST_URI)) {
				List<Session> sponsorsLevel = parseSessionJson(result);
				saveSessionToCache(sponsorsLevel);
			}
			if (resultType.equals(SessionListFragment.CATEGORY_LIST_URI)) {
				List<Category> cat = parseCategoryJson(result);
				saveCatToCache(cat);
			}
			if (resultType.equals(SessionListFragment.SESSIONS_LIST_URI)) {
				List<Speaker> speskers = parseSpeakerJson(result);
				saveSpeakerToCache(speskers);
			}
		} else {

			Toast.makeText(this, "Echec de mis à jour des données",
					Toast.LENGTH_LONG).show();
		}
	}

	private List<Session> parseSessionJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Session[] sessions = gson.fromJson(result, Session[].class);

		return Arrays.asList(sessions);
	}

	protected void saveSessionToCache(final List<Session> sessions) {
		Thread th = new Thread(new Runnable() {
			SessionProvider mProvider = new SessionProvider(
					DataUpdaterService.this);

			@Override
			public void run() {
				for (Session session : sessions)
					mProvider.store(session);
			}
		});
		th.start();

		try {

			th.join();

		} catch (InterruptedException e) {
			th.interrupt();
			e.printStackTrace();
		}
		if (++currentThreadNo == threadCount) {
			ShowNotificaton();
		} else {
			// loadData(SessionListFragment.CATEGORY_LIST_URI);
		}
	}

	private void saveCatToCache(final List<Category> cat) {
		Thread th = new Thread(new Runnable() {
			CategorieProvider catProvider = new CategorieProvider(
					DataUpdaterService.this);

			@Override
			public void run() {
				for (Category sl : cat) {
					catProvider.store(sl);
				}
			}
		});
		th.start();

		try {

			th.join();

		} catch (InterruptedException e) {
			th.interrupt();
			e.printStackTrace();
		}
		if (++currentThreadNo == threadCount) {
			ShowNotificaton();
		} else {
			loadData(SessionListFragment.SESSIONS_LIST_URI);
		}
	}

	private List<Category> parseCategoryJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Category[] sl = gson.fromJson(result, Category[].class);
		return Arrays.asList(sl);
	}

	private List<Speaker> parseSpeakerJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Speaker[] speakers = gson.fromJson(result, Speaker[].class);

		return Arrays.asList(speakers);

	}

	private void saveSpeakerToCache(final List<Speaker> result) {
		Thread th = new Thread(new Runnable() {
			SpeakerProvider mProvider = new SpeakerProvider(
					DataUpdaterService.this);

			@Override
			public void run() {
				if (result != null)
					for (Speaker sp : result)
						mProvider.store(sp);
			}
		});
		th.start();

		try {

			th.join();

		} catch (InterruptedException e) {
			th.interrupt();
			e.printStackTrace();
		}
		if (++currentThreadNo == threadCount) {
			ShowNotificaton();
		} else {
			loadData(SessionListFragment.CATEGORY_LIST_URI);
		}
	}

	void cleanDataBase() {
		new SpeakerProvider(this).deleteAll(Speaker.class);
		new SessionProvider(this).deleteAll(Session.class);
		new CategorieProvider(this).deleteAll(Category.class);
	}

	@SuppressWarnings("deprecation")
	void ShowNotificaton() {
		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(this.getApplicationContext(),
				MainActivity.class);

		Notification notification = new Notification(R.drawable.ic_launcher,
				this.getString(R.string.les_donn_es_ont_t_mis_jour),
				System.currentTimeMillis());
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this.getApplicationContext(),
				"JCertif", this.getString(R.string.les_donn_es_ont_t_mis_jour),
				pendingNotificationIntent);

		mManager.notify(0, notification);

	}

}
