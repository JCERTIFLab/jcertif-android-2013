package com.jcertif.android.fragments;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jcertif.android.R;

public class MapEventFragment extends RESTResponderFragment {

	MapView mapv;
	GoogleMap gmap;
	Marker marker;
	LatLng initPosition;
	private static final String geoURL="";
	private static final String LOG_TAG = MapEventFragment.class.getName();
	
	public MapEventFragment(){
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRetainInstance(true);
		View rootView=inflater.inflate(R.layout.fragment_map, container,false);
		
		getActivity().setTitle(R.string.app_name);
		setHasOptionsMenu(true);
		
		mapv=(MapView)rootView.findViewById(R.id.mapview);
		mapv.onCreate(savedInstanceState);
		
		onMapReady();
		
		return rootView;
	}

	
	public void onMapReady() {
		
		initPosition=new LatLng(-4.257399,15.283935);
		
		gmap=mapv.getMap();
		gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		gmap.getUiSettings().setCompassEnabled(true);
		try{
			MapsInitializer.initialize(this.getSherlockActivity());
		}catch(GooglePlayServicesNotAvailableException e){
			e.printStackTrace();
		}
		//Move the camera instantly to initial position with a zoom of 17.
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(initPosition, 17));
		// Zoom in, animating the camera.
		gmap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null); 

		setUpMap();
	
		
	}


	private void setUpMap() {
        // Retrieve data from the web service
        // In a worker thread since it's a network operation.
		try {
            retrieveAndAddPOI();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot retrieve poi", e);
            return;
        }
    }
	
	
	protected void retrieveAndAddPOI() throws IOException{
		HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the url
            /*URL url = new URL(geo_URL);
            conn = (HttpURLConnection) url.openConnection();*/
            /*InputStreamReader in = new InputStreamReader(conn.getInputStream());*/
            InputStreamReader in = new InputStreamReader(getSherlockActivity().getResources().openRawResource(R.raw.cgeventgeodata));
            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to service", e);
            throw new IOException("Error connecting to service", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
 
           try {
	       	 createMarkersFromJson(json.toString());
	       } catch (JSONException e) {
	           Log.e(LOG_TAG, "Error processing JSON", e);
	       }   
	}
	
	void createMarkersFromJson(String json) throws JSONException {
        // De-serialize the JSON string into an array of city objects
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            // Create a marker for each city in the JSON data.
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            gmap.addMarker(new MarkerOptions()
                .title(jsonObj.getString("name"))
                /*.snippet(Integer.toString(jsonObj.getInt("population")))*/
                .position(new LatLng(
                        jsonObj.getJSONArray("latlng").getDouble(0),
                        jsonObj.getJSONArray("latlng").getDouble(1)
                 ))
                 .visible(true)
                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
            );
        }
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapv.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapv.onLowMemory();
	}

	@Override
	public void onResume() {
		mapv.onResume();
		super.onResume();
	}

	@Override
	public void onRESTResult(int code, Bundle result) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
