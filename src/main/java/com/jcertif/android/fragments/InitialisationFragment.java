package com.jcertif.android.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.dao.CategorieProvider;
import com.jcertif.android.dao.ContributorProvider;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.dao.SponsorLevelProvider;
import com.jcertif.android.dao.SponsorProvider;
import com.jcertif.android.model.Category;
import com.jcertif.android.model.Contributor;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.model.Sponsor;
import com.jcertif.android.model.SponsorLevel;
import com.jcertif.android.service.RESTService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Patrick Bashizi (bashizip@gmail.com)
 * 
 */
public class InitialisationFragment extends RESTResponderFragment {

	private ProgressBar pb_init;
	private TextView tv_init;

	private final String SPONSOR_LEVEL_URI = JcertifApplication.BASE_URL
			+ "/ref/sponsorlevel/list";

	private final String CATEGORIES__URI = JcertifApplication.BASE_URL
			+ "/ref/category/list";
	private static final String SPEAKER_LIST_URI = JcertifApplication.BASE_URL
			+ "/speaker/list";


	private RefentielDataLodedListener listener;
	CategorieProvider catProvider;
	SponsorLevelProvider spProvider;
	SpeakerProvider spekerProvider;
    SponsorProvider sponsorProvider;
    SessionProvider sessionProvider;
    ContributorProvider contributorProvider;

	private static int threadCount = 3; // must be equal to urls count
	private static int currentThreadNo = 0; // id of the incoming thread from
											// intentService

	public InitialisationFragment() {
		super();
	}

	public interface RefentielDataLodedListener {
		public void OnRefDataLoaded();
	}

	public void setListener(RefentielDataLodedListener listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_init, container,
				false);
		pb_init = (ProgressBar) rootView.findViewById(R.id.pb_init);
		tv_init = (TextView) rootView.findViewById(R.id.tv_init);
		tv_init.setText(R.string.fetching_initial_data);
		getActivity().setTitle(R.string.app_name);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);

		catProvider = new CategorieProvider(
				InitialisationFragment.this.getSherlockActivity());
		spProvider = new SponsorLevelProvider(
				InitialisationFragment.this.getSherlockActivity());

		spekerProvider = new SpeakerProvider(
				InitialisationFragment.this.getSherlockActivity());
        sponsorProvider = new SponsorProvider(
                InitialisationFragment.this.getSherlockActivity());

        sessionProvider = new SessionProvider(
                InitialisationFragment.this.getSherlockActivity());

        contributorProvider = new ContributorProvider(
                InitialisationFragment.this.getSherlockActivity());


        if(JcertifApplication.ONLINE){
		    loadData(CATEGORIES__URI);
        }else{
            threadCount = 6;
            loadToCache();
        }
	}

	void loadData(String URI) {

		MainActivity activity = (MainActivity) getActivity();
		Intent intent = new Intent(activity, RESTService.class);
		intent.setData(Uri.parse(URI));
		Bundle params = new Bundle();
		params.putString(RESTService.KEY_JSON_PLAYLOAD, null);
		intent.putExtra(RESTService.EXTRA_PARAMS, params);
		intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER, getResultReceiver());
		activity.startService(intent);
	}

    void loadToCache(){
        List<Sponsor> sponsors = parseSponsorJson(loadFromRaw(R.raw.sponsorsdata));
        if (sponsors!=null)
            saveSponsorToCache(sponsors);
        List<Category> categories = parseCategoryJson(loadFromRaw(R.raw.categoriesdata));
        if (categories!=null)
            saveCatToCache(categories);
        List<Speaker> speakers = parseSpeakerJson(loadFromRaw(R.raw.speackersdata));
        if(speakers!=null)
            saveSpeakerToCache(speakers);
        List<Session> sessions = parseSessionJson(loadFromRaw(R.raw.sessionsdata));
        if(sessions!=null)
            saveSessionToCache(sessions);
        List<Contributor> contributors = parseContributorJson(loadFromRaw(R.raw.contributorsdata));
        if(contributors!=null)
            saveContributorToCache(contributors);

    }

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		if (resultData == null) {
			Toast.makeText(this.getActivity(), "Failled to load data, check your connection", Toast.LENGTH_LONG).show();
			return;
		}
		String result = resultData.getString(RESTService.REST_RESULT);
		String resultType = resultData.getString(RESTService.KEY_URI_SENT);

		if (resultType.equals(SPONSOR_LEVEL_URI)) {
			List<SponsorLevel> sponsorsLevel = parseSponsorLevelJson(result);
            if(sponsorsLevel!=null){
			saveSponsorLevelToCache(sponsorsLevel);
            }else{
                return;
            }
		}
		if (resultType.equals(CATEGORIES__URI)) {
			List<Category> cat = parseCategoryJson(result);
			if(cat!=null){
			saveCatToCache(cat);
			}else{
				return;
			}
		}
		if (resultType.equals(SPEAKER_LIST_URI)) {
			List<Speaker> speskers = parseSpeakerJson(result);
			saveSpeakerToCache(speskers);
		}

	}



	private List<Category> parseCategoryJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Category[] sl = gson.fromJson(result, Category[].class);
		return Arrays.asList(sl);
	}

	private List<SponsorLevel> parseSponsorLevelJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		SponsorLevel[] sl = gson.fromJson(result, SponsorLevel[].class);
		return Arrays.asList(sl);
	}

    private List<Sponsor> parseSponsorJson(String result) {
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
                .create();
        Sponsor[] sl = gson.fromJson(result, Sponsor[].class);
        return Arrays.asList(sl);
    }

    private List<Session> parseSessionJson(String result) {
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
                .create();
        Session[] sl = gson.fromJson(result, Session[].class);
        return Arrays.asList(sl);
    }


	private List<Speaker> parseSpeakerJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Speaker[] speakers = gson.fromJson(result, Speaker[].class);

		return Arrays.asList(speakers);

	}


    private List<Contributor> parseContributorJson(String result) {
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
                .create();
        Contributor[] contributors = gson.fromJson(result, Contributor[].class);

        return Arrays.asList(contributors);

    }

    private void saveCatToCache(final List<Category> cat) {
        Thread th = new Thread(new Runnable() {

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
            listener.OnRefDataLoaded();
        } else {
            loadData(SPONSOR_LEVEL_URI);
        }

    }

	protected void saveSponsorLevelToCache(final List<SponsorLevel> sls) {
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				for (SponsorLevel sl : sls)
					spProvider.store(sl);
			}
		});
		th.start();
		try {

			th.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (++currentThreadNo == threadCount) {
			listener.OnRefDataLoaded();
		} else {
			loadData(SPEAKER_LIST_URI);
		}

	}

	private void saveSpeakerToCache(final List<Speaker> result) {
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				if (result != null)
					for (Speaker sp : result)
						spekerProvider.store(sp);
			}
		});
		th.start();
		try {

			th.join();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (++currentThreadNo == threadCount) {
			listener.OnRefDataLoaded();
		}
	}


    private void saveSponsorToCache(final List<Sponsor> result) {
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                if (result != null)
                    for (Sponsor sp : result)
                        sponsorProvider.store(sp);
            }
        });
        th.start();
        try {

            th.join();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (++currentThreadNo == threadCount) {
            listener.OnRefDataLoaded();
        }
    }


    private void saveSessionToCache(final List<Session> result) {
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                if (result != null)
                    for (Session se : result)
                        sessionProvider.store(se);
            }
        });
        th.start();
        try {

            th.join();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (++currentThreadNo == threadCount) {
            listener.OnRefDataLoaded();
        }
    }

    private void saveContributorToCache(final List<Contributor> result) {
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                if (result != null)
                    for (Contributor co : result)
                        contributorProvider.store(co);
            }
        });
        th.start();
        try {

            th.join();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (++currentThreadNo == threadCount) {
            listener.OnRefDataLoaded();
        }
    }

    public String loadFromRaw(int inFile) {
        String tContents = "";

        try {
            InputStream stream = getResources().openRawResource(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}
